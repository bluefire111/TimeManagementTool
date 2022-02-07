package com.example.timemanagementtool;

import java.io.Serializable;

public class Appointment implements Serializable {

    private String date;
    private String time;
    private String title;
    private String description;

    public Appointment(String date, String time, String title, String description) {
        this.date = date;
        this.time = time;
        this.title = title;
        this.description = description;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }
}
