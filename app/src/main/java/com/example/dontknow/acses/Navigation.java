package com.example.dontknow.acses;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
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
import com.squareup.picasso.Picasso;

public class Navigation extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private RecyclerView recyclerView=null;

    private Query databaseReference=null;
    private DatabaseReference databaseReferencedelete;

    private FirebaseAuth auth=null;
    private FirebaseAuth.AuthStateListener authStateListener=null;

    private ProgressDialog progressDialog;
    private DatabaseReference databaseReferenceusers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        progressDialog = new ProgressDialog(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.dismiss();
                Intent intent =new Intent(Navigation.this,PostActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        auth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                if(auth.getCurrentUser()==null)
                {
                    progressDialog.dismiss();
                    Intent intent = new Intent(Navigation.this,login_Activity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            }
        } ;

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Blogs").orderByChild("postDate");
        databaseReference.keepSynced(true);
        databaseReferencedelete = FirebaseDatabase.getInstance().getReference().child("Blogs");
        databaseReferenceusers = FirebaseDatabase.getInstance().getReference().child("Users");

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);

        recyclerView = (RecyclerView ) findViewById(R.id.idblog_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    public void onStart() {
        super.onStart();

        auth.addAuthStateListener(authStateListener);

        FirebaseRecyclerAdapter<Blog_accept,BlogViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Blog_accept,BlogViewHolder>(
                Blog_accept.class,R.layout.blog_row,BlogViewHolder.class,databaseReference
        ) {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            protected void populateViewHolder(final BlogViewHolder viewHolder, final Blog_accept model, final int position) {

                /*if(new SimpleDateFormat("dd/mm/yyyy").format(model.getPostDate()).compareTo(new SimpleDateFormat("dd/mm/yyyy").format(new Date(System.currentTimeMillis())))<0)
                {
                    //arrayList.add(getRef(position).getKey());
                    Toast.makeText(Navigation.this,"delte",Toast.LENGTH_LONG).show();
                    //databaseReferencedelete.child(getRef(position).getKey()).removeValue();
                }
                else
                {*/
                    viewHolder.setTitle(model.getTitle());
                    viewHolder.setDesc(model.getDescription());
                    viewHolder.setImage(getApplicationContext(),model.getImage());
                    viewHolder.setpostdate(model.getPostDate());
                    viewHolder.setpostUserName(model.getUserName());
                //}


                viewHolder.view.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {

                       // Toast.makeText(Navigation.this,model.getUserUID()+"="+FirebaseAuth.getInstance().getCurrentUser().getUid(),Toast.LENGTH_LONG).show();
                        if(model.getUserUID().equals(FirebaseAuth.getInstance().getCurrentUser().getUid()))
                        {
                            Intent intent = new Intent(Navigation.this,Edit_Blog_Activity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.putExtra("Post_Key",getRef(position).getKey());
                            //Toast.makeText(Navigation.this,"Long Pressed",Toast.LENGTH_LONG).show();
                            startActivity(intent);
                        }
                        else
                        {
                            Toast.makeText(Navigation.this,"Your are not Authorised To Edit This Blog",Toast.LENGTH_LONG).show();
                        }
                        return false;
                    }
                });
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
        public void setpostUserName(String usr)
        {
            TextView Description = (TextView) view.findViewById(R.id.idpost_userName);
            Description.setText(usr);
        }
        public void setpostdate(String dt)
        {
            TextView Description = (TextView) view.findViewById(R.id.idpost_Date);
            Description.setText(dt);
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
            ImageView post123= (ImageView) view.findViewById(R.id.idpost_Image);
            //System.out.print(image);
            Picasso.with(ctx).load(image).into(post123);
            // Glide.with(ctx).load(image).dontAnimate().into(imageView);
        }
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.idlogout) {
            Intent intent = new Intent(Navigation.this,login_Activity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            auth.signOut();
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")


    public void displaySelectedScreen(int id)
    {

        if(id==R.id.idprofileActivity)
        {
            Intent intent = new Intent(Navigation.this,memberprofile.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        else if(id==R.id.idBlogsActivity)
        {

        }
        else if(id==R.id.idPostBlogsActivity)
        {
            databaseReferenceusers.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(!dataSnapshot.hasChild(auth.getCurrentUser().getUid()))
                    {
                        Toast.makeText(Navigation.this,"Build Your Profile First!!!",Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(Navigation.this,memberprofile.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                    else
                    {
                        Intent intent =new Intent(Navigation.this,PostActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        else if(id == R.id.idCreateEventActivity)
        {
            databaseReferenceusers.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(!dataSnapshot.hasChild(auth.getCurrentUser().getUid()))
                    {
                        Toast.makeText(Navigation.this,"Build Your Profile First!!!",Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(Navigation.this,memberprofile.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                    else
                    {
                        Intent intent =new Intent(Navigation.this,EventActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        else if(id==R.id.idAboutActivity)
        {
            Intent intent =new Intent(Navigation.this,AboutActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        else if(id==R.id.idUpcomingEventActivity)
        {
            Intent intent =new Intent(Navigation.this,Upcoming_Events.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        displaySelectedScreen(item.getItemId());
        return true;
    }
}
