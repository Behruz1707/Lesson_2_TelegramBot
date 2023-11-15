package org.example.EmailChat;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class App {
    public static void main(String[] args) throws TelegramApiException {
        TelegramBotsApi tba=new TelegramBotsApi(DefaultBotSession.class);
        tba.registerBot(new EmailBot());


    }

}
