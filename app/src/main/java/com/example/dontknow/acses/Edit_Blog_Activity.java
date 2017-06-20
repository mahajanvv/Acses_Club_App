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
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.Random;

public class Edit_Blog_Activity extends AppCompatActivity {

    private String Post_key;

    private EditText editposttittle;
    private EditText editpostdescrpt;
    private ImageButton editpostimagebutton;
    private Button editpostconfirm;

    public String imgDecodableString123;
    private Uri imageURI=null;
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1234;

    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private StorageReference photoRef;

    private static final int galleryrequest = 1;

    private ProgressDialog progressDialog;

    private String deleteimage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit__blog_);

        Post_key = getIntent().getExtras().getString("Post_Key");

        editposttittle = (EditText)findViewById(R.id.ideditposttitle);
        editpostdescrpt= (EditText) findViewById(R.id.ideditpostdescription);
        editpostimagebutton = (ImageButton) findViewById(R.id.ideditpostimage);
        editpostconfirm = (Button) findViewById(R.id.ideditpostbtn);

        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Blogs");

        progressDialog  = new ProgressDialog(this);

        Updatevalue();

//        photoRef = FirebaseStorage.getInstance().getReferenceFromUrl(String.valueOf(deleteimage));
        editpostimagebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent,galleryrequest);
            }
        });

        editpostconfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                starteditpost();
            }
        });

    }

    private  void starteditpost()
    {
        progressDialog.setMessage("Posting Edited Blog!!!!");
        progressDialog.show();
        final String tit=editposttittle.getText().toString().trim();
        final String titdis=editpostdescrpt.getText().toString().trim();



        if(!TextUtils.isEmpty(tit) && !TextUtils.isEmpty(titdis) && imageURI!= null)
        {
            StorageReference filepath = storageReference.child("Blogs_Images").child(random()+random()+"123");

            filepath.putFile(imageURI).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(Edit_Blog_Activity.this,"Posting Process Is Failed!!!",Toast.LENGTH_LONG).show();
                }
            });

            filepath.putFile(imageURI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Uri downloadUrl = taskSnapshot.getDownloadUrl();

                    databaseReference.child(Post_key).child("title").setValue(tit);
                    databaseReference.child(Post_key).child("description").setValue(titdis);
                    databaseReference.child(Post_key).child("image").setValue(downloadUrl.toString().trim());
                    databaseReference.child(Post_key).child("postDate").setValue(new Date().toString().trim());
                    //photoRef.delete();
                    FirebaseStorage.getInstance().getReferenceFromUrl(deleteimage).delete();
                    progressDialog.dismiss();
                    Toast.makeText(Edit_Blog_Activity.this,"Edited Blog Has Been Posted!!!!!!!!!",Toast.LENGTH_LONG).show();

                    Intent intent =new Intent(Edit_Blog_Activity.this,Navigation.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            });

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

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (checkPermissionREAD_EXTERNAL_STORAGE(this)) {
            // do your stuff..
            try {
                if(requestCode == galleryrequest && resultCode == RESULT_OK && null !=data)
                {
                    imageURI = data.getData();

                    editpostimagebutton.setImageURI(imageURI);
                    /*String[] filePathColumn = { MediaStore.Images.Media.DATA };

                    // Get the cursor
                    Cursor cursor = getContentResolver().query(imageURI,
                            filePathColumn, null, null, null);

                    // Move to first row
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);


                    imgDecodableString123 = cursor.getString(columnIndex);

                    cursor.close();

                    ImageButton imageButton = (ImageButton) findViewById(R.id.ideditpostimage);
                    // Set the Image in ImageView after decoding the String
                    //imageButton.setImageBitmap(BitmapFactory
                    //      .decodeFile(imgDecodableString));

                    Drawable d = new BitmapDrawable(getResources(), BitmapFactory.decodeFile(imgDecodableString123));

                    imageButton.setForeground(d);*/

                }

            }
            catch(Exception e){
                Toast.makeText(Edit_Blog_Activity.this,"something went wrong!!!!!!!!!!"+e,Toast.LENGTH_LONG).show();

            }

        }


    }

    private void Updatevalue()
    {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.hasChild(Post_key))
                {
                    editposttittle.setText(dataSnapshot.child(Post_key).child("title").getValue().toString());
                    editpostdescrpt.setText(dataSnapshot.child(Post_key).child("description").getValue().toString());
                    Picasso.with(getApplicationContext()).load(dataSnapshot.child(Post_key).child("image").getValue().toString()).into(editpostimagebutton);
                    deleteimage  = dataSnapshot.child(Post_key).child("image").getValue().toString();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);

        return super.onCreateOptionsMenu(menu);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int itemid = item.getItemId();

        if(itemid == R.id.ideventdone)
        {
            starteditpost();
        }
        if(itemid == R.id.ideventdelete)
        {
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.hasChild(Post_key))
                    {
                        FirebaseStorage.getInstance().getReferenceFromUrl(dataSnapshot.child(Post_key).child("image").getValue().toString()).delete();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            databaseReference.child(Post_key).removeValue();
            Toast.makeText(Edit_Blog_Activity.this,"Blog has been Deleted!!!!",Toast.LENGTH_LONG).show();
            Intent intent = new Intent(Edit_Blog_Activity.this,Navigation.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
