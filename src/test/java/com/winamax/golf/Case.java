package com.winamax.golf;

public class Case {
    String direction;
    Position position;
    String casepoint;
    public Case(String casepoint, int x, int y,String direction) {
        this.position = new Position(x,y);
        this.casepoint = casepoint;
        this.direction=direction;
    }

    public String getDirection() {
        return this.direction;
    }

    @Override
    public String toString() {
        return "Case{" +
                "direction='" + direction + '\'' +
                ", position=" + position +
                ", casepoint='" + casepoint + '\'' +
                '}';
    }
}
