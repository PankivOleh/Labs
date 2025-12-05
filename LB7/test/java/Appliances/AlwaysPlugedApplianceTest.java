package Appliances;

import Utils.SysFunc;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class AlwaysPlugedApplianceTest {

    // Допоміжний метод для налаштування сканера
    // Це необхідно, бо батьківський конструктор ElectricalAppliance читає дані з SysFunc.SC
    private void setupScanner(String data) {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data.getBytes(StandardCharsets.UTF_8));
        try {
            Field field = SysFunc.class.getDeclaredField("SC");
            field.setAccessible(true);
            field.set(null, new Scanner(inputStream, StandardCharsets.UTF_8));
        } catch (Exception e) {
            throw new RuntimeException("Не вдалося налаштувати сканер", e);
        }
    }

    @Test
    void testConstructorAndToString() {
        // 1. ПІДГОТОВКА ДАНИХ
        // Формат вводу: Назва -> Бренд -> Модель -> Потужність -> (пустий рядок після числа) -> ID
        String input = "Router\nTP-Link\nArcher\n15\n\n777\n";

        setupScanner(input);

        // 2. СТВОРЕННЯ ОБ'ЄКТА (Покриває конструктор і super())
        AlwaysPlugedAppliance app = new AlwaysPlugedAppliance();

        // Перевіряємо, чи об'єкт створився коректно (перевірка батьківських полів)
        assertEquals("Router", app.getName());
        assertEquals("TP-Link", app.getBrand());
        assertEquals(777, app.getID());
        assertEquals(15.0, app.getPower());

        // 3. ПЕРЕВІРКА toString() (Покриває решту класу)
        // Вмикаємо прилад, щоб перевірити відображення ison=true (опціонально)
        app.turnOn();

        String result = app.toString();

        // Перевіряємо унікальний текст саме цього класу
        assertTrue(result.contains("Постійно підключений прилад"), "Має містити специфічну назву класу");

        // Перевіряємо, чи підставились дані
        assertTrue(result.contains("Router"));
        assertTrue(result.contains("TP-Link"));
        assertTrue(result.contains("Archer"));
        assertTrue(result.contains("777"));
        assertTrue(result.contains("true")); // ison
    }
}