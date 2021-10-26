package battleship;

import java.util.ArrayList;

/*
 Класс наследник от абстрактной лодки.
 Далее классы-насследники лодки устроены по аналогии с этим.
 */
public class Battleship extends Boat {
    private static int count = 0;
    private static int iterId = 0;
    /*
     Список всех battleship.
     */
    private static ArrayList<Battleship> battleships = new ArrayList<>();

    Battleship() {
        super();
        size = 4;
        hp = 4;
    }

    /*
     Количество battleship.
     */
    public static int getCountBattleships() {
        return count;
    }

    /*
    Добавление количества лодок.
     */
    public static void addBattleship() {
        count++;
    }

    /*
    Получение количества здоровья у лодки
     */
    @Override
    public int getHp() {
        return hp;
    }

    /*
    Добавление лодки в список.
     */
    @Override
    public void setBoat() {
        id = iterId++;
        battleships.add(this);
    }

    /*
    Установка количества лодок.
     */
    public static void setCount(int newCount) {
        count = newCount;
    }

    /*
    Очищение статических методов лодки.
     */
    public static void clear() {
        iterId = 0;
        for (Boat boat : battleships) {
            boat.points = new ArrayList<>();
        }
        battleships = new ArrayList<>();
    }

    /*
    Получение огня.
     */
    @Override
    public void getFire() {
        hp--;
    }

    /*
    Переопределение для вывода названия.
     */
    @Override
    public String toString() {
        return "Battleship";
    }
}


