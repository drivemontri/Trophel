package com.example.bhurivatmontri.trophel;

import android.content.Context;

import com.example.bhurivatmontri.trophel.fragment.AppMenu;
import com.example.bhurivatmontri.trophel.fragment.Attraction;
import com.example.bhurivatmontri.trophel.fragment.AttractionType;
import com.example.bhurivatmontri.trophel.fragment.ListFriend;
import com.example.bhurivatmontri.trophel.fragment.Map;

/**
 * Created by Bhurivat Montri on 4/18/2018.
 */

public class HomeFragment {

    private static HomeFragment homeFragment;
    private static Attraction attraction;
    private static AttractionType attractionType;
    private static Map map;
    private static ListFriend listFriend;
    private static AppMenu appMenu;
    private static Context context;

    public static Context getHomeFragmentContext() {
        return context;
    }

    public static void setHomeFragmentContext(Context c){
        if(context == null){
            context = c;
        }
    }

    public static HomeFragment getInstance(){
        if (homeFragment == null)
            homeFragment = new HomeFragment();

        return homeFragment;

    }

    public static void setAttraction(){
        if (attraction == null) {
            attraction = new Attraction();
        }
    }

    public static Attraction getAttractionInstance() {
        return attraction;
    }


    public static void setMap(){
        if (map == null) {
            map = new Map();
        }
    }

    public static Map getMapInstance() {
        return map;
    }


    public static void setListFriend(){
        if (listFriend == null) {
            listFriend = new ListFriend();
        }
    }

    public static ListFriend getListFriendInstance() {
        return listFriend;
    }


    public static void setAppMenu(){
        if (appMenu == null) {
            appMenu = new AppMenu();
        }
    }

    public static AppMenu getAppMenuInstance() {
        return appMenu;
    }

    public static void setAttractionType(){
        if (attractionType == null) {
            attractionType = new AttractionType();
        }
    }

    public static AttractionType getAttractionTypeInstance() {
        return attractionType;
    }

}
