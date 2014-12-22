package com.weiyuan.wifi_direct_file_client;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;


public class File_Client extends ActionBarActivity {

    private String ip = "192.168.49.1";
    private Socket socket;
    private int serverPort = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file__client);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_file__client, menu);
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

    public void connect(View view){
        Thread t = new Thread(new Connection());
        t.start();
    }

    class Connection implements Runnable {

        @Override
        public void run() {
            socket = new Socket();
            try {
                socket.connect((new InetSocketAddress(ip,serverPort)));

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void sencSmallFile(View view){
        if(socket!=null){
            String fileName ="/mnt/sdcard/BuildingCocoaApps.pdf";
            Thread t = new Thread(new FileClientThread(socket,fileName));
            t.start();
        }
    }
    public void sencLargeFile(View view){
        if(socket!=null){
            String fileName ="/mnt/sdcard/java.pdf";
            Thread t = new Thread(new FileClientThread(socket,fileName));
            t.start();
        }
    }

    class FileClientThread implements Runnable{

        private String fileName;                    //hard coded, would be change to user select
        private File file;
        private DataOutputStream dataOutputStream;
        private DataInputStream fileInputStream;
        private BufferedReader inPutStream;
        private Socket socket;

        public FileClientThread(Socket socket,String fileName){
            this.socket = socket;
            this.fileName = fileName;
            try {
                file = new File(fileName);
                dataOutputStream = new DataOutputStream(socket.getOutputStream());
                fileInputStream = new DataInputStream(new BufferedInputStream(new FileInputStream(file)));
            } catch (IOException e) {


                e.printStackTrace();
            }
        }

        @Override
        public void run() {

            byte[] buffer = new byte[8096];
            long passedlen = 0;
            try {
                dataOutputStream.writeUTF(file.getName());
                dataOutputStream.flush();
                dataOutputStream.writeLong(file.length());
                dataOutputStream.flush();

                int read = 0;

                while((read = fileInputStream.read(buffer)) != -1){
                    passedlen += read;
                    dataOutputStream.write(buffer,0,read);
                    dataOutputStream.flush();
                }

                while(true)
                {

                }



            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    dataOutputStream.close();
                    fileInputStream.close();
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }
}
