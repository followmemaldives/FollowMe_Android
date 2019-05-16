package com.asif.followme.model;

public class Data_vtypes 
{
	private int ind;
	private int id  = 0;
	private String name = "";

	public Data_vtypes(int ind, int id, String name) {
		this.ind= ind;
		this.id=id;
		this.name=name;
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
}