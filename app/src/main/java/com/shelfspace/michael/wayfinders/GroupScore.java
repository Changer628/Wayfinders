package com.shelfspace.michael.wayfinders;

import java.util.ArrayList;

/**
 * Created by Michael on 25/02/2019.
 */

public class GroupScore {
    ArrayList<PlayerScore> groupScore;

    public GroupScore (ArrayList<PlayerScore> groupScore){
        this.groupScore = groupScore;
    }

    private ArrayList<Integer> totalScore (){
        //Calculate bonus values for all players

        //Need to cycle through all players
        for (PlayerScore player : groupScore) {
            totalScores.add(player.getBaseValue());
        }
    }
}
