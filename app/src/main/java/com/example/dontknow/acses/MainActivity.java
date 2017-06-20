package com.example.dontknow.acses;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends Fragment {

    private RecyclerView recyclerView=null;

    private DatabaseReference databaseReference=null;

    private FirebaseAuth auth=null;
    private FirebaseAuth.AuthStateListener authStateListener=null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_main, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Blogs");
        databaseReference.keepSynced(true);

        auth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                if(auth.getCurrentUser()==null)
                {
                    Intent intent = new Intent(getActivity().getApplicationContext(),login_Activity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            }
        } ;

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);

        recyclerView = (RecyclerView ) recyclerView.findViewById(R.id.idblog_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
    }

    @Override
    /*public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        databaseReference = FirebaseDatabase.getInstance().getReference().child("Blogs");
        databaseReference.keepSynced(true);

        auth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                if(auth.getCurrentUser()==null)
                {
                    Intent intent = new Intent(MainActivity.this,login_Activity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            }
        } ;

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);

        recyclerView = (RecyclerView ) findViewById(R.id.idblog_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }*/ public void onStart() {
        super.onStart();

        auth.addAuthStateListener(authStateListener);

        FirebaseRecyclerAdapter<Event_accept,Upcoming_Events.EventViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Event_accept, Upcoming_Events.EventViewHolder>(
                Event_accept.class,R.layout.upcoming_event_row,Upcoming_Events.EventViewHolder.class,databaseReference
        ) {
            @Override
            protected void populateViewHolder(Upcoming_Events.EventViewHolder viewHolder, Event_accept model, int position) {


               /*viewHolder.setTitle(model.getTitle());*/
             /*  viewHolder.setDesc(model.getDescription());
                viewHolder.setImage(getActivity().getApplicationContext(),model.getImage());*/
            }
        };

        recyclerView.setAdapter(firebaseRecyclerAdapter);

    }

    public static class BlogViewHolder extends RecyclerView.ViewHolder
    {

        View view=null;
        public BlogViewHolder(View itemView) {
            super(itemView);

            view= itemView;
        }

        public void setDesc(String Desc)
        {
            TextView Description = (TextView) view.findViewById(R.id.idpost_description);
            Description.setText(Desc);
        }
        public void setTitle(String tit)
        {
            TextView title = (TextView )view.findViewById(R.id.idpost_title);
            title.setText(tit);
        }
        public void setImage(Context ctx, String image)
        {
            //ImageView imageView= (ImageView) view.findViewById(R.id.idImageView);
            //Picasso.with(ctx).load(image).into(imageView);
           // Glide.with(ctx).load(image).dontAnimate().into(imageView);
        }
    }


/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()== R.id.idadd)
        {
            startActivity(new Intent(MainActivity.this,PostActivity.class));
        }
        if(item.getItemId()==R.id.idlogout)
        {
            logout();
        }
        if(item.getItemId()==R.id.idprofile)
        {
            startActivity(new Intent(MainActivity.this,memberprofile.class));
        }

        return super.onOptionsItemSelected(item);
    }

    private void logout() {

        auth.signOut();
    }*/
}
