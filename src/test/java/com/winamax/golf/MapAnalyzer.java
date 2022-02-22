package com.winamax.golf;

import java.util.List;

public class MapAnalyzer {

    public String getPath(String dimensions, List<String> rows) {
        int width= getWith(dimensions);
        String direction = findHorizontalDirection(width,rows.get(0));
        return ">.";
    }

    public String findHorizontalDirection(int width, String row) {
        String direction="";
        if(row.indexOf("H")<row.indexOf("1")) direction= ">";
        if(row.indexOf("H")>row.indexOf("1")) direction= "<";
        return row.replace("1",direction);
    }

    private int getWith(String dimensions) {
        return Integer.parseInt(dimensions.split(" ")[0]);
    }

}
