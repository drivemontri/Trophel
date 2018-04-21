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
public class AttractionFromRegion extends Fragment implements View.OnClickListener {

    protected Button buttonFindFromNorthern;
    protected Button buttonFindFromCentral;
    protected Button buttonFindFromNortheastern;
    protected Button buttonFindFromWestern;
    protected Button buttonFindFromSouthern;
    protected Button buttonFindFromEastern;

    public AttractionFromRegion() {
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
        View view = inflater.inflate(R.layout.fragment_attraction_from_region, container, false);

        buttonFindFromNorthern= (Button)view.findViewById(R.id.button_select_northern);
        buttonFindFromCentral= (Button)view.findViewById(R.id.button_select_central);
        buttonFindFromNortheastern= (Button)view.findViewById(R.id.button_select_northeastern);
        buttonFindFromWestern= (Button)view.findViewById(R.id.button_select_western);
        buttonFindFromSouthern= (Button)view.findViewById(R.id.button_select_southern);
        buttonFindFromEastern= (Button)view.findViewById(R.id.button_select_eastern);

        buttonFindFromNorthern.setOnClickListener(this);
        buttonFindFromCentral.setOnClickListener(this);
        buttonFindFromNortheastern.setOnClickListener(this);
        buttonFindFromWestern.setOnClickListener(this);
        buttonFindFromSouthern.setOnClickListener(this);
        buttonFindFromEastern.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v){
        Bundle bundle = new Bundle();
        FragmentManager manager = ((AppCompatActivity)getContext()).getSupportFragmentManager();
        switch (v.getId()){
            case R.id.button_select_northern:
                bundle.putString("Region","Northern");
                transacFragment(bundle);
                break;
            case R.id.button_select_central:
                bundle.putString("Region","Central");
                transacFragment(bundle);
                break;
            case R.id.button_select_northeastern:
                bundle.putString("Region","Northeastern");
                transacFragment(bundle);
                break;
            case R.id.button_select_western:
                bundle.putString("Region","Western");
                transacFragment(bundle);
                break;
            case R.id.button_select_southern:
                bundle.putString("Region","Southern");
                transacFragment(bundle);
                break;
            case R.id.button_select_eastern:
                bundle.putString("Region","Eastern");
                transacFragment(bundle);
                break;
        }
    }

    public void transacFragment(Bundle bundle){
        ListAttractionFromRegion listAttractionFromRegion = new ListAttractionFromRegion();
        listAttractionFromRegion.setArguments(bundle);
        FragmentManager manager = ((AppCompatActivity)getContext()).getSupportFragmentManager();
        manager.beginTransaction()
                .replace(R.id.attraction_from_region,listAttractionFromRegion,listAttractionFromRegion.getTag())
                .addToBackStack(null)
                .commit();
    }

}
