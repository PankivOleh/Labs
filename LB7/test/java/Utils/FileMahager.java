package Utils;

import Home.Home;
import Home.Room;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class FileManagerTest {

    // Використовуємо java.lang.System.out, щоб уникнути конфліктів імен
    private final PrintStream originalOut = java.lang.System.out;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    // Ім'я тимчасового файлу для тестів
    private final String TEST_FILE_NAME = "test_home_save.bin";

    @BeforeEach
    void setUp() {
        // Перехоплюємо вивід у консоль
        java.lang.System.setOut(new PrintStream(outContent, true, StandardCharsets.UTF_8));
    }

    @AfterEach
    void tearDown() {
        // 1. Відновлюємо консоль
        java.lang.System.setOut(originalOut);

        // 2. Видаляємо тимчасовий файл після тесту, щоб не смітити
        File file = new File(TEST_FILE_NAME);
        if (file.exists()) {
            file.delete();
        }
    }

    @Test
    void testDownloadAndUploadSuccess() throws IOException {
        // --- ЕТАП 1: Підготовка даних (Arrange) ---
        Home originalHome = new Home();
        // Якщо конструктор не ініціалізує список, робимо це вручну
        if (originalHome.getRooms() == null) {
            // Якщо у вас немає сеттера, цей крок може бути зайвим,
            // якщо конструктор Home() працює коректно.
        }

        // Додаємо кімнату, щоб перевірити, що дані справді зберігаються
        Room room = new Room("TestRoom_Serialization", 5);
        originalHome.addRoom(room);

        // --- ЕТАП 2: Збереження (Act - Save) ---
        FileManager.downloadToFile(originalHome, TEST_FILE_NAME);

        // Перевіряємо, що файл створився
        File file = new File(TEST_FILE_NAME);
        assertTrue(file.exists(), "Файл повинен був створитися");
        assertTrue(file.length() > 0, "Файл не повинен бути порожнім");

        // Перевіряємо вивід у консоль
        String outputSave = outContent.toString(StandardCharsets.UTF_8);
        assertTrue(outputSave.contains("Дані збережено в файл"));

        // Очищаємо консоль перед завантаженням
        outContent.reset();

        // --- ЕТАП 3: Завантаження (Act - Load) ---
        Home loadedHome = FileManager.uploadFromFile(TEST_FILE_NAME);

        // Перевіряємо вивід у консоль
        String outputLoad = outContent.toString(StandardCharsets.UTF_8);
        assertTrue(outputLoad.contains("Дані завантажено з файлу"));

        // --- ЕТАП 4: Порівняння (Assert) ---
        assertNotNull(loadedHome, "Завантажений об'єкт не має бути null");

        // Перевіряємо, чи зберігся вміст
        assertEquals(1, loadedHome.getRooms().size(), "Кількість кімнат має зберегтися");

        Room loadedRoom = loadedHome.getRooms().get(0);
        assertEquals("TestRoom_Serialization", loadedRoom.getName(), "Ім'я кімнати має зберегтися");
        assertEquals(5, loadedRoom.getCountOfSocket(), "Дані кімнати мають зберегтися");
    }

    @Test
    void testUploadFileNotFound() {
        //  Спроба завантажити неіснуючий файл ---
        String fakePath = "non_existent_file_12345.bin";

        // Ми очікуємо, що метод викине IOException (або FileNotFoundException)
        assertThrows(IOException.class, () -> {
            FileManager.uploadFromFile(fakePath);
        }, "Має бути помилка при читанні неіснуючого файлу");
    }
}