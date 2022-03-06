package com.winamax.golf;

import java.util.ArrayList;
import java.util.List;

public class BallEtMap {
    private final List<String> rows;
    private final Ball balle;

    public BallEtMap(Ball balle, List<String> rowsCopie) {
        this.balle=balle;
        this.rows=rowsCopie;
    }


    public Ball getBalle() {
        return balle;
    }

    public List<String> getRows() {
        return this.rows;
    }

    public String getCheminLePlusCourt() {
        return this.balle.getCheminParcouruLePlusCourt();

    }
}
