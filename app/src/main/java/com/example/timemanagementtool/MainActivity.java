package com.example.timemanagementtool;

import android.app.Activity;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.core.AmplifyConfiguration;
import com.amplifyframework.core.model.query.Where;
import com.amplifyframework.datastore.AWSDataStorePlugin;
import com.amplifyframework.datastore.generated.model.TimeHistory;
import com.amplifyframework.api.aws.AWSApiPlugin;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static com.example.timemanagementtool.R.layout.activity_main;

public class MainActivity extends AppCompatActivity {

    DateFormat timeFormatCheckIn;
    DateFormat dateTimeFormatCheckIn;
    DateFormat dateFormatCheckin;
    String sCheckInDescription;
    String sTimeCheckIn;
    String sTimeCheckOut;
    String sDateTimeCheckIn;
    String sTimeSystem;
    ProgressBar pbWorkingTime,pbPauseTime,pbOverTime;
    Button btnCheckIn, btnNFC, btnCalendar;
    TextView tvHeader, tvClock;
    User currentUser;
    NfcAdapter nfcAdapter;
    boolean msg=false;
    int totalMinutes = 0;
    private int handler_time = 1000; //1 seconds in milliseconds
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        configureAmplify();
        setContentView(activity_main);
        getSupportActionBar().hide();

        btnCheckIn = findViewById(R.id.btn_InAndOut);
        btnNFC = findViewById(R.id.btn_NFC);
        btnCalendar = findViewById(R.id.btnCalendar);
        tvHeader = findViewById(R.id.tvHeadline);
        tvClock = findViewById(R.id.tvClock);
        pbWorkingTime = findViewById(R.id.pbWorkingTime);
        pbPauseTime = findViewById(R.id.pbPauseTime);
        pbOverTime = findViewById(R.id.pbOverTime);

        timeFormatCheckIn = new SimpleDateFormat("HH:mm");
        dateTimeFormatCheckIn = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
        dateFormatCheckin = new SimpleDateFormat("YYYY-MM-dd");

