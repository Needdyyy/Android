package com.needyyy.app.Modules.Profile.models;

public class AddressDetails {
    String location, latitude, longitude, type, id;

    public AddressDetails(String id, String location, String latitude, String longitude, String type){
        this.id         = id;
        this.location   = location;
        this.latitude   = latitude;
        this.longitude  = longitude;
        this.type       = type;

    }

}
