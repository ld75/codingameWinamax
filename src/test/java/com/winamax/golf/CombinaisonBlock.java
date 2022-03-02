package com.winamax.golf;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class CombinaisonBlock {
    public String prefix="";
    public List<String> reste=new ArrayList<>();

    public CombinaisonBlock(String character, List<String> reste) {
        this.prefix=character;
        this.reste = reste;
    }

    public List<CombinaisonBlock> combiner() {
        if (this.reste.size()==0) return Arrays.asList(new CombinaisonBlock[] {this});
        List<CombinaisonBlock> res = new ArrayList();
        for (int i=0; i<this.reste.size(); i++){
            String comma = this.prefix.length()==0?"":",";
            String newprefix=this.prefix.concat(comma+this.reste.get(i));
            ArrayList reste2 = new ArrayList(Arrays.asList(reste.toArray()));
            reste2.remove(i);
            res.add(new CombinaisonBlock(newprefix,reste2));
        }
        return res;
    }

    public List<CombinaisonBlock> getToutesCombinaisons(){
        List<CombinaisonBlock> resultatFinal = new ArrayList<>();
        resultatFinal.add(this);
        int previousSize =0;
        while (resultatFinal.size()!=previousSize){
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

    public List<String> getToutesCombinaisonsString() {
        return this.getToutesCombinaisons().stream().map(c->c.prefix).collect(Collectors.toList());
    }
}
