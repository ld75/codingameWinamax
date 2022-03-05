package com.winamax.golf;

import java.util.ArrayList;

public class BallEtMap {
    private final ArrayList<String> rows;
    private final Ball balle;

    public BallEtMap(Ball balle, ArrayList<String> rowsCopie) {
        this.balle=balle;
        this.rows=rowsCopie;
    }


    public Ball getBalle() {
        return balle;
    }

    public ArrayList<String> getRows() {
        return this.rows;
    }

    public String getCheminLePlusCourt() {
        return this.balle.getCheminParcouruLePlusCourt();

    }
}
