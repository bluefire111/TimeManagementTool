package com.example.timemanagementtool;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import net.sourceforge.jtds.jdbc.DateTime;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    Connection connection;
    DateFormat timeFormatCheckIn;
    DateFormat dateTimeFormatCheckIn;
    String sCheckInDescription;
    String sTimeCheckIn;
    String sDateTimeCheckIn;
    String sTimeSystem;
    ProgressBar pbWorkingTime;
    Button btnCheckIn, btnNFC, btnCalendar, btnSettings;
    TextView tvHeader, tvClock;
    User currentUser;
    int iWorkingProgress = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        btnCheckIn = findViewById(R.id.btn_InAndOut);
        btnNFC = findViewById(R.id.btn_NFC);
        btnCalendar = findViewById(R.id.btnCalendar);
        btnSettings = findViewById(R.id.btnSettings);
        tvHeader = findViewById(R.id.tvHeadline);
        tvClock = findViewById(R.id.tvClock);
        pbWorkingTime = findViewById(R.id.pbWorkingTime);
        timeFormatCheckIn = new SimpleDateFormat("HH:mm");
        dateTimeFormatCheckIn = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
        check_login();
        sTimeCheckIn = "";
        sTimeSystem = "";
        //update the clock every second
        update_clock();
        final Handler handler = new Handler();
        final int delay = 1000; // 1000 milliseconds == 1 second

        handler.postDelayed(new Runnable() {
            public void run() {
                // update the clock every second
                update_clock();
                get_last_checkin_time();
                // updates the progressbar every second
                try {
                    update_progressbar();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                handler.postDelayed(this, delay);
            }
        }, delay);
        get_last_checkin_time();
        //check in, working time starts when the button is clicked
        btnCheckIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get the last check in entry from the database if no entry is found it is assumed no checkin happend today and a checkin will be created

                if (sTimeCheckIn == "") {
                    sTimeCheckIn = get_current_time();
                    sDateTimeCheckIn = get_current_date();
                    sCheckInDescription = "Check-In";
                } else {
                    sCheckInDescription = "Check-Out";
                }
                upload_checkin();


            }
        });
        // used to send out an NFC signal through out the phone to any nearby device that can read the signal
        btnNFC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iWorkingProgress = iWorkingProgress - 10;
                pbWorkingTime.setProgress(iWorkingProgress, true);
            }
        });

        // go to new activity to add, delete or configure  important meetings etc..
        btnCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    // get return value from login/loadingscreen activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                currentUser = (User) data.getSerializableExtra("User");
                tvHeader.setText("Hallo " + currentUser.getFirstName());

            }
            if (resultCode == Activity.RESULT_CANCELED) {

            }
        }
    }

    // Start Loading activity and check the user credentials that have been embedded in this system
    public void check_login() {
        Intent myIntent = new Intent(MainActivity.this, LoadingScreenActivity.class);
        myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivityForResult(myIntent, 1);

    }

    // function to get the current time
    public void update_clock() {
        tvClock.setText(get_current_time());
    }

    public String get_current_date()
    {
        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            connection = connectionHelper.connectionclass();
            if (connection != null) {
                String query = "SELECT GETDATE() AS date";
                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery(query);
                if(rs.next() != false){
                    System.out.println("+++++"+dateTimeFormatCheckIn.format(rs.getTimestamp(1)));
                    return dateTimeFormatCheckIn.format(rs.getTimestamp(1));
                }

            }

        } catch (Exception ex) {

            Log.e("Error function get_current_date", ex.getMessage());
        }

        return "";
    }
    public String get_current_time()
    {
        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            connection = connectionHelper.connectionclass();
            if (connection != null) {
                String query = "SELECT GETDATE() AS date";
                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery(query);
                if(rs.next() != false){
                    return timeFormatCheckIn.format(rs.getTimestamp(1));
                }

            }

        } catch (Exception ex) {

            Log.e("Error function get_current_date", ex.getMessage());
        }

        return "";
    }

    // function to get the last check in entry from the database
    public void get_last_checkin_time() {
        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            connection = connectionHelper.connectionclass();
            if (connection != null) {
                String query = "Select TOP (1) * from Time_History WHERE User_ID LIKE " + currentUser.getID().intValue() + " AND DATEADD(dd, 0, DATEDIFF(dd, 0, Date)) like DATEADD(dd, 0, DATEDIFF(dd, 0, GETDATE())) ORDER BY Date DESC";
                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery(query);
                if(rs.next() != false){
                    sDateTimeCheckIn = dateTimeFormatCheckIn.format(rs.getTimestamp(2));
                    sTimeCheckIn = timeFormatCheckIn.format(rs.getTimestamp(2));
                    connection.close();
                }

            }

        } catch (Exception ex) {

            Log.e("Error function get_last_checkin_time ", ex.getMessage());
        }
    }

    public void upload_checkin() {
        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            connection = connectionHelper.connectionclass();
            if (connection != null) {
                String query = "INSERT INTO Time_History VALUES (" + currentUser.getID() + ",GETDATE(),'" + sDateTimeCheckIn + "', '" + sCheckInDescription + "' )";
                Statement statement = connection.createStatement();
                statement.executeUpdate(query);
            }

        } catch (Exception ex) {
            Log.e("Error function upload_checkin", ex.getMessage());
        }
    }

    public void update_progressbar() throws ParseException {
        if (sTimeCheckIn != "" && sTimeSystem != "") {
            Integer iMinutesCheckIn = convert_to_minutes(sTimeCheckIn);
            Integer iMinutesSystemTime = convert_to_minutes(sTimeSystem);
            Integer iSumMinutes = iMinutesCheckIn + iMinutesSystemTime;
            double sum = Double.valueOf(645 / iSumMinutes);
            System.out.println("++++++" + iSumMinutes);
            pbWorkingTime.setProgress((int) sum, true);
        }
    }

    private static int convert_to_minutes(String s) {
        String[] array = s.split(":");
        int hour = Integer.parseInt(array[0]);
        int mins = Integer.parseInt(array[1]);
        int sum = hour * 60;
        return sum + mins;
    }

}