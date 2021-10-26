package battleship;

import java.util.Objects;

/*
Океан - поле боя.
 */
public class Ocean {
    /*
    Размеры океана.
     */
    private final int n;
    private final int m;
    /*
    Массив точек представляющий нам океан (у каждый точки есть поле лодка).
     */
    private static Point[][] ocean;
    /*
    Количество суден каждого вида.
     */
    private int submarines;
    private int destroyers;
    private int cruisers;
    private int battleships;
    private int carriers;
    private int summaryBoats;
    /*
    Режим игры.
     */
    private String mode;
    /*
    Предыдущая точка для режима recovery.
     */
    private Point previousPoint;

    public Ocean(int n, int m) {
        this.n = n;
        this.m = m;
    }

    /*
    Установление режима.
     */
    public void setMode(String mode) {
        this.mode = mode;
    }

    /*
    Рандомная расстановка лодок.
     */
    public boolean randomSetBoats() {
        setCountsOfBoats();
        summaryBoats = submarines + destroyers + cruisers + battleships + carriers;
        /*
        Проверка на возможность расставить в целом, если нельзя то сразу говорим, что нельзя.
         */
        if (isOceanValid(ocean, n, m)) {
            setCountsOfBoats();
            ocean = getStartUserOcean();
            Boat.TypeofBoat[] type = Boat.TypeofBoat.values();
            for (int i = 0; i < 5; i++) {
                /*
                Пробуем рандомить - если не получается, возвращаем ручную расстановку,
                которую использовали для проверки.
                 */
                if (!randomizeOcean(ocean, type[4 - i], 5 - i)) {
                    setCountsOfBoats();
                    ocean = getStartUserOcean();
                    isOceanValid(ocean, n, m);
                    break;
                }
            }
        } else {
            return false;
        }
        return true;
    }

    /*
    Установка количества лодок.
     */
    private void setCountsOfBoats() {
        submarines = Submarine.getCountSubmarine();
        destroyers = Destroyer.getCountDestroyers();
        cruisers = Cruiser.getCountCruisers();
        battleships = Battleship.getCountBattleships();
        carriers = Carrier.getCountCarriers();
        ocean = getStartUserOcean();
    }

    /*
    Суммарное количество лодок.
    */
    public int getSummaryBoats() {
        return summaryBoats;
    }

    /*
     Выстрел в лодку, в зависимости от режима.
     */
    public void fire(int x, int y) {
        if (ocean[y][x].isActive) {
            System.out.println("You are already shot here");
        } else {
            if (ocean[y][x].boat != null && !Objects.equals(mode, "recovery")) {
                switch (mode) {
                    case "classic":
                        ocean[y][x].fire();
                        if (ocean[y][x].boat.getHp() == 0) {
                            if (--summaryBoats == 0) {
                                System.out.printf("Killed %s !%n", ocean[y][x].boat);
                                System.out.println("You Win!");
                            } else {
                                System.out.printf("Killed %s !%n", ocean[y][x].boat);
                            }
                        } else {
                            System.out.println("Hit the target!");
                        }
                        break;
                    case "torpedo":
                        if (previousPoint != null) {
                            if (previousPoint.boat != null && ocean[y][x].boat != null
                                    && previousPoint.boat != ocean[y][x].boat && previousPoint.boat.getHp() != 0) {
                                previousPoint.heal();
                            }
                        }
                        ocean[y][x].torpedoFire();
                        if (--summaryBoats == 0) {
                            System.out.printf("Killed %s !%n", ocean[y][x].boat);
                            System.out.println("You Win!");
                        } else {
                            System.out.printf("Killed %s !%n", ocean[y][x].boat);
                        }
                        break;
                }
            } else if (Objects.equals(mode, "recovery")) {
                if (ocean[y][x].boat == null) {
                    ocean[y][x].fire();
                    if (previousPoint != null && previousPoint.boat.getHp() != 0) {
                        previousPoint.heal();
                    }
                    System.out.println("Missed!");
                } else {
                    if (previousPoint != null) {
                        if (previousPoint.boat != null && ocean[y][x].boat != null
                                && previousPoint.boat != ocean[y][x].boat && previousPoint.boat.getHp() != 0) {
                            previousPoint.heal();
                        }
                    }
                    ocean[y][x].fire();
                    if (ocean[y][x].boat.getHp() == 0) {
                        if (--summaryBoats == 0) {
                            System.out.printf("Killed %s !%n", ocean[y][x].boat);
                            System.out.println("You Win!");
                        } else {
                            System.out.printf("Killed %s !%n", ocean[y][x].boat);
                        }
                    }
                    previousPoint = ocean[y][x];
                }
            } else {
                ocean[y][x].fire();
                System.out.println("Missed!");
            }
        }
    }

