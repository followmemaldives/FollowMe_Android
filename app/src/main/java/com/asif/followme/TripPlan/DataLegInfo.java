package com.asif.followme.TripPlan;

/**
 * Created by user on 12/18/2017.
 */

public class DataLegInfo {
    private int pax_id  = 0;
    private int trip_id = 0;
    private String island_from = "";
    private String island_to = "";
    private String leg_name = "";
    private int pax_qty = 0;



    public String getIslandFrom() {return island_from;}
    public void setIslandFrom(String island_from) {this.island_from = island_from;}

    public String getIslandTo() {return island_from;}
    public void setIslandTo(String island_to) {this.island_to = island_to;}

    public int getPaxQty() {return pax_qty;}
    public void setPaxQty(int pax_qty) {this.pax_qty = pax_qty;}

    public String getLegName() {return leg_name;}
    public void setLegName(String leg_name) {this.leg_name = leg_name;}
}
