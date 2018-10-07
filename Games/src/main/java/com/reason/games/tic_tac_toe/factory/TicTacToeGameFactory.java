package com.reason.games.tic_tac_toe.factory;

import com.reason.games.tic_tac_toe.model.Cell;
import com.reason.games.tic_tac_toe.functional.Field;
import com.reason.games.tic_tac_toe.functional.TicTacToeGame;
import com.reason.games.tic_tac_toe.functional.History;
import com.reason.games.tic_tac_toe.functional.Info;
import com.reason.games.tic_tac_toe.functional.Menu;
import com.reason.games.tic_tac_toe.model.Player;
import com.reason.games.tic_tac_toe.model.Result;

public class TicTacToeGameFactory {
    
    private final static TicTacToeGameFactory factory = new TicTacToeGameFactory();

    public static TicTacToeGameFactory getFactory() {
        return factory;
    }
    
    public static Menu getMenu(){
        return Menu.getInstance();
    }
    
    public Cell createCell(){
        return new Cell();
    }
    
    public Player createPlayer(String name){
        return new Player(name);
    }
    
    public static Field createGameField(){
        return Field.getInstance();
    }
    
    public static History createHistory(){
        return History.getInstance();
    }
    
    public static TicTacToeGame createGame(){
        return TicTacToeGame.getInstance();
    }
    
    public static Info createInfo(){
        return Info.getInstance();
    }
    
    public Result createResult(boolean status, int symbol){
        return new Result(status,symbol);
    }
    
}
