package com.shelfspace.michael.wayfinders;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.Collections;

public class BoardSetup extends AppCompatActivity {

    int gridSize = 25;
    ArrayList<Integer> islandNumbers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_setup);

        Intent intent = getIntent();
        final int playerNumber = intent.getIntExtra("players", 0);
        islandNumbers = getIntent().getIntegerArrayListExtra("islands");


        final ImageButton[] buttonList = new ImageButton[gridSize];

        //Initialize all of the island buttons and give them an effect when pressed
        for (int i = 0; i < gridSize; i++) {
            //Associate the buttons in the XML file with the data structure
            String buttonID = "";
            if (i < 10) {
                buttonID = "boardIsland" + "0" + i;
            }else{
                buttonID = "boardIsland" + i;
            }

            ////Log.d("test", buttonID);
            //Used to get the ID of the associated button for this island
            final int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
            ////Log.d("test", String.valueOf(resID));
            buttonList[i] = ((ImageButton) findViewById(resID));

            final int index = i;
            buttonList[i].setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent myIntent = new Intent(BoardSetup.this, IslandSelection.class);
                    myIntent.putIntegerArrayListExtra("islands", (ArrayList<Integer>) islandNumbers);
                    myIntent.putExtra("buttonID", resID);
                    int oldIslandNum;
                    //Check to see if there is an existing island selected already. Will be null if none has been selected
                    if (buttonList[index].getTag() != null){
                        oldIslandNum = (int)buttonList[index].getTag();
                    }else{
                        //This means that the island is a default image
                        oldIslandNum = -1;
                    }
                    myIntent.putExtra("oldIslandNum", oldIslandNum);
                    //BoardSetup.this.startActivity(myIntent);
                    startActivityForResult(myIntent, 1);
                }
            });
        }
        int submitID = getResources().getIdentifier("boardSubmit", "id", getPackageName());
        Button submitButton = ((Button) findViewById(submitID));
        submitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ArrayList<Integer> orderedIslands = new ArrayList<Integer>();
                boolean setup = true;
                //Check if all Island spots have been filled. If not, notify user
                for (int i = 0; i < buttonList.length; i++){
                    if (buttonList[i].getTag() == null || (int)buttonList[i].getTag() == -1){
                        setup = false;
                        new AlertDialog.Builder(BoardSetup.this).setTitle("Setup Incomplete").setMessage("Please finish placing all island pieces to continue").setNeutralButton("Close", null).show();
                        break;
                        //Otherwise tag value is added to a variable to be passed onto next activity
                    }else{
                        orderedIslands.add((int)buttonList[i].getTag());
                    }
                }
                //We can move onto next activity if all islands have been selected
                if (setup){
                    Intent myIntent = new Intent(BoardSetup.this, Scoring.class);
                    //The setup order of the islands
                    myIntent.putIntegerArrayListExtra("setupIslands", (ArrayList<Integer>) orderedIslands);
                    //The number of players in the game
                    myIntent.putExtra("players", playerNumber);
                    BoardSetup.this.startActivity(myIntent);
                }
                /*Intent myIntent = new Intent(Setup.this, BoardSetup.class);
                myIntent.putExtra("players", playerNumber);
                //Sort the numbers collected so that they'll appear in numerical order
                Collections.sort(islandNumbers);
                myIntent.putIntegerArrayListExtra("islands", (ArrayList<Integer>) islandNumbers );
                Setup.this.startActivity(myIntent);*/
            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem item){
        finish();
        return true;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                ///Return the original button that was selected + the Island chosen
                int buttonID = data.getIntExtra("buttonID", 0);
                int selectedIsland = data.getIntExtra("islandImage", 0);
                int islandNum = data.getIntExtra("islandNum", 0);
                //Update button to reflect the image of the selected Island
                ImageButton button = ((ImageButton) findViewById(buttonID));
                button.setImageResource(selectedIsland);
                button.setTag(islandNum);
                islandNumbers.remove(Integer.valueOf(islandNum));
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }
}

