package com.shelfspace.michael.wayfinders;

import android.content.Context;
import android.content.res.Resources;

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

    //int boardWidth = this.context.getResources().getInteger(R.integer.boardWidth);
    //int multiplier = this.context.getResources().getInteger(R.integer.multiplier);
    int boardLength = 5;
    int boardWidth = 5;
    int multiplier = 3;


    private int baseValue;
    private int bonusValue;
    private int blue, green, orange, red, yellow;
    private int resources, workers;
    private ArrayList<Integer> islandNums = new ArrayList<Integer>();
    private ArrayList<Integer> indices = new ArrayList<Integer>();
    private Context context;

    public PlayerScore(Context ctext){
        super();
        baseValue = 0;
        blue = 0;
        green = 0;
        orange = 0;
        red = 0;
        yellow = 0;
        context = ctext;
        resources = 0;
        workers = 0;
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

    public int getWorkers() {
        return this.workers;
    }

    public void setWorkers(int workers) {
        this.workers = workers;
    }

    public int getResources() {
        return this.resources;
    }

    public void setResources(int resources) {
        this.resources = resources;
    }

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
        bonusScore += pointsUniqueColourIslands();
        bonusScore += islandGroups();
        bonusScore += islandCost();
        bonusScore += islandRow();
        bonusScore += islandColumn();
        bonusScore += islandSurround();
        bonusScore += awayFromHome();
        bonusScore += corners();
        bonusScore += squares();
        bonusScore += this.resources;
        bonusScore += this.workers;
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
        //if (this.getIslandNums().contains(context.getResources().getInteger(R.integer.islandBlue3))){
        if (this.getIslandNums().contains(4)){
            colourScore += this.blue * multiplier;
        }
        //Orange
        //if (this.getIslandNums().contains(this.context.getResources().getInteger(R.integer.islandOrange3))){
        if (this.getIslandNums().contains(13)){
            colourScore += this.orange * multiplier;
        }
        //Green
        //if (this.getIslandNums().contains(this.context.getResources().getInteger(R.integer.islandGreen3))){
        if (this.getIslandNums().contains(22)){
            colourScore += this.green * multiplier;
        }
        //Yellow
        //if (this.getIslandNums().contains(this.context.getResources().getInteger(R.integer.islandYellow3))){
        if (this.getIslandNums().contains(31)){
            colourScore += this.yellow * multiplier;
        }
        //Red
        //if (this.getIslandNums().contains(this.context.getResources().getInteger(R.integer.islandRed3))){
        if (this.getIslandNums().contains(40)){
            colourScore += this.red * multiplier;
        }

        return colourScore;
    }

    //Island 25: 3 points for each island of a different color you have settled.
    public int pointsUniqueColourIslands() {
        int uniqueColourScore = 0;
        //if (this.getIslandNums().contains(this.context.getResources().getInteger(R.integer.islandUniqueColour))){
        if (this.getIslandNums().contains(25)){
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

    //Island 16: 3 points for each separate group of islands you have settled. A group is defined as a series of one or more orthogonally adjacent islands settled by you.
    public int islandGroups() {
        int islandGroups = 0;
        //if (this.getIslandNums().contains(this.context.getResources().getInteger(R.integer.islandGroups))) {
        if (this.getIslandNums().contains(16)){
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

    //Island 26 + 35: 3 points for each island you have settled that costs X resources to settle. (26 = 3 resources, 35 = 4 resources.)
    public int islandCost() {
        int islandCount = 0;
        //if (this.getIslandNums().contains(this.context.getResources().getInteger(R.integer.island3Cost)) || (this.getIslandNums().contains(this.context.getResources().getInteger(R.integer.island4Cost)))){
        if (this.getIslandNums().contains(26) || this.getIslandNums().contains(35)){
            //Import the JSON file used for the Island data structure
            String myJson=inputStreamToString(this.context.getResources().openRawResource(R.raw.islands));
            //Create Island class based on JSON data
            final Island myModel = new Gson().fromJson(myJson, Island.class);
            //if (this.getIndices().contains(this.context.getResources().getInteger(R.integer.island3Cost))) {
            if (this.getIslandNums().contains(26)){
                //Need to cycle through all islands
                for (int islandNum : this.islandNums) {
                    if (myModel.islands.get(islandNum).cost.length == 3){
                        islandCount += 1;
                    }
                }
            }
            //if (this.getIndices().contains(this.context.getResources().getInteger(R.integer.island4Cost))) {
            if (this.getIslandNums().contains(35)){
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

    //Island 7: 3 points for each island you have settled that is in the same row as the one on which this symbol appears.
    public int islandRow() {
        int rowCount = 0;
        //if (this.getIslandNums().contains(this.context.getResources().getInteger(R.integer.islandRow))) {
        if (this.getIslandNums().contains(7)){
            //Island 7 counts itself in scoring
            rowCount += 1;
            //Get the index value of Island 7
            //int islandIndex = this.getIndices().get(this.getIslandNums().indexOf(this.context.getResources().getInteger(R.integer.islandRow)));
            int islandIndex = this.getIndices().get(this.getIslandNums().indexOf(7));
            //Track the horizontal position of Island 7
            int rowPosition = islandIndex % boardWidth;
            //rowPosition tells us the number of islands to the left of island 7. This for loop will run rowPosition # of times, with the first comparison being the island to the immediate left
            for (int i = 1; i <= rowPosition; i++){
                if (this.getIndices().contains(islandIndex - i)){
                    rowCount += 1;
                }
            }
            for (int i = 1; i < boardWidth; i++){
                if (this.getIndices().contains(islandIndex + i)){
                    rowCount += 1;
                }
            }
        }
        //Current multiplier is 3 points per island
        return rowCount * multiplier;
    }

    //Island 43: 3 points for each island you have settled that is in the same column as the one on which this symbol appears.
    public int islandColumn() {
        int columnCount = 0;
        //if (this.getIslandNums().contains(this.context.getResources().getInteger(R.integer.islandColumn))) {
        if (this.getIslandNums().contains(43)) {
            //Island 43 counts itself in scoring
            columnCount += 1;
            //Get the index value of Island 43
            //int islandIndex = this.getIndices().get(this.getIslandNums().indexOf(this.context.getResources().getInteger(R.integer.islandColumn)));
            int islandIndex = this.getIndices().get(this.getIslandNums().indexOf(43));
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
        //if (this.getIslandNums().contains(this.context.getResources().getInteger(R.integer.islandSurround))) {
        if (this.getIslandNums().contains(34)) {
            //Get the index value of Island
            //int islandIndex = this.getIndices().get(this.getIslandNums().indexOf(this.context.getResources().getInteger(R.integer.islandSurround)));
            int islandIndex = this.getIndices().get(this.getIslandNums().indexOf(34));
            //Track the horizontal position of Island 34
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
                surroundingPositions.remove(Integer.valueOf(islandIndex - (boardWidth - 1)));
                surroundingPositions.remove(Integer.valueOf(islandIndex - boardWidth));
                surroundingPositions.remove(Integer.valueOf(islandIndex - boardWidth - 1));
            }else if (rowPosition == 4){
                surroundingPositions.remove(Integer.valueOf(islandIndex + (boardWidth - 1)));
                surroundingPositions.remove(Integer.valueOf(islandIndex + boardWidth));
                surroundingPositions.remove(Integer.valueOf(islandIndex + boardWidth + 1));
            }

            if (columnPosition == 0){
                surroundingPositions.remove(Integer.valueOf(islandIndex - boardWidth - 1));
                surroundingPositions.remove(Integer.valueOf(islandIndex - 1));
                surroundingPositions.remove(Integer.valueOf(islandIndex + (boardWidth - 1)));
            } else if (columnPosition == 4){
                surroundingPositions.remove(Integer.valueOf(islandIndex - boardWidth + 1));
                surroundingPositions.remove(Integer.valueOf(islandIndex + 1));
                surroundingPositions.remove(Integer.valueOf(islandIndex + (boardWidth + 1)));
            }

            //Check to see if player has settled remaining islands
            for (int index : surroundingPositions) {
                if (this.getIndices().contains(index)){
                    surroundCount += 1;
                }
            }
        }
        return surroundCount * multiplier;
    }

    //Island 44: 3 points for each island you have settled that is 3 or more spaces away from the home island. For your information, there are 12 such islands on the map.
    public int awayFromHome() {
        int awayCount = 0;
        //if (this.getIslandNums().contains(this.context.getResources().getInteger(R.integer.awayFromHome))) {
        if (this.getIslandNums().contains(44)) {
            //int homeIndex = this.getIndices().get(this.getIslandNums().indexOf(this.context.getResources().getInteger(R.integer.islandHome)));

            //Need the position of the main Island. Should be in the middle of the board.
            //int homeIndex = this.getIndices().get(this.getIslandNums().indexOf(0));

            //This assumes the home is always on the middle of the board. Shouldn't be a hardcoded value of 12
            int homeIndex = 12;

            //Track the horizontal position of Island 44
            int rowPosition = homeIndex / boardWidth;
            int columnPosition = homeIndex % boardWidth;

            ArrayList<Integer> indices = new ArrayList<Integer>(this.getIndices());
            indices.remove(Integer.valueOf(homeIndex));

            for (int index : indices) {
                int tempRowPosition = index / boardWidth;
                int tempColumnPosition = index % boardWidth;
                if (Math.abs(rowPosition - tempRowPosition) + Math.abs(columnPosition - tempColumnPosition) >= 3){
                    awayCount += 1;
                }
            }
        }
        return awayCount * multiplier;
    }

    //Island 46: You must have settled 2 opposite corners of the map to receive points from this island.
    public int corners(){
        int corners = 0;
        if ((this.getIndices().contains(0) && this.getIndices().contains(24)) || (this.getIndices().contains(4) && this.getIndices().contains(20))){
            corners = 14;
        }
        return corners;
    }

    //Island 8: 3 points for each island in a 2x2 square of islands you have settled (i.e. 12 points per square). An island may not be part of more than 1 square.
    public int squares(){
        int squares = 0;
        //if (this.getIslandNums().contains(this.context.getResources().getInteger(R.integer.islandSquares))) {
        if (this.getIslandNums().contains(8)) {
            ArrayList<Integer> indices = new ArrayList<Integer>(this.getIndices());
            ArrayList<Integer> matchIndices = new ArrayList<Integer>();
            ArrayList<Integer> checkedIndices = new ArrayList<Integer>();
            boolean match = false;
            Collections.sort(indices);
            ArrayList<Integer> indicesRow = new ArrayList<Integer>();
            ArrayList<Integer> indicesColumn = new ArrayList<Integer>();
            //There is only one pattern of 10 islands that could mistakenly calculate one square instead of two. Identify if it has this shape, and remove treat it as two sqaures if so.
            if (this.indices.size() == 10){
                //We will check the row + column that the index variable lies on to see if at least 4 other islands are also settled here, and if they are all in order.
                int index = 0;
                //We don't need to check the last row + column because the shape that is formed needs at least 1 other 4 row/column to make.
                while (index < 24){
                    //Check row
                    if (indices.contains(index)){
                        //Check if this row has 4 islands settled consecutively
                        matchIndices = checkRow(indices, index);
                        matchIndices.add(index);
                        //check the next row under to see if it also has the same 4 consecutive column values settled
                        if (matchIndices.size() == 4){
                            //We have determined that there are two columns directly side by side of length 4. Can confirm there are 2 2x2 squares
                            //squares = 8;
                            //return squares * multiplier;

                            //checkedIndices = new ArrayList<Integer>(matchIndices);
                            matchIndices = checkRowBelow(indices, matchIndices);
                            //This means we have two rows of 4 consecutive islands settled, 2 in each column that are adjacent to each other
                            if (matchIndices.size() == 4) {
                                //We have determined that there are two columns directly side by side of length 4. Can confirm there are 2 2x2 squares
                                squares = 8;
                                return squares * multiplier;

                                /*for (int island : matchIndices) {
                                    checkedIndices.add(island);
                                }
                                Collections.sort(checkedIndices);
                                //Now we need to check if the islands above the 2nd and 3rd islands are both settled
                                //OR the islands below the 6th and 7th island
                                if ((indices.contains(Integer.valueOf(checkedIndices.get(1) - boardWidth)) && indices.contains(Integer.valueOf(checkedIndices.get(2) - boardWidth))) ||
                                        (indices.contains(Integer.valueOf(checkedIndices.get(5) + boardWidth)) && indices.contains(Integer.valueOf(checkedIndices.get(6) + boardWidth)))) {
                                    squares = 8;
                                    return squares * multiplier;
                                }*/
                            }
                        }

                        //check if the column has 4 islands settled consecutively;
                        matchIndices = checkColumn(indices, index);
                        matchIndices.add(index);
                        //check the next row under to see if it also has the same 4 consecutive column values settled
                        if (matchIndices.size() == 4){
                            //checkedIndices = new ArrayList<Integer>(matchIndices);
                            matchIndices = checkColumnRight(indices, matchIndices);
                            //This means we have two rows of 4 consecutive islands settled, 2 in each column that are adjacent to each other
                            if (matchIndices.size() == 4) {
                                //We have determined that there are two columns directly side by side of length 4. Can confirm there are 2 2x2 squares
                                squares = 8;
                                return squares * multiplier;
                                /*
                                for (int island : matchIndices) {
                                    checkedIndices.add(island);
                                }
                                Collections.sort(checkedIndices);*/


                                /*//Now we need to check if the islands above the 2nd and 3rd islands are both settled
                                //OR the islands below the 6th and 7th island
                                if ((Integer.valueOf(checkedIndices.get(2)) % boardWidth != 0) && (Integer.valueOf(checkedIndices.get(4)) % boardWidth != 0)){

                                }

                                if ((indices.contains(Integer.valueOf(checkedIndices.get(2) - boardWidth)) && indices.contains(Integer.valueOf(checkedIndices.get(4) - boardWidth))) ||
                                        (indices.contains(Integer.valueOf(checkedIndices.get(5) + boardWidth)) && indices.contains(Integer.valueOf(checkedIndices.get(6) + boardWidth)))) {
                                    squares = 8;
                                    return squares * multiplier;
                                }
                                */
                            }

                        }
                    }
                    //Thw next index spot to check is one column to the right, and one row down
                    index += boardLength + 1;
                }
            }
            while (!indices.isEmpty()) {
                int squareOne = indices.get(0);
                //this checks to make sure the value isn't on the far right side of the board, since we determine our squares from the top left island grouping
                if (squareOne % boardWidth < 4) {
                    if (indices.contains(Integer.valueOf(squareOne + 1)) && indices.contains(Integer.valueOf(squareOne + boardLength)) && indices.contains(Integer.valueOf(squareOne + 1 + boardLength))) {
                        squares += 4;
                        indices.remove(Integer.valueOf(squareOne + 1));
                        indices.remove(Integer.valueOf(squareOne + boardLength));
                        indices.remove(Integer.valueOf(squareOne + 1 + boardLength));
                    }
                }
                indices.remove(0);
            }
        }
        return squares * multiplier;
    }

    private ArrayList<Integer> checkRow(ArrayList<Integer> indices, int index){
        ArrayList<Integer> matchedRowIndices = new ArrayList<Integer>();
        //These are the number of remaining spots we need on the row that are connected to the index value
        //This will check all values to the left of the index
        for (int i = 1; i <= index % 5; i++){
            if (!indices.contains(index - i)){
                break;
            } else{
                matchedRowIndices.add(index - i);
            }
        }
        //This will check all values to the right of the index
        for (int i = 1; i < boardWidth - (index % 5); i++){
            if (!indices.contains(index + i)){
                break;
            } else{
                matchedRowIndices.add(index + i);
            }
        }
        if (matchedRowIndices.size() == 3){
            return matchedRowIndices;
        }
        matchedRowIndices = new ArrayList<Integer>();
        return matchedRowIndices;
    }

    private ArrayList<Integer> checkColumn(ArrayList<Integer> indices, int index){
        ArrayList<Integer> matchedRowIndices = new ArrayList<Integer>();
        //These are the number of remaining spots we need on the column that are connected to the index value
        //This will check all values on top of the index
        for (int i = 1; i <= index / 5; i++){
            if (!indices.contains(index - (boardWidth * i))){
                break;
            } else{
                matchedRowIndices.add(index - (boardWidth * i));
            }
        }
        //This will check all values below the index
        for (int i = 1; i < boardWidth - (index / 5); i++){
            if (!indices.contains(index + (boardWidth * i))){
                break;
            } else{
                matchedRowIndices.add(index + (boardWidth * i));
            }
        }
        if (matchedRowIndices.size() == 3){
            return matchedRowIndices;
        }
        matchedRowIndices = new ArrayList<Integer>();
        return matchedRowIndices;
    }

    private ArrayList<Integer> checkRowBelow(ArrayList<Integer> indices, ArrayList<Integer> matchIndices){
        ArrayList<Integer> matchedRowIndices = new ArrayList<Integer>();
        for (int index : matchIndices) {
            if (!indices.contains(index + boardWidth)){
                break;
            }{
                matchedRowIndices.add(index + boardWidth);
            }
        }
        if (matchedRowIndices.size() == 4){
            return matchedRowIndices;
        }
        matchedRowIndices = new ArrayList<Integer>();
        return matchedRowIndices;
    }

    private ArrayList<Integer> checkColumnRight (ArrayList<Integer> indices, ArrayList<Integer> matchIndices){
        ArrayList<Integer> matchedRowIndices = new ArrayList<Integer>();
        for (int index : matchIndices) {
            if (!indices.contains(index + 1)){
                break;
            }{
                matchedRowIndices.add(index + 1);
            }
        }
        if (matchedRowIndices.size() == 4){
            return matchedRowIndices;
        }
        matchedRowIndices = new ArrayList<Integer>();
        return matchedRowIndices;
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
