package com.asif.followme.Flight;


public class DataFlightSchedule {
    private String type  = "";
    private String status = "";
    private String iataCode = "";
    private String scheduledTime = "";
    private String estimatedRunway = "";
    private String actualRunway = "";
    private String airlineName = "";
    private String airlineCode = "";
    private String flightNumber = "";
    private String flightIataNumber = "";


    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public String getIataCode() {
        return iataCode;
    }
    public void setIataCode(String iataCode) {
        this.iataCode = iataCode;
    }

    public String getScheduledTime() {
        return scheduledTime;
    }
    public void setScheduledTime(String scheduledTime) {
        this.scheduledTime = scheduledTime;
    }

    public String getEstimatedRunway() {
        return estimatedRunway;
    }
    public void setEstimatedRunway(String estimatedRunway) {
        this.estimatedRunway = estimatedRunway;
    }

    public String getActualRunway() {
        return actualRunway;
    }
    public void setActualRunway(String actualRunway) {
        this.actualRunway = actualRunway;
    }

    public String getAirlineName() {
        return airlineName;
    }
    public void setAirlineName(String airlineName) {
        this.airlineName = airlineName;
    }

    public String getAirlineCode() {
        return airlineCode;
    }
    public void setAirlineCode(String airlineName) {
        this.airlineName = airlineName;
    }

    public String getFlightIataNumber() {
        return flightIataNumber;
    }
    public void setFlightIataNumber(String flightIataNumber) {
        this.flightIataNumber = flightIataNumber;
    }
 }
