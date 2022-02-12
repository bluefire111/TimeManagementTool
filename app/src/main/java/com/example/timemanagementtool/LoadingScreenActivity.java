package com.example.timemanagementtool;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.api.rest.RestOptions;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.core.Consumer;
import com.amplifyframework.core.model.query.Where;
import com.amplifyframework.datastore.AWSDataStorePlugin;
import com.amplifyframework.datastore.generated.model.Appointments;
import com.amplifyframework.datastore.generated.model.TimeHistory;


public class LoadingScreenActivity extends AppCompatActivity {
    ProgressBar pbLoadingScreen;
    Button btnLogin;
    EditText edtUserName,edtPassword;
    User currentUser;


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
                pbLoadingScreen.setProgress(10,true);
                String user = edtUserName.getText().toString();
                String pass = edtPassword.getText().toString().trim();
                pbLoadingScreen.setProgress(20,true);
                get_login(user,pass);
                // Hide the Keyboard on click
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(edtPassword.getWindowToken(),
                        InputMethodManager.RESULT_UNCHANGED_SHOWN);

                pbLoadingScreen.setVisibility(View.VISIBLE);




            }
        });




    }
    public void get_login(String user, String pass)
    {
        pbLoadingScreen.setProgress(30,true);
        Amplify.DataStore.query(
                com.amplifyframework.datastore.generated.model.User.class,
                Where.matches(com.amplifyframework.datastore.generated.model.User.USER_ID.eq(user).and(com.amplifyframework.datastore.generated.model.User.LOGIN_PIN.eq(pass))),
                items -> {
                    while (items.hasNext()) {
                        com.amplifyframework.datastore.generated.model.User item = items.next();
                        currentUser = new User(item.getId(),item.getUserId(),item.getFirstName(),item.getLastName());
                        System.out.println("+++" + item.getFirstName());
                        Log.i("Amplify", "Successfully retrieved the user information");
                        download_all_user_data();
                        rtn_to_Main();

                    }
                    }, failure -> Log.e("Amplify", "Could not query User Login information", failure));


    }
    public void rtn_to_Main() {
        pbLoadingScreen.setProgress(60,true);
        if(currentUser != null)
        {
            pbLoadingScreen.setProgress(100, true);
            Intent intent = new Intent(LoadingScreenActivity.this, MainActivity.class);
                    /*
                    intent.putExtra("ID", currentUser.getID());
                    intent.putExtra("UserId", currentUser.getUserId());
                    intent.putExtra("FirstName", currentUser.getFirstName());
                    intent.putExtra("LastName", currentUser.getLastName());
                    */
            intent.putExtra("User", currentUser);
            setResult(Activity.RESULT_OK, intent);
            finish();
        }
        else
        {
            pbLoadingScreen.setProgress(0,true);
            pbLoadingScreen.setVisibility(View.INVISIBLE);
            Toast.makeText(getApplicationContext(), "Unsuccessful login, please contact the local IT department.", Toast.LENGTH_LONG).show();
        }

    }

    public void download_all_user_data(){
        Amplify.DataStore.query(
                TimeHistory.class,
                items -> {
                    while (items.hasNext()) {
                        TimeHistory item = items.next();
                        Log.i("Amplify", "Time History Id " + item.getId());
                    }
                },
                failure -> Log.e("Amplify", "Could not query Time History", failure)
        );

        Amplify.DataStore.query(
                Appointments.class,
                items -> {
                    while (items.hasNext()) {
                        Appointments item = items.next();
                        Log.i("Amplify", "Appointments Id " + item.getId());
                    }
                },
                failure -> Log.e("Amplify", "Could not query Appointments", failure)
        );
    }

}
/*
    // SQL Database connection
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
                    currentUser = new User("1",rs.getString(4),rs.getString(2),rs.getString(3));
                    return true;
                }
            }
        }
        catch (Exception ex){
            Log.e("Error ", ex.getMessage());
        }
        return false;
    }

 */