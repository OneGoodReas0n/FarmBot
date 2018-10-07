package com.reason.games.tic_tac_toe;

import com.reason.games.tic_tac_toe.factory.TicTacToeGameFactory;
import com.reason.games.tic_tac_toe.functional.History;
import com.reason.games.tic_tac_toe.functional.Menu;

public class Application {

    public static void main(String[] args) {
        
        History history = TicTacToeGameFactory.createHistory();
        Menu menu = TicTacToeGameFactory.getMenu();
        menu.showGameInterface();
    }
}
