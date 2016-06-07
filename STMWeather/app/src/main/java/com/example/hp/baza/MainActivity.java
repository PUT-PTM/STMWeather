package com.example.hp.baza;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.database.Cursor;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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

        Measurement me = new Measurement(1,"23","61","1012","true","false");
        zb.addMeasurement(me);



        Measurement m = zb.giveMeasurement(zb.giveAll().size());
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
        tv_date.setText(m.getstringDate());
        //tv_date.setText(m.getDate());
        tv_temperature.setText(m.getTemperature());
        tv_humidity.setText(m.getHumidity());
        tv_pressure.setText(m.getPressure());

        Toast.makeText(getApplicationContext(), "Pomiar wykonany!", Toast.LENGTH_SHORT).show();
    }

    public void goToStatistics(View view) {
        Intent i = new Intent(this, StatisticsActivity.class);
        startActivity(i);
    }

    public void displayAbout(View view){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("ABOUT PROGRAM");
        alertDialogBuilder.setMessage(getString(R.string.weather_about_info));
        alertDialogBuilder.setCancelable(false);


        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                //Toast.makeText(MainActivity.this,"You clicked yes button",Toast.LENGTH_LONG).show();
            }
        });
/*
        alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
*/
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
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
        String DateAndHour = m.getstringDate();
        int Hour = Integer.parseInt(m.getstringDate().substring(11, 13));
        int Humidity = Integer.parseInt(m.getHumidity());
        int Pressure = Integer.parseInt(m.getPressure());
        int Temperature = Integer.parseInt(m.getTemperature());
        String Describe = "";

        if(Sun.equals("true") && Rain.equals("false") && (Hour < 17 && Hour > 7))
        {
            Describe = getString(R.string.weather_sunny);
        }
        else if(Temperature > 4 && Rain.equals("true") && Pressure >= 1000 && (Hour < 17 && Hour > 7))
        {
            Describe = getString(R.string.weather_rainy);
        }
        else if(Temperature <= 4 && Rain.equals("true") && (Hour < 17 && Hour > 7))
        {
            Describe = getString(R.string.weather_snowy);
        }
        else if(Temperature > 20 && Rain.equals("true") && Pressure < 1000 && Humidity > 55 && (Hour < 17 && Hour > 7))
        {
            Describe = getString(R.string.weather_thunder);
        }
        else if(Sun.equals("false") && Rain.equals("false") && (Hour < 17 && Hour > 7))
        {
            Describe = getString(R.string.weather_cloudy);
        }
        else if(Rain.equals("false") && (Hour > 17 || Hour <7))
        {
            Describe = getString(R.string.weather_clear_night);
        }

        return Describe;
    }

}
