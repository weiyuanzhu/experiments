package com.mackwell.wifi_direct_server;

import android.app.Activity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class WifiP2PServer extends Activity {

    static final String TAG = "WIFI_direct_server";

    private TextView statusTextView;
    private TextView messageTextView;
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_p2p_server);

        statusTextView = (TextView) findViewById(R.id.textView);
        messageTextView = (TextView) findViewById(R.id.textView2);
        mHandler = new Handler();
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
            Socket clientSocket = null;

            byte[] buffer = new byte[100];
            String inputLine;

            try {
                ServerSocket serverSocket = new ServerSocket(8888);
                mHandler.post(new UpdateStatus("Server started listening"));

                while(true) {
                    mHandler.post(new UpdateStatus("Server waits for connection"));
                    clientSocket = serverSocket.accept();
                    mHandler.post(new UpdateStatus("Connection accepted" + clientSocket.getInetAddress() + ": " + clientSocket.getPort()));


                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(clientSocket.getInputStream()));

                    while (true) {
                        if ((inputLine = in.readLine()) != null) {
                            Log.d(TAG, "Text received: " + inputLine);
                            mHandler.post(new UpdateMessage(inputLine));
                            if(inputLine.equals("exit")){
                                mHandler.post(new UpdateStatus("Server waits for connection"));
                                break;
                            }
                        }
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            } finally {

                try {
                    if(clientSocket!=null) clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    };

    public void clear(View view){
        messageTextView.setText("Received message:");

    }


    class UpdateStatus implements Runnable{

        private String status;

        public UpdateStatus(String status){
            this.status = status;
        }
        @Override
        public void run() {
            statusTextView.setText(status);
        }
    }

    class UpdateMessage implements Runnable{

        private String message;

        public UpdateMessage(String status){
            this.message = status;
        }
        @Override
        public void run() {
            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm:ss");

            Date resultDate = new Date(System.currentTimeMillis());


            messageTextView.append("\n"+ sdf.format(resultDate) +":     " +  message);
        }
    }



}
