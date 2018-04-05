package com.example.bhurivatmontri.trophel.adapter;

/**
 * Created by Bhurivat Montri on 10/20/2017.
 */

public class EndangeredItem {
    private String mName;
    private String mThumbnail;
    private String mKeyName;

    public EndangeredItem(String name,String thumbnail,String keyName){
        this.mName = name;
        this.mThumbnail = thumbnail;
        this.mKeyName =keyName;
    }

    public String getName() {
        return this.mName;
    }
    public void setName(String name) {
        this.mName = name;
    }

    public String getThumbnail() {
        return this.mThumbnail;
    }
    public void setThumbnail(String thumbnail) {
        this.mThumbnail = thumbnail;
    }

    public String getKeyName() {
        return this.mKeyName;
    }
    public void setKeyName(String keyName) {
        this.mKeyName = keyName;
    }
}
