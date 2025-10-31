package Game;
import Droids.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Player {
    ArrayList<Droid> droids;
    ArrayList<Droid> team = new ArrayList<>();
    String name;
    int score;
    int maxCountOfDroids;
    int defmaxCountOfDroids = 3;

    public Player(String name, int maxCountOfDroids){
        this.name = name;
        droids = new ArrayList<>();
        score = 0;
        this.maxCountOfDroids = maxCountOfDroids;
    }
    public Player( String name){
        this.name = name;
        droids = new ArrayList<>();
        score = 0;
        maxCountOfDroids = defmaxCountOfDroids;
    }
    public void addDroid(Droid droid){
        droids.add(droid);
    }

    public void setMaxCountOfDroids(int maxCountOfDroids) {
        this.maxCountOfDroids = maxCountOfDroids;
    }

    public void removeDroid(Droid droid){
        droids.remove(droid);
    }

    public ArrayList<Droid> getDroids(){
        return droids;
    }

    protected void createDroid(){
        int i;
        Scanner sc = new Scanner(System.in);
        System.out.print("\n💬 Введіть ім'я дроїда: ");
        String name = sc.nextLine();
        Menu.showDroidTypeMenu();
        i = Menu.getChoice(1 , 3);
        Droid droid = new Droid();
        switch (i){
            case 1 -> droid = new AttackDroid(name, droid.getDefHp() , droid.getDefDamage());
            case 2 -> droid = new DefensiveDroid(name , droid.getDefHp() , droid.getDefDamage());
            case 3 -> droid = new RepairDroid(name , droid.getDefHp() , droid.getDefDamage());
        }
        addDroid(droid);
    }

    public String getName() {
        return name;
    }

    public void setScore(int score) {
        this.score += score;
    }

    public void showTeam() {
        Menu.clearScreen();
        System.out.println("\n┌────────────────────────────────────────────────────────┐");
        System.out.println("                 КОМАНДА ГРАВЦЯ: " + getName() + "                    ");
        System.out.println("├────────────────────────────────────────────────────────┤");
        if (droids.isEmpty()) {
            System.out.println("│               🚫 КОМАНДА ПОРОЖНЯ                          │");
            System.out.println("└────────────────────────────────────────────────────────┘");
            return;
        }
        for (int i = 0; i < droids.size(); i++) {
            System.out.println("│  " + (i + 1) + ".\n " + droids.get(i).toString() + "                                           │");
        }
        System.out.println("└────────────────────────────────────────────────────────┘");
    }


}
