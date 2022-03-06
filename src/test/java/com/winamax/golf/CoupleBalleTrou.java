package com.winamax.golf;

public class CoupleBalleTrou {
        public final Trou trou;
        public final Ball balle;

        public CoupleBalleTrou(Ball ball, Trou trou) {
            this.balle = ball;
            this.trou = trou;
        }


    @Override
        public String toString() {
            return "CoupleBalleTrou{balle=" + balle.x +","+balle.y+",trou=" + trou.x+","+trou.y +'}';
        }
    }

