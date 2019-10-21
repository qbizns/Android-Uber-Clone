package com.tatx.partnerapp.pojos;

/**
 * Created by Home on 05-12-2016.
 */
public class TimeConvert {

    String days;
    String hours;
    String minutes;
    String seconds;


    public TimeConvert(String days, String hours, String minutes, String seconds) {
        this.days = days;
        this.hours = hours;
        this.minutes = minutes;
        this.seconds = seconds;
    }





    public String getMinutes() {
        return minutes;
    }

    public String getDays() {
        return days;
    }

    public String getHours() {
        return hours;
    }

    public String getSeconds() {
        return seconds;
    }




}
