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
import android.widget.TextView;

import net.sourceforge.jtds.jdbc.DateTime;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    Connection connection;
    DateTime dateCheckIn;
    ImageView imgLegend1,imgLegend2,imgLegend3;
    Button btnCheckIn,btnNFC,btnCalendar,btnSettings;
    TextView tvHeader,tvClock;
    User currentUser;

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

        check_login();

        //update the clock every second
        final Handler handler = new Handler();
        final int delay = 1000; // 1000 milliseconds == 1 second

        handler.postDelayed(new Runnable() {
            public void run() {
                //update the clock in MainActivity
                update_clock();
                handler.postDelayed(this, delay);
            }
        }, delay);


        btnCheckIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)  {

            }
        });

    }

// Start Loading activity and check the user credentials that have been embedded in this system
public void check_login(){
    Intent myIntent = new Intent(MainActivity.this, LoadingScreenActivity.class);
    myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    startActivityForResult(myIntent,1);

}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                currentUser = (User) data.getSerializableExtra("User");
                tvHeader.setText("Hallo " + currentUser.getFirstName());

            }
            if (resultCode == Activity.RESULT_CANCELED) {
                // Write your code if there's no result
            }
        }
    } //onActivityResult

public void update_clock()
{
    DateFormat dateFormat = new SimpleDateFormat("HH:mm");
    Date date = new Date();
    String sClock = dateFormat.format(date);
    tvClock.setText(sClock);
}

public String get_checkin_time()
{
    try {
        ConnectionHelper connectionHelper = new ConnectionHelper();
        connection = connectionHelper.connectionclass();
        if (connection != null) {
            DateFormat dateFormat = new SimpleDateFormat("dd-mm-YYYY");
            Date date = new Date();
            String query = "Select * from Time_History WHERE User_ID LIKE '" + currentUser.getUserId() + "' AND Date LIKE '" + date +"'" ;
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            rs.afterLast();
            if(rs.previous() == false) {
                DateFormat timeFormatCheckIn = new SimpleDateFormat("HH:mm");
                Date timeCheckIn = new Date();
                String sTimeCheckIn = dateFormat.format(timeCheckIn);
                return sTimeCheckIn;
            }
            else
            {
                return rs.getTimestamp(2).toString();
            }
        }

    }
    catch (Exception ex){
        Log.e("Error ", ex.getMessage());
        return "";
    }
    return "";
}

}