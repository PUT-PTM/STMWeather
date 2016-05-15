package com.example.hp.baza;

import android.graphics.Typeface;
import android.widget.TextView;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Measurement {
    private int nr;
    //private Date date;
    private String date;
    private String temperature;
    private String humidity;
    private String pressure;
    private String sun;
    private String rain;
    private String stringDate;
    //

    public Measurement(){}

    public Measurement(int nr, String date, String temperature, String humidity, String pressure, String sun, String rain)
    {
        this.nr=nr;
        //this.date=new Date();
        this.date=date;
        this.temperature=temperature;
        this.humidity=humidity;
        this.pressure=pressure;
        this.sun=sun;
        this.rain=rain;
    }

    public int getNr() {return nr;}
    public void setNr(int nr) {this.nr = nr;}
    //public Date getDate() {return date;}
    //public void setDate(Date date) {this.date = date;}
    public String getDate() {return date;}
    public void setDate(String date) {this.date = date;}
    public String getTemperature() {return temperature;}
    public void setTemperature(String temperature) { this.temperature = temperature;}
    public String getHumidity() { return humidity;}
    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }
    public String getPressure() {
        return pressure;
    }
    public void setPressure(String pressure) {
        this.pressure = pressure;
    }
    public String getSun() {return sun;}
    public void setSun(String sun) {
        this.sun = sun;
    }
    public String getRain() {
        return rain;
    }
    public void setRain(String rain) {
        this.rain = rain;
    }
    public String getDateInString()
    {
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String dateString = df.format(date);
        return dateString;
    }
    public void setstringDate(String stringDate)
    {
        this.stringDate=stringDate;
    }
/*
    public String setIcon()
    {
        if(this.sun.equals("true") && this.rain.equals("true"))
        {

            returned= getActivity().getString;"Pada deszcz!";
        }
        else if(this.sun.equals("true") && this.rain.equals("false"))
        {

            returned= "Słońce świeci!";
        }
        else if(this.sun.equals("false") && this.rain.equals("true"))
        {

            returned= "Pada śnieg!";
        }
        return returned;
    }
*/
}