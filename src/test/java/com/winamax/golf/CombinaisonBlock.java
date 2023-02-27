package com.winamax.golf;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public abstract class CombinaisonBlock<T,U,R> {
    public T prefix=instancieNewPrefix();

    public CombinaisonBlock() {
    }

    protected abstract T instancieNewPrefix();

    public List<U> reste=new ArrayList<>();

    public CombinaisonBlock(T character, List<U> reste) {
        this.prefix=character;
        this.reste = reste;
    }

    public List<CombinaisonBlock> combiner() {
        if (this.reste.size()==0) return Arrays.asList(new CombinaisonBlock[] {this});
        List<CombinaisonBlock> res = new ArrayList();
        for (int idxRest=0; idxRest<this.reste.size(); idxRest++){
            T newprefix=concatPrefix(idxRest);
            ArrayList reste2 = new ArrayList(Arrays.asList(reste.toArray()));
            reste2.remove(idxRest);
            res.add(createNewInstance(newprefix,reste2));
        }
        return res;
    }

    protected abstract CombinaisonBlock createNewInstance(T newprefix, ArrayList<U> reste2);

    protected abstract T concatPrefix(int idxRest);

    protected abstract int getLengthOfPrefix();

    public List<CombinaisonBlock> getToutesCombinaisons(){
        List<CombinaisonBlock> resultatFinal = new ArrayList<>();
        resultatFinal.add(this);
        int previousSize =0;
        int factorielNbDeCombi = IntStream.range(1,this.reste.size()+1).reduce((carry,elt)->elt*carry).getAsInt();
        while (resultatFinal.size()!=factorielNbDeCombi){
            int iterateur=0;
            previousSize=resultatFinal.size();
            while (iterateur<previousSize) {
                CombinaisonBlock parent = resultatFinal.get(0);
                resultatFinal.remove(parent);
                resultatFinal.addAll(parent.combiner());
                 iterateur++;
            }
        }
        return resultatFinal;
    }

    @Override
    public String toString() {
        return "CombinaisonBlock{" +
                "prefix='" + prefix + '\'' +
                '}';
    }

    public abstract List<R> getToutesCombinaisonsString();
}
