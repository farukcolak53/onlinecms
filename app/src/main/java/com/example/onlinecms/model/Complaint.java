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
    private int status;
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
        this.status = 0;
        this.count = count + 1;
    }

    public void setStatus(int status) {
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
        result.put("status",status);
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

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public int getStatus() {
        return status;
    }
}
