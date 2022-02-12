package com.example.timemanagementtool;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {
    private ArrayList <Appointment> appList;

    public RecyclerAdapter(ArrayList<Appointment> appList){
        this.appList = appList;

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvdate,tvtime,tvdesc,tvtitle;

        public MyViewHolder(final View view){
            super(view);
            tvdate = view.findViewById(R.id.tv_list_date);
            tvtime = view.findViewById(R.id.tv_list_time);
            tvdesc = view.findViewById(R.id.tv_list_description);
            tvtitle = view.findViewById(R.id.tv_list_title);
        }

    }

    @NonNull
    @Override
    public RecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_appointments,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter.MyViewHolder holder, int position) {
        String date = appList.get(position).getDate();
        String time = appList.get(position).getTime();
        String desc = appList.get(position).getDescription();
        String title = appList.get(position).getTitle();
        holder.tvdate.setText(date);
        holder.tvtime.setText(time);
        holder.tvdesc.setText(desc);
        holder.tvtitle.setText(title);


    }

    @Override
    public int getItemCount() {
        return appList.size();
    }



}
