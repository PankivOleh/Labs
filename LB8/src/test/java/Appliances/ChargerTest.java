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

class ChargerTest {

    private final PrintStream originalOut = System.out;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    @BeforeEach
    void setUp() {
        // Перехоплюємо вивід для перевірки повідомлень про помилки
        System.setOut(new PrintStream(outContent, true, StandardCharsets.UTF_8));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    // Метод для налаштування сканера
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
    void testChargingSuccess() {
        // Дані для Charger (ID 10) і BatteryAppliance (ID 20)
        String input =
                "MyCharger\nBrandA\nModA\n50\n\n10\n" +
                        "MyPhone\nBrandB\nModB\n20\n\n20\n";

        setupScanner(input);

        Charger charger = new Charger();
        BatteryAppliance phone = new BatteryAppliance();

        // Налаштовуємо успішний сценарій
        charger.PlugIn();          // Вмикаємо в розетку
        charger.setAppliance(phone); // Під'єднуємо телефон

        // Викликаємо Charging
        charger.Charging();

        // Перевіряємо стани
        assertTrue(charger.isCharging, "Charger: isCharging має бути true");
        assertTrue(charger.isOn(), "Charger: isOn має бути true");
        assertTrue(phone.isCharging, "Phone: isCharging має бути true (через виклик phone.setCharging)");
    }

    @Test
    void testChargingFailNotPlugged() {
        // Тільки Charger (ID 10) і Phone (ID 20)
        String input =
                "Ch\nA\nB\n1\n\n10\n" +
                        "Ph\nA\nB\n1\n\n20\n";
        setupScanner(input);

        Charger charger = new Charger();
        BatteryAppliance phone = new BatteryAppliance();

        charger.setAppliance(phone);
        // НЕ вмикаємо в розетку (PlugIn не викликаємо)

        charger.Charging();

        // Перевіряємо, що зарядка не пішла
        assertFalse(charger.isCharging);

        // Перевіряємо повідомлення про помилку
        String output = outContent.toString(StandardCharsets.UTF_8);
        assertTrue(output.contains("не ввімкнений в розетку"), "Має вивести помилку");
    }

    @Test
    void testChargingFailNoAppliance() {
        // Тільки Charger
        String input = "Ch\nA\nB\n1\n\n10\n";
        setupScanner(input);

        Charger charger = new Charger();
        charger.PlugIn();
        // НЕ під'єднуємо телефон (appliance == null)

        charger.Charging();

        assertFalse(charger.isCharging);
        String output = outContent.toString(StandardCharsets.UTF_8);
        assertTrue(output.contains("не під'єднано"), "Має вивести помилку про відсутність пристрою");
    }

    @Test
    void testStopCharging() {
        String input =
                "Ch\nA\nB\n1\n\n10\n" +
                        "Ph\nA\nB\n1\n\n20\n";
        setupScanner(input);

        Charger charger = new Charger();
        BatteryAppliance phone = new BatteryAppliance();

        // Запускаємо
        charger.PlugIn();
        charger.setAppliance(phone);
        charger.Charging();
        assertTrue(charger.isCharging);

        // Зупиняємо
        charger.StopCharging();

        // Перевіряємо скидання станів
        assertFalse(charger.isCharging, "Charger має зупинитись");
        assertFalse(charger.isOn(), "Charger має вимкнутись");
        // Перевіряємо, чи передалась команда на телефон
        // (Оскільки phone.setCharging викликається всередині, перевіряємо стан phone)
        // Примітка: у вашому коді BatteryAppliance поле isCharging package-private,
        // тому доступ до нього можливий, якщо тест у тому ж пакеті.
        // Якщо ні - додайте геттер isCharging() у BatteryAppliance.
        // Або перевіримо непрямо через toString() чи поведінку.
    }

    @Test
    void testTurnOnOverride() {
        // turnOn викликає super.turnOn() + this.Charging()
        String input =
                "Ch\nA\nB\n1\n\n10\n" +
                        "Ph\nA\nB\n1\n\n20\n";
        setupScanner(input);

        Charger charger = new Charger();
        BatteryAppliance phone = new BatteryAppliance();

        charger.PlugIn();
        charger.setAppliance(phone);

        // Викликаємо загальний метод turnOn
        charger.turnOn();

        assertTrue(charger.isCharging, "turnOn має запустити Charging()");
        assertTrue(charger.isOn());
    }

    @Test
    void testTurnOffOverride() {
        // turnOff викликає super.turnOff() + StopCharging()
        String input =
                "Ch\nA\nB\n1\n\n10\n" +
                        "Ph\nA\nB\n1\n\n20\n";
        setupScanner(input);

        Charger charger = new Charger();
        BatteryAppliance phone = new BatteryAppliance();

        charger.PlugIn();
        charger.setAppliance(phone);
        charger.Charging(); // Спочатку вмикаємо

        // Вимикаємо
        charger.turnOff();

        assertFalse(charger.isCharging, "turnOff має зупинити Charging");
        assertFalse(charger.isOn());
    }

    @Test
    void testToString() {
        // УВАГА: toString викликає appliance.getID(), тому appliance не може бути null
        String input =
                "SuperCharger\nAnker\nPowerPort\n65\n\n500\n" +
                        "Tablet\nSamsung\nTab\n20\n\n600\n";
        setupScanner(input);

        Charger charger = new Charger();
        BatteryAppliance tablet = new BatteryAppliance();

        charger.setAppliance(tablet);
        charger.PlugIn();
        charger.Charging(); // Щоб отримати isCharging = true у виводі

        String result = charger.toString();

        // Перевіряємо ключові поля
        assertTrue(result.contains("Зарядка{"));
        assertTrue(result.contains("SuperCharger")); // Ім'я
        assertTrue(result.contains("ID=500"));       // ID зарядки
        assertTrue(result.contains("Прилад=600"));   // ID приладу
        assertTrue(result.contains("Заряджає=true"));
        assertTrue(result.contains("Ввімкнено в розетку=true"));
    }
}