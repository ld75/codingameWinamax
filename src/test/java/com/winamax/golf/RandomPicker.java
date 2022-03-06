package com.winamax.golf;

import java.util.List;
import java.util.Random;

public class RandomPicker<T> {
    private static transient Random random = new Random(Runtime.getRuntime().freeMemory());

    public T getNextRandomRemainingObjet(List<T> objets, List<T> resolvedObjets) throws NonResoluException {
        int nbObjetRestant = objets.size() - resolvedObjets.size();
        if (nbObjetRestant==0) throw new NonResoluException();
        int idxObjetHasard=0;
        do {
            idxObjetHasard = random.nextInt(objets.size());
        } while(resolvedObjets.contains(objets.get(idxObjetHasard)));
        return objets.get(idxObjetHasard);
    }
}
