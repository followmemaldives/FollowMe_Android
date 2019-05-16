package com.asif.followme.model;

/**
 * Created by user on 12/18/2017.
 */

public class DataLogs {

    private String name = "";
    private String xtime = "";
    private String status = "";
    private String desc = "";
    private int isHeader =0;



    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return xtime;
    }
    public void setTime(String xtime) {
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
}
