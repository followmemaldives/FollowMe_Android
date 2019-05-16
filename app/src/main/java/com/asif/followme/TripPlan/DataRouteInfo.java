package com.asif.followme.TripPlan;

import org.json.JSONObject;

/**
 * Created by user on 12/18/2017.
 */

public class DataRouteInfo {
    private int route_id  = 0;
    private String route_name = "";
    private String route_state = "";
    private JSONObject route_data = null;

    public DataRouteInfo(int route_id, String route_name) {
        this.route_id = route_id;
        this.route_name = route_name;
       this.route_data = route_data;
       this.route_state = route_state;


    }



        public int getRouteId() {return route_id;}
    public void setRouteId(int route_id) {this.route_id = route_id;}

    public String getRouteName() {return route_name;}
    public void setRouteName(String leg_name) {this.route_name = route_name;}

    public JSONObject getRouteData(){return route_data;}
    public void setRouteData(JSONObject route_data){this.route_data=route_data;}

    public String getRouteState() {return route_state;}
    public void setRouteState(String route_state) {this.route_state = route_state;}

    public String toString() {	//Do not remove - important when populating spinners
        return route_name;
    }

}
