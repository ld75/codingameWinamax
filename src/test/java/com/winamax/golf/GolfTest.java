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
    public void isballcanmove()
    {
        Assertions.assertFalse(mapAnalyzer.isBallCanMoove(new Ball(2,0,0),">",List.of("2H"),new Trou(new Integer[]{1,0})));
        Assertions.assertTrue(mapAnalyzer.isBallCanMoove(new Ball(1,0,0),">",List.of("1H"),new Trou(new Integer[]{1,0})));
    }

    @Test
    public void avanceballedansdirection()
    {
        Ball balle = new Ball(1, 0, 0);
        ArrayList rows = new ArrayList(List.of("1H"));
        mapAnalyzer.avanceBalleDansDirection(balle, rows, ">");
        System.out.println(balle);
        Assertions.assertEquals(1,balle.x);
        System.out.println(rows);
    }
    @Test
@Disabled
    public void test1() throws TrouNonTrouveException, ToutRefaireAvecNouveauxCouples, StrategiePerdante {
        //taille: 2 1
        //ligne: 1H
        //attendu: >.
        List<String> rows = new ArrayList<>(List.of("1H"));
        Ball balle = mapAnalyzer.trouverBalles(rows).get(0);
        Trou trou= mapAnalyzer.trouveTrous(rows).get(0);
        mapAnalyzer.initialiserToutesLesStrategiesDeDeplacement();
        List<String> res = mapAnalyzer.getPath(balle, rows, trou);
        Assertions.assertEquals(">.",res.get(0));
    }
    @Test
@Disabled
    public void deuxDimensionsSimple() throws TrouNonTrouveException, ToutRefaireAvecNouveauxCouples, StrategiePerdante {
        List<String> rows = new ArrayList<>(List.of("1H", ".."));
        Ball balle = mapAnalyzer.trouverBalles(rows).get(0);
        Trou trou= mapAnalyzer.trouveTrous(rows).get(0);
        mapAnalyzer.initialiserToutesLesStrategiesDeDeplacement();
        List<String> res = mapAnalyzer.getPath(balle, rows, trou);
        List<String> expect = List.of(">.", "..");
        Assertions.assertEquals(expect,res);
    }
    @Test
@Disabled
    public void deuxDimensionsRempliesCibleEnBas() throws TrouNonTrouveException, ToutRefaireAvecNouveauxCouples, StrategiePerdante {
        List<String> rows = new ArrayList<>(List.of("1.",
                                    "H."));
        Ball balle = mapAnalyzer.trouverBalles(rows).get(0);
        Trou trou= mapAnalyzer.trouveTrous(rows).get(0);
        mapAnalyzer.initialiserToutesLesStrategiesDeDeplacement();
        List<String> res = mapAnalyzer.getPath(balle, rows, trou);
        List<String> expect = List.of("v.",
                                      "..");
        Assertions.assertEquals(expect,res);
    }

    @Test
@Disabled
    public void deuxDimensionsRempliesCibleEnHaut() throws TrouNonTrouveException, ToutRefaireAvecNouveauxCouples, StrategiePerdante {
        List<String> rows = new ArrayList<>(List.of("H.",
                                    "1."));
        Ball balle = mapAnalyzer.trouverBalles(rows).get(0);
        Trou trou= mapAnalyzer.trouveTrous(rows).get(0);
        mapAnalyzer.initialiserToutesLesStrategiesDeDeplacement();
        List<String> res = mapAnalyzer.getPath(balle, rows, trou);
        List<String> expect = List.of("..",
                                      "^.");
        Assertions.assertEquals(expect,res);
    }
    @Test
@Disabled
    public void zeroballes_trouverBalles_listevide()
    {
        List<String> rows = List.of("..X",
                                    "H..");
        List<Ball> balles = mapAnalyzer.trouverBalles(rows);
        Assertions.assertTrue(balles.size()==0);
    }
    @Test
