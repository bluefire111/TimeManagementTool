package com.example.timemanagementtool;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

        setApointments();
        setAdapter();


    }
    // set the recycler view to list every appointment
    private void setAdapter(){
        RecyclerAdapter adapter = new RecyclerAdapter(appList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager((getApplicationContext()));
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

    }

    // get all appointments of the current user from the database and add these to the array list
    private void setApointments(){

    }
}
