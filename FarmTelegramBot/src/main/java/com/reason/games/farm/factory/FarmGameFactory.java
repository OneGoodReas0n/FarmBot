package com.reason.games.farm.factory;

import com.reason.bot.info.StaticInfo;
import com.reason.games.farm.functional.FarmGame;
import com.reason.games.farm.model.GameField;
import com.reason.games.farm.model.Cell;
import com.reason.games.farm.functional.PlantsShop;
import com.reason.games.farm.model.Plant;
import com.reason.games.farm.model.Player;
import com.reason.games.farm.repository.CellRepository;
import com.reason.games.farm.repository.FieldRepository;
import com.reason.games.farm.repository.PlayerRepository;

public class FarmGameFactory {

    private final static FarmGameFactory instance = new FarmGameFactory();

    public static FarmGameFactory getInstance() {
        return instance;
    }
    
    public static FarmGame createFarmGame(){
        return FarmGame.getInstance();
    }
    
    public static PlantsShop createPlantsShop(){
        return PlantsShop.getInstance();
    }
    
    public Plant createPlant(String name, double price, double profit, int growthTime, String alternativeName){
        return new Plant(name, price, profit, growthTime, alternativeName);
    }
    
    public Plant createPlant(){
        return new Plant();
    }
    
    public Player createPlayer(String name){
        return new Player(name);
    }
    
    public Cell createCell(){
        return new Cell();
    }
    
    public GameField createGameField(){
        return new GameField();
    }
    
    public static PlayerRepository createPlayerRepository(){
        return PlayerRepository.getInstance();
    }
    
    public static CellRepository createCellRepository(){
        return CellRepository.getInstance();
    }
    
    public static FieldRepository createFieldRepository(){
        return FieldRepository.getInstance();
    }
    
    public static StaticInfo createStaticInfo(){
        return StaticInfo.getInstance();
    }
}
