package com.google.firebase.codelab.friendlychat;

/**
 * Created by DELL on 1/10/2018.
 */

public class Party {
    private String id;
    private String address;
    private String name;
    private String startTime;
    private String endTime;
    private String organizerName;
    private String organizerRating;
    private String imageUrl;
    private Object attendees;

    public Party(){

    }
    public Party(String id, String address, String name, String startTime, String endTime, String organizerName, String organizerRating, String imageUrl) {
        this.id = id;
        this.address = address;
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
        this.organizerName = organizerName;
        this.organizerRating = organizerRating;
        this.imageUrl = imageUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getOrganizerName() {
        return organizerName;
    }

    public void setOrganizerName(String organizerName) {
        this.organizerName = organizerName;
    }

    public String getOrganizerRating() {
        return organizerRating;
    }

    public void setOrganizerRating(String organizerRating) {
        this.organizerRating = organizerRating;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Object getAttendees() {
        return attendees;
    }

    public void setAttendees(Object attendees) {
        this.attendees = attendees;
    }
}
