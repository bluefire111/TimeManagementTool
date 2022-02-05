package com.example.timemanagementtool;

import java.io.Serializable;

public class User implements Serializable {

    private Long ID;
    private String UserId;
    private String FirstName,LastName;

    public User(Long id, String UserId, String FirstName, String LastName){
        this.ID = id;
        this.UserId = UserId;
        this.FirstName = FirstName;
        this.LastName = LastName;
    }
    public Long getID() {
        return ID;
    }
    public String getUserId() {
        return UserId;
    }
    public String getFirstName() {
        return FirstName;
    }
    public String getLastName() {
        return LastName;
    }


}
