package com.example.kwan.musicgraph;

/**
 * Created by Kwan on 2016/7/30.
 */
public class FriendList{
    private String Name;
    private Integer Score;
    private String  Comment;
    public FriendList(String Name,Integer Score,String Comment){
        this.Name=Name;
        this.Score=Score;
        this.Comment=Comment;
    }

    public String getGenreName(){
        return this.Name;
    }
    public Integer getGenreScore(){
        return this.Score;
    }
    public String getComment(){
        return this.Comment;
    }
}