@Disabled
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
@Disabled
    public void atPosition0_changeCharOfString(){
        String str= "mlqkjsdf";
        Assertions.assertEquals("Ulqkjsdf",mapAnalyzer.changeCharAt(str,0,"U"));
    }
    @Test
@Disabled
    public void atPositionEnd_changeCharOfString(){
        String str= "mlqkjsdf";
        Assertions.assertEquals("mlqkjsdU",mapAnalyzer.changeCharAt(str,8,"U"));
    }
    @Test
@Disabled
    public void atPosition_changeCharOfString(){
        String str= "mlqkjsdf";
        Assertions.assertEquals("mlqkUsdf",mapAnalyzer.changeCharAt(str,4,"U"));
    }

    @Test
@Disabled
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
@Disabled
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
@Disabled
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
@Disabled
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
@Disabled
    public void gootPosition_changeBallPosition_ballMoves() throws CanootMoveHereException {
        List<String> rows = List.of("......",
                                    "......",
                                    "......",
                                    "......",
                                    "......");
        Ball balle = new Ball(4, 0, 0);
        mapAnalyzer.changeBallPosition(balle,">", rows, new Trou());
        AssertNewPositions(balle, 3, 4, 0);
        mapAnalyzer.changeBallPosition(balle,"v", rows, new Trou());
        AssertNewPositions(balle, 2, 4, 3);
        mapAnalyzer.changeBallPosition(balle,"<", rows, new Trou());
        AssertNewPositions(balle, 1, 2, 3);
        mapAnalyzer.changeBallPosition(balle,"^", rows, new Trou());
        AssertNewPositions(balle, 0, 2, 2);
    }

    private void AssertNewPositions(Ball balle, int newscore, int newX, int newY) {
        Assertions.assertEquals(newscore, balle.score);
        Assertions.assertEquals(newX, balle.x);
        Assertions.assertEquals(newY, balle.y);
    }

    @Test
@Disabled
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
@Disabled
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
@Disabled
    public void isHTrouve_Non() throws TrouNonTrouveException {
        List<String> rows = List.of("..1",
                                    "H>.");
        Trou trou = mapAnalyzer.trouveTrous(rows).get(0);
        Assertions.assertFalse(mapAnalyzer.isHTrouve(rows, trou));
    }
    @Test
@Disabled
    public void isHTrouve_Oui1() throws TrouNonTrouveException {
        List<String> rows = List.of("v..",
                                    "H..");
        Trou trou = mapAnalyzer.trouveTrous(rows).get(0);
        Assertions.assertTrue(mapAnalyzer.isHTrouve(rows, trou));
    }
    @Test
@Disabled
    public void isHTrouve_Oui2() throws TrouNonTrouveException {
        List<String> rows = List.of("H..",
                                    "^..");
        Trou trou = mapAnalyzer.trouveTrous(rows).get(0);
        Assertions.assertTrue(mapAnalyzer.isHTrouve(rows, trou));
    }
    @Test
@Disabled
    public void troisDimensionsRemplies() throws TrouNonTrouveException, ToutRefaireAvecNouveauxCouples, StrategiePerdante {
        List<String> rows = new ArrayList<>(List.of("...H",
                                                    "4..."));
        Ball balle = mapAnalyzer.trouverBalles(rows).get(0);
        Trou trou= mapAnalyzer.trouveTrous(rows).get(0);
        mapAnalyzer.initialiserToutesLesStrategiesDeDeplacement();
        List<String> res = mapAnalyzer.getPath(balle, rows, trou);
        List<String> expect = List.of("....",
                                    ">>>^");
        Assertions.assertEquals(expect,res);
    }
    @Test
@Disabled
    public void nombreTrouDifferentDeNombreBalles_choisirCoupleBalleTrou_erreur(){
        List<String> rows = new ArrayList<>(List.of("3...H...3",
                                                    "...."));
        Assertions.assertThrows(JeuIncompletException.class,()->{mapAnalyzer.definirTousLesCouplesBalleTrou(rows);});
        List<String> rows2 = new ArrayList<>(List.of("3...H.1.3",
                ".H.."));
        Assertions.assertThrows(JeuIncompletException.class,()->{mapAnalyzer.definirTousLesCouplesBalleTrou(rows2);});
    }
    @Test
