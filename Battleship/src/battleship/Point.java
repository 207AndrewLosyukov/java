package battleship;

/*
Класс точки в океане.
 */
public class Point {
    private int x;
    private int y;
    /*
    У каждой точки есть свое поле - лодка, так удобно стрелять конкретно в точку.
     */
    Boat boat;
    /*
    Активность точки, чтобы отображалось что на ней, когда в нее стреляли.
     */
    public boolean isActive;

    public Point() {
        isActive = false;
    }

    public Point(int x, int y) {
        this();
        this.x = x;
        this.y = y;
        boat = null;
    }

    public Point(int x, int y, Boat boat) {
        this();
        this.x = x;
        this.y = y;
        this.boat = boat;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    /*
    Выстрел торпеды - это простреливание всего корабля, который на этой точке обычными выстрелами.
     */
    public void torpedoFire() {
        if (boat != null) {
            for (Point point : boat.points) {
                point.fire();
            }
        }
    }

    /*
    Вычитываем количество жизни у корабля, активируем точку.
     */
    public void fire() {
        if (boat != null) {
            if (boat.hp > 0) {
                boat.hp--;
            }
        }
        isActive = true;
    }

    /*
    Лечим корабль на точке.
     */
    public void heal() {
        if (boat instanceof Submarine) {
            boat.hp = 1;
        } else if (boat instanceof Destroyer) {
            boat.hp = 2;
        } else if (boat instanceof Cruiser) {
            boat.hp = 3;
        } else if (boat instanceof Battleship) {
            boat.hp = 4;
        } else if (boat instanceof Carrier) {
            boat.hp = 5;
        }
        for (Point point : boat.points) {
            point.isActive = false;
        }
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    /*
    В зависимости от активности точки и живости корабля выводим обозначение.
     */
    @Override
    public String toString() {
        if (!isActive) return "-";
        if (boat == null) {
            return "*";
        } else {
            if (boat.getHp() > 0) {
                return "+";
            } else {
                return "#";
            }
        }
    }
}
