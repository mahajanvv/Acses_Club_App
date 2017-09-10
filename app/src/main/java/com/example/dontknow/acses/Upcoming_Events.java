package com.example.dontknow.acses;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Upcoming_Events extends AppCompatActivity {


    private RecyclerView recyclerView;
    private Query databaseReference ;

    private DatabaseReference databaseReferencedelete;


    private FirebaseAuth auth=null;
    private FirebaseAuth.AuthStateListener authStateListener=null;

    private ArrayList<String>arrayList;

    private Bundle bundle;
    private TextView event;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upcoming__events);

        event =(TextView)findViewById(R.id.textView3);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Events").orderByChild("Event_Date");
        databaseReference.keepSynced(true);

        databaseReferencedelete = FirebaseDatabase.getInstance().getReference().child("Events");

        auth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                if(auth.getCurrentUser()==null)
                {
                    Intent intent = new Intent(Upcoming_Events.this,login_Activity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            }
        } ;

        arrayList = new ArrayList<String>();

        bundle =getIntent().getExtras();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);

        recyclerView = (RecyclerView ) findViewById(R.id.idEvent_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


    }

    @Override
    protected void onStart() {
        super.onStart();

        auth.addAuthStateListener(authStateListener);

        FirebaseDatabase.getInstance().getReference().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild("Events"))
                {
                    event.setVisibility(View.GONE);
                }
                else
                {
                    event.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        FirebaseRecyclerAdapter<Event_accept,EventViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Event_accept, EventViewHolder>(
                Event_accept.class,R.layout.upcoming_event_row,EventViewHolder.class,databaseReference
            ) {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                protected void populateViewHolder(EventViewHolder viewHolder, final Event_accept model, final int position) {
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    Date date = null;
                    try {
                        date = sdf.parse(model.getEvent_Date());
                    } catch (ParseException e) {
                        e.printStackTrace();
                        onStart();
                    }
                    long millis = date.getTime();
                        if(millis<(System.currentTimeMillis()-24*60*60*1000))
                        {
                            //arrayList.add(getRef(position).getKey());
                            databaseReferencedelete.child(getRef(position).getKey()).removeValue();

                        }
                        else
                        {
                            viewHolder.setRowTitle(model.getEvent_Topic());
                            viewHolder.setRowDesc(model.getEvent_Description());
                            viewHolder.setRowDetails(model.getEvent_Post_Time().replaceAll("GMT+5:30",""));
                            viewHolder.setRowType(model.getEvent_Type());
                            viewHolder.setRowDate(model.getEvent_Date());
                            viewHolder.setRowTime(model.getEvent_Time());
                            viewHolder.setRowuserName(model.getEvent_userName());

                        }

                    viewHolder.view.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {


                            if(auth.getCurrentUser().getUid().equals(model.getEvent_userUID()))
                            {
                                Intent intent = new Intent(Upcoming_Events.this,EditEventActivity.class);
                                intent.putExtra("post_key",getRef(position).getKey());
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            }
                            else
                            {
                                Toast.makeText(Upcoming_Events.this,"You are Not Authorised To Edit This Event",Toast.LENGTH_LONG).show();
                            }
                            return false;
                        }
                    });
                }
            };
            recyclerView.setAdapter(firebaseRecyclerAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.refershevent,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();

        if(id == R.id.idrefersheve)
        {
            onStart();
        }
        return true;
    }

    public static class EventViewHolder extends RecyclerView.ViewHolder
    {

        View view=null;
        public EventViewHolder(View itemView) {
            super(itemView);

            view= itemView;
        }

        private void setRowDesc(String Desc)
        {
            TextView Description = (TextView) view.findViewById(R.id.idrow_event_description);
            Description.setText(Desc);
        }
        private void setRowTitle(String tit)
        {
            TextView title = (TextView )view.findViewById(R.id.idrow_event_title);
            title.setText(tit);
        }
        private void setRowType(String tit)
        {
            TextView title1 = (TextView )view.findViewById(R.id.idrow_event_type);
            title1.setText(tit);
        }
        private  void setRowDate(String tit)
        {
            TextView title2 = (TextView )view.findViewById(R.id.idrow_event_date);
            title2.setText(tit);
        }
        private void setRowTime(String tit)
        {
            TextView title3 = (TextView )view.findViewById(R.id.idrow_event_time);
            title3.setText(tit);
        }
        private void setRowDetails(String tit)
        {
            TextView title4 = (TextView )view.findViewById(R.id.idrow_event_publish_details);
            title4.setText(tit);
        }
        private void setRowuserName(String tit)
        {
            TextView title5 = (TextView )view.findViewById(R.id.idrow_event_userName);
            title5.setText(tit);
        }

    }
}
