package com.example.match;

import java.util.ArrayList;

public class UserProfile {

    String UserId;
    ArrayList<String> InterestArray = new ArrayList<String>();
    ArrayList<String> LocationArray = new ArrayList<String>();
    String Radius;
    String Lat;
    String Long;
    boolean Accepted;
    String proposedLat;
    String proposedLong;


    public UserProfile(){

    }

    public UserProfile(String UserId, ArrayList<String> InterestArray, ArrayList<String> LocationArray, String Radius, String Lat, String Long){
        this.UserId = UserId;
        this.InterestArray = InterestArray;
        this.LocationArray = LocationArray;
        this.Radius = Radius;
        this.Lat = Lat;
        this.Long = Long;
        this.Accepted = false;
        this.proposedLat = "";
        this.proposedLong = "";

    }

}
