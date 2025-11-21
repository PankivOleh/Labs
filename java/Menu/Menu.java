package Menu;
import java.util.Scanner;
import System.SysFunc;
import Home.Home;
import command.*;
public class Menu {

    public static int MainMenu(Home home) {
        System.out.println("Головне меню:");
        System.out.println("1.Керування будинком.\n" +
                "2.Керування приладами.\n" +
                "3.Керуванням логуванням.\n"+
                "4.Довідка.\n"+
                "5.Вихід з програми.\n");
        int choice = SysFunc.get(1,5);
        switch (choice){
            case 1 -> HomeMenu(home);
            case 2 -> AppliancesMenu(home);
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
                "4.Створити або видалити кімнату.\n" );
        int choice = SysFunc.get(1,4);
        switch (choice){
            case 1 -> new HomeCommand(home::ShowHome).execute();
            case 2 -> new HomeCommand(home::ConfigAppInRoom).execute();
            case 3 -> new HomeCommand(home::ConfigRoom).execute();
            case 4 -> new HomeCommand(home::ConfigHome).execute();
        }
    }
    public static void AppliancesMenu(Home home){
        System.out.println("Меню керування приладами");
        System.out.println("1.Підрахувати сумарну потужність.\n" +
                "2.Показати всі прилади в кімнаті.\n" +
                "3.Увімкнути/вимкнути прилади.\n" +
                "4.Сортувати прилади за потужністю.\n" +
                "5.Знайти прилади за параметрами.\n" +
                "6.Розрахувати очікуване споживання за годину.\n");
        int choice = SysFunc.get(1,6);
        switch (choice){
            case 1 -> new HomeCommand(home::CalculateGeneralPower).execute();
            case 2 -> new HomeCommand(home::ShowAllAppliancesInRoom).execute();
            case 3 -> new HomeCommand(home::SetAppliances).execute();
            case 4 -> new HomeCommand(home::SortAppliancesByPower).execute();
            case 5 -> new HomeCommand(home::FindAppliancesByParams).execute();
            case 6 -> new HomeCommand(home::CalculateConsumptionPerHour).execute();
        }
    }

    public static void LogsMenu(Home home){
        System.out.println("Меню керування логуванням та файлами");
        System.out.println("1.Вивести лог роботи.\n" +
                "2.Завантажити дані з файлу.\n" +
                "3.Зберегти дані у файл.\n");
        int choice = SysFunc.get(1,3);
        switch (choice){
            case 1 -> new HomeCommand(home::ShowLogs).execute();
            case 2 -> new HomeCommand(home::DownloadToLogFromFile).execute();
            case 3 -> new HomeCommand(home::UploadFromLogToFile).execute();
        }
    }
    public static void Instruction(){
        System.out.println("Інструкція");
    }
}
