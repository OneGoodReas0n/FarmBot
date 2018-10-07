package com.reason.games.tic_tac_toe.functional;

import com.reason.games.tic_tac_toe.enums.Strings;
import com.reason.games.tic_tac_toe.factory.TicTacToeGameFactory;
import java.util.Scanner;

public class Menu {

    private final static Menu instance = new Menu();
    private final Scanner scan = new Scanner(System.in);
    private TicTacToeGame game;
    private Info info;
    private History history;

    public Menu() {
        init();
    }
    
    public void init(){
        game = TicTacToeGameFactory.createGame();
        info = TicTacToeGameFactory.createInfo();
        history = TicTacToeGameFactory.createHistory();
    }

    public static Menu getInstance() {
        return instance;
    }

    public void showGameInterface() {
        System.out.println(info.getPhrase(Strings.GAMENAME));
        System.out.println(info.getPhrase(Strings.USERINTERFACE));
        System.out.println(info.getPhrase(Strings.STARTGAME));
        System.out.println(info.getPhrase(Strings.SEEGAMEHISTORY));
        System.out.println(info.getPhrase(Strings.EXIT));
        System.out.println(info.getPhrase(Strings.YOURCHOICE));
        int choose = scan.nextInt();

        switch (choose) {
            case 1:
                startGame();
                break;
            case 2:
                showHistory();
                break;
            case 3:
                break;
        }
    }
    
    public void startGame(){
        game.playOneGame();
    }
    
    public void showHistory() {
        System.out.println(info.getPhrase(Strings.LABELGAMEHISTORY));
        System.out.println(history.getData());
        System.out.println(info.getPhrase(Strings.RETURNTOMAINMENU));
        String choose = scan.next();
        switch (choose) {
            default:
                TicTacToeGameFactory.getMenu().showGameInterface();
        }
    }
}
