package com.example.onlinecms.model;

import java.util.HashMap;
import java.util.Map;

public class Complaint {

    private String title;
    private String description;
    private String address;
    private static int count = 0;
    public Complaint(String title, String description, String address) {
        this.title = title;
        this.description = description;
        this.address = address;
        this.count = count + 1;
    }

    public static int getCount() {
        return count;
    }

    public Map<String, Object> toMap(){
        HashMap<String,Object> result = new HashMap<>();
        result.put("address",address);
        result.put("description", description);
        result.put("title",title);
        return result;
    }
}
