package com.example.infiny.tracker_master.Models;

import java.io.Serializable;

/**
 * Created by infiny on 23/3/17.
 */

public class LogHours implements Serializable {
    String date;
    String hours;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHours() {
        return hours;
    }

    public void setHours(String hours) {
        this.hours = hours;
    }
}
