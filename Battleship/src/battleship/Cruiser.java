package battleship;

import java.util.ArrayList;

public class Cruiser extends Boat {
    private static int count = 0;
    private static int iterId = 0;
    private static ArrayList<Cruiser> cruisers = new ArrayList<>();

    Cruiser() {
        super();
        hp = 3;
        size = 3;
    }

    public static int getCountCruisers() {
        return count;
    }

    public static void addCruiser() {
        count++;
    }

    public static void setCount (int newCount) {
        count = newCount;
    }

    public static void clear() {
        iterId = 0;
        for (Boat boat: cruisers) {
            boat.points = new ArrayList<>();
        }
        cruisers = new ArrayList<>();
    }

    @Override
    public int getHp() {
        return hp;
    }

    @Override
    public void setBoat() {
        id = iterId++;
        cruisers.add(this);
    }

    @Override
    public void getFire() {
        hp--;
    }

    @Override
    public String toString() {
        return "Cruiser";
    }
}
