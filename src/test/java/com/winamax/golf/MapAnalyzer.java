package com.winamax.golf;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MapAnalyzer {

    public List<String> getPath(Ball balle, List<String> rows, Trou trou) throws TrouNonTrouveException {
        List<String> resultRows = analyseRows(balle, rows,trou);
        if (isHTrouve(resultRows, trou)) return removeThisTrou(resultRows,trou);
        return getPath(balle,resultRows, trou);
    }

    public boolean isHTrouve(List<String> resultRows, Trou trou) throws TrouNonTrouveException {
        Integer y = trou.y;
        Integer x = trou.x;
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

    public List<Trou> trouveTrous(List<String> resultRows) throws TrouNonTrouveException {
        List<Trou> trous = new ArrayList();
        for(int y=0; y<resultRows.size(); y++){
            for (int x=0; x<resultRows.get(y).length(); x++)
            {
                if (resultRows.get(y).charAt(x)=='H') trous.add(new Trou(new Integer[]{x,y}));
            }
        }
        if (trous.size()==0) throw new TrouNonTrouveException();
        return trous;
    }

    private List<String> removeThisTrou(List<String> resultRows, Trou trou) {
        resultRows.set(trou.y,changeCharAt(resultRows.get(trou.y),trou.x,"."));
        return resultRows;
    }

    private List<String> analyseRows(Ball balle, List<String> rows, Trou trou) throws TrouNonTrouveException {
        findHorizontalAndVerticalDirection(balle, rows,trou);
        return rows;
    }

    private String removeThisTrou(String direction) {
        return direction.replace("H", ".");
    }

    public void findHorizontalAndVerticalDirection(Ball balle, List<String> rows, Trou trou) throws TrouNonTrouveException {
        String direction="";
            if (balle.y!=trou.y) direction=returnDirectionOfOtherRowhavingH(balle.y,rows,trou.y);
            else if (trou.x > balle.x) direction = ">";
            else direction = "<";
        rows.set(balle.y, changeCharAt( rows.get(balle.y),balle.x,direction));

        changeBallPosition(balle, direction,rows);
    }

    public void changeBallPosition(Ball balle, String direction, List<String> rows) {
        if (isBallCanMoove(balle, direction, rows)) balle.score--;
        if (canMooveRight(balle, direction, rows)) balle.x++;
        if (canMooveLeft(balle, direction)) balle.x--;
        if (canMooveDown(balle, direction, rows)) balle.y++;
        if (canMooveUp(balle, direction)) balle.y--;
    }

    private boolean isBallCanMoove(Ball balle, String direction, List<String> rows) {
        return canMooveDown(balle, direction, rows) || canMooveLeft(balle, direction) || canMooveRight(balle, direction, rows) || canMooveUp(balle, direction);
    }

    private boolean canMooveUp(Ball balle, String direction) {
        return direction.equals("^") && balle.y > 0;
    }

    private boolean canMooveDown(Ball balle, String direction, List<String> rows) {
        return direction.equals("V") && balle.y < rows.size()-1;
    }

    private boolean canMooveLeft(Ball balle, String direction) {
        return direction.equals("<") && balle.x > 0;
    }

    private boolean canMooveRight(Ball balle, String direction, List<String> rows) {
        return direction.equals(">") && rows.get(0).length()-1 > balle.x;
    }

    private String returnDirectionOfOtherRowhavingH(int balleY, List<String> rows, Integer trouY){
        if (trouY >balleY) return "V";
        else return "^";
    }
    private int getWith(String dimensions) {
        return Integer.parseInt(dimensions.split(" ")[0]);
    }

    public List<Ball> getBallesFromRow(String row, int y) {
        List<Ball> balles=new ArrayList();
        List<Character> listDeChars = row.chars().mapToObj(c -> (char) c).collect(Collectors.toList());
        IntStream.range(0, listDeChars.size())
                .forEach(x -> {
                    Character valeurDeLaCase = listDeChars.get(x);
                    if(Character.isDigit(valeurDeLaCase)) {
                    int scoreBalle = Integer.parseInt(Character.toString(valeurDeLaCase));
                    balles.add(new Ball(scoreBalle,x,y));
                }
                });
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

    public List<Ball> trouverBalles(List<String> rows) {
        List<Ball> res =  new ArrayList<>();
        for(int yPos=0; yPos<rows.size(); yPos++){
            List<Ball> balles = getBallesFromRow(rows.get(yPos),yPos);
            res.addAll(balles);
        }
        return res;
    }

    public String changeCharAt(String str, int at, String by) {
        if (at==0) return by.concat(str.substring(1));
        if (at==str.length()) return str.substring(0,at-1).concat(by).concat(str.substring(at));
        return str.substring(0,at).concat(by).concat(str.substring(at+1));
    }


    public CoupleBalleTrou choisirCoupleBalleTrou(List<String> rows) throws TrouNonTrouveException, JeuIncompletException {
        List<Ball> balles = trouverBalles(rows);
        List<Trou> trous = trouveTrous(rows);
        if (trous.size()!=balles.size()) throw new JeuIncompletException();
        return new CoupleBalleTrou(balles.get(0),trous.get(0));
    }

    public List<String> jouerChaqueCouple(List<String> rows) throws TrouNonTrouveException, JeuIncompletException {
        CoupleBalleTrou couple = choisirCoupleBalleTrou(rows);
        List<String> res = getPath(couple.balle, rows, couple.trou);
        System.out.println(res);
        if (isRestEncoreTrous(res)) jouerChaqueCouple(res);
        return res;
    }

    private boolean isRestEncoreTrous(List<String> res) {
        boolean trouexist = false;
        try {
            return trouveTrous(res).size() > 0;
        } catch (TrouNonTrouveException e) {
            return false;
        }
    }
}
