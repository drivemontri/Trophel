package com.example.bhurivatmontri.trophel.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.bhurivatmontri.trophel.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AttractionType extends Fragment implements View.OnClickListener {

    protected Button buttonFindFromRegion;
    protected Button buttonFindFromLocationCurrent;

    public AttractionType() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_attraction_type, container, false);

        buttonFindFromRegion = (Button)view.findViewById(R.id.button_find_attraction_region);
        buttonFindFromLocationCurrent = (Button)view.findViewById(R.id.button_find_attraction_location_current);

        buttonFindFromRegion.setOnClickListener(this);
        buttonFindFromLocationCurrent.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v){
        Bundle bundle = new Bundle();
        FragmentManager manager = ((AppCompatActivity)getContext()).getSupportFragmentManager();
        switch (v.getId()){
            case R.id.button_find_attraction_region:
                AttractionFromRegion attractionFromRegion = new AttractionFromRegion();
                attractionFromRegion.setArguments(bundle);
                manager.beginTransaction()
                        .replace(R.id.attraction_type,attractionFromRegion,attractionFromRegion.getTag())
                        .addToBackStack(null)
                        .commit();
                break;
            case R.id.button_find_attraction_location_current:
                ListAttractionFromLocationCurrent listAttractionFromLocationCurrent = new ListAttractionFromLocationCurrent();
                listAttractionFromLocationCurrent.setArguments(bundle);
                manager.beginTransaction()
                        .replace(R.id.attraction_type,listAttractionFromLocationCurrent,listAttractionFromLocationCurrent.getTag())
                        .addToBackStack(null)
                        .commit();
                break;
        }
    }

}
