package com.example.hp.baza;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.database.Cursor;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.graphics.Typeface;
import android.widget.Toast;

import java.util.Date;

public class MainActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView tv_icon = (TextView) findViewById(R.id.weatherIcon);
        TextView tv_date = (TextView) findViewById(R.id.date);
        TextView tv_temperature = (TextView) findViewById(R.id.temperature);
        TextView tv_humidity = (TextView) findViewById(R.id.humidity);
        TextView tv_pressure = (TextView) findViewById(R.id.pressure);
        Typeface weatherFont = Typeface.createFromAsset(getAssets(), "fonts/weather.ttf");
        tv_icon.setTypeface(weatherFont);
        Button b_update = (Button) findViewById(R.id.update);
        Button b_statistics = (Button) findViewById(R.id.goToStatistics);
        Button b_info = (Button) findViewById(R.id.about_info);



        ZarzadcaBazy zb = new ZarzadcaBazy(this);
        //tv.setText("");

      //  Measurement me = new Measurement(6,"14-05-2016 21:04:32","-2","80","1013","false","true");
      //  zb.addMeasurement(me);



        Measurement m = zb.giveMeasurement(1);
        updateData(m);
    }

    public void updateData(Measurement m) {
        TextView tv_icon = (TextView) findViewById(R.id.weatherIcon);
        TextView tv_date = (TextView) findViewById(R.id.date);
        TextView tv_temperature = (TextView) findViewById(R.id.temperature);
        TextView tv_humidity = (TextView) findViewById(R.id.humidity);
        TextView tv_pressure = (TextView) findViewById(R.id.pressure);
        //Button b_update=(Button)findViewById(R.id.update);

        tv_icon.setText(setIcon(m));
        //tv_date.setText(m.getDateInString());
        tv_date.setText(m.getDate());
        tv_temperature.setText(/*getString(R.string.weather_thermometr) + " " + */m.getTemperature() + getString(R.string.weather_degree) + "C");
        tv_humidity.setText(m.getHumidity() + " %");
        tv_pressure.setText(m.getPressure() + " hPa");

        Toast.makeText(getApplicationContext(), "Pomiar wykonany!", Toast.LENGTH_SHORT).show();
    }

    public void goToStatistics(View view) {
        Intent i = new Intent(this, StatisticsActivity.class);
        startActivity(i);
    }
/*
    @Override
    protected Dialog onCreateDialog()
    {
        return displayAbout();
    }

    public Dialog displayAbout()
    {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle("Dialog title");
        dialogBuilder.setMessage("Dialog content text...");
        return dialogBuilder.create();
    }
*/
    public String setIcon(Measurement m)
    {
        String Sun = m.getSun();
        String Rain = m.getRain();
        int Pressure = Integer.parseInt(m.getPressure());
        int Temperature = Integer.parseInt(m.getTemperature());
        String Describe = "";

        if(Sun.equals("true") && Rain.equals("false"))
        {
            Describe = getString(R.string.weather_sunny);/*"Swieci Slonce!";*/
        }
        else if(/*Sun.equals("false")*/ Temperature > 4 && Rain.equals("true"))
        {
            Describe = getString(R.string.weather_rainy);
        }
        else if(/*Sun.equals("false")*/ Temperature < 4 && Rain.equals("true"))
        {
            Describe = getString(R.string.weather_snowy);
        }
        else if(Rain.equals("true") && Pressure > 1020)
        {
            Describe = getString(R.string.weather_thunder);
        }
        else if(Sun.equals("false") && Rain.equals("false"))
        {
            Describe = getString(R.string.weather_cloudy);
        }
        /*
        else if()
        {

        }
        else if()
        {

        }
        else if()
        {

        }
        */
        return Describe;
    }

}
