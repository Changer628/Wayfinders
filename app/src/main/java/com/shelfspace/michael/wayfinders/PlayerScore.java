package com.shelfspace.michael.wayfinders;

import java.util.ArrayList;

/**
 * Created by Michael on 20/02/2019.
 */

public class PlayerScore {
    private int baseValue;
    private int bonusValue;
    private int blue, green, orange, red, yellow;
    private ArrayList<Integer> islandNums = new ArrayList<Integer>();
    private ArrayList<Integer> indices = new ArrayList<Integer>();

    public PlayerScore(){
        super();
        baseValue = 0;
        blue = 0;
        green = 0;
        orange = 0;
        red = 0;
        yellow = 0;
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
}
