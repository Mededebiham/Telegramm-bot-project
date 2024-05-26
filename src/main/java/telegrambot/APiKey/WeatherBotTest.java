package telegrambot.APiKey;

import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


@SuppressWarnings("ALL")
public class WeatherBotTest {
    public static void main(String[] args) throws TelegramApiException {
        WeatherBot bot = new WeatherBot();
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
