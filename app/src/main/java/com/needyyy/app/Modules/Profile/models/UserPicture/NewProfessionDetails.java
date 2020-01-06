package com.needyyy.app.Modules.Profile.models.UserPicture;

/**
 * Created by Admin on 30-07-2019.
 */

public class NewProfessionDetails {

    private String id,company, designation,  location,  latitude,  longitude,  date_joining,  date_left ;

    public NewProfessionDetails(String id,String company, String designation, String location, String latitude, String longitude, String date_joining, String date_left) {
        this.id=id;
        this.company             = company;
        this.designation           = designation;
        this.location        = location;
        this.latitude       = latitude;
        this.longitude         = longitude;
        this.date_joining            = date_joining;
        this.date_left            = date_left;
    }
}
