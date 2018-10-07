package com.reason.games.tic_tac_toe.model;

import java.util.HashMap;
import java.util.Map;

public class Cell {
    
    private final Map<Integer, String> statusMap = new HashMap<>();
    private int status;

    public Cell() {
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
    
    public void makeX(){
        this.status = 1;
    }
    
    public void makeO(){
        this.status = 2;
    }
    public void clear(){
        this.status = 0;
    }

    public final Map<Integer, String> getStatusMap() {
        statusMap.put(0, "*");
        statusMap.put(1, "X");
        statusMap.put(2, "O");
        return statusMap;
    }
    
    public void showPlaceholder(){
        for (Map.Entry<Integer,String> elem : getStatusMap().entrySet()) {
            if(elem.getKey().equals(this.status)){
                System.out.print(elem.getValue()+" ");
            }
        }
    }
    
    public String getStatusIcon(){
        for (Map.Entry<Integer,String> elem : getStatusMap().entrySet()) {
            if(elem.getKey().equals(this.status)){
                return elem.getValue();
            }
        }
        return null;
    }
    
}