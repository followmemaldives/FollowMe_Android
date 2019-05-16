package com.asif.followme.TripPlan;

/**
 * Created by user on 12/18/2017.
 */

public class DataPax {
    private int pax_id  = 0;
    private int trip_id = 0;
    private String name = "";
    private String bfrom = "";
    private String bto = "";
    private String image = "";
    private String contact = "";
    private int bstatus_int = 0;
    private String bstatus_str = "";

    public int getPaxId() {
        return pax_id;
    }
    public void setPaxId(int pax_id) {
        this.pax_id = pax_id;
    }

    public int getTripId() {
        return trip_id;
    }
    public void setTripId(int trip_id) {
        this.trip_id = trip_id;
    }

    public String getFrom() {return bfrom;}
    public void setFrom(String bfrom) {this.bfrom = bfrom;}

    public String getTo() {return bto;}
    public void setTO(String bto) {this.bto = bto;}

    public String getName() {return name;}
    public void setName(String name) {this.name = name;}

    public String getImage() {return image;}
    public void setImage(String image) {this.image = image;}

    public String getContact() {return contact;}
    public void setContact(String contact) {this.contact = contact;}

    public int getBookingStatusInt(){return bstatus_int;}
    public void setBookingStatusInt(int bstatus_int){this.bstatus_int=bstatus_int;}

    public String getBookingStatusStr(){return bstatus_str;}
    public void setbookingStatusStr(String bstatus_str){this.bstatus_str=bstatus_str;}
}
