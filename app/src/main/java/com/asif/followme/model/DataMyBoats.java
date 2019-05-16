package com.asif.followme.model;

/**
 * Created by user on 12/18/2017.
 */

public class DataMyBoats {
    private String id  = "";
    private String name = "";
    private String value = "";
    private String image = "";
    private String vdate = "";
    private int batt = 0;
    private String speed = "";
    private String island = "";
    private int marker = 0;
    private int notice = 0;
    private String contact ="";
    private int is_expired = 0;
//	private int fav = 0;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
    public String getDate() {
        return vdate;
    }

    public void setDate(String date) {
        this.vdate = date;
    }

    public int getBatt() {
        return batt;
    }
    public void setBatt(int batt) {
        this.batt = batt;
    }

    public String getSpeed() {
        return speed;
    }
    public void setSpeed(String speed) {
        this.speed = speed;
    }
    public String getIsland() {
        return island;
    }
    public void setIsland(String island) {
        this.island = island;
    }
    public int getMarker() {
        return marker;
    }
    public void setMarker(int marker) {
        this.marker = marker;
    }
    public int getNotice() {
        return notice;
    }
    public void setNotice(int notice) {
        this.notice = notice;
    }
    public String getContact() {
        return contact;
    }
    public void setContact(String contact) {
        this.contact = contact;
    }

    public int getIsExpired() {
        return is_expired;
    }
    public void setIsExpired(int is_expired) {
        this.is_expired = is_expired;
    }
}
