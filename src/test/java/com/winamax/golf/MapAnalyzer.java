package com.winamax.golf;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MapAnalyzer {

    public List<String> getPath(String dimensions, List<String> rows) {
        int width= getWith(dimensions);
        List<String> resultRows = analyseRows(rows, width);
        return resultRows;
    }

    private List<String> analyseRows(List<String> rows, int width) {
        List<String> resultRows= new ArrayList<>();
        for(String row : rows) {
            String rowWithDirection = findHorizontalDirection(width, row);
            row = removeAllH(rowWithDirection);
            resultRows.add(row);
        }
        return resultRows;
    }

    private String removeAllH(String direction) {
        return direction.replace("H", ".");
    }

    public String findHorizontalDirection(int width, String row) {
        String direction="";
        List<Integer> balles = getBallesFromRow(row);
        for(Integer balle:balles) {
            String thisballe = Integer.toString(balle);
            if (row.indexOf("H")==-1) direction="V";
            else if (row.indexOf("H") > row.indexOf(thisballe)) direction = ">";
            else direction = "<";
            row = row.replace(thisballe,direction);
        }
        return row;
    }

    private int getWith(String dimensions) {
        return Integer.parseInt(dimensions.split(" ")[0]);
    }

    public List<Integer> getBallesFromRow(String row) {
        List<Integer> balles=new ArrayList();
        row.chars().mapToObj(c -> (char) c).collect(Collectors.toList())
                .forEach(x->{if(Character.isDigit(x)) balles.add(Integer.parseInt(Character.toString(x)));});
        return balles;
    }
}
