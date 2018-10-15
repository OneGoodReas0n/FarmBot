package com.reason.games.farm.functional;

import com.reason.games.farm.enums.Status;
import com.reason.games.farm.model.GameField;
import com.reason.games.farm.factory.FarmGameFactory;
import com.reason.games.farm.model.Cell;
import com.reason.games.farm.model.Plant;
import com.reason.games.farm.model.Player;
import com.reason.games.farm.repository.CellRepository;
import com.reason.games.farm.repository.PlayerRepository;
import com.reason.games.farm.specification.CellSpecification;
import java.util.ArrayList;
import java.util.List;

public class FarmGame {

    private final static FarmGame instance = new FarmGame();
    private final FarmGameFactory factory = FarmGameFactory.getInstance();
    private final PlayerRepository playerRepository = FarmGameFactory.createPlayerRepository();
    private final CellRepository cellRepository = FarmGameFactory.createCellRepository();
    private final List<Thread> growingThreads = new ArrayList<>();

    public FarmGame() {
    }

    public static FarmGame getInstance() {
        return instance;
    }

    public Cell getOneCellFromRepository(GameField field, int num) {
        List<Cell> cells = cellRepository.getQuery(CellSpecification.whereFieldIs(field));
        Cell cell = cells.get(num - 1);
        if (cell != null) {
            return cell;
        }
        return null;
    }

    public boolean isCellFree(Cell cell) {
        if (cell.getStatus() == Status.FREE) {
            return true;
        } else {
            return false;
        }
    }

    public void makePlantation(Plant plant, Cell cell, Player player, GameField field) {
        player.setBalance(player.getBalance() - plant.getCost());
        playerRepository.update(player);
        cell.setPlant(plant);
        cell.setStatus(Status.OCCUPIED);
        cellRepository.update(cell);
    }

    public boolean canBuyPlant(Player player, Plant plant) {
        if (player.getBalance() < plant.getCost()) {
            return false;
        }
        return true;

    }

    public boolean cantBuyPlant(Player player, PlantsShop shop) {
        return player.getBalance() < shop.getMinCostPlant();
    }

    public boolean noFreeCells(GameField field) {
        boolean flag = true;
        for (Cell newCell : cellRepository.getQuery(CellSpecification.whereFieldIs(field))) {
            if (newCell.getStatus() == Status.FREE) {
                flag = false;
            }
        }
        return flag;
    }

    //GET HARVEST     3-BUTTON INTERFACE
    public void getProfit(Player player, GameField field, Cell cell) {
        player.setBalance(player.getBalance() + calculateProfit(cell));
        cell.clear();
        playerRepository.update(player);
        cellRepository.update(cell);
    }

    private double calculateProfit(Cell cell) {
        return cell.getPlant().getProfit();
    }

    // GROWING PLANTS
    public void startGrowingThreads(GameField field) {
        for (Cell cell : cellRepository.getQuery(CellSpecification.whereFieldIs(field))) {
            if (cell.getStatus() == Status.OCCUPIED || cell.getStatus() == Status.ALLMOSTREADY) {
                Thread thread = getGrowingThread(cell);
                thread.start();
                this.growingThreads.add(thread);
            }
        }
    }

    public boolean isPlantGrown(GameField field) {
        boolean flag = false;
        for (Cell cell : cellRepository.getQuery(CellSpecification.whereFieldIs(field))) {
            if (cell.getStatus() == Status.READY) {
                flag = true;
                cellRepository.update(cell);
            }
        }
        return flag;
    }

    public Thread getGrowingThread(Cell cell) {
        return new Thread(new Runnable() {
            @Override
            public void run() {
                growingProcess(cell);
            }
        });
    }

    private void growingProcess(Cell cell) {
        while (cell.getLeftTime() != 0) {
            try {
                Thread.sleep(1000);
                cell.setLeftTime(cell.getLeftTime() - 1);
                if (cell.getStatus() != Status.ALLMOSTREADY || cell.getStatus() != Status.READY) {
                    if (cell.getLeftTime() != 0) {
                        if (cell.getLeftTime() < cell.getPlant().getGrowthTime() / 2) {
                            cell.setStatus(Status.ALLMOSTREADY);
                        }
                    }
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        cell.setStatus(Status.READY);
        cellRepository.update(cell);
    }
}