@Disabled
    public void plusieursBallesPlusieursTrous_choisirCoupleBalleTrou_choisirCoupleBalleTrou() throws TrouNonTrouveException, JeuIncompletException {
        List<String> rows = new ArrayList<>(List.of("123......",
                                                    "HHH......"));
        List<List<CouplesBalleTrouCombines.CoupleBalleTrou>> coupleBalleTrouToutJeux = mapAnalyzer.definirTousLesCouplesBalleTrou(rows);

        Assertions.assertEquals(6,coupleBalleTrouToutJeux.size());
        Assertions.assertEquals(3,coupleBalleTrouToutJeux.get(0).size());
        System.out.println(coupleBalleTrouToutJeux);
        Assertions.assertTrue(coupleBalleTrouToutJeux.get(0).get(0).trou.x!=coupleBalleTrouToutJeux.get(1).get(0).trou.x);

    }
    @Test
@Disabled
    public void plusieursBalles_avanceChacuneASonTour() throws TrouNonTrouveException, ToutRefaireAvecNouveauxCouples, StrategiePerdante {
        List<String> rows = new ArrayList<>(List.of("...H..",
                "5....."));
        Ball balle = mapAnalyzer.trouverBalles(rows).get(0);
        Trou trou= mapAnalyzer.trouveTrous(rows).get(0);
        mapAnalyzer.initialiserToutesLesStrategiesDeDeplacement();
        List<String> res = mapAnalyzer.getPath(balle, rows, trou);
        List<String> expect = List.of(">>>.",
                "^...");
    }
    @Test
@Disabled
    public void combinaisonsDeStrategies2(){
        String test= "1,2";
        CombinaisonBlock combinaison = new CombinaisonBlock("", new ArrayList(Arrays.asList(test.split(","))));
        List<CombinaisonBlock> combinaisons = combinaison.combiner();
        Assertions.assertEquals(2,combinaisons.size());
        assertions(combinaisons, "1", 0, Arrays.asList("2"));
        assertions(combinaisons, "2", 1, Arrays.asList("1"));
    }
    @Test
@Disabled
    public void combinaisonsDeStrategies3(){
        String test= "1,2,3";
        CombinaisonBlock combinaison = new CombinaisonBlock("", new ArrayList(Arrays.asList(test.split(","))));
        List<CombinaisonBlock> combinaisons = combinaison.combiner();
        Assertions.assertEquals(3,combinaisons.size());
        assertions(combinaisons, "1", 0, Arrays.asList(new String[]{"2","3"}));
        assertions(combinaisons, "2", 1, Arrays.asList(new String[]{"1","3"}));
        assertions(combinaisons, "3", 2, Arrays.asList(new String[]{"1","2"}));

    }
    @Test
@Disabled
    public void propagCombinasion(){
        CombinaisonBlock block = new CombinaisonBlock("1", Arrays.asList(new String[]{"2"}));
        List<CombinaisonBlock> res = block.combiner();
        Assertions.assertEquals(1,res.size());
        Assertions.assertEquals("1,2",res.get(0).prefix);
        Assertions.assertEquals(0,res.get(0).reste.size());
    }
    @Test
@Disabled
    public void cannotCombineAnymore()
    {
        CombinaisonBlock block = new CombinaisonBlock("1", new ArrayList<>());
        Assertions.assertEquals(block,block.combiner().get(0));
    }
    @Test
@Disabled
    public void demultiplieCombinaisons()
    {
        String test= "1,2,3";
        CombinaisonBlock combinaison = new CombinaisonBlock("", new ArrayList(Arrays.asList(test.split(","))));
        List<CombinaisonBlock> combinaisons = combinaison.getToutesCombinaisons();
        Assertions.assertEquals(6,combinaisons.size());
        combinaisons.stream().forEach(c-> System.out.println(c.toString()));
    }
    @Test
