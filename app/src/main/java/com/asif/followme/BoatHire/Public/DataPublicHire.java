package com.asif.followme.BoatHire.Public;

/**
 * Created by user on 12/18/2017.
 */

public class DataPublicHire {
    private String id  = "";
    private String name = "";
    private String description = "";
    private String image = "";
    private String bids = "0";
    private String status="0";
    private int hire_status = 0;
    private String status_text ="";
    private String vdate = "";
    private String island = "";
    private String hire_date = "";
    private int marker = 0;
    private int notice = 0;
    private int nav =0;
    private String time_ago ="";
    private int bid_status =0;
    private String bid_status_text="";

//	private int fav = 0;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {return description; }

    public void setDescription(String description) {
        this.description = description;
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

    public String getBids() {
        return bids;
    }
    public void setBids(String bids) {
        this.bids = bids;
    }

/*    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
*/
    public int getHireStatus() {
        return hire_status;
    }
    public void setHireStatus(int status) {
        this.hire_status = status;
    }

    public String getStatusText() {
        return status_text;
    }
    public void setStatusText(String status_text) {
        this.status_text = status_text;
    }

    public String getBidStatusText() {
        return bid_status_text;
    }
    public void setBidStatusText(String bid_status_text) {
        this.bid_status_text = bid_status_text;
    }

    public int getBidStatus() {
        return bid_status;
    }
    public void setBidStatus(int bid_status) {
        this.bid_status = bid_status;
    }

    public int getNav() {
        return nav;
    }
    public void setNav(int nav) {
        this.nav = nav;
    }

    public String getTimeAgo() {
        return time_ago;
    }
    public void setTimeAgo(String time_ago) {
        this.time_ago = time_ago;
    }

    public String getHireDate() {
        return hire_date;
    }
    public void setHireDate(String hire_date) {
        this.hire_date = hire_date;
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
}
