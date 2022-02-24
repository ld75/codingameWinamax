package com.winamax.golf;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MapAnalyzer {

    public List<String> getPath(String dimensions, List<String> rows) throws TrouNonTrouveException {
        int width= getWith(dimensions);
        List<String> resultRows = analyseRows(rows, width);
        if (isHTrouve(resultRows)) return removeAllH(resultRows);
        System.out.println(resultRows);
        return null;//return getPath(dimensions,rows);
    }

    public boolean isHTrouve(List<String> resultRows) throws TrouNonTrouveException {
        Integer[] positionHXY = trouveHPosition(resultRows);
        Integer y = positionHXY[1];
        Integer x = positionHXY[0];
            if(identifie(resultRows,x+1,y)== '<'
                || identifie(resultRows,x-1,y)== '>'
                || identifie(resultRows,x,y-1)== 'V'
                || identifie(resultRows,x,y+1)== '^')
                return true;
        return false;
    }

    private boolean isNOContains(List<String> resultRows, Integer y, Integer x, char droite) {
        return y > 0 && x > 0 && resultRows.get(y).charAt(x - 1) == droite;
    }
    private boolean isNEContains(List<String> resultRows, Integer y, Integer x, char droite) {
        return y > 0 && x < resultRows.get(y).length()-1 && resultRows.get(y).charAt(x + 1) == droite;
    }
    private boolean isSEContains(List<String> resultRows, Integer y, Integer x, char droite) {
        return y < resultRows.size()-1 && x < resultRows.get(y).length()-1 && resultRows.get(y+1).charAt(x + 1) == droite;
    }
    private boolean isSOContains(List<String> resultRows, Integer y, Integer x, char droite) {
        return y < resultRows.size()-1  && x > 0  && resultRows.get(y+1).charAt(x + 1) == droite;
    }

    public Integer[] trouveHPosition(List<String> resultRows) throws TrouNonTrouveException {
        for(int y=0; y<resultRows.size(); y++){
            int x=resultRows.get(y).indexOf("H");
            if (x!=-1) return new Integer[]{x,y};
        }
        throw new TrouNonTrouveException();
    }

    private List<String> removeAllH(List<String> resultRows) {
        return resultRows.stream().map(this::removeAllH).collect(Collectors.toList());
    }

    private List<String> analyseRows(List<String> rows, int width) throws TrouNonTrouveException {
        List<String> resultRows= new ArrayList<>();
        int rownum=0;
        for(String row : rows) {
            String rowWithDirection = findHorizontalDirection(width, row,rownum,rows);
            resultRows.add(rowWithDirection);
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

    public char identifie(List<String> rows, int x, int y) {
        if (x<0 || y<0 ||y>=rows.size() ||x>=rows.get(0).length()) return 'E';
        return rows.get(y).charAt(x);
        /*return rows.get(0)y > 0 && x > 0 && resultRows.get(y).charAt(x - 1) == droite;
                || (y<resultRows.size()-1 && resultRows.get(y).charAt(x +1)=='<')
                || (y>0 && resultRows.get(y-1).charAt(x)=='V')
                || (y<resultRows.size()-1 && resultRows.get(y+1).charAt(x)=='^')) return true;*/
    }
}