@Disabled
    public void getTouteCombinaisonsDeDirectionsListString() {
        String test = ">,^,<,v";
        CombinaisonBlock combinaison = new CombinaisonBlock("", new ArrayList(Arrays.asList(test.split(","))));
        List<String> combinaisons = combinaison.getToutesCombinaisonsString();
        Assertions.assertEquals(24, combinaisons.size());
        combinaisons.stream().forEach(c -> System.out.println(c));
    }

    private void assertions(List<CombinaisonBlock> combinaisons, String prefixe, int ligne, List<String> reste1) {
        Assertions.assertEquals(prefixe, combinaisons.get(ligne).prefix);
        String[] reste = convertirListeEnTableau(combinaisons.get(ligne).reste);
        Assertions.assertArrayEquals(convertirListeEnTableau(reste1),reste);
    }

    private String[] convertirListeEnTableau(List<String> reste) {
        return (String[]) reste.toArray(new String[reste.size()]);
    }

    @Test
@Disabled
    public void allStrategiesPlayed_pickupNextStrategie_ThrowsToutRefaireException()  {
        mapAnalyzer.initialiserToutesLesStrategiesDeDeplacement();
        Assertions.assertThrows(IndexOutOfBoundsException.class,()->
        {for (int i=0; i<=24; i++) mapAnalyzer.pickupNextStrategie();});
    }

    @Test
@Disabled
    public void strategieEchouent_getPath_toutesStrategiesEssayeesPuisRemonterErreur() throws TrouNonTrouveException {
        MapAnalyzerRechercheMemoriseeEchecSpy mapAnalyzerSpy = new MapAnalyzerRechercheMemoriseeEchecSpy();
        Ball balle= new Ball();
        List<String> rows = new ArrayList();
        Trou trou=new Trou();
        mapAnalyzerSpy.initialiserToutesLesStrategiesDeDeplacement();
        Assertions.assertThrows(ToutRefaireAvecNouveauxCouples.class,()->{mapAnalyzerSpy.analyseRows(balle, rows, trou);});
        Assertions.assertEquals(24,mapAnalyzerSpy.strategiePlayed.size());
    }

    @Test
@Disabled
    public void strategiesReussient_getPath_toutesStrategiesEssayeesPuisRemonterMeilleureSolution() throws TrouNonTrouveException, ToutRefaireAvecNouveauxCouples {
        MapAnalyzerRechercheMemoriseeSuccesSpy mapAnalyzerSpy = new MapAnalyzerRechercheMemoriseeSuccesSpy();
        Ball balle= new Ball();
        List<String> rows = new ArrayList<>(List.of("2.H.H.2"));
        Trou trou=new Trou();
        mapAnalyzerSpy.initialiserToutesLesStrategiesDeDeplacement();
        mapAnalyzerSpy.analyseRows(balle, rows, trou);
        Assertions.assertEquals(24,mapAnalyzerSpy.strategiePlayed.size());
    }
    @Test
@Disabled
    public void faireJouerBallesPourUnTrouATourDeRole() throws TrouNonTrouveException, JeuIncompletException, NonResoluException {
        List<String> rows = new ArrayList<>(List.of("2.H.H.2"));
        rows = mapAnalyzer.initialiserJeu(rows);
        List<String> expect = List.of(">>...<<");
        Assertions.assertEquals(expect,rows);
    }
    @Test
@Disabled
    public void enleverlacs()
    {
        List<String> rows = new ArrayList<>(List.of("2.X",
                "..H",
                ".H1"));

        List<String> res = mapAnalyzer.enleverLacs(rows);
        System.out.println(res);
    }
    @Test
