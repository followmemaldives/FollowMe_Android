package com.asif.followme.model;

public class DataETA
{
	private int ind;
	private int id  = 0;
	private String name = "";
	private String lat = "";
	private String lon = "";

	public DataETA(int ind, int id, String name, String lat, String lon) {
		this.ind= ind;
		this.id=id;
		this.name=name;
		this.lat = lat;
		this.lon = lon;
		// TODO Auto-generated constructor stub
	}
	public int getIndex() { return ind; }
	public void setIndex() {this.ind = ind;}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
    public String toString() {
        return name;
    }

	public String getLat() {
		return lat;
	}
	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getLon() {
		return lon;
	}
	public void setLon(String lon) {
		this.lon = lon;
	}

}