package com.springchallenge;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

public class PlayerTest {

    public static final int LONGESTDISTANCE = 1000000000;
    private static List<Monster> monsters = new ArrayList<>();
    private static List<Heroe> heroes = new ArrayList<>();
    private static Base base=null;
    private static Map <Heroe,Monster>targetedMonster = new HashMap<>();
    public static void main(String args[]) {
            Scanner in = new Scanner(System.in);
            int baseX = in.nextInt(); // The corner of the map representing your base
            int baseY = in.nextInt();
            int heroesPerPlayer = in.nextInt(); // Always 3
            base = new Base(baseX,baseY);
            // game loop
            while (true) {
                for (int i = 0; i < 2; i++) {
                    int health = in.nextInt(); // Each player's base health
                    int mana = in.nextInt(); // Ignore in the first league; Spend ten mana to cast a spell
                }
                monsters=new ArrayList<>();
                heroes=new ArrayList<>();
                targetedMonster=new HashMap<>();
                int entityCount = in.nextInt(); // Amount of heros and monsters you can see
                for (int i = 0; i < entityCount; i++) {
                    int id = in.nextInt(); // Unique identifier
                    int type = in.nextInt(); // 0=monster, 1=your hero, 2=opponent hero
                    int x = in.nextInt(); // Position of this entity
                    int y = in.nextInt();
                    int shieldLife = in.nextInt(); // Ignore for this league; Count down until shield spell fades
                    int isControlled = in.nextInt(); // Ignore for this league; Equals 1 when this entity is under a control spell
                    int health = in.nextInt(); // Remaining health of this monster
                    int vx = in.nextInt(); // Trajectory of this monster
                    int vy = in.nextInt();
                    int nearBase = in.nextInt(); // 0=monster with no target yet, 1=monster targeting a base
                    int threatFor = in.nextInt(); // Given this monster's trajectory, is it a threat to 1=your base, 2=your opponent's base, 0=neither
                    findAllMonstersAndHeroes(id,type,x,y,health,vx,vy,nearBase,threatFor);
                }
                attribuerMonstreAHeros();
                definirActionsToHeroes();
                for (int i = 0; i < heroesPerPlayer; i++) {
                    int nbr = i;
                    Heroe heroiter = heroes.get(nbr);
                    System.err.println("heroiter="+heroiter);
                    Optional<Heroe> heroe = targetedMonster.keySet().stream().filter(heroeselect -> heroeselect == heroiter).findFirst();
                    System.err.println("hero present ? "+heroe.isPresent());
                    if (heroe.isPresent()) System.out.println(heroe.get().getAction());
                    else System.out.println("MOVE " + base.x+" " +base.y);
                    // Write an action using System.out.println()
                    // To debug: System.err.println("Debug messages...");

                    // In the first league: MOVE <x> <y> | WAIT; In later leagues: | SPELL <spellParams>;
                }
            }
        }

    static void findAllMonstersAndHeroes(int id, int type, int x, int y, int health, int vx, int vy, int nearBase, int threatFor) {
        try {
            monsters.add(detectThreaths(id,type,x,y,health,vx,vy,nearBase,threatFor));
            //System.err.println("nbrMonsters: "+ monsters.size());
        } catch (NotAMonsterException e) {
            try {
                heroes.add(detectHeroes(id,type,x,y,health,vx,vy,nearBase,threatFor));
                //System.err.println("nbrHeroes: "+ heroes.size());
            } catch (NotAHeroException ex) {
            }
        }
    }

    static Heroe detectHeroes(int id, int type, int x, int y, int health, int vx, int vy, int nearBase, int threatFor) throws NotAHeroException {
        if (type==1) return new Heroe(id,x,y);
        throw new NotAHeroException();
    }


    static int calculateDistance(int x0, int y0, int x1, int y1) {
        return (Math.abs(x1-x0)+Math.abs(y1-y0))/2;
    }

