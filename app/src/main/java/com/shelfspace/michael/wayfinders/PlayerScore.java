package com.shelfspace.michael.wayfinders;

import android.content.Context;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * Created by Michael on 20/02/2019.
 */

public class PlayerScore {

    int boardWidth = this.context.getResources().getInteger(R.integer.boardWidth);
    int multiplier = this.context.getResources().getInteger(R.integer.multiplier);

    private int baseValue;
    private int bonusValue;
    private int blue, green, orange, red, yellow;
    private ArrayList<Integer> islandNums = new ArrayList<Integer>();
    private ArrayList<Integer> indices = new ArrayList<Integer>();
    private Context context;

    public PlayerScore(Context context){
        super();
        baseValue = 0;
        blue = 0;
        green = 0;
        orange = 0;
        red = 0;
        yellow = 0;
        context = this.context;
    }

    public int getBaseValue() {
        return baseValue;
    }

    public void setBaseValue(int baseValue) {
        this.baseValue = baseValue;
    }

    public int getBonusValue() {
        return bonusValue;
    }

    public void setBonusValue(int bonusValue) {
        this.bonusValue = bonusValue;
    }

    public ArrayList<Integer> getIslandNums() {
        return islandNums;
    }

    public void setIslandNums(ArrayList<Integer> islandNums) {
        this.islandNums = islandNums;
    }

    public ArrayList<Integer> getIndices() {
        return indices;
    }

    public void setIndices(ArrayList<Integer> indices) {
        this.indices = indices;
    }

    public boolean checkIslandNum (int num){
        if(this.islandNums.contains(num)){
            return true;
        }else{
            return false;
        }
    }

    //public void addIslandNum (int num){ this.islandNums.add(num); }

    //public void subIslandNum(int num){this.islandNums.remove(num);}

    public void addIsland (int islandNum, int index){
        this.islandNums.add(islandNum);
        this.indices.add(index);
    }

    public void subIsland(int islandNum, int index){
        this.islandNums.remove(Integer.valueOf(islandNum));
        this.indices.remove(Integer.valueOf(index));
    }

    public void addBaseValue(int baseValue){
        this.baseValue += baseValue;
    }

    public void subBaseValue(int baseValue){
        this.baseValue -= baseValue;
    }

    public void addColour(String colour){
        if (colour.equals("blue")) {
            this.blue += 1;
        } else if (colour.equals("green")) {
            this.green += 1;
        } else if (colour.equals("orange")) {
            this.orange += 1;
        } else if (colour.equals("red")) {
            this.red += 1;
        } else if (colour.equals("yellow")) {
            this.yellow += 1;
        }
    }

    public void subColour(String colour){
        if (colour.equals("blue")) {
            this.blue -= 1;
        } else if (colour.equals("green")) {
            this.green -= 1;
        } else if (colour.equals("orange")) {
            this.orange -= 1;
        } else if (colour.equals("red")) {
            this.red -= 1;
        } else if (colour.equals("yellow")) {
            this.yellow -= 1;
        }
    }

    //Calculate the bonus scores of islands the player has settled.
    public int bonusScore(){
        int bonusScore = 0;
        bonusScore += pointsColourIsland();
        return bonusScore;
    }

    public int totalScore(){
        int baseScore = this.getBaseValue();
        int bonusScore = this.bonusScore();
        int totalScore = baseScore + bonusScore;
        return totalScore;
    }

    //These are the islands that score 3 points for every colour of that island type you have
    //Island 4, 13, 22, 31, 40: 3 points for each yellow island you have settled. (The same symbol exists for other colors.)
    public int pointsColourIsland (){
        int colourScore = 0;
        //Blue
        if (this.getIslandNums().contains(this.context.getResources().getInteger(R.integer.islandBlue3))){
            colourScore += this.blue * multiplier;
        }
        //Orange
        if (this.getIslandNums().contains(this.context.getResources().getInteger(R.integer.islandOrange3))){
            colourScore += this.orange * multiplier;
        }
        //Green
        if (this.getIslandNums().contains(this.context.getResources().getInteger(R.integer.islandGreen3))){
            colourScore += this.green * multiplier;
        }
        //Yellow
        if (this.getIslandNums().contains(this.context.getResources().getInteger(R.integer.islandYellow3))){
            colourScore += this.yellow * multiplier;
        }
        //Red
        if (this.getIslandNums().contains(this.context.getResources().getInteger(R.integer.islandRed3))){
            colourScore += this.red * multiplier;
        }

        return colourScore;
    }

