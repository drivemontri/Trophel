package com.example.bhurivatmontri.trophel.adapter;

/**
 * Created by Bhurivat Montri on 11/2/2017.
 */

public class Friend {

    private String name;
    private String detail;
    private String friendID;
    //private int icon;
    private String uriImg;
    private String countStar;

    public Friend(String name,String detail,String friendID,String uriImg,String countStar){
        this.setName(name);
        this.setDetail(detail);
        this.setFriendID(friendID);
        this.setUriImg(uriImg);
        this.setCountStar(countStar);
        //this.setIcon(icon);
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getDetail() {
        return detail;
    }
    public void setDetail(String detail) { this.detail = detail; }

    public String getFriendID() {
        return friendID;
    }
    public void setFriendID(String friendID) { this.friendID = friendID; }

    /*public int getIcon() { return icon; }
    public void setIcon(int icon) {this.icon = icon;}*/

    public String getUriImg(){return uriImg;}
    public void setUriImg(String uriImg){ this.uriImg = uriImg;}

    public String getCountStar(){return countStar;}
    public void setCountStar(String countStar){ this.countStar = countStar;}
}
