package com.winamax.golf;

import java.util.ArrayList;
import java.util.List;

public class CombinaisonsBalleTrouGenerator {


    private final List<Trou> trous;
    private final List<Ball> balls;

    protected List<CoupleBalleTrou> instancieNewPrefix() {
        return new ArrayList<>();
    }

    public CombinaisonsBalleTrouGenerator(List<Ball> balls, List<Trou> trous) {
        this.balls=balls;
        this.trous= trous;
    }



    public List<CoupleBalleTrou> generate() {
        List<CoupleBalleTrou> listCouples = new ArrayList<>();
        int size = balls.size();
        for(int b = 0; b< size; b++) {
            CoupleBalleTrou couple = new CoupleBalleTrou(balls.get(b), trous.get(0));
            listCouples.add(couple);
        }
        return listCouples;
    }
}
