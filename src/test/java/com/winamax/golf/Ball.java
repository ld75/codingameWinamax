package com.winamax.golf;

import java.util.*;
import java.util.stream.IntStream;

import static com.winamax.golf.MapAnalyzer.changeCharAt;

public class Ball {
    public Position position = new Position();
    public int scoreDecrementalDessai;
    public int score;
    public Stack<Case> stackDirection= new Stack();
    public int scoreInitial;
    private List<String> stackDirectionsFound = new ArrayList<>();
    private LinkedList<Integer[]> blocageQueue = new LinkedList<>();
    private List<String> invalidPaths = new ArrayList<>();
    private List<Case> dernierPas = new ArrayList();
    private List<Trou> listeLacs = new ArrayList<>();
    private List<Integer> idDirectionForScore = new ArrayList<>();
    private int limiteStrategieDirection=0;

    public Ball(int scoreBalle, int x, int y) {
        this.score=scoreBalle;
        this.scoreInitial=scoreBalle;
        this.scoreDecrementalDessai =scoreBalle;
        this.position=new Position(x,y);
    }

    public Ball() {

    }

    public void removeLastStep(List<String> rows) {
        System.out.println("ballstack: " + this.stackDirection);
        Case casepoint = this.stackDirection.pop();
        ecrireLeChemin(casepoint.position, rows, casepoint.casepoint);
//        Case avantderniercase = this.stackDirection.lastElement();
//        ecrireLeChemin(avantderniercase.position, rows, avantderniercase.casepoint);
    }
    public String getLastDirection(Ball balle) throws StrategiePerdante {
        try {
            Case prev = this.stackDirection.peek();
            return prev.direction;

        } catch (Exception e) {
            throw new StrategiePerdante(balle);
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
        return stackDirection.stream().map(Case::getDirection).reduce((first, second) -> first.concat(second)).get();
    }

    public void ajouteChemin(Case direction) {
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




    public static Ball createNewInstance(Ball ball) {
        Ball ret = new Ball();
        ret.setX(ball.getX());
        ret.setY(ball.getY());
        ret.score=ball.score;
        ret.scoreInitial=ball.scoreInitial;
        ret.scoreDecrementalDessai=ball.scoreDecrementalDessai;
        ret.stackDirection=(Stack<Case>) ball.stackDirection.clone();
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

    public void invaliderLeSousChemin(Ball balle, List<String> rows) {
        if (balle.score>1) {
            for (int i = dernierPas.size() ; i>score; i--){
                Case casepoint = dernierPas.get(i);
                ecrireLeChemin(casepoint.position, rows, casepoint.casepoint);
            }
        }
        if (score==-1) {
            invalidPaths.add(stackToString());
            System.out.println("invalider "+stackToString()+ " scoreDecremental "+this.scoreDecrementalDessai);
        }
    }


    @Override
    public boolean equals(Object o)
    {
        if(this.getX()==((Ball)o).getX() && this.getY()==((Ball)o).getY())  return true;
        return false;
    }


    public String imprimeStak() {
        return stackDirection.toString();
    }

    @Override
    public String toString() {
        return "Ball{" +
                "x=" + getX() +
                ", y=" + getY() +
                '}';
    }


    public void siLacMarqueLac(char identifie) {
        if (identifie=='X') listeLacs.add(new Trou(this.getX(),this.getY()));
    }
    int getX(){
        return position.x;
    }
    void setX(int x){
        position.x=x;
    }
    int getY(){
        return position.y;
    }
    void setY(int y){
        position.y=y;
    }

    public void decrementY() {
        position.y--;
    }

    public void incrementY() {
        position.y++;
    }

    public void decrementX() {
        position.x--;
    }

    public void incrementX() {
        position.x++;
    }

    protected void ecrireAuPropreLeChemin(List<String> rows, String chemin) {
        for (int i = 0; i< chemin.length(); i++){
            String direction= String.valueOf(chemin.charAt(i));
            ecrireLeChemin(this.position, rows, direction);
            changeBallPosition(direction);
        }
    }

    public void ecrireLeChemin(Position position, List<String> rows, String direction) {
        rows.set(position.y, changeCharAt( rows.get(position.y), position.x, direction));
    }

    public void changeBallPosition(String direction) {
        if (direction.equals(">")) this.incrementX();;
        if (direction.equals("<")) this.decrementX();
        if (direction.equals("v")) this.incrementY();
        if (direction.equals("^")) this.decrementY();
    }

    public int getIdDirectionForScore() {
        return idDirectionForScore.get(score);
    }

    public String getDirectionForScore(List<String> strategieDirections){
        Integer idDirection = idDirectionForScore.get(score);
        return strategieDirections.get(idDirection);
    }

    public void incrementIdDirectionForScore() {
        idDirectionForScore.set(score,idDirectionForScore.get(score)+1);
    }

    public void initIDDirectionForScores(List<String> strategieDirections) {
        IntStream.range(0, score+1).forEach(s->{
            idDirectionForScore.add(0);
        });
        this.limiteStrategieDirection = strategieDirections.size();
    }

    public boolean isToutesLesDirectonsOntJouePourCeScore() {
        return getIdDirectionForScore()==this.limiteStrategieDirection;
    }

    public void resetIdDirectionForScore() {
        idDirectionForScore.set(score,0);
    }
}
