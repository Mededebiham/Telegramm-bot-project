package telegrambot.trivialchalenge;

import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@SuppressWarnings("ALL")
public class TrivialTest {
    public static void main(String[] args) throws TelegramApiException {
        DatabaseManager.createNewDatabase();
        DatabaseManager.createNewTable();
        DatabaseManager.insertInitialData();
        TrivialBot bot = new TrivialBot();
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
