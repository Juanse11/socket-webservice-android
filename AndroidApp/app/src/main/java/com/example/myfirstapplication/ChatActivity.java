package com.example.myfirstapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.myfirstapplication.R;
import com.example.myfirstapplication.broadcast.BroadcastManager;
import com.example.myfirstapplication.broadcast.BroadcastManagerCallerInterface;
import com.example.myfirstapplication.network.SocketManagementService;

import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity implements BroadcastManagerCallerInterface {

    BroadcastManager broadcastManagerForSocketIO;
    boolean serviceStarted = false;
    ArrayList<String> messagesList = new ArrayList<String>();
    ArrayAdapter<String> adapter;

    public void initializeBroadcastManagerForSocketIO() {
        broadcastManagerForSocketIO = new BroadcastManager(this,
                SocketManagementService.
                        SOCKET_SERVICE_CHANNEL, this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((Button) findViewById(R.id.service_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SocketManagementService.class);
                intent.putExtra("SERVER_HOST", ((EditText) findViewById(R.id.host_editText)).getText() + "");
                intent.putExtra("SERVER_PORT", Integer.parseInt(((EditText) findViewById(R.id.port_editText)).getText() + ""));
                intent.setAction(SocketManagementService.ACTION_CONNECT);
                startService(intent);
                serviceStarted = true;
            }
        });
        initializeBroadcastManagerForSocketIO();
        adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, messagesList);
        ((ImageButton) findViewById(R.id.buttonMessage)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText ed = ((EditText) findViewById(R.id.message_editText));
                String message = ((EditText) findViewById(R.id.message_editText)).getText().toString();
                broadcastManagerForSocketIO.sendBroadcast(SocketManagementService.CLIENT_TO_SERVER_MESSAGE, message);
                ed.getText().clear();
            }
        });
    }

    @Override
    public void MessageReceivedThroughBroadcastManager(final String channel, final String type, final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (type.equals(SocketManagementService.MESSAGE_SENT)) {
                    messagesList.add(message);
                    ((ListView) findViewById(R.id.messages_view)).setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }

            }
        });
    }

    @Override
    public void ErrorAtBroadcastManager(Exception error) {

    }
}
