package com.example.timemanagementtool;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class MainActivity extends AppCompatActivity {
    Connection connection;
    Button btnChange;
    TextView tvHeader;
    String sResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnChange = findViewById(R.id.btn_InAndOut);
        tvHeader = findViewById(R.id.tvHeadline);




        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)  {
                startActivity(new Intent(MainActivity.this, LoadingScreenActivity.class));
            }
        });

    }

public void test_sqlConnection(){
    try {
        ConnectionHelper connectionHelper = new ConnectionHelper();
        connection = connectionHelper.connectionclass();
        if(connection!=null)
        {
            String query = "Select * from Users";
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query);

            while(rs.next()){
                sResult = rs.getString(2);
            }
        }

    }
    catch (Exception ex){
        Log.e("Error ", ex.getMessage());
    }
}


}