package Menu;
import java.io.IOException;

import Utils.*;
import Home.*;
import command.*;
import java.io.Serializable;
public class Menu implements Serializable{

    public static  int MainMenu(HomeWrapper home) throws IOException {
        System.out.println("Головне меню:");
        System.out.println("1.Керування будинком.\n" +
                "2.Керування приладами.\n" +
                "3.Керуванням логуванням.\n"+
                "4.Довідка.\n"+
                "5.Вихід з програми.\n");
        int choice = SysFunc.get(1,5);
        switch (choice){
            case 1 -> HomeMenu(home.getHome());
            case 2 -> AppliancesMenu(home.getHome());
            case 3 -> LogsMenu(home);
            case 4 -> Instruction();
            case 5 -> {return 0;}
        }
        return choice;
    }
    public static void HomeMenu(Home home){
        System.out.println("Меню керування будинком");
        System.out.println("1.Переглянути структуру будинку.\n" +
                "2.Додати або видалити прилад у кімнаті.\n" +
                "3.Редагувати конфігурацію кімнати.\n" +
                "4.Створити або видалити кімнату.\n" +
                "5.Назад" );
        int choice = SysFunc.get(1,5);
        switch (choice){
            case 1 -> new HomeCommand(home::ShowHome).execute();
            case 2 -> new HomeCommand(home::ConfigAppInRoom).execute();
            case 3 -> new HomeCommand(home::ConfigRoom).execute();
            case 4 -> new HomeCommand(home::ConfigHome).execute();
            case 5 -> {return;}
        }
    }
    public static void AppliancesMenu(Home home){
        System.out.println("Меню керування приладами");
        System.out.println("1.Підрахувати сумарну потужність.\n" +
                "2.Показати всі прилади в кімнаті.\n" +
                "3.Увімкнути/вимкнути прилади.\n" +
                "4.Сортувати прилади за потужністю.\n" +
                "5.Знайти прилади за параметрами.\n" +
                "6.Розрахувати очікуване споживання за годину.\n" +
                "7.Назад");
        int choice = SysFunc.get(1,7);
        switch (choice){
            case 1 -> new HomeCommand(home::CalculateGeneralPower).execute();
            case 2 -> new HomeCommand(home::ShowAllAppliancesInRoom).execute();
            case 3 -> new HomeCommand(home::SetAppliances).execute();
            case 4 -> new HomeCommand(home::SortAppliancesByPower).execute();
            case 5 -> new HomeCommand(home::FindAppliancesByParams).execute();
            case 6 -> new HomeCommand(home::CalculateConsumptionPerHour).execute();
            case 7 -> {return;}
        }
    }

    public static void LogsMenu(HomeWrapper home) throws IOException {
        System.out.println("Меню керування логуванням та файлами");
        System.out.println("1.Вивести лог роботи.\n" +
                "2.Завантажити дані з файлу.\n" +
                "3.Зберегти дані у файл.\n" +
                "4.Назад");

        int choice = SysFunc.get(1,4);
        switch (choice){
            case 1 -> {
                System.out.println("\n===== ПОЧАТОК ЛОГ-ФАЙЛУ =====");
                try {
                    java.nio.file.Files.lines(java.nio.file.Paths.get("app.log"))
                            .forEach(System.out::println);
                } catch (IOException e) {
                    System.out.println("Файл логів не знайдено (можливо, програма запущена вперше).");
                }
                System.out.println("===== КІНЕЦЬ ЛОГ-ФАЙЛУ =====\n");
            }
            case 2 -> {
                home.setHome(FileManager.uploadFromFile("C:\\Users\\Og\\IdeaProject\\java\\LB4\\Home.bin"));
                home.getHome().initTransient();
            }
            case 3 -> FileManager.downloadToFile(home.getHome() , "C:\\Urs\\Oleg\\IdeaProject\\java\\LB4\\Home.bin");
            case 4 -> {return;}
        }
    }
    public static void Instruction(){
        System.out.println("Інструкція");
    }
}

