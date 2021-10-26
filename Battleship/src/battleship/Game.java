package battleship;

import java.util.Scanner;

/*
Класс игры - она в нем запускается и идет.
 */
public class Game {
    private int n;
    private int m;
    /*
    Режим игры.
     */
    private String mode;

    public static Scanner scanner = new Scanner(System.in);

    public Ocean ocean;

    /*
    Конструктор с размерами площадки.
     */
    public Game(int n, int m) {
        this.n = n;
        this.m = m;
    }

    public boolean start() {
        Ocean ocean = new Ocean(n, m);
        if (!ocean.randomSetBoats()) {
            return false;
        }
        this.ocean = ocean;
        return true;
    }

    public void shooting(int torpedoes) {
        while (ocean.getSummaryBoats() != 0) {
            System.out.flush();
            System.out.println("Enter the coordinates <X> and <Y> (1 <= X <= width, 1 <= Y <= height) to shoot\n");
            String input = scanner.nextLine();
            Integer x = 0;
            Integer y = 0;
            boolean isTorpedo = false;
            /*
            Проверка регулярными выражениями на валидность ввода.
             */
            if (!input.matches("T \\d+ \\d+") && !input.matches("\\d+ \\d+")) {
                System.out.print("Bad format (Use <T numb numb> or <numb numb>)\n");
                continue;
            }
            if (input.matches("T \\d+ \\d+")) {
                if (torpedoes == 0) {
                    System.out.print("No torpedoes\n");
                    continue;
                }
                isTorpedo = true;
                torpedoes--;
                String[] strArr = input.split(" ");
                x = Integer.parseInt(strArr[1]);
                y = Integer.parseInt(strArr[2]);
            } else {
                String[] strArr = input.split(" ");
                x = Integer.parseInt(strArr[0]);
                y = Integer.parseInt(strArr[1]);
            }
            if (x < 1 || x > m || y < 1 || y > n) {
                System.out.println("Invalid coordinates\n");
                continue;
            }
            if (isTorpedo) {
                /*
                Установка режима торпед, если стрельба - торпедная.
                 */
                ocean.setMode("torpedo");
            } else {
                /*
                Иначе установка дефолтного режима (classic или recovery) для сохранения
                типичной логики программы, когда стрельба без торпед.
                 */
                ocean.setMode(mode);
            }
            /*
            Выстрел в океан.
             */
            ocean.fire(x - 1, y - 1);
            System.out.println(ocean);
            System.out.printf("You have %s torpedoes\n", torpedoes);
        }
    }

    /*
    Установка нового режима.
     */
    public void setMode(String mode) {
        this.mode = mode;
        ocean.setMode(mode);
    }

    /*
    Стартовая информация.
     */
    public static void startInformation() {
        System.out.println("Hello! You in the game <Battleship>\n" +
                "Please enter the ocean size in the format <height> <width>\n" +
                "Then enter the number of boats in descending order of length");
        System.out.println("Be careful, we have some limit: 6 < size numbers <= 200");
    }
}
