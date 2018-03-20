package com.example.bhurivatmontri.trophel;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailAttraction extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_attraction);
        String nameOfAttraction = null;

        Bundle extras = getIntent().getExtras();
        if(extras == null) {
            nameOfAttraction = null;
        } else {
            nameOfAttraction = extras.getString("nameOfAttraction");
        }

        Toolbar toolbarDetailAttr = (Toolbar) findViewById(R.id.toolbar_detail_attraction);
        setSupportActionBar(toolbarDetailAttr);
        getSupportActionBar().setTitle("Attraction detail");

        ImageView coverDetailAttraction = (ImageView) findViewById(R.id.cover_detail_attraction);
        TextView nameDetailAttraction = (TextView) findViewById(R.id.name_detail_attraction);
        TextView detailDetailAttraction = (TextView) findViewById(R.id.detail_detail_attraction);

        String src_drawable = "att_"+nameOfAttraction.toLowerCase();
        int id_cover_attraction = getResources().getIdentifier(src_drawable,"drawable",getPackageName());
        int id_detail_attraction = getResources().getIdentifier(nameOfAttraction,"string",getPackageName());

        coverDetailAttraction.setImageResource(id_cover_attraction);
        nameDetailAttraction.setText(nameOfAttraction);
        detailDetailAttraction.setText(id_detail_attraction);

        Button btn_back = (Button) findViewById(R.id.button_back_detail_attraction);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DetailAttraction.this.finish();
            }
        });
    }
}
