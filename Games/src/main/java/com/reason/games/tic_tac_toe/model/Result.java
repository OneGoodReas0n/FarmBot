package com.reason.games.tic_tac_toe.model;

public class Result {

    private boolean status;
    private int symbol;

    public Result() {
    }

    public Result(boolean status, int symbol) {
        this.status = status;
        this.symbol = symbol;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public int getSymbol() {
        return symbol;
    }

    public void setSymbol(int symbol) {
        this.symbol = symbol;
    }

}
