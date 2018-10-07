package com.reason.games.game;

public abstract class Game {

    protected abstract void initializeGame();

    protected abstract void playGame();

    protected abstract void endGame();

    public final void playOneGame() {
        initializeGame();
        playGame();
        endGame();
    }    
}
