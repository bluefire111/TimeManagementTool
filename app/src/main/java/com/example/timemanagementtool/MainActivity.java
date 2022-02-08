package com.example.timemanagementtool;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.api.aws.AWSApiPlugin;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.AWSDataStorePlugin;

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

import static com.example.timemanagementtool.R.layout.activity_main;

public class MainActivity extends AppCompatActivity {
    Connection connection;
    DateFormat timeFormatCheckIn;
    DateFormat dateTimeFormatCheckIn;
    String sCheckInDescription;
    String sTimeCheckIn;
    String sTimeCheckOut;
    String sDateTimeCheckIn;
    String sTimeSystem;
    ProgressBar pbWorkingTime;
    Button btnCheckIn, btnNFC, btnCalendar, btnSettings;
    TextView tvHeader, tvClock;
    User currentUser;
    NfcAdapter nfcAdapter;
    private int handler_time = 1000; //1 seconds in milliseconds
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_main);
        getSupportActionBar().hide();

        configureAmplify();

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
        sTimeCheckOut = "";
        sTimeSystem = get_current_time();


        //check in, working time starts when the button is clicked
        btnCheckIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get the last check in entry from the database if no entry is found it is assumed no checkin happend today and a checkin will be created

                if (sTimeCheckIn == "") {
                    sTimeCheckIn = get_current_time();
                    sDateTimeCheckIn = get_current_date();
                    sCheckInDescription = "Check-In";
                    Toast.makeText(MainActivity.this, "Check in uploaded at " + sTimeCheckIn, Toast.LENGTH_LONG).show();
                } else {
                    sCheckInDescription = "Check-Out";
                    sDateTimeCheckIn = get_current_date();
                    Toast.makeText(MainActivity.this, "Check out uploaded at " + sTimeCheckOut, Toast.LENGTH_LONG).show();
                }
                upload_checkin();


            }
        });
        // used to send out an NFC signal through out the phone to any nearby device that can read the signal
        btnNFC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(MainActivity.this);
                if (nfcAdapter == null) {
                    Toast.makeText(MainActivity.this,"This device does not support NFC.", Toast.LENGTH_LONG);
                    return;
                }

                if (!nfcAdapter.isEnabled()) {
                    Toast.makeText(MainActivity.this, "Please enable NFC via Settings.", Toast.LENGTH_LONG).show();
                }
                nfcAdapter.setNdefPushMessageCallback(MainActivity.this::createNdefMessage, MainActivity.this);
            }
        });

        // go to new activity to add, delete or configure  important meetings etc..
        btnCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity.this, AppointmentsActivity.class);
                startActivity(myIntent);
            }
        });


    }
    // get the return value (intent) from the login activitiy
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                currentUser = (User) data.getSerializableExtra("User");
                tvHeader.setText("Hallo " + currentUser.getFirstName());

                get_last_checkin_time();
                get_last_checkout_time();

                if (sTimeCheckOut != "") {
                    btnCheckIn.setAlpha(.5f);
                    btnCheckIn.setClickable(false);
                }

                handler.post(runnable);

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
    // function to set the clock to the current time
    public void update_clock() {
        tvClock.setText(get_current_time());
    }
    // function to get the current system date
    public String get_current_date() {
        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            connection = connectionHelper.connectionclass();
            if (connection != null) {
                String query = "SELECT GETDATE() AS date";
                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery(query);
                if (rs.next() != false) {
                    //System.out.println("+++++"+dateTimeFormatCheckIn.format(rs.getTimestamp(1)));
                    return dateTimeFormatCheckIn.format(rs.getTimestamp(1));
                }

            }

        } catch (Exception ex) {

            Log.e("Error function get_current_date", ex.getMessage());
        }

        return "";
    }
    // function to geth the current system time
    public String get_current_time() {
        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            connection = connectionHelper.connectionclass();
            if (connection != null) {
                String query = "SELECT GETDATE() AS date";
                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery(query);
                if (rs.next() != false) {
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
                String query = "Select TOP (1) * from Time_History WHERE User_ID LIKE " + currentUser + " AND Description LIKE 'Check-In' AND DATEADD(dd, 0, DATEDIFF(dd, 0, Date)) like DATEADD(dd, 0, DATEDIFF(dd, 0, GETDATE())) ORDER BY Date DESC";
                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery(query);
                if (rs.next() != false) {
                    sDateTimeCheckIn = dateTimeFormatCheckIn.format(rs.getTimestamp(2));
                    sTimeCheckIn = timeFormatCheckIn.format(rs.getTimestamp(2));
                    connection.close();
                }

            }

        } catch (Exception ex) {

            Log.e("Error function get_last_checkin_time ", ex.getMessage());
        }
    }
    // function to get the last check out entry from the database
    public void get_last_checkout_time() {
        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            connection = connectionHelper.connectionclass();
            if (connection != null) {
                String query = "Select TOP (1) * from Time_History WHERE User_ID LIKE " + currentUser.getID() + " AND Description LIKE 'Check-Out'  AND DATEADD(dd, 0, DATEDIFF(dd, 0, Date)) like DATEADD(dd, 0, DATEDIFF(dd, 0, GETDATE())) ORDER BY Date DESC";
                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery(query);
                if (rs.next() != false) {
                    sTimeCheckOut = timeFormatCheckIn.format(rs.getTimestamp(2));
                    connection.close();
                }

            }

        } catch (Exception ex) {

            Log.e("Error function get_last_checkin_time ", ex.getMessage());
        }
    }
    //function to upload the check in/out time
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
    // function to update the progressbar
    public void update_progressbar() throws ParseException {
        // In case check out has been already done select check in & check out and set the progress bar
        Integer totalMinutes=0;
        if (sTimeCheckOut != "" && sTimeCheckIn != "") {
            Integer iMinutesCheckIn = convert_to_minutes(sTimeCheckIn);
            Integer iMinutesSystemTime = convert_to_minutes(sTimeCheckOut);
            Integer iSumMinutes = iMinutesSystemTime - iMinutesCheckIn;
            totalMinutes = iSumMinutes;
            double maxPerDay = (double) (100.0 / 645.0);
            double sum = Double.valueOf(maxPerDay * iSumMinutes);
            int s = (int) sum;
            pbWorkingTime.setProgress((int) sum, true);
        } else {
            if (sTimeCheckIn != "" && sTimeSystem != "") {
                Integer iMinutesCheckIn = convert_to_minutes(sTimeCheckIn);
                Integer iMinutesSystemTime = convert_to_minutes(sTimeSystem);
                Integer iSumMinutes = iMinutesSystemTime - iMinutesCheckIn;
                totalMinutes = iSumMinutes;
                double maxPerDay = (double) (100.0 / 645.0);
                double sum = Double.valueOf(maxPerDay * iSumMinutes);
                int s = (int) sum;
                pbWorkingTime.setProgress((int) sum, true);
            }
        }
        // TO-DO Break + Overtime


    }
    // converts a String in the format of HH:MM to minutes
    private static int convert_to_minutes(String s) {
        String[] array = s.split(":");
        int hour = Integer.parseInt(array[0]);
        int mins = Integer.parseInt(array[1]);
        int sum = hour * 60;
        return sum + mins;
    }
    //loop for updating the progressbar and the clock
    private Runnable runnable = new Runnable() {

        @Override
        public void run() {
            //update the clock & the progress bar every second
            update_clock();
            sTimeSystem = get_current_time();
            if (sTimeCheckIn != "") {
                try {
                    update_progressbar();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            handler.postDelayed(this, handler_time);
        }

    };

    public NdefMessage createNdefMessage(NfcEvent nfcEvent) {
        String message = currentUser.getUserId();
        NdefRecord ndefRecord = NdefRecord.createMime("text/plain", message.getBytes());
        NdefMessage ndefMessage = new NdefMessage(ndefRecord);
        return ndefMessage;
    }

    //connect to aws
    public void configureAmplify(){
        try {
            //Amplify.DataStore.clear();
            Amplify.addPlugin(new AWSApiPlugin());
            Amplify.addPlugin(new AWSDataStorePlugin());
            Amplify.configure(getApplicationContext());
            Log.i("Amplify", "Initialized Amplify");
        } catch (AmplifyException error) {
            Log.e("Amplify", "Could not initialize Amplify", error);
        }
    }

}