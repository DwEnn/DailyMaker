package com.example.prgoh.dailymaker.Item;

import android.net.Uri;

import java.io.Serializable;

/**
 * Created by prgoh on 2017-07-24.
 */

public class RingtoneDto implements Serializable {

    private String title;
    private Long id;
    private String stringUri;
    private String str_id;

    public RingtoneDto(){

    }

    public String getStr_id() {
        return str_id;
    }

    public void setStr_id(String str_id) {
        this.str_id = str_id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStringUri() {
        return stringUri;
    }

    public void setStringUri(String stringUri) {
        this.stringUri = stringUri;
    }
}
