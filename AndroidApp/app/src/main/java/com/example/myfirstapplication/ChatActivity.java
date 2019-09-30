package com.example.myfirstapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.myfirstapplication.R;
import com.example.myfirstapplication.auth.LoggedInUserManager;
import com.example.myfirstapplication.broadcast.BroadcastManager;
import com.example.myfirstapplication.broadcast.BroadcastManagerCallerInterface;
import com.example.myfirstapplication.model.ChatMessage;
import com.example.myfirstapplication.network.SocketManagementService;
import com.example.myfirstapplication.network.WebServiceManagementService;
import com.example.myfirstapplication.ui.adapter.ChatMessagesListAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity implements BroadcastManagerCallerInterface {

    BroadcastManager broadcastManagerForSocketIO;
    BroadcastManager broadcastManagerForWebService;
    boolean serviceStarted = false;
    ArrayList<ChatMessage> messagesList = new ArrayList<ChatMessage>();
    ChatMessagesListAdapter adapter;
    Button mButton;

    public void initializeBroadcastManagerForSocketIO() {
        broadcastManagerForSocketIO = new BroadcastManager(this,
                SocketManagementService.
                        SOCKET_SERVICE_CHANNEL, this);
    }

    public void initializeBroadcastManagerForWebService() {
        broadcastManagerForWebService = new BroadcastManager(this,
                WebServiceManagementService.WEB_SERVICE_CHANNEL, this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        initializeBroadcastManagerForSocketIO();
        initializeBroadcastManagerForWebService();
        adapter = new ChatMessagesListAdapter(this, messagesList);
        ListView listView = (ListView) findViewById(R.id.messages_view);
        listView.setAdapter(adapter);
        sendRequestWebService("","GET","chat", WebServiceManagementService.GET_MESSAGES);
        ((ImageButton) findViewById(R.id.buttonMessage)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText ed = ((EditText) findViewById(R.id.message_editText));
                String message = ((EditText) findViewById(R.id.message_editText)).getText().toString();
                try{
                    JSONObject JsonMessage = new JSONObject();
                    JsonMessage.put("username", LoggedInUserManager.getUser().username);
                    JsonMessage.put("message", message);
                    broadcastManagerForSocketIO.sendBroadcast(SocketManagementService.CLIENT_TO_SERVER_MESSAGE, JsonMessage.toString());
                    sendRequestWebService(JsonMessage.toString(),"POST","chat", WebServiceManagementService.POST_MESSAGE);
                    ed.getText().clear();
                }catch(Exception e){

                }

            }
        });
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
    public void MessageReceivedThroughBroadcastManager(final String channel, final String type, final String JsonMessage) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try{
                    if (channel.equals(WebServiceManagementService.WEB_SERVICE_CHANNEL)){
                        if (type.equals(WebServiceManagementService.GET_MESSAGES)){
                            Gson g = new Gson();
                            TypeToken<List<ChatMessage>> token = new TypeToken<List<ChatMessage>>() {};
                            List<ChatMessage> users = g.fromJson(JsonMessage, token.getType());
                            messagesList.addAll(users);
                            adapter.notifyDataSetChanged();
                        }
                    }else{
                        if (type.equals(SocketManagementService.MESSAGE_SENT)) {
                            JSONObject message = new JSONObject(JsonMessage);
                            ChatMessage cm = new ChatMessage();
                            JSONObject innerMessage = new JSONObject(message.get("message").toString());
                            cm.username = innerMessage.get("username").toString();
                            cm.message=innerMessage.get("message").toString();
                            messagesList.add(cm);
                            ((ListView) findViewById(R.id.messages_view)).setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        }
                    }
                }catch(Exception e){

                }


            }
        });
    }

    @Override
    public void ErrorAtBroadcastManager(Exception error) {

    }

    public void onClick(View view) {
        Intent intent = new Intent(getApplicationContext(), SocketManagementService.class);
        intent.putExtra("SERVER_HOST", ((EditText) findViewById(R.id.host_editText)).getText() + "");
        intent.putExtra("SERVER_PORT", Integer.parseInt(((EditText) findViewById(R.id.port_editText)).getText() + ""));
        intent.setAction(SocketManagementService.ACTION_CONNECT);
        startService(intent);
        serviceStarted = true;
    }
}
