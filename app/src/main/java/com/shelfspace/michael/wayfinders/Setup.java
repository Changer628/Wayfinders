package com.shelfspace.michael.wayfinders;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;


public class Setup extends AppCompatActivity {

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
            int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
            ////Log.d("test", String.valueOf(resID));
            buttonList[i] = ((ImageButton) findViewById(resID));

            final int finalI = i;
            buttonList[i].setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // Code here executes on main thread after user presses button
                    //Intent intent = new Intent(v.getContext(), Scoring.class);
                    //startActivity(intent);
                    if (islandNumbers.contains(finalI) == false){
                        //totalScore.addName(myModel.cards.get(finalI).name);
                        //totalScore.addBaseValue(myModel.cards.get(finalI).baseValue);
                        //Log.d("test", String.valueOf(myModel.cards.get(finalI).baseValue));
                        //totalScore.addColour(myModel.cards.get(finalI).color);
                        //if(myModel.cards.get(finalI).attributes != null){
                        //    totalScore.addAttributes(myModel.cards.get(finalI).attributes);
                        //}
                        islandNumbers.add(finalI);
                        buttonList[finalI].setBackground(getDrawable(R.drawable.border));
                        Log.d("test", Integer.toString(finalI));
                    }else{
                        //totalScore.subName(myModel.cards.get(finalI).name);
                        //totalScore.subBaseValue(myModel.cards.get(finalI).baseValue);
                        //totalScore.subColour(myModel.cards.get(finalI).color);
                        //if(myModel.cards.get(finalI).attributes != null){
                        //   totalScore.subAttributes(myModel.cards.get(finalI).attributes);
                        //}
                        //buttonList[finalI].setBackground(getDrawable(R.drawable.border));

                        //Need to covert to string because otherwise it removes by index
                        islandNumbers.remove(Integer.valueOf(finalI));
                        buttonList[finalI].setBackgroundResource(0);
                        Log.d("test", Integer.toString(finalI));
                    }

                    //TextView tv = (TextView)findViewById(R.id.textView3);
                    //tv.setText("Score: " + totalScore.getTotalScore());
                }
            });
        }

        final Button buttonP2 = (Button) findViewById(R.id.player2Button);
        buttonP2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
            }
        });
    }

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