    /*
    Рандомизация океана.
     */
    private boolean randomizeOcean(Point[][] ocean, Boat.TypeofBoat type, int sizeBoat) {
        int count = getCountCurrentType(type);
        int exit = Math.max((int) (n * m * Math.log(n) * Math.log(m)), 500);
        while (count > 0 && exit-- > 0) {
            int[] randomLocation = getRandomLocation(ocean, sizeBoat);
            if (checkValidAreaAndInsert(ocean, randomLocation[0], randomLocation[1], randomLocation[2], sizeBoat)) {
                count--;
                takeCurrentType(type);
            }
        }
        return count == 0;
    }

    /*
    Вычитание одной лодки.
     */
    private void takeCurrentType(Boat.TypeofBoat type) {
        switch (type) {
            case SUBMARINE -> submarines--;
            case DESTROYER -> destroyers--;
            case CRUISER -> cruisers--;
            case BATTLESHIP -> battleships--;
            case CARRIER -> carriers--;
        }
    }

    /*
    Получение количества лодки нужного типа.
     */
    private int getCountCurrentType(Boat.TypeofBoat type) {
        return switch (type) {
            case SUBMARINE -> submarines;
            case DESTROYER -> destroyers;
            case CRUISER -> cruisers;
            case BATTLESHIP -> battleships;
            default -> carriers;
        };
    }

    /*
    Получение рандомной незанятой локации.
     */
    private int[] getRandomLocation(Point[][] ocean, int sizeBoat) {
        int[] randomLocation = new int[3];
        int countIter = 0;
        int exit = (int) (n * m * Math.log(n) * Math.log(m));
        do {
            randomLocation[2] = (int) (Math.random() * 2);
            /*
            Расположение - горизонтальное.
             */
            if (randomLocation[2] == 0) {
                randomLocation[0] = (int) (Math.random() * (m - sizeBoat + 1));
                randomLocation[1] = (int) (Math.random() * n);
            }
            /*
            Расположение - вертикальное.
             */
            else {
                randomLocation[0] = (int) (Math.random() * m);
                randomLocation[1] = (int) (Math.random() * (n - sizeBoat + 1));
            }
            if (countIter++ == exit) break;
        } while (ocean[randomLocation[1]][randomLocation[0]].boat != null);
        return randomLocation;
    }

    /*
    Проверка валидности и вставка, если это возможно.
     */
    private boolean checkValidAreaAndInsert(Point[][] ocean, int randXPos, int randYPos, int randSide, int size) {
        int extraXLen = 0;
        int extraYLen = 0;
        switch (randSide) {
            case 0 -> extraXLen = size;
            case 1 -> extraYLen = size;
        }
        /*
        Вырезаем необходимую зону, котору. нужно проверить.
         */
        int firstX = Math.max(0, randXPos - 1);
        int firstY = Math.max(0, randYPos - 1);
        int secondX = Math.min(randXPos + extraXLen + 1, m - 1);
        int secondY = Math.min(randYPos + extraYLen + 1, n - 1);
        boolean result = true;
        for (int i = firstY; i <= secondY; i++) {
            for (int j = firstX; j <= secondX; j++) {
                if (ocean[i][j].boat != null) {
                    result = false;
                    break;
                }
            }
        }
        /*
        Создаем новые точки, ставим туда корабль. Кораблю добавляем точки.
         */
        if (result) {
            Boat boat;
            switch (size) {
                case 1 -> boat = new Submarine();
                case 2 -> boat = new Destroyer();
                case 3 -> boat = new Cruiser();
                case 4 -> boat = new Battleship();
                default -> boat = new Carrier();
            }
            if (extraXLen == 0) {
                for (int i = randYPos; i < randYPos + size; i++) {
                    Point point = new Point(randXPos, i, boat);
                    ocean[i][randXPos] = point;
                    boat.setPoint(point);
                }
            } else {
                for (int j = randXPos; j < randXPos + size; j++) {
                    Point point = new Point(j, randYPos, boat);
                    ocean[randYPos][j] = point;
                    boat.setPoint(point);
                }
            }
            boat.setBoat();
        }
        return result;
    }

