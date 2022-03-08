package com.winamax.golf;

import java.util.LinkedList;
public class LimitedQueue<E> extends LinkedList<E> {
    private int limit;
    public LimitedQueue(int maxsize) {
        this.limit = maxsize;
    }
    @Override
    public boolean add(E o) {
        super.add(o);
        while (size() > limit) { super.remove(); }
        return true;
    }
}