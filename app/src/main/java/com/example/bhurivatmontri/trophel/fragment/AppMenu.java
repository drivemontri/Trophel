package com.example.bhurivatmontri.trophel.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.bhurivatmontri.trophel.ActProfile;
import com.example.bhurivatmontri.trophel.AddFriend;
import com.example.bhurivatmontri.trophel.Login;
import com.example.bhurivatmontri.trophel.R;
import com.example.bhurivatmontri.trophel.SettingApp;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;


/**
 * A simple {@link Fragment} subclass.
 */
public class AppMenu extends Fragment implements View.OnClickListener{

    private FragmentTabHost mTabhost;

    protected LinearLayout btn_my_profile;
    protected LinearLayout btn_add_friend;
    protected LinearLayout btn_setting;
    protected LinearLayout btn_about_us;
    protected LinearLayout btn_logout;

    public AppMenu() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_app_menu, container, false);

        btn_my_profile = (LinearLayout)view.findViewById(R.id.menu_edit_profile);
        btn_add_friend = (LinearLayout)view.findViewById(R.id.menu_add_friend);
        btn_setting = (LinearLayout)view.findViewById(R.id.menu_setting);
        btn_about_us = (LinearLayout)view.findViewById(R.id.menu_about_us);
        btn_logout = (LinearLayout)view.findViewById(R.id.menu_logout);

        btn_my_profile.setOnClickListener(this);
        btn_add_friend.setOnClickListener(this);
        btn_setting.setOnClickListener(this);
        btn_about_us.setOnClickListener(this);
        btn_logout.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.menu_edit_profile:
                Intent intent = new Intent(getActivity(),ActProfile.class);
                //intent.putExtra("Title","My Profile");
                startActivity(intent);
                break;
            case R.id.menu_add_friend:
                intent = new Intent(getActivity(),AddFriend.class);
                startActivity(intent);
                break;
            case R.id.menu_setting:
                intent = new Intent(getActivity(),SettingApp.class);
                startActivity(intent);
                break;
            case R.id.menu_about_us:
                break;
            case R.id.menu_logout:
                Logout();
                break;
        }
    }

    public void Logout() {
        FirebaseAuth.getInstance().signOut();
        LoginManager.getInstance().logOut();
        Intent intent = new Intent(this.getActivity(), Login.class);
        intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TOP | intent.FLAG_ACTIVITY_CLEAR_TASK | intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

}
