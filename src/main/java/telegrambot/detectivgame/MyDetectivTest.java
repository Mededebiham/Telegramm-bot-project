package telegrambot.detectivgame;

import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@SuppressWarnings("ALL")
public class MyDetectivTest {

    public static void main(String[] args) throws TelegramApiException {
        MysteryDetectiveBot bot = new MysteryDetectiveBot();
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




