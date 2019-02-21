package com.shelfspace.michael.wayfinders;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioButton;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class Scoring extends AppCompatActivity {

    ArrayList<Integer> setupIslands;
    PlayerScore activePlayer;
    int activeIndex;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scoring);

        //Island data from JSON file
        String myJson=inputStreamToString(this.getResources().openRawResource(R.raw.islands));
        final Island myModel = new Gson().fromJson(myJson, Island.class);

        //Set all player's score to 0 + hide unused players
        Intent intent = getIntent();
        final int playerNumber = intent.getIntExtra("players", 0);
        setupIslands = getIntent().getIntegerArrayListExtra("setupIslands");

        //Setup scoring values for each player
        final ArrayList<PlayerScore> playerScores = new ArrayList<PlayerScore>();

        //While the max player count should always be 4, trying to track this down after a change would be a little tedious
        int max = Integer.parseInt(getString(R.string.MaxPlayers));
        //Remove buttons for players that don't exist
        String buttonID = "";
        for (int i = 0; i < max; i++) {
            //Used to get the ID of the associated player button
            buttonID = "player" + (i+1) + "Score";
            final int radioID = getResources().getIdentifier(buttonID, "id", getPackageName());
            RadioButton myRadioButton = (RadioButton) findViewById(radioID);
            //Set the initial player score of 0
            if (i < playerNumber) {
                playerScores.add(new PlayerScore());
                myRadioButton.setText("Player " + (i + 1) + ": " + playerScores.get(i).getBaseValue());
                final int index = i;
                myRadioButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        activePlayer = playerScores.get(index);
                        activeIndex = index;
                    }
                });
                if (i == 0){
                    myRadioButton.setChecked(true);
                    activePlayer = playerScores.get(index);
                    activeIndex = index;
                }
            } else {
                //disable the button as they aren't in the game
                myRadioButton.setVisibility(View.INVISIBLE);
                myRadioButton.setEnabled(false);
            }
        }

        //Populate islands onto the board
        final ImageButton[] buttonList = new ImageButton[setupIslands.size()];

        //Initialize all of the buttons in the activity
        for (int i = 0; i < setupIslands.size(); i++) {
            //Associate the buttons in the XML file with the selected islands
            buttonID = "";
            if (i < 10) {
                buttonID = "boardIsland" + "0" + i;
            }else{
                buttonID = "boardIsland" + i;
            }

            //Used to get the ID of the associated button for this island
            int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
            ////Log.d("test", String.valueOf(resID));
            buttonList[i] = ((ImageButton) findViewById(resID));

            //This is used to get the numerically correct image onto the imageButton
            final int islandNum = setupIslands.get(i);
            String stringNum = String.valueOf(islandNum);
            if (islandNum < 10){
                stringNum = "0" + stringNum;
            }

            //Sets the corresponding image. imageID is also what we want to send back to the previous activity to update the button selected
            final int imageID = getResources().getIdentifier("island" + stringNum, "drawable", getPackageName());
            buttonList[i].setImageResource(imageID);

            final int finalI = i;
            //Set what happens when we press a button
            buttonList[i].setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (activePlayer.checkIslandNum(myModel.islands.get(islandNum).number) == false){
                        Log.d("test", "button pressed");
                        activePlayer.addIsland(myModel.islands.get(islandNum).number, finalI);
                        activePlayer.addBaseValue(myModel.islands.get(islandNum).baseValue);
                        activePlayer.addColour(myModel.islands.get(islandNum).colour);
                        buttonList[finalI].setBackground(getDrawable(R.drawable.border));
                    }else{
                        Log.d("test", "button unpressed");
                        activePlayer.subIsland(myModel.islands.get(islandNum).number, finalI);
                        activePlayer.subBaseValue(myModel.islands.get(islandNum).baseValue);
                        activePlayer.subColour(myModel.islands.get(islandNum).colour);
                        //buttonList[finalI].setBackground(getDrawable(R.drawable.border));
                        buttonList[finalI].setBackgroundResource(0);
                    }

                    //Store all the island numbers settled by each player (will also need their relative position)
                    ArrayList<ArrayList<Integer>> allPlayersIslands = new ArrayList<ArrayList<Integer>>();
                    ArrayList<ArrayList<Integer>> allPlayersIndices = new ArrayList<ArrayList<Integer>>();
                    ArrayList<Integer> entry = new ArrayList<Integer>();
                    //Update scores on all 4 players after the island is selected.
                    //This is because there is one island that scores based on islands that other players have settled on
                    //The first step is to get all of the information regarding the islands settled and their position
                    for (int i = 0; i < playerNumber; i++){
                        entry = playerScores.get(i).getIslandNums();
                        allPlayersIslands.add(entry);
                        entry = playerScores.get(i).getIndices();
                        allPlayersIndices.add(entry);
                    }
                    //TextView tv = (TextView)findViewById(R.id.textView3);
                    //tv.setText("Score: " + totalScore.getTotalScore());
                }
            });
        }
    }

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
