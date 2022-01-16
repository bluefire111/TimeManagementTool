package com.example.timemanagementtool;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class MainActivity extends AppCompatActivity {
    Connection connection;
    Button btnChange;

    String sResult;
    @SuppressLint("App")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnChange = findViewById(R.id.btn_InAndOut);

        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSQLConnection();
            }
        });

    }


    private void getSQLConnection()
    {
        try{
            ConnectionHelper connectionHelper = new ConnectionHelper();
            connection = connectionHelper.connectionclass();

            if(connection!=null)
            {
                String query = "SELECT * FROM sys.Users";
                Statement st = connection.createStatement();
                ResultSet rs = st.executeQuery(query);

                while(rs.next()){
                    System.out.println(rs.getString(1));
                }
            }
        }
        catch (Exception ex){

        }
    }

}