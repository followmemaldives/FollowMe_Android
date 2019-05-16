package com.asif.followme.model;

/**
 * Created by user on 12/18/2017.
 */

public class DataWallet {
    private String id  = "";
    private String amount = "";
    private String date = "";
    private String ref = "";
    private String mobile = "";
    private int status = 0;
    private String device_id = "";
    private String desc ="";

//	private int fav = 0;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getAmount() {return amount; }
    public void setAmount(String amount) {this.amount = amount;}

    public String getRef() {
        return ref;
    }
    public void setRef(String ref) {
        this.ref = ref;
    }

    public String getDeviceID() {
        return device_id;
    }
    public void setDeviceID(String device_id) {
        this.device_id = device_id;
    }

    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }

    public int getStatus() {
        return status;
    }
    public void setStatus(int status) {
        this.status = status;
    }

    public String getMobile() {
        return mobile;
    }
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getDesc() {
        return desc;
    }
    public void setDesc(String desc) {
        this.desc = desc;
    }
}
