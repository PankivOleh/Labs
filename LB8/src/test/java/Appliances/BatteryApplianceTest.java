package Appliances;

import Utils.SysFunc;
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

class BatteryApplianceTest {

    private final PrintStream originalOut = System.out;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    @BeforeEach
    void setUp() {
        // Перехоплюємо вивід для тестів консольних повідомлень
        System.setOut(new PrintStream(outContent, true, StandardCharsets.UTF_8));
    }

    @AfterEach
    void tearDown() {
        // Відновлюємо консоль
        System.setOut(originalOut);
    }

    // Метод для налаштування сканера для кількох об'єктів
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
    void testConstructorAndDefaults() {
        // Дані для BatteryAppliance: Name, Brand, Model, Power, ID
        String input = "Phone\nApple\niPhone\n20\n\n100\n";
        setupScanner(input);

        BatteryAppliance ba = new BatteryAppliance();

        assertNotNull(ba);
        assertEquals("Phone", ba.getName());
        assertEquals(100, ba.getID());
        assertFalse(ba.isCharging, "За замовчуванням не має заряджатися");
        assertNull(ba.charger, "За замовчуванням зарядка має бути null");
    }

    @Test
    void testSetCharger() {
        // Нам треба створити ДВА об'єкти: BatteryAppliance та Charger.
        // Тому готуємо ввід одразу для двох.
        String input =
                "Laptop\nDell\nXPS\n60\n\n200\n" + // BatteryAppliance
                        "Adapter\nDell\nCh1\n65\n\n201\n"; // Charger

        setupScanner(input);

        BatteryAppliance laptop = new BatteryAppliance();
        Charger adapter = new Charger();

        // Тестуємо прив'язку
        laptop.setCharger(adapter);

        // Перевіряємо, чи встановився зв'язок
        assertNotNull(laptop.charger);
        assertEquals(adapter, laptop.charger);

        // Перевіряємо зворотній зв'язок (якщо логіка Charger це підтримує)
        // У вашому коді BatteryAppliance викликає charger.setAppliance(this)
        // Тому перевіряємо це через рефлексію або геттер (якщо він є у Charger)
        // Або просто довіряємо, що помилки не вилетіло.
    }

    @Test
    void testChargingWithoutCharger() {
        String input = "Toy\nChina\nCar\n5\n\n300\n";
        setupScanner(input);

        BatteryAppliance toy = new BatteryAppliance();

        // Спроба зарядити без зарядки
        toy.Charging();

        // Перевіряємо повідомлення в консолі
        String output = outContent.toString(StandardCharsets.UTF_8);
        assertTrue(output.contains("Цей пристрій не має визначеної зарядки"));
        assertFalse(toy.isCharging);
    }

    @Test
    void testChargingWithCharger() {
        String input =
                "Drill\nBosch\nPro\n500\n\n400\n" + // BatteryAppliance
                        "Dock\nBosch\nSt\n500\n\n401\n";   // Charger

        setupScanner(input);

        BatteryAppliance drill = new BatteryAppliance();
        Charger dock = new Charger();

        // З'єднуємо їх
        drill.setCharger(dock);
        // Імітуємо, що зарядка ввімкнена в розетку (інакше вона може не заряджати, залежно від логіки Charger)
        dock.PlugIn();

        // Починаємо зарядку
        drill.Charging();

        assertTrue(drill.isCharging, "Має почати заряджатися");
    }

    @Test
    void testDischarging() {
        String input =
                "Watch\nSamsung\nGear\n2\n\n500\n" +
                        "Pad\nSamsung\nW1\n5\n\n501\n";

        setupScanner(input);

        BatteryAppliance watch = new BatteryAppliance();
        Charger pad = new Charger();

        watch.setCharger(pad);
        pad.PlugIn();

        // Спочатку вмикаємо
        watch.Charging();
        assertTrue(watch.isCharging);

        // Тепер вимикаємо (Discharging)
        watch.Discharging();
        assertFalse(watch.isCharging, "Має перестати заряджатися");
    }

    @Test
    void testSetChargingManual() {
        String input = "Test\nA\nB\n1\n\n999\n";
        setupScanner(input);
        BatteryAppliance ba = new BatteryAppliance();

        ba.setCharging(true);
        assertTrue(ba.isCharging);

        ba.setCharging(false);
        assertFalse(ba.isCharging);
    }

    @Test
    void testSetChargerReplace() {
        // Тест заміни старої зарядки на нову (покриває гілку if (this.charger != null))
        String input =
                "Phone\nA\nA\n1\n\n10\n" +
                        "Ch1\nA\nA\n1\n\n11\n" +
                        "Ch2\nA\nA\n1\n\n12\n";

        setupScanner(input);

        BatteryAppliance phone = new BatteryAppliance();
        Charger ch1 = new Charger();
        Charger ch2 = new Charger();

        phone.setCharger(ch1);
        assertEquals(ch1, phone.charger);

        // Замінюємо на ch2
        phone.setCharger(ch2);
        assertEquals(ch2, phone.charger);
    }

    @Test
    void testToString() {
        // УВАГА: Ваш метод toString викликає charger.getID().
        // Якщо charger == null, буде NullPointerException.
        // Тому для тесту ми ОБОВ'ЯЗКОВО створюємо і додаємо зарядку.

        String input =
                "Tablet\nXi\nPad5\n30\n\n600\n" + // Tablet (ID 600)
                        "Cable\nXi\nTypeC\n30\n\n601\n";  // Charger (ID 601)

        setupScanner(input);

        BatteryAppliance tablet = new BatteryAppliance();
        Charger cable = new Charger();

        tablet.setCharger(cable);
        tablet.Charging(); // Щоб isCharging стало true

        String result = tablet.toString();

        // Перевіряємо вміст
        assertTrue(result.contains("Акумуляторний прилад"), "Має бути назва класу");
        assertTrue(result.contains("Tablet"), "Має бути ім'я");
        assertTrue(result.contains("ID=600"), "Має бути ID приладу");
        assertTrue(result.contains("Зарядка=601"), "Має бути ID зарядки");
        assertTrue(result.contains("Заряджається=true"), "Має відображати статус зарядки");
    }
}