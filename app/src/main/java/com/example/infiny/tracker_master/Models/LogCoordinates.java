package com.example.infiny.tracker_master.Models;

import java.io.Serializable;

/**
 * Created by infiny on 23/3/17.
 */

public class LogCoordinates implements Serializable {
    String latitude;
    String longitude;
    String time;

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
