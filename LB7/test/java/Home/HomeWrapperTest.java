package Home;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HomeWrapperTest {

    @Test
    void testWrapperFunctionality() {
        // 1. Створення об'єктів Home
        Home home1 = new Home();
        Home home2 = new Home();

        // 2. Тест конструктора
        HomeWrapper wrapper = new HomeWrapper(home1);

        // Перевіряємо, що геттер повертає той самий об'єкт, що ми передали
        assertNotNull(wrapper.getHome());
        assertEquals(home1, wrapper.getHome(), "Геттер має повертати об'єкт з конструктора");

        // 3. Тест сеттера
        wrapper.setHome(home2);

        // Перевіряємо, що об'єкт змінився
        assertEquals(home2, wrapper.getHome(), "Геттер має повертати новий об'єкт після сеттера");
        assertNotEquals(home1, wrapper.getHome(), "Старий об'єкт більше не має бути в wrapper");
    }

    @Test
    void testWrapperWithNull() {
        // Перевіряємо, чи клас нормально реагує на null (якщо це допустимо логікою)
        HomeWrapper wrapper = new HomeWrapper(null);
        assertNull(wrapper.getHome());

        Home h = new Home();
        wrapper.setHome(h);
        assertEquals(h, wrapper.getHome());

        wrapper.setHome(null);
        assertNull(wrapper.getHome());
    }
}