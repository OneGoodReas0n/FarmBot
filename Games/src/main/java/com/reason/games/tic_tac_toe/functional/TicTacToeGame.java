package com.reason.games.tic_tac_toe.functional;

import com.reason.games.tic_tac_toe.model.Player;
import com.reason.games.tic_tac_toe.model.Cell;
import com.reason.games.tic_tac_toe.enums.Strings;
import com.reason.games.game.Game;
import com.reason.games.tic_tac_toe.factory.TicTacToeGameFactory;
import java.util.Map;
import java.util.Scanner;

public class TicTacToeGame extends Game{
    
    

    private final static TicTacToeGame instance = new TicTacToeGame();
    private final Scanner scan = new Scanner(System.in, "utf-8");
    private TicTacToeGameFactory factory;
    private Field field;
    private History history;
    private Info info;
    private int turn = 1;
    private Player lastPlayer;
    private Player futurePlayer;

    public TicTacToeGame() {
    }

    public static TicTacToeGame getInstance() {
        return instance;
    }

    @Override
    protected void playGame() {
        field.clearField();
        System.out.println(info.getPhrase(Strings.GAMEISSTARTED));
        System.out.println(info.getPhrase(Strings.ENTERPLAYER1NAME));
        Player player1 = factory.createPlayer(scan.nextLine());
        System.out.println(info.getPhrase(Strings.ENTERPLAYER2NAME));
        Player player2 = factory.createPlayer(scan.nextLine());
        Player[] players = {player1, player2};
        System.out.println(info.getPhrase(Strings.GAMEINFO));
        gameProcess(players);
    }
    
    @Override
    protected void initializeGame() {
        factory = TicTacToeGameFactory.getFactory();
        history = TicTacToeGameFactory.createHistory();
        field = TicTacToeGameFactory.createGameField();
        info = TicTacToeGameFactory.createInfo();
    }

    @Override
    protected void endGame() {}
    
    public void restartGame(Player[] players) {
        field.clearField();
        turn = 1;
        gameProcess(players);
    }

    private void gameProcess(Player[] players) {
        System.out.println("Приветствуем " + players[0].getName() + " и " + players[1].getName() + " в игре!\n");
        firstMove(players);
        turnProcess(players);
        field.showField();
        String result = gameResult(field.getField(), players);
        System.out.println(result);
        if (!"Ничья!".equals(result)) {
            saveResultInHistory(players, field.getField(), result);
        }
        getExtraGame(players);
    }

    private void saveResultInHistory(Player[] players, Map<String, Cell> map, String result) {
        history.updateResults(getResultForm(players, map, result));
    }

    private String getResultForm(Player[] players, Map<String, Cell> map, String result) {
        StringBuilder sb = new StringBuilder();
        sb.append("Игроки ");
        for (Player player : players) {
            sb.append(player.getName()).append("(").append(player.showSymbol(player.getSymbol())).append(") ");
        }
        sb.append(System.getProperty("line.separator"));
        int i = 0;
        for (Map.Entry<String, Cell> entry : map.entrySet()) {
            sb.append(entry.getValue().getStatusIcon()).append(" ");
            i++;
            if (i % 3 == 0) {
                sb.append(System.getProperty("line.separator"));
            }
        }
        sb.append(result);
        sb.append(System.getProperty("line.separator"));
        return sb.toString();
    }

    private void makeMove(String str, Player player) {
        Map<String, Cell> cellMap = field.getField();
        assignSymbol(str, cellMap, player.getSymbol());
    }

    private void firstMove(Player[] players) {
        if ((int) (Math.random() * 10) % 2 == 0) {
            players[0].setSymbol(1);
            players[1].setSymbol(2);
            futurePlayer = players[0];
        } else {
            players[1].setSymbol(1);
            players[0].setSymbol(2);
            futurePlayer = players[1];
        }
    }

    private Player changeTurn(Player[] players) {
        if (turn == 1) {
            return assignTurnPlayers(players[0].getSymbol(),players);
        } else {
            return swapPlayers(players);
        }

    }

    private void getInfo() {
        System.out.println("Ход игрока " + futurePlayer.getName() + "(" + futurePlayer.showSymbol(futurePlayer.getSymbol()) + ")");
    }

    private String gameResult(Map<String, Cell> map, Player[] players) {
        return defineResult(map, players);
    }

    private void turnProcess(Player[] players) {
        do {
            field.showField();
            getInfo();
            String str = scan.next();
            makeMove(str, changeTurn(players));
            System.out.println("");
        } while (!field.fullField() && !field.checkGameField(field.getField()).getStatus());
    }

    private void getExtraGame(Player[] players) {
        System.out.println(info.getPhrase(Strings.RETRY));
        int answer = scan.nextInt();
        System.out.println(answer);
        switch (answer) {
            case 1:
                restartGame(players);
            default:
                break;
        }
    }

    private void assignSymbol(String str, Map<String, Cell> cellMap, int symbol) {
        for (Map.Entry<String, Cell> elem : cellMap.entrySet()) {
            if (elem.getKey().equals(str)) {
                if (elem.getValue().getStatus() == 0) {
                    if (symbol == 1) {
                        elem.getValue().makeX();
                    } else {
                        elem.getValue().makeO();
                    }
                } else {
                    System.out.println("Неверное действие, ход переходит к следующему игроку");
                }
            }
        }
    }

    private Player assignTurnPlayers(int symbol,Player[] players) {
        turn++;
        if (symbol == 1) {
            lastPlayer = players[0];
            futurePlayer = players[1];
            return players[0];
        } else {
            lastPlayer = players[1];
            futurePlayer = players[0];
            return players[1];
        }

    }

    private Player swapPlayers(Player[] players) {
        if (lastPlayer.equals(players[0])) {
            lastPlayer = players[1];
            futurePlayer = players[0];
            return players[1];
        } else {
            lastPlayer = players[0];
            futurePlayer = players[1];
            return players[0];
        }
    }

    private String defineResult(Map<String, Cell> map, Player[] players) {
        int symbol = field.checkGameField(map).getSymbol();
        for (Player player : players) {
            if (player.getSymbol() == symbol) {
                return "Игрок " + player.getName() + " выиграл!";
            }
        }
        return "Ничья!";
    }

}
