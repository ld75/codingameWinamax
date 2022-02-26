package com.winamax.golf;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class GolfTest {
    MapAnalyzer mapAnalyzer = new MapAnalyzer();
    @Test
    public void findPathHorizontallyRight() throws TrouNonTrouveException {
        Ball whateverball = new Ball(1, 0, 0);
        List<String> rows = Arrays.asList("1H");
        Trou trou= mapAnalyzer.trouveTrous(rows).get(0);
        mapAnalyzer.findHorizontalAndVerticalDirection(whateverball, rows, trou);
        Assertions.assertEquals(">H",rows.get(0));
        Assertions.assertEquals(0,whateverball.score);
        Assertions.assertEquals(1,whateverball.x);
        Assertions.assertEquals(0,whateverball.y);
    }
    @Test
    public void getBalleFromRow()
    {
        List<Ball> balles = mapAnalyzer.getBallesFromRow("H....2...H",0);
        Assertions.assertEquals(2, balles.get(0).score);
        Assertions.assertEquals(0, balles.get(0).y);
        Assertions.assertEquals(5, balles.get(0).x);
    }
    @Test
    public void getBalleFromRow3()
    {
        List<Ball> ballesFromRow = mapAnalyzer.getBallesFromRow("H1...2..3H",0);
        Assertions.assertEquals(Arrays.asList(1,2,3), ballesFromRow.stream().map(b->b.score).collect(Collectors.toList()));
    }
    @Test
    public void findPathHorizontallyAnyNumber() throws TrouNonTrouveException {
        Ball whateverball= new Ball(5,6,0);
        List<String> rows = Arrays.asList("H.....5");
        Trou trou= mapAnalyzer.trouveTrous(rows).get(0);
        mapAnalyzer.findHorizontalAndVerticalDirection(whateverball, rows, trou);
        Assertions.assertEquals("H.....<",rows.get(0));
        Assertions.assertEquals(4,whateverball.score);
        Assertions.assertEquals(5,whateverball.x);
        Assertions.assertEquals(0,whateverball.y);
    }
    @Test
    @Disabled
    public void deuxBallesSurUnPlan() throws TrouNonTrouveException {
        Ball balle= new Ball(0,0,0);
        List<String> rows = Arrays.asList("H..1..5");
        Trou trou= mapAnalyzer.trouveTrous(rows).get(0);
        mapAnalyzer.findHorizontalAndVerticalDirection(balle, rows, trou);
        Assertions.assertEquals("H..<..<",rows.get(0));
    }
    @Test
    public void findPathHorizontallyTooFar() throws TrouNonTrouveException {
        Ball balle=new Ball(3,6,0);
        List<String> rows = Arrays.asList("H.....3");
        Trou trou= mapAnalyzer.trouveTrous(rows).get(0);
        mapAnalyzer.findHorizontalAndVerticalDirection(balle, rows, trou);
        Assertions.assertEquals("H.....<",rows.get(0));
        Assertions.assertEquals(2,balle.score);
        Assertions.assertEquals(5,balle.x);
        Assertions.assertEquals(0,balle.y);
    }

    @Test
    public void test1() throws TrouNonTrouveException {
        //taille: 2 1
        //ligne: 1H
        //attendu: >.
        List<String> rows = new ArrayList<>(List.of("1H"));
        Ball balle = mapAnalyzer.trouverBalles(rows).get(0);
        Trou trou= mapAnalyzer.trouveTrous(rows).get(0);
        List<String> res = mapAnalyzer.getPath(balle, rows, trou);
        Assertions.assertEquals(">.",res.get(0));
    }
    @Test
    public void deuxDimensionsSimple() throws TrouNonTrouveException {
        List<String> rows = new ArrayList<>(List.of("1H", ".."));
        Ball balle = mapAnalyzer.trouverBalles(rows).get(0);
        Trou trou= mapAnalyzer.trouveTrous(rows).get(0);
        List<String> res = mapAnalyzer.getPath(balle, rows, trou);
        List<String> expect = List.of(">.", "..");
        Assertions.assertEquals(expect,res);
    }
    @Test
    public void deuxDimensionsRempliesCibleEnBas() throws TrouNonTrouveException {
        List<String> rows = new ArrayList<>(List.of("1.",
                                    "H."));
        Ball balle = mapAnalyzer.trouverBalles(rows).get(0);
        Trou trou= mapAnalyzer.trouveTrous(rows).get(0);
        List<String> res = mapAnalyzer.getPath(balle, rows, trou);
        List<String> expect = List.of("V.",
                                      "..");
        Assertions.assertEquals(expect,res);
    }

    @Test
    public void deuxDimensionsRempliesCibleEnHaut() throws TrouNonTrouveException {
        List<String> rows = new ArrayList<>(List.of("H.",
                                    "1."));
        Ball balle = mapAnalyzer.trouverBalles(rows).get(0);
        Trou trou= mapAnalyzer.trouveTrous(rows).get(0);
        List<String> res = mapAnalyzer.getPath(balle, rows, trou);
        List<String> expect = List.of("..",
                                      "^.");
        Assertions.assertEquals(expect,res);
    }
    @Test
    public void zeroballes_trouverBalles_listevide()
    {
        List<String> rows = List.of("..X",
                                    "H..");
        List<Ball> balles = mapAnalyzer.trouverBalles(rows);
        Assertions.assertTrue(balles.size()==0);
    }
    @Test
    public void uneBalle_trouverBalles_balleEtPosition()
    {
        List<String> rows = List.of("..2",
                "H..");
        List<Ball> balles = mapAnalyzer.trouverBalles(rows);
        Assertions.assertTrue(balles.size()==1);
        Assertions.assertTrue(balles.get(0).x==2);
        Assertions.assertTrue(balles.get(0).y==0);
    }

    @Test
    public void atPosition0_changeCharOfString(){
        String str= "mlqkjsdf";
        Assertions.assertEquals("Ulqkjsdf",mapAnalyzer.changeCharAt(str,0,"U"));
    }
    @Test
    public void atPositionEnd_changeCharOfString(){
        String str= "mlqkjsdf";
        Assertions.assertEquals("mlqkjsdU",mapAnalyzer.changeCharAt(str,8,"U"));
    }
    @Test
    public void atPosition_changeCharOfString(){
        String str= "mlqkjsdf";
        Assertions.assertEquals("mlqkUsdf",mapAnalyzer.changeCharAt(str,4,"U"));
    }

    @Test
    public void invalidPosition1_changeBallPosition_dontChangePosition(){
        List<String> rows = List.of("...",
                                       "...");
        Ball balle = new Ball(1, 0, 0);
        mapAnalyzer.changeBallPosition(balle,"<", rows);
        Assertions.assertEquals(1,balle.score);
        Assertions.assertEquals(0,balle.x);
        Assertions.assertEquals(0,balle.y);
    }
    @Test
    public void invalidPosition2_changeBallPosition_dontChangePosition(){
        List<String> rows = List.of("...",
                "...");
        Ball balle = new Ball(1, 2, 0);
        mapAnalyzer.changeBallPosition(balle,">", rows);
        Assertions.assertEquals(1,balle.score);
        Assertions.assertEquals(2,balle.x);
        Assertions.assertEquals(0,balle.y);
    }
    @Test
    public void invalidPosition3_changeBallPosition_dontChangePosition(){
        List<String> rows = List.of("...",
                "...");
        Ball balle = new Ball(1, 1, 1);
        mapAnalyzer.changeBallPosition(balle,"V", rows);
        Assertions.assertEquals(1,balle.score);
        Assertions.assertEquals(1,balle.x);
        Assertions.assertEquals(1,balle.y);
    }
    @Test
    public void invalidPosition4_changeBallPosition_dontChangePosition(){
        List<String> rows = List.of("...",
                "...");
        Ball balle = new Ball(1, 1, 0);
        mapAnalyzer.changeBallPosition(balle,"^", rows);
        Assertions.assertEquals(1,balle.score);
        Assertions.assertEquals(1,balle.x);
        Assertions.assertEquals(0,balle.y);
    }
    @Test
    public void gootPosition_changeBallPosition_ballMoves(){
        List<String> rows = List.of("...",
                                    "...");
        Ball balle = new Ball(4, 1, 0);
        mapAnalyzer.changeBallPosition(balle,">", rows);
        AssertNewPositions(balle, 3, 2, 0);
        mapAnalyzer.changeBallPosition(balle,"V", rows);
        AssertNewPositions(balle, 2, 2, 1);
        mapAnalyzer.changeBallPosition(balle,"<", rows);
        AssertNewPositions(balle, 1, 1, 1);
        mapAnalyzer.changeBallPosition(balle,"^", rows);
        AssertNewPositions(balle, 0, 1, 0);
    }

    private void AssertNewPositions(Ball balle, int newscore, int newX, int newY) {
        Assertions.assertEquals(newscore, balle.score);
        Assertions.assertEquals(newX, balle.x);
        Assertions.assertEquals(newY, balle.y);
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
        Trou trou = mapAnalyzer.trouveTrous(rows).get(0);
        Assertions.assertFalse(mapAnalyzer.isHTrouve(rows, trou));
    }
    @Test
    public void isHTrouve_Oui1() throws TrouNonTrouveException {
        List<String> rows = List.of("V..",
                                    "H..");
        Trou trou = mapAnalyzer.trouveTrous(rows).get(0);
        Assertions.assertTrue(mapAnalyzer.isHTrouve(rows, trou));
    }
    @Test
    public void isHTrouve_Oui2() throws TrouNonTrouveException {
        List<String> rows = List.of("H..",
                                    "^..");
        Trou trou = mapAnalyzer.trouveTrous(rows).get(0);
        Assertions.assertTrue(mapAnalyzer.isHTrouve(rows, trou));
    }
    @Test
    public void troisDimensionsRemplies() throws TrouNonTrouveException {
        List<String> rows = new ArrayList<>(List.of("...H",
                                                    "1..."));
        Ball balle = mapAnalyzer.trouverBalles(rows).get(0);
        Trou trou= mapAnalyzer.trouveTrous(rows).get(0);
        List<String> res = mapAnalyzer.getPath(balle, rows, trou);
        List<String> expect = List.of(">>>.",
                                    "^...");
        Assertions.assertEquals(expect,res);
    }
    @Test
    public void nombreTrouDifferentDeNombreBalles_choisirCoupleBalleTrou_erreur(){
        List<String> rows = new ArrayList<>(List.of("3...H...3",
                                                    "...."));
        Assertions.assertThrows(JeuIncompletException.class,()->{mapAnalyzer.choisirCoupleBalleTrou(rows);});
        List<String> rows2 = new ArrayList<>(List.of("3...H.1.3",
                ".H.."));
        Assertions.assertThrows(JeuIncompletException.class,()->{mapAnalyzer.choisirCoupleBalleTrou(rows2);});
    }
    @Test
    public void plusieursBallesPlusieursTrous_choisirCoupleBalleTrou_choisirCoupleBalleTrou() throws TrouNonTrouveException, JeuIncompletException {
        List<String> rows = new ArrayList<>(List.of("1...H...3",
                                                    ".H......."));
        CoupleBalleTrou coupleBalleTrou = mapAnalyzer.choisirCoupleBalleTrou(rows);
        Assertions.assertEquals(0,coupleBalleTrou.balle.y);
        Assertions.assertEquals(0,coupleBalleTrou.balle.x);
        Assertions.assertEquals(0,coupleBalleTrou.trou.y);
        Assertions.assertEquals(4,coupleBalleTrou.trou.x);
    }
    @Test
    public void plusieursBalles_avanceChacuneASonTour() throws TrouNonTrouveException {
        List<String> rows = new ArrayList<>(List.of("...H..",
                "1..."));
        Ball balle = mapAnalyzer.trouverBalles(rows).get(0);
        Trou trou= mapAnalyzer.trouveTrous(rows).get(0);
        List<String> res = mapAnalyzer.getPath(balle, rows, trou);
        List<String> expect = List.of(">>>.",
                "^...");
    }
    @Test
    public void faireJouerBallesPourUnTrouATourDeRole() throws TrouNonTrouveException, JeuIncompletException {
        List<String> rows = new ArrayList<>(List.of("1.H.H.1"));
        rows = mapAnalyzer.jouerChaqueCouple(rows);
        List<String> expect = List.of(">>...<<");
        Assertions.assertEquals(expect,rows);
    }
    @Test
    public void test2() throws TrouNonTrouveException, JeuIncompletException {
        //taille: 3 3
        //ligne: 2.X
        //ligne: ..H
        //ligne: .H1
        //attendu: v..
        List<String> rows = new ArrayList<>(List.of("2.X",
                                                    "..H",
                                                    ".H1"));
        List<String> res = mapAnalyzer.jouerChaqueCouple( rows);
        List<String> expect = List.of("V.X",
                                      ">>.",
                                      "..<");
        Assertions.assertEquals(expect,res);
    }
    @Test
    public void trouEtrangerEnChemin_jouer_eviterTrouEtranger() throws TrouNonTrouveException, JeuIncompletException {
        List<String> rows = new ArrayList(List.of(
                "...H.",
                "...H.",
                ".2..2",
                "....."));
        List<String> expect = List.of(
                ".>>..",
                ".^..<",
                ".^..^",
                ".....");
        List<String> res = mapAnalyzer.jouerChaqueCouple( rows);
        Assertions.assertEquals(expect,res);
    }

    @Test
    public void test3() throws TrouNonTrouveException, JeuIncompletException {
        //taille: 5 5
        //ligne: 4..XX
        //ligne: .H.H.
        //ligne: ...H.
        //ligne: .2..2
        //ligne: .....
        //attendu: v....
        List<String> rows = new ArrayList(List.of(
                "4..XX",
                ".H.H.",
                "...H.",
                ".2..2",
                "....."));
        List<String> expect = List.of(
                ".>>VX",
                ".....",
                ".^..<",
                ".^..^",
                ".....");
/*             [V..XX,
                >>>..,
                .^..<,
                .^..^,
                .....]*/
        List<String> res = mapAnalyzer.jouerChaqueCouple( rows);
        Assertions.assertEquals(expect,res);
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
