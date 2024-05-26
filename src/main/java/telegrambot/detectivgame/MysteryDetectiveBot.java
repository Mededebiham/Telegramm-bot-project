package telegrambot.detectivgame;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import telegrambot.GetConfig;

@SuppressWarnings("ALL")
public class MysteryDetectiveBot extends TelegramLongPollingBot {
    private GetConfig getConfig = new GetConfig("config.txt");
    private String currentScenario = "stolen_painting";
    private Scenario scenario = ScenarioRepository.getScenario(currentScenario);
    private boolean getstart;
    private TelegramBotsApi botsApi;
    public boolean running = true;
    public boolean getMain_Menu;

    public MysteryDetectiveBot() {
        sendStartupMessage();
    }

    @Override
    public void onUpdateReceived(Update update) {
        long chatId = update.getMessage().getChatId();
        String messageReceived = update.getMessage().getText();
        System.out.println(messageReceived);
        if (getstart) {
            getMain_Menu = false;
            if (messageReceived.equalsIgnoreCase("/start")) {
                sendMessage(chatId, "Welcome to Mystery Detectiv! Type /start to begin, or /quit to stop  bot" +
                        " type /Where were you  to get position, /clues to get clues and /solution to get solution");

            } else if (messageReceived.equals("/quit")) {
                sendMessage(chatId, "Bot is stopping: Goodbye!");
                stopBot();
            } else if (messageReceived.equalsIgnoreCase("/main_menu")) {
                getstart = false;
                getMain_Menu = true;
                sendMessage(chatId, "you go back to main menu!" +
                        "typ /yes to back Main menu, /no to continue /quit to close bot");
            } else if (messageReceived.equals("/cancel")) {
                sendStartupMessage();
                getstart = false;
            } else {
                String responseText = processUserMessage(messageReceived);
                System.out.println(responseText);
                SendMessage message = new SendMessage();
                message.setChatId(chatId);
                message.setText(responseText);
                try {
                    execute(message);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }


            }

        } else {
            if (messageReceived.equals("/start")) {
                sendWelcomeMessage(chatId);
                scenario.getDescription();
                getstart = true;
            } else if (messageReceived.equalsIgnoreCase("/main_menu")) {
                getstart = false;
                getMain_Menu = true;
                sendMessage(chatId, "you go back to main menu!" +
                        "typ /yes to back Main menu, /no to continue /quit to close bot");
            } else if (messageReceived.equals("/quit")) {
                sendMessage(chatId, "Bot is stopping: Goodbye!");
                stopBot();
            } else {
                sendMessage(chatId, "in valide choice. type /start  to begin or /quit to stop bot");
            }
        }

    }

    private String processUserMessage(String messageText) {
        String response = "";

        if (messageText.equalsIgnoreCase("/start")) {
            response = scenario.getDescription();
        } else if (messageText.toLowerCase().contains("where were you")) {
            Suspect suspect = scenario.getSuspects().get("John Doe");  // This would be dynamic based on the context
            response = suspect.getAlibi();
        } else if (messageText.toLowerCase().contains("clues")) {
            Suspect suspect = scenario.getSuspects().get("John Doe");  // This would be dynamic based on the context
            response = String.join(", ", suspect.getClues());
        } else if (messageText.toLowerCase().contains("solution")) {
            response = "The solution is: " + scenario.getSolution();
        } else {
            response = "I'm not sure how to respond to that.";
        }

        return response;
    }


    private void sendMessage(long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotToken() {
        return getConfig.getToken();
    }

    @Override
    public String getBotUsername() {


        return getConfig.getUserName();
    }

    private void sendStartupMessage() {
        SendMessage message = new SendMessage();
        message.setChatId("7163912977");
        message.setText("hello and welcome in my Mystery detectiv  bot. please type /start to beginn or /quit to stop bot");
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }


    private void sendWelcomeMessage(long chatId) {
        sendMessage(chatId, "Welcome to Mystery Detectiv! Type /start to begin, /cancel to get start or /quit to stop  bot." +
                " type /Where were you  to get position, /clues to get clues and /solution to get solution");
    }

    public void stopBot() {

        // Shutdown the application
        System.exit(0);
    }

    public void startBot() {
        try {
            botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(this);
            System.out.println("Bot started successfully.");
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public boolean isGetstart() {
        return getstart;
    }

    public boolean isRunning() {
        return running;
    }

    public void setGetstart(boolean getstart) {
        this.getstart = getstart;
    }
}

