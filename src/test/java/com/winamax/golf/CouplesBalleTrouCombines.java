package com.winamax.golf;

import org.junit.jupiter.api.extension.ExtensionContext;

import java.util.ArrayList;
import java.util.List;

public class CouplesBalleTrouCombines {
    public List<List<CoupleBalleTrou>> toutJeuxCouples= new ArrayList<>();
    private int jeuNumero=0;
    int coupleNumero=0;
    private int ballenbr=0;
    private int trounbr=0;

    public void addCouple(Ball ball, Trou trou) {
        toutJeuxCouples.get(jeuNumero).add(new CoupleBalleTrou(ball,trou));
    }

    public CoupleBalleTrou getProchainCoupleDeCeJeu() {
        CoupleBalleTrou ret = toutJeuxCouples.get(jeuNumero).get(coupleNumero);
        coupleNumero++;
        return ret;
    }

    public void combiner(List<Ball> balles, List<Trou> trous) {
        int nbrCouplesUnJeu=balles.size();
        int nombreCombinaisons=0;
        while (nombreCombinaisons<balles.size()*trous.size()) {
            nombreCombinaisons++;
            if (ballenbr >= nbrCouplesUnJeu) {
                ballenbr = 0;
                jeuNumero++;
                trounbr++;
                if (trounbr >= nbrCouplesUnJeu) trounbr = 0;
            }
            if (toutJeuxCouples.size() <= jeuNumero) toutJeuxCouples.add(new ArrayList<>());
            List<CoupleBalleTrou> jeudeCouples = toutJeuxCouples.get(jeuNumero);
            jeudeCouples.add(new CoupleBalleTrou(balles.get(ballenbr), trous.get(trounbr)));
            ballenbr++;
            trounbr++;
            if (trounbr >= nbrCouplesUnJeu) trounbr = 0;

        }
        jeuNumero=0;
    }


    public class CoupleBalleTrou {
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