@Disabled
    public void test2() throws TrouNonTrouveException, JeuIncompletException, NonResoluException {
        //ligne: 2.X
        //ligne: ..H
        //ligne: .H1
        //attendu: v..
        List<String> rows = new ArrayList<>(List.of("2.X",
                                                    "..H",
                                                    ".H1"));
        List<String> res = mapAnalyzer.initialiserJeu( rows);
        List<String> expect = List.of("v..",
                                      "v..",
                                      ">.^");
        Assertions.assertEquals(expect,res);
    }
    @Test
@Disabled
    public void scoreetDistances1() throws TrouNonTrouveException, JeuIncompletException, NonResoluException {
        List<String> rows = new ArrayList(List.of(
                ".....",
                "...H.",
                "....2",
                "....."));
        List<String> expect = List.of(
                ".....",
                "....<",
                "....^",
                ".....");
        List<String> res = mapAnalyzer.initialiserJeu( rows);
        mapAnalyzer.printChemin(res);
        mapAnalyzer.printChemin(expect);
        Assertions.assertEquals(expect,res);
    }
    @Test
@Disabled
    public void scoreetDistances2() throws TrouNonTrouveException, JeuIncompletException, NonResoluException {
        List<String> rows = new ArrayList(List.of(
                "...H.",
                ".....",
                ".5...",
                "....."));
        List<String> expect = List.of(
                ".....",
                "...^.",
                ".>>^.",
                ".....");
        List<String> res = mapAnalyzer.initialiserJeu( rows);
        mapAnalyzer.printChemin(res);
        mapAnalyzer.printChemin(expect);
        Assertions.assertEquals(expect,res);
    }
    @Test
@Disabled
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
@Disabled
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
@Disabled
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
@Disabled
    public void rechercheMemorisee() throws TrouNonTrouveException, StrategiePerdante {
        List<String> rows = new ArrayList(List.of(
                "4....",
                ".....",
                ".....",
                "....H"));
        mapAnalyzer.printChemin(rows, new Ball(), new Trou());
        Trou trou = mapAnalyzer.trouveTrous(rows).get(0);
        Ball balle = mapAnalyzer.trouverBalles(rows).get(0);
        BallEtMap resultat = mapAnalyzer.rechercheMemorisee(balle, rows, trou,Arrays.asList(">","v","<","^"));
        String chemin =  resultat.getCheminLePlusCourt();
        Assertions.assertEquals(">>>>vvv",chemin);
    }
    @Test
@Disabled
    public void analyseRows() throws TrouNonTrouveException, ToutRefaireAvecNouveauxCouples {
        List<String> rows = new ArrayList(List.of(
                "9....",
                ".....",
                "..H.."));
        mapAnalyzer.printChemin(rows, new Ball(), new Trou());
        Trou trou = mapAnalyzer.trouveTrous(rows).get(0);
        Ball balle = new Ball(4, 0, 0);
        mapAnalyzer.initialiserToutesLesStrategiesDeDeplacement();
        List<String> res = mapAnalyzer.analyseRows(balle, rows, trou);
        System.out.println(res);
    }
    @Test
@Disabled
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
    mapAnalyzer.printChemin(res);
    Assertions.assertEquals(expect,res);
    }

    @Test
@Disabled
    public void cheminPlusCourtExiste_BalleDroitTrouverCheminPlusCourt() throws TrouNonTrouveException, StrategiePerdante {
        List<String> rows = new ArrayList(List.of(
                "4....",
                "..H.."));
        Trou trou = mapAnalyzer.trouveTrous(rows).get(0);
        Ball balle = mapAnalyzer.trouverBalles(rows).get(0);
        String res = mapAnalyzer.rechercheMemorisee(balle, rows, trou, Arrays.asList(">","^","<","v")).getCheminLePlusCourt();
        String expect = ">>v";
        mapAnalyzer.printChemin(rows, new Ball(), new Trou());
        Assertions.assertEquals(expect,res);
    }

    @Test
