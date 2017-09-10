package com.example.dontknow.acses;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Date;
import java.util.Random;

public class PostActivity extends AppCompatActivity {

    private ImageButton imageButton;

    public String imgDecodableString;

    private Uri imageURI=null;
    private EditText post_title;
    private EditText post_Description;
    private Button submit;

    private static final int galleryrequest = 1;

    private ProgressDialog progressDialog;

    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;

    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private DatabaseReference databaseReferenceBlogsCount;

    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        progressDialog = new ProgressDialog(this);

        //flg=true;

        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference= FirebaseDatabase.getInstance().getReference().child("Blogs");
        databaseReferenceBlogsCount= FirebaseDatabase.getInstance().getReference().child("BlogsCount");

        imageButton = (ImageButton )findViewById(R.id.idimage);

        post_title = (EditText )findViewById(R.id.idposttitle);

        post_Description =(EditText )findViewById(R.id.idpostdescription);

        submit = (Button )findViewById(R.id.idpost);

        bundle = getIntent().getExtras();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog.setMessage("Posting!!!!!!!!!!!!");
                //progressDialog.show();

                    starttposting();

            }
        });



        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                //galleryIntent.setType("Image/*");
                startActivityForResult(galleryIntent,galleryrequest);
            }
        });
    }

    private void starttposting()
    {

        progressDialog.setCancelable(false);
        progressDialog.show();

        final String tit=post_title.getText().toString().trim();
        final String titdis=post_Description.getText().toString().trim();



        if(!TextUtils.isEmpty(tit) && !TextUtils.isEmpty(titdis) && imageURI!= null)
        {
            StorageReference filepath = storageReference.child("Blogs_Images").child(random()+random()+"123");

            filepath.putFile(imageURI).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(PostActivity.this,"Uploading Of Image Is Failed!!!",Toast.LENGTH_LONG).show();
                    Toast.makeText(PostActivity.this,"Retry Again!!!",Toast.LENGTH_LONG).show();
                }
            });

            filepath.putFile(imageURI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Uri downloadUrl = taskSnapshot.getDownloadUrl();

                    try {
                        final DatabaseReference newPost= databaseReference.push();
                        if(newPost!=null)
                        {
                            newPost.child("title").setValue(tit);
                            newPost.child("description").setValue(titdis);
                            newPost.child("image").setValue(downloadUrl.toString());
                            newPost.child("postDate").setValue(new Date().toString().replace("GMT+05:30",""));
                            newPost.child("TimeStamp").setValue(System.currentTimeMillis());
                            newPost.child("userName").setValue(bundle.getString("UserName"));
                            newPost.child("userUID").setValue(bundle.getString("UserUid"));
                        }
                        else
                        {
                            onSuccess(taskSnapshot);
                        }
                    }catch (Exception e){Toast.makeText(getApplicationContext(),"Uploading failed",Toast.LENGTH_LONG).show();
                    onSuccess(taskSnapshot);}
                    progressDialog.dismiss();
                    Toast.makeText(PostActivity.this,"Blog Has Been Posted!!!!!!!!!",Toast.LENGTH_LONG).show();
                    Intent intent =new Intent(PostActivity.this,Navigation.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            });

        }
        else
        {
            if(TextUtils.isEmpty(tit))
            {
                progressDialog.dismiss();
                Toast.makeText(PostActivity.this,"Please, Fill Out Title For The Blog!!!",Toast.LENGTH_LONG).show();
            }
            if(TextUtils.isEmpty(titdis))
            {
                progressDialog.dismiss();
                Toast.makeText(PostActivity.this,"Please , Fill Out Description For The Blog!!!",Toast.LENGTH_LONG).show();
            }
            if(imageURI== null)
            {
                progressDialog.dismiss();
                Toast.makeText(PostActivity.this,"Please , Select a Picture For The Blog!!!",Toast.LENGTH_LONG).show();
            }
        }

    }
    public String random() {
        Random generator = new Random();
        StringBuilder randomStringBuilder = new StringBuilder();
        int randomLength = generator.nextInt(12);
        char tempChar;
        for (int i = 0; i < randomLength; i++){
            tempChar = (char) (generator.nextInt(96) + 32);
            randomStringBuilder.append(tempChar);
        }
        return randomStringBuilder.toString();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        /*if (checkPermissionREAD_EXTERNAL_STORAGE(this)) {
            // do your stuff..
        }*/
        if (checkPermissionREAD_EXTERNAL_STORAGE(this)) {
            // do your stuff..
            try {
                if(requestCode == galleryrequest && resultCode == RESULT_OK && null !=data)
                {
                    imageURI = data.getData();

                    imageButton.setImageURI(imageURI);

                   /* String[] filePathColumn = { MediaStore.Images.Media.DATA };

                    // Get the cursor
                    Cursor cursor = getContentResolver().query(imageURI,
                            filePathColumn, null, null, null);

                    // Move to first row
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);


                    imgDecodableString = cursor.getString(columnIndex);

                    cursor.close();

                    ImageButton imageButton = (ImageButton) findViewById(R.id.idimage);
                    // Set the Image in ImageView after decoding the String
                    //imageButton.setImageBitmap(BitmapFactory
                    //      .decodeFile(imgDecodableString));

                    Drawable d = new BitmapDrawable(getResources(), BitmapFactory.decodeFile(imgDecodableString));

                    imageButton.setBackground(d);
                        */
                }

            }
            catch(Exception e){
                Toast.makeText(PostActivity.this,"something went wrong!!!!!!!!!!"+e,Toast.LENGTH_LONG).show();

            }

        }

    }

    public boolean checkPermissionREAD_EXTERNAL_STORAGE(
            final Context context) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        (Activity) context,
                        android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    showDialog("External storage", context,
                            android.Manifest.permission.READ_EXTERNAL_STORAGE);

                } else {
                    ActivityCompat
                            .requestPermissions(
                                    (Activity) context,
                                    new String[] { android.Manifest.permission.READ_EXTERNAL_STORAGE },
                                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
                return false;
            } else {
                return true;
            }

        } else {
            return true;
        }
    }

    public void showDialog(final String msg, final Context context,
                           final String permission) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
        alertBuilder.setCancelable(true);
        alertBuilder.setTitle("Permission necessary");
        alertBuilder.setMessage(msg + " permission is necessary");
        alertBuilder.setPositiveButton(android.R.string.yes,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions((Activity) context,
                                new String[] { permission },
                                MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                    }
                });
        AlertDialog alert = alertBuilder.create();
        alert.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // do your stuff
                } else {
                    //Toast.makeText(Login.this, "GET_ACCOUNTS Denied",
                    // Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions,
                        grantResults);
        }
    }

}
