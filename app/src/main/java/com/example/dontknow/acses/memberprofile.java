package com.example.dontknow.acses;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class memberprofile extends AppCompatActivity {

    private EditText mfname;
    private EditText mlname;
    private EditText memail;
    private EditText mphone;
    private EditText mpost;

    private Button sumbitbtn;
    private Button cancelbtn;

    private FirebaseAuth auth;
    private DatabaseReference databaseReference;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memberprofile);

        mfname  = (EditText)findViewById(R.id.idfname);
        mlname  =  (EditText)findViewById(R.id.idlname);
        memail = (EditText)findViewById(R.id.idmemberemail);
        mphone = (EditText)findViewById(R.id.idphone);
        mpost = (EditText)findViewById(R.id.idmemberpost);

        sumbitbtn  = (Button)findViewById(R.id.idsubmit);
        cancelbtn  = (Button)findViewById(R.id.idcancel);

        progressDialog  = new ProgressDialog(this);

        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");

        checkUserExists();

        sumbitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buildProfile();
            }
        });

        cancelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(memberprofile.this,Navigation.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    private void checkUserExists() {

        final String uid = auth.getCurrentUser().getUid();

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.hasChild(auth.getCurrentUser().getUid()))
                {
                    mfname.setText(dataSnapshot.child(uid).child("First_Name").getValue().toString());
                    mlname.setText(dataSnapshot.child(uid).child("Last_Name").getValue().toString());
                    memail.setText(dataSnapshot.child(uid).child("Email_Address").getValue().toString());
                    mphone.setText(dataSnapshot.child(uid).child("Phone_Number").getValue().toString());
                    mpost.setText(dataSnapshot.child(uid).child("Post_Of_User").getValue().toString());

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void buildProfile() {
        progressDialog.setMessage("submitting");
        progressDialog.show();
       final String fname = mfname.getText().toString().trim();
        final String lname  = mlname.getText().toString().trim();
        final String email =  memail.getText().toString().trim();
        final String phone = mphone.getText().toString().trim();
        final  String post = mpost.getText().toString().trim();

        if(!TextUtils.isEmpty(fname) && !TextUtils.isEmpty(lname) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(phone) && !TextUtils.isEmpty(post))
        {
            final String uid= auth.getCurrentUser().getUid();

            if(auth.getCurrentUser().getUid()==null)
            {
                progressDialog.dismiss();
                Toast.makeText(memberprofile.this,"Users doesnot exists!!!!!!!",Toast.LENGTH_LONG).show();
                Intent intent=new Intent(memberprofile.this,Navigation.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(!dataSnapshot.hasChild(uid))
                    {
                        DatabaseReference databaseuser= databaseReference.child(uid);
                        databaseuser.child("First_Name").setValue(fname);
                        databaseuser.child("Last_Name").setValue(lname);
                        databaseuser.child("Email_Address").setValue(email);
                        databaseuser.child("Phone_Number").setValue(phone);
                        databaseuser.child("Post_Of_User").setValue(post);
                        progressDialog.dismiss();
                        Toast.makeText(memberprofile.this,"Successfully submitted",Toast.LENGTH_LONG).show();
                        Intent intent=new Intent(memberprofile.this,Navigation.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                    else
                    {
                        DatabaseReference databaseuser= databaseReference.child(uid);
                        try
                        {
                            databaseuser.child("First_Name").setValue(fname);
                            databaseuser.child("Last_Name").setValue(lname);
                            databaseuser.child("Email_Address").setValue(email);
                            databaseuser.child("Phone_Number").setValue(phone);
                            databaseuser.child("Post_Of_User").setValue(post);
                            progressDialog.dismiss();
                            Toast.makeText(memberprofile.this,"Successfully Updated",Toast.LENGTH_LONG).show();
                            Intent intent=new Intent(memberprofile.this,Navigation.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                        catch (Exception e){e.printStackTrace();}
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

    }
}
