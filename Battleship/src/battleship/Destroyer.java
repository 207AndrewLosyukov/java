package battleship;

import java.util.ArrayList;

public class Destroyer extends Boat {
    private static int count = 0;
    private static int iterId = 0;
    private static ArrayList<Destroyer> destroyers = new ArrayList<>();

    Destroyer() {
        super();
        hp = 2;
        size = 2;
    }

    public static int getCountDestroyers() {
        return count;
    }

    public static void addDestroyer() {
        count++;
    }

    public static void setCount (int newCount) {
        count = newCount;
    }

    public static void clear() {
        iterId = 0;
        for (Boat boat : destroyers) {
            boat.points = new ArrayList<>();
        }
        destroyers = new ArrayList<>();
    }

    @Override
    public int getHp() {
        return hp;
    }

    @Override
    public void setBoat() {
        id = iterId++;
        destroyers.add(this);
    }

    @Override
    public void getFire() {
        hp--;
    }

    @Override
    public String toString() {
        return "Destroyer";
    }
}
