package com.example.timemanagementtool;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class LoadingScreenActivity extends AppCompatActivity {
    ProgressBar pbLoadingScreen;
    Connection connection;
    boolean okLogin;
    String userId = "0000";
    String loginPin = "1234";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_screen);

        pbLoadingScreen = (ProgressBar) findViewById(R.id.progressBarLoadingScreen);
        pbLoadingScreen.setProgress(20,true);
        okLogin = check_login();
        pbLoadingScreen.setProgress(80);
        Intent intent = new Intent();
        if(okLogin = true)
        {
            pbLoadingScreen.setProgress(100);
            setResult(RESULT_OK, intent);
            finish();
        }
        else
        {
            pbLoadingScreen.setProgress(100);
            Toast.makeText(this, "Unsuccessful login, please contact the local IT department.", Toast.LENGTH_SHORT).show();
            setResult(RESULT_CANCELED,intent);
        }


    }
    public boolean check_login(){
        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            connection = connectionHelper.connectionclass();
            pbLoadingScreen.setProgress(40);
            if (connection != null) {
                String query = "Select * from Users WHERE User_ID LIKE '" + userId + "' AND Login_Pin LIKE '" + loginPin +"'" ;
                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery(query);
                pbLoadingScreen.setProgress(60);
                if(rs.next() == false) {
                    pbLoadingScreen.setProgress(80);
                    return false;
                }
                else
                {
                    pbLoadingScreen.setProgress(80);
                    return true;
                }
            }
        }
        catch (Exception ex){
            Log.e("Error ", ex.getMessage());
        }
        return false;
    }

}
