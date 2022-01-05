package com.example.timemanagementtool;

public class User {

    private Long ID;
    private String PhoneID;
    private String FirstName,LastName;

    public User(Long id, String PhoneID, String FirstName, String LastName){
        this.ID = id;
        this.PhoneID = PhoneID;
        this.FirstName = FirstName;
        this.LastName = LastName;
    }
    public Long getID() {
        return ID;
    }
    public String getPhoneID() {
        return PhoneID;
    }
    public String getFirstName() {
        return FirstName;
    }
    public String getLastName() {
        return LastName;
    }
}
