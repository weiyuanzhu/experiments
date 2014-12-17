package com.mackwell.wifi_p2p;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.WpsInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends ActionBarActivity {

    static final String TAG = "MainActivity";

    ListView peerListView;
    ArrayAdapter<String> mAdapter;
    SimpleAdapter mSimpleAdapter;
    List<String> peerNames;
    List<Map<String,String>> mDataList;

    WifiP2pManager mManager;
    WifiP2pManager.Channel mChannel;
    BroadcastReceiver mReceiver;
    IntentFilter mIntentFilter;

    private List<WifiP2pDevice> peers;




    public void setPeers(List peers) {
        this.peers = peers;
        peerNames.clear();

        for(int i =0; i<peers.size();i++)
        {
            WifiP2pDevice device = (WifiP2pDevice) peers.get(i);
            peerNames.add(i, device.deviceName);
        }


        updatemDataList();
        mSimpleAdapter.notifyDataSetChanged();
//        mSimpleAdapter = new SimpleAdapter(this, mDataList,android.R.layout.simple_list_item_activated_2,new String[]{"deviceName","deviceStatus"},new int[]{android.R.id.text1,android.R.id.text2});
//        peerListView.setAdapter(mSimpleAdapter);

        Log.d(TAG, peers.toString());
    }

    private void updatemDataList(){
//        List dataList = new ArrayList<Map<String,String>>();
        if(mDataList==null){
            mDataList = new ArrayList<Map<String, String>>();
        }else   mDataList.clear();

        Map map;

        /*map = new HashMap<String,String>();

        map.put("deviceName","test1");
        map.put("deviceStatus","1");
        mDataList.add(map);

        map = new HashMap<String,String>();

        map.put("deviceName","test2");
        map.put("deviceStatus","2");
        mDataList.add(map);*/

        for(int i=0; i<peers.size();i++){

            map = new HashMap<String,String>();
            WifiP2pDevice  device = (WifiP2pDevice) peers.get(i);
            map.put("deviceName",device.deviceName);
            switch(device.status){
                case 0: map.put("deviceStatus","CONNECTED");
                    break;
                case 1: map.put("deviceStatus","INVITED");
                    break;
                case 2: map.put("deviceStatus","FAILED");
                    break;
                case 3: map.put("deviceStatus","AVAILABLE");
                    break;
                case 4: map.put("deviceStatus","UNAVAILABLE");
                    break;
                default: break;

            }
            mDataList.add(map);
        }


    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        peerListView = (ListView) findViewById(R.id.peerListView);
        peerNames = new ArrayList<String>();
        peers = new ArrayList<WifiP2pDevice>();
        updatemDataList();

        mAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_single_choice,peerNames);
        mSimpleAdapter = new SimpleAdapter(this, mDataList,android.R.layout.simple_list_item_activated_2,new String[]{"deviceName","deviceStatus"},new int[]{android.R.id.text1,android.R.id.text2});

        peerListView.setAdapter(mSimpleAdapter);
        peerListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        peerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                peerListView.setItemChecked(position,true);
                peerListView.setSelection(position);
            }
        });

        mManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        mChannel = mManager.initialize(this, getMainLooper(), null);
        mReceiver = new WiFiDirectBroadcastReceiver(mManager, mChannel, this);

        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mReceiver, mIntentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mReceiver);
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

    public void discoverPeer(View view)
    {
        peers.clear();
        mDataList.clear();
        peerNames.clear();
        mAdapter.notifyDataSetChanged();
        mSimpleAdapter.notifyDataSetChanged();

        mManager.discoverPeers(mChannel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                // Code for when the discovery initiation is successful goes here.
                // No services have actually been discovered yet, so this method
                // can often be left blank.  Code for peer discovery goes in the
                // onReceive method, detailed below.
                Log.d(TAG,"Discovery in progress");
            }

            @Override
            public void onFailure(int reasonCode) {
                // Code for when the discovery initiation fails goes here.
                // Alert the user that something went wrong.
            }
        });
    }

    public void connect(View view)
    {

        // Picking the first device found on the network.
        int position = peerListView.getCheckedItemPosition();
        WifiP2pDevice device = (WifiP2pDevice) peers.get(position);


        if (device!=null) {
            WifiP2pConfig config = new WifiP2pConfig();
            config.deviceAddress = device.deviceAddress;
            config.wps.setup = WpsInfo.PBC;

            mManager.connect(mChannel, config, new WifiP2pManager.ActionListener() {

                @Override
                public void onSuccess() {
                    // WiFiDirectBroadcastReceiver will notify us. Ignore for now.
                }

                @Override
                public void onFailure(int reason) {
                    Toast.makeText(MainActivity.this, "Connect failed. Retry.",
                            Toast.LENGTH_SHORT).show();
                }
            });

//            mManager.stopPeerDiscovery(mChannel, new WifiP2pManager.ActionListener() {
//                @Override
//                public void onSuccess() {
//                    // Code for when the discovery initiation is successful goes here.
//                    // No services have actually been discovered yet, so this method
//                    // can often be left blank.  Code for peer discovery goes in the
//                    // onReceive method, detailed below.
//                    Log.d(TAG,"Discovery stopped");
//                }
//
//                @Override
//                public void onFailure(int reasonCode) {
//                    // Code for when the discovery initiation fails goes here.
//                    // Alert the user that something went wrong.
//                }
//            });
        }
    }

    public void disconnect(View view)
    {
//        mManager.dis
    }



}
