package com.example.hp.baza;

/**
 * Created by HP on 2016-09-11.
 */

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Set;
import java.util.UUID;

public class BluetoothService extends Activity
{
    /* NIEZBĘDNE DLA TRANSMISJI BLUETOOTH */

    public final static int REQUEST_ENABLE_BT = 1;
    public BluetoothAdapter ba = BluetoothAdapter.getDefaultAdapter();
    UUID MY_UUID = UUID.fromString("550e8400-a12b-41d4-a716-446655449876");
    public BluetoothDevice weatherStation = null;
    public BluetoothSocket btSocket = null;
    public DataOutputStream outStream = null;

    public boolean runBluetooth()
    {

        boolean stan = true;

        if (ba != null)
        {
            // Jeżeli mamy możliwość obsługi bluetooth

            if (!ba.isEnabled())
            {
                // Jezeli modul bluetooth bl wylaczony wlaczamy go

                try
                {
                    // Usiłujemy włączyć bluetooth
                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);

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
        Set<BluetoothDevice> pairedDevices = ba.getBondedDevices();
        if (pairedDevices.size() > 0)
        {
            // Znaleziono sparowane urzadzenia
            for (BluetoothDevice device : pairedDevices)
            {
                if(device.getAddress().equals("20:14:07:01:15:79"))
                {
                    // Wsrod znalezionych sparowanych urzadzen jest nasz modul bluetooth HC-06
                    return ba.getRemoteDevice(device.getAddress());
                    // Zwrocono uchwyt do znalezionego stacji w celu pozniejszego polaczenia z nia
                }
            }
        }
        return null;// W przypadku braku dostepnosci stacji
    }
    public boolean connectToWeatherStation()
    {
        boolean stan = false;

        if(btSocket != null && btSocket.isConnected()) stan = true;
        else
        {
            try
            {
                btSocket = weatherStation.createInsecureRfcommSocketToServiceRecord(MY_UUID);
                outStream = new DataOutputStream(btSocket.getOutputStream());
                btSocket.connect();

                if(btSocket.isConnected())
                {
                    // Udało się nawiązać połączenie ze stacja
                    stan = true;

                }
            }
            catch(IOException e)
            {
                // Nie udało nam się nawiązać połączenia ze stacja
            }
        }
        return stan;
    }



}
