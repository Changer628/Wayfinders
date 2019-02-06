package com.shelfspace.michael.wayfinders;

import java.util.ArrayList;

/**
 * Created by Michael on 28/01/2019.
 */

public class Island {
    public ArrayList<MyObject> islands;

    static public class MyObject {
        public String name;
        public int baseValue;
        public String color;
        public String[] cost;
        public boolean ability;
    }
}
