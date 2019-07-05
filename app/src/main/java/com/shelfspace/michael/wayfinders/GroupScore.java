package com.shelfspace.michael.wayfinders;

import java.util.ArrayList;

/**
 * Created by Michael on 25/02/2019.
 */

public class GroupScore {

    int multiplier = 3;

    public ArrayList<Integer> totalScore (ArrayList<PlayerScore> playerScores){
        //Calculate bonus values for all players
        //Need to cycle through all players
        ArrayList<Integer> baseScores = new ArrayList<Integer>();

        ArrayList<Integer> totalScores = new ArrayList<Integer>();
        for (PlayerScore player : playerScores) {
            baseScores.add(player.totalScore());
        }

        ArrayList<Integer> bonusScores = new ArrayList<Integer>(islandSameSettled(playerScores));
        for (int i = 0; i < playerScores.size(); i++){
            totalScores.add(baseScores.get(i) + bonusScores.get(i));
        }
        return totalScores;
    }

    //Island 17: 3 points for each island you have settled that was also settled by another player.
    private ArrayList<Integer> islandSameSettled (ArrayList<PlayerScore> playerScores){
        //Used to keep the score of each individual player
        ArrayList<Integer> sameSettledPoints = new ArrayList<Integer>();
        for (int i = 0; i < playerScores.size(); i++){
            if (playerScores.get(i).getIslandNums().contains(17)){
                ArrayList<Integer> matchingIndex = new ArrayList<Integer>();
                ArrayList<Integer> activeIndex = playerScores.get(i).getIndices();
                for (int j = 0; j < playerScores.size(); j++){
                    //make sure we are not comparing the same player
                    if (i != j){
                        ArrayList<Integer> otherIndex = playerScores.get(j).getIndices();
                        for (int index : activeIndex) {
                            if (otherIndex.contains(index) && !matchingIndex.contains(index)){
                                matchingIndex.add(index);
                            }
                        }
                    }
                }
                sameSettledPoints.add(matchingIndex.size() * multiplier);
            }else{
                sameSettledPoints.add(0);
            }
        }
        return sameSettledPoints;
    }
}
