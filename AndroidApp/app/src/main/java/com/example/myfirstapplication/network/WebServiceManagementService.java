package com.example.myfirstapplication.network;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.myfirstapplication.MainActivity;
import com.example.myfirstapplication.broadcast.BroadcastManager;
import com.example.myfirstapplication.broadcast.BroadcastManagerCallerInterface;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.util.logging.Logger;

import javax.net.ssl.SSLSocket;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class WebServiceManagementService extends IntentService implements BroadcastManagerCallerInterface {

    public static BroadcastManager broadcastManager;
    public static String WEB_SERVICE_CHANNEL = "com.example.myfirstapplication.WEB_SERVICE_CHANNEL";
    public static String GET_USERS = "GET_USERS";
    public static String GET_MESSAGES= "GET_MESSAGES";
    public static String GET_POSITIONS= "GET_POSTIONS";
    public static String POST_USER= "POST_USER";
    public static String POST_MESSAGE= "POST_MESSAGE";
    public static String CLIENT_TO_SERVER_MESSAGE = "CLIENT_TO_SERVER_MESSAGE";


    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    public static final String GET_REQUEST = "com.example.myfirstapplication.network.action.GET_REQUEST";
    private static final String ACTION_BAZ = "com.example.myfirstapplication.network.action.BAZ";

    // TODO: Rename parameters
    private String SERVER_HOST = "172.17.9.21";
    private int SERVER_PORT = 9090;
    private int responseCode;


    public WebServiceManagementService() {
        super("WebServiceManagementService");
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (GET_REQUEST.equals(action)) {
                String BASE_URL = intent.getStringExtra("BASE_URL");
                String PAYLOAD = intent.getStringExtra("PAYLOAD");
                String METHOD_TYPE = intent.getStringExtra("METHOD_TYPE");
                String RESOURCE = intent.getStringExtra("RESOURCE");
                String ACTION = intent.getStringExtra("ACTION");
                initializeBroadcastManager();
                CallWebServiceOperation(BASE_URL, PAYLOAD,  RESOURCE, METHOD_TYPE, ACTION);
            }

        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionFoo(String param1, String param2) {
        // TODO: Handle action Foo
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionBaz(String param1, String param2) {
        // TODO: Handle action Baz
        throw new UnsupportedOperationException("Not yet implemented");
    }

//    public void initializeClientSocketManager(){
//        try{
//            if(clientSocketManager==null){
//                clientSocketManager=new ClientSocketManager(
//                        SERVER_HOST,
//                        SERVER_PORT,this);
//                clientSocketManager.run();
//            }
//        }catch (Exception error){
//            Toast.makeText(this,error.getMessage(),Toast.LENGTH_LONG).show();
//        }
//    }

    public static void CallWebServiceOperation(
            final String webServiceURL,
            final String payload,
            final String resource,
            final String methodType,
            final String action) {

        try {
            URL url = new URL(webServiceURL + "/" + resource);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod(methodType);
            httpURLConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            httpURLConnection.setRequestProperty("Accept", "application/json");

            if (methodType.equals("POST")) {
                httpURLConnection.setDoOutput(true);
                try (OutputStream os = httpURLConnection.getOutputStream()) {
                    byte[] input = payload.getBytes("UTF-8");
                    os.write(input, 0, input.length);
                }

            }
            int responseCode = httpURLConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream in = httpURLConnection.getInputStream();
                StringBuffer stringBuffer = new StringBuffer();
                int charIn = 0;
                while ((charIn = in.read()) != -1) {
                    stringBuffer.append((char) charIn);
                }
                broadcastManager.sendBroadcast(action, stringBuffer.toString());
            }


            httpURLConnection.disconnect();

        } catch (Exception error) {

        }
    }

    public void initializeBroadcastManager() {
        try {
            if (broadcastManager == null) {
                broadcastManager = new BroadcastManager(
                        getApplicationContext(),
                        WEB_SERVICE_CHANNEL,
                        this);
            }
        } catch (Exception error) {
            Toast.makeText(this, error.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
//
//    @Override
//    public void MessageReceived(String message) {
//        try {
//
//            if (broadcastManager != null) {
//                broadcastManager.sendBroadcast(SERVER_TO_CLIENT_MESSAGE, message);
//            }
//        } catch (Exception error) {
//
//        }
//    }
//
//    @Override
//    public void ErrorFromSocketManager(Exception error) {
//
//    }

    @Override
    public void MessageReceivedThroughBroadcastManager(String channel, String type, String
            message) {
        try {
//            if (clientSocketManager != null) {
//                if (type.equals(CLIENT_TO_SERVER_MESSAGE)) {
//                    clientSocketManager.sendMessage(message);
//                }
//            }
        } catch (Exception error) {

        }

    }


    @Override
    public void ErrorAtBroadcastManager(Exception error) {

    }

    @Override
    public void onDestroy() {
        if (broadcastManager != null) {
            broadcastManager.unRegister();
        }
        broadcastManager = null;
        super.onDestroy();
    }

}
