package com.example.kwan.musicgraph;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class RecommendArtistsProfileActivity extends AppCompatActivity {
    String ServerIP;
    static JsonParser jsonParser = new JsonParser();
    JSONObject requestData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend_artists_profile);
        ServerIP=getResources().getString(R.string.ServerIP);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        String userID = getIntent().getExtras().getString("userID");
      final  String ArtistName = getIntent().getExtras().getString("ArtistName");
        requestData= new JSONObject();
        try {
            requestData.put("ArtistName",ArtistName);
            requestData.put("id",userID);
        }catch (JSONException e){

        }
        new HttpRequest.HttpAsyncTaskPost(requestData,null, new HttpRequest.HttpAsyncTaskPost.AsyncResponse() {
            @Override
            public void processFinish(String output, View v,long elapsedTime) {
                String ArtistDescription =jsonParser.ArtistsProfileDescription(output);
                TextView  ArtistNameText = (TextView) findViewById(R.id.ArtistNameTextView);
                TextView Description = (TextView) findViewById(R.id.ArtistDescriptionTextView);
                ArtistDescription= ArtistName+" is similar to "+ArtistDescription;
                ArtistNameText.setText(ArtistName);
                Description.setText(ArtistDescription);

            }
        }).execute("http://"+ServerIP+"/RecommendArtistsProfile");

       final Button DontLikeButton = (Button) findViewById(R.id.ArtistDislikeButton);
        new HttpRequest.HttpAsyncTaskPost(requestData,null, new HttpRequest.HttpAsyncTaskPost.AsyncResponse() {
            @Override
            public void processFinish(String output, View v,long elapsedTime) {
                if(output.equals("true")){
                    DontLikeButton.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "You already like "+ArtistName, Toast.LENGTH_LONG).show();
                }
                else if(output.equals("false")) {
                    if (DontLikeButton != null)
                        DontLikeButton.setOnClickListener( new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {


                                new HttpRequest.HttpAsyncTaskPost(requestData,null, new HttpRequest.HttpAsyncTaskPost.AsyncResponse() {
                                    @Override
                                    public void processFinish(String output, View v,long elapsedTime) {
                                        Toast.makeText(getApplicationContext(), "We will not recommend "+ArtistName+" to you.", Toast.LENGTH_LONG).show();

                                    }
                                }).execute("http://"+ServerIP+"/DontRecommendTheArtist");

                            }
                        });
                }
            }
        }).execute("http://"+ServerIP+"/CheckUserLikesTheArtist");

    }

}
