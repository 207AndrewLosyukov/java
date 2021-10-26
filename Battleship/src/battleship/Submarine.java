package battleship;

import java.util.ArrayList;

public class Submarine extends Boat {
    private static int count = 0;
    private static int iterId = 0;
    private static ArrayList<Submarine> submarines = new ArrayList<>();

    Submarine() {
        super();
        hp = 1;
        size = 1;
    }

    public static int getCountSubmarine() {
        return count;
    }

    public static void addSubmarine() {
        count++;
    }

    public static void setCount (int newCount) {
        count = newCount;
    }

    public static void clear() {
        iterId = 0;
        for (Boat boat : submarines) {
            boat.points = new ArrayList<>();
        }
        submarines = new ArrayList<>();
    }

    @Override
    public int getHp() {
        return hp;
    }

    @Override
    public void setBoat() {
        id = iterId++;
        submarines.add(this);
    }

    @Override
    public void getFire() {
        hp--;
    }

    @Override
    public String toString() {
        return "Submarine";
    }
}
