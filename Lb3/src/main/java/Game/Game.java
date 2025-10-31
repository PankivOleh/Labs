package Game;
import Droids.*;

import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.random.*;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Game {
    static final Scanner scanner = new Scanner(System.in);
    boolean gameOver;
    Player  Player1;
    ArrayList<Player> players;
    int countOfPlayers;
    public Game(){
        this.countOfPlayers = 2;
        players = new ArrayList<>();
        createDefPlayers();
        gameOver = false;
    }

    public void startGame(){
        for (int i = 0; i < countOfPlayers; i++) {
            players.add(new Player("Player"+(i+1)));
        }
        Player1 = players.getFirst();
    }

    public ArrayList<Player> getPlayers() {return players;}

    protected void createDefPlayers(){
        String name;
        for (int i = 0; i < countOfPlayers; i++) {
            System.out.println("Введіть ім'я гравця:");
            name = scanner.nextLine();
            players.add(new Player(name));
        }
    }

     public static void DuelBattle(ArrayList<Player> players, Droid firstDroid, Droid secondDroid) {
    StringBuilder battleLog = new StringBuilder();
    Random random = new Random();
    // Заголовок бою
    String battleHeader = """
        ╔════════════════════════════════════════════════════════╗
        ║                    🤖 ДУЕЛЬ ДРОЇДІВ 🤖                  ║
        ╚════════════════════════════════════════════════════════╝
        """;
    
    System.out.println(battleHeader);
    battleLog.append(battleHeader).append("\n");
    
    String battleInfo = String.format("""
        ⚔️  %s (%s) VS %s (%s)
        
        📊 Характеристики:
        ┌─────────────────────────────────────────────────────────┐
        │ %s:
        │   ❤️  Здоров'я: %g
        │   ⚡ Атака: %g
        ├─────────────────────────────────────────────────────────┤
        │ %s:
        │   ❤️  Здоров'я: %g
        │   ⚡ Атака: %g
        └─────────────────────────────────────────────────────────┘
        
        """,
        players.get(0).getName(), firstDroid.getName(),
        players.get(1).getName(), secondDroid.getName(),
        firstDroid.getName(), firstDroid.getHp(), firstDroid.getDamage(),
        secondDroid.getName(), secondDroid.getHp(), secondDroid.getDamage()
    );
    
    System.out.println(battleInfo);
    battleLog.append(battleInfo).append("\n");
    
    // Визначаємо хто атакує першим
    int attackerIndex = random.nextInt(2);
    String startMessage = "🎲 Хто атакує перший: " + players.get(attackerIndex).getName() + "\n\n";
    System.out.println(startMessage);
    battleLog.append(startMessage).append("\n");
    
    Menu.pressEnterToContinue();
    
    // Проводимо раунди
    processBattleRounds(players, firstDroid, secondDroid, attackerIndex, battleLog);
}

private static void processBattleRounds(ArrayList<Player> players, Droid firstDroid, 
                                       Droid secondDroid, int attackerIndex, StringBuilder battleLog) {
    Droid attacker = attackerIndex == 0 ? firstDroid : secondDroid;
    Droid defender = attackerIndex == 0 ? secondDroid : firstDroid;
    Player attackerPlayer = players.get(attackerIndex);
    Player defenderPlayer = players.get(attackerIndex == 0 ? 1 : 0);
    
    int round = 1;
    Random random = new Random();
    
    while (firstDroid.isAlive() && secondDroid.isAlive()) {
        Menu.clearScreen();
        
        // Заголовок раунду
        String roundHeader = String.format("""
            ╔════════════════════════════════════════════════════════╗
            ║                     РАУНД #%d                           ║
            ╚════════════════════════════════════════════════════════╝
            """, round);
        
        System.out.println(roundHeader);
        battleLog.append(roundHeader).append("\n");
        
        // Інформація про поточний стан використовуючи toString()
        System.out.println("📊 Стан дроїдів:\n");
        battleLog.append("📊 Стан дроїдів:\n\n");
        
        System.out.println(firstDroid.toString());
        battleLog.append(firstDroid.toString()).append("\n");
        
        System.out.println(secondDroid.toString());
        battleLog.append(secondDroid.toString()).append("\n");
        
        // Атака
        String attackMessage = String.format("⚔️  %s (%s) атакує %s (%s)!\n",
            attacker.getName(), attackerPlayer.getName(),
            defender.getName(), defenderPlayer.getName());
        
        System.out.println(attackMessage);
        battleLog.append(attackMessage).append("\n");
        
        // Шанс промаху (10%)
        boolean isMiss = random.nextInt(100) < 10;
        double oldHealth = defender.getHp();
        double newHealth = defender.getHp();
        double actualDamage = oldHealth - newHealth;
        if (isMiss) {
            String missMessage = "💨 ПРОМАХ! Атака не вдалася!\n";
            System.out.println(missMessage);
            battleLog.append(missMessage).append("\n");
        }
        else {
            // Застосування шкоди
            oldHealth = defender.getHp();
            attacker.attack(defender);
            newHealth = defender.getHp();
            actualDamage = oldHealth - newHealth;
        }

        String damageMessage = String.format("""
            💢 Базова атака: %g
            🩸 Завдано шкоди: %g
            ❤️  Здоров'я %s: %g → %g
            
            """,
            attacker.getDamage(), actualDamage,
            defender.getName(), oldHealth, newHealth
        );
        
        System.out.println(damageMessage);
        battleLog.append(damageMessage).append("\n");
        
        // Перевірка на перемогу
        if (!defender.isAlive()) {
            String victoryMessage = String.format("""
                ╔════════════════════════════════════════════════════════╗
                ║                    🏆 ПЕРЕМОГА! 🏆                      ║
                ╚════════════════════════════════════════════════════════╝
                
                🎉 Переможець: %s (%s)
                🤖 Дроїд-переможець: %s
                ⚔️  Раундів: %d
                
                ☠️  %s (%s) знищено!
                
                """,
                attackerPlayer.getName(), attacker.getName(),
                attacker.getName(), round,
                defender.getName(), defenderPlayer.getName()
            );
            
            System.out.println(victoryMessage);
            battleLog.append(victoryMessage).append("\n");
            
            // Відновлення дроїдів після дуелі
            String reviveMessage = """
                
                ╔════════════════════════════════════════════════════════╗
                ║               💚 ВІДНОВЛЕННЯ ДРОЇДІВ 💚                 ║
                ╚════════════════════════════════════════════════════════╝
                
                🔧 Дроїди відновлюють здоров'я!
                
                """;
            System.out.print(reviveMessage);
            battleLog.append(reviveMessage);
            
            firstDroid.revive();
            secondDroid.revive();
            
            String reviveLog = String.format("""
                ✅ %s відновлено до %.1f HP
                ✅ %s відновлено до %.1f HP
                
                💫 Дроїди готові до нового бою!
                
                """, 
                firstDroid.getName(), firstDroid.getHp(),
                secondDroid.getName(), secondDroid.getHp());
            
            System.out.print(reviveLog);
            battleLog.append(reviveLog);
            
            break;
        }
        
        // Зміна атакуючого
        Droid temp = attacker;
        attacker = defender;
        defender = temp;
        
        Player tempPlayer = attackerPlayer;
        attackerPlayer = defenderPlayer;
        defenderPlayer = tempPlayer;
        
        round++;
        
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n");
        battleLog.append("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n\n");
        
        Menu.pressEnterToContinue();
    }
    
    // Збереження логу бою
    saveBattleLog(battleLog.toString(), players.get(0).getName(), players.get(1).getName());
}

private static void saveBattleLog(String battleLog, String player1Name, String player2Name) {
    try {
        // Створюємо ім'я файлу з датою та часом
        LocalDateTime now = LocalDateTime.now();
        String fileName = "C:\\Users\\Oleg\\Desktop\\Battle_Log_Duel_LB3.txt";
        
        
        FileWriter writer = new FileWriter(fileName);
        
        // Записуємо заголовок файлу
        writer.write("═══════════════════════════════════════════════════════════\n");
        writer.write("                    ЛОГ ДУЕЛІ ДРОЇДІВ\n");
        writer.write("═══════════════════════════════════════════════════════════\n");
        writer.write("Дата та час: " + now.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")) + "\n");
        writer.write("Гравці: " + player1Name + " VS " + player2Name + "\n");
        writer.write("═══════════════════════════════════════════════════════════\n\n");
        
        // Записуємо лог бою
        writer.write(battleLog);
        
        // Записуємо футер
        writer.write("\n═══════════════════════════════════════════════════════════\n");
        writer.write("                    КІНЕЦЬ ЛОГУ\n");
        writer.write("═══════════════════════════════════════════════════════════\n");
        
        writer.close();
        
        String successMessage = String.format("""
            
            ╔════════════════════════════════════════════════════════╗
            ║              📝 ЛОГ БОЮ ЗБЕРЕЖЕНО 📝                    ║
            ╚════════════════════════════════════════════════════════╝
            
            📄 Файл: %s
            ✅ Лог бою успішно записано!
            
            """, fileName);
        
        System.out.println(successMessage);
        
    } catch (IOException e) {
        System.out.println("❌ Помилка при збереженні логу бою: " + e.getMessage());
    }
    
    Menu.pressEnterToContinue();
}


    protected static void createNewDuelDroids(ArrayList<Player> players){
        System.out.println("\n┌────────────────────────────────────────────────────────┐");
        System.out.println("│            Гравець "+players.get(0).getName()+ "                             ");
        System.out.println("|────────────────────────────────────────────────────────|");
        players.getFirst().createDroid();
        System.out.println("┌────────────────────────────────────────────────────────┐");
        System.out.println("│              Гравець "+players.get(1).getName()+ "                            ");
        System.out.println("|────────────────────────────────────────────────────────|");
        players.get(1).createDroid();
        Droid firstDroid = players.getFirst().getDroids().getLast();
        Droid secondDroid = players.get(1).getDroids().getLast();
        DuelBattle(players, firstDroid , secondDroid);
    }

    protected static void selectExistingDroids(ArrayList<Player> players){
        if(players.getFirst().getDroids().isEmpty()||players.getFirst().getDroids().getFirst().isDead() ){
            System.out.println("Недостатньо дроїдів в гравця:"+players.getFirst().getName());
            players.getFirst().createDroid();
        }
        System.out.println("┌────────────────────────────────────────────────────────┐");
        System.out.println("│              Дроїди гравця "+players.get(0).getName()+ "                            ");
        System.out.println("|────────────────────────────────────────────────────────|");
        for(int i = 0 ; i < players.get(0).getDroids().size() ; i++){
            System.out.println(i+1+":\n");
            System.out.println(players.getFirst().getDroids().get(i).toString());
        }
        System.out.print("🎮 Ваш вибір: ");
        Droid firstDroid = players.getFirst().getDroids().get(Menu.getChoice(1 , players.getFirst().getDroids().size())-1);
        if(players.get(1).getDroids().isEmpty()){
            players.get(1).createDroid();
        }
        System.out.println("┌────────────────────────────────────────────────────────┐");
        System.out.println("│              Дроїди гравця "+players.get(1).getName()+ "                            ");
        System.out.println("|────────────────────────────────────────────────────────|");
        for(int i = 0 ; i < players.get(1).getDroids().size() ; i++){
            System.out.println(i+1+":\n");
            System.out.println(players.get(1).getDroids().get(i).toString());
        }
        Droid secondDroid = players.get(1).getDroids().get(Menu.getChoice(1 , players.get(1).getDroids().size())-1);
        DuelBattle(players , firstDroid , secondDroid );
    }

    protected void exit(){
        this.gameOver = true;
    }

    public  void duelMenu() {
        Menu.showDuelMenu();
        int choice = Menu.getChoice(1, 3);
        switch (choice) {
            case 1 -> createNewDuelDroids(this.players);
            case 2 -> selectExistingDroids(this.players);
            case 3 -> MainMenu();
        }
    }

    public void MainMenu() {
        Menu.showMainMenu();
        int i = Menu.getChoice(1, 4);
        switch (i) {
            case 1 -> {
                duelMenu();
            }
            case 2 -> {
                teamBattleMenu();
            }
            case 3 -> {
                exit();
            }
        }
    }

    public boolean isGameOver() {
        return gameOver;
    }


    public static void createNewTeams(ArrayList<Player> players) {
        int i;
        System.out.println("┌────────────────────────────────────────────────────────┐");
        System.out.println("│            Гравець "+players.get(0).getName()+ "                             ");
        System.out.println("|────────────────────────────────────────────────────────|");
        for( i = 0 ; i<4 ; i++){
            System.out.println("│                                                        │");
            System.out.println("                    Ствооріть дроїда #"+(i+1)+"                ");
            System.out.println("|────────────────────────────────────────────────────────|");
            players.getFirst().createDroid();
            players.getFirst().team.add(players.getFirst().getDroids().getLast());
        }
        System.out.println("┌────────────────────────────────────────────────────────┐");
        System.out.println("│            Гравець "+players.get(1).getName()+ "                             ");
        System.out.println("|────────────────────────────────────────────────────────|");
        for( i = 0 ; i<4 ; i++){
            System.out.println("│                                                        │");
            System.out.println("                    Ствооріть дроїда #"+(i+1)+"                ");
            System.out.println("|────────────────────────────────────────────────────────|");
            players.get(1).createDroid();
            players.get(1).team.add(players.get(1).getDroids().getLast());
        }
        teamBattle(players);
    }

    protected static void selectTeams(ArrayList<Player> players){
        final int REQUIRED_DROIDS = 4;

        // Перевірка та створення дроїдів для першого гравця
        int aliveCount = 0;
        for (Droid droid : players.get(0).getDroids()) {
            if (droid.isAlive()) {
                aliveCount++;
            }
        }
        
        if (aliveCount < REQUIRED_DROIDS) {
            int needToCreate = REQUIRED_DROIDS - aliveCount;
            System.out.println("┌────────────────────────────────────────────────────────┐");
            System.out.println("│  ⚠️  Гравець " + players.get(0).getName() + " має недостатньо живих дроїдів!    │");
            System.out.println("│  Поточна кількість живих: " + aliveCount + " | Потрібно: " + REQUIRED_DROIDS + "              │");
            System.out.println("└────────────────────────────────────────────────────────┘");
            System.out.println("\n🤖 Створіть ще " + needToCreate + " дроїд(ів):\n");

            for(int i = 0; i < needToCreate; i++){
                System.out.println("┌────────────────────────────────────────────────────────┐");
                System.out.println("│          Гравець " + players.get(0).getName() + " - Дроїд #" + (i+1) + " з " + needToCreate + "              │");
                System.out.println("└────────────────────────────────────────────────────────┘");
                players.get(0).createDroid();
                System.out.println("✅ Дроїда створено! Залишилось: " + (needToCreate - i - 1) + "\n");
            }

            System.out.println("✅ Гравець " + players.get(0).getName() + " тепер має достатньо дроїдів!\n");
            Menu.pressEnterToContinue();
        }

        // Перевірка та створення дроїдів для другого гравця
        aliveCount = 0;
        for (Droid droid : players.get(1).getDroids()) {
            if (droid.isAlive()) {
                aliveCount++;
            }
        }
        
        if (aliveCount < REQUIRED_DROIDS) {
            int needToCreate = REQUIRED_DROIDS - aliveCount;
            System.out.println("┌────────────────────────────────────────────────────────┐");
            System.out.println("│  ⚠️  Гравець " + players.get(1).getName() + " має недостатньо живих дроїдів!    │");
            System.out.println("│  Поточна кількість живих: " + aliveCount + " | Потрібно: " + REQUIRED_DROIDS + "              │");
            System.out.println("└────────────────────────────────────────────────────────┘");
            System.out.println("\n🤖 Створіть ще " + needToCreate + " дроїд(ів):\n");

            for(int i = 0; i < needToCreate; i++){
                System.out.println("┌────────────────────────────────────────────────────────┐");
                System.out.println("│          Гравець " + players.get(1).getName() + " - Дроїд #" + (i+1) + " з " + needToCreate + "              │");
                System.out.println("└────────────────────────────────────────────────────────┘");
                players.get(1).createDroid();
                System.out.println("✅ Дроїда створено! Залишилось: " + (needToCreate - i - 1) + "\n");
            }

            System.out.println("✅ Гравець " + players.get(1).getName() + " тепер має достатньо дроїдів!\n");
            Menu.pressEnterToContinue();
        }

        // Вибір команди для першого гравця
        Menu.clearScreen();
        System.out.println("┌────────────────────────────────────────────────────────┐");
        System.out.println("│              Дроїди гравця "+players.get(0).getName()+ "                            ");
        System.out.println("|────────────────────────────────────────────────────────|");

        for(int i = 0; i < players.get(0).getDroids().size(); i++){
            Droid droid = players.get(0).getDroids().get(i);
            if (droid.isAlive()) {
                System.out.println((i+1) + ":");
                System.out.println(droid.toString());
                System.out.println();
            } else {
                System.out.println((i+1) + ": ☠️ " + droid.getName() + " - ЗНИЩЕНО");
            }
        }

        System.out.println("Оберіть 4 живих дроїдів для команди:");
        for(int i = 0; i < 4; i++){
            System.out.print("🎮 Оберіть дроїда #" + (i+1) + ": ");
            int choice = Menu.getChoice(1, players.get(0).getDroids().size());
            Droid selectedDroid = players.get(0).getDroids().get(choice - 1);

            // Перевірка чи дроїд живий
            if (!selectedDroid.isAlive()) {
                System.out.println("⚠️ Цей дроїд знищений! Виберіть живого дроїда.");
                i--;
                continue;
            }

            // Перевірка чи дроїд вже обраний
            if(players.get(0).team.contains(selectedDroid)){
                System.out.println("⚠️ Цей дроїд вже обраний! Виберіть іншого.");
                i--;
                continue;
            }

            players.get(0).team.add(selectedDroid);
            System.out.println("✅ Дроїд " + selectedDroid.getName() + " додано до команди!");
        }

        System.out.println("\n✅ Команда гравця " + players.get(0).getName() + " сформована!\n");
        Menu.pressEnterToContinue();

        // Вибір команди для другого гравця
        Menu.clearScreen();
        System.out.println("┌────────────────────────────────────────────────────────┐");
        System.out.println("│              Дроїди гравця "+players.get(1).getName()+ "                            ");
        System.out.println("|────────────────────────────────────────────────────────|");

        for(int i = 0; i < players.get(1).getDroids().size(); i++){
            Droid droid = players.get(1).getDroids().get(i);
            if (droid.isAlive()) {
                System.out.println((i+1) + ":");
                System.out.println(droid.toString());
                System.out.println();
            } else {
                System.out.println((i+1) + ": ☠️ " + droid.getName() + " - ЗНИЩЕНО");
            }
        }

        System.out.println("Оберіть 4 живих дроїдів для команди:");
        for(int i = 0; i < 4; i++){
            System.out.print("🎮 Оберіть дроїда #" + (i+1) + ": ");
            int choice = Menu.getChoice(1, players.get(1).getDroids().size());
            Droid selectedDroid = players.get(1).getDroids().get(choice - 1);

            // Перевірка чи дроїд живий
            if (!selectedDroid.isAlive()) {
                System.out.println("⚠️ Цей дроїд знищений! Виберіть живого дроїда.");
                i--;
                continue;
            }

            // Перевірка чи дроїд вже обраний
            if(players.get(1).team.contains(selectedDroid)){
                System.out.println("⚠️ Цей дроїд вже обраний! Виберіть іншого.");
                i--;
                continue;
            }

            players.get(1).team.add(selectedDroid);
            System.out.println("✅ Дроїд " + selectedDroid.getName() + " додано до команди!");
        }

        System.out.println("\n✅ Команда гравця " + players.get(1).getName() + " сформована!\n");
        Menu.pressEnterToContinue();

        teamBattle(players);
    }

    public void teamBattleMenu(){
        Menu.showTeamButtleMenu();
        int i = Menu.getChoice(1, 3);
        switch (i) {
            case 1 -> {createNewTeams(this.players);}
            case 2 -> {selectTeams(this.players);}
            case 3 -> MainMenu();
        }
    }

    public static void teamBattle(ArrayList<Player> players) {
        StringBuilder battleLog = new StringBuilder();
        
        // Заголовок бою
        String battleHeader = """
            ╔════════════════════════════════════════════════════════════════╗
            ║                    ⚔️ КОМАНДНИЙ БІЙ ⚔️                         ║
            ╚════════════════════════════════════════════════════════════════╝
            """;
        
        System.out.println(battleHeader);
        battleLog.append(battleHeader).append("\n");
        
        // Інформація про команди
        String teamsInfo = String.format("""
            
            👥 Команда гравця %s:
            """, players.get(0).getName());
        
        System.out.println(teamsInfo);
        battleLog.append(teamsInfo).append("\n");
        
        for (int i = 0; i < players.get(0).team.size(); i++) {
            String droidInfo = String.format("   %d. %s - ❤️ %.1f HP | ⚔️ %.1f DMG\n", 
                i + 1, 
                players.get(0).team.get(i).getName(),
                players.get(0).team.get(i).getHp(),
                players.get(0).team.get(i).getDamage());
            System.out.print(droidInfo);
            battleLog.append(droidInfo);
        }
        
        teamsInfo = String.format("""
            
            👥 Команда гравця %s:
            """, players.get(1).getName());
        
        System.out.println(teamsInfo);
        battleLog.append(teamsInfo).append("\n");
        
        for (int i = 0; i < players.get(1).team.size(); i++) {
            String droidInfo = String.format("   %d. %s - ❤️ %.1f HP | ⚔️ %.1f DMG\n", 
                i + 1, 
                players.get(1).team.get(i).getName(),
                players.get(1).team.get(i).getHp(),
                players.get(1).team.get(i).getDamage());
            System.out.print(droidInfo);
            battleLog.append(droidInfo);
        }
        
        System.out.println("\n" + "═".repeat(64));
        battleLog.append("\n" + "═".repeat(64) + "\n");
        
        Menu.pressEnterToContinue();
        
        // Починаємо бій
        processTeamBattle(players, battleLog);
    }
    
    private static void processTeamBattle(ArrayList<Player> players, StringBuilder battleLog) {
        Random random = new Random();
        int currentPlayerIndex = random.nextInt(2); // Хто ходить першим
        int round = 1;
        
        String firstPlayerMessage = String.format("\n🎲 Перший хід робить: %s\n", 
            players.get(currentPlayerIndex).getName());
        System.out.println(firstPlayerMessage);
        battleLog.append(firstPlayerMessage).append("\n");
        Menu.pressEnterToContinue();
        
        // Основний цикл бою
        while (hasAliveDroids(players.get(0).team) && hasAliveDroids(players.get(1).team)) {
            Menu.clearScreen();
            
            Player currentPlayer = players.get(currentPlayerIndex);
            Player enemyPlayer = players.get(currentPlayerIndex == 0 ? 1 : 0);
            
            // Заголовок раунду
            String roundHeader = String.format("""
                ╔════════════════════════════════════════════════════════════════╗
                ║                        РАУНД #%d                                ║
                ║                   Хід гравця: %s                               
                ╚════════════════════════════════════════════════════════════════╝
                """, round, currentPlayer.getName());
            
            System.out.println(roundHeader);
            battleLog.append(roundHeader).append("\n");
            
            // Показуємо стан команд
            displayTeamsStatus(players, battleLog);
            
            // Гравець робить свої ходи (до 3 дій)
            performPlayerTurn(currentPlayer, enemyPlayer, battleLog);
            
            // Перевірка на перемогу
            if (!hasAliveDroids(enemyPlayer.team)) {
                displayVictory(currentPlayer, enemyPlayer, round, battleLog);
                break;
            }
            
            // Передаємо хід наступному гравцю
            currentPlayerIndex = currentPlayerIndex == 0 ? 1 : 0;
            round++;
            
            System.out.println("\n" + "═".repeat(64));
            System.out.println("Хід переходить до наступного гравця...");
            battleLog.append("\n" + "═".repeat(64) + "\n");
            battleLog.append("Хід переходить до наступного гравця...\n");
            
            Menu.pressEnterToContinue();
        }
        
        // Збереження логу
        saveTeamBattleLog(battleLog.toString(), players.get(0).getName(), players.get(1).getName());
    }
    
    private static boolean hasAliveDroids(ArrayList<Droid> team) {
        for (Droid droid : team) {
            if (droid.isAlive()) {
                return true;
            }
        }
        return false;
    }
    
    private static void displayTeamsStatus(ArrayList<Player> players, StringBuilder battleLog) {
        String status = "\n📊 СТАН КОМАНД:\n\n";
        System.out.print(status);
        battleLog.append(status);
        
        for (int p = 0; p < 2; p++) {
            String teamHeader = String.format("👥 %s:\n", players.get(p).getName());
            System.out.print(teamHeader);
            battleLog.append(teamHeader);
            
            for (int i = 0; i < players.get(p).team.size(); i++) {
                Droid droid = players.get(p).team.get(i);
                if (droid.isAlive()) {
                    System.out.println(droid.toString());
                    battleLog.append(droid.toString()).append("\n");
                } else {
                    String deadStatus = String.format("""
                        ╔════════════════════════════════════════
                        ║ ☠️  ЗНИЩЕНО: %s
                        ╚════════════════════════════════════════
                        """, droid.getName());
                    System.out.println(deadStatus);
                    battleLog.append(deadStatus).append("\n");
                }
            }
            System.out.println();
            battleLog.append("\n");
        }
        
        System.out.println("─".repeat(64) + "\n");
        battleLog.append("─".repeat(64) + "\n\n");
    }
    
    private static void performPlayerTurn(Player currentPlayer, Player enemyPlayer, StringBuilder battleLog) {
        Scanner scanner = new Scanner(System.in);
        
        // Гравець має 3 дії за хід
        for (int action = 1; action <= 3; action++) {
            System.out.println("╔════════════════════════════════════════════════════════════════╗");
            System.out.printf("║                    ДІЯ %d З 3                                    ║\n", action);
            System.out.println("╚════════════════════════════════════════════════════════════════╝\n");
            
            // Показуємо живих дроїдів гравця
            System.out.println("🤖 Ваші дроїди:");
            ArrayList<Droid> aliveDroids = new ArrayList<>();
            for (int i = 0; i < currentPlayer.team.size(); i++) {
                Droid droid = currentPlayer.team.get(i);
                if (droid.isAlive()) {
                    aliveDroids.add(droid);
                    String droidType = "";
                    if (droid instanceof AttackDroid) droidType = "⚔️";
                    else if (droid instanceof DefensiveDroid) droidType = "🛡️";
                    else if (droid instanceof RepairDroid) droidType = "🔧";
                    
                    System.out.printf("   %d. %s %s - ❤️ %.1f HP | ⚔️ %.1f DMG\n", 
                        aliveDroids.size(), droidType, droid.getName(), droid.getHp(), droid.getDamage());
                }
            }
            
            if (aliveDroids.isEmpty()) {
                System.out.println("❌ У вас не залишилось живих дроїдів!");
                break;
            }
            
            // Вибір дроїда
            System.out.print("\n🎮 Оберіть дроїда (1-" + aliveDroids.size() + "): ");
            int droidChoice = Menu.getChoice(1, aliveDroids.size());
            Droid selectedDroid = aliveDroids.get(droidChoice - 1);
            
            String actionLog = String.format("\n🤖 Обрано: %s\n", selectedDroid.getName());
            System.out.print(actionLog);
            battleLog.append(actionLog);
            
            // Вибір дії
            System.out.println("\n⚡ ЩО РОБИТИ?");
            System.out.println("   1. ⚔️ Атакувати ворожого дроїда");
            
            int maxOptions = 1;
            boolean canHeal = selectedDroid instanceof RepairDroid;
            boolean canTaunt = selectedDroid instanceof DefensiveDroid;
            
            if (canHeal) {
                maxOptions++;
                System.out.println("   " + maxOptions + ". 💚 Вилікувати союзника");
            }
            
            if (canTaunt) {
                maxOptions++;
                System.out.println("   " + maxOptions + ". 🎭 Насмішка (привертає ворожі атаки)");
            }
            
            System.out.print("\n🎮 Ваш вибір: ");
            int actionChoice = Menu.getChoice(1, maxOptions);
            
            if (actionChoice == 1) {
                // Атака
                performAttack(selectedDroid, enemyPlayer, currentPlayer, battleLog);
            } else if (canHeal && actionChoice == 2 && !canTaunt) {
                // Лікування (якщо немає насмішки)
                performHeal((RepairDroid) selectedDroid, currentPlayer, battleLog);
            } else if (canTaunt && actionChoice == 2 && !canHeal) {
                // Насмішка (якщо немає лікування)
                performTaunt((DefensiveDroid) selectedDroid, battleLog);
            } else if (canHeal && canTaunt) {
                // Обидві здібності доступні
                if (actionChoice == 2) {
                    performHeal((RepairDroid) selectedDroid, currentPlayer, battleLog);
                } else if (actionChoice == 3) {
                    performTaunt((DefensiveDroid) selectedDroid, battleLog);
                }
            }
            
            // Перевірка чи ворожа команда ще жива
            if (!hasAliveDroids(enemyPlayer.team)) {
                break;
            }
            
            if (action < 3) {
                System.out.println("\n" + "─".repeat(64));
                Menu.pressEnterToContinue();
                Menu.clearScreen();
                displayTeamsStatus(new ArrayList<>(List.of(currentPlayer, enemyPlayer)), battleLog);
            }
        }
    }
    
    private static void performAttack(Droid attacker, Player enemyPlayer, Player currentPlayer, StringBuilder battleLog) {
        // Перевіряємо чи є дроїд з активною насмішкою в ворожій команді
        DefensiveDroid enemyTaunter = null;
        for (Droid droid : enemyPlayer.team) {
            if (droid instanceof DefensiveDroid && droid.isAlive()) {
                DefensiveDroid defDroid = (DefensiveDroid) droid;
                if (defDroid.getTaunt() > 0) {
                    enemyTaunter = defDroid;
                    break;
                }
            }
        }
        
        Droid target;
        
        if (enemyTaunter != null) {
            // Якщо є ворожий дроїд з насмішкою, атакуємо його
            target = enemyTaunter;
            String forcedTargetLog = String.format("""
                
                🎭 %s використовує НАСМІШКУ!
                ⚠️ Атака автоматично спрямована на %s!
                
                """, enemyTaunter.getName(), enemyTaunter.getName());
            System.out.print(forcedTargetLog);
            battleLog.append(forcedTargetLog);
        } else {
            // Показуємо ворожих дроїдів
            System.out.println("\n🎯 ЦІЛІ ДЛЯ АТАКИ:");
            ArrayList<Droid> aliveEnemies = new ArrayList<>();
            for (int i = 0; i < enemyPlayer.team.size(); i++) {
                Droid droid = enemyPlayer.team.get(i);
                if (droid.isAlive()) {
                    aliveEnemies.add(droid);
                    String droidType = "";
                    if (droid instanceof AttackDroid) droidType = "⚔️";
                    else if (droid instanceof DefensiveDroid) droidType = "🛡️";
                    else if (droid instanceof RepairDroid) droidType = "🔧";
                    
                    System.out.printf("   %d. %s %s - ❤️ %.1f HP\n", 
                        aliveEnemies.size(), droidType, droid.getName(), droid.getHp());
                }
            }
            
            System.out.print("\n🎮 Оберіть ціль (1-" + aliveEnemies.size() + "): ");
            int targetChoice = Menu.getChoice(1, aliveEnemies.size());
            target = aliveEnemies.get(targetChoice - 1);
        }
        
        // Виконуємо атаку
        double oldHp = target.getHp();
        Random random = new Random();
        boolean isMiss = random.nextInt(100) < 10; // 10% шанс промаху
        
        String attackLog = String.format("\n⚔️ %s атакує %s!\n", 
            attacker.getName(), target.getName());
        System.out.print(attackLog);
        battleLog.append(attackLog);
        
        if (isMiss) {
            String missLog = "💨 ПРОМАХ! Атака не вдалася!\n";
            System.out.print(missLog);
            battleLog.append(missLog);
        } else {
            boolean targetDied = attacker.attack(target);
            double newHp = target.getHp();
            double damage = oldHp - newHp;
            
            String damageLog = String.format("""
                💥 Завдано шкоди: %.1f
                ❤️ Здоров'я %s: %.1f → %.1f
                """, damage, target.getName(), oldHp, newHp);
            System.out.print(damageLog);
            battleLog.append(damageLog);
            
            if (targetDied) {
                String deathLog = String.format("☠️ %s ЗНИЩЕНО!\n", target.getName());
                System.out.print(deathLog);
                battleLog.append(deathLog);
            }
        }
        
        // Зменшуємо лічильник насмішки тільки після атаки
        if (enemyTaunter != null && enemyTaunter.isAlive()) {
            enemyTaunter.useTaunt(1);
            if (enemyTaunter.getTaunt() > 0) {
                String tauntRemainingLog = String.format("🎭 Насмішка %s ще активна (%d атак залишилось)\n", 
                    enemyTaunter.getName(), enemyTaunter.getTaunt());
                System.out.print(tauntRemainingLog);
                battleLog.append(tauntRemainingLog);
            } else {
                String tauntEndLog = String.format("🎭 Насмішка %s закінчилась\n", enemyTaunter.getName());
                System.out.print(tauntEndLog);
                battleLog.append(tauntEndLog);
                enemyTaunter.stopTaunt();
            }
        }
        
        Menu.pressEnterToContinue();
    }
    
    private static void performHeal(RepairDroid healer, Player currentPlayer, StringBuilder battleLog) {
        // Показуємо союзників для лікування
        System.out.println("\n💚 ДРОЇДИ ДЛЯ ЛІКУВАННЯ:");
        ArrayList<Droid> woundedAllies = new ArrayList<>();
        for (int i = 0; i < currentPlayer.team.size(); i++) {
            Droid droid = currentPlayer.team.get(i);
            if (droid.isAlive() && droid.getHp() < droid.getMaxHp()) {
                woundedAllies.add(droid);
                System.out.printf("   %d. %s - ❤️ %.1f/%.1f HP\n", 
                    woundedAllies.size(), droid.getName(), droid.getHp(), droid.getMaxHp());
            }
        }
        
        if (woundedAllies.isEmpty()) {
            String noWoundedLog = "✅ Всі союзники мають повне здоров'я!\n";
            System.out.print(noWoundedLog);
            battleLog.append(noWoundedLog);
            Menu.pressEnterToContinue();
            return;
        }
        
        System.out.print("\n🎮 Оберіть дроїда для лікування (1-" + woundedAllies.size() + "): ");
        int healChoice = Menu.getChoice(1, woundedAllies.size());
        Droid target = woundedAllies.get(healChoice - 1);
        
        double oldHp = target.getHp();
        healer.repair(target);
        double newHp = target.getHp();
        double healed = newHp - oldHp;
        
        String healLog = String.format("""
            
            🔧 %s лікує %s!
            💚 Відновлено: %.1f HP
            ❤️ Здоров'я %s: %.1f → %.1f
            """, 
            healer.getName(), target.getName(), 
            healed, target.getName(), oldHp, newHp);
        System.out.print(healLog);
        battleLog.append(healLog);
        
        Menu.pressEnterToContinue();
    }
    
    private static void displayVictory(Player winner, Player loser, int rounds, StringBuilder battleLog) {
        String victoryMessage = String.format("""
            
            ╔════════════════════════════════════════════════════════════════╗
            ║                      🏆 ПЕРЕМОГА! 🏆                            ║
            ╚════════════════════════════════════════════════════════════════╝
            
            🎉 ПЕРЕМОЖЕЦЬ: %s
            
            📊 СТАТИСТИКА БОЮ:
            ⚔️ Раундів: %d
            
            👥 Команда переможця (%s):
            """, winner.getName(), rounds, winner.getName());
        
        System.out.println(victoryMessage);
        battleLog.append(victoryMessage).append("\n");
        
        for (Droid droid : winner.team) {
            System.out.println(droid.toString());
            battleLog.append(droid.toString()).append("\n");
        }
        
        String loserInfo = String.format("""
            
            👥 Команда переможеного (%s):
            """, loser.getName());
        System.out.print(loserInfo);
        battleLog.append(loserInfo);
        
        for (Droid droid : loser.team) {
            if (droid.isAlive()) {
                System.out.println(droid.toString());
                battleLog.append(droid.toString()).append("\n");
            } else {
                String deadStatus = String.format("""
                    ╔════════════════════════════════════════
                    ║ ☠️  ЗНИЩЕНО: %s
                    ╚════════════════════════════════════════
                    """, droid.getName());
                System.out.println(deadStatus);
                battleLog.append(deadStatus).append("\n");
            }
        }
        
        System.out.println("\n" + "═".repeat(64));
        battleLog.append("\n" + "═".repeat(64) + "\n");
        
        // Відновлення всіх дроїдів після бою
        reviveAllDroids(winner, loser, battleLog);
        
        Menu.pressEnterToContinue();
    }
    
    private static void saveTeamBattleLog(String battleLog, String player1Name, String player2Name) {
        try {
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
            String timestamp = now.format(formatter);
            String fileName = String.format("C:\\Users\\Oleg\\Desktop\\Battle_Log_Team_LB3.txt.txt");
            
            FileWriter writer = new FileWriter(fileName);
            
            writer.write("═══════════════════════════════════════════════════════════\n");
            writer.write("                 ЛОГ КОМАНДНОГО БОЮ\n");
            writer.write("═══════════════════════════════════════════════════════════\n");
            writer.write("Дата та час: " + now.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")) + "\n");
            writer.write("Гравці: " + player1Name + " VS " + player2Name + "\n");
            writer.write("═══════════════════════════════════════════════════════════\n\n");
            
            writer.write(battleLog);
            
            writer.write("\n═══════════════════════════════════════════════════════════\n");
            writer.write("                    КІНЕЦЬ ЛОГУ\n");
            writer.write("═══════════════════════════════════════════════════════════\n");
            
            writer.close();
            
            String successMessage = String.format("""
                
                ╔════════════════════════════════════════════════════════════════╗
                ║              📝 ЛОГ КОМАНДНОГО БОЮ ЗБЕРЕЖЕНО 📝                 ║
                ╚════════════════════════════════════════════════════════════════╝
                
                📄 Файл: %s
                ✅ Лог бою успішно записано!
                
                """, fileName);
            
            System.out.println(successMessage);
            
        } catch (IOException e) {
            System.out.println("❌ Помилка при збереженні логу бою: " + e.getMessage());
        }
        
        Menu.pressEnterToContinue();
    }

    private static void reviveAllDroids(Player winner, Player loser, StringBuilder battleLog) {
        String reviveHeader = """
            
            ╔════════════════════════════════════════════════════════════════╗
            ║               💚 ВІДНОВЛЕННЯ ДРОЇДІВ 💚                        ║
            ╚════════════════════════════════════════════════════════════════╝
            
            🔧 Всі дроїди відновлюють здоров'я та повертаються до строю!
            
            """;
        
        System.out.print(reviveHeader);
        battleLog.append(reviveHeader);
        
        // Відновлюємо команду переможця
        System.out.println("👥 Команда " + winner.getName() + ":");
        battleLog.append("👥 Команда " + winner.getName() + ":\n");
        
        for (Droid droid : winner.team) {
            droid.revive();
            String reviveLog = String.format("   ✅ %s відновлено до %.1f HP\n", 
                droid.getName(), droid.getHp());
            System.out.print(reviveLog);
            battleLog.append(reviveLog);
        }
        
        System.out.println();
        battleLog.append("\n");
        
        // Відновлюємо команду переможеного
        System.out.println("👥 Команда " + loser.getName() + ":");
        battleLog.append("👥 Команда " + loser.getName() + ":\n");
        
        for (Droid droid : loser.team) {
            droid.revive();
            String reviveLog = String.format("   ✅ %s відновлено до %.1f HP\n", 
                droid.getName(), droid.getHp());
            System.out.print(reviveLog);
            battleLog.append(reviveLog);
        }
        
        System.out.println("\n💫 Усі дроїди готові до нового бою!\n");
        battleLog.append("\n💫 Усі дроїди готові до нового бою!\n\n");
    }

    private static void performTaunt(DefensiveDroid taunter, StringBuilder battleLog) {
        taunter.taunt();
        
        String tauntLog = String.format("""
            
            🎭 %s використовує НАСМІШКУ!
            🛡️ Наступні 2 ворожі атаки будуть спрямовані на %s!
            
            """, taunter.getName(), taunter.getName());
        
        System.out.print(tauntLog);
        battleLog.append(tauntLog);
        
        Menu.pressEnterToContinue();
    }
}
