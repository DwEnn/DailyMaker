package com.example.prgoh.dailymaker.Item;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Created by prgoh on 2017-07-24.
 */

public class MusicDto implements Serializable {

    private String id;
    private String albumId;
    private String title;
    private String artist;
    private String strUri;

    public MusicDto(){

    }

    public MusicDto(String id, String albumId, String title, String artist){
        this.id = id;
        this.albumId = albumId;
        this.title = title;
        this.artist = artist;
    }

    public String getId(){
        return id;
    }

    public void setId(String id){
        this.id = id;
    }

    public String getAlbumId(){
        return albumId;
    }

    public void setAlbumId(String albumId){
        this.albumId = albumId;
    }

    public String getTitle(){
        return title;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public String getArtist(){
        return artist;
    }

    public void setArtist(String artist){
        this.artist = artist;
    }

    @Override
    public String toString() {
        return "MusicDto{"+"id = "+id+"\\"+", albumId = "+
                albumId + "\\" + ", title = " + title + "\\"+
                ", artist = "+artist+"\\"+"}";
    }

    public void setStrUri(String strUri) {
        this.strUri = strUri;
    }

    public String getStrUri() {
        return strUri;
    }
}