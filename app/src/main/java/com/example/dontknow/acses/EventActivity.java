package com.example.dontknow.acses;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class EventActivity extends Activity {

    private EditText eventtype;
    private EditText eventtopic;
    private EditText eventDescription;
    private EditText eventdate;
    private EditText eventtime;

    private Button eventpublish;
    private Button eventback;

    private DatabaseReference databaseReference;
    private DatabaseReference databaseReferenceusers;

    private ProgressDialog progressDialog;

    private CalendarView calendarView;

    private Calendar myCalendar;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    private RadioGroup radioGroup;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        progressDialog = new ProgressDialog(this);

        eventback = (Button) findViewById(R.id.ideventback);
        eventpublish = (Button) findViewById(R.id.ideventpublish);

        eventtype = (EditText) findViewById(R.id.ideventtype);
        eventtopic = (EditText) findViewById(R.id.ideventtopic);
        eventDescription = (EditText) findViewById(R.id.ideventDescription);
        eventdate = (EditText) findViewById(R.id.ideventdate);
        eventtime = (EditText) findViewById(R.id.ideventtime);

        //calendarView = (CalendarView)findViewById(R.id.idcalendarView);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Events");
        databaseReference.keepSynced(true);

        databaseReferenceusers = FirebaseDatabase.getInstance().getReference().child("Users");


        eventback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EventActivity.this, Navigation.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        myCalendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };
        eventdate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(EventActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        eventtime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(EventActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        eventtime.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });

        eventpublish.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                startpublishing();
            }
        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        eventtype.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCreateDialog(savedInstanceState);
            }
        });
    }
    public void onCreateDialog(Bundle savedInstanceState) {

        final CharSequence[] items = {" Club Service "," Coding Competition "," Meeting "," Guest Lecture "," Techumen Event "," SITAC "," Other Event "};

        // Creating and Building the Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Event Type");
        builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {


                switch(item)
                {
                    case 0:
                        // Your code when first option seletced
                        eventtype.setText("Club Service");
                        break;
                    case 1:
                        // Your code when 2nd  option seletced
                        eventtype.setText("Coding Competition");
                        break;
                    case 2:
                        // Your code when 3rd option seletced
                        eventtype.setText("Meeting");
                        break;
                    case 3:
                        // Your code when 4th  option seletced
                        eventtype.setText("Guest Lecture");
                        break;
                    case 4:
                        eventtype.setText("Techumen Event");
                        break;
                    case 5:
                        eventtype.setText("SITAC");
                        break;
                    case 6:
                        eventtype.setText("Other Event");
                        break;

                }

                dialog.dismiss();
            }
        });
        Dialog dialog = builder.create();
        dialog.show();


    }
    private void updateLabel() {

        String myFormat = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        eventdate.setText(sdf.format(myCalendar.getTime()));
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void startpublishing() {
        progressDialog.setMessage("Publishing Event !!!!");
        progressDialog.show();
        String type = eventtype.getText().toString().trim();
        String topic = eventtopic.getText().toString().trim();
        String desc = eventDescription.getText().toString().trim();
        String date = eventdate.getText().toString().trim();
        String time = eventtime.getText().toString().trim();

        if (!TextUtils.isEmpty(type) && !TextUtils.isEmpty(topic) && !TextUtils.isEmpty(desc) && !TextUtils.isEmpty(date) && !TextUtils.isEmpty(time)) {
            final DatabaseReference databaseReferenceevent = databaseReference.push();
            databaseReferenceevent.child("Event_Type").setValue(type);
            databaseReferenceevent.child("Event_Topic").setValue(topic);
            databaseReferenceevent.child("Event_Description").setValue(desc);
            databaseReferenceevent.child("Event_Date").setValue(date);
            databaseReferenceevent.child("Event_Time").setValue(time);
            /*SimpleDateFormat sfd = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            sfd.format(ServerValue.TIMESTAMP);
            System.out.print(ServerValue.TIMESTAMP.toString());
            System.out.print(sfd.toString().trim());*/
            databaseReferenceevent.child("Event_Post_Time").setValue(new Date().toString());
            DatabaseReference data = FirebaseDatabase.getInstance().getReference().child("Users");
            data.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChild(FirebaseAuth.getInstance().getCurrentUser().getUid().toString())) {
                        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();
                        String name = "" + dataSnapshot.child(uid).child("First_Name").getValue().toString() + " " + dataSnapshot.child(uid).child("Last_Name").getValue().toString() + " " + dataSnapshot.child(uid).child("Post_Of_User").getValue().toString();
                        databaseReferenceevent.child("Event_userName").setValue(name);
                        databaseReferenceevent.child("Event_userUID").setValue(uid);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            // databaseReferenceevent.child("Event_PosterName").child();
            progressDialog.dismiss();
            Intent intent = new Intent(EventActivity.this, Navigation.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            Toast.makeText(EventActivity.this, "Event Has been published", Toast.LENGTH_LONG).show();
            startActivity(intent);

        }
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Event Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }


    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}
