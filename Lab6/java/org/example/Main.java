package org.example;

import Home.*;
import Menu.Menu;
import java.io.IOException;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws IOException {
        Home home = new Home();
        HomeWrapper homeWrapper = new HomeWrapper(home);
        while(true){
            if(Menu.MainMenu(homeWrapper) == 0){
                break;
            }
        }
    }
}