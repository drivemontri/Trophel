package com.example.bhurivatmontri.trophel.adapter;

/**
 * Created by Bhurivat Montri on 3/29/2018.
 */

public class EndangeredItem2 {

    private String mName;
    private int mTrophy;
    private int[] star = {0,0,0,0,0};
    private int numStarAtt = 0;
    private int numStarAttSucc = 0;
    private String attractionID;
    private String mThumbnail;

    public String getName() {
        return mName;
    }
    public void setName(String name) {
        this.mName = name;
    }

    public int getTrophy() {
        return mTrophy;
    }
    public void setTrophy(int mTrophy) {
        this.mTrophy = mTrophy;
    }

    public int[] getStar1() {
        return star;
    }
    public void setStar(int star1,int star2,int star3,int star4,int star5 ) {
        this.star[0] = star1;
        this.star[1] = star2;
        this.star[2] = star3;
        this.star[3] = star4;
        this.star[4] = star5;
        this.numStarAtt = 0;
        this.numStarAttSucc = 0;
        for (int i = 0; i < this.star.length; i++) {
            if(this.star[i] == 1){
                this.numStarAtt += 1;
            }else if (this.star[i] == 2){
                this.numStarAtt += 1;
                this.numStarAttSucc += 1;
            }
        }
    }

    public int getNumStarAtt(){
        return this.numStarAtt;
    }
    public int getNumStarAttSucc(){
        return this.numStarAttSucc;
    }

    public String getAttractionID() {return attractionID;}
    public void setAttractionID(String attractionID) { this.attractionID = attractionID; }

    public String getThumbnail() {
        return this.mThumbnail;
    }
    public void setThumbnail(String thumbnail) {
        this.mThumbnail = thumbnail;
    }

}
