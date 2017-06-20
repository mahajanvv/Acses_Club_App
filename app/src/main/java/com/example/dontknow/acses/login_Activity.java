package com.example.dontknow.acses;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class login_Activity extends AppCompatActivity {

    private EditText email;
    private EditText password;
    private Button login;

    private FirebaseAuth auth;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_);

        email = (EditText) findViewById(R.id.idemail);
        password  = (EditText )findViewById(R.id.idpassword);
        login = (Button )findViewById(R.id.idsignin);

        auth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Signin();
            }
        });
    }
    private void Signin()
    {
        progressDialog.setMessage("Signing In!!!!!!!!");
        progressDialog.show();
        String em  = email.getText().toString().trim();
        String pin = password.getText().toString().trim();

        //Toast.makeText(login_Activity.this,em,Toast.LENGTH_LONG).show();
        //Toast.makeText(login_Activity.this,pin,Toast.LENGTH_LONG).show();
        if(!TextUtils.isEmpty(em ) && !TextUtils.isEmpty(pin))
        {
            auth.signInWithEmailAndPassword(em,pin).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {
                        progressDialog.dismiss();
                        Toast.makeText(login_Activity.this,"successfully Signed In!!!!!!!!!!",Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(login_Activity.this,Navigation.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);

                    }
                    else
                    {
                        progressDialog.dismiss();
                        Toast.makeText(login_Activity.this,"Sign in Failed!!!!!!!!!!",Toast.LENGTH_LONG).show();
                    }
                }
            });
        }




    }
}
