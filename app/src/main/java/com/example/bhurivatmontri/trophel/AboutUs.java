package com.example.bhurivatmontri.trophel;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.support.v7.widget.Toolbar;

public class AboutUs extends AppCompatActivity {

    Button back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        Toolbar toolbarAboutus = (Toolbar) findViewById(R.id.toolbar_aboutus);
        setSupportActionBar(toolbarAboutus);
        getSupportActionBar().setTitle("About Us");

        back = findViewById(R.id.button_back_aboutus);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AboutUs.this.finish();
            }
        });

    }
}
