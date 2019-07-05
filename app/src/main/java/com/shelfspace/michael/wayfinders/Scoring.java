package com.shelfspace.michael.wayfinders;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;

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


        Intent intent = getIntent();

        String priorActivity = intent.getStringExtra("callingActivity");

        if (priorActivity.equals("MainActivity")){

        }else{

        }

        //Set all player's score to 0 + hide unused players
        final int playerNumber = intent.getIntExtra("players", 0);
        setupIslands = getIntent().getIntegerArrayListExtra("setupIslands");

        //Setup scoring values for each player
        final ArrayList<PlayerScore> playerScores = new ArrayList<PlayerScore>();
        final GroupScore groupScore = new GroupScore();

        //This layout manages the custom radiobutton setup we have. Normally radiobuttons have to all be on one row or one column.
        int layoutID= getResources().getIdentifier("radGroup", "id", getPackageName());
        final ToggleButtonGroupTableLayout toggleButtonGroupTableLayout = (ToggleButtonGroupTableLayout) findViewById(layoutID);

        //While the max player count should always be 4, trying to track this down after a change would be a little tedious
        final int max = this.getResources().getInteger(R.integer.maxPlayers);
        //temp string for formatting
        String buttonID = "";

        //Populate islands onto the board
        final ImageButton[] buttonList = new ImageButton[setupIslands.size()];

        //Setup + Remove buttons for players that don't exist
        for (int i = 0; i < max; i++) {
            //Used to get the ID of the associated player button
            buttonID = "player" + (i+1) + "Score";
            final int radioID = getResources().getIdentifier(buttonID, "id", getPackageName());
            final RadioButton myRadioButton = (RadioButton) findViewById(radioID);
            //Set the initial player score of 0
            if (i < playerNumber) {
                playerScores.add(new PlayerScore(this));
                //myRadioButton.setText("Player " + (i + 1) + ": " + groupScore.playerScores.get(i).getBaseValue());
                myRadioButton.setText("Player " + (i + 1) + ": " + playerScores.get(i).getBaseValue());
                final int index = i;
                /*myRadioButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        activePlayer = playerScores.get(index);
                        activeIndex = index;
                    }
                });*/
                myRadioButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        final RadioButton rb = (RadioButton) v;
                        if ( toggleButtonGroupTableLayout.getCheckedRadioButtonId() != -1 ) {
                            //Uncheck the current activeButton
                            int activeButtonID = toggleButtonGroupTableLayout.getCheckedRadioButtonId();
                            RadioButton uncheckRadioButton = (RadioButton) findViewById(activeButtonID);
                            uncheckRadioButton.setChecked(false);
                        }
                        //Set the new active RadioButton
                        rb.setChecked(true);
                        toggleButtonGroupTableLayout.setActiveRadioButton(rb);
                        activePlayer = playerScores.get(index);
                        activeIndex = index;

                        //Update the borders for the current radioButton
                        updateSelectedIsland(activePlayer, activeIndex, buttonList);
                    }
                });
            } else {
                //disable the button as they aren't in the game
                myRadioButton.setVisibility(View.INVISIBLE);
                myRadioButton.setEnabled(false);
            }
        }

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
                    //This prevents app from crashing when no player has yet been selected to score
                    if (toggleButtonGroupTableLayout.getCheckedRadioButtonId() != -1){
                        if (activePlayer.checkIslandNum(myModel.islands.get(islandNum).number) == false){
                            activePlayer.addIsland(myModel.islands.get(islandNum).number, finalI);
                            activePlayer.addBaseValue(myModel.islands.get(islandNum).baseValue);
                            activePlayer.addColour(myModel.islands.get(islandNum).colour);
                            int borderID = getResources().getIdentifier("borderp" + (activeIndex + 1), "drawable", getPackageName());
                            buttonList[finalI].setBackground(getDrawable(borderID));
                        }else{
                            activePlayer.subIsland(myModel.islands.get(islandNum).number, finalI);
                            activePlayer.subBaseValue(myModel.islands.get(islandNum).baseValue);
                            activePlayer.subColour(myModel.islands.get(islandNum).colour);
                            //buttonList[finalI].setBackground(getDrawable(R.drawable.border));
                            buttonList[finalI].setBackgroundResource(0);
                        }

                        ////////////////////////////////////////////////////////
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
                        ////////////////////////////////////////////////////////

                        ArrayList<Integer> totalScores = new ArrayList<Integer>();
                        //totalScores = tempTotalPlayerScores(playerScores);

                        //Get the updated player scores with bonus values
                        totalScores = groupScore.totalScore(playerScores);

                        for (int i = 0; i < max; i++) {
                            //Used to get the ID of the associated player button
                            String tempButtonID = "player" + (i + 1) + "Score";
                            final int radioID = getResources().getIdentifier(tempButtonID, "id", getPackageName());
                            final RadioButton myRadioButton = (RadioButton) findViewById(radioID);
                            //Update the player score
                            if (i < playerNumber) {
                                myRadioButton.setText("Player " + (i + 1) + ": " + totalScores.get(i));
                            }
                        }


                        //TextView tv = (TextView)findViewById(R.id.textView3);
                        //tv.setText("Score: " + totalScore.getTotalScore());
                    }
                }
            });
        }

        int scoreID = getResources().getIdentifier("subResource", "id", getPackageName());
        Button subResource = ((Button) findViewById(scoreID));
        subResource.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (activePlayer != null){
                    TextView textView=(TextView)findViewById(R.id.resources);
                    int resources = activePlayer.getResources();
                    if (resources > 0){
                        activePlayer.setResources(resources - 1);
                        textView.setText(String.valueOf(activePlayer.getResources()));
                        scoreUpdate(activeIndex, playerNumber, activePlayer);
                    }
                }
            }
        });

        scoreID = getResources().getIdentifier("addResource", "id", getPackageName());
        Button addResource = ((Button) findViewById(scoreID));
        addResource.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (activePlayer != null){
                    TextView textView=(TextView)findViewById(R.id.resources);
                    activePlayer.setResources(activePlayer.getResources() + 1);
                    textView.setText(String.valueOf(activePlayer.getResources()));
                    scoreUpdate(activeIndex, playerNumber, activePlayer);
                }
            }
        });

        scoreID = getResources().getIdentifier("subWorker", "id", getPackageName());
        Button subWorker = ((Button) findViewById(scoreID));
        subWorker.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (activePlayer != null){
                    TextView textView=(TextView)findViewById(R.id.workers);
                    int workers = activePlayer.getWorkers();
                    if (workers > 0){
                        activePlayer.setWorkers(workers - 1);
                        textView.setText(String.valueOf(activePlayer.getWorkers()));
                        scoreUpdate(activeIndex, playerNumber, activePlayer);
                    }
                }
            }
        });

        scoreID = getResources().getIdentifier("addWorker", "id", getPackageName());
        Button addWorker = ((Button) findViewById(scoreID));
        addWorker.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (activePlayer != null){
                    TextView textView=(TextView)findViewById(R.id.workers);
                    activePlayer.setWorkers(activePlayer.getWorkers() + 1);
                    textView.setText(String.valueOf(activePlayer.getWorkers()));
                    scoreUpdate(activeIndex, playerNumber, activePlayer);
                }
            }
        });

        int submitID = getResources().getIdentifier("gameComplete", "id", getPackageName());
        Button endButton = ((Button) findViewById(submitID));
        endButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AlertDialog myQuittingDialogBox =new AlertDialog.Builder(Scoring.this)
                        //set message, title, and icon
                        .setTitle("Game Complete")
                        .setMessage("Are you sure you would like to end this session?")

                        //This is actually the button for No
                        .setPositiveButton("No", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.dismiss();
                            }

                        })


                        //This is actually the button for Yes
                        .setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //your deleting code
                                Intent intent = new Intent(Scoring.this,
                                        MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                dialog.dismiss();

                            }
                        })
                        .create();
                myQuittingDialogBox.show();
            }
        });


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

    /*
    public ArrayList<Integer> updateScore (ArrayList<ArrayList<Integer>> allPlayersIslands, ArrayList<ArrayList<Integer>> allPlayersIndices) {

    }*/

    //Temp method to get base values. We're gonna be doing it with the bonus values as a function as well, so I wanted my temp scoring to follow a similar format
    public ArrayList<Integer> tempTotalPlayerScores (ArrayList<PlayerScore> playerScores){
        ArrayList<Integer> totalScores = new ArrayList<Integer>();
        for (PlayerScore player : playerScores) {
            totalScores.add(player.getBaseValue());
        }
        return totalScores;
    }

    /*//The real method to get the player scores. Will also include the bonus values.
    public ArrayList<Integer> totalPlayerScores (ArrayList<ArrayList<Integer>> allPlayersIslands, ArrayList<ArrayList<Integer>> allPlayersIndices, ArrayList<PlayerScore> playerScores){

    }*/

    //Might be able to use radiobutton as input to get which colour. Delete this comment if unneeded
    public void updateSelectedIsland (PlayerScore playerScore, int playerIndex, ImageButton[] buttonList){
        //Get selected island positions from Playerscore
        ArrayList<Integer> islandIndices = playerScore.getIndices();

        //Go through every island, removing "selected" border and adding updated colour
        for(int i = 0; i < buttonList.length; i++){
            buttonList[i].setBackgroundResource(0);
            //Add updated border if this island has been selected
            if (islandIndices.contains(i)){
                int borderID = getResources().getIdentifier("borderp" + (playerIndex + 1), "drawable", getPackageName());
                buttonList[i].setBackground(getDrawable(borderID));
            }
        }
        //iterate through all buttons

        //Update Resource + Worker values
        TextView textView=(TextView)findViewById(R.id.resources);
        textView.setText(String.valueOf(playerScore.getResources()));

        textView=(TextView)findViewById(R.id.workers);
        textView.setText(String.valueOf(playerScore.getWorkers()));


    }


    public void scoreUpdate (int playerIndex, int playerNumber, PlayerScore playerScore){
        //Used to get the ID of the associated player button
        String tempButtonID = "player" + (playerIndex + 1) + "Score";
        final int radioID = getResources().getIdentifier(tempButtonID, "id", getPackageName());
        final RadioButton myRadioButton = (RadioButton) findViewById(radioID);
        //Update the player score
        if (playerIndex < playerNumber) {
            myRadioButton.setText("Player " + (playerIndex + 1) + ": " + playerScore.totalScore());
        }
    }

    public boolean onOptionsItemSelected(MenuItem item){
        finish();
        return true;
    }
}
