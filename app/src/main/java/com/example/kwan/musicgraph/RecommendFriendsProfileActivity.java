package com.example.kwan.musicgraph;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class RecommendFriendsProfileActivity extends AppCompatActivity {
    TextView FriendName;
    TextView Description;
    TextView ArtistsDescription;
    String ServerIP;
    static JsonParser jsonParser = new JsonParser();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend_friends_profile);
        ServerIP=getResources().getString(R.string.ServerIP);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String userID = getIntent().getExtras().getString("userID");
        String RfirendID = getIntent().getExtras().getString("RfirendID");
        final String RfirendName = getIntent().getExtras().getString("RfirendName");
        new HttpRequest.HttpAsyncTask(null, new HttpRequest.HttpAsyncTask.AsyncResponse() {
            @Override
            public void processFinish(String output, View v,long elapsedTime) {
                String GenreDescription =jsonParser.FriendsProfileDescription(output);
                FriendName = (TextView) findViewById(R.id.FriendNameTextView);
                Description = (TextView) findViewById(R.id.DescriptionTextView);
                GenreDescription= "You and "+RfirendName+" both like "+GenreDescription;
                FriendName.setText(RfirendName);
                Description.setText(GenreDescription);
            }
        }).execute("http://"+ServerIP+"/RecommendFriendsProfile/"+userID+"/"+RfirendID);

        new HttpRequest.HttpAsyncTask(null, new HttpRequest.HttpAsyncTask.AsyncResponse() {
            @Override
            public void processFinish(String output, View v,long elapsedTime) {
                String artistsDescription =jsonParser.FriendsArtistsDescription(output);
                ArtistsDescription = (TextView) findViewById(R.id.ArtistsTextView);
                artistsDescription= "Artists you both like : "+artistsDescription;
                ArtistsDescription.setText(artistsDescription);
            }
        }).execute("http://"+ServerIP+"/RecommendFriendsLikeArtist/"+userID+"/"+RfirendID);

    }

}
