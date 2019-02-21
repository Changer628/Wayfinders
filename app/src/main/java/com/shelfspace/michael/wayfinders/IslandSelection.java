package com.shelfspace.michael.wayfinders;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.Collections;

public class IslandSelection extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_island_selection);
        Intent intent = getIntent();
        final ArrayList<Integer> islandNumbers = intent.getIntegerArrayListExtra("islands");
        //This is required to update the button image after.
        final int buttonIndex = intent.getIntExtra("buttonID", 0);
        //The number of the old island selected. If -1, it means that no island has been selected yet
        final int oldIslandNum = intent.getIntExtra("oldIslandNum", 0);

        //If an island was previously selected for this spot, we need to allow the user to select the same island again
        if (oldIslandNum > -1){
            islandNumbers.add(oldIslandNum);
            //Sort the numbers collected so that they'll appear in numerical order
            Collections.sort(islandNumbers);
        }

        //Used to initialize all of the buttons in the activity
        final ImageButton[] buttonList = new ImageButton[islandNumbers.size()];

        //Initialize all of the buttons in the activity
        for (int i = 0; i < islandNumbers.size(); i++) {
            //Associate the buttons in the XML file with the selected islands
            String buttonID = "";
            if (i < 10) {
                buttonID = "islandSelect" + "0" + i;
            }else{
                buttonID = "islandSelect" + i;
            }

            //Used to get the ID of the associated button for this island
            int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
            ////Log.d("test", String.valueOf(resID));
            buttonList[i] = ((ImageButton) findViewById(resID));

            //This is used to get the numerically correct image onto the imageButton
            int num = islandNumbers.get(i);
            String stringNum = String.valueOf(num);
            if (num < 10){
                stringNum = "0" + stringNum;
            }

            //Sets the corresponding image. imageID is also what we want to send back to the previous activity to update the button selected
            final int imageID = getResources().getIdentifier("island" + stringNum, "drawable", getPackageName());
            buttonList[i].setImageResource(imageID);

            final int islandNum = num;
            buttonList[i].setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    //Send back the original button that was selected + the Island chosen
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("buttonID", buttonIndex);
                    returnIntent.putExtra("islandImage", imageID);
                    returnIntent.putExtra("islandNum", islandNum);
                    setResult(Activity.RESULT_OK,returnIntent);
                    finish();
                }
            });
        }

        if (islandNumbers.size() < 5){
            for (int i = 0; i < 5 - islandNumbers.size(); i++){
                Log.d("test", "i = " + i);
                String buttonID = "";
                if (i < 10) {
                    buttonID = "islandSelect" + "0" + (islandNumbers.size() + i);
                }else{
                    buttonID = "islandSelect" + (islandNumbers.size() + i);
                }

                //Used to get the ID of the associated button for this island
                int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
                ////Log.d("test", String.valueOf(resID));
                ImageButton tempButton = ((ImageButton) findViewById(resID));

                //Sets the corresponding image. imageID is also what we want to send back to the previous activity to update the button selected
                final int imageID = getResources().getIdentifier("number" + "00", "drawable", getPackageName());
                tempButton.setImageResource(imageID);
                tempButton.setEnabled(false);
                tempButton.setVisibility(View.INVISIBLE);
            }
        }
    }

    public boolean onOptionsItemSelected(MenuItem item){
        //Intent myIntent = new Intent(getApplicationContext(), Setup.class);
        //startActivityForResult(myIntent, 0);
        finish();
        return true;
    }
}
