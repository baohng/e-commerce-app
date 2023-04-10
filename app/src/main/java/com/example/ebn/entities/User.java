package com.example.ebn.entities;

import java.util.Date;

public class User {
    public String fullName, phoneNumber, email, descript, gender, occupation, dob;

    public User() {

    }

    public User(String fullName, String phoneNumber, String email, String descript, String gender, String occupation, String dob) {
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.descript = descript;
        this.gender = gender;
        this.occupation = occupation;
        this.dob = dob;
    }
}
