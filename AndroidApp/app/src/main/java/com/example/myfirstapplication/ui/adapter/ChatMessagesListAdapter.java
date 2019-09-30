package com.example.myfirstapplication.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.example.myfirstapplication.R;
import com.example.myfirstapplication.auth.LoggedInUserManager;
import com.example.myfirstapplication.model.ChatMessage;
import com.example.myfirstapplication.model.User;

import java.util.List;

public class ChatMessagesListAdapter extends ArrayAdapter<ChatMessage> {
    private final Context context;
    private final List<ChatMessage> values;

    public ChatMessagesListAdapter(Context context, List<ChatMessage> values) {
        super(context, -1, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ChatMessage cm = getItem(position);
        LayoutInflater inflater = LayoutInflater.from(getContext());
        if (convertView == null){
            convertView = inflater.inflate(R.layout.chat_messages_list_item, parent, false);
        }
        TextView textView1 = (TextView) convertView.findViewById(R.id.chat_username_list_text_view);
        TextView textView2 = (TextView) convertView.findViewById(R.id.chat_messages_list_text_view);

        textView2.setText(cm.message);

        LinearLayout linearLayout = (LinearLayout) convertView.findViewById(R.id.chat_layout);
        LinearLayout linearLayout2 = (LinearLayout) convertView.findViewById(R.id.outer_chat_layout);
        RelativeLayout.LayoutParams layoutParams;

        layoutParams = (RelativeLayout.LayoutParams) linearLayout2.getLayoutParams();

        if (cm.username.equals(LoggedInUserManager.getUser().username)) {
            textView1.setText("You");
            linearLayout.setBackgroundColor(Color.argb(9,20,219,40));
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_END);
        } else {
            textView1.setText(cm.username);
            linearLayout.setBackgroundColor(Color.argb(9,219,20,20));
            layoutParams.removeRule(RelativeLayout.ALIGN_PARENT_END);
        }

        return convertView;
    }
}