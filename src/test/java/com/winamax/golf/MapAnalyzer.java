package com.winamax.golf;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MapAnalyzer {

    public List<String> getPath(String dimensions, List<String> rows) throws TrouNonTrouveException {
        int width= getWith(dimensions);
        List<String> resultRows = analyseRows(rows, width);
        return resultRows;
    }

    private List<String> analyseRows(List<String> rows, int width) throws TrouNonTrouveException {
        List<String> resultRows= new ArrayList<>();
        int rownum=0;
        for(String row : rows) {
            String rowWithDirection = findHorizontalDirection(width, row,rownum,rows);
            row = removeAllH(rowWithDirection);
            resultRows.add(row);
            rownum++;
        }
        return resultRows;
    }

    private String removeAllH(String direction) {
        return direction.replace("H", ".");
    }

    public String findHorizontalDirection(int width, String row,int rownum,List<String>rows) throws TrouNonTrouveException {
        String direction="";
        List<Integer> balles = getBallesFromRow(row);
        for(Integer balle:balles) {
            String thisballe = Integer.toString(balle);
            if (row.indexOf("H")==-1) direction=returnDirectionOfOtherRowhavingH(rownum,rows);
            else if (row.indexOf("H") > row.indexOf(thisballe)) direction = ">";
            else direction = "<";
            row = row.replace(thisballe,direction);
        }
        return row;
    }

    private String returnDirectionOfOtherRowhavingH(int rownum, List<String> rows) throws TrouNonTrouveException {
        if (returnOtherRowhavingH(rownum,rows)>rownum) return "V";
        else return "^";
    }

    public int returnOtherRowhavingH(int rownum, List<String> rows) throws TrouNonTrouveException {
        for (int rowidx=0; rowidx<rows.size(); rowidx++)
        {
            if (rowidx==rownum)continue;
            if (rows.get(rowidx).contains("H")) return rowidx;
        }
        throw new TrouNonTrouveException();
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
