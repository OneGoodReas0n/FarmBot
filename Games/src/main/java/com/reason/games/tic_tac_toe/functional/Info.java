package com.reason.games.tic_tac_toe.functional;

import com.reason.games.tic_tac_toe.enums.Strings;
import java.util.HashMap;
import java.util.Map;

public class Info {
    
    private final static Info instance = new Info();
    private final Map<Strings,String> phrases = new HashMap<>();

    public Info() {
        initializePhrasesMap();
    }

    public static Info getInstance() {
        return instance;
    }
    
    private void initializePhrasesMap(){
        phrases.put(Strings.GAMENAME, " -------- Крестики-нолики -------- ");
        phrases.put(Strings.USERINTERFACE, "Интерфейс пользователя: ");
        phrases.put(Strings.STARTGAME, "1- Начать игру");
        phrases.put(Strings.SEEGAMEHISTORY, "2- Посмотреть историю игр");
        phrases.put(Strings.EXIT, "3- Выход\n");
        phrases.put(Strings.YOURCHOICE, "Сделайте ваш выбор:");
        phrases.put(Strings.ENTERPLAYER1NAME, "Введите имя игрока 1: ");
        phrases.put(Strings.ENTERPLAYER2NAME, "Введите имя игрока 2: ");
        phrases.put(Strings.GAMEISSTARTED, "------ Игра начилась ------");
        phrases.put(Strings.GAMEINFO, "Выбор клеточек осуществляется набором на клавиатуре комбинаций: \n №Строки + х + №Столбца (Пример 1х1, 3х3, 2х1)");
        phrases.put(Strings.RETRY, "Еще одну игру? (1-да,2-нет)");
        phrases.put(Strings.RETURNTOMAINMENU, "Вернуться в главное меню -  введите любой символ");
        phrases.put(Strings.LABELGAMEHISTORY, "-------- ИСТОРИЯ ИГР --------");
    }
    
    public String getPhrase(Strings string){
        return phrases.get(string);
    } 
}
