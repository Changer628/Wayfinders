package com.shelfspace.michael.wayfinders;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;


public class Setup extends AppCompatActivity {

    int playerNumber = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);
        //Import the JSON file used for the Island data structure
        String myJson=inputStreamToString(this.getResources().openRawResource(R.raw.islands));
        //Create Island class based on JSON data
        final Island myModel = new Gson().fromJson(myJson, Island.class);
        final ImageButton[] buttonList = new ImageButton[myModel.islands.size()];
        //Used to keep track of which islands we're using
        final ArrayList<Integer> islandNumbers = new ArrayList<Integer>();


        int resID = getResources().getIdentifier("setupSubmit", "id", getPackageName());
        Button submitButton = ((Button) findViewById(resID));
        submitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent = new Intent(Setup.this, BoardSetup.class);
                myIntent.putExtra("players", playerNumber);
                //Sort the numbers collected so that they'll appear in numerical order
                Collections.sort(islandNumbers);
                myIntent.putIntegerArrayListExtra("islands", (ArrayList<Integer>) islandNumbers );
                Setup.this.startActivity(myIntent);
            }
        });



        //Initialize all of the island buttons and give them an effect when pressed
        for (int i = 0; i < myModel.islands.size(); i++) {
            //Associate the buttons in the XML file with the data structure
            String buttonID = "";
            if (i < 10) {
                buttonID = "imageButton" + "0" + i;
            }else{
                buttonID = "imageButton" + i;
            }

            ////Log.d("test", buttonID);
            //Used to get the ID of the associated button for this island
            resID = getResources().getIdentifier(buttonID, "id", getPackageName());
            ////Log.d("test", String.valueOf(resID));
            buttonList[i] = ((ImageButton) findViewById(resID));

            final int finalI = i;
            buttonList[i].setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    //Check to see if this island is not yet selected
                    if (islandNumbers.contains(finalI) == false){
                        //Add to list if it isn't
                        islandNumbers.add(finalI);
                        //Visually indicate it's been selected
                        buttonList[finalI].setBackground(getDrawable(R.drawable.border));
                    }else{
                        //Remove island from list if it was already selected
                        islandNumbers.remove(Integer.valueOf(finalI));
                        //Visually indicate it's been removed
                        buttonList[finalI].setBackgroundResource(0);
                    }
                }
            });
        }
    }

    //Used to keep track of the number of players in the game
    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.player2Button:
                if (checked)
                    playerNumber = 2;
                    break;
            case R.id.player3Button:
                if (checked)
                    playerNumber = 3;
                    break;
            case R.id.player4Button:
                if (checked)
                    playerNumber = 4;
                break;
        }
    }
    /*public void buttonSwitch(int num){
        Log.d("test", "buttonSwitch Working");
        //Remove background highlight for all other buttons
        //Player count is 4 players, this should never change
        for (int i = 2; i < 5; i++){
            String buttonID = "player" + Integer.toString(i) + "Button";
            int tempId = getResources().getIdentifier(buttonID, "id", getPackageName());
            Button tempButton = ((Button) findViewById(tempId));
            if (num != i){
                Log.d("test", String.valueOf(tempButton.getDrawingCacheBackgroundColor()));
                //tempButton.setBackground(getDrawable(R.drawable.btn_default_normal));
                //tempButton.setBackgroundResource(android.R.drawable.btn_default);
            }else{
                tempButton.setBackgroundColor(0xFFF8EC1D);
            }
        }
    }*/

    //Used to convert the JSON data to string
    public String inputStreamToString(InputStream inputStream) {
        try {
            byte[] bytes = new byte[inputStream.available()];
            inputStream.read(bytes, 0, bytes.length);
            String json = new String(bytes);
            return json;
        } catch (IOException e) {
            return null;
        }
    }
}
