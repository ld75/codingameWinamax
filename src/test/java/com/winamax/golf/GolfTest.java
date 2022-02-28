package com.winamax.golf;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;

public class GolfTest {
    MapAnalyzer mapAnalyzer = new MapAnalyzer();
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
    public void test1() throws TrouNonTrouveException, ToutRefaireAvecStrategieSuivanteException {
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
    public void deuxDimensionsSimple() throws TrouNonTrouveException, ToutRefaireAvecStrategieSuivanteException {
        List<String> rows = new ArrayList<>(List.of("1H", ".."));
        Ball balle = mapAnalyzer.trouverBalles(rows).get(0);
        Trou trou= mapAnalyzer.trouveTrous(rows).get(0);
        List<String> res = mapAnalyzer.getPath(balle, rows, trou);
        List<String> expect = List.of(">.", "..");
        Assertions.assertEquals(expect,res);
    }
    @Test
    public void deuxDimensionsRempliesCibleEnBas() throws TrouNonTrouveException, ToutRefaireAvecStrategieSuivanteException {
        List<String> rows = new ArrayList<>(List.of("1.",
                                    "H."));
        Ball balle = mapAnalyzer.trouverBalles(rows).get(0);
        Trou trou= mapAnalyzer.trouveTrous(rows).get(0);
        List<String> res = mapAnalyzer.getPath(balle, rows, trou);
        List<String> expect = List.of("v.",
                                      "..");
        Assertions.assertEquals(expect,res);
    }

    @Test
    public void deuxDimensionsRempliesCibleEnHaut() throws TrouNonTrouveException, ToutRefaireAvecStrategieSuivanteException {
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
    public void invalidPosition1_changeBallPosition_dontChangePosition() throws CanootMoveHereException {
        List<String> rows = List.of("...",
                                       "...");
        Ball balle = new Ball(1, 0, 0);
        mapAnalyzer.changeBallPosition(balle,"<", rows,new Trou());
        Assertions.assertEquals(1,balle.score);
        Assertions.assertEquals(0,balle.x);
        Assertions.assertEquals(0,balle.y);
    }
    @Test
    public void invalidPosition2_changeBallPosition_dontChangePosition() throws CanootMoveHereException {
        List<String> rows = List.of("...",
                "...");
        Ball balle = new Ball(1, 2, 0);
        mapAnalyzer.changeBallPosition(balle,">", rows, new Trou());
        Assertions.assertEquals(1,balle.score);
        Assertions.assertEquals(2,balle.x);
        Assertions.assertEquals(0,balle.y);
    }
    @Test
    public void invalidPosition3_changeBallPosition_dontChangePosition() throws CanootMoveHereException {
        List<String> rows = List.of("...",
                "...");
        Ball balle = new Ball(1, 1, 1);
        mapAnalyzer.changeBallPosition(balle,"v", rows, new Trou());
        Assertions.assertEquals(1,balle.score);
        Assertions.assertEquals(1,balle.x);
        Assertions.assertEquals(1,balle.y);
    }
    @Test
    public void invalidPosition4_changeBallPosition_dontChangePosition() throws CanootMoveHereException {
        List<String> rows = List.of("...",
                "...");
        Ball balle = new Ball(1, 1, 0);
        mapAnalyzer.changeBallPosition(balle,"^", rows, new Trou());
        Assertions.assertEquals(1,balle.score);
        Assertions.assertEquals(1,balle.x);
        Assertions.assertEquals(0,balle.y);
    }
    @Test
    public void gootPosition_changeBallPosition_ballMoves() throws CanootMoveHereException {
        List<String> rows = List.of("...",
                                    "...");
        Ball balle = new Ball(4, 1, 0);
        mapAnalyzer.changeBallPosition(balle,">", rows, new Trou());
        AssertNewPositions(balle, 3, 2, 0);
        mapAnalyzer.changeBallPosition(balle,"v", rows, new Trou());
        AssertNewPositions(balle, 2, 2, 1);
        mapAnalyzer.changeBallPosition(balle,"<", rows, new Trou());
        AssertNewPositions(balle, 1, 1, 1);
        mapAnalyzer.changeBallPosition(balle,"^", rows, new Trou());
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
    public void positionEnChamp_identifie_char(){
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
        List<String> rows = List.of("v..",
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
    public void troisDimensionsRemplies() throws TrouNonTrouveException, ToutRefaireAvecStrategieSuivanteException {
        List<String> rows = new ArrayList<>(List.of("...H",
                                                    "4..."));
        Ball balle = mapAnalyzer.trouverBalles(rows).get(0);
        Trou trou= mapAnalyzer.trouveTrous(rows).get(0);
        List<String> res = mapAnalyzer.getPath(balle, rows, trou);
        List<String> expect = List.of("....",
                                    ">>>^");
        Assertions.assertEquals(expect,res);
    }
    @Test
    public void nombreTrouDifferentDeNombreBalles_choisirCoupleBalleTrou_erreur(){
        List<String> rows = new ArrayList<>(List.of("3...H...3",
                                                    "...."));
        Assertions.assertThrows(JeuIncompletException.class,()->{mapAnalyzer.choisirProchinCoupleBalleTrou(rows);});
        List<String> rows2 = new ArrayList<>(List.of("3...H.1.3",
                ".H.."));
        Assertions.assertThrows(JeuIncompletException.class,()->{mapAnalyzer.choisirProchinCoupleBalleTrou(rows2);});
    }
    @Test
    public void plusieursBallesPlusieursTrous_choisirCoupleBalleTrou_choisirCoupleBalleTrou() throws TrouNonTrouveException, JeuIncompletException {
        List<String> rows = new ArrayList<>(List.of("1H2H3H.",
                                                    "........."));
        CoupleBalleTrou coupleBalleTrou = mapAnalyzer.choisirProchinCoupleBalleTrou(rows);
        Assertions.assertEquals(0,coupleBalleTrou.balle.x);
        Assertions.assertEquals(1,coupleBalleTrou.trou.x);
        coupleBalleTrou = mapAnalyzer.choisirProchinCoupleBalleTrou(rows);
        Assertions.assertEquals(2,coupleBalleTrou.balle.x);
        Assertions.assertEquals(3,coupleBalleTrou.trou.x);
        coupleBalleTrou = mapAnalyzer.choisirProchinCoupleBalleTrou(rows);
        Assertions.assertEquals(4,coupleBalleTrou.balle.x);
        Assertions.assertEquals(5,coupleBalleTrou.trou.x);
        coupleBalleTrou = mapAnalyzer.choisirProchinCoupleBalleTrou(rows);
        Assertions.assertEquals(0,coupleBalleTrou.balle.x);
        Assertions.assertEquals(3,coupleBalleTrou.trou.x);
        coupleBalleTrou = mapAnalyzer.choisirProchinCoupleBalleTrou(rows);
        Assertions.assertEquals(2,coupleBalleTrou.balle.x);
        Assertions.assertEquals(5,coupleBalleTrou.trou.x);
        coupleBalleTrou = mapAnalyzer.choisirProchinCoupleBalleTrou(rows);
        Assertions.assertEquals(4,coupleBalleTrou.balle.x);
        Assertions.assertEquals(1,coupleBalleTrou.trou.x);
        coupleBalleTrou = mapAnalyzer.choisirProchinCoupleBalleTrou(rows);
        Assertions.assertEquals(0,coupleBalleTrou.balle.x);
        Assertions.assertEquals(5,coupleBalleTrou.trou.x);
        coupleBalleTrou = mapAnalyzer.choisirProchinCoupleBalleTrou(rows);
        Assertions.assertEquals(2,coupleBalleTrou.balle.x);
        Assertions.assertEquals(1,coupleBalleTrou.trou.x);
        coupleBalleTrou = mapAnalyzer.choisirProchinCoupleBalleTrou(rows);
        Assertions.assertEquals(4,coupleBalleTrou.balle.x);
        Assertions.assertEquals(3,coupleBalleTrou.trou.x);
        coupleBalleTrou = mapAnalyzer.choisirProchinCoupleBalleTrou(rows);
        Assertions.assertEquals(0,coupleBalleTrou.balle.x);
        Assertions.assertEquals(1,coupleBalleTrou.trou.x);
    }
    @Test
    public void plusieursBalles_avanceChacuneASonTour() throws TrouNonTrouveException, ToutRefaireAvecStrategieSuivanteException {
        List<String> rows = new ArrayList<>(List.of("...H..",
                "5....."));
        Ball balle = mapAnalyzer.trouverBalles(rows).get(0);
        Trou trou= mapAnalyzer.trouveTrous(rows).get(0);
        List<String> res = mapAnalyzer.getPath(balle, rows, trou);
        List<String> expect = List.of(">>>.",
                "^...");
    }
    @Test
    public void combinaisonsDeStrategies2()
    {
        String test= "12";
        List<String> combinaisons = mapAnalyzer.returnCombinasons(test);
        System.out.println(combinaisons);
        Assertions.assertEquals(2,combinaisons.size());
        Assertions.assertEquals("12",combinaisons.get(0));
        Assertions.assertEquals("21",combinaisons.get(1));
    }
    @Test
    @Disabled //TODO: produitcartesien toutes les combinaisons possibles à faire
    public void combinaisonsDeStrategies3()
    {
        String test= "123";
        List<String> combinaisons = mapAnalyzer.returnCombinasons(test);
        System.out.println(combinaisons);
        Assertions.assertEquals(6,combinaisons.size());
        Assertions.assertEquals("123",combinaisons.get(0));
        Assertions.assertEquals("21",combinaisons.get(1));
    }
    @Test
    public void allStrategiesPlayed_pickupNextStrategie_ThrowsToutRefaireException()  {
        Assertions.assertThrows(ToutRefaireAvecStrategieSuivanteException.class,()->
        {for (int i=0; i<24; i++) mapAnalyzer.pickupNextStrategie();});
    }

    @Test
    public void strategieEchouent_getPath_toutesStrategiesEssayeesPuisRemonterErreur() throws TrouNonTrouveException {
        MapAnalyzerRechercheMemoriseeSpy mapAnalyzerSpy = new MapAnalyzerRechercheMemoriseeSpy();
        Ball balle= new Ball();
        List<String> rows = new ArrayList();
        Trou trou=new Trou();
        Assertions.assertThrows(ToutRefaireAvecStrategieSuivanteException.class,()->{mapAnalyzerSpy.analyseRows(balle, rows, trou);});
        Assertions.assertEquals(8,mapAnalyzerSpy.strategiePlayed.size());
    }
    @Test
    public void faireJouerBallesPourUnTrouATourDeRole() throws TrouNonTrouveException, JeuIncompletException, NonResoluException {
        List<String> rows = new ArrayList<>(List.of("2.H.H.2"));
        rows = mapAnalyzer.initialiserJeu(rows);
        List<String> expect = List.of(">>...<<");
        Assertions.assertEquals(expect,rows);
    }
    @Test
    public void test2() throws TrouNonTrouveException, JeuIncompletException, NonResoluException {
        //taille: 3 3
        //ligne: 2.X
        //ligne: ..H
        //ligne: .H1
        //attendu: v..
        List<String> rows = new ArrayList<>(List.of("2.X",
                                                    "..H",
                                                    ".H1"));
        List<String> res = mapAnalyzer.initialiserJeu( rows);
        List<String> expect = List.of(">>v",
                                      "...",
                                      "..<");
        Assertions.assertEquals(expect,res);
    }
    @Test
    public void trouEtrangerEnChemin_jouer_eviterTrouEtranger() throws TrouNonTrouveException, JeuIncompletException, NonResoluException {
        List<String> rows = new ArrayList(List.of(
                "...H.",
                "...H.",
                ".5..2",
                "....."));
        List<String> expect = List.of(
                "..>..",
                "..^.<",
                ".>^.^",
                ".....");
        List<String> res = mapAnalyzer.initialiserJeu( rows);
        mapAnalyzer.printChemin(res, new Ball(), new Trou());
        mapAnalyzer.printChemin(expect, new Ball(), new Trou());
        Assertions.assertEquals(expect,res);
    }
    @Test
    public void printChemin()
    {
        List<String> rows = new ArrayList(List.of(
                "4..XX",
                ".H.H.",
                "...H.",
                ".2..2",
                "....."));
        mapAnalyzer.printChemin(rows, new Ball(0,0,0), new Trou(new Integer[]{1,1}));
    }

    @Test
    public void changeCharAtTalbeau()
    {
        List<String> rows = new ArrayList(List.of(
                "9....",
                ".....",
                "....H"));
        mapAnalyzer.changeCharAt(rows, 3,1,"O");
        Assertions.assertEquals("[9...., ...O., ....H]",rows.toString());
    }

    @Test
    public void rechercheMemorisee() throws TrouNonTrouveException, StrategiePerdante {
        List<String> rows = new ArrayList(List.of(
                "9....",
                ".....",
                "....H"));
        mapAnalyzer.printChemin(rows, new Ball(), new Trou());
        Trou trou = mapAnalyzer.trouveTrous(rows).get(0);
        Ball balle = mapAnalyzer.trouverBalles(rows).get(0);
        String chemin = mapAnalyzer.rechercheMemorisee(balle, rows, trou,Arrays.asList(">","v","<","^"));
        Assertions.assertEquals(">>>>vv",chemin);
    }
    @Test
    public void analyseRows() throws TrouNonTrouveException, ToutRefaireAvecStrategieSuivanteException {
        List<String> rows = new ArrayList(List.of(
                "9....",
                ".....",
                "..H.."));
        mapAnalyzer.printChemin(rows, new Ball(), new Trou());
        Trou trou = mapAnalyzer.trouveTrous(rows).get(0);
        Ball balle = new Ball(4, 0, 0);
        List<String> res = mapAnalyzer.analyseRows(balle, rows, trou);
        System.out.println(res);
    }
    @Test
public void flechesBellesEtTrousCroisees_jeu_eviterCroisementFlechesBallesEtTrous() throws TrouNonTrouveException, JeuIncompletException, NonResoluException {
    List<String> rows = new ArrayList(List.of(
            "4....",
            "4...H",
            "....H"));
        List<String> res = mapAnalyzer.initialiserJeu( rows);
    List<String> expect = new ArrayList(List.of(
            ">>>>v",
            ">>>v.",
            "...>."));
    mapAnalyzer.printChemin(res, new Ball(), new Trou());
    Assertions.assertEquals(expect,res);
    }

    @Test
    public void cheminPlusCourtExiste_BalleDroitTrouverCheminPlusCourt() throws TrouNonTrouveException, StrategiePerdante {
        List<String> rows = new ArrayList(List.of(
                "4....",
                "..H.."));
        Trou trou = mapAnalyzer.trouveTrous(rows).get(0);
        Ball balle = mapAnalyzer.trouverBalles(rows).get(0);
        String res = mapAnalyzer.rechercheMemorisee(balle, rows, trou, Arrays.asList(">","^","<","v"));
        String expect = ">>v";
        mapAnalyzer.printChemin(rows, new Ball(), new Trou());
        Assertions.assertEquals(expect,res);
    }

    @Test
    public void rienNaFonctionne_changerCouplesEtRefaire() throws TrouNonTrouveException, NonResoluException, JeuIncompletException {
        MapAnalyzerRechercheMemoriseeSpy mapAnalyzerSpy = new MapAnalyzerRechercheMemoriseeSpy();
        List<String> rows = new ArrayList(List.of(
                "111",
                "HHH"));
        Assertions.assertThrows(NonResoluException.class,()->{mapAnalyzerSpy.initialiserJeu(rows);});
        mapAnalyzerSpy.couples.forEach(c->System.out.println(c));
        Assertions.assertEquals(8,mapAnalyzerSpy.couples.size());
    }
    @Test
    public void test3() throws TrouNonTrouveException, JeuIncompletException, NonResoluException {
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
        List<String> res = mapAnalyzer.initialiserJeu( rows);
        mapAnalyzer.printChemin(res, new Ball(), new Trou());
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

    private class MapAnalyzerRechercheMemoriseeSpy extends MapAnalyzer {
        public List<List<String>> strategiePlayed = new ArrayList<>();
        public Set<String> couples = new HashSet<>();
        public MapAnalyzerRechercheMemoriseeSpy() {
            this.strategiePlayed=new ArrayList<>();
            this.couples = new HashSet<>();
        }

        @Override
        public String rechercheMemorisee(Ball balleOriginale, List<String> rowsOriginal, Trou trou, List<String> strategieDirections) throws StrategiePerdante{
            this.strategiePlayed.add(strategieDirections);
            this.couples.add(Integer.toString(balleOriginale.x)+Integer.toString(trou.x));
            throw new StrategiePerdante(new ArrayList<>());
        }
    }

}
