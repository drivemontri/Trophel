package com.example.bhurivatmontri.trophel;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class SettingApp extends AppCompatActivity {

    protected SharedPreferences settings;
    SharedPreferences.Editor editor;

    protected Spinner spinner_language;
    protected int select_language_position;
    protected String select_language_string;

    protected Button button_setting_ok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_app);

        Toolbar toolbarActListTrophy = (Toolbar) findViewById(R.id.toolbar_setting);
        setSupportActionBar(toolbarActListTrophy);
        getSupportActionBar().setTitle("Setting");

        settings = this.getSharedPreferences("Trophel",MODE_WORLD_WRITEABLE);
        editor = settings.edit();

        select_language_position = settings.getInt("select_language_position",-1);

        spinner_language = (Spinner) findViewById(R.id.setting_language);

        button_setting_ok = (Button) findViewById(R.id.button_setting_ok);

        String[] language = getResources().getStringArray(R.array.setting_language);
        ArrayAdapter<String> adapterLanguage = new ArrayAdapter<String>(getApplicationContext(),R.layout.spinner_layout,language);

        spinner_language.setAdapter(adapterLanguage);
        if(select_language_position == -1){
            select_language_position = 0;
        }
        Log.d("onDataChange","language position old : "+select_language_position);
        spinner_language.setSelection(select_language_position);

        spinner_language.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                switch (position){
                    case 0:
                        Log.d("onDataChange","None");
                        spinner_language.setSelection(0);
                        break;
                    case 1:
                        Log.d("onDataChange","English");
                        spinner_language.setSelection(1);
                        select_language_position = 1;
                        break;
                    case 2:
                        Log.d("onDataChange","Thai");
                        spinner_language.setSelection(2);
                        select_language_position = 2;
                        break;

                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        button_setting_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.putInt("select_language_position", select_language_position);
                editor.commit();
                SettingApp.this.finish();
                Log.d("onDataChange","Language Position : "+select_language_position);
            }
        });

    }
}
