package Home;

import Appliances.*;
import Utils.SysFunc;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class HomeTest {

    private Home home;
    private final InputStream originalSystemIn = System.in;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final InputStream originalIn = System.in;

    @BeforeEach
    void setUp() {
        // Ми не створюємо тут прилади вручну через new, щоб не зависнути.
        // Просто готуємо чистий дім і кімнати.
        home = new Home();
        home.initTransient(); // Ініціалізуємо логер і сканер Home

        Room testRoom1 = new Room("Кухня", 3);
        home.addRoom(testRoom1);
    }

    @AfterEach
    void tearDown() {
        System.setIn(originalSystemIn);
        // Повертаємо оригінальний сканер на місце
        SysFunc.SC = new Scanner(originalSystemIn);
        System.setOut(originalOut);
        System.setIn(originalIn);
    }

    /**
     * Цей метод закидає дані в System.in і оновлює статичний сканер.
     * Викликай його ПЕРЕД викликом методів Home або конструкторів приладів.
     */
    private void provideInput(String data) {
        ByteArrayInputStream testIn = new ByteArrayInputStream(data.getBytes());
        System.setIn(testIn);
        // ОНОВЛЮЄМО статичний сканер, щоб він побачив нові дані
        SysFunc.SC = new Scanner(System.in);
        // Оновлюємо сканер всередині об'єкта home
        home.initTransient();
    }
    // Цей метод оновлює приватний сканер всередині об'єкта Home
    private void updateHomeScanner(Home homeInstance, java.io.InputStream stream) {
        try {
            java.lang.reflect.Field scField = Home.class.getDeclaredField("sc");
            scField.setAccessible(true);
            // Створюємо новий сканер для цього потоку і пхаємо його в home
            scField.set(homeInstance, new java.util.Scanner(stream, java.nio.charset.StandardCharsets.UTF_8));
        } catch (Exception e) {
            throw new RuntimeException("Не вдалося оновити сканер у Home", e);
        }
    }
    private void resetSysFuncScanner(java.io.InputStream stream) {
        try {
            // Отримуємо доступ до поля SC
            Field field = SysFunc.class.getDeclaredField("SC");
            field.setAccessible(true);

            // Замінюємо об'єкт сканера на новий, прив'язаний до нашого stream
            field.set(null, new Scanner(stream));

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Не вдалося оновити статичний Scanner у SysFunc. Тест впав.");
        }
    }

    private ElectricalAppliance createAppliance(String name, int id) {
        // Дані: Ім'я -> Бренд -> Модель -> Потужність -> ID
        // Важливо: ID передається в SysFunc.get(), який юзає статичний сканер!
        String input = name + "\nTestBrand\nTestModel\n100\n" + id + "\n";

        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);

        // !!! ОСЬ ТУТ КЛЮЧОВИЙ МОМЕНТ !!!
        // Ми кажемо SysFunc: "Дивись сюди, потік вводу змінився!"
        resetSysFuncScanner(inputStream);

        return new PluggedAppliance();
    }

    @Test
    void testConfigRoom() {
        // --- 1. ПІДГОТОВКА СТАНУ ---
        Room room = new Room("OldName", 3);

        // Готуємо прилади
        String appCreationInput = "Tv\nB\nM\n100\n\n10\n" +
                "Pc\nB\nM\n200\n\n11\n" +
                "Lamp\nB\nM\n50\n\n12\n";
        ByteArrayInputStream createStream = new ByteArrayInputStream(appCreationInput.getBytes(java.nio.charset.StandardCharsets.UTF_8));

        // Оновлюємо SysFunc (для створення об'єктів ElectricalAppliance, які юзають SysFunc.get)
        resetSysFuncScanner(createStream);

        PluggedAppliance p1 = new PluggedAppliance(); p1.PlugIn(); p1.setID(10);
        PluggedAppliance p2 = new PluggedAppliance(); p2.PlugIn(); p2.setID(11);
        PluggedAppliance p3 = new PluggedAppliance(); p3.PlugIn(); p3.setID(12);

        room.getAppliances().add(p1); room.getPluggedAppliances().add(p1); room.getAppliancesOn().add(p1);
        room.getAppliances().add(p2); room.getPluggedAppliances().add(p2); room.getAppliancesOn().add(p2);
        room.getAppliances().add(p3); room.getPluggedAppliances().add(p3); room.getAppliancesOn().add(p3);

        home.getRooms().clear();
        home.addRoom(room);

        // --- 2. ВВІД ДЛЯ ConfigRoom ---
        String menuInput =
                "1\n" +             // Вибір кімнати
                        "1\n" +             // Змінити ім'я? (Так)
                        "NewLuxuryRoom\n" + // Нове ім'я
                        "1\n" +             // Змінити розетки? (Так)
                        "1\n";              // Кількість розеток -> 1

        ByteArrayInputStream menuStream = new ByteArrayInputStream(menuInput.getBytes(java.nio.charset.StandardCharsets.UTF_8));

        resetSysFuncScanner(menuStream);



        Scanner sharedScanner = new Scanner(menuStream, java.nio.charset.StandardCharsets.UTF_8);

        try {
            // Встановлюємо цей сканер в SysFunc
            java.lang.reflect.Field sysField = SysFunc.class.getDeclaredField("SC");
            sysField.setAccessible(true);
            sysField.set(null, sharedScanner);

            // Встановлюємо ЦЕЙ ЖЕ сканер в Home
            java.lang.reflect.Field homeField = Home.class.getDeclaredField("sc");
            homeField.setAccessible(true);
            homeField.set(home, sharedScanner);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        ByteArrayOutputStream localOut = new ByteArrayOutputStream();
        PrintStream captureStream = new PrintStream(localOut, true, java.nio.charset.StandardCharsets.UTF_8);
        PrintStream oldOut = java.lang.System.out;
        java.lang.System.setOut(captureStream);

        try {
            home.ConfigRoom();
        } finally {
            java.lang.System.setOut(oldOut);
        }

        String output = localOut.toString(java.nio.charset.StandardCharsets.UTF_8);

        assertTrue(output.contains("було видалено 2"), "Має бути повідомлення про видалення");

        Room modifiedRoom = home.getRooms().get(0);
        org.junit.jupiter.api.Assertions.assertEquals("NewLuxuryRoom", modifiedRoom.getName());
        org.junit.jupiter.api.Assertions.assertEquals(1, modifiedRoom.getCountOfSocket());
    }
    @Test
    void testShoweAllAppliancesOn() throws Exception {
        // 1. ПІДГОТОВКА ДАНИХ (Input)
        // Додаємо зайві переноси рядків (\n), щоб сканер точно не застряг
        String inputData = "Televizor\nSamsung\nQLED\n200\n\n777\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputData.getBytes(java.nio.charset.StandardCharsets.UTF_8));

        // Встановлюємо ввід
        java.lang.System.setIn(inputStream);
        resetSysFuncScanner(inputStream);

        // 2. СТВОРЕННЯ ОБ'ЄКТІВ (поки що вивід йде в звичайну консоль)
        PluggedAppliance tv = new PluggedAppliance();
        tv.PlugIn();
        tv.turnOn();
        home.allAppliancesOn.add(tv);

        // 3. НАЛАШТУВАННЯ ПЕРЕХОПЛЕННЯ ВИВОДУ (Output)
        ByteArrayOutputStream localOut = new ByteArrayOutputStream();
        PrintStream captureStream = new PrintStream(localOut, true, java.nio.charset.StandardCharsets.UTF_8);

        // Зберігаємо старий потік, щоб потім відновити
        PrintStream oldOut = java.lang.System.out;
        // Перемикаємо потік
        java.lang.System.setOut(captureStream);

        try {
            // 4. ВИКОНАННЯ МЕТОДУ
            home.ShoweAllAppliancesOn();
        } finally {
            java.lang.System.setOut(oldOut);
        }

        // 5. ОТРИМАННЯ РЕЗУЛЬТАТУ
        String output = localOut.toString(java.nio.charset.StandardCharsets.UTF_8);


        // 6. ПЕРЕВІРКИ
        assertTrue(output.length() > 0, "Вивід методу порожній! System.out не перехопився.");

        // Перевіряємо ключові дані
        assertTrue(output.contains("777"), "Не знайдено ID=777");
        assertTrue(output.contains("Televizor"), "Не знайдено Ім'я");
        assertTrue(output.contains("Samsung"), "Не знайдено Бренд");
        assertTrue(output.contains("true"), "Не знайдено статус true");
    }

    @Test
    void testShowHome() {
        // 1. ПІДГОТОВКА ДАНИХ
        // Створюємо кімнату вручну (тут сканер не потрібен)
        Room kitchen = new Room("Кухня", 5);
        home.addRoom(kitchen);

        // 2. НАЛАШТУВАННЯ ПЕРЕХОПЛЕННЯ (Локально)
        ByteArrayOutputStream localOut = new ByteArrayOutputStream();
        // Вказуємо UTF-8
        PrintStream captureStream = new PrintStream(localOut, true, java.nio.charset.StandardCharsets.UTF_8);

        PrintStream oldOut = java.lang.System.out;
        java.lang.System.setOut(captureStream);

        try {
            // 3. ВИКОНАННЯ МЕТОДУ
            home.ShowHome();
        } finally {
            // 4. ВІДНОВЛЕННЯ КОНСОЛІ
            java.lang.System.setOut(oldOut);
        }

        // 5. ОТРИМАННЯ РЕЗУЛЬТАТУ
        String output = localOut.toString(java.nio.charset.StandardCharsets.UTF_8);

        // 6. ПЕРЕВІРКИ
        // Перевіряємо, чи вивід не порожній
        assertTrue(output.length() > 0, "Вивід методу ShowHome порожній!");

        assertTrue(output.contains("Кімната{"), "Не знайдено початок об'єкта 'Кімната{'");
        assertTrue(output.contains("Кухня"), "Не знайдено назву кімнати 'Кухня'");
        assertTrue(output.contains("Кількість розеток=5"), "Не знайдено кількість розеток");
    }

    @Test
    void testShowAllAppliancesInRoom() {
        // --- ЕТАП 1: Створення приладу
        String appInput = "Fridge\nLG\nSmart\n300\n\n555\n"; // Додав \n між 300 і 555 для надійності
        ByteArrayInputStream appStream = new ByteArrayInputStream(appInput.getBytes(java.nio.charset.StandardCharsets.UTF_8));

        java.lang.System.setIn(appStream);
        resetSysFuncScanner(appStream);

        PluggedAppliance fridge = new PluggedAppliance();
        fridge.PlugIn();

        // --- ЕТАП 2: Створення кімнати і додавання в Дім ---
        Room kitchen = new Room("Kitchen", 2);
        kitchen.getAppliances().add(fridge);
        kitchen.getPluggedAppliances().add(fridge);

        // Очищаємо список кімнат, щоб наша Kitchen точно була під номером 1
        home.getRooms().clear();
        home.addRoom(kitchen);

        // --- ЕТАП 3: Підготовка вводу для Меню (Вибір кімнати "1") ---
        String menuInput = "1\n";
        ByteArrayInputStream menuStream = new ByteArrayInputStream(menuInput.getBytes(java.nio.charset.StandardCharsets.UTF_8));

        java.lang.System.setIn(menuStream);
        resetSysFuncScanner(menuStream); // Оновлюємо сканер для меню!

        // --- ЕТАП 4: Перехоплення виводу (Локально) ---
        ByteArrayOutputStream localOut = new ByteArrayOutputStream();
        PrintStream captureStream = new PrintStream(localOut, true, java.nio.charset.StandardCharsets.UTF_8);

        PrintStream oldOut = java.lang.System.out;
        java.lang.System.setOut(captureStream);

        try {
            // ВИКОНАННЯ
            home.ShowAllAppliancesInRoom();
        } finally {
            // ВІДНОВЛЕННЯ
            java.lang.System.setOut(oldOut);
        }

        // --- ЕТАП 5: Перевірка ---
        String output = localOut.toString(java.nio.charset.StandardCharsets.UTF_8);

        // ДЕБАГ (якщо тест впаде, розкоментуйте)
        // java.lang.System.err.println(output);

        assertTrue(output.length() > 0, "Вивід методу порожній!");
        assertTrue(output.contains("Оберіть кімнату"), "Немає запиту на вибір кімнати");
        assertTrue(output.contains("Kitchen"), "Не відображається назва кімнати у меню");

        // Перевіряємо, чи показало прилад всередині обраної кімнати
        assertTrue(output.contains("Fridge"), "Не знайдено прилад Fridge");
        assertTrue(output.contains("ID=555"), "Не знайдено ID приладу");
    }

    @Test
    void configAppInRoom_AddPluggedAppliance() {
        // Дані для методу ConfigAppInRoom:
        // 1 (Додати) -> 1 (Тип: Провідний) -> 1 (Кімната: Кухня)
        // ДАЛІ йдуть дані для конструктора PluggedAppliance:
        // Назва -> Бренд -> Модель -> Потужність -> ID
        String input = "1\n1\n1\nЧайник\nTefal\nK2000\n2000\n10\n";

        provideInput(input);
        home.ConfigAppInRoom();

        assertEquals(1, home.getRooms().get(0).getAppliances().size());
        assertEquals("Чайник", home.getRooms().get(0).getAppliances().get(0).getName());
    }

    @Test
    void configAppInRoom_DeleteAppliance() {
        // Щоб видалити прилад, його спочатку треба створити.
        // Але конструктор вимагає вводу. Тому ми спочатку готуємо ввід для КОНСТРУКТОРА.

        String constructorInput = "Тостер\nBosch\nM1\n500\n99\n";
        provideInput(constructorInput);

        // Тепер це безпечно, бо дані вже в буфері:
        PluggedAppliance toaster = new PluggedAppliance();

        // Додаємо створений тостер в дім вручну
        home.addRoom(new Room("Спальня", 2)); // про всяк випадок
        home.getRooms().get(0).getAppliances().add(toaster);
        home.allAppliances.add(toaster);

        // Тепер готуємо ввід для самого методу видалення:
        // 2 (Видалити) -> 1 (Кімната) -> 99 (ID тостера)
        String deleteInput = "2\n1\n99\n";
        provideInput(deleteInput);

        home.ConfigAppInRoom();

        assertTrue(home.getRooms().get(0).getAppliances().isEmpty(), "Прилад мав видалитися");
    }

    @Test
    void setAppliances_TurnOn() {
        // 1. Створюємо прилад (готуємо ввід для конструктора)
        String constrInput = "Лампа\nPhil\nL1\n60\n55\n";
        provideInput(constrInput);
        PluggedAppliance lamp = new PluggedAppliance();

        // Налаштовуємо його стан для тесту
        lamp.PlugIn();
        home.getRooms().get(0).getAppliances().add(lamp);
        home.allAppliances.add(lamp);

        // 2. Готуємо ввід для методу SetAppliances:
        // 1 (Увімкнути) -> 55 (ID лампи)
        String methodInput = "1\n55\n";
        provideInput(methodInput);

        home.SetAppliances();

        assertTrue(lamp.isOn());
        assertEquals(1, home.allAppliancesOn.size());
    }

    @Test
    void calculateGeneralPower() {
        // Створюємо два прилади. Нам треба дві порції даних для конструкторів.
        String inputApp1 = "P1\nB1\nM1\n100\n101\n";
        String inputApp2 = "P2\nB2\nM2\n200\n102\n";

        // Об'єднуємо ввід, бо ми будемо створювати їх підряд
        provideInput(inputApp1 + inputApp2);

        PluggedAppliance p1 = new PluggedAppliance();
        PluggedAppliance p2 = new PluggedAppliance();

        p1.turnOn();
        p2.turnOn();

        home.allAppliancesOn.add(p1);
        home.allAppliancesOn.add(p2);

        // Метод CalculateGeneralPower просто виводить дані, вводу не просить,
        // але provideInput скидає System.in, тому краще передати порожній рядок або щось нейтральне
        provideInput("\n");

        double power = home.CalculateGeneralPower();
        assertEquals(300.0, power);
    }

    @Test
    void findAppliancesByParams() {
        // Створюємо прилад: ID 77, Power 1000, ввімкнений
        String appInput = "TestApp\nBr\nMd\n1000\n77\n";
        provideInput(appInput);
        PluggedAppliance app = new PluggedAppliance();
        app.turnOn();
        app.PlugIn(); // Важливо, бо ми будемо шукати "провідний" (Plugged)
        home.allAppliances.add(app);

        // Ввід для пошуку:
        // 2 (Не батарея -> значить провідний)
        // 1 (Ввімкнений)
        // 2000 (Max power)
        // 500 (Min power)
        String searchInput = "2\n1\n2000\n500\n";
        provideInput(searchInput);

        assertDoesNotThrow(() -> home.FindAppliancesByParams());
    }

    @Test
    void configHome_AddRoom() {
        // Тут конструктори приладів не викликаються, лише прості сканери
        // 1 (Додати кімнату) -> "Зал" (Назва) -> 5 (Розетки)
        provideInput("1\nЗал\n5\n");

        home.ConfigHome();

        assertEquals(2, home.getRooms().size()); // 1 була в setUp + 1 додали
        assertEquals("Зал", home.getRooms().get(1).getName());
    }

    @Test
    void checkUniqueIdLogic() {
        // Створюємо перший прилад
        provideInput("A1\nB1\nM1\n10\n100\n");
        PluggedAppliance a1 = new PluggedAppliance();
        home.allAppliances.add(a1);

        provideInput("\n");

        int result = home.checkAppliancesID(100);
        assertEquals(100, result, "Має повернути ID, якщо він зайнятий");

        int resultFree = home.checkAppliancesID(999);
        assertEquals(0, resultFree, "Має повернути 0, якщо ID вільний");
    }
    @Test
    void configAppInRoom_AddBatteryAppliance_CreateNewCharger() {

        String menuInput = "1\n2\n1\n";


        String batteryAppInput = "Phone\nApple\niPhone\n20\n200\n";

        String logicInput = "1\n1\n";

        String chargerInput = "Charger\nApple\n20W\n15\n201\n";

        provideInput(menuInput + batteryAppInput + logicInput + chargerInput);

        home.ConfigAppInRoom();

        Room room = home.getRooms().get(0);
        // Має бути 2 прилади: Телефон і Зарядка
        assertEquals(2, room.getAppliances().size());

        // Перевіряємо типи
        boolean hasBattery = room.getAppliances().stream().anyMatch(a -> a instanceof BatteryAppliance);
        boolean hasCharger = room.getAppliances().stream().anyMatch(a -> a instanceof Charger);
        assertTrue(hasBattery);
        assertTrue(hasCharger);
    }

    @Test
    void configRoom_DecreaseSockets_ShouldRemovePluggedDevice() {
        // Підготовка: Кімната має 3 розетки. Ми додамо туди 2 підключені прилади.
        Room room = home.getRooms().get(0); // Кухня, 3 розетки

        // Створюємо прилади вручну, щоб не гратися зі сканером
        // (використовуємо provideInput для конструкторів, щоб не зависло)
        provideInput("A1\nB\nM\n100\n301\nA2\nB\nM\n100\n302\n");
        PluggedAppliance p1 = new PluggedAppliance();
        PluggedAppliance p2 = new PluggedAppliance();

        p1.PlugIn();
        p2.PlugIn();

        room.getAppliances().add(p1);
        room.getAppliances().add(p2);
        room.getPluggedAppliances().add(p1);
        room.getPluggedAppliances().add(p2);

        // Зараз 2 підключені. Змінюємо кількість розеток на 1.
        // Меню ConfigRoom:
        // 1 (Вибрати кімнату 1)
        // 2 (Змінити ім'я? Ні)
        // 1 (Змінити розетки? Так)
        // 1 (Нова кількість)
        provideInput("1\n2\n1\n1\n");

        home.ConfigRoom();

        // Перевірка: має залишитися тільки 1 підключений пристрій
        assertEquals(1, room.getPluggedAppliances().size());
        assertEquals(1, room.getCountOfSocket());
    }

    @Test
    void configHome_DeleteRoom_WithAppliances() {
        // Підготовка: додаємо прилад у кімнату
        provideInput("TV\nLG\nOLED\n100\n400\n");
        PluggedAppliance tv = new PluggedAppliance();
        home.getRooms().get(0).getAppliances().add(tv);
        home.allAppliances.add(tv);

        // Меню ConfigHome:
        // 2 (Видалити кімнату)
        // 1 (Номер кімнати)
        provideInput("2\n1\n");

        home.ConfigHome();

        // Перевірки
        assertTrue(home.getRooms().isEmpty(), "Кімната має бути видалена");
        assertTrue(home.allAppliances.isEmpty(), "Прилади з видаленої кімнати мають зникнути з загального списку");
    }

    @Test
    void setAppliances_TurnOff() {
        // Підготовка: Увімкнений прилад
        provideInput("Fan\nXiao\nF1\n50\n500\n");
        PluggedAppliance fan = new PluggedAppliance();
        fan.PlugIn();
        fan.turnOn();

        home.getRooms().get(0).getAppliances().add(fan);
        home.getRooms().get(0).getAppliancesOn().add(fan);
        home.allAppliancesOn.add(fan);

        // Меню SetAppliances:
        // 2 (Вимкнути пристрій)
        // 1 (Кімната 1)
        // 500 (ID)
        provideInput("2\n1\n500\n");

        home.SetAppliances();

        assertFalse(fan.isOn());
        assertTrue(home.allAppliancesOn.isEmpty());
    }

    @Test
    void setAppliances_Unplug() {
        // Підготовка: Підключений (в розетку) прилад
        provideInput("Iron\nTe\nI1\n2000\n600\n");
        PluggedAppliance iron = new PluggedAppliance();
        iron.PlugIn();
        iron.turnOn(); // Також увімкнений

        Room room = home.getRooms().get(0);
        room.getAppliances().add(iron);
        room.getPluggedAppliances().add(iron);
        room.getAppliancesOn().add(iron);
        home.allAppliancesOn.add(iron);

        // Меню SetAppliances:
        // 3 (Вимкнути з розетки)
        // 1 (Кімната 1)
        // 600 (ID)
        provideInput("3\n1\n600\n");

        home.SetAppliances();

        assertFalse(iron.isPlugged());
        assertFalse(iron.isOn()); // Має вимкнутись автоматично
        assertFalse(room.getPluggedAppliances().contains(iron));
    }

    @Test
    void setAppliances_PlugIn_SuccessAndFail() {
        // Сценарій 1: Успішне підключення
        provideInput("PC\nDell\nX\n500\n700\n");
        PluggedAppliance pc = new PluggedAppliance(); // за замовчуванням unplugged
        home.getRooms().get(0).getAppliances().add(pc);

        // Меню: 4 (Ввімкнути в розетку) -> 1 (Кімната) -> 700 (ID)
        provideInput("4\n1\n700\n");
        home.SetAppliances();

        assertTrue(pc.isPlugged());

        // Сценарій 2: Немає вільних розеток
        // Зменшуємо к-сть розеток кімнати до 1 (яка вже зайнята PC)
        home.getRooms().get(0).setCountOfSocket(1);

        // Меню: 4 (Ввімкнути в розетку) -> 1 (Кімната) -> (Далі код не піде, бо перевірка)
        provideInput("4\n1\n");
        // Тут ми просто перевіряємо, що метод не впаде і виведе повідомлення (coverage пройде гілку)
        assertDoesNotThrow(() -> home.SetAppliances());
    }

    @Test
    void sortAppliancesByPower() {
        provideInput("A\nB\nM\n10\n801\nA\nB\nM\n50\n802\nA\nB\nM\n5\n803\n");
        PluggedAppliance weak = new PluggedAppliance(); // 10W -> set to 5W manually logic below
        PluggedAppliance strong = new PluggedAppliance(); // 50W
        PluggedAppliance weakest = new PluggedAppliance(); // 5W created last

        // Коригуємо потужність, бо конструктор зчитав те, що було в provideInput
        // Але оскільки ми не можемо легко змінити поля (немає сеттерів),
        // покладаємось на порядок вводу: 10, 50, 5.

        home.allAppliances.add(weak);     // 10
        home.allAppliances.add(strong);   // 50
        home.allAppliances.add(weakest);  // 5

        // Виклик методу (ввід не потрібен, лише вивід)
        provideInput("\n");
        home.SortAppliancesByPower();

        assertEquals(5.0, home.allAppliances.get(0).getPower());
        assertEquals(10.0, home.allAppliances.get(1).getPower());
        assertEquals(50.0, home.allAppliances.get(2).getPower());
    }

    @Test
    void calculateConsumptionPerHour() {
        provideInput("H\nB\nM\n1000\n900\n");
        PluggedAppliance heater = new PluggedAppliance();
        heater.turnOn();
        home.allAppliancesOn.add(heater);

        // Ввід: 2.5 години
        provideInput("2,5\n");

        double consumption = home.CalculateConsumptionPerHour();
        // 1000W * 2.5h / 1000 = 2.5 kWh
        assertEquals(2.5, consumption);
    }

    @Test
    void edgeCases_EmptyLists() {
        // Перевірка пустих списків у різних методах

        // 1. ConfigRoom без кімнат
        home.getRooms().clear();
        provideInput("\n");
        home.ConfigRoom(); // Should print "Немає кімнат" and return

        // 2. ShowAllAppliancesInRoom без кімнат
        provideInput("\n");
        home.ShowAllAppliancesInRoom();

        // 3. SetAppliances без приладів (Option 1)
        provideInput("1\n");
        home.SetAppliances();
    }
    @Test
    void testRemoveRoom() {

        home.getRooms().clear();

        // 1. ПІДГОТОВКА
        Room kitchen = new Room("Кухня", 1);
        Room bedroom = new Room("Спальня", 2);

        home.addRoom(kitchen);
        home.addRoom(bedroom);

        // Тепер тут точно буде 2
        org.junit.jupiter.api.Assertions.assertEquals(2, home.getRooms().size(), "Має бути 2 кімнати після додавання");

        // 2. ДІЯ (Видаляємо кухню)
        home.removeRoom(kitchen);

        // 3. ПЕРЕВІРКА
        org.junit.jupiter.api.Assertions.assertEquals(1, home.getRooms().size(), "Має залишитись 1 кімната");

        // Перевіряємо, що залишилась саме спальня
        org.junit.jupiter.api.Assertions.assertTrue(home.getRooms().contains(bedroom));

        // Перевіряємо, що кухня видалилась
        org.junit.jupiter.api.Assertions.assertFalse(home.getRooms().contains(kitchen));
    }
    @Test
    void testGetAllAppliancesOn() {
        // 1. ПІДГОТОВКА (Створення приладу)
        // Нам треба створити прилад, тому знову "годуємо" сканер
        String inputData = "Fan\nBrandX\nModelY\n50\n\n999\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputData.getBytes(java.nio.charset.StandardCharsets.UTF_8));

        java.lang.System.setIn(inputStream);
        resetSysFuncScanner(inputStream); // Обов'язково оновлюємо сканер!

        PluggedAppliance fan = new PluggedAppliance();

        // Імітуємо ситуацію: прилад увімкнено
        fan.turnOn();

        // Додаємо його в список увімкнених приладів (вручну, бо ми тестуємо геттер)
        home.allAppliancesOn.add(fan);

        // 2. ДІЯ (Виклик методу)
        ArrayList<ElectricalAppliance> resultList = home.getAllAppliancesOn();

        // 3. ПЕРЕВІРКА
        // Список не має бути null
        org.junit.jupiter.api.Assertions.assertNotNull(resultList);

        // Має містити 1 елемент
        org.junit.jupiter.api.Assertions.assertEquals(1, resultList.size());

        // Цей елемент має бути нашим вентилятором
        org.junit.jupiter.api.Assertions.assertEquals(fan, resultList.get(0));

        // Перевіряємо ID, щоб впевнитись на 100%
        org.junit.jupiter.api.Assertions.assertEquals(999, resultList.get(0).getID());
    }
}