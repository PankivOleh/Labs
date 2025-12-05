package org.example;

import Home.*;
import Menu.Menu;
import Utils.*;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static final Logger logger = Logger.getLogger(Main.class.getName());
    public static void main(String[] args) throws IOException {

        try {
            // 1. Налаштовуємо логування (створиться файл app.log)
            LogSetup.setup();
            logger.info("=== Програма запущена ===");
            Home home = new Home();
            HomeWrapper homeWrapper = new HomeWrapper(home);
            while (true) {
                if (Menu.MainMenu(homeWrapper) == 0) {
                    break;
                }
            }
        } catch (IOException e) {
            EmailSender.sendFatalError("Програма впала з помилкою: " + e.getMessage(), e);
            System.err.println("Критична помилка: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            EmailSender.sendFatalError("Програма впала з помилкою: " + e.getMessage(), e);
            logger.log(Level.SEVERE, "Сталася неочікувана помилка", e);
        } finally {
            logger.info("=== Програма зупинена ===");
        }
    }
}
