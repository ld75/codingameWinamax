package com.winamax.golf;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MapAnalyzer {
    int strategieNumber=0;
    List<String> strategies;

    public List<Trou> resolvedTrous= new ArrayList();
    public List<Ball> resolvedBall= new ArrayList<>();
    private List<CoupleBalleTrou> couplesInvalide = new ArrayList<>();

    protected List<String> initialMap;
    List<Ball> balles;
    List<Trou> trous;
    protected List<List<CoupleBalleTrou>> allCombinaisonsCouples;
    private int idxCouple= 0;
    private int idxcombinaisonCouples=0;

    public void printrowsArrayCodinggame(List<String> rowsToPrint) {
        rowsToPrint.forEach(System.out::println);
    }
    public List<String> resoudreBalle(Ball balle, List<String> rowsAvantEssai, Trou trou) throws ToutRefaireAvecNelleCombinaisonCouples {
        ArrayList rowAtravailler = new ArrayList(rowsAvantEssai);
        Ball balleOriginale =Ball.createNewInstance(balle);
        List<String> resultRows = new ArrayList<>();
            resultRows = testerBalleEtReecrirePlan(balle, rowAtravailler,trou);
        boolean hTrouve = isHTrouve(balle, trou);
        System.out.println("htrouve: "+hTrouve+" pour balle "+ balleOriginale+ "trou "+ trou);
        printChemin(resultRows,balleOriginale,trou);
        if (hTrouve) return MarkBallEtTrouHasResolved(balleOriginale,trou, resultRows);
        throw new ToutRefaireAvecNelleCombinaisonCouples();
    }

    private List<String> MarkBallEtTrouHasResolved(Ball balle,Trou trou, List<String> resultRows) {
        addTrouToResolvedTrouList(trou);
        addBallToResolvedBallList(balle);
        return removeThisTrouFromMap(resultRows, trou);
    }

    private void addBallToResolvedBallList(Ball balle){
        this.resolvedBall.add(balle);
    }
    private void addTrouToResolvedTrouList(Trou trou) {
        this.resolvedTrous.add(trou);
    }

    public boolean isHTrouve(Ball balle, Trou trou){
            return (trou.getY()==balle.getY() && trou.getX()==balle.getX());
    }

    public List<Trou> trouveTrous(List<String> resultRows) throws TrouNonTrouveException {
        char h = 'H';
        return trouveCharEtTransformeEnTrous(resultRows, h);
    }

    public List<Trou> trouveZeros(List<String> resultRows) throws TrouNonTrouveException {
        char h = '0';
        return trouveCharEtTransformeEnTrous(resultRows, h);
    }
    public List<Trou> trouveLacs(List<String> resultRows) throws TrouNonTrouveException {
        char h = 'X';
        return trouveCharEtTransformeEnTrous(resultRows, h);
    }
    private List<Trou> trouveCharEtTransformeEnTrous(List<String> resultRows, char h) throws TrouNonTrouveException {
        List<Trou> trous = new ArrayList();
        for(int y = 0; y< resultRows.size(); y++){
            for (int x = 0; x< resultRows.get(y).length(); x++)
            {
                char charAtPosition = resultRows.get(y).charAt(x);
                if (charAtPosition == h) {
                    trous.add(new Trou(new Integer[]{x,y}));
                }
            }
        }
        if (trous.size()==0) throw new TrouNonTrouveException();
        return trous;
    }

    private List<String> removeThisTrouFromMap(List<String> resultRows, Trou trou) {
        resultRows.set(trou.getY(),changeCharAt(resultRows.get(trou.getY()),trou.getX(),"."));
        return resultRows;
    }

    List<String> testerBalleEtReecrirePlan(Ball balle, List<String> rows, Trou trou) throws ToutRefaireAvecNelleCombinaisonCouples {
        String chemin ="";
        chemin = rechercheParStrategies(balle, rows, trou);
        balle.ecrireAuPropreLeChemin(rows, chemin);
        return rows;
    }

    private String rechercheParStrategies(Ball balleOriginal, List<String> rowsOriginal, Trou trou) throws ToutRefaireAvecNelleCombinaisonCouples {
        List<String> strategieDirection =pickupFirstStrategie();
        Ball balleExperimentee=null;
        List<String> rowsCopie = new ArrayList(rowsOriginal);
        try {
                System.out.println("strategie en cours : "+strategieDirection);
                BallEtMap ballExperimenteeEtMapConsequent = rechercheMemorisee(balleOriginal, rowsCopie, trou, strategieDirection);
                printChemin(rowsCopie);
                balleExperimentee= ballExperimenteeEtMapConsequent.getBalle();
            } catch (StrategiePerdante e) {
                //System.out.println("Strategie "+strategieNumber+" perdante POUR CETTE BALLE");
                balleExperimentee= e.getBallWithInvalidPaths();
            }
            String cheminParcouruLePlusCourt = balleExperimentee.getCheminParcouruLePlusCourt();
            if (cheminParcouruLePlusCourt.length()==0) throw new ToutRefaireAvecNelleCombinaisonCouples();
            return cheminParcouruLePlusCourt;
    }
    private List<String> pickupFirstStrategie() {
        reinitialiserStrategies();
        List<String> ret = Arrays.asList(strategies.get(strategieNumber).split(","));
        //System.out.println("STRATEGIE: "+strategieNumber+" " +ret);
        return ret;
    }

    public List<String> pickupNextStrategie() throws IndexOutOfBoundsException {
        strategieNumber++;
        if (isToutesLesStrategiesOntJoue()) throw new IndexOutOfBoundsException();
        List<String> ret = Arrays.asList(strategies.get(strategieNumber).split(","));
        //System.out.println("STRATEGIE: "+strategieNumber+" " +ret);
        return ret;
    }

    private boolean isToutesLesStrategiesOntJoue() {
        return strategieNumber >= strategies.size();
    }


    public BallEtMap rechercheMemorisee(Ball balleOriginale, List<String> rows, Trou trou, List<String> strategieDirections) throws StrategiePerdante, ToutRefaireAvecNelleCombinaisonCouples {
        //System.out.println("NOUVELLE RECHERCHE MEMORISEE "+balleOriginale + " Trou: "+trou);
        //printChemin(rows,balleOriginale,trou);
        Ball balle=Ball.createNewInstance(balleOriginale);
        balle.initIDDirectionForScores(strategieDirections);
        while (!isHTrouve(balle,trou)) {
            System.out.println("stack: " + balle.stackDirection);
            if (isBallAToutExplorePourCeScore(strategieDirections, balle)){
              if (balle.score==balle.scoreInitial) throw new ToutRefaireAvecNelleCombinaisonCouples();
              else {
                  retourArriere(rows,balle);
              }
            }
            if (balle.isToutesLesDirectonsOntJouePourCeScore()) throw new ToutRefaireAvecNelleCombinaisonCouples();
            String direction =balle.getDirectionForScore(strategieDirections);
            while(!isBallAToutExplorePourCeScore(strategieDirections, balle)){
                if (!isBallCanMooveInDir(balle, direction, rows,trou)){
                    balle.incrementIdDirectionForScore();
                    if (balle.isToutesLesDirectonsOntJouePourCeScore()) break;
                    direction =balle.getDirectionForScore(strategieDirections);
                    continue;
                }
                avanceBalleDansDirection(balle, rows, direction,trou);
                printChemin(rows, balle, trou);
                if (isTombeDansLac(balle)) break;
                if (isHTrouve(balle,trou)) {
                    balle.sauveParcourtEtReinitStack();
                    System.out.println("TROUVE!");
                    return new BallEtMap(balle,rows);
                }
                if (isBallAToutExplorePourCeScore(strategieDirections, balle) ) break;
                direction =balle.getDirectionForScore(strategieDirections);
            }
            if (isTombeDansLac(balle)) retourArriere(rows,balle);
            else if (balle.isToutesLesDirectonsOntJouePourCeScore()) {
                if (balle.isATrouveChemin()) {
                    return new BallEtMap(balle,rows);
                }
                else {
                    retourArriere(rows, balle);
                }
                printChemin(rows,balle,trou);
            }
            /*String lastDirection = balle.getLastDirection(balle);
            iddirections = sauteIdDirectionPrecedent(strategieDirections, lastDirection);
            if (isAfaitCetteSerieDeDirections(strategieDirections, iddirections)) iddirections=0;*/
        }
        return new BallEtMap(balle,rows);
    }

    private void retourArriere(List<String> rows, Ball balle) throws StrategiePerdante {
        String lastDirection = balle.getLastDirection(balle);
        /*if (balle.isToutesLesDirectonsOntJouePourCeScore()){
            balle.invaliderLeSousChemin(balle, rows);
        }*/
        balle.resetIdDirectionForScore();
        rebrousseChemin(balle, getOppose(lastDirection, balle),rows);
        balle.incrementIdDirectionForScore();
        System.out.println("aaaaaaaaaaaaaaaaaaaaa");
        printChemin(rows,balle);
        System.out.println("bbbbbbbbbbbbbbbbbb");
    }

    private void printChemin(List<String> rows, Ball balle) {
        ArrayList rowsToPrint = new ArrayList(Arrays.asList(rows.toArray()));
        changeCharAt(rowsToPrint,balle.getX(),balle.getY(),"B");
        printrowsArray(rowsToPrint);
    }

    private boolean isBallAToutExplorePourCeScore(List<String> strategieDirections, Ball balle) {
        return balle.getIdDirectionForScore() >= strategieDirections.size();
    }

    private boolean isTombeDansLac(Ball balle) {
        boolean isTombeDansLac = identifie(this.initialMap,balle.getX(),balle.getY())=='X';
        if (isTombeDansLac) return true;
        return false;
    }

    public void avanceBalleDansDirection(Ball balle, List<String> rowsCopie, String direction, Trou trou) {
        for (int scorestant=balle.score; scorestant>0; scorestant--) {
            char caseb = identifie(rowsCopie, balle.getX(), balle.getY());
            balle.siLacMarqueLac(caseb);
             changeCharAt(rowsCopie, balle.getX(), balle.getY(), direction);
            Case casedirection = new Case(Character.toString(caseb), balle.getX(), balle.getY(), direction);
            balle.ajouteChemin(casedirection);
            balle.changeBallPosition(direction);
        }
        balle.decrementeScore();
    }

    private boolean isAfaitCetteSerieDeDirections(List<String> strategieDirections, int iddirections) {
        return iddirections > strategieDirections.size() - 1;
    }

    private int sauteIdDirectionPrecedent(List<String> strategieDirections, String lastDirection) {
        int iddirections;
        iddirections = strategieDirections.indexOf(lastDirection) + 1;
        return iddirections;
    }


    private boolean isToutesLesDirectonsOntJoue(List<String> strategieDirections, int iddirections) {
        return iddirections > strategieDirections.size() - 1;
    }

    public static void changeCharAt(List<String> rows, int x, int y, String marque) {
        rows.set(y,changeCharAt(rows.get(y),x,marque));
    }

    private String getOppose(String direction, Ball balle) throws StrategiePerdante {
        if (direction.equals(">")) return "<";
        if (direction.equals("v")) return "^";
        if (direction.equals("<")) return ">";
        if (direction.equals("^")) return "v";
        else throw new StrategiePerdante(balle);
    }



    public void rebrousseChemin(Ball balle, String direction, List<String> rows){
        int previousScore = balle.score+1;
        for (int step=0; step<previousScore;step++) {
            balle.removeLastStep(rows);
            printChemin(rows);
            if (direction.equals(">")) balle.incrementX();
            if (direction.equals("<")) balle.decrementX();
            if (direction.equals("v")) balle.incrementY();
            if (direction.equals("^")) balle.decrementY();
        }
        balle.incrementeScore();
    }
    public boolean isBallCanMooveInDir(Ball balle, String direction, List<String> rows, Trou trouCible) {
        if (balle.score==0) return false;
        if (direction.equals("v")) return canMooveDown(balle, rows,trouCible);
        if(direction.equals("<")) return canMooveLeft(balle,rows,trouCible);
        if(direction.equals(">")) return canMooveRight(balle,rows,trouCible);
        if(direction.equals("^"))return canMooveUp(balle,rows,trouCible);
        return false;
    }

    private boolean canMooveUp(Ball balle, List<String> rows, Trou trouCible) {
        int posy = balle.getY();
        for (int pas = 0; pas <= balle.score-1; pas++) {
            posy--;
            boolean isDansPlan = posy > -1;
            boolean validDest = isValidDest(balle.getX(), posy, rows, trouCible);
            if (!(isDansPlan && validDest)) return false;
        }
        return true;
    }



    private boolean canMooveLeft(Ball balle, List<String> rows, Trou trouCible) {
        int posx = balle.getX();
        for (int pas = 0; pas <= balle.score-1; pas++) {
            posx--;
            boolean isDansPlan = posx > -1;
            boolean validDest = isValidDest(posx, balle.getY(), rows, trouCible);
            if (!(isDansPlan && validDest)) return false;
        }
        return true;
    }

    public boolean canMooveRight(Ball balle, List<String> rows, Trou trouCible) {
        int posx = balle.getX();
        for (int pas = 0; pas <= balle.score-1; pas++) {
            posx++;
            boolean isDansPlan = posx < rows.get(0).length();
            boolean validDest = isValidDest(posx, balle.getY(), rows, trouCible);
            if (!(isDansPlan && validDest)) return false;
        }
        return true;
    }


    private boolean canMooveDown(Ball balle, List<String> rows, Trou trouCible) {
        int posy = balle.getY();
        for (int pas = 0; pas <= balle.score-1; pas++) {
            posy++;
            boolean isDansPlan = posy < rows.size();
            boolean validDest = isValidDest(balle.getX(), posy, rows, trouCible);
            if (!(isDansPlan && validDest)) return false;
        }
        return true;
    }

    private boolean isValidDest(int x, int y, List<String> rows, Trou trouCible) {
        if (isResolvedTrouInThisPosition(x,y)) return false;
        char identifie = identifie(rows, x, y);
        return identifie == '.' || identifie == 'X' || (identifie == 'H' && trouCible.getX()==x && trouCible.getY()==y);

    }

    private boolean isResolvedTrouInThisPosition(int x,int y) {
        return resolvedTrous.stream().filter(trou -> trou.getX()==x&&trou.getY()==y).collect(Collectors.toList()).size()>0;
    }

    private int getWith(String dimensions) {
        return Integer.parseInt(dimensions.split(" ")[0]);
    }

    public List<Ball> getBallesFromRow(String row, int y) {
        List<Ball> balles=new ArrayList();
        List<Character> listDeChars = row.chars().mapToObj(c -> (char) c).collect(Collectors.toList());
        IntStream.range(0, listDeChars.size())
                .forEach(x -> {
                    Character valeurDeLaCase = listDeChars.get(x);
                    if(Character.isDigit(valeurDeLaCase)) {
                    int scoreBalle = Integer.parseInt(Character.toString(valeurDeLaCase));
                    balles.add(new Ball(scoreBalle,x,y));
                }
                });
        return balles;
    }

    public static char identifie(List<String> rows, int x, int y) {
        if (x<0 || y<0 ||y>=rows.size() ||x>=rows.get(0).length()) return 'E';
        return rows.get(y).charAt(x);
        /*return rows.get(0)y > 0 && x > 0 && resultRows.get(y).charAt(x - 1) == droite;
                || (y<resultRows.size()-1 && resultRows.get(y).charAt(x +1)=='<')
                || (y>0 && resultRows.get(y-1).charAt(x)=='v')
                || (y<resultRows.size()-1 && resultRows.get(y+1).charAt(x)=='^')) return true;*/
    }

    public List<Ball> trouverBalles(List<String> rows) {
        List<Ball> res =  new ArrayList<>();
        for(int yPos=0; yPos<rows.size(); yPos++){
            List<Ball> balles = getBallesFromRow(rows.get(yPos),yPos);
            res.addAll(balles);
        }
        return res;
    }

    public static String changeCharAt(String str, int at, String by) {
        if (at==0) return by.concat(str.substring(1));
        if (at==str.length()) return str.substring(0,at-1).concat(by).concat(str.substring(at));
        return str.substring(0,at).concat(by).concat(str.substring(at+1));
    }


    public void definirTousBallesEtTrous(List<String> initialMap) throws TrouNonTrouveException, JeuIncompletException {
        balles = trouverBalles(initialMap);
        trous = trouveTrous(initialMap);
        if(balles.size()!=trous.size()) throw new JeuIncompletException();
    }


    public List<String> initialiserJeu(List<String> rows) throws TrouNonTrouveException, NonResoluException, JeuIncompletException {
        reinitialiserStrategies();
        initialiserToutesLesStrategiesDeDeplacement();
        definirTousBallesEtTrous(rows);
        initCombinaisonsCouples();
        if (initialMap==null) initialMap= new ArrayList(Arrays.asList(rows.toArray()));
        List<String> resAvecLacs = jouerChaqueCouple(rows);
        return enleverLacs(resAvecLacs);
    }

    protected void initCombinaisonsCouples() {
        idxCouple=0;
        idxcombinaisonCouples=0;
        allCombinaisonsCouples =genererAllCombinaisons(balles,trous);
    }

    private List<String> enleverZeros(List<String> rowsCopie) {
        try {
            List<Trou> zeros = trouveZeros(rowsCopie);
            return removeObjetFromMap(rowsCopie, zeros);
        } catch (TrouNonTrouveException e) {
            return rowsCopie;
        }
    }
    public List<String> enleverLacs(List<String> resAvecLacs) {
        try {
            List<Trou> lacs = trouveLacs(resAvecLacs);
            return removeObjetFromMap(resAvecLacs, lacs);
        } catch (TrouNonTrouveException e) {
            return resAvecLacs;
        }
    }

    private List<String> removeObjetFromMap(List<String> resAvecLacs, List<Trou> lacs) {
        lacs.stream().forEach(l -> {removeThisTrouFromMap(resAvecLacs, l);});
        return resAvecLacs;
    }

    public void initialiserToutesLesStrategiesDeDeplacement() {
        CombinaisonBlock combinaison = new CombinaisonBlockString("", new ArrayList(Arrays.asList("v,^,<,>".split(","))));
        //strategies= combinaison.getToutesCombinaisonsString();
        strategies= List.of(combinaison.getToutesCombinaisonsString().get(0).toString());
        System.out.println(strategies);
    }

    private void toutRecommencerAvecNouvellePremiereBalle() throws NonResoluException {
        resolvedTrous.clear();
        resolvedBall.clear();
        idxcombinaisonCouples++;
        while (isCoupleInvalidesInCombinaisonCouples()) idxcombinaisonCouples++;
        //System.out.println("idxcombicouples"+idxcombinaisonCouples);
        idxCouple=0;
        if(idxcombinaisonCouples>allCombinaisonsCouples.size()) throw new NonResoluException();
    }

    private boolean isCoupleInvalidesInCombinaisonCouples() throws NonResoluException {
        if(idxcombinaisonCouples>allCombinaisonsCouples.size()-1){
            System.out.println(idxcombinaisonCouples+" " +allCombinaisonsCouples.size());
            throw new NonResoluException();
        }
        List<CoupleBalleTrou> coupleBalleTrous = allCombinaisonsCouples.get(idxcombinaisonCouples);
        for (CoupleBalleTrou couple:coupleBalleTrous)
        {
            if (couplesInvalide.contains(couple)) return true;
        }
        return false;
    }

    public List<String> jouerChaqueCouple(List<String> rowsAvantEssai) throws NonResoluException {
        List<String> nouveauPlanResolu = null;
        ArrayList<String> copieOriginal = new ArrayList(rowsAvantEssai);
        CoupleBalleTrou couple =null;
        while (true) {
            try {
                while (resolvedBall.size() < balles.size()) {
                    couple = getNextCoupleBalleTrou();
                    if (couplesInvalide.contains(couple)) continue;
                    Ball copieBalle = Ball.createNewInstance(couple.balle);
                    System.out.println("CHANGER COUPLE " + copieBalle + " " + couple.trou +" couples invalides: " + couplesInvalide.size()+" idCombiCouple: "+idxcombinaisonCouples+"/"+allCombinaisonsCouples.size() );
                    nouveauPlanResolu = resoudreBalle(copieBalle, rowsAvantEssai, couple.trou);
                    reinitialiserStrategies();
                    rowsAvantEssai = nouveauPlanResolu;
                }
                return nouveauPlanResolu;
            } catch (ToutRefaireAvecNelleCombinaisonCouples e) {
                System.out.println("COMBI KO "+couple);
                 printChemin(rowsAvantEssai, couple.balle, couple.trou);
                toutRecommencerAvecNouvellePremiereBalle();
                reinitialiserStrategies();
                rowsAvantEssai = copieOriginal;
            }
        }
    }

    private CoupleBalleTrou getNextCoupleBalleTrou() throws ToutRefaireAvecNelleCombinaisonCouples, NonResoluException {
        if(idxcombinaisonCouples>allCombinaisonsCouples.size()-1) throw new NonResoluException();
        if (idxCouple> allCombinaisonsCouples.get(idxcombinaisonCouples).size()-1) throw new ToutRefaireAvecNelleCombinaisonCouples();
        CoupleBalleTrou couple =  allCombinaisonsCouples.get(idxcombinaisonCouples).get(idxCouple);
        idxCouple++;
        return couple;
    }


    protected void reinitialiserStrategies() {
        strategieNumber=0;
    }

    private boolean isRestEncoreTrous(List<String> res) {
        boolean trouexist = false;
        try {
            return trouveTrous(res).size() > 0;
        } catch (TrouNonTrouveException e) {
            return false;
        }
    }

    public void printChemin(List<String> rows, Ball balle, Trou trou) {
        ArrayList rowsToPrint = new ArrayList(Arrays.asList(rows.toArray()));
        //System.out.println("stack de la balle "+ balle.imprimeStak());
        changeCharAt(rowsToPrint,trou.getX(),trou.getY(),"T");
        changeCharAt(rowsToPrint,balle.getX(),balle.getY(),"B");
        printrowsArray(rowsToPrint);
    }
    public static void printChemin(List<String> rows) {
        ArrayList rowsToPrint = new ArrayList(Arrays.asList(rows.toArray()));
        printrowsArray(rowsToPrint);
    }

    private static void printrowsArray(ArrayList rowsToPrint) {
        System.out.println("-----");
        rowsToPrint.forEach(System.out::println);
        System.out.println("-----");
    }

    public List<List<CoupleBalleTrou>> genererAllCombinaisons(List<Ball> balles, List<Trou> trous){
        System.out.println(trous);
        List<List<CoupleBalleTrou>> allCombis = new ArrayList<>();
        List<String> listeBalles = IntStream.range(0, balles.size()).boxed().map(i->Integer.toString(i)).collect(Collectors.toList());
        CombinaisonBlock combinaisons = new CombinaisonBlockString("",listeBalles);
        List<String> listeCombiTrous = combinaisons.getToutesCombinaisonsString();
        listeCombiTrous.forEach(sequenceTrous->{
            List<CoupleBalleTrou> coupleBalleTrous=new ArrayList<>();
            List<Integer> uneCombinaisonTrous = Arrays.stream(sequenceTrous.split(",")).map(s -> Integer.parseInt(s)).collect(Collectors.toList());
            for (int i=0; i<balles.size(); i++){
                coupleBalleTrous.add(new CoupleBalleTrou(balles.get(i),trous.get(uneCombinaisonTrous.get(i))));
            }
            allCombis.add(coupleBalleTrous);
        });
        System.out.println("couples: "+ allCombis);
        return allCombis;
    }
}
