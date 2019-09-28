package com.example.myfirstapplication.broadcast;

public interface NetworkStateBroadcastManagerCallerInterface {

    void MessageReceivedThroughBroadcastManager(
            String channel, String type, String message);

    void ErrorAtBroadcastManager(Exception error);
}
