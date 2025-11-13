package Menu;
import java.util.Scanner;
import System.SysFunc;
import Home.Home;
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
        System.out.println("1.Переглянути структуру будинку." +
                "2.Додати або видалити прилад у кімнаті." +
                "3.Редагувати конфігурацію кімнати." +
                "4.Створити або видалити кімнату." );
        int choice = SysFunc.get(1,4);
        switch (choice){
            case 1 -> home.ShowHome();
            case 2 -> home.ConfigAppInRoom();
            case 3 -> home.ConfigRoom();
            case 4 -> home.ConfigHome();
        }
    }
    public static void AppliancesMenu(Home home){
        System.out.println("Меню керування приладами");
        System.out.println("1.Підрахувати сумарну потужність." +
                "2.Показати всі прилади в кімнаті." +
                "3.Увімкнути/вимкнути прилади." +
                "4.Сортувати прилади за потужністю." +
                "5.Знайти прилади за параметрами." +
                "6.Розрахувати очікуване споживання за годину.");
        int choice = SysFunc.get(1,6);
        switch (choice){
            case 1 -> home.CalculateGeneralPower();
            case 2 -> home.ShowAllAppliancesInRoom();
            case 3 -> home.SetAppliances();
            case 4 -> home.SortAppliancesByPower();
            case 5 -> home.FindAppliancesByParams();
            case 6 -> home.CalculateConsumptionPerHour();
        }
    }

    public static void LogsMenu(Home home){
        System.out.println("Меню керування логуванням");
        System.out.println("1.Вивести лог роботи." +
                "2.Завантажити дані з файлу." +
                "3.Зберегти дані у файл.");
        int choice = SysFunc.get(1,3);
        switch (choice){
            case 1 -> home.ShowLogs();
            case 2 -> home.DownloadToLogFromFile();
            case 3 -> home.UploadFromLogToFile();
        }
    }
    public static void Instruction(){
        System.out.println("Інструкція");
    }
}
