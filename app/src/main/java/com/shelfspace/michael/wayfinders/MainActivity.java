package com.shelfspace.michael.wayfinders;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Button to link to the scoring app
        final Button scoring = (Button) findViewById(R.id.scoring);
        scoring.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                Intent intent = new Intent(v.getContext(), Setup.class);
                startActivity(intent);
            }
        });

        //Button to link to rules
        final Button rules = (Button) findViewById(R.id.rules);
        rules.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                //Intent intent = new Intent(v.getContext(), Rules.class);
                //startActivity(intent);
            }
        });


        final Button howtoplay = (Button) findViewById(R.id.howToPlay);
        howtoplay.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                //Intent intent = new Intent(v.getContext(), Video.class);
                //startActivity(intent);
            }
        });

        //Button to link to Shelf Space channel
        int logoID = getResources().getIdentifier("logo", "id", getPackageName());
        final ImageButton logo = ((ImageButton) findViewById(logoID));
        logo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //logo.setBackground(getDrawable(R.drawable.border));
                //Intent intent = new Intent(v.getContext(), About.class);
                //startActivity(intent);
            }
        });
    }
}
