package com.winamax.golf;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GolfTest {
    MapAnalyzer mapAnalyzer = new MapAnalyzer();
    @Test
    public void findPathHorizontallyRight() throws TrouNonTrouveException {
        String res = mapAnalyzer.findHorizontalDirection(2, "1H",0,new ArrayList<>());
        Assertions.assertEquals(">H",res);
    }
    @Test
    public void getBalleFromRow()
    {
        Assertions.assertEquals(Arrays.asList(Integer.valueOf(2)), mapAnalyzer.getBallesFromRow("H....2...H"));
    }
    @Test
    public void getBalleFromRow3()
    {
        Assertions.assertEquals(Arrays.asList(Integer.valueOf(1),Integer.valueOf(2),Integer.valueOf(3)), mapAnalyzer.getBallesFromRow("H1...2..3H"));
    }
    @Test
    public void findPathHorizontallyAnyNumber() throws TrouNonTrouveException {
        String res = mapAnalyzer.findHorizontalDirection(2, "H.....5",0,new ArrayList<>());
        Assertions.assertEquals("H.....<",res);
    }
    @Test
    public void deuBallesSurUnPlan() throws TrouNonTrouveException {
        String res = mapAnalyzer.findHorizontalDirection(2, "H..3..5",0,new ArrayList<>());
        Assertions.assertEquals("H..<..<",res);
    }
    @Test
    public void findPathHorizontallyTooFar() throws TrouNonTrouveException {
        String res = mapAnalyzer.findHorizontalDirection(2, "H.....3",0,new ArrayList<>());
        Assertions.assertEquals("H.....<",res);
    }

    @Test
    public void test1() throws TrouNonTrouveException {
        //taille: 2 1
        //ligne: 1H
        //attendu: >.
        List<String> res = mapAnalyzer.getPath("2 1", List.of("1H"));
        Assertions.assertEquals(">.",res.get(0));
    }
    @Test
    public void deuxDimensionsSimple() throws TrouNonTrouveException {
        List<String> res = mapAnalyzer.getPath("2 2",List.of("1H",".."));
        List<String> expect = List.of(">.", "..");
        Assertions.assertEquals(expect,res);
    }
    @Test
    public void deuxDimensionsRempliesCibleEnBas() throws TrouNonTrouveException {
        List<String> rows = List.of("1.",
                                    "H.");
        List<String> res = mapAnalyzer.getPath("2 2", rows);
        List<String> expect = List.of("V.",
                                      "..");
        Assertions.assertEquals(expect,res);
    }

    @Test
    public void trouverHAbsentAilleursQueLaLigneActuelle()
    {
        Assertions.assertThrows(TrouNonTrouveException.class,()->{mapAnalyzer.returnOtherRowhavingH(0,Arrays.asList("mlHHHkjqsdf","mlqjsdf","lmqjsdf","lmjkqsdf"));});
    }
    @Test
    public void trouverHAilleursQueLaLigneActuelle() throws TrouNonTrouveException {
        int res = mapAnalyzer.returnOtherRowhavingH(0,Arrays.asList("mlHHHkjqsdf","mlqjsdf","lmqjsdf","HHHHHHH"));
        Assertions.assertEquals(3,res);
    }
    @Test
    public void deuxDimensionsRempliesCibleEnHaut() throws TrouNonTrouveException {
        List<String> rows = List.of("H.",
                                    "1.");
        List<String> res = mapAnalyzer.getPath("2 2", rows);
        List<String> expect = List.of("..",
                                      "^.");
        Assertions.assertEquals(expect,res);
    }
    @Test
    public void troisDimensionsRempliesCibleEnBas() throws TrouNonTrouveException {
        List<String> rows = List.of("..1",
                                    "H..");
        List<String> res = mapAnalyzer.getPath("2 2", rows);
        List<String> expect = List.of("..V",
                                      ".<<");
        Assertions.assertEquals(expect,res);
    }
    @Test
    public void horsChamp_identifie_erreur()
    {
        List < String > rows = List.of("abc",
                                       "fed");
        Assertions.assertEquals('E',mapAnalyzer.identifie(rows, -1, -1));
        Assertions.assertEquals('E',mapAnalyzer.identifie(rows, 3, 2));
        Assertions.assertEquals('E',mapAnalyzer.identifie(rows, 3, 1));
        Assertions.assertEquals('E',mapAnalyzer.identifie(rows, 2, 2));
    }
    @Test
    public void positionEnChamp_identifie_char() throws HorsChampException {
        List < String > rows = List.of("abc",
                "fed");
        Assertions.assertEquals('a',mapAnalyzer.identifie(rows, 0, 0));
        Assertions.assertEquals('b',mapAnalyzer.identifie(rows, 1, 0));
        Assertions.assertEquals('c',mapAnalyzer.identifie(rows, 2, 0));
        Assertions.assertEquals('f',mapAnalyzer.identifie(rows, 0, 1));
        Assertions.assertEquals('e',mapAnalyzer.identifie(rows, 1, 1));
        Assertions.assertEquals('d',mapAnalyzer.identifie(rows, 2, 1));
    }
    @Test
    public void isHTrouve_Non() throws TrouNonTrouveException {
        List<String> rows = List.of("..1",
                                    "H>.");
        Assertions.assertFalse(mapAnalyzer.isHTrouve(rows));
    }
    @Test
    public void isHTrouve_Oui1() throws TrouNonTrouveException {
        List<String> rows = List.of("V..",
                                    "H..");
        Assertions.assertTrue(mapAnalyzer.isHTrouve(rows));
    }
    @Test
    public void isHTrouve_Oui2() throws TrouNonTrouveException {
        List<String> rows = List.of("H..",
                                    "^..");
        Assertions.assertTrue(mapAnalyzer.isHTrouve(rows));
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
