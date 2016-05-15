package com.example.hp.baza;

import java.util.LinkedList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class ZarzadcaBazy extends SQLiteOpenHelper{

    public ZarzadcaBazy(Context context) {
        super(context, "WeatherMeasurements.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(
                "create table Measurements(" +
                        "Nr integer primary key autoincrement," +
                        "Date text," +
                        "Temperature text," +
                        "Humidity text," +
                        "Pressure text," +
                        "Sun text," +
                        "Rain text);" +
                        "");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }


    public void addMeasurement(Measurement measurement){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        //values.put("Date", measurement.getDateInString());
        values.put("Date", measurement.getDate());
        values.put("Temperature",measurement.getTemperature());
        values.put("Humidity",measurement.getHumidity());
        values.put("Pressure",measurement.getPressure());
        values.put("Sun",measurement.getSun());
        values.put("Rain",measurement.getRain());
        db.insertOrThrow("Measurements", null, values);
    }

    public void deleteMeasurement(int id){
        SQLiteDatabase db = getWritableDatabase();
        String[] arguments={""+id};
        db.delete("Measurements", "Nr=?", arguments);
    }
/*
    public void deleteAll(){
        SQLiteDatabase db = getWritableDatabase();
        String[] arguments={""};
        db.delete("Measurements", "Nr=?", arguments);
    }
*/
    public static void deleteAll(SQLiteDatabase db){
        db.execSQL("DELETE Measurements;");
    }


   /* public void aktualizujKontakt(Measurement kontakt){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("Date", kontakt.getDate());
        values.put("Temperature",kontakt.getTemperature());
        values.put("Humidity",kontakt.getHumidity());
        values.put("Pressure",kontakt.getPressure());
        values.put("Sun",kontakt.getSun());
        values.put("Rain",kontakt.getRain());
        String args[]={kontakt.getNr()+""};
        db.update("Measurements", values,"Nr=?",args);
    }
*/
    public List<Measurement> giveAll(){
        List<Measurement> measurements = new LinkedList<Measurement>();
        String[] columns={"Nr","Date","Temperature","Humidity","Pressure","Sun","Rain"};
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor =db.query("Measurements",columns,null,null,null,null,null);
        while(cursor.moveToNext()){
            Measurement measurement = new Measurement();
            measurement.setNr(cursor.getInt(0));
            //measurement.setstringDate(cursor.getString(1));
            measurement.setDate(cursor.getString(1));
            measurement.setTemperature(cursor.getString(2));
            measurement.setHumidity(cursor.getString(3));
            measurement.setPressure(cursor.getString(4));
            measurement.setSun(cursor.getString(5));
            measurement.setRain(cursor.getString(6));
            measurements.add(measurement);
        }
        return measurements;
    }

    public Measurement giveMeasurement(int nr){
        Measurement measurement=new Measurement();
        SQLiteDatabase db = getReadableDatabase();
        String[] columns={"Nr","Date","Temperature","Humidity","Pressure","Sun","Rain"};
        String args[]={nr+""};
        Cursor cursor=db.query("Measurements",columns," Nr=?",args,null,null,null,null);
        if(cursor!=null){
            cursor.moveToFirst();
            measurement.setNr(cursor.getInt(0));
            //measurement.setstringDate(cursor.getString(1));
            measurement.setDate(cursor.getString(1));
            measurement.setTemperature(cursor.getString(2));
            measurement.setHumidity(cursor.getString(3));
            measurement.setPressure(cursor.getString(4));
            measurement.setSun(cursor.getString(5));
            measurement.setRain(cursor.getString(6));
        }
        return measurement;
    }



}