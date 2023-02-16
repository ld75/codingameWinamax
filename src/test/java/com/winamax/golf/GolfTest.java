package com.winamax.golf;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;

public class GolfTest {
    MapAnalyzer mapAnalyzer = new MapAnalyzer();
    private static transient Random random = new Random(Runtime.getRuntime().freeMemory());

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
        mapAnalyzer.avanceBalleDansDirection(balle, rows, ">", new Trou(new Integer[]{1,0}));
        System.out.println(balle);
        Assertions.assertEquals(1,balle.x);
        System.out.println(rows);
    }
    @Test
    public void avanceballedansdirection3()
    {
        Ball balle = new Ball(3, 0, 0);
        ArrayList rows = new ArrayList(List.of("3....H"));
        mapAnalyzer.avanceBalleDansDirection(balle, rows, ">", new Trou(new Integer[]{1,0}));
        System.out.println(balle);
        System.out.println(rows);
        Assertions.assertEquals(3,balle.x);
        Assertions.assertEquals(">>>..H", rows.get(0));
    }
    @Test
    public void test1() throws TrouNonTrouveException, ToutRefaireAvecNouveauCouple, StrategiePerdante {
        //taille: 2 1
        //ligne: 1H
        //attendu: >.
        List<String> rows = new ArrayList<>(List.of("1H"));
        Ball balle = mapAnalyzer.trouverBalles(rows).get(0);
        Trou trou= mapAnalyzer.trouveTrous(rows).get(0);
        mapAnalyzer.initialiserToutesLesStrategiesDeDeplacement();
        List<String> res = mapAnalyzer.resoudreBalle(balle, rows, trou);
        Assertions.assertEquals(">.",res.get(0));
    }
    @Test
    public void deuxDimensionsSimple() throws TrouNonTrouveException, ToutRefaireAvecNouveauCouple, StrategiePerdante {
        List<String> rows = new ArrayList<>(List.of("1H", ".."));
        Ball balle = mapAnalyzer.trouverBalles(rows).get(0);
        Trou trou= mapAnalyzer.trouveTrous(rows).get(0);
        mapAnalyzer.initialiserToutesLesStrategiesDeDeplacement();
        List<String> res = mapAnalyzer.resoudreBalle(balle, rows, trou);
        List<String> expect = List.of(">.", "..");
        Assertions.assertEquals(expect,res);
    }
    @Test
    public void deuxDimensionsRempliesCibleEnBas() throws TrouNonTrouveException, ToutRefaireAvecNouveauCouple, StrategiePerdante {
        List<String> rows = new ArrayList<>(List.of("1.",
                                    "H."));
        Ball balle = mapAnalyzer.trouverBalles(rows).get(0);
        Trou trou= mapAnalyzer.trouveTrous(rows).get(0);
        mapAnalyzer.initialiserToutesLesStrategiesDeDeplacement();
        List<String> res = mapAnalyzer.resoudreBalle(balle, rows, trou);
        List<String> expect = List.of("v.",
                                      "..");
        Assertions.assertEquals(expect,res);
    }

    @Test
    public void deuxDimensionsRempliesCibleEnHaut() throws TrouNonTrouveException, ToutRefaireAvecNouveauCouple, StrategiePerdante {
        List<String> rows = new ArrayList<>(List.of("H.",
                                    "1."));
        Ball balle = mapAnalyzer.trouverBalles(rows).get(0);
        Trou trou= mapAnalyzer.trouveTrous(rows).get(0);
        mapAnalyzer.initialiserToutesLesStrategiesDeDeplacement();
        List<String> res = mapAnalyzer.resoudreBalle(balle, rows, trou);
        List<String> expect = List.of("..",
                                      "^.");
        Assertions.assertEquals(expect,res);
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
        Assertions.assertFalse(mapAnalyzer.isBallCanMoove(balle,"<", rows,new Trou()));
    }
    @Test
    public void invalidPosition2_changeBallPosition_dontChangePosition() throws CanootMoveHereException {
        List<String> rows = List.of("...",
                                    "...");
        Ball balle = new Ball(1, 2, 0);
        Assertions.assertFalse(mapAnalyzer.isBallCanMoove(balle,">", rows, new Trou()));
    }
    @Test
    public void invalidPosition3_changeBallPosition_dontChangePosition() throws CanootMoveHereException {
        List<String> rows = List.of("...",
                "...");
        Ball balle = new Ball(1, 1, 1);
        Assertions.assertFalse(mapAnalyzer.isBallCanMoove(balle,"v", rows, new Trou()));
    }
    @Test
    public void invalidPosition4_changeBallPosition_dontChangePosition() throws CanootMoveHereException {
        List<String> rows = List.of("...",
                "...");
        Ball balle = new Ball(1, 1, 0);
        Assertions.assertFalse(mapAnalyzer.isBallCanMoove(balle,"^", rows, new Trou()));
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
    public void troisDimensionsRemplies() throws TrouNonTrouveException, ToutRefaireAvecNouveauCouple, StrategiePerdante {
        List<String> rows = new ArrayList<>(List.of("..H..",
                                                    ".....",
                                                    "3...."));
        Ball balle = mapAnalyzer.trouverBalles(rows).get(0);
        Trou trou= mapAnalyzer.trouveTrous(rows).get(0);
        mapAnalyzer.initialiserToutesLesStrategiesDeDeplacement();
        List<String> res = mapAnalyzer.resoudreBalle(balle, rows, trou);
        List<String> expect = List.of("...<.",
                                      "...^.",
                                      ">>>^.");
        Assertions.assertEquals(expect,res);
    }
    @Test
    public void nombreTrouDifferentDeNombreBalles_choisirCoupleBalleTrou_erreur(){
        List<String> rows = new ArrayList<>(List.of("3...H...3",
                                                    "...."));
        Assertions.assertThrows(JeuIncompletException.class,()->{mapAnalyzer.definirTousBallesEtTrous(rows);});
        List<String> rows2 = new ArrayList<>(List.of("3...H.1.3",
                                                     ".H.."));
        Assertions.assertThrows(JeuIncompletException.class,()->{mapAnalyzer.definirTousBallesEtTrous(rows2);});
    }

    @Test
    public void combinaisonsDeStrategies2(){
        String test= "1,2";
        CombinaisonBlock combinaison = new CombinaisonBlockString("", new ArrayList(Arrays.asList(test.split(","))));
        List<CombinaisonBlock> combinaisons = combinaison.combiner();
        Assertions.assertEquals(2,combinaisons.size());
        assertionsCombinaisonsCommenceParPrefixFiniParRAF(combinaisons, "1", 0, Arrays.asList("2"));
        assertionsCombinaisonsCommenceParPrefixFiniParRAF(combinaisons, "2", 1, Arrays.asList("1"));
    }
    @Test
    public void combinaisonsDeStrategies3(){
        String test= "1,2,3";
        CombinaisonBlock combinaison = new CombinaisonBlockString("", new ArrayList(Arrays.asList(test.split(","))));
        List<CombinaisonBlock> combinaisons = combinaison.combiner();
        Assertions.assertEquals(3,combinaisons.size());
        assertionsCombinaisonsCommenceParPrefixFiniParRAF(combinaisons, "1", 0, Arrays.asList(new String[]{"2","3"}));
        assertionsCombinaisonsCommenceParPrefixFiniParRAF(combinaisons, "2", 1, Arrays.asList(new String[]{"1","3"}));
        assertionsCombinaisonsCommenceParPrefixFiniParRAF(combinaisons, "3", 2, Arrays.asList(new String[]{"1","2"}));

    }
    @Test
    public void propagCombinasion(){
        CombinaisonBlock block = new CombinaisonBlockString("1", Arrays.asList(new String[]{"2"}));
        List<CombinaisonBlock> res = block.combiner();
        System.out.println(res);
        Assertions.assertEquals(1,res.size());
        Assertions.assertEquals("1,2",res.get(0).prefix);
        Assertions.assertEquals(0,res.get(0).reste.size());
    }
    @Test
    public void cannotCombineAnymore()
    {
        CombinaisonBlock block = new CombinaisonBlockString("1", new ArrayList<>());
        Assertions.assertEquals(block,block.combiner().get(0));
    }
    @Test
    public void demultiplieCombinaisons()
    {
        String test= "1,2,3";
        CombinaisonBlock combinaison = new CombinaisonBlockString("", new ArrayList(Arrays.asList(test.split(","))));
        List<CombinaisonBlock> combinaisons = combinaison.getToutesCombinaisons();
        Assertions.assertEquals(6,combinaisons.size());
        combinaisons.stream().forEach(c-> System.out.println(c.toString()));
    }
    @Test
    public void getTouteCombinaisonsDeDirectionsListString() {
        String test = ">,^,<,v";
        CombinaisonBlock combinaison = new CombinaisonBlockString("", new ArrayList(Arrays.asList(test.split(","))));
        List<String> combinaisons = combinaison.getToutesCombinaisonsString();
        Assertions.assertEquals(24, combinaisons.size());
        combinaisons.stream().forEach(c -> System.out.println(c));
    }

    private void assertionsCombinaisonsCommenceParPrefixFiniParRAF(List<CombinaisonBlock> combinaisons, String prefixe, int ligne, List<String> reste1) {
        Assertions.assertEquals(prefixe, combinaisons.get(ligne).prefix);
        String[] reste = convertirListeEnTableau(combinaisons.get(ligne).reste);
        Assertions.assertArrayEquals(convertirListeEnTableau(reste1),reste);
    }

    private String[] convertirListeEnTableau(List<String> reste) {
        return (String[]) reste.toArray(new String[reste.size()]);
    }

    @Test
    public void allStrategiesPlayed_pickupNextStrategie_ThrowsToutRefaireException()  {
        mapAnalyzer.initialiserToutesLesStrategiesDeDeplacement();
        Assertions.assertThrows(IndexOutOfBoundsException.class,()->
        {for (int i=0; i<=24; i++) mapAnalyzer.pickupNextStrategie();});
    }

    @Test
    public void strategieEchouent_getPath_toutesStrategiesEssayeesPuisRemonterErreur() throws TrouNonTrouveException {
        MapAnalyzerRechercheMemoriseeEchecSpy mapAnalyzerSpy = new MapAnalyzerRechercheMemoriseeEchecSpy();
        Ball balle= new Ball();
        List<String> rows = new ArrayList();
        Trou trou=new Trou();
        mapAnalyzerSpy.initialiserToutesLesStrategiesDeDeplacement();
        Assertions.assertThrows(ToutRefaireAvecNouveauCouple.class,()->{mapAnalyzerSpy.testerBalleEtReecrirePlan(balle, rows, trou);});
        Assertions.assertEquals(24,mapAnalyzerSpy.strategiePlayed.size());
    }

    @Test
    public void strategiesReussient_getPath_toutesStrategiesEssayeesPuisRemonterMeilleureSolution() throws TrouNonTrouveException, ToutRefaireAvecNouveauCouple {
        MapAnalyzerRechercheMemoriseeSuccesSpy mapAnalyzerSpy = new MapAnalyzerRechercheMemoriseeSuccesSpy();
        Ball balle= new Ball();
        List<String> rows = new ArrayList<>(List.of("2.H.H.2"));
        Trou trou=new Trou();
        mapAnalyzerSpy.initialiserToutesLesStrategiesDeDeplacement();
        mapAnalyzerSpy.testerBalleEtReecrirePlan(balle, rows, trou);
        Assertions.assertEquals(24,mapAnalyzerSpy.strategiePlayed.size());
    }
    @Test
    public void faireJouerBallesPourUnTrouATourDeRole() throws TrouNonTrouveException, JeuIncompletException, NonResoluException {
        List<String> rows = new ArrayList<>(List.of("2.H.H.2"));
        rows = mapAnalyzer.initialiserJeu(rows);
        List<String> expect = List.of(">>...<<");
        Assertions.assertEquals(expect,rows);
    }
    @Test
    public void enleverlacs()
    {
        List<String> rows = new ArrayList<>(List.of("2.X",
                                                    "..H",
                                                    ".H1"));

        List<String> res = mapAnalyzer.enleverLacs(rows);
        System.out.println(res);
        Assertions.assertEquals(res,List.of("2..",
                                            "..H",
                                            ".H1"));
    }
    @Test
    public void unCouple_toutescombinaisonsCouples()
    {
        List<Ball> balles= List.of(new Ball(1,2,3));
        List<Trou> trous= List.of(new Trou(2,3));
        List<List<CoupleBalleTrou>> couples = mapAnalyzer.genererAllCombinaisons(balles,trous);
        Assertions.assertEquals(1,couples.size());
    }
    @Test
    public void deuxCouples_toutescombinaisonsCouples()
    {
        List<Ball> balles= List.of(new Ball(1,1,1),new Ball(10,10,10));
        List<Trou> trous= List.of(new Trou(1,1),new Trou(10,10));
        List<List<CoupleBalleTrou>> couples = mapAnalyzer.genererAllCombinaisons(balles,trous);
        System.out.println(couples);
        Assertions.assertEquals(2,couples.size());
    }
    @Test
    public void troisBallesEtTrois_toutescombinaisonsCouples_SixCombisDeCouples()
    {
        List<Ball> balles= List.of(new Ball(1,1,1),new Ball(10,10,10),new Ball(20,20,20));
        List<Trou> trous= List.of(new Trou(1,1),new Trou(10,10),new Trou(20,20));
        List<List<CoupleBalleTrou>> couples = mapAnalyzer.genererAllCombinaisons(balles,trous);
        System.out.println(couples);
        Assertions.assertEquals(6,couples.size());
    }

    @Test
   // @Disabled //TODO: cas impossible: stackoverflow
    public void impossible_test1Moi_Erreur() throws TrouNonTrouveException, JeuIncompletException, NonResoluException {
        //ligne: 2.X
        //ligne: ..H
        //ligne: .H1
        //attendu: v..
        List<String> rows = new ArrayList<>(List.of("2.X",
                                                    "...",
                                                    "..H"));
        Assertions.assertThrows(NonResoluException.class,()->{mapAnalyzer.initialiserJeu( rows);});
    }
    @Test
    public void nextRemainingBall() throws NonResoluException {
        Ball e0 = new Ball(1, 0, 0);
        Ball e1 = new Ball(1, 1, 1);
        BallRandomPicker randomPicker = new BallRandomPicker();
        Ball res = randomPicker.getNextRandomRemainingObjet(List.of(e1,e0),List.of(e0));
        System.out.println(res);
    }
    @Test
    public void lastnextRemainingBall() throws NonResoluException {
        Ball e1 = new Ball(1, 1, 1);
        BallRandomPicker randomPicker = new BallRandomPicker();
        Ball res = randomPicker.getNextRandomRemainingObjet(List.of(e1),List.of());
        System.out.println(res);
    }
    @Test
    public void test2() throws TrouNonTrouveException, JeuIncompletException, NonResoluException {
        //ligne: 2.X
        //ligne: ..H
        //ligne: .H1
        //attendu: v..
        List<String> rows = new ArrayList<>(List.of(
                "2.X",
                "..H",
                ".H1"));
        List<String> res = mapAnalyzer.initialiserJeu( rows);
        mapAnalyzer.printChemin(res);
        List<String> expect = List.of(
                "v..",
                "v..",
                ">.^");
        Assertions.assertEquals(expect,res);
    }
    @Test
    public void test2bis() throws TrouNonTrouveException, JeuIncompletException, NonResoluException {
        //ligne: 2.X
        //ligne: ..H
        //ligne: .H1
        //attendu: v..
        List<String> rows = new ArrayList<>(List.of("3.X.",
                                                    "....",
                                                    "..H.",
                                                    ".H1."));
        List<String> res = mapAnalyzer.initialiserJeu( rows);
        mapAnalyzer.printChemin(res);
        List<String> expect = List.of(">>>v",
                                      "...v",
                                      "...<",
                                      "..<.");
        Assertions.assertEquals(expect,res);
    }
    @Test
    public void scoreetDistances1() throws TrouNonTrouveException, JeuIncompletException, NonResoluException {
        List<String> rows = new ArrayList(List.of(
                ".....",
                "..H..",
                "....2",
                "....."));
        List<String> expect = List.of(
                ".....",
                ".....",
                "..^<<",
                ".....");
        List<String> res = mapAnalyzer.initialiserJeu( rows);
        mapAnalyzer.printChemin(res);
        mapAnalyzer.printChemin(expect);
        Assertions.assertEquals(expect,res);
    }
    @Test
    public void scoreetDistances2() throws TrouNonTrouveException, JeuIncompletException, NonResoluException {
        List<String> rows = new ArrayList(List.of(
                "..........H",
                "...........",
                "...........",
                "...........",
                "5.........."));
        List<String> res = mapAnalyzer.initialiserJeu( rows);
        mapAnalyzer.printChemin(res);
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
    @Disabled //infini
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
    @Disabled //infini
    public void test3bout() throws TrouNonTrouveException, NonResoluException, JeuIncompletException {
        List<String> rows = new ArrayList(List.of(
                            "4..XX",
                            "...H."));
        List<String> res = mapAnalyzer.initialiserJeu( rows);
        System.out.println("************");
        mapAnalyzer.printChemin(res);
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
    public void power()
    {
        Assertions.assertEquals(2,mapAnalyzer.power(2));
        Assertions.assertEquals(6,mapAnalyzer.power(3));
        Assertions.assertEquals(24,mapAnalyzer.power(4));
        Assertions.assertEquals(771881146,mapAnalyzer.power(38));
        Assertions.assertEquals(771881146,mapAnalyzer.power(37));
    }
    @Test
    @Disabled
    public void random(){
        while(true) {
            int res = random.nextInt(2);
            System.out.println(res);
        }
        }
    @Test
    @Disabled //TODO: car il cherche à l'infini
    // à améliorer
    public void coupleFaux_jouerChaqueCouple_joueBalleAvecAutreTrou() throws NonResoluException {
        List<Ball> balles =List.of(new Ball(1,0,0),new Ball(1,1,1));
        List<Trou> trous =List.of(new Trou(0,0),new Trou(1,1));
        MapAnalyzerResoudreBalleEnErreurStub mapAnalizerStub = new MapAnalyzerResoudreBalleEnErreurStub(balles,trous);
        Assertions.assertThrows(NonResoluException.class,()->{mapAnalizerStub.jouerChaqueCouple(new ArrayList<>());});
        Assertions.assertEquals(4,mapAnalizerStub.getCouplesJoues());
    }

    @Test
    public void balleAvance_connaitAvantdernierCoup(){
        Ball balle = new Ball(4, 0, 0);
        Assertions.assertEquals(balle.getAvantDerniereCase(),"");
        mapAnalyzer.avanceBalleDansDirection(balle,new ArrayList(List.of("ABCDEFG")),">",new Trou(0,6));
        Assertions.assertEquals("D",balle.getAvantDerniereCase());
    }
@Test
public void vientDunLac_isHTrouve_false(){
    Ball balle= new Ball(0,2,2);
    balle.ajouteHistorique('.');
    Assertions.assertTrue(mapAnalyzer.isHTrouve(balle,new Trou(2,2)));
    balle.ajouteHistorique('X');
    Assertions.assertFalse(mapAnalyzer.isHTrouve(balle,new Trou(2,2)));
   }
    @Test
    public void test4mini() throws TrouNonTrouveException, NonResoluException, JeuIncompletException {
        List<String> rows = new ArrayList(List.of(
                "....1H",
                "...2.X",
                ".H...."));
       List<String> res = mapAnalyzer.initialiserJeu( rows);
        mapAnalyzer.printChemin(res);
        List<String> expect = List.of(
        "....>.",
        ".v<<..",
        "......");
        Assertions.assertEquals(expect,res);
    }
    @Test
    public void
    test4() throws TrouNonTrouveException, NonResoluException, JeuIncompletException {
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
                                ">>>..v",
                                ".>>>.v",
                                ">>....",
                                "^..v..",
                                "^..v..",
                                "^.....");
                        //ERREUR: //v...<<
                        //ERREUR: //v>>>..
                        //ERREUR: //v.....
                        //ERREUR: //>>^>>^
                        //ERREUR: //......
                        //ERREUR: //>>>...



        List<String> res = mapAnalyzer.initialiserJeu( rows);
        mapAnalyzer.printChemin(res);
        Assertions.assertEquals(expect,res);


    }
    @Test
    public void canMoveRight()
    {
        for (int i=0; i<20; i++) {
            boolean res = mapAnalyzer.canMooveRight(new Ball(4, 0, 0), new ArrayList(List.of("....H....")), new Trou(20, 20));
            Assertions.assertFalse(res);
        }
    }
    @Test
    public void test5() throws TrouNonTrouveException, NonResoluException, JeuIncompletException {
        List<String> rows = new ArrayList(List.of(
                ".XXX.5X.",
                "X.4.X..X",
                "X4..X3.X",
                "X...X.X.",
                ".X.X.H.X",
                "X.HX...X",
                "X..X.H.X",
                ".XH.XXX."));
        List<String> expect = List.of(
                    "v<<<<<..",
                    "v.>>>>v.",
                    "vvv<<<v.",
                    "vvv...v.",
                    "vvv.>.v.",
                    "vv..^.v.",
                    "v>>>^.<.",
                    ">>......");
        List<String> res = mapAnalyzer.initialiserJeu( rows);
        mapAnalyzer.printChemin(res);
        Assertions.assertEquals(expect,res);


    }
    @Test
    public void test6() throws TrouNonTrouveException, NonResoluException, JeuIncompletException {
        List<String> rows = new ArrayList(List.of(
        ".......4",
        "....HH.2",
        "..5.....",
        "H....22X",
        ".3XH.HXX",
        "..X3.H.X",
        "..XH....",
        "H2X.H..3"));
        List<String> expect = List.of(
                            "v<<<<<<<",
                            "v>>>..<<",
                            "v^>>>>>v",
                            ".^.v<<vv",
                            ".^..>.vv",
                            "v<<<^.<v",
                            "v...^<<<",
                            ".>>^.<<<");
        List<String> res = mapAnalyzer.initialiserJeu( rows);
        mapAnalyzer.printChemin(res);
//        Assertions.assertEquals(expect,res);

    }
    @Test
    public void test9() throws TrouNonTrouveException, NonResoluException, JeuIncompletException {
        List<String> rows = new ArrayList(List.of(
                ".XXX.5XX4H5............4H..3XXH.2.HX3...",
                "XX4.X..X......3.....HH.2X.....5.....4XX.",
                "X4..X3.X......H...5.....XXXXXXX2.HX2..H.",
                "X..XXXXX.....H3.H.X..22X3XXH.X2X...2HHXH",
                ".X.X.H.X........X3XH.HXX.XXXXX.H..HX..2.",
                "X.HX.X.X....HH....X3.H.X.....H..XXXX3...",
                "X..X.H.X.43......XXH....HXX3..H.X2.HX2..",
                ".XHXXXXX..H3H...H2X.H..3X2..HXX3H.2XXXXH"));
        List<String> expect = List.of(
                "v<<<<<<<",
                "v>>>..<<",
                "v^>>>>>v",
                ".^.v<<vv",
                ".^..>.vv",
                "v<<<^.<v",
                "v...^<<<",
                ".>>^.<<<");
        List<String> res = mapAnalyzer.initialiserJeu( rows);
        mapAnalyzer.printChemin(res);

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

    private class MapAnalyzerResoudreBalleEnErreurStub extends MapAnalyzer {
        private List<CoupleBalleTrou> couplesJoues = new ArrayList<>();
        public MapAnalyzerResoudreBalleEnErreurStub(List<Ball> balles, List<Trou> trous) {
            this.balles=balles;
            this.trous=trous;
        }

        @Override
        public void printChemin(List<String> rows, Ball balle, Trou trou) {
        }

        @Override
        public List<String> resoudreBalle(Ball balle, List<String> rowsAvantEssai, Trou trou) throws ToutRefaireAvecNouveauCouple {
            couplesJoues.add(new CoupleBalleTrou(balle,trou));
            throw new ToutRefaireAvecNouveauCouple();
        }

        public int getCouplesJoues() {
            return couplesJoues.size();
        }
    }
}
