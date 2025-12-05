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

class PluggedApplianceTest {

    private final PrintStream originalOut = System.out;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    @BeforeEach
    void setUp() {
        // Перехоплюємо System.out для перевірки повідомлень про помилки
        System.setOut(new PrintStream(outContent, true, StandardCharsets.UTF_8));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    // Хак для підміни сканера (бо конструктор батьківського класу читає з консолі)
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
        // Input: Name -> Brand -> Model -> Power -> (newline) -> ID
        String input = "Iron\nPhillips\nGC\n2000\n\n100\n";
        setupScanner(input);

        PluggedAppliance app = new PluggedAppliance();

        // Перевіряємо, що дані записались
        assertEquals("Iron", app.getName());
        assertEquals(2000.0, app.getPower());
        assertEquals(100, app.getID());

        // Перевіряємо дефолтний стан
        assertFalse(app.isPlugged(), "За замовчуванням має бути не в розетці");
        assertFalse(app.isOn(), "За замовчуванням має бути вимкнено");
    }

    @Test
    void testPlugInAndTurnOnSuccess() {
        String input = "TV\nLG\nOLED\n100\n\n101\n";
        setupScanner(input);
        PluggedAppliance app = new PluggedAppliance();

        // 1. Вмикаємо в розетку
        app.PlugIn();
        assertTrue(app.isPlugged(), "Має бути isPlugged = true");

        // 2. Вмикаємо прилад
        app.turnOn();
        assertTrue(app.isOn(), "Має бути isOn = true, бо прилад в розетці");
    }

    @Test
    void testTurnOnFailWhenNotPlugged() {
        String input = "Mixer\nBosch\nX\n300\n\n102\n";
        setupScanner(input);
        PluggedAppliance app = new PluggedAppliance();

        // Не викликаємо PlugIn(), тобто isPlugged = false

        // Спроба ввімкнути
        app.turnOn();

        // Перевіряємо, що не ввімкнувся
        assertFalse(app.isOn(), "Не повинен вмикатися без розетки");

        // Перевіряємо повідомлення про помилку
        String output = outContent.toString(StandardCharsets.UTF_8);
        assertTrue(output.contains("Пристрій не ввімкнено в розетку"), "Має вивести повідомлення про помилку");
    }

    @Test
    void testPlugOutTurnsOffDevice() {
        String input = "Heater\nUFO\nEco\n1500\n\n103\n";
        setupScanner(input);
        PluggedAppliance app = new PluggedAppliance();

        // Підготовка: вмикаємо все
        app.PlugIn();
        app.turnOn();
        assertTrue(app.isOn());

        // Дія: Витягаємо з розетки
        app.PlugOut();

        // Перевірка:
        assertFalse(app.isPlugged(), "Має бути не в розетці");
        assertFalse(app.isOn(), "PlugOut має автоматично вимикати прилад (super.turnOff)");
    }

    @Test
    void testTurnOffManual() {
        String input = "Lamp\nDesk\nA1\n10\n\n104\n";
        setupScanner(input);
        PluggedAppliance app = new PluggedAppliance();

        app.PlugIn();
        app.turnOn();
        assertTrue(app.isOn());

        // Ручне вимкнення кнопкою
        app.turnOff();

        assertFalse(app.isOn(), "Має вимкнутися");
        assertTrue(app.isPlugged(), "Має залишитися в розетці");
    }

    @Test
    void testToString() {
        String input = "Toaster\nTefal\nBread\n800\n\n105\n";
        setupScanner(input);
        PluggedAppliance app = new PluggedAppliance();

        app.PlugIn(); // isplugged = true

        String result = app.toString();

        // Перевіряємо поля
        assertTrue(result.contains("Прилад на розетку{"));
        assertTrue(result.contains("Toaster"));
        assertTrue(result.contains("ID=105"));
        assertTrue(result.contains("В розетці=true"));
        assertTrue(result.contains("Ввімкнено=false")); // Ми не робили turnOn
    }
}