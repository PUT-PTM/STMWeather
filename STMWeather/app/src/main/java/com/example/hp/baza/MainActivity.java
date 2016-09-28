package com.example.hp.baza;

import android.app.AlertDialog;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.database.Cursor;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.graphics.Typeface;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends Activity
{
    private StringBuilder sb = new StringBuilder();
    TextView t;
    TextView m;
    Button s;
    Button connectTest;
    int x = 0;
    int measurementNr = 0;
    byte[] buffer = new byte[1024];
    int bytes;
    int[] measurementValue = new int[4];
    private static final int REQUEST_ENABLE_BT = 1;
    final int RECIEVE_MESSAGE = 1;
    private String address;
    ZarzadcaBazy zb = new ZarzadcaBazy(this);
    Measurement me = new Measurement(1, Integer.toString(measurementValue[0]), Integer.toString(measurementValue[1]),
            intToBooleanString(measurementValue[2]), intToBooleanString(measurementValue[3]));
    Measurement theNewestMeasurement;
    Handler h;
    public String convertBuffer;
    private static final UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
    private BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
    private BluetoothSocket btSocket = null;
    public BluetoothDevice weatherStation = null;
    private ConnectedThread mConnectedThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        address = (String) getResources().getText(R.string.default_MAC);
        TextView tv_icon = (TextView) findViewById(R.id.weatherIcon);
        TextView tv_date = (TextView) findViewById(R.id.date);
        TextView tv_temperature = (TextView) findViewById(R.id.temperature);
        TextView tv_humidity = (TextView) findViewById(R.id.humidity);
        Typeface weatherFont = Typeface.createFromAsset(getAssets(), "fonts/weather.ttf");
        tv_icon.setTypeface(weatherFont);

        Button b_update = (Button) findViewById(R.id.update);
        Button b_statistics = (Button) findViewById(R.id.goToStatistics);
        Button b_info = (Button) findViewById(R.id.about_info);
        s = (Button)findViewById(R.id.btSwitch);
        connectTest = (Button)findViewById(R.id.connectTest);
        Typeface buttonFont = Typeface.createFromAsset(getAssets(), "fonts/FontAwesome.otf");
        b_update.setTypeface(buttonFont);
        b_statistics.setTypeface(buttonFont);
        b_info.setTypeface(buttonFont);
        s.setTypeface(buttonFont);

        t = (TextView)findViewById(R.id.bluetoothStatus);
        //m = (TextView)findViewById(R.id.messageStatus);

        if(theNewestMeasurement == null)
        {
            tv_temperature.setText("Make a new measurement");
        }
        else
        {
            theNewestMeasurement = zb.giveMeasurement(zb.giveAll().size());
            updateData(theNewestMeasurement);
        }


        //connectBtn();


        h = new Handler()
        {
            public void handleMessage(android.os.Message msg) {
               /* switch (msg.what)
                {
                    case RECIEVE_MESSAGE:													// if message is recieved
                        byte[] readBuf = (byte[]) msg.obj;
                        String strIncom = new String(readBuf, 0, msg.arg1);
                        x += msg.arg1;
                        Log.d("BT", "Int: " + x);
                        sb.append(strIncom);												// append string
                        //m.setText(strIncom);
                        int endOfLineIndex = sb.indexOf("\r\n");							// determine end of line
                        if (endOfLineIndex > 0) { 											// id end of line,
                            String sbprint = sb.substring(0, endOfLineIndex);
                            sb.delete(0, sb.length());                                        // slear sb
                            m.setText("Status: " + sbprint); 	        // update TextView
                            s.setEnabled(true);
                        }
                        Log.d("BT", "...String:"+ sb.toString() +  "Byte:" + msg.arg1 + "...");
                        break;
                }
            };
            */

                Log.d("BT", "Values: " + measurementValue[0] + measurementValue[1] +
                        measurementValue[2] + measurementValue[3]);
                byte[] readBuf = (byte[]) msg.obj;
                String strIncom = new String(readBuf, 0, msg.arg1);
                char character = strIncom.charAt(0);
                int bufferInt = (int) character;
                //m.append(strIncom);
                measurementValue[measurementNr] = bufferInt;
                Log.d("BT", "Value of : " + measurementNr + " is: " + measurementValue[measurementNr]);
                if(measurementNr == 3)
                {
                    me = new Measurement(1, Integer.toString(measurementValue[0]), Integer.toString(measurementValue[1]),
                            intToBooleanString(measurementValue[2]), intToBooleanString(measurementValue[3]));
                    zb.addMeasurement(me);

                    Measurement m = zb.giveMeasurement(zb.giveAll().size());
                    updateData(m);

                    Toast.makeText(getApplicationContext(), "Pomiar wykonany!", Toast.LENGTH_SHORT).show();
                }

                if(measurementNr < 3) measurementNr++;
                else measurementNr = 0;
            }

        };



        connectTest.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                connectTest.setEnabled(false);
                mConnectedThread.write("1");    // Send string via Bluetooth
                //Toast.makeText(getBaseContext(), "Try LED On", Toast.LENGTH_SHORT).show();
            }
        });

        b_update.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                s.setEnabled(false);
                mConnectedThread.write("2");    // Send string via Bluetooth
                //Toast.makeText(getBaseContext(), "Try LED On", Toast.LENGTH_SHORT).show();
            }
        });

    }


    public String intToBooleanString(int booleanInt)
    {
        if(booleanInt == 1)
        {
            return "true";
        }
        else
        {
            return "false";
        }
    }

    public void updateData(Measurement m)
    {
        TextView tv_icon = (TextView) findViewById(R.id.weatherIcon);
        TextView tv_date = (TextView) findViewById(R.id.date);
        TextView tv_temperature = (TextView) findViewById(R.id.temperature);
        TextView tv_humidity = (TextView) findViewById(R.id.humidity);
        //Button b_update=(Button)findViewById(R.id.update);

        tv_icon.setText(setIcon(m));
        tv_date.setText(m.getstringDate());
        //tv_date.setText(m.getDate());
        //String temperatureIcon = getString(R.string.weather_celsius);
        //String humidityIcon = getString(R.string.weather_humidity);
        tv_temperature.setText("Temperature: " + m.getTemperature());
        tv_humidity.setText("Humidity:       " + m.getHumidity());


    }

    public void goToStatistics(View view)
    {
        Intent i = new Intent(this, StatisticsActivity.class);
        startActivity(i);
    }

    public void displayAbout(View view)
    {
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


    public boolean runBluetooth()
    {
        boolean stan = true;
        if (btAdapter != null)
        {
            // Jeżeli mamy możliwość obsługi bluetooth
            if (!btAdapter.isEnabled())
            {
                // Jezeli modul bluetooth bl wylaczony wlaczamy go
                try
                {
                    // Usiłujemy włączyć bluetooth
                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                    Log.d("INFO","Bluetooth wlaczone");
                }
                catch (Exception ex)
                {
                    stan = false;
                }
            }
        }
        else stan = false;
        return stan;
    }

    public BluetoothDevice findWeatherStation()
    {
        Set<BluetoothDevice> pairedDevices = btAdapter.getBondedDevices();
        if (pairedDevices.size() > 0)
        {
            // Paired devices found
            for (BluetoothDevice device : pairedDevices)
            {
                if(device.getAddress().equals("20:14:07:01:15:79"))
                {
                    Log.d("INFO","Sparowano "+device.getAddress());
                    // Our HC-06 is paired
                    return btAdapter.getRemoteDevice(device.getAddress());
                    // This module is returned
                }
            }
        }
        return null;// If our station not found
    }
    public boolean connectToWeatherStation()
    {
        boolean stan = false;

        if(btSocket != null && btSocket.isConnected()) stan = true;
        else
        {
            try
            {
                btSocket = weatherStation.createInsecureRfcommSocketToServiceRecord(uuid);
                btSocket.connect();
                Log.d("INFO","Socket is " + btSocket.isConnected());

                if(btSocket.isConnected())
                {
                    // Connected to the weather station
                    stan = true;
                    Log.d("INFO","Connected");
                    mConnectedThread = new ConnectedThread(btSocket);
                    mConnectedThread.start();
                }
            }
            catch(IOException e)
            {
                Log.d("INFO",e.getMessage());
                // Not connected to weather station
                Log.d("INFO","Not connected");
            }
        }
        return stan;
    }


    public void connectBtn(View v)
    {

        //Switch s = (Switch)findViewById(R.id.btSwitch);
        TextView t = (TextView)findViewById(R.id.bluetoothStatus);

        if (btAdapter != null)
        {
            // The device supports Bluetooth
            Log.d("INFO","ba isn't null");
            if (t != null)
            {
                Log.d("INFO","t exists");
                // Controller exists
                if(runBluetooth() == true)
                {
                    Log.d("INFO","bluetooth = " + runBluetooth());
                    // HC-06 launched
                    weatherStation = findWeatherStation();
                    if(weatherStation != null && connectToWeatherStation() == true)
                    {
                        // Found a paired device and connected to it
                        t.setText("Connected");
                    }
                    else
                    {
                        t.setText("Not connected");
                    }
                }
            }
        }
    }




    private void errorExit(String title, String message)
    {
        Toast.makeText(getBaseContext(), title + " - " + message, Toast.LENGTH_LONG).show();
        finish();
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
        int Temperature = Integer.parseInt(m.getTemperature());
        String Describe = "";

        if(Sun.equals("true") && Rain.equals("false") && (Hour <= 17 && Hour > 7))
        {
            Describe = getString(R.string.weather_sunny);
        }
        else if(Temperature > 4 && Rain.equals("true") /*&& Pressure >= 1000*/ && (Hour < 17 && Hour > 7))
        {
            Describe = getString(R.string.weather_rainy);
        }
        else if(Temperature <= 4 && Rain.equals("true") && (Hour < 17 && Hour > 7))
        {
            Describe = getString(R.string.weather_snowy);
        }
        else if(Temperature > 20 && Rain.equals("true") && Humidity > 55 && (Hour < 17 && Hour > 7))
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



    //BLUETOOTH V2









    private class ConnectedThread extends Thread
    {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket)
        {
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the input and output streams, using temp objects because
            // member streams are final
            try
            {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
                Log.d("BT","In&Out available");
            }
            catch (IOException e) { }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
            Log.d("BT","Hmmmm In&Out");
        }

        public void run()
        {
            byte[] buffer = new byte[256];  // buffer store for the stream
            int bytes; // bytes returned from read()

            // Keep listening to the InputStream until an exception occurs
            while (true)
            {
                try
                {
                    // Read from the InputStream
                    bytes = mmInStream.read(buffer);
                    h.obtainMessage(RECIEVE_MESSAGE, bytes, -1, buffer).sendToTarget();
                }
                catch (IOException e)
                {
                    break;
                }
            }
        }

        /* Call this from the main activity to send data to the remote device */
        public void write(String message)
        {
            Log.d("BT", "...String to send: " + message + "...");
            byte[] msgBuffer = message.getBytes();
            try
            {
                mmOutStream.write(msgBuffer);
                Log.d("BT", "Sent!");
            }
            catch (IOException e)
            {
                Log.d("BT", "...Error send: " + e.getMessage() + "...");
            }
        }

        /* Call this from the main activity to shutdown the connection */
        public void cancel()
        {
            try
            {
                mmSocket.close();
            }
            catch (IOException e) { }
        }
    }





}
