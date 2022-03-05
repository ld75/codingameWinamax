package com.winamax.golf;

import java.util.List;

public class StrategiePerdante extends Exception {
    private Ball ballWithInvalidPaths;

    public Ball getBallWithInvalidPaths() {
        return this.ballWithInvalidPaths;
    }
    StrategiePerdante(Ball balle)
    {
        this.ballWithInvalidPaths=balle;
    }
}
