package com.reason.bot.functional;

import com.reason.bot.info.StaticInfo;
import com.reason.games.farm.enums.Info;
import com.reason.games.farm.enums.Status;
import com.reason.games.farm.factory.FarmGameFactory;
import com.reason.games.farm.functional.FarmGame;
import com.reason.games.farm.functional.PlantsShop;
import com.reason.games.farm.model.Cell;
import com.reason.games.farm.model.GameField;
import com.reason.games.farm.model.Plant;
import com.reason.games.farm.model.Player;
import com.reason.games.farm.repository.CellRepository;
import com.reason.games.farm.repository.FieldRepository;
import com.reason.games.farm.repository.PlayerRepository;
import com.reason.games.farm.specification.CellSpecification;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendSticker;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class TelegramBot extends TelegramLongPollingBot {

    private final static TelegramBot instance = new TelegramBot();
    private final FarmGameFactory factory = FarmGameFactory.getInstance();
    private final PlantsShop shop = FarmGameFactory.createPlantsShop();
    private final PlayerRepository playerRepository = FarmGameFactory.createPlayerRepository();
    private final FieldRepository fieldRepository = FarmGameFactory.createFieldRepository();
    private final CellRepository cellRepository = FarmGameFactory.createCellRepository();
    private final List<Thread> growingThreads = new ArrayList<>();
    private final FarmGame game = FarmGameFactory.createFarmGame();
    private final StaticInfo info = FarmGameFactory.createStaticInfo();
    private Map<Info, String> phrases = new HashMap<>();
    private final Map<Info, String> phrasesAll = info.getPhrases("");
    private String lang = "";
    private Cell cell;
    private Plant plant;
    private Player player;
    private GameField field;
    private int numCell = -1;

    @Override
    public String getBotToken() {
        return StaticInfo.BOT_TOKEN();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            Message message = update.getMessage();

            if (message.getText().equals("/start")) {
                startReply(message);
            }

            if (message.getText().startsWith("/name")) {
                String name = message.getText().replaceAll("/name", "").trim();
                if (!name.equals("")) {
                    initializeGame(name.trim());
                    welcomeText(message, name, phrases);
                    sendMsgForm(message, phrases.get(Info.GAMERULES));
                    userInterface(message, phrases);
                } else {
                    sendMsgForm(message, phrases.get(Info.WRONGINPUTNAME));
                }
            } else {
                //sendMsgForm(message, phrases.get(Info.WRONGINPUT));
            }
        } else if (update.hasCallbackQuery()) {
            CallbackQuery query = update.getCallbackQuery();
            String text = query.getData();
            //LANGUAGE SETUP
            if (lang.isEmpty()) {
                if (text.equalsIgnoreCase("ru")) {
                    setLang("ru", query.getMessage());
                }
                if (text.equalsIgnoreCase("en")) {
                    setLang("en", query.getMessage());
                }
            }

            //INTERFACE BUTTONS
            if (text.equalsIgnoreCase("Back")) {
                userInterface(query.getMessage(), phrases);
            }
            if (numCell != -1 && text.equalsIgnoreCase("leftCell")) {
                if (numCell == 1) {
                    numCell = 8;
                    PlantCellForm(query, numCell);
                } else {
                    numCell--;
                    PlantCellForm(query, numCell);
                }
            }
            if (numCell != -1 && text.equalsIgnoreCase("rightCell")) {
                if (numCell == 8) {
                    numCell = 1;
                    PlantCellForm(query, numCell);
                } else {
                    numCell++;
                    PlantCellForm(query, numCell);
                }
            }

            //SECTION COLUMN INFO
            if (text.equalsIgnoreCase(phrasesAll.get(Info.CALLCOLUMNINFO))) {
                columnCellInfoSection(query.getMessage());
            }
            if (text.startsWith("cell")) {
                if (text.split(" ")[1].equals("info")) {
                    numCell = Integer.parseInt(text.split(" ")[2]);
                    PlantCellForm(query, numCell);
                }
                if (text.split(" ")[1].equals("select")) {
                    numCell = Integer.parseInt(text.split(" ")[2]);
                    Cell cell = game.getOneCellFromRepository(field, numCell);
                    if (game.noFreeCells(field)) {
                        sendMsgForm(query.getMessage(), phrases.get(Info.NOFREECELLS));
                        userInterface(query.getMessage(), phrases);
                    }
                    if (!game.isCellFree(cell)) {
                        sendMsgForm(query.getMessage(), phrases.get(Info.CELLNOTFREE));
                        selectCell(query.getMessage());
                    }
                    if (cell.getStatus() == Status.FREE) {
                        game.makePlantation(getPlant(), cell, player, field);
                        Thread thread = game.getGrowingThread(cell);
                        thread.start();
                        growingThreads.add(thread);
                        sendMsgForm(query.getMessage(), phrases.get(Info.PLANTSUCCESSFUL) + numCell);
                        delay();
                        userInterface(query.getMessage(), phrases);
                    }

                }
                if (text.split(" ")[1].equals("harvest")) {
                    numCell = Integer.parseInt(text.split(" ")[2]);
                    Cell cell = game.getOneCellFromRepository(field, numCell);
                    double profit = cell.getPlant().getProfit();
                    game.getProfit(player, field, cell);
                    sendMsgForm(query.getMessage(), phrases.get(Info.PROFITISGOT));
                    sendMsgForm(query.getMessage(), phrases.get(Info.INCOME) + profit);
                    sendMsgForm(query.getMessage(), phrases.get(Info.BALANCE) + player.round(player.getBalance(), 2));
                    delay();
                    userInterface(query.getMessage(), phrases);
                }

            }

            //SECTION MAKE PLANTATION
            if (text.equalsIgnoreCase(phrasesAll.get(Info.CALLMAKEPLANTATION))) {
                if (game.cantBuyPlant(player, shop)) {
                    sendMsgForm(query.getMessage(), phrases.get(Info.NOTHAVEMONEYANYPLANT));
                    userInterface(query.getMessage(), phrases);
                } else {
                    makePlantationSection(query.getMessage());
                }
            }

            if (text.startsWith("plant")) {
                String namePlant = text.replace("plant:", "");
                plant = shop.getOneByName(namePlant);
                if (game.canBuyPlant(player, plant)) {
                    setPlant(plant);
                    selectCell(query.getMessage());
                } else {
                    sendMsgForm(query.getMessage(), phrases.get(Info.DONTHAVEMONEY));
                    delay();
                    userInterface(query.getMessage(), phrases);
                }
            }
            //SECTION HARVEST THE PLANTS
            if (text.equalsIgnoreCase(phrasesAll.get(Info.CALLHARVEST))) {
                if (game.isPlantGrown(field)) {
                    double allProfit = 0;
                    for (Cell cell : cellRepository.getQuery(CellSpecification.whereFieldIs(field))) {
                        if (cell.getPlant() != null) {
                            allProfit += cell.getPlant().getProfit();
                            game.getProfit(player, field, cell);
                        }
                    }
                    sendMsgForm(query.getMessage(), phrases.get(Info.PROFITISGOT));
                    sendMsgForm(query.getMessage(), phrases.get(Info.INCOME) + allProfit);
                    sendMsgForm(query.getMessage(), phrases.get(Info.BALANCE) + player.round(player.getBalance(), 2));
                    userInterface(query.getMessage(), phrases);
                } else {
                    sendMsgForm(query.getMessage(), phrases.get(Info.NOREADYPLANT));
                    delay();
                    userInterface(query.getMessage(), phrases);
                }
            }
        }
    }

    @Override
    public String getBotUsername() {
        return StaticInfo.BOT_NAME();
    }

    public static TelegramBot getInstance() {
        return instance;
    }

    //DEFAULT METHODS
    public void initializeGame(String name) {
        if (playerRepository.findByName(name) != null) {
            player = playerRepository.findByName(name);
            field = fieldRepository.findById(player.getField().getId());
            game.startGrowingThreads(field);
        } else {
            field = factory.createGameField();
            player = factory.createPlayer(name);
            field.setPlayer(player);
            player.setField(field);
            for (int i = 0; i < 8; i++) {
                Cell cell = factory.createCell();
                cell.setField(field);
                cellRepository.add(cell);
                field.setGameCells(cell);
            }
            fieldRepository.update(field);
        }
    }

    private void startReply(Message message) {
        SendMessage sendMessage = new SendMessage(message.getChatId(), phrasesAll.get(Info.CHOOSELANGUAGE));
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> cols = new ArrayList<>();
        cols.add(new InlineKeyboardButton("ru").setCallbackData("ru"));
        cols.add(new InlineKeyboardButton("en").setCallbackData("en"));
        rows.add(cols);
        markup.setKeyboard(rows);
        sendMessage.setReplyMarkup(markup);
        send(sendMessage);
    }

    private void sendMsgForm(Message message, String string) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId());
        sendMessage.setText(string);
        send(sendMessage);
    }

    private void welcomeText(Message message, String name, Map<Info, String> pharases) {
        SendMessage sendMessage = new SendMessage(message.getChatId(), phrases.get(Info.HELLOTEXT) + name);
        send(sendMessage);
    }

    private void send(SendMessage msg) {
        try {
            execute(msg);
        } catch (TelegramApiException e) {
            System.err.println(e);
        }
    }

    private void sendSticker(SendSticker sticker) {
        try {
            execute(sticker);
        } catch (TelegramApiException e) {
            System.err.println(e);
        }
    }

    private void delay() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void setLang(String str, Message message) {
        lang = str;
        phrases = info.getPhrases(str);
        sendMsgForm(message, phrases.get(Info.LANGINTERFACE));
        delay();
        sendMsgForm(message, phrases.get(Info.WELCOMETEXT));
    }

    //INTERFACE METHODS
    private void userInterface(Message message, Map<Info, String> phrases) {
        SendMessage msg = new SendMessage();
        msg.setChatId(message.getChatId());
        msg.setText(phrases.get(Info.INTERFACETEXT));
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> col1 = new ArrayList<>();
        List<InlineKeyboardButton> col2 = new ArrayList<>();
        List<InlineKeyboardButton> col3 = new ArrayList<>();
        col1.add(new InlineKeyboardButton(phrases.get(Info.INTERFACESEESTATUSCOLUMN)).setCallbackData(phrasesAll.get(Info.CALLCOLUMNINFO)));
        col2.add(new InlineKeyboardButton(phrases.get(Info.INTERFACEMAKEPLANTATION)).setCallbackData(phrasesAll.get(Info.CALLMAKEPLANTATION)));
        col3.add(new InlineKeyboardButton(phrases.get(Info.INTERFACEGETPROFIT)).setCallbackData(phrasesAll.get(Info.CALLHARVEST)));
        rows.add(col1);
        rows.add(col2);
        rows.add(col3);
        markup.setKeyboard(rows);
        msg.setReplyMarkup(markup);
        send(msg);

    }

    //SECTION ------ CELL INFO METHODS
    private void columnCellInfoSection(Message message) {
        SendMessage msg = new SendMessage();
        msg.setChatId(message.getChatId());
        msg.setText(phrases.get(Info.CHOOSECELL));
        msg.setReplyMarkup(keyboardCellInfo());
        send(msg);
    }

    private InlineKeyboardMarkup keyboardCellInfo() {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> col1 = new ArrayList<>();
        List<InlineKeyboardButton> col2 = new ArrayList<>();
        List<InlineKeyboardButton> col3 = new ArrayList<>();
        if (field.getCells().size() % 2 == 0) {
            for (int i = 1; i < field.getCells().size()+1; i++) {
                if (i <= field.getCells().size()/2) {
                    col1.add(new InlineKeyboardButton("" + i).setCallbackData("cell info " + i));
                } else {
                    col2.add(new InlineKeyboardButton("" + i).setCallbackData("cell info " + i));
                }
            }
        }
        col3.add(new InlineKeyboardButton(phrasesAll.get(Info.LEFT)).setCallbackData("leftCell"));
        col3.add(new InlineKeyboardButton(phrases.get(Info.BACK)).setCallbackData("Back"));
        col3.add(new InlineKeyboardButton(phrasesAll.get(Info.RIGHT)).setCallbackData("rightCell"));
        rows.add(col1);
        rows.add(col2);
        rows.add(col3);
        markup.setKeyboard(rows);
        return markup;
    }

    private void PlantCellForm(CallbackQuery query, int id) {
        cell = game.getOneCellFromRepository(field, id);
        sendStickerForm(query.getMessage(), cell);
        sendMsgForm(query.getMessage(), phrases.get(Info.CELLNUMBER) + id);
        sendMsgInfoAndTryToHarvest(query.getMessage(), cell.showInfo(phrases), id);
        columnCellInfoSection(query.getMessage());
    }

    private void sendStickerForm(Message message, Cell cell) {
        SendSticker sendSticker = new SendSticker();
        if (cell.getStatus() == Status.FREE) {
            sendSticker.setSticker("CAADAgADAQADFJJmENh3SW43ehV2Ag");
        }
        if (cell.getStatus() == Status.OCCUPIED) {
            sendSticker.setSticker("CAADAgADAgADFJJmECcBy9RSqZ-0Ag");
        }
        if (cell.getStatus() == Status.ALLMOSTREADY) {
            sendSticker.setSticker("CAADAgADAwADFJJmEPLvUcIAAa7EUAI");
        }
        if (cell.getStatus() == Status.READY) {
            if (cell.getPlant().getName().equals("corn")) {
                sendSticker.setSticker("CAADAgADBQADFJJmEGcQWGDu4gGfAg");
            }
            if (cell.getPlant().getName().equals("potato")) {
                sendSticker.setSticker("CAADAgADBAADFJJmEJH5UrnXR1ucAg");
            }
            if (cell.getPlant().getName().equals("wheat")) {
                sendSticker.setSticker("CAADAgADCgADFJJmEDcKQLfobMsEAg");
            }
            if (cell.getPlant().getName().equals("cucumber")) {
                sendSticker.setSticker("CAADAgADBgADFJJmEEFM4xsi0qUBAg");
            }
            if (cell.getPlant().getName().equals("raspberry")) {
                sendSticker.setSticker("CAADAgADBwADFJJmEPPsSmXy8DLbAg");
            }
            if (cell.getPlant().getName().equals("strawberry")) {
                sendSticker.setSticker("CAADAgADCAADFJJmEPKOyUV6CxwfAg");
            }
            if (cell.getPlant().getName().equals("tomato")) {
                sendSticker.setSticker("CAADAgADCQADFJJmEJClHI2KZlauAg");
            }

        }
        sendSticker.setChatId(message.getChatId());
        sendSticker(sendSticker);
    }

    //SECTION ------ MAKE PLANTATION
    private void makePlantationSection(Message message) {
        SendMessage sendMsg = new SendMessage();
        sendMsg.setChatId(message.getChatId());
        sendMsg.setText(phrases.get(Info.BALANCE) + player.getBalance());
        send(sendMsg);
        sendMsg.setText(phrases.get(Info.CHOOSEPLANT));
        sendMsg.setReplyMarkup(keyboardPlantation());
        send(sendMsg);
    }

    private InlineKeyboardMarkup keyboardPlantation() {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        for (Map.Entry<String, Plant> entry : shop.getPlantsMap().entrySet()) {
            String key = entry.getKey();
            Plant value = entry.getValue();
            List<InlineKeyboardButton> col1 = new ArrayList<>();
            if (lang.equalsIgnoreCase("ru")) {
                col1.add(new InlineKeyboardButton(value.getInfoRus()).setCallbackData("plant:" + key));
            } else {
                col1.add(new InlineKeyboardButton(value.getInfoEng()).setCallbackData("plant:" + key));
            }
            rows.add(col1);
        }
        List<InlineKeyboardButton> col2 = new ArrayList<>();
        col2.add(new InlineKeyboardButton(phrases.get(Info.BACK)).setCallbackData("Back"));
        rows.add(col2);
        markup.setKeyboard(rows);
        return markup;
    }

    private void selectCell(Message message) {
        SendMessage sendMessage = new SendMessage(message.getChatId(), phrases.get(Info.CHOOSECELLTOPLANT));
        sendMessage.setReplyMarkup(keyboardCellSelection());
        send(sendMessage);
    }

    private InlineKeyboardMarkup keyboardCellSelection() {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> col1 = new ArrayList<>();
        List<InlineKeyboardButton> col2 = new ArrayList<>();
        List<InlineKeyboardButton> col3 = new ArrayList<>();
        if (field.getCells().size() % 2 == 0) {
            for (int i = 1; i < field.getCells().size()+1; i++) {
                if (i <= field.getCells().size() / 2) {
                    col1.add(new InlineKeyboardButton("" + i).setCallbackData("cell select " + i));
                } else {
                    col2.add(new InlineKeyboardButton("" + i).setCallbackData("cell select " + i));
                }
            }
        }
        col3.add(new InlineKeyboardButton(phrases.get(Info.BACK)).setCallbackData("Back"));
        rows.add(col1);
        rows.add(col2);
        rows.add(col3);
        markup.setKeyboard(rows);
        return markup;
    }

    private void setPlant(Plant plant) {
        this.plant = plant;
    }

    private Plant getPlant() {
        return this.plant;
    }
    //SECTION ------ HARVEST THE PLANTS

    private InlineKeyboardMarkup keyboardCellGetProfit(int id) {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> col1 = new ArrayList<>();
        col1.add(new InlineKeyboardButton(phrases.get(Info.INTERFACEGETPROFIT)).setCallbackData("cell harvest " + id));
        rows.add(col1);
        markup.setKeyboard(rows);
        return markup;
    }

    private void sendMsgInfoAndTryToHarvest(Message message, String showInfo, int id) {
        SendMessage sendMessage = new SendMessage(message.getChatId(), showInfo);
        if (game.getOneCellFromRepository(field, id).getStatus() == Status.READY) {
            sendMessage.setReplyMarkup(keyboardCellGetProfit(id));
        }
        send(sendMessage);
    }
}
