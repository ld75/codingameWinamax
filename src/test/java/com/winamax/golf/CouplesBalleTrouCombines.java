package com.winamax.golf;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.stream.Collector;

public class CouplesBalleTrouCombines {
    public List<List<CoupleBalleTrou>> toutJeuxCouples= new ArrayList<>();
    private int jeuNumero=0;
    private int ballenbr=0;
    private int trounbr=0;


    public void combinerDeuxsens(List<Ball> balles, List<Trou> trous) {
        Deque<Trou> trouInverses =trous.stream().collect(Collector.of(ArrayDeque::new,(deq, t) -> deq.addFirst(t),(d1, d2) -> { d2.addAll(d1); return d2; }));
        combiner(balles, new ArrayList<>(trouInverses));
        combiner(balles, trous);
        jeuNumero=0;
    }

    private void combiner(List<Ball> balles, List<Trou> trous) {
        int nbrCouplesUnJeu= balles.size();
        int nombreCombinaisons=0;
        while (nombreCombinaisons< balles.size()* trous.size()) {
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
    }



}