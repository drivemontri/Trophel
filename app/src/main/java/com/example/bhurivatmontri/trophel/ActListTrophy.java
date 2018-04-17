package com.example.bhurivatmontri.trophel;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.example.bhurivatmontri.trophel.adapter.GridAdapter2;

public class ActListTrophy extends AppCompatActivity {

    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView.Adapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_list_trophy);

        String rg = null;

        Bundle extras = getIntent().getExtras();
        if(extras == null) {
            rg = null;
        } else {
            rg = extras.getString("Region");
        }

        Toolbar toolbarActListTrophy = (Toolbar) findViewById(R.id.toolbar_act_list_trophy);
        setSupportActionBar(toolbarActListTrophy);
        getSupportActionBar().setTitle("My Trophy ("+rg+")");

        mRecyclerView = (RecyclerView)findViewById(R.id.recyclerView_act_Trophy);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager  = new GridLayoutManager(this,1);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new GridAdapter2(this,rg);
        mRecyclerView.setAdapter(mAdapter);

    }
}
