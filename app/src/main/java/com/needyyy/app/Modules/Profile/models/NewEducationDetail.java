package com.needyyy.app.Modules.Profile.models;

import java.util.HashMap;
import java.util.Map;

public class NewEducationDetail {

    private String id,  name,  qualification,  latitude,  longitude,  from_year,  to_year,  type ;

    public NewEducationDetail(String id, String name, String qualification, String latitude, String longitude, String from_year, String to_year, String type) {
        this.id             = id;
        this.name           = name;
        this.qualification        = qualification;
        this.from_year       = from_year;
        this.to_year         = to_year;
        this.latitude            = latitude;
        this.longitude            = longitude;
        this.type           = type;

//        Map<String, String> queryMap = new HashMap<String, String>();
//        queryMap.put("id", id);
//        queryMap.put("name", eduId);
//        queryMap.put("qualification", qualiId);
//        queryMap.put("latitude", lat);
//        queryMap.put("longitute", lon);
//        queryMap.put("from_year", fromYear);
//        queryMap.put("to_year", toYear);
//        queryMap.put("type", name);

    }
}
