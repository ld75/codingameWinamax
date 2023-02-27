package com.winamax.golf;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CombinaisonBlockString extends CombinaisonBlock<String,String,String>{

    public CombinaisonBlockString() {
        super();
    }

    @Override
    protected String instancieNewPrefix() {
        return new String();
    }

    public CombinaisonBlockString(String character, List<String> reste) {
        super(character, reste);
    }

    @Override
    protected CombinaisonBlock createNewInstance(String newprefix, ArrayList<String> reste2) {
        return new CombinaisonBlockString(newprefix,reste2);
    }

    @Override
    protected String concatPrefix(int idxRest) {
        String comma = getLengthOfPrefix()==0?"":",";
        return this.prefix.concat(comma+this.reste.get(idxRest));
    }

    @Override
    protected int getLengthOfPrefix(){
        return this.prefix.length();
    }

    @Override
    public List<String> getToutesCombinaisonsString() {
            return this.getToutesCombinaisons().stream().map(c->{
                String prefix = ((CombinaisonBlockString) c).prefix;
                String reste = ((CombinaisonBlockString) c).reste.get(0);
                if ("".equals(prefix))return reste;
                return prefix.concat(",").concat(reste);
            }).collect(Collectors.toList());
        }


    ;

}
