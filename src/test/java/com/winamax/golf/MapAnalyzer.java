package com.winamax.golf;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MapAnalyzer {
    int ballenbr = 0;  int trounbr=0; int combinaisoncoupleNbr =0;
    int strategieNumber=0;
    List<String> strategies;

    private List<Trou> resolvedTrous= new ArrayList();
    private ArrayList initialMap;
    private List<List<CouplesBalleTrouCombines.CoupleBalleTrou>> combinaisoncouples;

    public List<String> resoudreBalle(Ball balle, List<String> rows, Trou trou) throws ToutRefaireAvecNouveauxCouples {
        List<String> resultRows = new ArrayList<>();
            resultRows = testerBalleEtReecrirePlan(balle, rows,trou);
        if (isHTrouve(resultRows, trou)) return MarkTheTrouHasResolved(trou, resultRows);
        throw new ToutRefaireAvecNouveauxCouples();
    }

    private List<String> MarkTheTrouHasResolved(Trou trou, List<String> resultRows) {
        addTrouToResolvedTrouList(trou);
        return removeThisTrouFromMap(resultRows, trou);
    }

    private void addTrouToResolvedTrouList(Trou trou) {
        this.resolvedTrous.add(trou);
    }

    public boolean isHTrouve(List<String> resultRows, Trou trou){
        if (resultRows.size()==0) return false;
        Integer y = trou.y;
        Integer x = trou.x;
            if(identifie(resultRows,x+1,y)== '<'
                || identifie(resultRows,x-1,y)== '>'
                || identifie(resultRows,x,y-1)== 'v'
                || identifie(resultRows,x,y+1)== '^')
                return true;
        return false;
    }

    public List<Trou> trouveTrous(List<String> resultRows) throws TrouNonTrouveException {
        char h = 'H';
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
                if (resultRows.get(y).charAt(x)== h) trous.add(new Trou(new Integer[]{x,y}));
            }
        }
        if (trous.size()==0) throw new TrouNonTrouveException();
        return trous;
    }

    private List<String> removeThisTrouFromMap(List<String> resultRows, Trou trou) {
        resultRows.set(trou.y,changeCharAt(resultRows.get(trou.y),trou.x,"."));
        return resultRows;
    }

    List<String> testerBalleEtReecrirePlan(Ball balle, List<String> rows, Trou trou) throws ToutRefaireAvecNouveauxCouples {
        String chemin ="";
        chemin = rechercheParStrategies(balle, rows, trou);
        ecrireAuPropreLeChemin(balle, rows, chemin);
        return rows;
    }

    private String rechercheParStrategies(Ball balleOriginal, List<String> rowsOriginal, Trou trou) throws ToutRefaireAvecNouveauxCouples {
        List<String> strategieDirection =pickupFirstStrategie();
        Ball balleExperimentee=null;
        List<String> rowsConsequent;
        while(!isToutesLesStrategiesOntJoue()) {
            try {
                BallEtMap ballExperimenteeEtMapConsequent = rechercheMemorisee(balleOriginal, rowsOriginal, trou, strategieDirection);
                balleExperimentee= ballExperimenteeEtMapConsequent.getBalle();
                rowsConsequent = ballExperimenteeEtMapConsequent.getRows();
            } catch (StrategiePerdante e) {
                System.out.println("Strategie "+strategieNumber+" perdante POUR CETTE BALLE");
                balleExperimentee= e.getBallWithInvalidPaths();
                rowsConsequent=rowsOriginal;
            }
                try {strategieDirection = pickupNextStrategie();}catch(IndexOutOfBoundsException e){break;}
                balleOriginal = Ball.copieExperience(balleExperimentee,balleOriginal);
                System.out.println("NOUVELLE STRATEGIE NUMERO "+strategieNumber+" POUR CETTE BALLE :"+ strategieDirection);
        }

        String cheminParcouruLePlusCourt = balleExperimentee.getCheminParcouruLePlusCourt();
        if (cheminParcouruLePlusCourt.length()==0) throw new ToutRefaireAvecNouveauxCouples();
        return cheminParcouruLePlusCourt;
    }

    private List<String> pickupFirstStrategie() {
        reinitialiserStrategies();
        List<String> ret = Arrays.asList(strategies.get(strategieNumber).split(","));
        System.out.println("STRATEGIE: "+strategieNumber+" " +ret);
        return ret;
    }

    public List<String> pickupNextStrategie() throws IndexOutOfBoundsException {
        strategieNumber++;
        if (isToutesLesStrategiesOntJoue()) throw new IndexOutOfBoundsException();
        List<String> ret = Arrays.asList(strategies.get(strategieNumber).split(","));
        System.out.println("STRATEGIE: "+strategieNumber+" " +ret);
        return ret;
    }

    private boolean isToutesLesStrategiesOntJoue() {
        return strategieNumber >= strategies.size();
    }

    private void ecrireAuPropreLeChemin(Ball balle, List<String> rows, String chemin) {
        for (int i = 0; i< chemin.length(); i++){
            String direction= String.valueOf(chemin.charAt(i));
            ecrireLeChemin(balle, rows, direction);
            changeBallPosition(balle,direction, rows, new Trou());
        }
    }

    private void ecrireLeChemin(Ball balle, List<String> rows, String direction) {
        rows.set(balle.y, changeCharAt( rows.get(balle.y), balle.x, direction));
    }

    public BallEtMap rechercheMemorisee(Ball balleOriginale, List<String> rowsOriginal, Trou trou, List<String> strategieDirections) throws StrategiePerdante {
        System.out.println("NOUVELLE RECHERCHE MEMORISEE "+balleOriginale + " Trou: "+trou);
        Ball balle=Ball.createNewInstance(balleOriginale);
        ArrayList<String> rowsCopie = new ArrayList(rowsOriginal);
        int iddirections=0;
        while (!isHTrouve(rowsCopie,trou)) {
            if (balle.isTourneEnRond()){
                if (balle.isATrouveChemin()) return new BallEtMap(balle,rowsCopie);
                throw new StrategiePerdante(balle);
            }
            String direction =strategieDirections.get(iddirections);
            while (isBallCanMoove(balle, direction, rowsCopie,trou)) {
                direction = strategieDirections.get(iddirections);
                avanceBalleDansDirection(balle, rowsCopie, direction,trou);
                printChemin(rowsCopie, balle, trou);
                if (isHTrouve(rowsCopie,trou)) {
                    balle.sauveParcourtEtReinitStack();
                    System.out.println("TROUVE!");
                    return new BallEtMap(balle,rowsCopie);
                }
                iddirections = 0;
                direction = strategieDirections.get(iddirections);
            }
            iddirections++;
            if (balle.score==0 || isToutesLesDirectonsOntJoue(strategieDirections, iddirections)) {
                String lastDirection = balle.getLastDirection();
                iddirections = sauteIdDirectionPrecedent(strategieDirections, lastDirection);
                balle.ajouteStackDeblocage(rowsCopie);
                if (isNaPasEchoue(balle, lastDirection)) {
                    return new BallEtMap(balle,rowsCopie);
                }
                else rebrousseChemin(balle, getOppose(lastDirection));
                printChemin(rowsCopie,balle,trou);
            }
            if (isAfaitCetteSerieDeDirections(strategieDirections, iddirections)) iddirections=0;
        }
        return new BallEtMap(balle,rowsCopie);
    }

    public void avanceBalleDansDirection(Ball balle, ArrayList<String> rowsCopie, String direction, Trou trou) {
        for (int scorestant=balle.score; scorestant>0; scorestant--) {
             changeCharAt(rowsCopie, balle.x, balle.y, direction);
             changeBallPosition(balle, direction, rowsCopie, trou);
             balle.ajouteChemin(direction);
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

    private boolean isNaPasEchoue(Ball balle, String lastDirection) throws StrategiePerdante {
        if (lastDirection.equals("0")) {
            if (balle.isATrouveChemin()) return true;
            throw new StrategiePerdante(balle);
        }
        return false;
    }

    private boolean isToutesLesDirectonsOntJoue(List<String> strategieDirections, int iddirections) {
        return iddirections > strategieDirections.size() - 1;
    }

    public static void changeCharAt(List<String> rows, int x, int y, String marque) {
        rows.set(y,changeCharAt(rows.get(y),x,marque));
    }

    private String getOppose(String direction) {
        if (direction.equals(">")) return "<";
        if (direction.equals("v")) return "^";
        if (direction.equals("<")) return ">";
        else return "v";
    }

    public void changeBallPosition(Ball balle, String direction, List<String> rows, Trou trouCible) {
        if (direction.equals(">")) balle.x++;;
        if (direction.equals("<")) balle.x--;
        if (direction.equals("v")) balle.y++;
        if (direction.equals("^")) balle.y--;
    }

    public void rebrousseChemin(Ball balle, String direction){
        balle.invaliderLeCheminComplet();
        int previousScore = balle.score+1;
        for (int step=0; step<previousScore;step++) {
            balle.removeLastStep();
            if (direction.equals(">")) balle.x++;
            if (direction.equals("<")) balle.x--;
            if (direction.equals("v")) balle.y++;
            if (direction.equals("^")) balle.y--;
        }
        balle.incrementeScore();
    }
    public boolean isBallCanMoove(Ball balle, String direction, List<String> rows, Trou trouCible) {
        if (balle.score==0) return false;
        if (direction.equals("v")) return canMooveDown(balle, rows,trouCible);
        if(direction.equals("<")) return canMooveLeft(balle,rows,trouCible);
        if(direction.equals(">")) return canMooveRight(balle,rows,trouCible);
        if(direction.equals("^"))return canMooveUp(balle,rows,trouCible);
        return false;
    }

    private boolean canMooveUp(Ball balle, List<String> rows, Trou trouCible) {
        int posy = balle.y;
        for (int pas = 0; pas <= balle.score-1; pas++) {
            posy--;
            boolean isDansPlan = posy > -1;
            boolean validDest = isValidDest(balle.x, posy, rows, trouCible);
            if (!(isDansPlan && validDest)) return false;
        }
        return true;
    }



    private boolean canMooveLeft(Ball balle, List<String> rows, Trou trouCible) {
        int posx = balle.x;
        for (int pas = 0; pas <= balle.score-1; pas++) {
            posx--;
            boolean isDansPlan = posx > -1;
            boolean validDest = isValidDest(posx, balle.y, rows, trouCible);
            if (!(isDansPlan && validDest)) return false;
        }
        return true;
    }

    private boolean canMooveRight(Ball balle, List<String> rows, Trou trouCible) {
        int posx = balle.x;
        for (int pas = 0; pas <= balle.score-1; pas++) {
            posx++;
            boolean isDansPlan = posx < rows.get(0).length();
            boolean validDest = isValidDest(posx, balle.y, rows, trouCible);
            if (!(isDansPlan && validDest)) return false;
        }
        return true;
    }


    private boolean canMooveDown(Ball balle, List<String> rows, Trou trouCible) {
        int posy = balle.y;
        for (int pas = 0; pas <= balle.score-1; pas++) {
            posy++;
            boolean isDansPlan = posy < rows.size();
            boolean validDest = isValidDest(balle.x, posy, rows, trouCible);
            if (!(isDansPlan && validDest)) return false;
        }
        return true;
    }

    private boolean isValidDest(int x, int y, List<String> rows, Trou trouCible) {
        if (isResolvedTrouInThisPosition(x,y)) return false;
        return identifie(rows, x, y) == '.' || identifie(rows, x, y) == 'X' || (identifie(rows, x, y) == 'H' && trouCible.x==x && trouCible.y==y);

    }

    private boolean isResolvedTrouInThisPosition(int x,int y) {
        return resolvedTrous.stream().filter(trou -> trou.x==x&&trou.y==y).collect(Collectors.toList()).size()>0;
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

    public char identifie(List<String> rows, int x, int y) {
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


    public List<List<CouplesBalleTrouCombines.CoupleBalleTrou>> definirTousLesCouplesBalleTrou(List<String> initialMap) throws TrouNonTrouveException, JeuIncompletException {
        CouplesBalleTrouCombines couplesBalleTrou = new CouplesBalleTrouCombines();
        List<Ball> balles = trouverBalles(initialMap);
        List<Trou> trous = trouveTrous(initialMap);
        if (trous.size()!=balles.size()) throw new JeuIncompletException();
        couplesBalleTrou.combinerDeuxsens(balles, trous);
        return couplesBalleTrou.toutJeuxCouples;
    }


    public List<String> initialiserJeu(List<String> rows) throws TrouNonTrouveException, JeuIncompletException, NonResoluException {
        reinitialiserStrategies();
        initialiserToutesLesStrategiesDeDeplacement();
        combinaisoncouples = definirTousLesCouplesBalleTrou(rows);
        if (initialMap==null) initialMap= new ArrayList(Arrays.asList(rows.toArray()));
        List<String> resAvecLacs = jouerChaqueCouple(rows);
        return enleverLacs(resAvecLacs);
    }

    public List<String> enleverLacs(List<String> resAvecLacs) {
        try {
            List<Trou> lacs = trouveLacs(resAvecLacs);
            lacs.stream().forEach(l -> {removeThisTrouFromMap(resAvecLacs, l);});
            return resAvecLacs;
        } catch (TrouNonTrouveException e) {
            return resAvecLacs;
        }
    }

    public void initialiserToutesLesStrategiesDeDeplacement() {
        CombinaisonBlock combinaison = new CombinaisonBlock("", new ArrayList(Arrays.asList("v,^,<,>".split(","))));
        strategies= combinaison.getToutesCombinaisonsString();
    }

    private void toutRecommencerAvecDifferentsCouples() throws NonResoluException {
        resolvedTrous.clear();
        combinaisoncoupleNbr++;
        if (combinaisoncoupleNbr>combinaisoncouples.size()-1) throw new NonResoluException();
        System.out.println("CHANGER COUPLES "+combinaisoncouples.get(combinaisoncoupleNbr));
    }
    private List<String> jouerChaqueCouple(List<String> rows) throws TrouNonTrouveException, JeuIncompletException, NonResoluException {
        List<String> nouveauPlanResolu = null;
        ArrayList<String> copieOriginal = new ArrayList(rows);
        try {
            List<CouplesBalleTrouCombines.CoupleBalleTrou> couples = combinaisoncouples.get(combinaisoncoupleNbr);
            for (CouplesBalleTrouCombines.CoupleBalleTrou couple:couples) {
                Ball copieBalle = Ball.createNewInstance(couple.balle);
                printChemin(rows,copieBalle,couple.trou);
                nouveauPlanResolu = resoudreBalle(copieBalle, rows, couple.trou);
                printChemin(nouveauPlanResolu,copieBalle,couple.trou);
                reinitialiserStrategies();
            }
            return nouveauPlanResolu;
        } catch (ToutRefaireAvecNouveauxCouples e) {
            reinitialiserStrategies();
            toutRecommencerAvecDifferentsCouples();
            printChemin(rows,combinaisoncouples.get(combinaisoncoupleNbr).get(0).balle,combinaisoncouples.get(combinaisoncoupleNbr).get(0).trou);
            return jouerChaqueCouple(copieOriginal);
        }
    }



    private void reinitialiserStrategies() {
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
        System.out.println("stack de la balle "+ balle.imprimeStak());
        changeCharAt(rowsToPrint,trou.x,trou.y,"T");
        changeCharAt(rowsToPrint,balle.x,balle.y,"B");
        printrowsArray(rowsToPrint);
    }
    public void printChemin(List<String> rows) {
        ArrayList rowsToPrint = new ArrayList(Arrays.asList(rows.toArray()));
        printrowsArray(rowsToPrint);
    }

    private void printrowsArray(ArrayList rowsToPrint) {
        System.out.println("-----");
        rowsToPrint.forEach(System.out::println);
        System.out.println("-----");
    }


}