        set_screen_invisible();
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
                    try {
                        sDateTimeCheckIn = get_current_datetime();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    sCheckInDescription = "Check-In";
                    Toast.makeText(MainActivity.this, "Check in uploaded at " + sTimeCheckIn, Toast.LENGTH_LONG).show();
                } else {
                    sCheckInDescription = "Check-Out";
                    try {
                        sDateTimeCheckIn = get_current_datetime();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(MainActivity.this, "Check out uploaded at " + sTimeCheckOut, Toast.LENGTH_LONG).show();
                }
                upload_checkin();


            }
        });
        // used to send out an NFC signal through out the phone to any nearby device that can read the signal
        NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        btnNFC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (nfcAdapter == null) {
                    Toast.makeText(getApplicationContext(), "This device does not support NFC.", Toast.LENGTH_LONG).show();
                    return;
                }

                if (!nfcAdapter.isEnabled()) {
                    Toast.makeText(getApplicationContext(), "Please enable NFC via Settings.", Toast.LENGTH_LONG).show();
                }

                if (nfcAdapter.isEnabled()) {
                    nfcAdapter.setNdefPushMessageCallback(MainActivity.this::createNdefMessage, MainActivity.this);
                    Toast.makeText(getApplicationContext(),"Sucessfully activated NFC!", Toast.LENGTH_LONG).show();
                }

            }
        });


        // go to new activity to add, delete or configure  important meetings etc..
        btnCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity.this, AppointmentsActivity.class);
                myIntent.putExtra("userid",currentUser.getID());
                startActivity(myIntent);
            }
        });




    }

    // get the return value (intent) from the login activitiy
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // wait 3 seconds
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                set_screen_visible();
            }
        }, 3000);   //3 seconds

        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                currentUser = (User) data.getSerializableExtra("User");
                tvHeader.setText("Hallo " + currentUser.getFirstName());

                try {
                    get_last_checkin_time();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    get_last_checkout_time();
                } catch (Exception e) {
                    e.printStackTrace();
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
    public String get_current_datetime() {
        Date currentTime = Calendar.getInstance().getTime();
        return dateTimeFormatCheckIn.format(currentTime);
    }

    public String get_current_date() {
        Date currentTime = Calendar.getInstance().getTime();
        return dateFormatCheckin.format(currentTime);
    }

    // function to get the current system time
    public String get_current_time() {
        Date currentTime = Calendar.getInstance().getTime();
        return timeFormatCheckIn.format(currentTime);
    }

    // function to get the last check in entry from the database
    public void get_last_checkin_time()  {
        Amplify.DataStore.query(
                com.amplifyframework.datastore.generated.model.TimeHistory.class,
                Where.matches(TimeHistory.USER_ID.eq(currentUser.getID()).and(TimeHistory.DATE.contains(get_current_date())).and(TimeHistory.DESCRIPTION.eq("Check-In"))),
                items -> {
                    while (items.hasNext()) {
                        com.amplifyframework.datastore.generated.model.TimeHistory item = items.next();
                        sDateTimeCheckIn = item.getCheckIn();
                        sTimeCheckIn = item.getCheckIn();
                        Log.i("Amplify", "Successfully retrieved the checkin information" + item.getCheckIn().toString());
                    }
                }, failure -> Log.e("Amplify", "Could not query DataStore for last checkin", failure));
    }

    // function to get the last check out entry from the database
    public void get_last_checkout_time() throws Exception {
        Amplify.DataStore.query(
                com.amplifyframework.datastore.generated.model.TimeHistory.class,
                Where.matches(TimeHistory.USER_ID.eq(currentUser.getID()).and(TimeHistory.DATE.contains(get_current_date())).and(TimeHistory.DESCRIPTION.eq("Check-Out"))),
                items -> {
                    while (items.hasNext()) {
                        com.amplifyframework.datastore.generated.model.TimeHistory item = items.next();
                        sTimeCheckOut = item.getCheckIn();
                        Log.i("Amplify", "Successfully retrieved the checkout information");
                    }
                }, failure -> Log.e("Amplify", "Could not query DataStore for last checkout", failure));
    }

    //function to upload the check in/out time
    public void upload_checkin() {
        TimeHistory item = TimeHistory.builder()
                .userId(currentUser.getID())
                .date(sDateTimeCheckIn)
                .checkIn(sTimeCheckIn)
                .description(sCheckInDescription)
                .build();
        Amplify.DataStore.save(
                item,
                success -> Log.i("Amplify", "Saved uploaded: " + success.item().getDescription()),
                error -> Log.e("Amplify", "Could not save Checkin or CheckOut to DataStore", error)
        );
    }

    // function to update the progressbar based on the total minutes the user worked or is working till now
    public void update_progressbar() throws ParseException {
        // In case check out has been already done select check in & check out and set the progress bar

        if (sTimeCheckOut != "" && sTimeCheckIn != "") {
            btnCheckIn.setAlpha(.5f);
            btnCheckIn.setClickable(false);
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
        //  Break + Overtime
        if(totalMinutes> 360 && totalMinutes < 540){
            pbPauseTime.setProgress(30,true);

        }

        if(totalMinutes > 540){
            pbPauseTime.setProgress(45,true);
        }

        if(totalMinutes> 645){
            pbOverTime.setProgress(totalMinutes-645,true);
        }

        if(totalMinutes> 645 && msg == false){
            Toast.makeText(getApplicationContext(),"You are working overtime for  " + (totalMinutes-645) + " minutes!", Toast.LENGTH_LONG).show();
            msg = true;
        }

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

                btnCheckIn.setText("Check-Out");
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
        NdefRecord ndef = NdefRecord.createMime("text/plain", message.getBytes());
        NdefMessage msg = new NdefMessage(ndef);
        return msg;
    }

    //connect to aws-amplify
    public void configureAmplify() {
        try {
            Amplify.addPlugin(new AWSApiPlugin());
            Amplify.addPlugin(new AWSDataStorePlugin());
            // disable dev menu
            Amplify.configure(AmplifyConfiguration.builder(getApplicationContext()).devMenuEnabled(false).build(), getApplicationContext());

            Log.i("MyAmplifyApp", "Initialized Amplify");
        } catch (AmplifyException error) {
            Log.e("MyAmplifyApp", "Could not initialize Amplify", error);
        }
    }

    // set the screen invisible
    public void set_screen_invisible(){
        btnCheckIn.setVisibility(View.INVISIBLE);
        btnCalendar.setVisibility(View.INVISIBLE);
        btnNFC.setVisibility(View.INVISIBLE);

        pbWorkingTime.setVisibility(View.INVISIBLE);
        pbPauseTime.setVisibility(View.INVISIBLE);
        pbOverTime.setVisibility(View.INVISIBLE);
        tvHeader.setVisibility(View.INVISIBLE);
        tvClock.setVisibility(View.INVISIBLE);

    }
    // set the screen visible
    public void set_screen_visible(){
        btnCheckIn.setVisibility(View.VISIBLE);
        btnCalendar.setVisibility(View.VISIBLE);
        btnNFC.setVisibility(View.VISIBLE);

        pbWorkingTime.setVisibility(View.VISIBLE);
        pbPauseTime.setVisibility(View.VISIBLE);
        pbOverTime.setVisibility(View.VISIBLE);
        tvHeader.setVisibility(View.VISIBLE);
        tvClock.setVisibility(View.VISIBLE);

    }


}

