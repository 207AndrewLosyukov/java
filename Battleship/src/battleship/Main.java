package battleship;

import java.util.Objects;
import java.util.Scanner;

public class Main {

    /*
    Сканнер.
     */
    static Scanner scanner = new Scanner(System.in);

    /*
    Начальное считывание, обработка некорректных значений, запуск игры.
     */
    public static void main(String[] args) {
        Game.startInformation();
        int n = getValidNumber(7, 200);
        int m = getValidNumber(7, 200);
        int sum = 0;
        sum = Init();
        Game game = new Game(n, m);
        while (!game.start()) {
            System.out.println("Incorrect input <too many boats>\n" +
                    "Try more!\n" +
                    "Enter everything from the very beginning.\n");
            n = getValidNumber(7, 200);
            m = getValidNumber(7, 200);
            clear();
            sum = Init();
            game = new Game(n, m);
        }
        System.out.println("Select the game mode (<classic> or <recovery>)");
        game.setMode(getCorrectMode());
        System.out.printf("Set counts of torpedoes (0 <= count <= %s)\n", sum);
        int torpedoes = getValidNumber(0, sum);
        System.out.println("Now insert coordinates");
        game.shooting(torpedoes);
    }

    /*
    Проверка на валидность режима.
     */
    private static String getCorrectMode() {
        String mode = scanner.next();
        while (!Objects.equals(mode, "classic") &&
                !Objects.equals(mode, "recovery")) {
            System.out.println("Game haven't this mode! Choose <classic> or <recovery>");
            mode = scanner.next();
        }
        return mode;
    }

    /*
    Проверка на валидность числа.
     */
    private static int getValidNumber(int left, int right) {
        int n;
        do {
            while (!scanner.hasNextInt()) {
                System.out.println("That not a number!");
                scanner.next();
            }
            n = scanner.nextInt();
            if (n < left || n > right) {
                System.out.printf("Please enter number > %s and <= %s%n", left - 1, right);
            }
        } while (n < left || n > right);
        return n;
    }

    /*
    Инициализация кораблей.
     */
    private static int Init() {
        System.out.println("Enter the number of boats of each type sequentially\n");
        int count;
        int sum = 0;
        for (int k = 0; k < 5; k++) {
            count = getValidNumber(0, 500);
            sum += count;
            Boat.TypeofBoat[] type = Boat.TypeofBoat.values();
            for (int i = 0; i < count; i++) {
                Boat.addBoat(type[4 - k]);
            }
        }
        return sum;
    }

    /*
    Очистка значений, если мы начали вводить неправильно.
     */
    private static void clear() {
        Submarine.setCount(0);
        Destroyer.setCount(0);
        Cruiser.setCount(0);
        Battleship.setCount(0);
        Carrier.setCount(0);
    }
}
