package org.example;

import Home.Home;
import Menu.Menu;
//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        Home home = new Home();
        while(true){
            if(Menu.MainMenu(home) == 0){
                break;
            }
        }
    }
}