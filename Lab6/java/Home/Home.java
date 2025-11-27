package Home;
import Appliances.*;
import java.io.Serializable;
import java.util.*;
import java.util.logging.Logger;
import System.*;

public class Home implements Serializable {
    ArrayList<Room> rooms;
    ArrayList<ElectricalAppliance> allAppliances ;
    ArrayList<ElectricalAppliance> allAppliancesOn;
    private static final long serialVersionUID = 1L;
    private transient Logger logger = Logger.getLogger("Home");
    private transient Scanner sc = new Scanner(System.in);

    FileManager fileManager;
    public Home(){
        rooms = new ArrayList<>();
        allAppliances = new ArrayList<>();
        allAppliancesOn = new ArrayList<>();
        logger = Logger.getLogger("Home");
        fileManager = null;
    }
    public void ShowHome(){
        for(Room r : rooms){
            System.out.println(r.toString());
        }
    }
    public void ConfigAppInRoom() {
        SysFunc.Println("1. Додати прилад.\n2. Видалити прилад;");
        int choice = SysFunc.get(1, 2);

        if (choice == 1) {
            ElectricalAppliance app = null;

            SysFunc.Println("Оберіть тип приладу:");
            System.out.println("1. Провідний прилад з вилкою.\n" +
                    "2. Безпровідний прилад з акумулятором.\n" +
                    "3. Зарядний пристрій.\n" +
                    "4. Прилад з постійним під'єднанням до мережі");
            int type = SysFunc.get(1, 4);

            if(rooms.isEmpty()){
                SysFunc.Println("В домі немає кімнат");
                return;
            }
            SysFunc.Println("Оберіть кімнату для нового приладу:");
            for (int k = 0; k < rooms.size(); k++) {
                System.out.println((k + 1) + ". " + rooms.get(k).toString());
            }
            int roomIndex = SysFunc.get(1, rooms.size()) - 1;
            Room room = rooms.get(roomIndex);

            switch (type) {
                case 1 -> app = new PluggedAppliance();
                case 2 -> {
                    BatteryAppliance bapp = new BatteryAppliance();

                    // --- Перевірка наявності зарядок у кімнаті ---
                    List<Charger> chargersInRoom = new ArrayList<>();
                    for (ElectricalAppliance el : room.getAppliances()) {
                        if (el instanceof Charger) chargersInRoom.add((Charger) el);
                    }

                    SysFunc.Println("Бажаєте призначити зарядку? 1. Так, 2. Ні");
                    int assignCharger = SysFunc.get(1, 2);

                    if (assignCharger == 1) {
                        if (chargersInRoom.isEmpty()) {
                            SysFunc.Println("У цій кімнаті немає зарядок. Створити нову? 1. Так, 2. Ні");
                            int createCharger = SysFunc.get(1, 2);
                            if (createCharger == 1) {
                                Charger newCharger = new Charger();
                                for (ElectricalAppliance el : allAppliances) {
                                    if (el.getID() == newCharger.getID()) {
                                        SysFunc.Println("Зарядка з таким ID вже існує. Створіть пристрій заново.");
                                        return;
                                    }
                                }
                                allAppliances.add(newCharger);
                                room.getAppliances().add(newCharger);
                                bapp.setCharger(newCharger);
                                newCharger.setAppliance(bapp);
                            }
                        } else {
                            SysFunc.Println("Виберіть ID зарядки з цієї кімнати:");
                            for (Charger ch : chargersInRoom) {
                                SysFunc.Println(ch.getID() + ". " + ch);
                            }
                            int chargerID = sc.nextInt();
                            boolean found = false;
                            for (Charger ch : chargersInRoom) {
                                if (ch.getID() == chargerID) {
                                    bapp.setCharger(ch);
                                    ch.setAppliance(bapp);
                                    found = true;
                                    break;
                                }
                            }
                            if (!found) {
                                SysFunc.Println("Невірний ID зарядки.");
                                return;
                            }
                        }
                    }
                    app = bapp;
                }
                case 3 -> app = new Charger();
                case 4 -> app = new AlwaysPlugedAppliance();
            }

            if(checkAppliancesID(app.getID())!=0){
                return;
            }

            allAppliances.add(app);
            room.getAppliances().add(app);

        } else {
            if(rooms.isEmpty()){
                SysFunc.Println("Немає приладів");
                return;
            }
            SysFunc.Println("Оберіть кімнату, з якої хочете видалити прилад:");
            for (int k = 0; k < rooms.size(); k++) {
                System.out.println((k + 1) + ". " + rooms.get(k).toString());
            }
            int roomIndex = SysFunc.get(1, rooms.size()) - 1;
            Room room = rooms.get(roomIndex);

            if (room.getAppliances().isEmpty()) {
                SysFunc.Println("У цій кімнаті немає приладів для видалення.");
                return;
            }

            SysFunc.Println("Список приладів у кімнаті:");
            room.showAllAppliances();

            SysFunc.Println("Введіть ID приладу, який хочете видалити:");
            int applianceID = sc.nextInt();
            boolean found = false;
            for (Iterator<ElectricalAppliance> it = room.getAppliances().iterator(); it.hasNext(); ) {
                ElectricalAppliance el = it.next();
                if (el.getID() == applianceID) {
                    it.remove();
                    allAppliances.remove(el);
                    found = true;
                    break;
                }
            }

            if (!found) {
                SysFunc.Println("ID вказано невірно, або такого приладу не існує.");
            }
        }
    }


