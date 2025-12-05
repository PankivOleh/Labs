package Home;
import Appliances.*;
import java.io.Serializable;
import java.util.*;
import java.util.logging.Logger;
import Utils.*;

public class Home implements Serializable {
    ArrayList<Room> rooms;
    ArrayList<ElectricalAppliance> allAppliances ;
    ArrayList<ElectricalAppliance> allAppliancesOn;
    private static final long serialVersionUID = 1L;
    private transient Logger logger;
    private transient Scanner sc = SysFunc.SC;

    FileManager fileManager;
    public Home(){
        rooms = new ArrayList<>();
        allAppliances = new ArrayList<>();
        allAppliancesOn = new ArrayList<>();
        logger = Logger.getLogger("Home");
        fileManager = null;
        logger = Logger.getLogger(Home.class.getName());
        logger.info("Створено новий пустий дім.");
    }
    public void ShowHome(){
        for(Room r : rooms){
            System.out.println(r.toString());
        }
    }
    public void ConfigAppInRoom() {
        System.out.println("1. Додати прилад.\n2. Видалити прилад;");
        int choice = SysFunc.get(1, 2);

        if (choice == 1) {
            ElectricalAppliance app = null;

            System.out.println("Оберіть тип приладу:");
            System.out.println("1. Провідний прилад з вилкою.\n" +
                    "2. Безпровідний прилад з акумулятором.\n" +
                    "3. Зарядний пристрій.\n" +
                    "4. Прилад з постійним під'єднанням до мережі");
            int type = SysFunc.get(1, 4);

            if(rooms.isEmpty()){
                System.out.println("В домі немає кімнат");
                return;
            }
            System.out.println("Оберіть кімнату для нового приладу:");
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

                    System.out.println("Бажаєте призначити зарядку? 1. Так, 2. Ні");
                    int assignCharger = SysFunc.get(1, 2);

                    if (assignCharger == 1) {
                        if (chargersInRoom.isEmpty()) {
                            System.out.println("У цій кімнаті немає зарядок. Створити нову? 1. Так, 2. Ні");
                            int createCharger = SysFunc.get(1, 2);
                            if (createCharger == 1) {
                                Charger newCharger = new Charger();
                                for (ElectricalAppliance el : allAppliances) {
                                    if (el.getID() == newCharger.getID()) {
                                        System.out.println("Зарядка з таким ID вже існує. Створіть пристрій заново.");
                                        return;
                                    }
                                }
                                allAppliances.add(newCharger);
                                room.getAppliances().add(newCharger);
                                bapp.setCharger(newCharger);
                                newCharger.setAppliance(bapp);
                            }
                        } else {
                            System.out.println("Виберіть ID зарядки з цієї кімнати:");
                            for (Charger ch : chargersInRoom) {
                                System.out.println(ch.getID() + ". " + ch);
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
                                System.out.println("Невірний ID зарядки.");
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
                System.out.println("Немає приладів");
                return;
            }
            System.out.println("Оберіть кімнату, з якої хочете видалити прилад:");
            for (int k = 0; k < rooms.size(); k++) {
                System.out.println((k + 1) + ". " + rooms.get(k).toString());
            }
            int roomIndex = SysFunc.get(1, rooms.size()) - 1;
            Room room = rooms.get(roomIndex);

            if (room.getAppliances().isEmpty()) {
                System.out.println("У цій кімнаті немає приладів для видалення.");
                return;
            }

            System.out.println("Список приладів у кімнаті:");
            room.showAllAppliances();

            System.out.println("Введіть ID приладу, який хочете видалити:");
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
                System.out.println("ID вказано невірно, або такого приладу не існує.");
            }
        }
    }


    public void ConfigRoom(){
        if(rooms.isEmpty()){
            System.out.println("Немає кімнат");
            if (logger != null) logger.warning("Спроба конфігурації кімнат, але список порожній.");
            return;
        }
        System.out.println("Оберіть кімнату, яку бажаєте змінити:");
        for (int k = 0; k < this.rooms.size(); k++) {
            System.out.println((k+1) + ". " + rooms.get(k).toString());
        }
        int roomIndex = SysFunc.get(1, rooms.size()) - 1;
        Room room = this.rooms.get(roomIndex);
        System.out.println("Бажаєте змінити ім'я? 1 так , 2 ні");
        int i  = SysFunc.get(1 , 2);
        if (i ==1){
            System.out.println("Введіть ім'я кімнати");
            String name = sc.nextLine();
            room.setNames(name);
        }
        System.out.println("Бажаєте змінити кількість розеток? 1 так , 2 ні");
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
        if(logger!=null)
            logger.info("Було змінено конфігурацію кімнати ");


    }
    public void ConfigHome(){
        System.out.println("1. Додати кімнату \n2. Видалити кімнату");
        int i = SysFunc.get(1 , 2);
        if(i == 1){
            String name = "";
            int countOfSocket;
            System.out.println("Введіть ім'я");
            name = sc.nextLine();
            System.out.println("Введіть кількість розеток");
            countOfSocket = sc.nextInt();
            sc.nextLine();
            Room room = new Room(name, countOfSocket);
            rooms.add(room);
            if(logger!=null)
                logger.info("Було додано кімнату");
            return;
        }
        if(!(rooms.isEmpty())){
            int j = 1;
            for(Room r : rooms){
                System.out.println("Кімната№" + j + r.toString());
                j++;
            }
            System.out.println("Введіть номер кімнати яку хочете видалити(Усі прилади будуть видалені разом з кімнатою)");
            int roomIndex = SysFunc.get(1, rooms.size()) - 1;
            deleteRoom(rooms.get(roomIndex));
            if(logger!=null)
                logger.info("Було видалено кімнату");
            return;
        }
        else{
            System.out.println("В домі поки немає жодної кімнати");
            if(logger!=null)
                logger.info("Була спроба видалити кімнату, але не існувало жодної. ");
            return;
        }


    }
    public double CalculateGeneralPower(){
        double generalPower = 0;
        if(allAppliancesOn.isEmpty()){
            System.out.println("Немає ввімкнених приладів. Загальна потужність = " + generalPower);
            return generalPower;
        }
        for(ElectricalAppliance app : allAppliancesOn){
            generalPower += app.getPower();
        }
        System.out.println("Загальна потужність = " + generalPower/1000 + "kW");
        return generalPower;
    }
    public void ShowAllAppliancesInRoom(){
        if(rooms.isEmpty()){
            System.out.println("В домі немає кімнат;");
            return;
        }
        System.out.println("Оберіть кімнату, в якій бажаєте переглянути усі прилади:");
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
                System.out.println("В домі немає приладів");
                if(logger!=null)
                    logger.info("Була спроба ввімкнути пристрій, але не було наявних пристроїв.");
                return;
            }
            for(ElectricalAppliance el : allAppliances){
                System.out.println(el.toString());
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
                    if(logger!=null)
                        logger.info("Пристрій не вдалося ввімкнути.\nПристрою з таким ID або не існує, або він уже ввімкнений.");
                }
                if(logger!=null)
                    logger.info("Пристрій було ввімкнено в розетку");
            return;
        }
        if( i == 2){
            if (!(rooms.isEmpty())) {
                int j = 1;
                for (Room r : rooms) {
                    System.out.println("Кімната№" + j + r.toString());
                    j++;
                }
                System.out.println("Введіть номер кімнати в якій ви хочете вимкнути пристрій");
                int roomIndex = SysFunc.get(1, rooms.size()) - 1;
                this.rooms.get(roomIndex).showAllAppliancesOn();
                if(this.rooms.get(roomIndex).getAppliancesOn().isEmpty()){
                    System.out.println("Немає ввімкнених пристроїв");
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
                    if(logger!=null)
                        logger.info("Пристрій не вдалося вимкнути.\nПристрою з таким ID або не існує, або він уже вимкнений.");
                }
                if(logger!=null)
                    logger.info("Пристрій було вимкнено.");
            return;
        }else{
                System.out.println("В домі немає приладів");
                if(logger!=null)
                    logger.info("Була спроба вимкнути прилад, але в домі немає приладів.");
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
                System.out.println("Введіть номер кімнати в якій ви хочете вимкнути пристрій з розетки");
                int roomIndex = SysFunc.get(1, rooms.size()) - 1;
                this.rooms.get(roomIndex).showAllPluggedAppliances();
                if(this.rooms.get(roomIndex).pluggedAppliances.isEmpty()){
                    System.out.println("Немає ввімкнених в розетку пристроїв");
                    if(logger!=null)
                        logger.info("Була спроба вимкнути пристрій з розетки, але ну бло ввімкнених в розетку приладів.");
                    return;
                }
                System.out.println("Введіть ID пристрою який ви хочете вимкнути з розетки");
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
                    System.out.println("Пристрою з таким ID або не існує, або він уже вимкнений.");
                    if(logger!=null)
                        logger.info("Була спроба вимкнути прилад з розетки, але пристрою з таким ID або не існує, або він уже вимкнений.");
                }
            }else{
                System.out.println("В домі немає приладів");
                if(logger!=null)
                    logger.info("Була спроба вимкнути прилад з розетки, але в будинку не було приладів.");
                return;
            }
            if(logger!=null)
                logger.info("Пристрій було вимкнено з розетки.");
            return;
        }

        if(i==4){
            if (!(rooms.isEmpty())) {
                int j = 1;
                for (Room r : rooms) {
                    System.out.println("Кімната№" + j + r.toString());
                    j++;
                }
                System.out.println("Введіть номер кімнати в якій ви хочете ввімкнути пристрій в розетку");
                int roomIndex = SysFunc.get(1, rooms.size()) - 1;
                if(this.rooms.get(roomIndex).CheckSocket() >= 0){
                    System.out.println("Немає вільних розеток");
                    if(logger!=null)
                        logger.info("Була спроба ввімкнути пристрій в розетку, але не було вільних розеток");
                    return;
                }
                boolean is = false;
                 for(ElectricalAppliance el : this.rooms.get(roomIndex).getAppliances()) {
                     if (el instanceof PluggedAppliance) {
                         System.out.println(el.toString());
                         is = true;
                     }
                 }
                 if(is == false){
                     System.out.println("Немає пристроїв на розетку");
                     if(logger!=null)
                         logger.info("Була спроба ввімкнути пристрій в розетку, але не було відповідних приладів");
                 return;}
                System.out.println("Введіть ID пристрою який ви хочете ввімкнути");
                i = sc.nextInt();
                for(ElectricalAppliance el : this.rooms.get(roomIndex).getAppliances()) {
                    if(el instanceof PluggedAppliance){
                        if(((PluggedAppliance) el).getID() == i && !((PluggedAppliance) el).isPlugged()){
                            ((PluggedAppliance) el).PlugIn();
                            this.rooms.get(roomIndex).pluggedAppliances.add((PluggedAppliance) el);
                            i = 0;
                            if(logger!=null)
                                logger.info("Пристрій було ввімкнено в розетку.");
                            return;
                        }
                    }
                }

            }
            else{
                System.out.println("В домі немає приладів");
                if(logger!=null)
                    logger.info("Була спроба ввімкнути пристрій в розетку, але не було відповідних приладів.");
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
        if(logger!=null)
            logger.info("Пристрої були відсортовані за потужністю.");
    }
    public void FindAppliancesByParams() {
        boolean isBattery, isPlugged, isOn;

        System.out.println("Пошук приладів за параметрами:");
        System.out.println("Цей пристрій безпровідний (на батареях)?\n1. Так\n2. Ні");
        int batteryChoice = sc.nextInt();

        isBattery = batteryChoice == 1;
        isPlugged = batteryChoice == 2;

        System.out.println("Цей пристрій ввімкнений?\n1. Так\n2. Ні");
        isOn = sc.nextInt() == 1;

        System.out.println("Введіть верхню межу потужності (в ватах):");
        double maxPower = sc.nextDouble();
        System.out.println("Введіть нижню межу потужності:");
        double minPower = sc.nextDouble();

        for (ElectricalAppliance el : allAppliances) {
            boolean powerOK = el.getPower() <= maxPower && el.getPower() >= minPower;
            boolean stateOK = el.isOn() == isOn;

            boolean typeMatches =
                    (isPlugged && (el instanceof PluggedAppliance || el instanceof AlwaysPlugedAppliance)) ||
                            (isBattery && el instanceof BatteryAppliance);

            if (powerOK && stateOK && typeMatches) {
                System.out.println(el.toString());
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
        System.out.println("Споживання за "+hour+"год = " + consumptionPerHour + "kWh");
        return consumptionPerHour;
    }
    public void ShoweAllAppliances(){
        for(ElectricalAppliance el : allAppliances){
            System.out.println(el.toString());
        }
    }
    public void ShoweAllAppliancesOn(){
        for(ElectricalAppliance el : allAppliancesOn){
            System.out.println(el.toString());
        }
    }
    public int checkAppliancesID(int id){
        for(int i = 0 ; i < allAppliances.size() ; i++ ){
            if(allAppliances.get(i).getID() == id){
                System.out.println("Прилад з таким ID вже існує.");
                if(logger!=null){
                    logger.warning("При спробі створення приладу, ID виявився не унікальним");
                }
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
    public ArrayList<Room> getRooms(){
        return rooms;
    }
    public ArrayList<ElectricalAppliance> getAllAppliancesOn(){
        return allAppliancesOn;
    }
    public void addRoom(Room room){
        rooms.add(room);
    }
    public void removeRoom(Room room){
        rooms.remove(room);
    }
    public void initTransient() {
        this.sc = SysFunc.SC;
        this.logger = Logger.getLogger(Home.class.getName());
        logger.info("Дім успішно відновлено з файлу.");
    }


}
