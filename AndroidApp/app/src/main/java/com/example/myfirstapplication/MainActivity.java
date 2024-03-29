package com.example.myfirstapplication;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;

import com.example.myfirstapplication.database.DatabaseManager;
import com.example.myfirstapplication.ui.adapter.UsersListAdapter;
import com.example.myfirstapplication.broadcast.BroadcastManager;
import com.example.myfirstapplication.broadcast.BroadcastManagerCallerInterface;
import com.example.myfirstapplication.broadcast.NetworkStateBroadcastManager;
import com.example.myfirstapplication.broadcast.NetworkStateBroadcastManagerCallerInterface;
import com.example.myfirstapplication.database.AppDatabase;
import com.example.myfirstapplication.ui.dialog.LogoutDialogAlert;
import com.example.myfirstapplication.gps.GPSManager;
import com.example.myfirstapplication.gps.GPSManagerCallerInterface;
import com.example.myfirstapplication.model.Position;
import com.example.myfirstapplication.model.User;
import com.example.myfirstapplication.network.SocketManagementService;
import com.example.myfirstapplication.network.WebServiceManagementService;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.room.Room;

import android.view.Menu;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, GPSManagerCallerInterface, BroadcastManagerCallerInterface, NetworkStateBroadcastManagerCallerInterface {

    GPSManager gpsManager;
    private MapView map;
    private MyLocationNewOverlay mLocationOverlay;
    BroadcastManager broadcastManagerForSocketIO;
    BroadcastManager broadcastManagerForWebService;
    NetworkStateBroadcastManager networkStateBroadcastManager;
    UsersListAdapter adapter;
    ArrayList<User> allUsers = new ArrayList<>();
    ArrayList<Position> usersPositions;
    boolean serviceStarted = false;
    AppDatabase appDatabase;



    public void initializeGPSManager() {
        gpsManager = new GPSManager(this, this);
        gpsManager.initializeLocationManager();
    }

    public void initializeBroadcastManagerForSocketIO() {
        broadcastManagerForSocketIO = new BroadcastManager(this,
                SocketManagementService.
                        SOCKET_SERVICE_CHANNEL, this);
    }

    public void initializeBroadcastManagerForWebService() {
        broadcastManagerForWebService = new BroadcastManager(this,
                WebServiceManagementService.WEB_SERVICE_CHANNEL, this);
    }

    public void initializeBroadcastManagerForNetworkState() {
        networkStateBroadcastManager = new NetworkStateBroadcastManager(this, "NETWORK_STATE", this);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);


        ((Button) findViewById(R.id.start_service_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(
                        getApplicationContext(), SocketManagementService.class);
                intent.putExtra("SERVER_HOST", ((EditText) findViewById(R.id.server_ip_txt)).getText() + "");
                intent.putExtra("SERVER_PORT", Integer.parseInt(((EditText) findViewById(R.id.server_port_txt)).getText() + ""));
                intent.setAction(SocketManagementService.ACTION_CONNECT);
                startService(intent);
                serviceStarted = true;

            }
        });
        appDatabase = DatabaseManager.getInstance(this);
        initializeGPSManager();
        initializeOSM();
        initializeBroadcastManagerForWebService();
        initializeBroadcastManagerForSocketIO();
        initializeBroadcastManagerForNetworkState();
        sendRequestWebService("", "GET", "users", WebServiceManagementService.GET_USERS);

        adapter = new UsersListAdapter(this, allUsers);
        ListView listView = (ListView) findViewById(R.id.messages_list_view);
        listView.setAdapter(adapter);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            LogoutDialogAlert dialog = new LogoutDialogAlert(this);
            dialog.show(getSupportFragmentManager(), "dialog");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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

    public void sendRequestWebService(String payload, String type, String resource, String action) {
        Intent intent = new Intent(this, WebServiceManagementService.class);
        intent.setAction(WebServiceManagementService.GET_REQUEST);
        intent.putExtra("BASE_URL", "http://192.168.0.15:61103/WebServiceREST/resources");
        intent.putExtra("METHOD_TYPE", type);
        intent.putExtra("PAYLOAD", payload);
        intent.putExtra("RESOURCE", resource);
        intent.putExtra("ACTION", action);
        startService(intent);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_home) {

        } else if (id == R.id.nav_chat) {
            Intent intent = new Intent(this, ChatActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_logout) {
            LogoutDialogAlert dialog = new LogoutDialogAlert(this);
            dialog.show(getSupportFragmentManager(), "dialog");
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void needPermissions() {
        this.requestPermissions(new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION},
                1001);
    }

    @Override
    public void locationHasBeenReceived(final Location location) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ((TextView) findViewById(R.id.latitude_text_view)).setText(location.getLatitude() + "");
                ((TextView) findViewById(R.id.longitude_text_view)).setText(location.getLongitude() + "");
                if (map != null)
                    setMapCenter(location);

            }
        });

