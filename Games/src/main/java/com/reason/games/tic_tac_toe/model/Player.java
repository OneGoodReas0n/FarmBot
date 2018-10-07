package com.reason.games.tic_tac_toe.model;

public class Player {
    
    private String name;
    private int symbol=0;

    public Player() {
    }

    public Player(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSymbol() {
        return symbol;
    }

    public void setSymbol(int symbol) {
        this.symbol = symbol;
    }
    
    public String showSymbol(int symbol){
        switch(symbol){
            case 1:
                return "X";
            case 2: 
                return "O";
        }
        return null;
    }
}
