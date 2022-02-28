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

    @Override
    public boolean equals(Object o)
    {
        if(this.x==((Ball)o).x && this.y==((Ball)o).y)  return true;
        return false;
    }
}