    static void attribuerMonstreAHeros(){
        List<Monster> monstreAAttaquer = null;
        for (Heroe heroe:heroes) {
        try {
            monstreAAttaquer = findNotAttackedNearestMonstersToBase();
            //System.err.println("nbr monstre a attaquer: "+monstreAAttaquer.size());
        } catch (NoMonsterFoundException e) {
            try {
                monstreAAttaquer = findAnyNearestMonstersToBase();
            } catch (NoMonsterFoundException ex) {
                makeHeroeGoBackToBase(heroe);
                return;
            }
        }
        Monster monstreattribue=null;
        int previousDistance=LONGESTDISTANCE;
        for (Monster monster : monstreAAttaquer) {
            int distance = calculateDistance(heroe.x, heroe.y, monster.x, monster.y);
            if (distance<previousDistance){
                previousDistance=distance;
                monstreattribue=monster;
            }
        }
        if (monstreattribue!=null){
            System.err.println("AJOUT MONSTRE POUR HERO "+ heroe+ " / "+ monstreattribue);
            targetedMonster.put(heroe,monstreattribue);
        }
    }
    }

    private static void makeHeroeGoBackToBase(Heroe heroe) {
        heroe.setAction("MOVE " +base.x +" " +base.y);
    }

    static void makeAllHeroesGoBackToBase() {
        for (Heroe heroe:heroes) {
            heroe.setAction("MOVE " +base.x +" " +base.y);
        }
    }

    static Monster detectThreaths(int id, int type, int x, int y, int health, int vx, int vy, int nearBase, int threatFor) throws NotAMonsterException {
        if (isMonsterDangerous(type, health, nearBase, threatFor)) return new Monster(id,x,y,vx,vy,nearBase,threatFor,health);
        throw new NotAMonsterException();
    }
    static boolean isMonsterDangerous(int health, int nearBase, int threatFor) {
        //System.err.println("dangerositemonstre: "+health+" " +nearBase+" "+threatFor);
        return health > 0 && ((nearBase == 1 && threatFor == 1) || (nearBase == 0 && threatFor == 1));
    }
    static boolean isMonsterDangerous(int type, int health, int nearBase, int threatFor) {
        //System.err.println("monster: "+ type+" "+health+" " +nearBase+" "+threatFor);
        boolean isMonsterDangerous = type == 0 && isMonsterDangerous(health, nearBase, threatFor);
        //System.err.println("isMonster dangerous: "+ isMonsterDangerous);
        return isMonsterDangerous;
    }

    static class Monster {

        public int health=-1;
        private int id=-1;
        private int x;
        private int y;
        private int vx=-1;
        private int vy=-1;
        private int nearBase=-1;
        private int threatFor=-1;
        public Monster(int id, int x, int y, int vx, int vy, int nearBase, int threatFor) {
            this.id=id;this.x=x;this.y=y;this.vx=vx;this.vy=vy;this.nearBase=nearBase;this.threatFor=threatFor;
        }

        public Monster(int x, int y) {
            this.x=x;
            this.y=y;
        }

        public Monster(int id, int x, int y) {
            this.id=id;
            this.x=x;
            this.y=y;
        }

        public Monster(int health, int id, int x, int y) {
            this.health =health;
            this.id=id;
            this.x=x;
            this.y=y;
        }

        public Monster(int id, int x, int y, int vx, int vy, int nearBase, int threatFor, int health) {
            this.id=id;this.x=x;this.y=y;this.vx=vx;this.vy=vy;this.nearBase=nearBase;this.threatFor=threatFor;
            this.health =health;
        }

        public boolean isTargeted() {
            return false;
        }

        @Override
        public String toString() {
            return "Monster{" +
                    "health=" + health +
                    ", id=" + id +
                    ", x=" + x +
                    ", y=" + y +
                    ", nearBase=" + nearBase +
                    ", threatFor=" + threatFor +
                    '}';
        }

