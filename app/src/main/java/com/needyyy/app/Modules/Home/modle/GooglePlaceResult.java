package com.needyyy.app.Modules.Home.modle;

import java.io.Serializable;

public class GooglePlaceResult implements Serializable {
    public String id;
    public String name;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    private String location;

    public GooglePlaceResult(String id, String name){
        this.id = id;
        this.name=name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
