package com.asif.followme.model;

public class DataPublicBoats
{	
	private String id  = "";
	private String name = "";
	private String value = "";	
	private String image = "";
	private String vdate = "";
	private int batt = 0;
	private String speed = "";
	private String islandspeed = "";
	private int marker = 0;
	private int notice = 0;
	private int fav = 0;
	private String fav_count ="0";
	private String island="";
	private String contact ="";

	public DataPublicBoats() {
	}

	public DataPublicBoats(String id, String value, String name, String image, String date, int batt, String speed, String islandspeed, int marker, int notice, int fav, String fav_count, String island, String contact) {
		this.id = id;
		this.value = value;
		this.name = name;
		this.image = image;
		this.vdate = date;
		this.batt = batt;
		this.speed = speed;
		this.islandspeed = islandspeed;
		this.marker = marker;
		this.notice = notice;
		this.fav = fav;
		this.fav_count=fav_count;
		this.island = island;
		this.contact = contact;
	}
	
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImage() {
		return this.image;
	}

	public void setImage(String image) {
		this.image = image;
	}
	public String getDate() {
		return this.vdate;
	}

	public void setDate(String date) {
		this.vdate = date;
	}
	
	public int getBatt() {
		return this.batt;
	}
	public void setBatt(int batt) {
		this.batt = batt;
	}
	
	public String getSpeed() {
		return this.speed;
	}
	public void setSpeed(String speed) {
		this.speed = speed;
	}
	public String getIslandSpeed() {return this.islandspeed;}
	public void setIslandSpeed(String islandspeed) {
		this.islandspeed = islandspeed;
	}
	public int getMarker() {
		return this.marker;
	}
	public void setMarker(int marker) {
		this.marker = marker;
	}
	public int getNotice() {
		return this.notice;
	}
	public void setNotice(int notice) {
		this.notice = notice;
	}
	public int getFav() {
		return this.fav;
	}
	public void setFav(int fav) {
		this.fav = fav;
	}

	public String getFavCount(){return this.fav_count;}
	public void setFavCount(String fav_count){this.fav_count = fav_count;}

	public String getIsland() {return this.island;}
	public void setIsland(String island) {
		this.island = island;
	}

	public String getContact() {return this.contact;}
	public void setContact(String contact) {
		this.contact = contact;
	}
}