        public void setHealth(int health) {
            this.health =health;
        }
    }
    static class NotAMonsterException extends Exception{

    }
    interface IEntity {

    }
    static class Heroe {

        private  int y;
        private  int id;
        private  int x;
        private String action="WAIT";

        public Heroe(int id, int x, int y) {
            this.id=id;
            this.x=x;
            this.y=y;
        }

        public Heroe() {

        }

        public Monster getTarget() {
            return null;
        }

        public String getAction() {
            System.err.println("action hero: "+this.action);
            return this.action;
        }

        public void setAction(String action) {
            this.action=action;
        }
    }
    static class NotAHeroException extends Exception {

    }
    static class Base {

        private int y=0;
        private int x=0;
        public Base(int baseX, int baseY) {
            this.x=baseX;
            this.y=baseY;
        }

    }
    Monster findNotAttackedNearestMonsterToBase() throws NoMonsterFoundException {
        Monster nearestMonster = null;
        int previousDistance= LONGESTDISTANCE;
        for(Monster monster:monsters){
            if (targetedMonster.values().contains(monster)) continue;
            int distance = calculateDistance(monster.x, monster.y, base.x, base.y);
            if (distance<previousDistance) {
                previousDistance=distance;
                nearestMonster=monster;
            }
        }
        if (nearestMonster==null) throw new NoMonsterFoundException();
        return nearestMonster;
    }
    static List<Monster> findAnyNearestMonstersToBase() throws NoMonsterFoundException {
        List<Monster> nearestMonsters = new ArrayList<>();
        for(Monster monster:monsters) {
            boolean isMonsterDangerous = isMonsterDangerous(monster.health, monster.nearBase, monster.threatFor);
            if (!isMonsterDangerous) continue;
            nearestMonsters.add(monster);
        }
        if (nearestMonsters.size()==0) throw new NoMonsterFoundException();
        return nearestMonsters;
    }
        static List<Monster> findNotAttackedNearestMonstersToBase() throws NoMonsterFoundException {
            List<Monster> nearestMonsters = new ArrayList<>();
            for(Monster monster:monsters) {
                //System.err.println("analyse monstre: "+monster);
                if (targetedMonster.values().contains(monster)) {
                    System.err.println("PASSER!");
                    continue;
                }
                boolean isMonsterDangerous = isMonsterDangerous(monster.health, monster.nearBase, monster.threatFor);
                //System.err.println("monstre dangereux ?: "+ isMonsterDangerous);
                if (!isMonsterDangerous) continue;
                nearestMonsters.add(monster);
            }
            if (nearestMonsters.size()==0) throw new NoMonsterFoundException();
            return nearestMonsters;
        }



    Monster createDangerousMonster(int id, int x, int y) {
        Monster monster=  new Monster(id, x, y,0,0,1, 1);
        monster.setHealth(1);
        return monster;
    }


    static class NoMonsterFoundException extends Throwable {

    }
    static void definirActionsToHeroes() {
        targetedMonster.forEach((h,m)->{chooseCoupleAction(h,m);});
    }

    private static void chooseCoupleAction(Heroe h, Monster m) {
            h.setAction("MOVE "+m.x+ " " +m.y);
            conditionalWindTrigered(h,m);
        }

    private static void conditionalWindTrigered(Heroe heroe, Monster monster) {
        int distanceHeroeMonster = calculateDistance(heroe.x, heroe.y, monster.x, monster.y);
        System.err.println("dist h m "+ distanceHeroeMonster);
        if (calculateDistance(base.x,base.y,monster.x,monster.y)> calculateDistance(base.x,base.y,heroe.x,heroe.y)){
            if (distanceHeroeMonster <=640){
                heroe.setAction("SPELL WIND "+monster.x +" " +monster.y);
            }
        }
    }

