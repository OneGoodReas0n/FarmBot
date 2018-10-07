package com.reason.games.tic_tac_toe.functional;

import com.reason.games.tic_tac_toe.model.Result;
import com.reason.games.tic_tac_toe.model.Cell;
import com.reason.games.tic_tac_toe.factory.TicTacToeGameFactory;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Field {

    private final static Field instance = new Field();
    private final TicTacToeGameFactory factory = new TicTacToeGameFactory();
    private final Map<String, Cell> field = new LinkedHashMap<>();

    public Field() {
        init();
    }
    
    public void init(){
        field.put("1x1", factory.createCell());
        field.put("1x2", factory.createCell());
        field.put("1x3", factory.createCell());
        field.put("2x1", factory.createCell());
        field.put("2x2", factory.createCell());
        field.put("2x3", factory.createCell());
        field.put("3x1", factory.createCell());
        field.put("3x2", factory.createCell());
        field.put("3x3", factory.createCell());
    }

    public static Field getInstance() {
        return instance;
    }

    public final Map<String, Cell> getField() {
        return field;
    }

    public void showField() {
        int i = 0;
        Map<String, Cell> map = getField();
        for (Map.Entry<String, Cell> entry : map.entrySet()) {
            entry.getValue().showPlaceholder();
            i++;
            if (i % 3 == 0) {
                System.out.println("");
            }
        }
    }

    public void clearField() {
        Map<String, Cell> map = getField();
        map.entrySet().stream().map((entry)
                -> entry.getValue()).forEachOrdered((value) -> {
            value.setStatus(0);
        });
    }

    public boolean fullField() {
        int counter = 0;

        counter = field.entrySet().stream().map((entry)
                -> entry.getValue()).filter((value)
                -> (value.getStatus() != 0)).map((_item)
                -> 1).reduce(counter, Integer::sum);
        return counter == 9;
    }

    public Result checkGameField(Map<String, Cell> map) throws NullPointerException {
        List<Integer> diagonalSymbolList = new ArrayList<>();
        List<Integer> ortagonalSymbolList = new ArrayList<>();
        int k = 3;
        for (int j = 1; j <= 3; j++) {
            List<Integer> horizontalSymbolList = new ArrayList<>();
            List<Integer> verticalSymbolList = new ArrayList<>();
            for (int i = 1; i <= 3; i++) {
                //Row searching...
                verticalSymbolList.add(map.get(i + "x" + j).getStatus());
                //Column searching...
                horizontalSymbolList.add(map.get(j + "x" + i).getStatus());
            }
            if (winCondition(verticalSymbolList).getStatus()) {
                return winCondition(verticalSymbolList);
            } else if (winCondition(horizontalSymbolList).getStatus()) {
                return winCondition(horizontalSymbolList);
            }
            // Diagonal searching...
            diagonalSymbolList.add(map.get(j + "x" + j).getStatus());
            ortagonalSymbolList.add(map.get(j + "x" + k).getStatus());
            k--;
        }
        if (winCondition(diagonalSymbolList).getStatus()) {
            return winCondition(diagonalSymbolList);
        } else if (winCondition(ortagonalSymbolList).getStatus()) {
            return winCondition(ortagonalSymbolList);
        }
        return factory.createResult(false, 3);
    }

    private Result winCondition(List<Integer> list) throws NullPointerException{
        boolean flag = false;
        int prototype = list.get(0);
        if (prototype != 0) {
            for (int i = 1; i < list.size(); i++) {
                if(prototype!=list.get(i)){
                    return factory.createResult(flag, 0);
                }
            }
            flag = true;
        }
        return factory.createResult(flag, prototype);
    }
}
