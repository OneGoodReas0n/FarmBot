package com.reason.bot.info;

import com.reason.games.farm.enums.Info;
import java.util.HashMap;
import java.util.Map;

public class StaticInfo {

    private final static StaticInfo instance = new StaticInfo();
    private final static String BOT_NAME = "FarmBot";
    private final static String BOT_TOKEN = "656037872:AAH6pmqtYN9TLFtqFa48iAdc0kdzlFrTEE0";
    private final Map<Info, String> phrasesEN = new HashMap<>();
    private final Map<Info, String> phrasesRU = new HashMap<>();
    private final Map<Info, String> allPhrases = new HashMap<>();

    public static String BOT_NAME() {
        return BOT_NAME;
    }

    public static String BOT_TOKEN() {
        return BOT_TOKEN;
    }

    public static StaticInfo getInstance() {
        return instance;
    }

    public StaticInfo() {
        initPhrasesEn();
        initPhrasesRu();
        initAllPhrases();
    }

    private void initPhrasesEn() {
        phrasesEN.put(Info.LANG, "en");
        phrasesEN.put(Info.WELCOMETEXT, "Welcome to the new game\nEnter please your name in format /name \"yourname\"");
        phrasesEN.put(Info.HELLOTEXT, "Hello, ");
        phrasesEN.put(Info.INTERFACETEXT, "     User interface     ");//\n1) Cells status\n2) Make a plantation\n3) Harvest plants
        phrasesEN.put(Info.LANGINTERFACE, "Interface language: English");
        
        phrasesEN.put(Info.INTERFACESEESTATUSCOLUMN, "Cells status");
        phrasesEN.put(Info.CHOOSECELL, "Choose cell to see the information");
        phrasesEN.put(Info.CELLHASSTATUS, "\nPlant: %s\nStatus: %s\nTime left to harvest: %s");
        phrasesEN.put(Info.CELLHASSTATUSREADY, "\nPlant: %s\nStatus: %s");
        phrasesEN.put(Info.CELLNOTSTATUS, "Plant: \n");
        phrasesEN.put(Info.NOPLANT, "\nPlant: free\n");
        phrasesEN.put(Info.CELLNUMBER, "Cell ");
        phrasesEN.put(Info.STATUSMATURES, "matures ");
        phrasesEN.put(Info.STATUSREADY, "ready ");
        phrasesEN.put(Info.STATUSHALFREADY, "half time passed ");

        phrasesEN.put(Info.INTERFACEMAKEPLANTATION, "Make a plantation");
        phrasesEN.put(Info.CHOOSECELLTOPLANT, "Choose a cell to do a plantation");
        phrasesEN.put(Info.CHOOSEPLANT, "Choose a plant that you want to plant ");
        phrasesEN.put(Info.ARRAYOFPLANTS, "List of plants: ");
        phrasesEN.put(Info.WRONGNAMEPLANT, "You enter a wrong name of plant, try again please... ");
        phrasesEN.put(Info.WRONGCELL, "You enter a wrong number of cell, try again please... ");
        phrasesEN.put(Info.BALANCE, "Your balance is ");
        phrasesEN.put(Info.CELLNOTFREE, "This cell is occupied, choose another one ");
        phrasesEN.put(Info.NOFREECELLS, "There are no free cells wait for something is grown");
        phrasesEN.put(Info.PLANTSUCCESSFUL, "Plant is successfully planted in cell ");
        phrasesEN.put(Info.INCOME, "Your income: ");

        phrasesEN.put(Info.INTERFACEGETPROFIT, "Harvest plant and get profit");
        phrasesEN.put(Info.GETALLPROFIT, "Harvest all plant and get profit");
        phrasesEN.put(Info.HARVESTRULES, "To get a profit from harvest you should choose a cell (from 1 to 8) with a ready plant ");
        phrasesEN.put(Info.PLANTISNOTREADY, "Plant is not ready yet, wait for a while");
        phrasesEN.put(Info.TOHARVEST, "Plant is harvested, you get a profit ");
        phrasesEN.put(Info.NOREADYPLANT, "There is no plant to harvest, wait for a while...");
        phrasesEN.put(Info.PROFITISGOT, "Profit from harvest has been got");

        phrasesEN.put(Info.WRONGINPUT, "Wrong input, try again please... ");
        phrasesEN.put(Info.WRONGINPUTNAME, "Wrong name input, try again please... ");
        phrasesEN.put(Info.DONTHAVEMONEY, "Sorry, but you don't have anough money to plant this plant, get more money ");
        phrasesEN.put(Info.NOTHAVEMONEYANYPLANT, "Sorry, but you don't have anough money to plant any plant, get more money ");
        phrasesEN.put(Info.BACK, " Back to interface ");
        phrasesEN.put(Info.GAMERULES, "Game rules: you have start amount of money 100 points and 8 cells for buying and planting the plants."
                + "Your aim is to earn as much more money and plant all kinds of plants. I wish you good game)");
    }