    @BeforeEach
    public void init()
    {
        monsters=new ArrayList<>();
        heroes= new ArrayList<>();
        base=null;
        targetedMonster=new HashMap<>();
    }
    @Test
    public void entityIsNotDangerous_detectThreaths_Exception() {
        int id = 1;
        int type =0;
        int x = 85;
        int y = 52;
        int health = 8;
        int vx = 80;
        int vy = -50;
        int nearBase = 0;
        int threatFor = 0;
        Assertions.assertThrows(NotAMonsterException.class,()->{detectThreaths(id,type,x,y,health,vx,vy,nearBase,threatFor);});
    }

    @Test
    public void entityisDangerous_detectThreaths_monster() throws NotAMonsterException {
        int id = 1;int type =0;int x = 85;int y = 52;int health = 8;int vx = 80;int vy = -50;int nearBase = 1;int threatFor = 1;
        Monster monster =detectThreaths(id,type,x,y,health,vx,vy,nearBase,threatFor);
        Assertions.assertNotNull(monster);
        Assertions.assertEquals(8,monster.health);
        Assertions.assertEquals(1,monster.id);
        Assertions.assertEquals(1,monster.nearBase);
        Assertions.assertEquals(1,monster.threatFor);
        Assertions.assertEquals(85,monster.x);
        Assertions.assertEquals(52,monster.y);
    }

    @Test
    public void noMonsterLeft_findMonsterToAttack_heroeWait() throws NoMonsterFoundException {
        base = new Base(0,0);
        Heroe heroe = new Heroe(1,0,0);
        heroes.add(heroe);
        attribuerMonstreAHeros();
        Assertions.assertEquals(heroe.getAction(),"MOVE 0 0");
    }
    @Test
    public static void testFindAllMonstersAndHeroes()
    {
        for (int i=0; i<12; i++) {
            int id = 1;int type =0;int x = 85;int y = 52;int health = 8;int vx = 80;int vy = -50;int nearBase = 1;int threatFor = 1;
            if (i>4) type=1;
            findAllMonstersAndHeroes(id,type,x,y,health,vx,vy,nearBase,threatFor);
        }
        Assertions.assertEquals(5,monsters.size());
        Assertions.assertEquals(7,heroes.size());
    }

    @Test
    public void testCalculateDistance()
    {
        int x0=0;int y0=0;
        int x1=0;int y1=0;
        Assertions.assertEquals(0,calculateDistance(x0,y0,x1,y1));
        x0=0;y0=0;
        x1=1;y1=1;
        Assertions.assertEquals(1,calculateDistance(x0,y0,x1,y1));
        x0=0;y0=0;
        x1=2;y1=2;
        Assertions.assertEquals(2,calculateDistance(x0,y0,x1,y1));
        x0=2;y0=2;
        x1=0;y1=0;
        Assertions.assertEquals(2,calculateDistance(x0,y0,x1,y1));
        x0=2;y0=10;
        x1=0;y1=0;
        Assertions.assertEquals(6,calculateDistance(x0,y0,x1,y1));
    }

    @Test
    public void testFindNearestMonsterToBase() throws NoMonsterFoundException {
        base=new Base(5,5);
        monsters.add(new Monster(0,0));
        monsters.add(new Monster(0,20));
        Monster monster = findNotAttackedNearestMonsterToBase();
        Assertions.assertEquals(monster,monsters.get(0));
    }

