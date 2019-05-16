package com.asif.followme.model;

/**
 * Created by user on 12/18/2017.
 */

public class DataGuardLogs {

    private String name = "";
    private String xtime = "";
    private String status = "";
    private String desc = "";
    private int isHeader =0;
    private String title = "";
    private String time_in = "";
    private String loc_name = "";



    public String getName() {
        return name;
    }
    public void setName(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getTimeIn() {
        return time_in;
    }
    public void setTimeIn(String time_in) {
        this.time_in = time_in;
    }

    public String getTimeOut() {
        return xtime;
    }
    public void setTimeOut(String xtime) {
        this.xtime = xtime;
    }

    public String getDesc() {
        return desc;
    }
    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public int getIsHeader() {
        return isHeader;
    }
    public void setIsHeader(int isHeader) {
        this.isHeader = isHeader;
    }

    public String getLocationName() {
        return loc_name;
    }
    public void setLocationName(String loc_name) {
        this.loc_name = loc_name;
    }
}
