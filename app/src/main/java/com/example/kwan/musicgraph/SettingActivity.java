package com.example.kwan.musicgraph;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
/*
* Facebook log in Reference from https://developers.facebook.com/docs/facebook-login/android by Facebook, 2016/7/6
* */
public class SettingActivity extends AppCompatActivity {
    CallbackManager callbackManager;
    private AccessToken accessToken;
    private AccessTokenTracker accessTokenTracker;
    private JSONArray MusicDataList = new JSONArray();
    private JSONObject MusicData = new JSONObject();
    String ServerIP ;
    ProgressBar pbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        setContentView(R.layout.activity_setting);
        ServerIP=getResources().getString(R.string.ServerIP);
        final Button SyncButton = (Button) findViewById(R.id.syncButton);
        LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        pbar= (ProgressBar)findViewById(R.id.SyncProgressBar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        FacebookSdk.sdkInitialize(getApplicationContext(),new FacebookSdk.InitializeCallback() {
            @Override
            public void onInitialized() {

                if(AccessToken.getCurrentAccessToken() == null){
                    SyncButton.setVisibility(View.GONE);
                } else {
                    SyncButton.setVisibility(View.VISIBLE);
                }
            }
        });
        callbackManager = CallbackManager.Factory.create();

        loginButton.setReadPermissions(Arrays.asList("user_likes"));

        loginButton.registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        accessToken = loginResult.getAccessToken();
                        GraphRequest request = GraphRequest.newMeRequest(
                                accessToken,
                                new GraphRequest.GraphJSONObjectCallback() {

                                    @Override
                                    public void onCompleted(JSONObject object, GraphResponse response) {

                                        String userId =object.optString("id");
                                        String UserName=object.optString("name");
                                        String token = accessToken.getToken();
                                        JSONObject requestData= new JSONObject();
                                        try {
                                            requestData.put("name",UserName);
                                            requestData.put("token",token);
                                            requestData.put("id",userId);
                                        }catch (JSONException e){

                                        }


                                        new HttpRequest.HttpAsyncTaskPost(requestData,null, new HttpRequest.HttpAsyncTaskPost.AsyncResponse() {
                                            @Override
                                            public void processFinish(String output, View v,long elapsedTime) {
                                                String o=output;
                                                Toast.makeText(getApplicationContext(), "Login is done", Toast.LENGTH_LONG).show();
                                            }
                                        }).execute("http://"+ServerIP+"/Login");

                                    }
                                });

                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id,name,link,music");
                        request.setParameters(parameters);
                        request.executeAsync();

                    }

                    @Override
                    public void onCancel() {
                    }

                    @Override
                    public void onError(FacebookException exception) {
                    }

                });

        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(
                    AccessToken oldAccessToken,
                    AccessToken currentAccessToken) {
                accessToken =currentAccessToken;
                if(accessToken==null){
                    SyncButton.setVisibility(View.GONE);
                }
                else {

                    SyncButton.setVisibility(View.VISIBLE);
                }
            }
        };


        if (SyncButton!=null)
            SyncButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    pbar.setVisibility(View.VISIBLE);
                    SyncButton.setVisibility(View.GONE);
                    MusicDataList = new JSONArray();
                    Profile profile = Profile.getCurrentProfile();
                    String url= "/"+profile.getId()+"/music";
                    GraphRequest request = new GraphRequest(
                            AccessToken.getCurrentAccessToken(),
                            url,
                            null,
                            HttpMethod.GET,
                            new GraphRequest.Callback() {
                                public void onCompleted(GraphResponse response) {
                                    GraphResponse a = response;
                                    try {
                                        JSONArray MusicData = a.getJSONObject().getJSONArray("data");
                                        String aa="";
                                        for(int i=0;i<MusicData.length();i++){
                                            JSONObject obj= MusicData.getJSONObject(i);

                                            MusicDataList.put(obj);
                                            String name = obj.getString("name");
                                            String id = obj.getString("id");

                                        }
                                    }
                                    catch (JSONException exception){

                                    }

                                }
                            }
                    );
                    GraphResponse response = request.executeAndWait();
                    while(response.getRequestForPagedResults(GraphResponse.PagingDirection.NEXT)!=null){
                        GraphRequest nextRequest = response.getRequestForPagedResults(GraphResponse.PagingDirection.NEXT);

                        nextRequest.setCallback(request.getCallback());
                        response = nextRequest.executeAndWait();

                    }
                    try{
                        MusicData.put("data",MusicDataList);
                        MusicData.put("name",profile.getName());
                        MusicData.put("id",profile.getId());
                        MusicData.put("token",AccessToken.getCurrentAccessToken().getToken());
                    }
                    catch (JSONException e){

                    }

                    new HttpRequest.HttpAsyncTaskPost(MusicData,v, new HttpRequest.HttpAsyncTaskPost.AsyncResponse() {
                        @Override
                        public void processFinish(String output, View v,long elapsedTime) {
                            String o=output;
                            pbar.setVisibility(View.GONE);
                            SyncButton.setVisibility(View.VISIBLE);
                            Toast.makeText(getApplicationContext(), "Sync is done", Toast.LENGTH_LONG).show();
                        }
                    }).execute("http://"+ServerIP+"/");

                }
            });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        accessTokenTracker.stopTracking();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