    /*
    Рекурсивная проверка океана.
     */
    private boolean isOceanValid(Point[][] ocean, int height, int width) {
        /*
        Выход из рекурсии, если корабли закончились.
         */
        if (!(carriers > 0 || battleships > 0 || cruisers > 0 || destroyers > 0 || submarines > 0)) {
            return true;
        } else {
            if (width > m / 2 && height > n / 2) {
                /*
                Заполнение верха, правого столбца, низа, левого столбца.
                 */
                fillTop(ocean, m - width, width - 2, n - height);
                fillRight(ocean, n - height, height - 2, width - 1);
                fillBottom(ocean, width - 1, m - width + 2, height - 1);
                fillLeft(ocean, height - 1, n - height + 2, m - width);
                return isOceanValid(ocean, height - 2, width - 2);
            } else {
                /*
                Другой выход из рекурсии, если мы уже все поле прошли - ответ метода отрицательный.
                */
                return false;
            }
        }
    }

    /*
    Заполнение верха.
     */
    private void fillTop(Point[][] ocean, int start, int finish, int index) {
        for (int i = start; i < finish; i++) {
            int j = 0;
            // Заполнение верха, начиная с левого верхнего угла.
            if (finish - i >= 5 && carriers > 0) {
                Boat boat = new Carrier();
                carriers--;
                while (j++ < 5) {
                    Point point = new Point(i, index, boat);
                    ocean[index][i++] = point;
                    boat.setPoint(point);
                }
            } else if (finish - i >= 4 && battleships > 0) {
                Boat boat = new Battleship();
                battleships--;
                while (j++ < 4) {
                    Point point = new Point(i, index, boat);
                    ocean[index][i++] = point;
                    boat.setPoint(point);
                }
            } else if (finish - i >= 3 && cruisers > 0) {
                Boat boat = new Cruiser();
                cruisers--;
                while (j++ < 3) {
                    Point point = new Point(i, index, boat);
                    ocean[index][i++] = point;
                    boat.setPoint(point);
                }
            } else if (finish - i >= 2 && destroyers > 0) {
                Boat boat = new Destroyer();
                destroyers--;
                while (j++ < 2) {
                    Point point = new Point(i, index, boat);
                    ocean[index][i++] = point;
                    boat.setPoint(point);
                }
            } else if (finish - i >= 1 && submarines > 0) {
                Boat boat = new Submarine();
                submarines--;
                Point point = new Point(i, index, boat);
                ocean[index][i++] = point;
                boat.setPoint(point);
            }
        }
    }

    /*
    Заполнение правого столбца.
     */
    private void fillRight(Point[][] ocean, int start, int finish, int index) {
        for (int i = start; i < finish; i++) {
            int j = 0;
            // Заполнение правого столбца, начиная с правого верхнего угла.
            if (finish - i >= 5 && carriers > 0) {
                Boat boat = new Carrier();
                carriers--;
                while (j++ < 5) {
                    Point point = new Point(index, i, boat);
                    ocean[i++][index] = point;
                    boat.setPoint(point);
                }
            } else if (finish - i >= 4 && battleships > 0) {
                Boat boat = new Battleship();
                battleships--;
                while (j++ < 4) {
                    Point point = new Point(index, i, boat);
                    ocean[i++][index] = point;
                    boat.setPoint(point);
                }
            } else if (finish - i >= 3 && cruisers > 0) {
                Boat boat = new Cruiser();
                cruisers--;
                while (j++ < 3) {
                    Point point = new Point(index, i, boat);
                    ocean[i++][index] = point;
                    boat.setPoint(point);
                }
            } else if (finish - i >= 2 && destroyers > 0) {
                Boat boat = new Destroyer();
                destroyers--;
                while (j++ < 2) {
                    Point point = new Point(index, i, boat);
                    ocean[i++][index] = point;
                    boat.setPoint(point);
                }
            } else if (finish - i >= 1 && submarines > 0) {
                Boat boat = new Submarine();
                submarines--;
                Point point = new Point(index, i, boat);
                ocean[i++][index] = point;
                boat.setPoint(point);
            }
        }
    }

