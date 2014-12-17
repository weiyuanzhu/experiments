package com.mackwell.wifi_direct_file_server;

import android.app.ActionBar;
import android.app.Activity;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DecimalFormat;


public class FileServer extends Activity {

    private ProgressBar progressBar;
    private TextView textView;
    private TextView statusTextView;
    private TextView speedTextView;

    private Handler mHandler;

    private long passedlen = 0;
    private long len = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_server);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setMax(100);

        textView = (TextView) findViewById(R.id.textView2);
        statusTextView = (TextView) findViewById(R.id.textView);
        speedTextView = (TextView) findViewById(R.id.textView3);


        mHandler = new Handler();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_file_server, menu);
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

    public void startServer(View view){
        Thread fileServerThread = new Thread(new FileServerThread());
        fileServerThread.start();
    }

    class FileServerThread implements Runnable{

        static final int serverPort = 9001;





        private ServerSocket serverSocket = null;
        private Socket clientSocket = null;
        private DataInputStream dis = null;
        private DataOutputStream fos = null;

        FileServerThread(){
            try {
                serverSocket = new ServerSocket(serverPort);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        @Override
        public void run() {

            int bufferSize = 8096;
            byte[] buffer = new byte[bufferSize];      //buffer

            int read = 0;
            String fileName;


            while(true) {

                String filePath = "/mnt/sdcard/download/";


                try {
                    mHandler.post(new UpdateStatus("Server listening"));
                    Log.d("FileServer","---------------start listening------------");
                    clientSocket = serverSocket.accept();

                    mHandler.post(new UpdateStatus("Connection accepted " + clientSocket.getInetAddress() + ": " + clientSocket.getPort()));

                    passedlen = 0;
                    len = 0;
                    long beginTime = 0;

                    Log.d("FileServer","Connection accepted " + clientSocket.getInetAddress() + ": " + clientSocket.getPort()) ;

                    dis = new DataInputStream(new BufferedInputStream(clientSocket.getInputStream()));
                    filePath +=  System.currentTimeMillis() + "_" + dis.readUTF();
                    len = dis.readLong();
                    Log.d("FileServer","File path: " + filePath + "File bytes: " + len);



                    fos = new DataOutputStream(new BufferedOutputStream(new BufferedOutputStream(new FileOutputStream(filePath))));

                    while((read = dis.read(buffer))!=-1){

                        if(passedlen==0){
                            beginTime = System.nanoTime();
                            Log.d("FileServer", "Upload started");
                        }

                        passedlen += read;
                        mHandler.post(updateProgressBar);
                        mHandler.post(updateTextView);

//                        Log.d("FileServer","Received bytes: " + passedlen);
                        fos.write(buffer, 0, read);
                        if(passedlen == len) {

                            DecimalFormat df = new DecimalFormat("##0.000");

                            long timeElapsed = System.nanoTime() - beginTime;
                            Log.d("FileServer", "Time elapsed: " + timeElapsed / 1e9 + "s");

                            Double rate = len/(timeElapsed/1e9);
                            Double MBs = rate/1e6;
                            Double Mbs = MBs * 8;

                            Log.d("FileServer", "Received bytes: " + passedlen);

                            String speed = "Transfer rate: " + df.format(Mbs) + " Mbps (" + len + " bytes in " + df.format(timeElapsed / 1e9) + "s)";
                            Log.d("FileServer", speed);
                            mHandler.post(new UpdateSpeed(speed));
                            break;
                        }
                    }

                    fos.close();

                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (clientSocket != null) try {
                        clientSocket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }


        }
    }

    Runnable updateProgressBar = new Runnable() {

        @Override
        public void run() {
            double n = (double)passedlen/len;
            int percentage = (int) (n * 100);
            progressBar.setProgress(percentage);
        }
    };

    Runnable updateTextView = new Runnable() {
        @Override
        public void run() {
            String s = Long.toString(passedlen) + "/" + Long.toString(len) + " Bytes";
            textView.setText(s);

        }
    };

    class UpdateStatus implements Runnable {

        private String status;

        public UpdateStatus(String status){
            this.status = status;
        }
        @Override
        public void run() {
            statusTextView.setText(status);
        }
    };

   class  UpdateSpeed implements Runnable {

       private String speed;

       public UpdateSpeed(String speed){
           this.speed = speed;
       }

        @Override
        public void run() {
            speedTextView.setText(speed);
        }
    }

}
