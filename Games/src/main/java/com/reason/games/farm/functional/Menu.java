package com.reason.games.farm.functional;

public class Menu {

    private final static Menu instance = new Menu();
    
    public Menu() {
    }
    
    

    public static Menu getInstance() {
        return instance;
    }
    
    
}
