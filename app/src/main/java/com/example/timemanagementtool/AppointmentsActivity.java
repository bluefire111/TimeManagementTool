package com.example.timemanagementtool;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amplifyframework.core.Amplify;

import com.amplifyframework.core.model.query.Where;
import com.amplifyframework.datastore.generated.model.Appointments;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class AppointmentsActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{
    private ArrayList<Appointment> appList;
    private RecyclerView recyclerView;
    private String currentUserId;
    private DateFormat timeFormater,dateformater;
    private String title,desc,date,time;
    private Button btn_add,btn_back,btn_upload;
    private EditText edtTitle,edtDesc,edtDate,edtTime;
    private TextView tvDateTime,tvTitle,tvDesc;
    private RecyclerAdapter adapter;
    private RecyclerAdapter.RecyclerViewClickListener listener;
    String datetime = "";
    private int iUserDelete;
    private int handler_time = 1000; //1 seconds in milliseconds
    private Handler handler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointments);
        getSupportActionBar().hide();
        currentUserId = getIntent().getSerializableExtra("userid").toString();
        timeFormater = new SimpleDateFormat("HH:mm");
        dateformater = new SimpleDateFormat("YYYY-MM-dd");
        btn_add = findViewById(R.id.btn_add);
        btn_back = findViewById(R.id.btn_back);
        btn_upload = findViewById(R.id.btn_upload_appointment);
        recyclerView = findViewById(R.id.recyclerview);
        edtTitle = findViewById(R.id.edt_title);
        edtDesc = findViewById(R.id.edt_description);
        edtDate = findViewById(R.id.edt_date);
        edtTime = findViewById(R.id.edt_time);
        tvDateTime =findViewById(R.id.tv_datetime);
        tvDesc = findViewById(R.id.tv_desc);
        tvTitle = findViewById(R.id.tv_title);

        // setup the view
        set_upload_invisible();
        set_download_visible();

        appList = new ArrayList<>();
        // create_appointment();
        get_appointments();
        setAdapter();


        // Show content for adding new Appointment
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                set_download_invisible();
                set_upload_visible();
                edtDate.getText().clear();
                edtTitle.getText().clear();
                edtDesc.getText().clear();
                edtTime.getText().clear();
            }
        });
        // Show TimePickerDialog
        edtTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtTime.setRawInputType(edtTime.getInputType());
                edtTime.setTextIsSelectable(true);
                showTimePickerDialog();
                // Hide the Keyboard on click
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(edtTime.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
            }
        });
        // Show TimePickerDialog
        edtTime.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    edtTime.setRawInputType(edtTime.getInputType());
                    edtTime.setTextIsSelectable(true);
                    // Hide the Keyboard on click
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(edtTime.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
                    showTimePickerDialog();
                }
            }
        });
        // Show DatePickerDialog
        edtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtDate.setRawInputType(edtDate.getInputType());
                edtDate.setTextIsSelectable(true);
                showDatePickerDialog();
                // Hide the Keyboard on click
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(edtDate.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
            }
        });
        // Show DatePickerDialog
        edtDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    edtDate.setRawInputType(edtDate.getInputType());
                    edtDate.setTextIsSelectable(true);
                    showDatePickerDialog();
                    // Hide the Keyboard on click
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(edtDate.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
                }
            }
        });

        // Upload the Appointment
        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title = edtTitle.getText().toString();
                desc = edtDesc.getText().toString();
                time = edtTime.getText().toString();
                date = edtDate.getText().toString();
                datetime = date + " " + time;


                if(title.length()<=1|| desc.length()<=1|| datetime.length()<=1){
                    Toast.makeText(getApplicationContext(),"Please enter appropriate appointment information's",Toast.LENGTH_LONG).show();
                }
                else
                {
                    upload_appointment(currentUserId,title,desc,datetime);
                    appList.clear();
                    get_appointments();
                    set_upload_invisible();
                    set_download_visible();
                    Toast.makeText(getApplicationContext(),"Appointment has been successfully added!",Toast.LENGTH_LONG).show();
                    finish();
                }

            }
        });

        // go back to main activity
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });




    }
    // set the recycler view to list every appointment
    private void setAdapter(){
        setOnClickListener();
        adapter = new RecyclerAdapter(appList,listener);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager((getApplicationContext()));
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        //Log.i("RecyclerView","Added " + adapter.getItemCount()+1+" items to the view");

    }
    private void setOnClickListener(){
        listener = new RecyclerAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View v, int pos) {
                iUserDelete = pos;
                AlertDialog.Builder builder = new AlertDialog.Builder(AppointmentsActivity.this);
                builder.setMessage("Are you sure you want to delete this Appointment?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
            }
        };
    }
    // get all appointments of the current user from the DB
    public void get_appointments(){
        Amplify.DataStore.query(
                Appointments.class,
                Where.matches(Appointments.USER_ID.eq(currentUserId).and(Appointments.DATEOF_APPO.contains(get_current_date()))).sorted(Appointments.DATEOF_APPO.ascending()),
                items -> {
                    while (items.hasNext()) {
                        Appointments item = items.next();
                        String[] splited = item.getDateofAppo().trim().split("\\s+");

                        appList.add(new Appointment(splited[0],splited[1],item.getTitle(),item.getDescription(),item.getId()));
                        Log.i("Amplify", "Appointments Id " + item.getId());
                    }
                },
                failure -> Log.e("Amplify", "Could not query Appointments", failure)
        );
    }
    // Create and upload an appointment
    public void upload_appointment(String userid,String title,String desc,String date){
        Appointments item = Appointments.builder()
                .userId(userid)
                .title(title)
                .description(desc)
                .dateofAppo(date)
                .build();
        Amplify.DataStore.save(
                item,
                success -> Log.i("Amplify", "Saved item: " + success.item().getId()),
                error -> Log.e("Amplify", "Could not save item to DataStore", error)
        );
    }
    // delete an appointment
    public void delete_appointment( int pos){
        Log.i("Delete",appList.get(pos).getID());
        Amplify.DataStore.query(Appointments.class, Where.id(appList.get(pos).getID()),
                matches -> {
                    if (matches.hasNext()) {
                        Appointments post = matches.next();
                        Amplify.DataStore.delete(post,
                                deleted -> {
                                    Toast.makeText(this, "Deleted the Appointment", Toast.LENGTH_SHORT);
                                    finish();
                                },
                                failure ->{
                                    Toast.makeText(this,"Could not delete the Appointment at the moment",Toast.LENGTH_SHORT);
                                    finish();
                                }
                        );
                    }
                },
                failure -> Log.e("MyAmplifyApp", "Query failed.", failure)
        );
    }
    // set all views for the upload invisible
    public void set_upload_invisible(){
        btn_upload.setVisibility(View.INVISIBLE);
        edtTime.setVisibility(View.INVISIBLE);
        edtDate.setVisibility(View.INVISIBLE);
        edtDesc.setVisibility(View.INVISIBLE);
        edtTitle.setVisibility(View.INVISIBLE);
    }
    // set all views for the download invisible
    public void set_download_invisible(){
        btn_add.setVisibility(View.INVISIBLE);
        tvTitle.setVisibility(View.INVISIBLE);
        tvDesc.setVisibility(View.INVISIBLE);
        tvDateTime.setVisibility(View.INVISIBLE);
        recyclerView.setVisibility(View.INVISIBLE);
    }
    // set all views for the upload visible
    public void set_upload_visible(){
        btn_upload.setVisibility(View.VISIBLE);
        edtTime.setVisibility(View.VISIBLE);
        edtDate.setVisibility(View.VISIBLE);
        edtDesc.setVisibility(View.VISIBLE);
        edtTitle.setVisibility(View.VISIBLE);
    }
    // set all views for the download visible
    public void set_download_visible(){
        btn_add.setVisibility(View.VISIBLE);
        tvTitle.setVisibility(View.VISIBLE);
        tvDesc.setVisibility(View.VISIBLE);
        tvDateTime.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.VISIBLE);
    }
    // get current system time
    public String get_current_date() {
        Date currentTime = Calendar.getInstance().getTime();
        return dateformater.format(currentTime);
    }


    private void showDatePickerDialog(){
        DatePickerDialog dPickerDialog = new DatePickerDialog(
                this,
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        dPickerDialog.show();;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String sDay="";
        String sMonth="";
        if(dayOfMonth<10)
        {
            sDay = "0"+ dayOfMonth;
        }
        else{
            sDay = dayOfMonth + "";
        }
        if(month<10)
        {
            sMonth = "0"+ (month + 1);
        }
        else{
            sMonth = (month + 1) + "";
        }
        String date = year + "-" + sMonth +"-"+sDay;
        edtDate.setText(date);
    }

    private void showTimePickerDialog(){
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(AppointmentsActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                edtTime.setText( selectedHour + ":" + selectedMinute);
            }
        }, hour, minute, true);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }
    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    //Yes button clicked
                        delete_appointment(iUserDelete);
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    //No button clicked

                    break;
            }
        }
    };
}
