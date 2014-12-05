package com.mackwell.wifi_direct_server;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;


public class WifiP2PServer extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_p2p_server);
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

    public void startListening(View view)
    {
        Thread t = new Thread(test);
        t.start();
    }

    Runnable test = new Runnable() {
        @Override
        public void run() {

            byte[] buffer = new byte[100];
            String inputLine;

            try {
                ServerSocket serverSocket = new ServerSocket(8888);
                Socket client = serverSocket.accept();
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(client.getInputStream()));

                while ((inputLine = in.readLine()) != null) {
                }

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
            }

        }
    };



}
