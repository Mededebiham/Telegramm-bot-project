package telegrambot.example.dame;

import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@SuppressWarnings("ALL")
public class CheckerTest {
    public static void main(String[] args) throws TelegramApiException {
        CheckersBot bot = new CheckersBot();
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
