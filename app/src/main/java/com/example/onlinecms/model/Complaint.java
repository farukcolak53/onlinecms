package com.example.onlinecms.model;

import java.util.HashMap;
import java.util.Map;

public class Complaint {

    private String title;
    private String description;
    private String address;
    private String imageUrl;
    private String dateCreated;
    private static int count = 0;
    private String id;
    private String email;
    private String status;
    public Complaint(){

    }

    public Complaint(String title, String description, String address, String imageUrl, String dateCreated, String id, String email) {
        this.title = title;
        this.description = description;
        this.address = address;
        this.imageUrl = imageUrl;
        this.dateCreated = dateCreated;
        this.id = id;
        this.email = email;
        this.status = "open";
        this.count = count + 1;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static int getCount() {
        return count;
    }

    public Map<String, Object> toMap(){
        HashMap<String,Object> result = new HashMap<>();
        result.put("address",address);
        result.put("description", description);
        result.put("title",title);
        result.put("imageUrl", imageUrl);
        result.put("dateCreated", dateCreated);
        result.put("id",id);
        result.put("email", email);
        return result;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getAddress() {
        return address;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getDateCreated() {
        return dateCreated;
    }
}