    public void ConfigRoom(){
        if(rooms.isEmpty()){
            SysFunc.Println("Немає кімнат");
            return;
        }
        SysFunc.Println("Оберіть кімнату, яку бажаєте змінити:");
        for (int k = 0; k < this.rooms.size(); k++) {
            System.out.println((k+1) + ". " + rooms.get(k).toString());
        }
        int roomIndex = SysFunc.get(1, rooms.size()) - 1;
        Room room = this.rooms.get(roomIndex);
        SysFunc.Println("Бажаєте змінити ім'я? 1 так , 2 ні");
        int i  = SysFunc.get(1 , 2);
        if (i ==1){
            SysFunc.Println("Введіть ім'я кімнати");
            String name = sc.nextLine();
            room.setNames(name);
        }
        SysFunc.Println("Бажаєте змінити кількість розеток? 1 так , 2 ні");
        i  = SysFunc.get(1 , 2);
        if(i==1){
            System.out.println("Введіть кількість розеток:");
            int countOfSocket = sc.nextInt();
            sc.nextLine();
            room.setCountOfSocket(countOfSocket);
            i = room.CheckSocket();
            if(i>0){
                System.out.println("Оскільки розеток стало менше, було видалено "+ i + " останніх підключених пристроїв");
            }
        }


    }
    public void ConfigHome(){
        System.out.println("1. Додати кімнату \n2. Видалити кімнату");
        int i = SysFunc.get(1 , 2);
        if(i == 1){
            String name = "";
            int countOfSocket;
            SysFunc.Println("Введіть ім'я");
            name = sc.nextLine();
            SysFunc.Println("Введіть кількість розеток");
            countOfSocket = sc.nextInt();
            sc.nextLine();
            Room room = new Room(name, countOfSocket);
            rooms.add(room);
            return;
        }
        if(!(rooms.isEmpty())){
            int j = 1;
            for(Room r : rooms){
                System.out.println("Кімната№" + j + r.toString());
                j++;
            }
            SysFunc.Println("Введіть номер кімнати яку хочете видалити(Усі прилади будуть видалені разом з кімнатою)");
            int roomIndex = SysFunc.get(1, rooms.size()) - 1;
            deleteRoom(rooms.get(roomIndex));
            return;
        }
        else{
            SysFunc.Println("В домі поки немає жодної кімнати");
        }


    }
    public double CalculateGeneralPower(){
        double generalPower = 0;
        if(allAppliancesOn.isEmpty()){
            SysFunc.Println("Немає ввімкнених приладів. Загальна потужність = " + generalPower);
            return generalPower;
        }
        for(ElectricalAppliance app : allAppliancesOn){
            generalPower += app.getPower();
        }
        SysFunc.Println("Загальна потужність = " + generalPower/1000 + "kW");
        return generalPower;
    }
    public void ShowAllAppliancesInRoom(){
        if(rooms.isEmpty()){
            SysFunc.Println("В домі немає кімнат;");
            return;
        }
        SysFunc.Println("Оберіть кімнату, в якій бажаєте переглянути усі прилади:");
        for (int k = 0; k < this.rooms.size(); k++) {
            System.out.println((k+1) + ". " + rooms.get(k).toString());
        }
        int roomIndex = SysFunc.get(1, rooms.size()) - 1;
        Room room = this.rooms.get(roomIndex);
       System.out.println("Усі прилади:");
       room.showAllAppliances();
       System.out.println("Пристрої які ввімкнуті в розетку");
       room.showAllPluggedAppliances();
    }
    public void SetAppliances(){
        System.out.println("1.Ввімкнути пристрій\n" +
                "2.Вимкнути пристрій\n" +
                "3.Вимкнути пристрій з розетки\n" +
                "4.Ввімкнути пристрій в розетку");
        int i = SysFunc.get(1,4);
        if( i == 1){
            if(allAppliances.isEmpty()){
                SysFunc.Println("В домі немає приладів");
                return;
            }
            for(ElectricalAppliance el : allAppliances){
                SysFunc.Println(el.toString());
            }
            System.out.println("Введіть ID пристрою який ви хочете ввімкнути");
            i = sc.nextInt();
            for(ElectricalAppliance el : allAppliances) {
                if (el.getID() == i && !el.isOn()) {
                    el.turnOn();
                    if (el.isOn())
                        allAppliancesOn.add(el);
                    i = 0;
                    this.checkOnAppliances();
                    break;
                }
            }
                if(i!=0){
                    System.out.println("Пристрій не вдалося ввімкнути.\nПристрою з таким ID або не існує, або він уже ввімкнений.");
                }
            return;
        }
        if( i == 2){
            if (!(rooms.isEmpty())) {
                int j = 1;
                for (Room r : rooms) {
                    System.out.println("Кімната№" + j + r.toString());
                    j++;
                }
                SysFunc.Println("Введіть номер кімнати в якій ви хочете вимкнути пристрій");
                int roomIndex = SysFunc.get(1, rooms.size()) - 1;
                this.rooms.get(roomIndex).showAllAppliancesOn();
                if(this.rooms.get(roomIndex).getAppliancesOn().isEmpty()){
                    SysFunc.Println("Немає ввімкнених пристроїв");
                }
            System.out.println("Введіть ID пристрою який ви хочете вимкнути");
            i = sc.nextInt();
            for(Iterator<ElectricalAppliance> it = this.rooms.get(roomIndex).getAppliancesOn().iterator(); it.hasNext();) {
                ElectricalAppliance el = it.next();
                if (el.getID() == i && el.isOn()) {
                    el.turnOff();
                    it.remove();
                    allAppliancesOn.remove(el);
                    i = 0;
                    break;
                }
            }
                if(i!=0){
                    System.out.println("Пристрій не вдалося вимкнути.\nПристрою з таким ID або не існує, або він уже вимкнений.");
                }
            return;
        }else{
                SysFunc.Println("В домі немає приладів");
                return;
            }
            }
        if(i == 3) {
            if (!(rooms.isEmpty())) {
                int j = 1;
                for (Room r : rooms) {
                    System.out.println("Кімната№" + j + r.toString());
                    j++;
                }
                SysFunc.Println("Введіть номер кімнати в якій ви хочете вимкнути пристрій з розетки");
                int roomIndex = SysFunc.get(1, rooms.size()) - 1;
                this.rooms.get(roomIndex).showAllPluggedAppliances();
                if(this.rooms.get(roomIndex).pluggedAppliances.isEmpty()){
                    SysFunc.Println("Немає ввімкнених в розетку пристроїв");
                    return;
                }
                SysFunc.Println("Введіть ID пристрою який ви хочете вимкнути з розетки");
                i = sc.nextInt();
                for (Iterator<PluggedAppliance> it = this.rooms.get(roomIndex).pluggedAppliances.iterator(); it.hasNext();) {
                    PluggedAppliance el = it.next();
                    if (el.getID() == i && el.isPlugged()) {
                        if (el.isOn()) {
                            allAppliancesOn.remove(el);
                            this.rooms.get(roomIndex).appliancesOn.remove(el);
                        }
                        el.PlugOut();
                        it.remove();
                        i = 0;
                        break;
                    }
                }
                if (i != 0) {
                    SysFunc.Println("Пристрою з таким ID або не існує, або він уже вимкнений.");
                }
            }else{
                SysFunc.Println("В домі немає приладів");
                return;
            }
            return;
        }

        if(i==4){
            if (!(rooms.isEmpty())) {
                int j = 1;
                for (Room r : rooms) {
                    System.out.println("Кімната№" + j + r.toString());
                    j++;
                }
                SysFunc.Println("Введіть номер кімнати в якій ви хочете ввімкнути пристрій в розетку");
                int roomIndex = SysFunc.get(1, rooms.size()) - 1;
                if(this.rooms.get(roomIndex).CheckSocket() >= 0){
                    SysFunc.Println("Немає вільних розеток");
                    return;
                }
                boolean is = false;
                 for(ElectricalAppliance el : this.rooms.get(roomIndex).getAppliances()) {
                     if (el instanceof PluggedAppliance) {
                         SysFunc.Println(el.toString());
                         is = true;
                     }
                 }
                 if(is == false){
                     SysFunc.Println("Немає пристроїв на розетку");
                 return;}
                System.out.println("Введіть ID пристрою який ви хочете ввімкнути");
                i = sc.nextInt();
                for(ElectricalAppliance el : this.rooms.get(roomIndex).getAppliances()) {
                    if(el instanceof PluggedAppliance){
                        if(((PluggedAppliance) el).getID() == i && !((PluggedAppliance) el).isPlugged()){
                            ((PluggedAppliance) el).PlugIn();
                            this.rooms.get(roomIndex).pluggedAppliances.add((PluggedAppliance) el);
                            i = 0;
                            return;
                        }
                    }
                }

            }
            else{
            SysFunc.Println("В домі немає приладів");
            return;
        }
        }
    }
    public void SortAppliancesByPower(){
        allAppliances.sort(Comparator.comparingDouble(ElectricalAppliance::getPower));
        allAppliancesOn.sort(Comparator.comparingDouble(ElectricalAppliance::getPower));
        for(Room r : rooms){
            r.getAppliances().sort(Comparator.comparingDouble(ElectricalAppliance::getPower));
            r.getAppliancesOn().sort(Comparator.comparingDouble(ElectricalAppliance::getPower));
            r.getPluggedAppliances().sort(Comparator.comparingDouble(ElectricalAppliance::getPower));
        }
        this.ShoweAllAppliances();
    }
    public void FindAppliancesByParams() {
        boolean isBattery, isPlugged, isOn;

        SysFunc.Println("Пошук приладів за параметрами:");
        SysFunc.Println("Цей пристрій безпровідний (на батареях)?\n1. Так\n2. Ні");
        int batteryChoice = sc.nextInt();

        isBattery = batteryChoice == 1;
        isPlugged = batteryChoice == 2;

        SysFunc.Println("Цей пристрій ввімкнений?\n1. Так\n2. Ні");
        isOn = sc.nextInt() == 1;

        SysFunc.Println("Введіть верхню межу потужності (в ватах):");
        double maxPower = sc.nextDouble();
        SysFunc.Println("Введіть нижню межу потужності:");
        double minPower = sc.nextDouble();

        for (ElectricalAppliance el : allAppliances) {
            boolean powerOK = el.getPower() <= maxPower && el.getPower() >= minPower;
            boolean stateOK = el.isOn() == isOn;

            boolean typeMatches =
                    (isPlugged && (el instanceof PluggedAppliance || el instanceof AlwaysPlugedAppliance)) ||
                            (isBattery && el instanceof BatteryAppliance);

            if (powerOK && stateOK && typeMatches) {
                SysFunc.Println(el.toString());
            }
        }
    }
        public double CalculateConsumptionPerHour(){
        double consumptionPerHour = 0;
        double hour;
        System.out.println("Введіть кількість годин");
        hour = sc.nextDouble();
        for(ElectricalAppliance el : allAppliancesOn){
            consumptionPerHour += el.getPower();
        }
        consumptionPerHour  *= hour;
        consumptionPerHour /= 1000;
        SysFunc.Println("Споживання за "+hour+"год = " + consumptionPerHour + "kWh");
        return consumptionPerHour;
    }
    public void ShowLogs(){}
    public void ShoweAllAppliances(){
        for(ElectricalAppliance el : allAppliances){
            SysFunc.Println(el.toString());
        }
    }
    public void ShoweAllAppliancesOn(){
        for(ElectricalAppliance el : allAppliancesOn){
            SysFunc.Println(el.toString());
        }
    }

    public int checkAppliancesID(int id){
        for(int i = 0 ; i < allAppliances.size() - 1; i++ ){
            if(allAppliances.get(i).getID() == id){
                SysFunc.Println("Прилад з таким ID вже існує.");
                return id;
            }
        }
        return 0;
    }
    protected void deleteRoom(Room room){
        for(ElectricalAppliance el : room.getAppliances()){
            allAppliances.remove(el);
        }
        for(ElectricalAppliance el : room.getAppliancesOn()){
            allAppliancesOn.remove(el);
        }
        rooms.remove(room);
    }

    public void initTransient() {
        this.sc = new Scanner(System.in);
        this.logger = Logger.getLogger("Home");
    }

    public void checkOnAppliances() {
        Set<Integer> seen = new HashSet<>();

        Iterator<ElectricalAppliance> it = allAppliancesOn.iterator();
        while (it.hasNext()) {
            ElectricalAppliance el = it.next();
            if (seen.contains(el.getID())) {
                it.remove(); // дубль — видаляємо
            } else {
                seen.add(el.getID()); // перша поява — запамʼятовуємо
            }
        }
    }



}
