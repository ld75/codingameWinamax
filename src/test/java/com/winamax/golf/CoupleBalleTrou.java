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

    @Override
    public boolean equals(Object obj) {
            if(this==obj) return true;
            if(!(obj instanceof CoupleBalleTrou)) return false;
            CoupleBalleTrou autreCoupleBalleTrou = (CoupleBalleTrou)obj;
            if (autreCoupleBalleTrou==null) return false;
            if (autreCoupleBalleTrou.balle.equals(this.balle) && autreCoupleBalleTrou.trou.equals(this.trou)) return true;
            return false;
    }
}

