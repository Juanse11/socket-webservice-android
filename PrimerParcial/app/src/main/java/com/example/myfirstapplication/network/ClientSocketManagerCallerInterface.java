package com.example.myfirstapplication.network;

public interface ClientSocketManagerCallerInterface {
    void MessageReceived(String message);
    void ErrorFromSocketManager(Exception error);
}
