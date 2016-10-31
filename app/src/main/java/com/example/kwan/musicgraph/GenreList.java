package com.example.kwan.musicgraph;

/**
 * Created by Kwan on 2016/7/10.
 */
public class GenreList{
    private String GenreName;
    private Integer GenreScore;
    private String  Comment;
    public GenreList(String GenreName,Integer GenreScore,String Comment){
        this.GenreName=GenreName;
        this.GenreScore=GenreScore;
        this.Comment=Comment;
    }

    public String getGenreName(){
        return this.GenreName;
    }
    public Integer getGenreScore(){
        return this.GenreScore;
    }
    public String getComment(){
        return this.Comment;
    }
}
