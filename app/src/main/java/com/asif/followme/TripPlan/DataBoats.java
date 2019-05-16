package com.asif.followme.TripPlan;

// this to be used for all spinner populations

public class DataBoats
{
	private int index;
	private int id  = 0;
	private String name = "";
	private int seats;

	public DataBoats(int ind, int id, String name, int seats) {
		this.index= index;
		this.id=id;
		this.name=name;
		this.seats = seats;
		// TODO Auto-generated constructor stub
	}
	public int getIndex() { return index; }
//	public void setIndex() {this.index = index;}

	public int getId() {
		return id;
	}
//	public void setId(int id) {
//		this.id = id;
//	}

	public String getName() {
		return name;
	}
//	public void setName(String name) {
//		this.name = name;
//	}

    public String toString() {	//Do not remove - important when populating spinners
        return name;
    }
	public int getSeats() {
		return seats;
	}
//	public void setSeata(int seats) {this.seats = seats;}
}