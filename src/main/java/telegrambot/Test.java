package telegrambot;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import telegrambot.APiKey.MovieBot;
import telegrambot.trivialchalenge.DatabaseManager;

public class Test {
    public static void main(String[] args) throws TelegramApiException {
        DatabaseManager.createNewDatabase();
        DatabaseManager.createNewTable();
        DatabaseManager.insertInitialData();
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        botsApi.registerBot(new GlobalBot());
    }
}
