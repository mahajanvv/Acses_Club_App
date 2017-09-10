package com.example.dontknow.acses;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class EditEventActivity extends AppCompatActivity {

    private EditText edittype;
    private EditText edittopic;
    private EditText editDescription;
    private EditText editdate;
    private EditText edittime;

    private String post_key;

    private Button editconfirm;
    private Button editback;

    private DatabaseReference databaseReference;

    private CalendarView calendarView;

    private Calendar myCalendar;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event);

        edittype = (EditText)findViewById(R.id.idediteventtype);
        edittopic = (EditText)findViewById(R.id.idediteventtopic);
        editDescription = (EditText)findViewById(R.id.idediteventDescription);
        editdate = (EditText)findViewById(R.id.idediteventdate);
        edittime = (EditText)findViewById(R.id.idediteventtime);

        editconfirm  = (Button)findViewById(R.id.idediteventpublish);
        editback = (Button)findViewById(R.id.idediteventback);

        progressDialog = new ProgressDialog(this);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Events");
        post_key  = getIntent().getExtras().getString("post_key");

        UpdateValues();

        editconfirm.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                startpublishing();
            }
        });

        editback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditEventActivity.this,Navigation.class);
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
        editdate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(EditEventActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        edittime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(EditEventActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        edittime.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });

        edittype.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCreateDialog(savedInstanceState);
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void startpublishing() {
        progressDialog.setMessage("Updating Event !!!!");
        progressDialog.setCancelable(false);
        progressDialog.show();
        String type = edittype.getText().toString().trim();
        String topic = edittopic.getText().toString().trim();
        String desc = editDescription.getText().toString().trim();
        String date = editdate.getText().toString().trim();
        String time = edittime.getText().toString().trim();

        if (!TextUtils.isEmpty(type) && !TextUtils.isEmpty(topic) && !TextUtils.isEmpty(desc) && !TextUtils.isEmpty(date) && !TextUtils.isEmpty(time)) {
            databaseReference.child(post_key).child("Event_Type").setValue(type);
            databaseReference.child(post_key).child("Event_Topic").setValue(topic);
            databaseReference.child(post_key).child("Event_Description").setValue(desc);
            databaseReference.child(post_key).child("Event_Date").setValue(date);
            databaseReference.child(post_key).child("Event_Time").setValue(time);
            /*SimpleDateFormat sfd = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            sfd.format(ServerValue.TIMESTAMP);
            System.out.print(ServerValue.TIMESTAMP.toString());
            System.out.print(sfd.toString().trim());*/
            databaseReference.child(post_key).child("Event_Post_Time").setValue(new Date().toString());
            // databaseReferenceevent.child("Event_PosterName").child();
            progressDialog.dismiss();
            Intent intent = new Intent(EditEventActivity.this, Navigation.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            Toast.makeText(EditEventActivity.this, "Event Has been Updated", Toast.LENGTH_LONG).show();
            startActivity(intent);

        }
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
                        edittype.setText("Club Service");
                        break;
                    case 1:
                        // Your code when 2nd  option seletced
                        edittype.setText("Coding Competition");
                        break;
                    case 2:
                        // Your code when 3rd option seletced
                        edittype.setText("Meeting");
                        break;
                    case 3:
                        // Your code when 4th  option seletced
                        edittype.setText("Guest Lecture");
                        break;
                    case 4:
                        edittype.setText("Techumen Event");
                        break;
                    case 5:
                        edittype.setText("SITAC");
                        break;
                    case 6:
                        edittype.setText("Other Event");
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

        editdate.setText(sdf.format(myCalendar.getTime()));
    }

    private void UpdateValues()
    {

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.hasChild(post_key))
                {
                    edittype.setText(dataSnapshot.child(post_key).child("Event_Type").getValue().toString());
                    edittopic.setText(dataSnapshot.child(post_key).child("Event_Topic").getValue().toString());
                    editDescription.setText(dataSnapshot.child(post_key).child("Event_Description").getValue().toString());
                    editdate.setText(dataSnapshot.child(post_key).child("Event_Date").getValue().toString());
                    edittime.setText(dataSnapshot.child(post_key).child("Event_Time").getValue().toString());
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


        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemid = item.getItemId();

        if(itemid  == R.id.ideventdone)
        {
            startpublishing();
        }
        if(itemid == R.id.ideventdelete)
        {
            databaseReference.child(post_key).removeValue();
//            FirebaseDatabase.getInstance().getReference().child("EventsCount").setValue(""+(Integer.parseInt(getIntent().getExtras().getString("eventcnt"))-1));
            Intent intent = new Intent(EditEventActivity.this, Navigation.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            Toast.makeText(EditEventActivity.this,"Event has Been deleted!!!!!",Toast.LENGTH_LONG).show();
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