    @Test
    public void targetedMonster_testFindNearestMonsterToBase_ignoreTargetedMonster() throws NoMonsterFoundException {
        base=new Base(5,5);
        monsters.add(new Monster(0,0,0));
        monsters.add(new Monster(1,0,20));
        Monster nearestMonster = new Monster(2,4, 4);
        targetedMonster.put(new Heroe(),nearestMonster);
        monsters.add(nearestMonster);
        Monster monster = findNotAttackedNearestMonsterToBase();
        Assertions.assertEquals(monsters.get(0),monster);
    }
    @Test
    public void listOfMonsters_findNearestToBaseMonsterToAttack_heroeTargetMonster() throws NoMonsterFoundException {
        base=new Base(80, 80);
        monsters.add(createDangerousMonster(0,0,0));
        monsters.add(createDangerousMonster(1,0,5));
        monsters.add(createDangerousMonster(2,20,20));
        monsters.add(createDangerousMonster(3,0,1));
        monsters.add(createDangerousMonster(4,10,5));
        Heroe heroe = new Heroe(1,15,12);
        heroes.add(heroe);
        attribuerMonstreAHeros();
        Monster attackedMonster = monsters.get(2);
        Assertions.assertEquals(attackedMonster,targetedMonster.get(heroe));
        Assertions.assertTrue(targetedMonster.containsValue(attackedMonster));
    }

    @Test
    public void moreHeroesThanMonsters_allheroesGoesToUniqueMonster() throws NoMonsterFoundException {
        base = new Base(4,4);
        Heroe h1 = new Heroe(1, 0, 0);
        heroes.add(h1);
        Heroe h2 = new Heroe(2, 7, 7);
        heroes.add(h2);
        Monster m1 = createDangerousMonster(1, 1, 7);
        monsters.add(m1);
        attribuerMonstreAHeros();
        Assertions.assertEquals(m1,targetedMonster.get(h1));
        Assertions.assertEquals(m1,targetedMonster.get(h2));
    }

    @Test
    public void heroesAndMonster_heroesGoesToBestMonster() throws NoMonsterFoundException {
        base = new Base(4,4);
        Heroe h1 = new Heroe(1, 0, 0);
        heroes.add(h1);
        Heroe h2 = new Heroe(2, 7, 7);
        heroes.add(h2);
        Monster m1 = createDangerousMonster(1, 1, 7);
        monsters.add(m1);
        Monster m2 = createDangerousMonster(2, 7, 3);
        monsters.add(m2);
        Heroe h3 = new Heroe(3, 10, 10);
        heroes.add(h3);
        attribuerMonstreAHeros();
        Assertions.assertEquals(m1,targetedMonster.get(h1));
        Assertions.assertEquals(m2,targetedMonster.get(h2));
        Assertions.assertEquals(m2,targetedMonster.get(h3));
    }
    @Test
    public void faireBougerHeroesVersMonstres()
    {
        base = new Base(10,10);
        targetedMonster.put(new Heroe(0,0,0),createDangerousMonster(0,1,1));
        targetedMonster.put(new Heroe(1,1,1),createDangerousMonster(1,3,3));
        targetedMonster.put(new Heroe(2,2,2),createDangerousMonster(2,5,5));
        definirActionsToHeroes();
        Iterator<Heroe> it = targetedMonster.keySet().stream().iterator();
        while (it.hasNext()){
            Heroe h = it.next();
            System.out.println(h.getAction());
            Assertions.assertTrue(h.getAction().startsWith("MOVE"));
        }

    }
    @Test
    public void monsterTooFar_triggerWind_NowindTriggered(){
        base = new Base(0,0);
        Heroe heroe = new Heroe(1, 3, 3);
        conditionalWindTrigered(heroe,createDangerousMonster(1,1290,1290));
        Assertions.assertEquals("WAIT",heroe.getAction());
    }
    @Test
    public void monsterCloserToBaseThanHeroe_triggerWind_NowindTriggered(){
        base = new Base(1300,1300);
        Heroe heroe = new Heroe(1, 3, 3);
        conditionalWindTrigered(heroe,createDangerousMonster(1,1290,1290));
        Assertions.assertEquals("WAIT",heroe.getAction());
    }

    @Test
    public void monsterIsALaPorteeDeWindApresBase_triggerWind_windTriggered(){
        base = new Base(0,0);
        Heroe heroe = new Heroe(1, 3, 3);
        conditionalWindTrigered(heroe,createDangerousMonster(1,4,4));
        Assertions.assertEquals("SPELL WIND 4 4",heroe.getAction());
    }
}


