package com.example.kwan.musicgraph;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Kwan on 2016/7/8.
 */
public class RecommendFriendsAdapter extends BaseAdapter {
    Context context;
    LayoutInflater layoutInflater;
    ArrayList<FriendList> items= new ArrayList<FriendList>();
    public RecommendFriendsAdapter(ArrayList<FriendList> items, Context context) {
        super();
        this.items = items;
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {

        return items.size();
    }

    @Override
    public Object getItem(int position) {

        return null;
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null) {
            convertView= layoutInflater.inflate(R.layout.recommend_friend_list, null);
        }
        TextView ItemName=(TextView)convertView.findViewById(R.id.RF_ItemName);
        TextView Level=(TextView)convertView.findViewById(R.id.RF_MatchLevel);
        ProgressBar bar=(ProgressBar)convertView.findViewById(R.id.RFProgressBar);
        ItemName.setText(items.get(position).getGenreName());
        Level.setText(items.get(position).getComment());
        bar.setProgress(items.get(position).getGenreScore());
        return convertView;
    }
}