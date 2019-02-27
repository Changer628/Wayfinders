package com.shelfspace.michael.wayfinders;

import java.util.ArrayList;

/**
 * Created by Michael on 25/02/2019.
 */

public class GroupScore extends ArrayList{
    ArrayList<PlayerScore> playerScores;

    public GroupScore (ArrayList<PlayerScore> playerScores){
        this.playerScores = playerScores;
    }

    public ArrayList<Integer> totalScore (){
        //Calculate bonus values for all players
        //Need to cycle through all players
        ArrayList<Integer> totalScores = new ArrayList<Integer>();
        for (PlayerScore player : playerScores) {
            totalScores.add(player.totalScore());
        }
        return totalScores;
    }
}
