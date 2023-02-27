package com.winamax.golf;

public class Trou {
    public Position position = new Position();

    public Trou(Integer[] hPosition) {
        this.setX(hPosition[0]);
        this.setY(hPosition[1]);
    }

    public Trou() {
    }

    public Trou(int x, int y) {
        this.setX(x);
        this.setY(y);
    }

    @Override
    public boolean equals(Object o)
    {
        if(this.getX()==((Trou)o).getX() && this.getY()==((Trou)o).getY())  return true;
        return false;
    }

    @Override
    public String toString() {
        return "Trou{" +
                "x=" + getX()+
                ", y=" + getY() +
                '}';
    }
    int getX(){
        return position.x;
    }
    void setX(int x){
        position.x=x;
    }
    int getY(){
        return position.y;
    }
    void setY(int y){
        position.y=y;
    }
}
