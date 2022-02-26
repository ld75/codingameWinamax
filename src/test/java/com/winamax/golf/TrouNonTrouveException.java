package com.winamax.golf;

public class TrouNonTrouveException extends Exception {
    public TrouNonTrouveException(String chemin_interdit) {
        super(chemin_interdit);
    }

    public TrouNonTrouveException() {

    }
}