    /*
    Заполнение низа.
     */
    private void fillBottom(Point[][] ocean, int start, int finish, int index) {
        for (int i = start; i >= finish; i--) {
            int j = 0;
            // Заполнение нижней строки, начиная с правого нижнего угла.
            if (i + 1 - finish >= 5 && carriers > 0) {
                Boat boat = new Carrier();
                carriers--;
                while (j++ < 5) {
                    Point point = new Point(i, index, boat);
                    ocean[index][i--] = point;
                    boat.setPoint(point);
                }
                // мб и+1
            } else if (i + 1 - finish >= 4 && battleships > 0) {
                Boat boat = new Battleship();
                battleships--;
                while (j++ < 4) {
                    Point point = new Point(i, index, boat);
                    ocean[index][i--] = point;
                    boat.setPoint(point);
                }
            } else if (i + 1 - finish >= 3 && cruisers > 0) {
                Boat boat = new Cruiser();
                cruisers--;
                while (j++ < 3) {
                    Point point = new Point(i, index, boat);
                    ocean[index][i--] = point;
                    boat.setPoint(point);
                }
            } else if (i + 1 - finish >= 2 && destroyers > 0) {
                Boat boat = new Destroyer();
                destroyers--;
                while (j++ < 2) {
                    Point point = new Point(i, index, boat);
                    ocean[index][i--] = point;
                    boat.setPoint(point);
                }
            } else if (i + 1 - finish >= 1 && submarines > 0) {
                Boat boat = new Submarine();
                submarines--;
                Point point = new Point(i, index, boat);
                ocean[index][i--] = point;
                boat.setPoint(point);
            }
        }
    }

    /*
    Заполнение левого столбца.
     */
    private void fillLeft(Point[][] ocean, int start, int finish, int index) {
        for (int i = start; i >= finish; i--) {
            int j = 0;
            // Заполнение левого столбца, начиная с левого нижнего угла.
            if (i + 1 - finish >= 5 && carriers > 0) {
                Boat boat = new Carrier();
                carriers--;
                while (j++ < 5) {
                    Point point = new Point(index, i, boat);
                    ocean[i--][index] = point;
                    boat.setPoint(point);
                }
            } else if (i + 1 - finish >= 4 && battleships > 0) {
                Boat boat = new Battleship();
                battleships--;
                while (j++ < 4) {
                    Point point = new Point(index, i, boat);
                    ocean[i--][index] = point;
                    boat.setPoint(point);
                }
            } else if (i + 1 - finish >= 3 && cruisers > 0) {
                Boat boat = new Cruiser();
                cruisers--;
                while (j++ < 3) {
                    Point point = new Point(index, i, boat);
                    ocean[i--][index] = point;
                    boat.setPoint(point);
                }
            } else if (i + 1 - finish >= 2 && destroyers > 0) {
                Boat boat = new Destroyer();
                destroyers--;
                while (j++ < 2) {
                    Point point = new Point(index, i, boat);
                    ocean[i--][index] = point;
                    boat.setPoint(point);
                }
            } else if (i + 1 - finish >= 1 && submarines > 0) {
                Boat boat = new Submarine();
                submarines--;
                Point point = new Point(index, i, boat);
                ocean[i--][index] = point;
                boat.setPoint(point);
            }
        }
    }

    /*
    Стартовый океан.
     */
    private Point[][] getStartUserOcean() {
        Submarine.clear();
        Destroyer.clear();
        Cruiser.clear();
        Battleship.clear();
        Carrier.clear();
        Point[][] ocean = new Point[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                ocean[i][j] = new Point(j, i);
            }
        }
        return ocean;
    }

    /*
    Вывод океана.
     */
    @Override
    public String toString() {
        StringBuilder board = new StringBuilder();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                board.append(ocean[i][j]).append(" ");
            }
            board.append("\n");
        }
        return board.toString();
    }
}
