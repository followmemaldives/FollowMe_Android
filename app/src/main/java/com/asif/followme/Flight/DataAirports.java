package com.asif.followme.Flight;

/**
 * Created by user on 12/18/2017.
 */

public class DataAirports {
    private String airportId  = "";
    private String nameAirport = "";
    private String codeIataAirport = "";
    private String latitude = "";
    private String longitude = "";
    private String timezone = "";
    private String codeIataCity = "";

    public String getAirportId() {
        return airportId;
    }
    public void setAirportId(String airportId) {this.airportId = airportId;}

    public String getNameAirport() {
        return nameAirport;
    }
    public void setNameAirport(String nameAirport) {this.nameAirport = nameAirport;}

    public String getCodeIataAirport() {
        return codeIataAirport;
    }
    public void setCodeIataAirport(String codeIataAirport) {this.codeIataAirport = codeIataAirport;}

    public String getLatitude() {
        return latitude;
    }
    public void setLatitude(String latitude) {this.latitude = latitude;}

    public String getLongitude() {
        return longitude;
    }
    public void setLongitude(String longitude) {this.longitude = longitude;}

    public String getTimezone() {
        return timezone;
    }
    public void setTimezone(String timezone) {this.timezone = timezone;}

    public String getCodeIataCity() {
        return codeIataCity;
    }
    public void setCodeIataCity(String codeIataCity) {this.codeIataCity = codeIataCity;}

}
