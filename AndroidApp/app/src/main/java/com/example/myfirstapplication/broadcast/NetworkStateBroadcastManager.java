package com.example.myfirstapplication.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.widget.Toast;

public class NetworkStateBroadcastManager extends BroadcastReceiver {

    private Context context;
    private String channel;
    private BroadcastManagerCallerInterface caller;

    public NetworkStateBroadcastManager(Context context,
                                        String channel,
                                        BroadcastManagerCallerInterface caller) {
        this.context = context;
        this.channel = channel;
        this.caller = caller;
        initializeBroadcast();
    }

    public void initializeBroadcast(){
        try{
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
            intentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
            context.registerReceiver(this,intentFilter);
        }catch (Exception error){
            caller.ErrorAtBroadcastManager(error);
        }
    }

    public void unRegister(){
        try{
            context.unregisterReceiver(this);
        }catch (Exception error){
            caller.ErrorAtBroadcastManager(error);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        final ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        final NetworkInfo activeNetwork = connMgr.getActiveNetworkInfo();
        if(activeNetwork != null && activeNetwork.isConnected())
        {
            Toast.makeText(context, "Online", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "Offline", Toast.LENGTH_SHORT).show();
        }


    }

}