    //Island 25: 3 points for each island of a different color you have settled.
    public int pointsUniqueColourIslands() {
        int uniqueColourScore = 0;
        if (this.getIslandNums().contains(this.context.getResources().getInteger(R.integer.islandUniqueColour))){
            if (this.blue > 0){
                uniqueColourScore += 1;
            }
            if (this.orange > 0){
                uniqueColourScore += 1;
            }
            if (this.green > 0){
                uniqueColourScore += 1;
            }
            if (this.yellow > 0){
                uniqueColourScore += 1;
            }
            if (this.red > 0){
                uniqueColourScore += 1;
            }
        }
        return uniqueColourScore * multiplier;
    }

    //Island 17: 3 points for each separate group of islands you have settled. A group is defined as a series of one or more orthogonally adjacent islands settled by you.
    public int islandGroups() {
        int islandGroups = 0;
        if (this.getIslandNums().contains(this.context.getResources().getInteger(R.integer.islandGroups))) {
            ArrayList<Integer> islandIndices = new ArrayList<>(this.getIndices());
            Collections.sort(islandIndices);
            //This will represent all of the islands that make up one group
            ArrayList<Integer> activeGroup = new ArrayList<Integer>();

            //Check all island indices to form groups of islands
            while (islandIndices.isEmpty() == false) {
                activeGroup.add(islandIndices.get(0));
                islandIndices.remove(islandIndices.get(0));
                getGroups(islandIndices, activeGroup);
                islandGroups += 1;
            }
        }
        return islandGroups * multiplier;
    }

    private void getGroups (ArrayList<Integer> islandIndices, ArrayList<Integer> activeGroup){
        //Start from latest added value. We don't need to check the other values because the surrounding islands for other values will already be checked
        int currentIslandIndex = activeGroup.get(activeGroup.size()-1);
        //If current island isn't on the far right side, check if the player has scored the island to the right side of the current island
        if (currentIslandIndex % 5 != 4){
            if (islandIndices.contains(currentIslandIndex + 1)){
                activeGroup.add(currentIslandIndex + 1);
                islandIndices.remove(Integer.valueOf(currentIslandIndex + 1));
                getGroups(islandIndices, activeGroup);
            }
        }
        if (currentIslandIndex < 20){
            if (islandIndices.contains(currentIslandIndex + 5)){
                activeGroup.add(currentIslandIndex + 5);
                islandIndices.remove(Integer.valueOf(currentIslandIndex + 5));
                getGroups(islandIndices, activeGroup);
            }
        }
    }

    //Island 35 + 43: 3 points for each island you have settled that costs X resources to settle. (35 = 3 resources, 43 = 4 resources.)
    public int islandCost() {
        int islandCount = 0;
        if (this.getIslandNums().contains(this.context.getResources().getInteger(R.integer.island3Cost)) || (this.getIslandNums().contains(this.context.getResources().getInteger(R.integer.island4Cost)))){
            //Import the JSON file used for the Island data structure
            String myJson=inputStreamToString(this.context.getResources().openRawResource(R.raw.islands));
            //Create Island class based on JSON data
            final Island myModel = new Gson().fromJson(myJson, Island.class);
            if (this.getIndices().contains(this.context.getResources().getInteger(R.integer.island3Cost))) {
                //Need to cycle through all islands
                for (int islandNum : this.islandNums) {
                    if (myModel.islands.get(islandNum).cost.length == 3){
                        islandCount += 1;
                    }
                }
            }
            if (this.getIndices().contains(this.context.getResources().getInteger(R.integer.island4Cost))) {
                //Need to cycle through all islands
                for (int islandNum : this.islandNums) {
                    if (myModel.islands.get(islandNum).cost.length == 4){
                        islandCount += 1;
                    }
                }
            }
        }
        //Current multiplier is 3 points per island
        return islandCount * multiplier;
    }

    //Island 8: 3 points for each island you have settled that is in the same row as the one on which this symbol appears.
    public int islandRow() {
        int rowCount = 0;
        if (this.getIslandNums().contains(this.context.getResources().getInteger(R.integer.islandRow))) {
            //Island 8 counts itself in scoring
            rowCount += 1;
            //Get the index value of Island 8
            int islandIndex = this.getIndices().get(this.getIslandNums().indexOf(this.context.getResources().getInteger(R.integer.islandRow)));
            //Track the horizontal position of Island 8
            int rowPosition = islandIndex % boardWidth;
            //rowPosition tells us the number of islands to the left of island 8. This for loop will run rowPosition # of times, with the first comparison being the island to the immediate left
            for (int i = 1; i <= rowPosition; i++){
                if (this.getIndices().contains(islandIndex - i)){
                    rowCount += 1;
                }
            }
            for (int i = rowPosition + 1; i < boardWidth; i++){
                if (this.getIndices().contains(islandIndex + i)){
                    rowCount += 1;
                }
            }
        }
        //Current multiplier is 3 points per island
        return rowCount * multiplier;
    }

