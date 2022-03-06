package com.winamax.golf;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CombinaisonBlockBalleTrou extends CombinaisonBlock<List<CoupleBalleTrou>,Trou, CoupleBalleTrou>{

    public CombinaisonBlockBalleTrou() {
        super();
    }

    @Override
    protected List<CoupleBalleTrou> instancieNewPrefix() {
        return new ArrayList<>();
    }

    public CombinaisonBlockBalleTrou(List<CoupleBalleTrou> character, List<Trou> reste) {
        super(character, reste);
    }

    @Override
    protected CombinaisonBlock createNewInstance(List<CoupleBalleTrou> newprefix, ArrayList<Trou> reste2) {
        return new CombinaisonBlockBalleTrou(newprefix,reste2);
    }

    @Override
    protected List<CoupleBalleTrou> concatPrefix(int idxRest) {
        Trou trou = this.reste.get(idxRest);
        Ball lastBalle = this.prefix.get(this.prefix.size() - 1).balle;
        CoupleBalleTrou couple = new CoupleBalleTrou(lastBalle, trou);
        this.prefix.add(couple);
        return this.prefix;
    }

    @Override
    protected int getLengthOfPrefix(){
        return this.prefix.size();
    }

    @Override
    public List<CoupleBalleTrou> getToutesCombinaisonsString() {
            List<CoupleBalleTrou> res = new ArrayList<>();
            this.getToutesCombinaisons().stream().forEach(c->{
                CombinaisonBlockBalleTrou combi = (CombinaisonBlockBalleTrou)c;
                res.addAll(combi.prefix);
            });
            return res;
        }
}