    private void initPhrasesRu() {
        phrasesRU.put(Info.LANG, "ru");
        phrasesRU.put(Info.WELCOMETEXT, "Добро пожаловать в игру\nВведите свое имя в формате /name \"ваше имя\"");
        phrasesRU.put(Info.HELLOTEXT, "Здравствуйте, ");
        phrasesRU.put(Info.INTERFACETEXT, "     Интерфейс игры     "); //\n1) Посмотреть статус поля\n2) Меню посадки растения\n3) Собрать урожай
        phrasesRU.put(Info.LANGINTERFACE, "Язык интерфейса: русский");
        
        phrasesRU.put(Info.INTERFACESEESTATUSCOLUMN, "Посмотреть статус поля");
        phrasesRU.put(Info.CHOOSECELL, "Выберите поле чтобы посмотреть информацию");
        phrasesRU.put(Info.CELLHASSTATUS, "Растение: %s\nСтатус: %s\nОсталось времени для созревания: %s");
        phrasesRU.put(Info.CELLHASSTATUSREADY, "\nPlant: %s\nStatus: %s");
        phrasesRU.put(Info.CELLNOTSTATUS, "Растение: %s\n");
        phrasesRU.put(Info.NOPLANT, "\nРастение: пусто\n");
        phrasesRU.put(Info.CELLNUMBER, "Поле ");
        phrasesRU.put(Info.STATUSMATURES, "созревает ");
        phrasesRU.put(Info.STATUSREADY, "созрело ");
        phrasesRU.put(Info.STATUSHALFREADY, "почти созрел ");

        phrasesRU.put(Info.INTERFACEMAKEPLANTATION, "Меню посадки растения");
        phrasesRU.put(Info.CHOOSEPLANT, "Выберите растение, которое вы хотите посадить ");
        phrasesRU.put(Info.CHOOSECELLTOPLANT, "Выберите клетку чтобы посадить растение");
        phrasesRU.put(Info.ARRAYOFPLANTS, "Список растений: ");
        phrasesRU.put(Info.WRONGNAMEPLANT, "Вы ввели неверное название растения, попробуйте еще раз... ");
        phrasesRU.put(Info.WRONGCELL, "Вы ввели неверное значение клеточки, попробуйте еще раз... ");
        phrasesRU.put(Info.BALANCE, "Ваш баланс: ");
        phrasesRU.put(Info.CELLNOTFREE, "Данная клеточка уже занята, выберите другую ");
        phrasesRU.put(Info.NOFREECELLS, "Нет свободных клеточек, подожите пока что-нибудь созреет");
        phrasesRU.put(Info.PLANTSUCCESSFUL, "Растение успешно посежно в клеточку ");

        phrasesRU.put(Info.INTERFACEGETPROFIT, "Собрать урожай");
        phrasesRU.put(Info.GETALLPROFIT, "Собрать урожай со всех клеточек");
        phrasesRU.put(Info.HARVESTRULES, "Чтобы собрать урожай нужно выбрать клетку (от 1 до 8) с созревшим растением ");
        phrasesRU.put(Info.PLANTISNOTREADY, "Растение еще не созрело, подождите еще немного ");
        phrasesRU.put(Info.TOHARVEST, "Растение созрело, получите свою прибыль ");
        phrasesRU.put(Info.NOREADYPLANT, "Ни одно растение еще не созрело, подождите немного...");
        phrasesRU.put(Info.PROFITISGOT, "Урожай собран");
        phrasesRU.put(Info.INCOME, "Ваш доход: ");

        phrasesRU.put(Info.WRONGINPUT, "Неверный ввод, попробуйте еще раз... ");
        phrasesRU.put(Info.WRONGINPUTNAME, "Неверный ввод имени, попробуйте еще раз... ");
        phrasesRU.put(Info.DONTHAVEMONEY, "Извините, но у вас не хватает денег на посадку этого растения, заработайте больше денег ");
        phrasesRU.put(Info.NOTHAVEMONEYANYPLANT, "Извините, но у вас недостаточно денег на покупку растений, заработайте больше денег ");
        phrasesRU.put(Info.BACK, " Назад в меню ");
        phrasesRU.put(Info.GAMERULES, "Условия игры: у вас есть начальная сумма 100 ед и 8 клеточек для покупки и посадки растений. "
                + "Ваша задача заработать как можно больше денег и посадить все виды растений. Приятной игры)");
    }

    private void initAllPhrases() {
        allPhrases.put(Info.ENLANG, "en");
        allPhrases.put(Info.RULANG, "ru");
        allPhrases.put(Info.CHOOSELANGUAGE, "Choose please language interface\\ Выберите пожалуйста язык интерфейса");
        allPhrases.put(Info.CALLCOLUMNINFO, "ColumnInfo");
        allPhrases.put(Info.CALLMAKEPLANTATION, "Plantation");
        allPhrases.put(Info.CALLHARVEST, "Harvest");
        allPhrases.put(Info.LEFT, "<");
        allPhrases.put(Info.RIGHT, ">");
    }

    public Map<Info, String> getPhrases(String lang) {
        if (lang.equalsIgnoreCase("en")) {
            return phrasesEN;
        }
        if (lang.equalsIgnoreCase("ru")) {
            return phrasesRU;
        }
        return allPhrases;
    }
}
