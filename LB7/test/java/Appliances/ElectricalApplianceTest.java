package Appliances;


import Utils.SysFunc;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class ElectricalApplianceTest {

    private final InputStream originalIn = System.in;

    // 1. Створюємо конкретну реалізацію абстрактного класу для тестів
    static class TestAppliance extends ElectricalAppliance {
        // Конструктор просто викликає батьківський (який читає з консолі)
        public TestAppliance() {
            super();
        }
    }

    // Метод для налаштування сканера (той самий хак, що ми використовували раніше)
    private void setupScanner(String data) {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data.getBytes(StandardCharsets.UTF_8));
        System.setIn(inputStream);

        try {
            Field field = SysFunc.class.getDeclaredField("SC");
            field.setAccessible(true);
            // Важливо використовувати той самий потік і UTF-8
            field.set(null, new Scanner(inputStream, StandardCharsets.UTF_8));
        } catch (Exception e) {
            throw new RuntimeException("Не вдалося підмінити сканер", e);
        }
    }

    @AfterEach
    void tearDown() {
        System.setIn(originalIn);
    }

    @Test
    void testConstructorAndGetters() {
        // Підготовка даних для вводу:
        // Ім'я -> Бренд -> Модель -> Потужність -> (пустий рядок для очистки буфера) -> ID
        // Використовуємо ціле число 1500 для потужності, щоб уникнути проблем з комою/крапкою
        String input = "MyToaster\nPhilips\nSuperModel\n1500\n\n12345\n";

        setupScanner(input);

        // Викликаємо конструктор (він прочитає дані з нашого рядка)
        ElectricalAppliance appliance = new TestAppliance();

        // Перевіряємо, чи правильно записалися дані
        assertEquals("MyToaster", appliance.getName());
        assertEquals("Philips", appliance.getBrand());
        assertEquals("SuperModel", appliance.getModel());
        assertEquals(1500.0, appliance.getPower());
        assertEquals(12345, appliance.getID());

        // Перевіряємо початковий стан (має бути вимкнений)
        assertFalse(appliance.isOn());
    }

    @Test
    void testStateChange() {
        // Мінімальні дані для створення
        String input = "Fan\nX\nY\n50\n\n111\n";
        setupScanner(input);

        ElectricalAppliance appliance = new TestAppliance();

        // Тестуємо turnOn
        appliance.turnOn();
        assertTrue(appliance.isOn(), "Прилад має бути ввімкнений");

        // Тестуємо turnOff
        appliance.turnOff();
        assertFalse(appliance.isOn(), "Прилад має бути вимкнений");
    }

    @Test
    void testSetID() {
        String input = "Iron\nA\nB\n2000\n\n10\n";
        setupScanner(input);

        ElectricalAppliance appliance = new TestAppliance();

        assertEquals(10, appliance.getID());

        // Тестуємо setter
        appliance.setID(999);
        assertEquals(999, appliance.getID(), "ID має змінитись на 999");
    }

    @Test
    void testToString() {
        String input = "Mixer\nBosch\nTurbo\n300\n\n777\n";
        setupScanner(input);

        ElectricalAppliance appliance = new TestAppliance();

        // Вмикаємо, щоб перевірити відображення "ison=true"
        appliance.turnOn();

        String result = appliance.toString();

        // Перевіряємо наявність усіх ключових полів у рядку
        assertTrue(result.contains("Електроприлад"));
        assertTrue(result.contains("Mixer"));
        assertTrue(result.contains("Bosch"));
        assertTrue(result.contains("Turbo"));
        assertTrue(result.contains("777"));
        assertTrue(result.contains("true")); // перевірка ison

        // Перевіряємо потужність (як частину рядка, наприклад "300")
        assertTrue(result.contains("300"));
    }
}