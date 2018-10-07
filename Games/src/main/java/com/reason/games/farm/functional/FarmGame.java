package com.reason.games.farm.functional;

import com.reason.games.farm.enums.Status;
import com.reason.games.farm.model.GameField;
import com.reason.games.farm.factory.FarmGameFactory;
import com.reason.games.farm.model.Cell;
import com.reason.games.farm.model.Plant;
import com.reason.games.farm.model.Player;
import com.reason.games.farm.repository.CellRepository;
import com.reason.games.farm.repository.FieldRepository;
import com.reason.games.farm.repository.PlayerRepository;
import com.reason.games.farm.specification.CellSpecification;
import com.reason.games.farm.specification.GameFieldSpecification;
import com.reason.games.game.Game;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FarmGame extends Game {

    private final static FarmGame instance = new FarmGame();
    private final Scanner scan = new Scanner(System.in, "utf-8");
    private final FarmGameFactory factory = FarmGameFactory.getInstance();
    private final PlantsShop shop = FarmGameFactory.createPlantsShop();
    private final PlayerRepository playerRepository = FarmGameFactory.createPlayerRepository();
    private final FieldRepository fieldRepository = FarmGameFactory.createFieldRepository();
    private final CellRepository cellRepository = FarmGameFactory.createCellRepository();
    private final List<Thread> growingThreads = new ArrayList<>();
    private Cell cell;
    private Player player;
    private GameField field;

    public FarmGame() {
    }

    public static FarmGame getInstance() {
        return instance;
    }

    @Override
    protected void initializeGame() {
        System.out.println("Добро пожаловать в игру Ферма!");
        System.out.println("Введите свое имя: ");
        String name = scan.nextLine().toLowerCase();
        if (playerRepository.findByName(name) != null) {
            player = playerRepository.findByName(name);
            field = fieldRepository.findById(player.getField().getId());
            welcomeInfo(player, "С возвращением %s !\n");
            startGrowingThreads(field);
        } else {
            field = factory.createGameField();
            player = factory.createPlayer(name);
            field.setPlayer(player);
            player.setField(field);
            for (int i = 0; i < 8; i++) {
                Cell cell = factory.createCell();
                cell.setField(field);
                cellRepository.add(cell);
            }
            fieldRepository.update(field);
            welcomeInfo(player, "%s добро пожаловать в новую игру!\n");
        }
    }

    @Override
    protected void playGame() {
        gameInterface(player, field);
    }

    @Override
    protected void endGame() {

    }

    public void gameInterface(Player player, GameField field) {
        System.out.println("Интерфейс пользователя: ");
        System.out.println("\n1-Посмотреть информацию по клетке");
        System.out.println("2-Посадить растение (выбрать растение и выбрать клетку)");
        System.out.println("3-Собрать урожай (выбрать клетку на которой созрело растение)");
        System.out.println("4-Выйти из приложения");
        List<Cell> cells = field.getCells();
        switch (scan.nextInt()) {
            case 1:
                showCellsInfo(field, player);
                break;
            case 2:
                makePlant(player, field);
                gameInterface(player, field);
                break;
            case 3:
                toHarvest(player, field);
                break;
            default:
                try {
                    stopAllGrowingThreads();
                    saveCellState(cells);
                } catch (InterruptedException e) {
                    System.err.println(e);
                }

                break;
        }
    }

    private void showCellsInfo(GameField field, Player player) {
        isPlantGrown(field);
        showPlantation(player, field);
        int i = 1;
        do {
            System.out.println("Введите номер ячейки (от 1 до 8), 0- для выхода в главное меню");
            i = scan.nextInt();
            if (interruptLoop(i)) {
                cell = getOneCellFromRepository(field, i);
                cell.showInfo();
            }
        } while (interruptLoop(i));
        gameInterface(player, field);
    }

    private void makePlant(Player player, GameField field) {
        isPlantGrown(field);
        System.out.println("Добро пожаловать в меню посадки растения");
        System.out.println("Ваш баланс: " + player.getBalance());
        showPlantation(player, field);
        if (cantBuyPlant(player, shop)) {
            System.out.println("На вашем аккаунте недостаточно денег для покупки растения! "
                    + "Подождите пока что-нибудь созреет.");
        }
        if(noFreeCells(field)){
            System.out.println("Нет свободных ячеек, подождите пока что-нибудь созреет");
        }
        else {
            System.out.println("Для посадки нужно ввести название растения и номер клетки (от 1 до 8)");
            System.out.println("Список растений: ");
            shop.showAllPlants();
            chooseAndBuy(player, field, cell);
            Thread thread = getGrowingThread(cell);
            thread.start();
            growingThreads.add(thread);
        }
    }

    private void toHarvest(Player player, GameField field) {
        isPlantGrown(field);
        showPlantation(player, field);
        boolean flag = true;
        if (getReadyCellList(field).size() > 0) {
            do {
                System.out.println("Введите номер ячейки: (от 1 до 8)");
                int num = scan.nextInt();
                Cell cell = getOneCellFromRepository(field, num);
                for (Cell cellfromlist : getReadyCellList(field)) {
                    if (cell.equals(cellfromlist)) {
                        getProfit(player, field, cell);
                        flag = false;
                        System.out.println(String.format("Урожай с ячейки %d собран", num));
                    }
                }
            } while (flag);
            showPlantation(player, field);
            System.out.println("Нажмите любую клавишу для выхода в главное меню:");
            switch (scan.next()) {
                default:
                    gameInterface(player, field);
            }
        } else {
            System.out.println("Ни одно из растений еще не созрело! Вернитесь пожалуйста позже");
            exitButton(player, field);
        }
    }

    //START PAGE
    private void welcomeInfo(Player player, String str) {
        System.out.println(String.format(str, player.getName().substring(0, 1).toUpperCase()
                + player.getName().substring(1)));
    }

    //SHOW CELLS INFO   1-BUTTON INTERFACE
    private void showPlantation(Player player, GameField field) {
        List<GameField> fields = fieldRepository.getQuery(GameFieldSpecification.whereFieldIs(player));
        field = fields.get(0);
        field.showGameField();
    }

    private boolean interruptLoop(int num) {
        return !(num < 1 || num > Cell.COUNT);
    }

    //MAKE PLANTATION   2-BUTTON INTERFACE
    private Plant choosePlant() {
        Plant plant;
        do {
            System.out.println("Выберите растение: ");
            plant = shop.getOneByName(scan.next());
            if (checkOnNullPlant(plant)) {
                System.out.println("Неверный ввод названия, попробуйте еще раз");
            }
        } while (checkOnNullPlant(plant));
        return plant;
    }

    private boolean checkOnNullPlant(Plant plant) {
        return plant == null;
    }

    private boolean checkOnNullCell(Cell cell) {
        return cell == null;
    }

    private Cell chooseFreeCell(GameField field) {
        Cell cell = null;
        do {
            System.out.println("Выберите ячейку (от 1 до 8) ");
            int num = scan.nextInt();
            if (num >= 1 && num <= Cell.COUNT) {
                if (getOneCellFromRepository(field, num).getStatus() == Status.FREE && checkOnNullCell(cell)) {
                    cell = getOneCellFromRepository(field, num);
                } else {
                    System.out.println("Неверный ввод, либо такой ячейки нет, "
                            + "либо она уже занята попробуйте еще раз");
                }
            }
        } while (checkOnNullCell(cell));
        return cell;
    }

    private List<Cell> getReadyCellList(GameField field) {
        List<Cell> cells = cellRepository.getQuery(CellSpecification.whereFieldIs(field));
        List<Cell> readyCells = new ArrayList<>();
        cells.stream().filter((cell) -> (cell.getStatus() == Status.READY)).forEachOrdered((cell) -> {
            readyCells.add(cell);
        });
        return readyCells;
    }

    private void chooseAndBuy(Player player, GameField field, Cell cell) {
        Plant plant = choosePlant();
        if (canBuyPlant(player, plant)) {
            cell = chooseFreeCell(field);
            setCell(cell);
            makePlantation(plant, cell, player, field);
            System.out.println("Растение " + plant.getName() + " посажено в ячейку");
        } else {
            System.out.println("У вас недостаточно денег для покупки этого растения");
            chooseAndBuy(player, field, cell);
        }
    }

    private Cell getOneCellFromRepository(GameField field, int num) {
        List<Cell> cells = cellRepository.getQuery(CellSpecification.whereFieldIs(field));
        Cell cell = cells.get(num - 1);
        if (cell != null) {
            return cell;
        }
        return null;
    }

    private void makePlantation(Plant plant, Cell cell, Player player, GameField field) {
        playerRepository.update(player);
        cell.setPlant(plant);
        cell.setStatus(Status.OCCUPIED);
        cellRepository.update(cell);
    }

    private boolean canBuyPlant(Player player, Plant plant) {
        if (player.getBalance() < plant.getCost()) {
            return false;
        }
        player.setBalance(player.getBalance() - plant.getCost());
        return true;

    }

    private boolean cantBuyPlant(Player player, PlantsShop shop) {
        return player.getBalance() < shop.getMinCostPlant();
    }

    private boolean noFreeCells(GameField field) {
        boolean flag = true;
        for (Cell newCell : cellRepository.getQuery(CellSpecification.whereFieldIs(field))) {
            if(newCell.getStatus()==Status.FREE){
                flag = false;
            }
        }
        return flag;
    }

    //GET HARVEST     3-BUTTON INTERFACE
    private void getProfit(Player player, GameField field, Cell cell) {
        player.setBalance(player.getBalance() + calculateProfit(cell));
        cell.clear();
        playerRepository.update(player);
        cellRepository.update(cell);
    }

    private double calculateProfit(Cell cell) {
        return cell.getPlant().getProfit();
    }

    private void exitButton(Player player, GameField field) {
        System.out.println("Нажмите любую клавишу для выхода в главное меню:");
        switch (scan.next()) {
            default:
                gameInterface(player, field);
        }
    }

    private void setCell(Cell cell) {
        this.cell = cell;
    }

    // GROWING PLANTS
    private void startGrowingThreads(GameField field) {
        List<Thread> threads = new ArrayList<>();
        for (Cell cell : cellRepository.getQuery(CellSpecification.whereFieldIs(field))) {
            if (cell.getStatus() == Status.OCCUPIED) {
                Thread thread = getGrowingThread(cell);
                thread.start();
                this.growingThreads.add(thread);
            }
        }
    }

    private void isPlantGrown(GameField field) {
        for (Cell cell : cellRepository.getQuery(CellSpecification.whereFieldIs(field))) {
            if (cell.getStatus() == Status.READY) {
                cellRepository.update(cell);
            }
        }
    }

    private Thread getGrowingThread(Cell cell) {
        return new Thread(new Runnable() {
            @Override
            public void run() {
                growingProcess(cell);
            }
        });
    }

    private void saveCellState(List<Cell> cells) {
        for (Cell cell : cells) {
            cellRepository.update(cell);
        }
    }

    private void growingProcess(Cell cell) {
        while (cell.getLeftTime()!=0) {
            try {
                Thread.sleep(1000);
                cell.setLeftTime(cell.getLeftTime() - 1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        cell.setStatus(Status.READY);
        cellRepository.update(cell);
    }

    private void stopAllGrowingThreads() throws InterruptedException {
        for (Thread thread : growingThreads) {
            thread.stop();
        }
    }
}
