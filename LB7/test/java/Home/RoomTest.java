package Home;

import Appliances.PluggedAppliance;
import Utils.SysFunc;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayInputStream;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RoomTest {

    @Test
    void testAddAppliance() {
        // 1. Підготовка: "Годуємо" сканер, щоб створити прилад без зависання
        String input = "TestApp\nBrand\nModel\n100\n\n999\n";
        ByteArrayInputStream stream = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));

        // Підміняємо сканер в SysFunc
        try {
            Field sysField = SysFunc.class.getDeclaredField("SC");
            sysField.setAccessible(true);
            sysField.set(null, new Scanner(stream, StandardCharsets.UTF_8));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // 2. Створення об'єктів
        PluggedAppliance app = new PluggedAppliance(); // Створиться з ID 999
        Room room = new Room("TestRoom", 2);

        // 3. ДІЯ: Викликаємо твій метод
        room.addAppliance(app);

        // 4. ПЕРЕВІРКА
        assertEquals(1, room.getAppliances().size(), "В кімнаті має бути 1 прилад");
        assertTrue(room.getAppliances().contains(app), "Прилад має бути в списку");
        assertEquals(999, room.getAppliances().get(0).getID(), "ID приладу має співпадати");
    }
}