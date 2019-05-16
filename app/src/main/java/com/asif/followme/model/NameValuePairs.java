package com.asif.followme.model;

// this to be used for all spinner populations

public class NameValuePairs
{
	private int index;
	private int id  = 0;
	private String name = "";

	public NameValuePairs(int ind, int id, String name) {
		this.index= index;
		this.id=id;
		this.name=name;
		// TODO Auto-generated constructor stub
	}
	public int getIndex() { return index; }
	public void setIndex() {this.index = index;}

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
}