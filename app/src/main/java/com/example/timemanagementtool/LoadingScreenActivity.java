package com.example.timemanagementtool;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class LoadingScreenActivity extends AppCompatActivity {
    ProgressBar pbLoadingScreen;
    Button btnLogin;
    EditText edtUserName,edtPassword;
    User currentUser;
    Connection connection;
    boolean okLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_screen);
        getSupportActionBar().hide();
        edtUserName = findViewById(R.id.edt_UserId);
        edtPassword = findViewById(R.id.edt_Password);
        btnLogin = findViewById(R.id.btn_Login);
        pbLoadingScreen = findViewById(R.id.progressBarLoadingScreen);
        pbLoadingScreen.setVisibility(View.INVISIBLE);


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = edtUserName.getText().toString();
                String pass = edtPassword.getText().toString().trim();

                // Hide the Keyboard on click
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(edtPassword.getWindowToken(),
                        InputMethodManager.RESULT_UNCHANGED_SHOWN);

                pbLoadingScreen.setVisibility(View.VISIBLE);
                pbLoadingScreen.setProgress(20,true);

                okLogin = check_login(user,pass);
                pbLoadingScreen.setProgress(80,true);
                // when user has been found set progress to 100 and go back to main activity else error message and close the app
                if(okLogin == true)
                {
                    pbLoadingScreen.setProgress(100,true);
                    Intent intent = new Intent(v.getContext(), MainActivity.class);
                    /*
                    intent.putExtra("ID", currentUser.getID());
                    intent.putExtra("UserId", currentUser.getUserId());
                    intent.putExtra("FirstName", currentUser.getFirstName());
                    intent.putExtra("LastName", currentUser.getLastName());
                    */
                    intent.putExtra("User",currentUser);
                    setResult(Activity.RESULT_OK,intent);
                    finish();
                }
                else
                {
                    pbLoadingScreen.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(), "Unsuccessful login, please contact the local IT department.", Toast.LENGTH_LONG).show();
                }
            }
        });





    }
    public boolean check_login(String userId,String loginPin){
        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            connection = connectionHelper.connectionclass();
            pbLoadingScreen.setProgress(40,true);
            if (connection != null) {
                String query = "Select * from Users WHERE User_ID LIKE '" + userId + "' AND Login_Pin LIKE '" + loginPin +"'" ;
                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery(query);
                pbLoadingScreen.setProgress(60,true);
                if(rs.next() == false) {
                    pbLoadingScreen.setProgress(80,true);
                    return false;
                }
                else
                {
                    pbLoadingScreen.setProgress(80,true);
                    currentUser = new User(rs.getLong(1),rs.getString(4),rs.getString(2),rs.getString(3));
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
/*

 */