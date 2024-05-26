package telegrambot;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import telegrambot.APiKey.MovieBot;
import telegrambot.trivialchalenge.DatabaseManager;

@SuppressWarnings("ALL")
public class Test {
    public static void main(String[] args) throws TelegramApiException {
        DatabaseManager.createNewDatabase();
        DatabaseManager.createNewTable();
        DatabaseManager.insertInitialData();
        GlobalBot bot = new GlobalBot();
        bot.startBot();
        new Thread(() -> {
            while (bot.isRunning()) {
                try {
                    Thread.sleep(1000); // Check every second
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            bot.stopBot();
        }).start();
    }
}

