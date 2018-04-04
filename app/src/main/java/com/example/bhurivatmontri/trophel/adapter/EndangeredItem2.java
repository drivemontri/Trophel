package com.example.bhurivatmontri.trophel.adapter;

/**
 * Created by Bhurivat Montri on 3/29/2018.
 */

public class EndangeredItem2 {

    private String mName;
    private int mTrophy;
    private int star1 = 0;
    private int star2 = 0;
    private int star3 = 0;
    private int star4 = 0;
    private int star5 = 0;
    private String attractionID;

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

    public int getStar1() {
        return star1;
    }
    public int getStar2() {
        return star2;
    }
    public int getStar3() {
        return star3;
    }
    public int getStar4() {
        return star4;
    }
    public int getStar5() {
        return star5;
    }
    public void setStar(int star1,int star2,int star3,int star4,int star5 ) {
        this.star1 = star1;
        this.star2 = star2;
        this.star3 = star3;
        this.star4 = star4;
        this.star5 = star5;
    }

   public String getAttractionID() {return attractionID;}
   public void setAttractionID(String attractionID) { this.attractionID = attractionID; }

}
