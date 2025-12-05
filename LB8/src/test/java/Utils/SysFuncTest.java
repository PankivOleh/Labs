package Utils;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class SysFuncTest {

    private final PrintStream originalOut = System.out;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    @BeforeEach
    void setUp() {
        // Перехоплюємо вивід консолі, щоб перевіряти повідомлення ("Хибний ввід" тощо)
        System.setOut(new PrintStream(outContent, true, StandardCharsets.UTF_8));
    }

    @AfterEach
    void tearDown() {
        // Відновлюємо стандартну консоль
        System.setOut(originalOut);
    }

    private void setupScanner(String data) {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data.getBytes(StandardCharsets.UTF_8));
        
        try {
            // Отримуємо доступ до приватного/статичного поля SC через Reflection
            Field field = SysFunc.class.getDeclaredField("SC");
            field.setAccessible(true);
            // Замінюємо старий сканер на новий
            field.set(null, new Scanner(inputStream, StandardCharsets.UTF_8));
        } catch (Exception e) {
            throw new RuntimeException("Не вдалося підмінити SysFunc.SC через Reflection", e);
        }
    }

    @Test
    void testGet_ValidInputImmediately() {
        // СЦЕНАРІЙ: Користувач одразу вводить правильне число "5"
        // Діапазон: 1 - 10
        setupScanner("5\n");

        int result = SysFunc.get(1, 10);

        // Перевіряємо, що метод повернув 5
        assertEquals(5, result);

        // Перевіряємо, що було виведено запрошення
        String output = outContent.toString(StandardCharsets.UTF_8);
        assertTrue(output.contains("Введіть число від 1 до 10"));
    }

    @Test
    void testGet_OutOfRangeValues() {
        // СЦЕНАРІЙ:
        // 1. Вводить 0 (замало) -> має вивести помилку
        // 2. Вводить 11 (забагато) -> має вивести помилку
        // 3. Вводить 5 (ок) -> метод завершує роботу
        setupScanner("0\n11\n5\n");

        int result = SysFunc.get(1, 10);

        // У підсумку має повернути 5
        assertEquals(5, result);

        String output = outContent.toString(StandardCharsets.UTF_8);
        // Перевіряємо наявність повідомлення про помилку
        assertTrue(output.contains("Хибний ввід"));
    }

    @Test
    void testGet_InvalidType_TextInsteadOfNumber() {
        // СЦЕНАРІЙ:
        // 1. Вводить "привіт" (текст) -> має обробити виключення сканера
        // 2. Вводить "7" (ок) -> завершення
        setupScanner("привіт\n7\n");

        int result = SysFunc.get(1, 10);

        assertEquals(7, result);

        String output = outContent.toString(StandardCharsets.UTF_8);
        // Перевіряємо, що програма повідомила про некоректний токен
        assertTrue(output.contains("Очікувалося число, отримано: \"привіт\""));
    }

    @Test
    void testGet_BoundaryValues() {
        // Перевірка граничних значень (1 і 10 включно)
        
        // Тест на мінімум
        setupScanner("1\n");
        assertEquals(1, SysFunc.get(1, 10));

        // Тест на максимум
        setupScanner("10\n");
        assertEquals(10, SysFunc.get(1, 10));
    }
}