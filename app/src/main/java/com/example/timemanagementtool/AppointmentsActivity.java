package com.example.timemanagementtool;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

public class AppointmentsActivity extends AppCompatActivity {
    private ArrayList<Appointment> appList;
    private RecyclerView recyclerView;
    private String currentUserId;
    private DateFormat timeFormater,dateformater;
    private String title,desc,date,time;
    private Button btn_add,btn_back,btn_upload;
    private EditText edtTitle,edtDesc,edtDate,edtTime;
    private TextView tvDateTime,tvTitle,tvDesc;
    RecyclerAdapter adapter;
    String datetime = "";
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

                // TO-DO upload appointment function
                if(title != "" || desc != "" || datetime != ""){
                    upload_appointment(currentUserId,title,desc,datetime);
                    set_upload_invisible();
                    edtDate.getText().clear();
                    edtTitle.getText().clear();
                    edtDesc.getText().clear();
                    edtTime.getText().clear();
                    set_download_visible();

                    appList = new ArrayList<>();
                    // create_appointment();
                    get_appointments();

                    setAdapter();
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Please enter the appointment information",Toast.LENGTH_LONG).show();
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
        adapter = new RecyclerAdapter(appList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager((getApplicationContext()));
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

    }
    // get all appointments of the current user from the DB
    public void get_appointments(){
        Amplify.DataStore.query(
                Appointments.class,
                Where.matches(Appointments.USER_ID.eq(currentUserId).and(Appointments.DATEOF_APPO.contains(get_current_date()))),
                items -> {
                    while (items.hasNext()) {
                        Appointments item = items.next();
                        String[] splited = item.getDateofAppo().trim().split("\\s+");

                        appList.add(new Appointment(splited[0],splited[1],item.getTitle(),item.getDescription()));
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
        btn_back.setVisibility(View.INVISIBLE);
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

        btn_back.setVisibility(View.VISIBLE);
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
}
