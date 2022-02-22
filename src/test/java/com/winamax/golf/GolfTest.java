package com.winamax.golf;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;

public class GolfTest {
    MapAnalyzer mapAnalyzer = new MapAnalyzer();
    @Test
    public void findPathHorizontally()
    {
        String res = mapAnalyzer.findHorizontalDirection(2, "1H");
        Assertions.assertEquals(">.",res);
    }



    @Test
    @Disabled
    public void test1()
    {
        //taille: 2 1
        //ligne: 1H
        //attendu: >.
        String res = mapAnalyzer.getPath("2 1", List.of("1H"));
        Assertions.assertEquals(">.",res);
    }

    @Test
    public void test2()
    {
        //taille: 3 3
        //ligne: 2.X
        //ligne: ..H
        //ligne: .H1
        //attendu: v..
    }
    @Test
    public void test3()
    {
        //taille: 5 5
        //ligne: 4..XX
        //ligne: .H.H.
        //ligne: ...H.
        //ligne: .2..2
        //ligne: .....
        //attendu: v....
    }
    @Test
    public void test4()
    {
        //taille: 6 6
        //ligne: 3..H.2
        //ligne: .2..H.
        //ligne: ..H..H
        //ligne: .X.2.X
        //ligne: ......
        //ligne: 3..H..
        //">>>..v"
    }
}
