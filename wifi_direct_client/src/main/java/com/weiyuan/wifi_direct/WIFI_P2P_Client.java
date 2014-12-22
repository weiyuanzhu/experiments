package com.weiyuan.wifi_direct;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URI;
import java.util.Enumeration;


public class WIFI_P2P_Client extends ActionBarActivity {

    private static final String TAG = "Client";

    private String ip = "192.168.49.1";
    private Socket socket;
    private PrintWriter out;
    private int port = 8888;

    private EditText editText;
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi__p2_p__client);

        editText = (EditText) findViewById(R.id.editText);
        mHandler = new Handler();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    /*        try {
                if(out != null){
                    out.close();
                }
                if(socket!=null) socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_wifi__p2_p__client, menu);
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


    public void connect(View view)
    {
        Thread t = new Thread(connect);
        t.start();
    }

    public void disconnect(View view)
    {
        try {
            if(out != null){
                out.close();
            }
            if(socket!=null) socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    Runnable connect = new Runnable() {
        @Override
        public void run() {
            socket = new Socket();

            DataOutputStream dataOutput;
            try {
                socket.connect((new InetSocketAddress(ip,port)),3000);
                out = new PrintWriter(socket.getOutputStream());
//                dataOutput = new DataOutputStream(socket.getOutputStream());
                String command = "test";
                out.println(command);
                out.flush();

            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    };

    public void send(View view) throws InterruptedException {
        Thread t = new Thread(send);
        t.start();
    }


    Runnable send = new Runnable() {
        @Override
        public void run() {
            if (socket!=null && socket.isConnected() && out!=null) {
                out.println(editText.getText());
                out.flush();
            }
            mHandler.post(clear);
        }
    };

    Runnable clear = new Runnable() {
        @Override
        public void run() {
            editText.setText("");
        }
    };

    public void sendFile(View view){

        //file open test

        Log.d(TAG,"file test");

        byte[] buf = new byte[1024];
        Context context = this.getApplicationContext();

        ContentResolver cr = context.getContentResolver();
        InputStream inputStream = null;

        String fileName ="/mnt/sdcard/BuildingCocoaApps.pdf";

        int len;
        try {
            Log.d(TAG, Environment.getDataDirectory().toString());
//            String uri = "/mnt/sdcard/BuildingCocoaApps.pdf";

//            Log.d(TAG, String.valueOf(Uri.parse("file://" + "/mnt/sdcard/BuildingCocoaApps.pdf")));

            File file = new File(fileName);
            FileInputStream fileInputStream = new FileInputStream(file);
            DataInputStream dataInputStream = new DataInputStream(new BufferedInputStream(new FileInputStream(file)));
            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());

            dataOutputStream.writeUTF(file.getName());
            dataOutputStream.flush();
            dataOutputStream.writeLong(file.length());
            dataOutputStream.flush();




//            inputStream = cr.openInputStream(Uri.parse("file:///mnt/sdcard/BuildingCocoaApps.pdf"));
//            Log.d(TAG, String.valueOf(inputStream.available()));
            /*while ((len = inputStream.read(buf)) != -1) {
                outputStream.write(buf, 0, len);
            }*/
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e){

        }


    }
}
