package com.winamax.golf;

import org.junit.jupiter.api.extension.ExtensionContext;

import java.util.List;

public class CouplesBalleTrou {
    public List<CoupleBalleTrou> couples;

    public void addCouple(Ball ball, Trou trou) {
        couples.add(new CoupleBalleTrou(ball,trou));
    }


    public class CoupleBalleTrou() {
    public final Trou trou;
    public final Ball balle;

    public CoupleBalleTrou(Ball ball, Trou trou) {
        this.balle = ball;
        this.trou = trou;
    }
    @Override
    public String toString() {
        return "CoupleBalleTrou{balle=" + balle.x +",trou=" + trou.x +'}';
    }
}
}