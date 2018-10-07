package com.reason.games.farm.functional;

import com.reason.games.farm.factory.FarmGameFactory;
import com.reason.games.farm.model.Plant;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.DirectoryIteratorException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class PlantsShop {

    private final static PlantsShop instance = new PlantsShop();
    private final Map<String, Plant> plantsMap = new HashMap<>();
    private final Path fileDirectory = Paths.get("d:\\JavaProjects\\Games\\src\\main\\java\\com\\reason\\games\\farm\\plants");
    private final FarmGameFactory factory = FarmGameFactory.getInstance();

    public PlantsShop() {
        init();
    }

    public static PlantsShop getInstance() {
        return instance;
    }

    private void init() {
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(fileDirectory)) {
            JSONParser parser = new JSONParser();
            for (Path file : stream) {
                try {
                    JSONObject data = (JSONObject) parser.parse(new FileReader(file.toString()));
                    String name = new String(((String) data.get("name")).getBytes(), "utf-8");
                    double price = new Double((String) data.get("price"));
                    double profit = new Double((String) data.get("profit"));
                    int growthTime = new Integer((String) data.get("growthTime"));
                    plantsMap.put(name, factory.createPlant(name, price, profit, growthTime));
                } catch (FileNotFoundException e) {
                    System.err.println(e.getMessage());
                } catch (IOException | ParseException ex) {
                    System.err.println(ex.getMessage());
                }
            }
        } catch (IOException | DirectoryIteratorException x) {
            // IOException can never be thrown by the iteration.
            // In this snippet, it can only be thrown by newDirectoryStream.
            System.err.println(x);
        }

    }

    public Map<String, Plant> getPlantsMap() {
        return plantsMap;
    }

    public void showAllPlants() {
        for (Map.Entry<String, Plant> entry : plantsMap.entrySet()) {
            Plant value = entry.getValue();
            System.out.println(value.toString());
        }
    }

    public Plant getOneByName(String name) {
        for (Map.Entry<String, Plant> entry : plantsMap.entrySet()) {
            String key = entry.getKey();
            if (key.equalsIgnoreCase(name)) {
                return entry.getValue();
            }
        }
        return null;
    }
    
    public double getMinCostPlant(){
        double min=100;
        for (Map.Entry<String, Plant> entry : plantsMap.entrySet()) {
            Plant value = entry.getValue();
            if(min>value.getCost()){
                min = value.getCost();
            }
        }
        return min;
    }
}
