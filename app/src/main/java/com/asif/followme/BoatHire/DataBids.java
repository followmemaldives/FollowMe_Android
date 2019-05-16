package com.asif.followme.BoatHire;

/**
 * Created by user on 12/18/2017.
 */

public class DataBids {
    private String id  = "";
    private String name = "";
    private String image = "";
    private String vdate = "";
    private String contact = "";
    private String price="";
    private int marker = 0;
    private String islandspeed = "";
    private int bid_status = 0;
    private String bid_status_text="";
    private String time_ago ="";
    private String device_id = "";
    private int tender_status = 0;
    private int is_expired = 0;

//	private int fav = 0;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPrice() {return price; }
    public void setPrice(String price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getDeviceID() {
        return device_id;
    }
    public void setDeviceID(String device_id) {
        this.device_id = device_id;
    }

    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }

    public int getIsExpired() {
        return is_expired;
    }
    public void setIsExpired(int is_expired) {
        this.is_expired = is_expired;
    }

    public String getDate() {
        return vdate;
    }
    public void setDate(String date) {
        this.vdate = date;
    }

    public int getBidStatus() {
        return bid_status;
    }
    public void setBidStatus(int bid_status) {
        this.bid_status = bid_status;
    }

    public int getTenderStatus() {
        return tender_status;
    }
    public void setTenderStatus(int tender_status) {
        this.tender_status = tender_status;
    }

    public String getBidStatusText() {
        return bid_status_text;
    }
    public void setBidStatusText(String bid_status_text) {
        this.bid_status_text = bid_status_text;
    }

    public String getTimeAgo() {
        return time_ago;
    }
    public void setTimeAgo(String time_ago) {
        this.time_ago = time_ago;
    }

    public String getContact() {
        return contact;
    }
    public void setContact(String contact) {
        this.contact = contact;
    }

    public int getMarker() {
        return this.marker;
    }
    public void setMarker(int marker) {
        this.marker = marker;
    }

    public String getIslandSpeed() {return this.islandspeed;}
    public void setIslandSpeed(String islandspeed) {
        this.islandspeed = islandspeed;
    }
}
