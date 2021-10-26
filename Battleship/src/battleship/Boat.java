package battleship;

import java.util.ArrayList;

/*
Абстрактный класс для других лодок, объединяет функциональные члены.
 */
public abstract class Boat {
    /*
    Количество лодок.
     */
    private static int boatCount;
    /*
    Лист точек, на которых эта лодка находится.
     */
    protected ArrayList<Point> points = new ArrayList<>();
    /*
    Идентификационный номер лодки.
     */
    protected int id;
    /*
    Количество хп.
     */
    protected int hp;
    /*
    Размер.
     */
    protected int size;

    public Boat() {
    }

    /*
    Получение выстрела.
     */
    public abstract void getFire();

    /*
    Получение отсавшихся хп у корабля.
     */
    public abstract int getHp();

    /*
    Установка корабля.
     */
    public abstract void setBoat();

    /*
    Добавление лодки (по сути подсчет каждого типа, нужный в самом начале).
     */
    public static void addBoat(TypeofBoat type) {
        switch (type) {
            case SUBMARINE -> Submarine.addSubmarine();
            case DESTROYER -> Destroyer.addDestroyer();
            case CRUISER -> Cruiser.addCruiser();
            case BATTLESHIP -> Battleship.addBattleship();
            case CARRIER -> Carrier.addCarrier();
        }
    }

    /*
    Установление точки (добавление в лист точек).
     */
    public void setPoint(Point point) {
        points.add(point);
    }

    /*
    Получение размера корабля.
     */
    public int getSize() {
        return size;
    }

    /*
    Перечисление кораблей.
     */
    public enum TypeofBoat {SUBMARINE, DESTROYER, CRUISER, BATTLESHIP, CARRIER}
}