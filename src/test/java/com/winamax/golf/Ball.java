package com.winamax.golf;

import java.util.*;

public class Ball {
    public int scoreDecrementalDessai;
    public int score;
    public int x;
    public int y;
    private Stack<String> stackDirection= new Stack();
    private List<String> stackDirectionsFound = new ArrayList<>();
    private LinkedList<Integer[]> blocageQueue = new LinkedList<>();
    private List<String> invalidPaths = new ArrayList<>();
    private LinkedList<String> dernierPas = new LimitedQueue(2);

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
        return this.scoreDecrementalDessai <1;
    }

    public void sauveParcourtEtReinitStack() {
        this.stackDirectionsFound.add(stackToString());
        this.stackDirection=new Stack<>();
    }

    public void decrementeScoreDessai() {
        scoreDecrementalDessai--;
        score=scoreDecrementalDessai;
    }

    public void ajouteStackDeblocage(List<String> rowsCopie) {
        MapAnalyzer.changeCharAt(rowsCopie, x, y, "O");
        addBlocage(rowsCopie,x,y);
    }

    private void addBlocage(List<String> rowsCopie,int x, int y) {
        if (blocageQueue.size()>2) deleteFirstBlocage(rowsCopie);
        blocageQueue.add(new Integer[]{x,y});
    }

    private void deleteFirstBlocage(List<String> rowsCopie) {
        Integer[] oldBlocage = blocageQueue.removeFirst();
        MapAnalyzer.changeCharAt(rowsCopie, oldBlocage[0], oldBlocage[1], ".");
    }

    public static Ball createNewInstance(Ball ball) {
        Ball ret = new Ball();
        ret.x=ball.x;
        ret.y=ball.y;
        ret.score=ball.score;
        ret.scoreDecrementalDessai=ball.scoreDecrementalDessai;
        ret.stackDirection=(Stack<String>) ball.stackDirection.clone();
        ret.stackDirectionsFound= new ArrayList(Arrays.asList(ball.stackDirectionsFound.toArray()));
        ret.invalidPaths= new ArrayList(Arrays.asList(ball.invalidPaths.toArray()));
        return ret;
    }

    public static Ball copieExperience(Ball Actualballe, Ball originalBalle) {
        Ball copieOriginale = createNewInstance(originalBalle);
        List<String> stackDirectionLePlusCourt = new ArrayList<>();
        stackDirectionLePlusCourt.add(Actualballe.getCheminParcouruLePlusCourt());
        copieOriginale.stackDirectionsFound=stackDirectionLePlusCourt;
        copieOriginale.scoreDecrementalDessai=Actualballe.scoreDecrementalDessai;
        copieOriginale.score=Actualballe.scoreDecrementalDessai;
        copieOriginale.stackDirection=new Stack<>();
        copieOriginale.invalidPaths=new ArrayList<>();
        return copieOriginale;
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

    public void resetInvalidPaths() {
        this.invalidPaths=new ArrayList<>();
    }

    public String imprimeStak() {
        return stackDirection.toString();
    }

    @Override
    public String toString() {
        return "Ball{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

    public String getAvantDerniereCase() {
        try {
            return this.dernierPas.getLast();
        } catch (Exception e) {
            return "";
        }
    }

    public void ajouteHistorique(char identifie) {
        this.dernierPas.add(Character.toString(identifie));
    }
}
