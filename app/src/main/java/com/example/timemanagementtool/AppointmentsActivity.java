package com.example.timemanagementtool;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.core.model.query.Where;
import com.amplifyframework.datastore.AWSDataStorePlugin;
import com.amplifyframework.datastore.generated.model.Appointments;
import com.amplifyframework.datastore.generated.model.User;

import java.util.ArrayList;

public class AppointmentsActivity extends AppCompatActivity {
    private ArrayList<Appointment> appList;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointments);

        recyclerView = findViewById(R.id.recyclerview);
        appList = new ArrayList<>();

        //create_appointment();
        //get_appointments();

        //setAdapter();


    }
    // set the recycler view to list every appointment
    private void setAdapter(){
        RecyclerAdapter adapter = new RecyclerAdapter(appList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager((getApplicationContext()));
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

    }


    public void get_appointments(){
        Amplify.DataStore.query(
                Appointments.class,
                items -> {
                    while (items.hasNext()) {
                        Appointments item = items.next();
                        Log.i("Amplify", "Id +++++++++ " + item.getDescription());
                    }
                },
                failure -> Log.e("Amplify", "Could not query DataStore", failure)
        );
    }

    public void create_appointment(){
        Appointments item = Appointments.builder()
                .userId("1234")
                .title("Meeting 101")
                .description("Presentation of App 101")
                .dateofAppo("2022-02-08 14:00:00.000")
                .build();
        Amplify.DataStore.save(
                item,
                success -> Log.i("Amplify", "Saved item: " + success.item().getId()),
                error -> Log.e("Amplify", "Could not save item to DataStore", error)
        );
    }
}
