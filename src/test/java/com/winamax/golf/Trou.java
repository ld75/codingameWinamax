package com.winamax.golf;

public class Trou {
    public Integer y=0;
    public Integer x=0;

    public Trou(Integer[] hPosition) {
        this.x=hPosition[0];
        this.y=hPosition[1];
    }

    public Trou() {
    }

    public Trou(int x, int y) {
        this.x=x;
        this.y=y;
    }

    @Override
    public boolean equals(Object o)
    {
        if(this.x==((Trou)o).x && this.y==((Trou)o).y)  return true;
        return false;
    }

    @Override
    public String toString() {
        return "Trou{" +
                "x=" + x+
                ", y=" + y +
                '}';
    }
}
