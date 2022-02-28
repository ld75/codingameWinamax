package com.winamax.golf;

import java.util.*;

import static java.util.stream.Collectors.toSet;

public class Ball {
    public int scoreDecrementalDessai;
    public int score;
    public int x;
    public int y;
    private Stack<String> stackDirection= new Stack();
    private List<String> stackDirectionsFound = new ArrayList<>();
    private LinkedList<Integer[]> blocageQueue = new LinkedList<>();
    private List<String> invalidPaths = new ArrayList<>();

    public Ball(int scoreBalle, int x, int y) {
        this.score=scoreBalle;
        this.scoreDecrementalDessai =scoreBalle;
        this.x= x;
        this.y=y;
    }

    public Ball() {

    }

    public String removeLastStep() {
        try {
            String prev = this.stackDirection.pop();
            return prev;
        } catch (Exception e) {
            return "0";
        }
    }
    public String getLastDirection() {
        try {
            String prev = this.stackDirection.peek();
            return prev;

        } catch (Exception e) {
            return "0";
        }
    }

    public String getCheminParcouruLePlusCourt() {
        String winnerPath="";
        for (int i = 0; i< stackDirectionsFound.size(); i++)
        {
            if (winnerPath.length()==0) winnerPath= stackDirectionsFound.get(i);
            if (stackDirectionsFound.get(i).length()<winnerPath.length()) winnerPath= stackDirectionsFound.get(i);
        }
        return winnerPath;
    }

    private String stackToString() {
        return stackDirection.stream().reduce((first, second) -> first.concat(second)).get();
    }

    public void ajouteChemin(String direction) {
        this.stackDirection.push(direction);
    }

    public void decrementeScore() {
        score--;
    }

    public void incrementeScore() {
        score++;
    }

    public boolean isAEssayeChaqueScore() {
        return this.scoreDecrementalDessai <2;
    }

    public void sauveParcourtEtReinitStack() {
        this.stackDirectionsFound.add(stackToString());
        this.stackDirection=new Stack<>();
    }

    public void decrementeScoreDessai() {
        scoreDecrementalDessai--;
        score=scoreDecrementalDessai;
    }

    public void ajouteStackDeblocage(ArrayList<String> rowsCopie) {
        MapAnalyzer.changeCharAt(rowsCopie, x, y, "O");
        addBlocage(rowsCopie,x,y);
    }

    private void addBlocage(ArrayList<String> rowsCopie,int x, int y) {
        if (blocageQueue.size()>2) deleteFirstBlocage(rowsCopie);
        blocageQueue.add(new Integer[]{x,y});
    }

    private void deleteFirstBlocage(ArrayList<String> rowsCopie) {
        Integer[] oldBlocage = blocageQueue.removeFirst();
        MapAnalyzer.changeCharAt(rowsCopie, oldBlocage[0], oldBlocage[1], ".");
    }

    public Ball createNewInstance() {
        Ball ret = new Ball();
        ret.x=this.x;
        ret.y=this.y;
        ret.score=this.score;
        ret.scoreDecrementalDessai=this.scoreDecrementalDessai;
        ret.stackDirection=this.stackDirection;
        ret.stackDirectionsFound=this.stackDirectionsFound;
        ret.invalidPaths=this.invalidPaths;
        return ret;
    }

    public void copieExperience(Ball balle) {
        this.stackDirectionsFound=balle.stackDirectionsFound;
        this.scoreDecrementalDessai=balle.scoreDecrementalDessai;
        this.score=scoreDecrementalDessai;
        this.stackDirection=new Stack<>();
        this.invalidPaths=balle.invalidPaths;
    }

    public boolean isATrouveChemin() {
        return stackDirectionsFound.size()>0;
    }

    public void invaliderLeCheminComplet() {
        if (score==-1) {
            invalidPaths.add(stackToString());
            System.out.println("invalider "+stackToString()+ " scoreDecremental "+this.scoreDecrementalDessai);
            System.out.println("");
        }
    }

    public List<String> getInvalidPaths() {
        return this.invalidPaths;
    }

    public void setInvalidPaths(List<String> invalidPaths) {
        this.invalidPaths=invalidPaths;
    }


    public boolean isTourneEnRond() {
        Set set = new HashSet() ;
        set.addAll(this.invalidPaths) ;
        return this.invalidPaths.size()!=set.size();
    }
    @Override
    public boolean equals(Object o)
    {
        if(this.x==((Ball)o).x && this.y==((Ball)o).y)  return true;
        return false;
    }
}