//        JSONObject locationToJson = new JSONObject();
//        try {
//            locationToJson.put("latitude", ((int) location.getLatitude()));
//            locationToJson.put("longitude", ((int) location.getLongitude()));
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        sendRequestWebService(locationToJson.toString(), "POST", "positions", WebServiceManagementService.GET_USERS);

        if (serviceStarted)
            if (broadcastManagerForSocketIO != null) {
                broadcastManagerForSocketIO.sendBroadcast(
                        SocketManagementService.CLIENT_TO_SERVER_MESSAGE,
                        location.getLatitude() + " / " + location.getLongitude());
            }
    }

    @Override
    public void gpsErrorHasBeenThrown(final Exception error) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder builder =
                        new AlertDialog.
                                Builder(getApplicationContext());
                builder.setTitle("GPS Error")
                        .setMessage(error.getMessage())
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //TODO
                            }
                        });
                builder.show();
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1001) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this,
                        "Thanks!!", Toast.LENGTH_SHORT).show();
                gpsManager.startGPSRequesting();
            }

        }
        if (requestCode == 1002) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initializeOSM();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {


    }


    public void initializeOSM() {
        try {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{
                                Manifest.permission.
                                        WRITE_EXTERNAL_STORAGE}, 1002);

                return;
            }
            Context ctx = getApplicationContext();
            Configuration.getInstance().load(ctx,
                    PreferenceManager.
                            getDefaultSharedPreferences(ctx));
            map = (MapView) findViewById(R.id.map);
            map.setTileSource(TileSourceFactory.MAPNIK);
            this.mLocationOverlay =
                    new MyLocationNewOverlay(
                            new GpsMyLocationProvider(
                                    this), map);
            this.mLocationOverlay.enableMyLocation();
            map.getOverlays().add(this.mLocationOverlay);
        } catch (Exception error) {
            Toast.makeText(this, error.getMessage(), Toast.LENGTH_SHORT).show();

        }
    }

    public void setMapCenter(Location location) {
        IMapController mapController =
                map.getController();
        mapController.setZoom(9.5);
        GeoPoint startPoint = new GeoPoint(
                location.getLatitude(), location.getLongitude());
        mapController.setCenter(startPoint);
    }

    public void updatePositions() {

    }

    @Override
    public void MessageReceivedThroughBroadcastManager(final String channel, final String type, final String JsonMessage) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {

                    if (channel.equals(WebServiceManagementService.WEB_SERVICE_CHANNEL)) {
                        if (type.equals(WebServiceManagementService.GET_USERS)){
                            Gson g = new Gson();
                            TypeToken<List<User>> token = new TypeToken<List<User>>() {};
                            List<User> users = g.fromJson(JsonMessage, token.getType());
                            allUsers.addAll(users);
                            adapter.notifyDataSetChanged();
                        }
                    } else {
                        JSONObject message = new JSONObject(JsonMessage);
                        if (type.equals(SocketManagementService.USER_CONNECTED)) {
                            if (!findUserByID(message.getInt("id"), "Online")){
                                User u = new User();
                                u.Id = message.getInt("id");
                                u.status = "Online";
                                allUsers.add(u);
                            }
                            ((ListView)findViewById(R.id.messages_list_view)).setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        } else if (type.equals(SocketManagementService.USER_DISCONNECTED)) {
                            findUserByID(message.getInt("id"), "Offline");
                            ((ListView)findViewById(R.id.messages_list_view)).setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        }
                    }
                } catch (Exception e) {

                }

            }
        });

    }

    public boolean findUserByID(int id, String status){
        for (int i = 0; i < allUsers.size();i++){
            User u = allUsers.get(i);
            if (u.Id == id){
                u.status = status;
                return true;
            }
        }
        return false;
    }

    @Override
    public void onNetworkStatusChange(String type, String message) {
        TextView mStatusTextView = (TextView) findViewById(R.id.status_text_view);
        ImageView mStatusImageView = (ImageView) findViewById(R.id.status_image_view);
        if (message.equals("ONLINE")) {
            mStatusTextView.setText("Online");
            mStatusImageView.setImageResource(R.drawable.ic_green_dot);
        }else{
            mStatusTextView.setText("Offline");
            mStatusImageView.setImageResource(R.drawable.ic_red_dot);

        }
    }

    @Override
    public void ErrorAtBroadcastManager(Exception error) {

    }

    @Override
    protected void onPause() {
        if (broadcastManagerForSocketIO != null) {
            broadcastManagerForSocketIO.unRegister();
        }
        if (broadcastManagerForWebService != null) {
            broadcastManagerForWebService.unRegister();
        }
        if (networkStateBroadcastManager != null) {
            networkStateBroadcastManager.unRegister();
        }
        super.onPause();
    }
}
