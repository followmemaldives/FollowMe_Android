package com.asif.followme.MyAccount;

/**
 * Created by user on 12/18/2017.
 */

public class DataMyGroups {
    private String id  = "";
    private String name = "";
    private String value = "";
    private String image = "";
    private String vdate = "";
    private String count = "";
    private String speed = "";
    private String island = "";
    private int marker = 0;
    private int notice = 0;
//	private int fav = 0;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getCount() {
        return count;
    }
    public void setCount(String count) {
        this.count = count;
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

    public int getNotice() {
        return notice;
    }
    public void setNotice(int notice) {
        this.notice = notice;
    }
}
