package com.example.myfirstapplication.broadcast;

public interface NetworkStateBroadcastManagerCallerInterface {

    void onNetworkStatusChange( String type, String message);

    void ErrorAtBroadcastManager(Exception error);
}
