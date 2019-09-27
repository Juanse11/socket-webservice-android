package com.example.myfirstapplication.gps;

import android.location.Location;

public interface GPSManagerCallerInterface {

    void needPermissions();
    void locationHasBeenReceived(Location location);
    void gpsErrorHasBeenThrown(Exception error);
}
