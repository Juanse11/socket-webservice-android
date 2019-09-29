package com.example.myfirstapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myfirstapplication.R;
import com.example.myfirstapplication.model.User;

import java.util.List;

public class UsersListAdapter extends ArrayAdapter<User> {
    private final Context context;
    private final List<User> values;

    public UsersListAdapter(Context context, List<User> values) {
        super(context, -1, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        User user = getItem(position);
        LayoutInflater inflater = LayoutInflater.from(getContext());
        if (convertView == null){
            convertView = inflater.inflate(R.layout.user_list_item, parent, false);
        }
        TextView textView = (TextView) convertView.findViewById(R.id.users_list_text_view);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.users_list_image_view);
        textView.setText(values.get(position).username);
        // change the icon for Windows and iPhone
        if (user.status.equals("Online")) {
            imageView.setImageResource(R.drawable.ic_green_dot);
        } else {
            imageView.setImageResource(R.drawable.ic_red_dot);
        }

        return convertView;
    }
}