package com.asif.followme.TripPlan;

/**
 * Created by user on 12/18/2017.
 */

public class DataTripPlans {
    private int trip_id  = 0;
    private String device_id = "";
    private String name = "";
    private String route = "";
    private String image = "";
    private String trip_date = "";
    private int trip_status=0;
    private String trip_status_text = "";
    private int captain =0;
    private int trip_access =0;
    private int pax_total_count = 0;
    private int pax_booked_count = 0;
    private int pax_confirmed_count = 0;

//	private int fav = 0;

    public int getTripId() {
        return trip_id;
    }
    public void setTripId(int trip_id) {
        this.trip_id = trip_id;
    }

    public String getDeviceId() {
        return device_id;
    }
    public void setDeviceId(String device_id) {
        this.device_id = device_id;
    }

    public String getRoute() {return route;}
    public void setRoute(String route) {this.route = route;}

    public String getName() {return name;}
    public void setName(String name) {this.name = name;}

    public String getImage() {return image;}
    public void setImage(String image) {this.image = image;}

    public String getTripDate() {return trip_date;}
    public void setTripDate(String trip_date) {this.trip_date = trip_date;}

    public int getTripStatus() {return trip_status;}
    public void setTripStatus(int trip_status) {this.trip_status = trip_status;}

    public String getTripStatusText() {return trip_status_text;}
    public void setTripStatusText(String trip_status_text) {this.trip_status_text = trip_status_text;}

    public int getCaptain() {return captain;}
    public void setCaptain(int captain) {this.captain = captain;}

    public int getTripAccess() {return trip_access;}
    public void setTripAccess(int trip_access) {this.trip_access = trip_access;}

    public int getPaxTotalCount() {return pax_total_count;}
    public void setPaxTotalCount(int pax_total_count) {this.pax_total_count = pax_total_count;}

    public int getPaxBookedCount() {return pax_booked_count;}
    public void setPaxBookedCount(int pax_booked_count) {this.pax_booked_count = pax_booked_count;}

    public int getPaxConfirmedCount() {return pax_confirmed_count;}
    public void setPaxConfirmedCount(int pax_confirmed_count) {this.pax_confirmed_count = pax_confirmed_count;}
}
