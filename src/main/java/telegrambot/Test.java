package telegrambot;

import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

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

