package Utils;

import java.util.Scanner;

public class SysFunc {
    // Використовуємо один спільний Scanner на весь застосунок
    public static Scanner SC = new Scanner(System.in);

    public static int get(int i , int j){
        System.out.println("Введіть число від "+ i +" до "+j);
        while (true) {
            if (SC.hasNextInt()) {
                int t = SC.nextInt();
                SC.nextLine(); // з’їдаємо кінець рядка після числа
                if (t >= i && t <= j) {
                    return t;
                } else {
                    System.out.println("Хибний ввід");
                    System.out.println("Введіть число від " + i + " до " + j);
                }
            } else {
                // Некоректний токен — прибираємо рядок і просимо знову
                String bad = SC.nextLine();
                System.out.println("Очікувалося число, отримано: \"" + bad + "\"");
                System.out.println("Введіть число від " + i + " до " + j);
            }
        }
    }

}