    //Island 26: 3 points for each island you have settled that is in the same column as the one on which this symbol appears.
    public int islandColumn() {
        int columnCount = 0;
        if (this.getIslandNums().contains(this.context.getResources().getInteger(R.integer.islandColumn))) {
            //Island 26 counts itself in scoring
            columnCount += 1;
            //Get the index value of Island 26
            int islandIndex = this.getIndices().get(this.getIslandNums().indexOf(this.context.getResources().getInteger(R.integer.islandColumn)));
            int checkIndex = islandIndex - this.context.getResources().getInteger(R.integer.boardLength);
            while (checkIndex >= 0){
                if (this.getIndices().contains(checkIndex)){
                    columnCount += 1;
                }
                checkIndex -= this.context.getResources().getInteger(R.integer.boardLength);
            }
            checkIndex = islandIndex + this.context.getResources().getInteger(R.integer.boardLength);
            while (checkIndex <= this.context.getResources().getInteger(R.integer.islandMaxIndex)){
                if (this.getIndices().contains(checkIndex)){
                    columnCount += 1;
                }
                checkIndex += this.context.getResources().getInteger(R.integer.boardLength);
            }
        }
        //Current multiplier is 3 points per island
        return columnCount * multiplier;
    }

    //Island 34: 3 points for each neighboring island you have settled. The neighboring islands are the 8 islands surrounding the one on which this symbol appears.
    public int islandSurround() {
        int surroundCount = 0;
        if (this.getIslandNums().contains(this.context.getResources().getInteger(R.integer.islandSurround))) {
            //Get the index value of Island
            int islandIndex = this.getIndices().get(this.getIslandNums().indexOf(this.context.getResources().getInteger(R.integer.islandSurround)));
            //Track the horizontal position of Island 8
            int rowPosition = islandIndex / boardWidth;
            int columnPosition = islandIndex % boardWidth;

            //There are a maximum of 8 islands surrounding island 34. They are calculated by the current position modified the following values
            //-1, -(width -1), -width, -width - 1, +1, +(width -1), + width, + width + 1
            ArrayList<Integer> surroundingPositions = new ArrayList<Integer>();
            surroundingPositions.add(islandIndex - 1);
            surroundingPositions.add(islandIndex - (boardWidth - 1));
            surroundingPositions.add(islandIndex - boardWidth);
            surroundingPositions.add(islandIndex - boardWidth - 1);
            surroundingPositions.add(islandIndex + 1);
            surroundingPositions.add(islandIndex + (boardWidth - 1));
            surroundingPositions.add(islandIndex + boardWidth);
            surroundingPositions.add(islandIndex + boardWidth + 1);


            //Initialize all of the above positiuons, then remove them as we find out they're unneeded.
            if (rowPosition == 0){
                surroundingPositions.remove(islandIndex - (boardWidth - 1));
                surroundingPositions.remove(islandIndex - boardWidth);
                surroundingPositions.remove(islandIndex - boardWidth - 1);
            }else if (rowPosition == 4){
                surroundingPositions.remove(islandIndex + (boardWidth - 1));
                surroundingPositions.remove(islandIndex + boardWidth);
                surroundingPositions.remove(islandIndex + boardWidth + 1);
            }

            if (columnPosition == 0){
                surroundingPositions.remove(islandIndex - boardWidth - 1);
                surroundingPositions.remove(islandIndex - 1);
                surroundingPositions.remove(islandIndex + (boardWidth - 1));
            } else if (columnPosition == 4){
                surroundingPositions.remove(islandIndex - boardWidth + 1);
                surroundingPositions.remove(islandIndex + 1);
                surroundingPositions.remove(islandIndex + (boardWidth + 1));
            }

            //Check to see if player has settled remaining islands
            for (int index : surroundingPositions) {
                if (this.getIndices().contains(index)){
                    surroundCount += 1;
                }
            }
        }
        return surroundCount;
    }

    //Used to convert the JSON data to string
    private String inputStreamToString(InputStream inputStream) {
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
