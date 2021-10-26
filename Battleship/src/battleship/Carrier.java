package battleship;

import java.util.ArrayList;

public class Carrier extends Boat {
    private static int count = 0;
    private static int iterId = 0;
    private static ArrayList<Carrier> carriers = new ArrayList<>();

    Carrier() {
        super();
        hp = 5;
        size = 5;
    }

    public static int getCountCarriers() {
        return count;
    }

    public static void addCarrier() {
        count++;
    }

    public static void setCount (int newCount) {
        count = newCount;
    }

    public static void clear() {
        iterId = 0;
        for (Boat boat: carriers) {
            boat.points = new ArrayList<>();
        }
        carriers = new ArrayList<>();
    }

    @Override
    public int getHp() {
        return hp;
    }

    @Override
    public void setBoat() {
        id = iterId++;
        carriers.add(this);
    }

    @Override
    public void getFire() {
        hp--;
    }

    @Override
    public String toString() {
        return "Carrier";
    }
}
