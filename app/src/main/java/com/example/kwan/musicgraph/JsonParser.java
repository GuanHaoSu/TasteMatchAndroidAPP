package com.example.kwan.musicgraph;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Kwan on 2016/7/6.
 */
public class JsonParser {
    public ArrayList<GenreList> GenreList(String result){
        ArrayList<GenreList> list = new ArrayList<GenreList>();
        try {
            JSONObject json = new JSONObject(result);
            JSONArray items = json.getJSONArray("data");
            for(int i=0;i<items.length();i++){
                String genreName =items.getJSONObject(i).getString("genreName");
                Integer genreScore =items.getJSONObject(i).getInt("Score");
                String comment =items.getJSONObject(i).getString("comment");
                GenreList obj = new GenreList(genreName,genreScore,comment);
                list.add(obj);
            }
        }
        catch (JSONException e){

        }
        return list;
    }

    public ArrayList<String> RecommendArtistList(String result){
        ArrayList<String> list = new ArrayList<String>();
        try {
            JSONObject json = new JSONObject(result);
            JSONArray items = json.getJSONArray("data");
            for(int i=0;i<items.length();i++){
                String ArtistName = items.getString(i);
                list.add(ArtistName);
            }
        }
        catch (JSONException e){

        }
        return list;
    }
    public ArrayList<FriendList> FriendsRecommendNameList(String result){
        ArrayList<FriendList> list = new ArrayList<FriendList>();
        try {
            JSONObject json = new JSONObject(result);
            JSONArray items = json.getJSONArray("data");
            for(int i=0;i<items.length();i++){
                String name =items.getJSONObject(i).getString("name");
                Integer score =items.getJSONObject(i).getInt("score");
                String comment =items.getJSONObject(i).getString("comment");
                FriendList obj = new FriendList(name,score,comment);
                list.add(obj);
            }
        }
        catch (JSONException e){

        }
        return list;
    }

    public ArrayList<String> FriendsRecommendIDList(String result){
        ArrayList<String> list = new ArrayList<String>();
        try {
            JSONObject json = new JSONObject(result);
            JSONArray items = json.getJSONArray("data");
            for(int i=0;i<items.length();i++){
                String id =items.getJSONObject(i).getString("id");
                list.add(id);
            }
        }
        catch (JSONException e){

        }
        return list;
    }

    public String FriendsProfileDescription(String result){
        String Description ="";
        try {
            JSONObject json = new JSONObject(result);
            JSONArray items = json.getJSONArray("data");
            for(int i=0;i<items.length();i++){
                String genreName =items.getString(i);
                if(i==0&&i==items.length()-1){
                    Description = Description + genreName ;
                }
                else if(i!=items.length()-1) {
                    if(i==items.length()-2){
                        Description = Description + genreName;
                    }
                    else {
                        Description = Description + genreName + ", ";
                    }
                }
                else if(i==items.length()-1)  {
                    Description = Description +" and "+ genreName;
                }
            }
        }
        catch (JSONException e){

        }
        return Description;
    }

    public String FriendsArtistsDescription(String result){
        String Description ="";
        try {
            JSONObject json = new JSONObject(result);
            JSONArray items = json.getJSONArray("data");
            for(int i=0;i<items.length();i++){
                String artistName =items.getString(i);
                if(i==0&&i==items.length()-1){
                    Description = Description + artistName ;
                }
                else if(i!=items.length()-1) {
                    if(i==items.length()-2){
                        Description = Description + artistName;
                    }
                    else {
                        Description = Description + artistName + ", ";
                    }
                }
                else if(i==items.length()-1) {
                    Description = Description +" and "+ artistName;
                }
            }
        }
        catch (JSONException e){

        }
        return Description;
    }

    public String ArtistsProfileDescription(String result){
        String Description ="";
        try {
            JSONObject json = new JSONObject(result);
            JSONArray items = json.getJSONArray("data");
            for(int i=0;i<items.length();i++){
                String ArtistName =items.getString(i);
                if(i!=items.length()-1) {
                    Description = Description + ArtistName + ", ";
                }
                else  {
                    Description = Description +"and "+ ArtistName;
                }
            }
        }
        catch (JSONException e){

        }
        return Description;
    }
}

