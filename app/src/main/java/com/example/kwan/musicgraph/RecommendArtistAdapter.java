package com.example.kwan.musicgraph;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import com.facebook.Profile;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

/**
 * Created by Kwan on 2016/7/9.
 */
public class RecommendArtistAdapter extends BaseAdapter {
    Context context;
    LayoutInflater layoutInflater;
    ArrayList<String> items= new ArrayList<String>();
    static int btnState[];
    static ViewHolder holder;
    String ServerIP ;
    String UserID;
    public RecommendArtistAdapter(ArrayList<String> items,int[] btnState, Context context,String ServerIP,String UserID) {
        super();
        this.items = items;
        this.context = context;
        this.btnState=btnState;
        this.ServerIP =ServerIP;
        this.UserID=UserID;
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
            convertView= layoutInflater.inflate(R.layout.recommend_artist_list, null);
            holder = new ViewHolder();
            holder.ItemName=(TextView)convertView.findViewById(R.id.RA_ItemName);
            holder.LikeButton=(ToggleButton) convertView.findViewById(R.id.toggleButton);
            holder.LikeButton.setText(null);
            holder.LikeButton.setTextOn(null);
            holder.LikeButton.setTextOff(null);
            holder.LikeButton.setFocusableInTouchMode(false);
            holder.LikeButton.setFocusable(false);
            holder.LikeButton.setBackgroundResource(R.drawable.like_button);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.ItemName.setText(items.get(position));
        if(btnState[position]==1){
            holder.LikeButton.setChecked(true);
        }
        else {
            holder.LikeButton.setChecked(false);
        }

        holder.LikeButton.setTag(position);
        holder.LikeButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                int position=(Integer) v.getTag();
                boolean on = ((ToggleButton) v).isChecked();

                if (on) {
                    btnState[position]=1;
                    holder.LikeButton.setChecked(true);
                    final String ArtistName=items.get(position);
                    JSONObject requestData= new JSONObject();
                    try {
                        requestData.put("ArtistName",ArtistName);
                        requestData.put("id",UserID);
                    }catch (JSONException e){

                    }
                    new HttpRequest.HttpAsyncTaskPost(requestData,null, new HttpRequest.HttpAsyncTaskPost.AsyncResponse() {
                        @Override
                        public void processFinish(String output, View v,long elapsedTime) {
                            String o=output;
                            Toast.makeText(context.getApplicationContext(), "You Liked "+ArtistName, Toast.LENGTH_LONG).show();
                        }
                    }).execute("http://"+ServerIP+"/LikeArtist");

                    notifyDataSetChanged();

                } else {
                    btnState[position]=0;
                    holder.LikeButton.setChecked(false);
                    final String ArtistName=items.get(position);
                    JSONObject requestData= new JSONObject();
                    try {
                        requestData.put("ArtistName",ArtistName);
                        requestData.put("id",UserID);
                    }catch (JSONException e){

                    }
                    new HttpRequest.HttpAsyncTaskPost(requestData,null, new HttpRequest.HttpAsyncTaskPost.AsyncResponse() {
                        @Override
                        public void processFinish(String output, View v,long elapsedTime) {
                            String o=output;
                            Toast.makeText(context.getApplicationContext(), "You disliked "+ArtistName, Toast.LENGTH_LONG).show();
                        }
                    }).execute("http://"+ServerIP+"/DislikeArtist");
                    notifyDataSetChanged();
                }

            }
        });

        return convertView;
    }
    static class ViewHolder {
        private TextView ItemName;
        private ToggleButton LikeButton;
    }
}