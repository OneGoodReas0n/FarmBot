package com.reason.games.farm;

import com.reason.games.farm.factory.FarmGameFactory;
import com.reason.games.farm.functional.FarmGame;
import com.reason.games.farm.repository.PlayerRepository;

public class Application {
    
    public static void main(String[] args) {
        
        FarmGame game = FarmGameFactory.createFarmGame();
        game.playOneGame();
    }
}
