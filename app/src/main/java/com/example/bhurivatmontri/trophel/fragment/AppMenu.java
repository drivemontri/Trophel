package com.example.bhurivatmontri.trophel.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bhurivatmontri.trophel.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class AppMenu extends Fragment {

    private FragmentTabHost mTabhost;
    public AppMenu() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_app_menu, container, false);
    }

}
