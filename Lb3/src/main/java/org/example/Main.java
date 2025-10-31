package org.example;
import Droids.*;
import Game.Game;


//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args){
        Game game = new Game();
        game.startGame();
        while(!game.isGameOver()) {
            game.MainMenu();
        }
    }
}