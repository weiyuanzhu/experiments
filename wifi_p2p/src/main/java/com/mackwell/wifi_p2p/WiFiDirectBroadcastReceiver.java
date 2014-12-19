package com.mackwell.wifi_p2p;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by weiyuan zhu on 04/12/14.
 */
public class WiFiDirectBroadcastReceiver extends BroadcastReceiver {

    static final String TAG = "BroadcastReceiver";

    private WifiP2pManager mManager;
    private WifiP2pManager.Channel mChannel;
    private MainActivity mActivity;

    private List peers = new ArrayList();

    private boolean connectionFlag = true;

    private WifiP2pManager.PeerListListener mPeerListListener = new WifiP2pManager.PeerListListener() {
        @Override
        public void onPeersAvailable(WifiP2pDeviceList peerList) {

            // Out with the old, in with the new.
            Log.d(TAG,"Peer found");
            peers.clear();
            peers.addAll(peerList.getDeviceList());
            mActivity.setPeers(peers);

            // If an AdapterView is backed by this data, notify it
            // of the change.  For instance, if you have a ListView of available
            // peers, trigger an update.

        }
    };


    public WiFiDirectBroadcastReceiver(WifiP2pManager manager, WifiP2pManager.Channel channel,
                                       MainActivity activity) {
        super();
        this.mManager = manager;
        this.mChannel = channel;
        this.mActivity = activity;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
//        WifiP2pDevice device = intent.getParcelableExtra(WifiP2pManager.EXTRA_WIFI_P2P_DEVICE);



        if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
            Log.d(TAG,"state_changed");

            int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
            if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {
                // Wifi P2P is enabled
            } else {
                // Wi-Fi P2P is not enabled
            }
            // Check to see if Wi-Fi is enabled and notify appropriate activity
            // request available peers from the wifi p2p manager. This is an
            // asynchronous call and the calling activity is notified with a
            // callback on PeerListListener.onPeersAvailable()


        } else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {
            // Call WifiP2pManager.requestPeers() to get a list of current peers
            Log.d(TAG,"peers_changed");
            if (mManager != null) {
                mManager.requestPeers(mChannel, mPeerListListener);
                Log.d(TAG, "p2p state changed, looking for peers");

            }
        } else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {

            Log.d(TAG, "connection_changed");
            connectionFlag = !connectionFlag;
            if(connectionFlag == false){
                Log.d(TAG,"Device disconnected");
                Toast.makeText(mActivity,"Device not connected",Toast.LENGTH_SHORT).show();
            }else {
                Log.d(TAG,"Device connected");
                Toast.makeText(mActivity,"Device connected",Toast.LENGTH_SHORT).show();

                mManager.requestConnectionInfo(mChannel,new WifiP2pManager.ConnectionInfoListener() {
                    @Override
                    public void onConnectionInfoAvailable(WifiP2pInfo wifiP2pInfo) {
                        Toast.makeText(mActivity,"Group owner IP: " + wifiP2pInfo.groupOwnerAddress + "Is Group owner: " + wifiP2pInfo.isGroupOwner, Toast.LENGTH_SHORT).show();
                    }
                });
            }

            // Respond to new connection or disconnections
        } else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {
            Log.d(TAG,"this_device_changed");

            // Respond to this device's wifi state changing


        }
    }


}
