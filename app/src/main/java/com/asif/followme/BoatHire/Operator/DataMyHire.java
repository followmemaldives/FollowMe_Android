package com.asif.followme.BoatHire.Operator;

/**
 * Created by user on 12/18/2017.
 */

public class DataMyHire {
    private String id  = "";
    private String name = "";
    private String desc1 = "";
    private String desc2 = "";
    private String image = "";
    private String bids = "0";
    private int status=0;
    private String status_text ="";
    private String hire_date = "";
    private String request_by = "";
    private String time_ago = "";
    private int my_bid_count = 0;
    private int my_win_count = 0;
    private int my_accept_count = 0;

//	private int fav = 0;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getDesc1() {return desc1; }
    public void setDesc1(String desc1) {
        this.desc1 = desc1;
    }

    public String getDesc2() {return desc2; }
    public void setDesc2(String desc2) {
        this.desc2 = desc2;
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

    public String getHireDate() {return hire_date;}
    public void setHireDate(String hire_date) {this.hire_date = hire_date;}

    public String getBids() {
        return bids;
    }
    public void setBids(String bids) {
        this.bids = bids;
    }

    public int getStatus() {
        return status;
    }
    public void setStatus(int status) {
        this.status = status;
    }

    public String getStatusText() {
        return status_text;
    }
    public void setStatusText(String status_text) {
        this.status_text = status_text;
    }

//    public String getRequestBy() {
//        return request_by;
//    }
//    public void setRequestBy(String request_by) {
//        this.request_by = request_by;
//    }

    public String getTimeAgo() {
        return time_ago;
    }
    public void setTimeAgo(String time_ago) {
        this.time_ago = time_ago;
    }

    public int getMyBidCount() {
        return my_bid_count;
    }
    public void setMyBidCount(int my_bid_count) {
        this.my_bid_count = my_bid_count;
    }

    public int getMyWinCount() {
        return my_win_count;
    }
    public void setMyWinCount(int my_win_count) {
        this.my_win_count = my_win_count;
    }

    public int getMyAcceptCount() {
        return my_accept_count;
    }
    public void setMyAcceptCount(int my_accept_count) {
        this.my_accept_count = my_accept_count;
    }
}
