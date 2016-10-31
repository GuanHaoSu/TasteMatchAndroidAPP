package com.example.kwan.musicgraph;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Kwan on 2016/7/6.
 */
public class PreferMusicGenreAdapter extends BaseAdapter {
    Context context;
    LayoutInflater layoutInflater;
    ArrayList<GenreList> items= new ArrayList<GenreList>();
    ArrayList<Integer> ScoreItems= new ArrayList<Integer>();
    public PreferMusicGenreAdapter(ArrayList<GenreList> items, Context context) {
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
            convertView= layoutInflater.inflate(R.layout.prefer_music_genre_list, null);
        }
        TextView ItemName=(TextView)convertView.findViewById(R.id.Itemname);
        TextView commentName=(TextView)convertView.findViewById(R.id.commentName);
        ProgressBar progressBar = (ProgressBar) convertView.findViewById(R.id.GenreProgressBar);

        ItemName.setText(items.get(position).getGenreName());
        commentName.setText("Level: "+items.get(position).getComment());
        progressBar.setProgress(items.get(position).getGenreScore());

        return convertView;
    }
}