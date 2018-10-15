package com.reason.game.app;

import com.reason.bot.functional.TelegramBot;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

public class App {

    public static void main(String[] args) {
        ApiContextInitializer.init();
        
        TelegramBotsApi api = new TelegramBotsApi();
        
        try {
            api.registerBot(new TelegramBot());
        } catch (TelegramApiRequestException e) {
            System.err.println(e);
        }
        
        System.out.println("Application is ready");
    }
}