@Disabled
    public void rienNaFonctionne_changerCouplesEtRefaire() {
        MapAnalyzerRechercheMemoriseeEchecSpy mapAnalyzerSpy = new MapAnalyzerRechercheMemoriseeEchecSpy();
        List<String> rows = new ArrayList(List.of(
                "111",
                "HHH"));
        Assertions.assertThrows(NonResoluException.class,()->{mapAnalyzerSpy.initialiserJeu(rows);});
        mapAnalyzerSpy.couples.forEach(c->System.out.println(c));
        Assertions.assertEquals(3,mapAnalyzerSpy.couples.size());// les trois premiers couples de chacun des trois jeux de couples
        System.out.println(mapAnalyzerSpy.couples);
    }
    @Test
@Disabled
    public void test3bout() throws TrouNonTrouveException, NonResoluException, JeuIncompletException {
        List<String> rows = new ArrayList(List.of(
                            "4..XX",
                            "...H."));
        List<String> res = mapAnalyzer.initialiserJeu( rows);
        System.out.println("************");
        mapAnalyzer.printChemin(res);
    }
    @Test
@Disabled
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
                ".H.H" +
                        ".",
                "...H.",
                ".2..2",
                "....."));
        List<String> expect = List.of(
                "v....",
                "v...<",
                "v^..^",
                "v^.^^",
                ">>>^.");
                //List.of(
                //".>>vX",
                //".....",
                //".^..<",
                //".^..^",
                //".....");
        List<String> res = mapAnalyzer.initialiserJeu( rows);
        mapAnalyzer.printChemin(res);
        Assertions.assertEquals(expect,res);
    }
    @Test
@Disabled
    public void test4() throws TrouNonTrouveException, NonResoluException, JeuIncompletException {
        //taille: 6 6
        //ligne: 3..H.2
        //ligne: .2..H.
        //ligne: ..H..H
        //ligne: .X.2.X
        //ligne: ......
        //ligne: 3..H..
        //">>>..v"

        List<String> rows = new ArrayList(List.of(
                "3..H.2",
                ".2..H.",
                "..H..H",
                ".X.2.X",
                "......",
                "3..H.."));
        List<String> expect = List.of(
                ">>>.v<",
                ".>v...",
                "......",
                "...>>^",
                "......",
                ">>>...");
        List<String> res = mapAnalyzer.initialiserJeu( rows);
        mapAnalyzer.printChemin(res);
        Assertions.assertEquals(expect,res);


    }

    private class MapAnalyzerRechercheMemoriseeEchecSpy extends MapAnalyzer {
        public List<List<String>> strategiePlayed = new ArrayList<>();
        public Set<String> couples = new HashSet<>();
        public MapAnalyzerRechercheMemoriseeEchecSpy() {
            this.strategiePlayed=new ArrayList<>();
            this.couples = new HashSet<>();
        }

        @Override
        public BallEtMap rechercheMemorisee(Ball balleOriginale, List<String> rowsOriginal, Trou trou, List<String> strategieDirections) throws StrategiePerdante{
            this.strategiePlayed.add(strategieDirections);
            this.couples.add(Integer.toString(balleOriginale.x)+Integer.toString(trou.x));
            throw new StrategiePerdante(new Ball());
        }
    }

    private class MapAnalyzerRechercheMemoriseeSuccesSpy extends MapAnalyzer {
        public List<List<String>> strategiePlayed = new ArrayList<>();
        public Set<String> couples = new HashSet<>();
        public MapAnalyzerRechercheMemoriseeSuccesSpy() {
            this.strategiePlayed=new ArrayList<>();
            this.couples = new HashSet<>();
        }

        @Override
        public BallEtMap rechercheMemorisee(Ball balleOriginale, List<String> rowsOriginal, Trou trou, List<String> strategieDirections) throws StrategiePerdante{
            this.strategiePlayed.add(strategieDirections);
            this.couples.add(Integer.toString(balleOriginale.x)+Integer.toString(trou.x));
            Ball balle = new Ball();
            balle.ajouteChemin(">");
            balle.sauveParcourtEtReinitStack();
            return new BallEtMap(balle,new ArrayList<>());
        }
    }

}
