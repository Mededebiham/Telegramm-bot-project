package telegrambot;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import telegrambot.APiKey.MovieBot;
import telegrambot.APiKey.WeatherBot;
import telegrambot.detectivgame.MysteryDetectiveBot;
import telegrambot.storingtelling.StoringTellingBot;
import telegrambot.tictactoe.Bot;
import telegrambot.trivialchalenge.TrivialBot;

@SuppressWarnings("ALL")
public class GlobalBot extends TelegramLongPollingBot {
    private boolean getstart;
    private TelegramBotsApi botsApi;
    public boolean running = true;
    private TrivialBot trivialbot;
    private Bot bot;
    private StoringTellingBot storingTellingBot;
    MovieBot movieBot;
    private WeatherBot weatherBot;
    MysteryDetectiveBot mysteryDetectiveBot;
    private GetConfig getConfig = new GetConfig("config.txt");
    private boolean mainMenu;
    private int changeGame = 0;

    public GlobalBot() {
        // Send a message immediately when the bot starts
        sendStartupMessage();
    }

    @Override
    public String getBotUsername() {
        return getConfig.getUserName();
    }

    @Override
    public String getBotToken() {
        return getConfig.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();


        if (!mainMenu && changeGame == 0) {
            if ("/start".equals(message.getText())) {
                String chatId = message.getChatId().toString();
                sendMainMenu(chatId);
                mainMenu = true;
                changeGame = 8;

            } else if (message.getText().equalsIgnoreCase("/quit")) {
                String chatId = message.getChatId().toString();
                sendResponse(chatId, "Bot is stopping: Goodbye!");
                stopBot();
            } else {
                String chatId = message.getChatId().toString();
                sendResponse(chatId, "invalid choice. type /start to begin or /quit to close bot");
            }


        } else {
            if (mainMenu) {
                getMainMenu(update);
                if (changeGame == 8) {

                    if (message.getText().equalsIgnoreCase("/cancel")) {
                        String chatId = message.getChatId().toString();
                        sendStartupMessage();
                        mainMenu = false;
                        changeGame = 0;
                    } else if (message.getText().equalsIgnoreCase("/quit")) {
                        String chatId = message.getChatId().toString();
                        sendResponse(chatId, "Bot is stopping: Goodbye!");
                        stopBot();
                    } else {
                        String chatId = message.getChatId().toString();
                        sendResponse(chatId, "invalide choice. you have to choose one option or" +
                                " Typ /cancel to back main menu or /quit to close bot");
                    }

                }


            } else {

                if (changeGame == 1) {

                    if (trivialbot.getMain_Menu) {
                        // changeGame = 9;
                        if (message.getText().equalsIgnoreCase("/no")) {
                            sendResponse("7163912977", "typ /start to continue");
                            changeGame = 1;
                            trivialbot.getMain_Menu = false;
                        } else if (message.getText().equalsIgnoreCase("/quit")) {
                            String chatId = message.getChatId().toString();
                            sendResponse(chatId, "Bot is stopping: Goodbye!");
                            stopBot();
                        } else if (message.getText().equalsIgnoreCase("/yes")) {
                            trivialbot.getMain_Menu = false;
                            trivialbot.setGetstart(false);
                            changeGame = 8;
                            mainMenu = true;
                            String chatId = message.getChatId().toString();
                            sendMainMenu(chatId);
                            getMainMenu(update);

                        } else {
                            String chatId = message.getChatId().toString();
                            sendResponse(chatId, "invalide choice. typ /yes to back Main menu, /no to continue /quit to close bot");
                        }


                    } else {
                        trivialbot.onUpdateReceived(update);

                    }
                } else if (changeGame == 2) {

                    if (bot.getMain_Menu) {
                        if (message.getText().equalsIgnoreCase("/no")) {
                            sendResponse("7163912977", "typ /start to continue");
                            changeGame = 2;
                            bot.getMain_Menu = false;
                        } else if (message.getText().equalsIgnoreCase("/quit")) {
                            String chatId = message.getChatId().toString();
                            sendResponse(chatId, "Bot is stopping: Goodbye!");
                            stopBot();
                        } else if (message.getText().equalsIgnoreCase("/yes")) {
                            bot.getMain_Menu = false;
                            bot.setGetstart(false);
                            changeGame = 8;
                            mainMenu = true;
                            String chatId = message.getChatId().toString();
                            sendMainMenu(chatId);
                            getMainMenu(update);

                        } else {
                            String chatId = message.getChatId().toString();
                            sendResponse(chatId, "invalide choice. typ /yes to back Main menu, /no to continue /quit to close bot");
                        }


                    } else {
                        bot.onUpdateReceived(update);

                    }
                } else if (changeGame == 3) {

                    if (storingTellingBot.getMain_Menu) {
                        if (message.getText().equalsIgnoreCase("/no")) {
                            sendResponse("7163912977", "typ /start to continue");
                            changeGame = 3;
                            storingTellingBot.getMain_Menu = false;
                        } else if (message.getText().equalsIgnoreCase("/quit")) {
                            String chatId = message.getChatId().toString();
                            sendResponse(chatId, "Bot is stopping: Goodbye!");
                            stopBot();
                        } else if (message.getText().equalsIgnoreCase("/yes")) {
                            storingTellingBot.getMain_Menu = false;
                            storingTellingBot.setGetstart(false);
                            changeGame = 8;
                            mainMenu = true;
                            String chatId = message.getChatId().toString();
                            sendMainMenu(chatId);
                            getMainMenu(update);

                        } else {
                            String chatId = message.getChatId().toString();
                            sendResponse(chatId, "invalide choice. typ /yes to back Main menu, /no to continue /quit to close bot");
                        }

                    } else {
                        storingTellingBot.onUpdateReceived(update);

                    }

                } else if (changeGame == 4) {

                    if (mysteryDetectiveBot.getMain_Menu) {

                        if (message.getText().equalsIgnoreCase("/no")) {
                            sendResponse("7163912977", "typ /start to continue");
                            changeGame = 4;
                            mysteryDetectiveBot.getMain_Menu = false;
                        } else if (message.getText().equalsIgnoreCase("/quit")) {
                            String chatId = message.getChatId().toString();
                            sendResponse(chatId, "Bot is stopping: Goodbye!");
                            stopBot();
                        } else if (message.getText().equalsIgnoreCase("/yes")) {
                            mysteryDetectiveBot.getMain_Menu = false;
                            mysteryDetectiveBot.setGetstart(false);
                            changeGame = 8;
                            mainMenu = true;
                            String chatId = message.getChatId().toString();
                            sendMainMenu(chatId);
                            getMainMenu(update);

                        } else {
                            String chatId = message.getChatId().toString();
                            sendResponse(chatId, "invalide choice. typ /yes to back Main menu, /no to continue /quit to close bot");
                        }


                    } else {
                        mysteryDetectiveBot.onUpdateReceived(update);

                    }


                } else if (changeGame == 5) {

                    if (movieBot.getMain_Menu) {

                        if (message.getText().equalsIgnoreCase("/no")) {
                            sendResponse("7163912977", "typ /start to continue");
                            changeGame = 5;
                            movieBot.getMain_Menu = false;
                        } else if (message.getText().equalsIgnoreCase("/quit")) {
                            String chatId = message.getChatId().toString();
                            sendResponse(chatId, "Bot is stopping: Goodbye!");
                            stopBot();
                        } else if (message.getText().equalsIgnoreCase("/yes")) {
                            movieBot.getMain_Menu = false;
                            movieBot.setGetstart(false);
                            changeGame = 8;
                            mainMenu = true;
                            String chatId = message.getChatId().toString();
                            sendMainMenu(chatId);
                            getMainMenu(update);

                        } else {
                            String chatId = message.getChatId().toString();
                            sendResponse(chatId, "invalide choice. typ /yes to back Main menu, /no to continue /quit to close bot");
                        }
                    } else {
                        movieBot.onUpdateReceived(update);

                    }


                } else if (changeGame == 6) {

                    if (weatherBot.getMain_Menu) {

                        if (message.getText().equalsIgnoreCase("/no")) {
                            sendResponse("7163912977", "typ /start to continue");
                            changeGame = 6;
                            weatherBot.getMain_Menu = false;
                        } else if (message.getText().equalsIgnoreCase("/quit")) {
                            String chatId = message.getChatId().toString();
                            sendResponse(chatId, "Bot is stopping: Goodbye!");
                            stopBot();
                        } else if (message.getText().equalsIgnoreCase("/yes")) {
                            weatherBot.getMain_Menu = false;
                            weatherBot.setGetstart(false);
                            changeGame = 8;
                            mainMenu = true;
                            String chatId = message.getChatId().toString();
                            sendMainMenu(chatId);
                            getMainMenu(update);

                        } else {
                            String chatId = message.getChatId().toString();
                            sendResponse(chatId, "invalide choice. typ /yes to back Main menu, /no to continue /quit to close bot");
                        }

                    } else {
                        weatherBot.onUpdateReceived(update);

                    }

                }

            }
        }

    }

    private void sendStartupMessage() {
        SendMessage message = new SendMessage();
        message.setChatId("7163912977");
        message.setText("hello and welcome in mein telegramm bot. please type /start to beginn or /quit to close bot");
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void getMainMenu(Update update) {

        if (update.hasMessage() && update.getMessage().hasText()) {
            Message message = update.getMessage();
            String chatId = message.getChatId().toString();

        } else if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            String chatId = callbackQuery.getMessage().getChatId().toString();
            String data = callbackQuery.getData();

            // Gérer la sélection d'une option du menu
            switch (data) {
                case "Trivialchallenge":
                    sendResponse(chatId, "you choose trivial  challenge");
                    trivialbot = new TrivialBot();
                    changeGame = 1;
                    mainMenu = false;
                    break;
                case "Puzzle":
                    sendResponse(chatId, "you choose Puzzle and quiz");
                    bot = new Bot();
                    changeGame = 2;
                    mainMenu = false;
                    break;
                case "Storingtelling":
                    sendResponse(chatId, "you chosse Storing telling.");
                    storingTellingBot = new StoringTellingBot();
                    changeGame = 3;
                    mainMenu = false;
                    break;
                case "Detectivgame":
                    sendResponse(chatId, "You choose Detectiv bot");
                    mysteryDetectiveBot = new MysteryDetectiveBot();
                    changeGame = 4;
                    mainMenu = false;
                    break;
                case "Moovies":
                    sendResponse(chatId, "You choose Moovie bot");
                    movieBot = new MovieBot();
                    changeGame = 5;
                    mainMenu = false;
                    break;
                case "Weather":
                    sendResponse(chatId, "You choose weather bot");
                    weatherBot = new WeatherBot();
                    changeGame = 6;
                    mainMenu = false;
                    break;
            }
        }

    }

    private void sendMainMenu(String chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("choose one option:");

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

        // Ligne 1 du clavier
        List<InlineKeyboardButton> row1 = new ArrayList<>();

        InlineKeyboardButton button1 = new InlineKeyboardButton();
        button1.setText("Trivialchallenge");
        button1.setCallbackData("Trivialchallenge");
        InlineKeyboardButton button2 = new InlineKeyboardButton();
        button2.setText("Puzzle");
        button2.setCallbackData("Puzzle");
        InlineKeyboardButton button3 = new InlineKeyboardButton();
        button3.setText("Storingtelling");
        button3.setCallbackData("Storingtelling");
        InlineKeyboardButton button4 = new InlineKeyboardButton();
        button4.setText("Detectivgame");
        button4.setCallbackData("Detectivgame");
        InlineKeyboardButton button5 = new InlineKeyboardButton();
        button5.setText("Moovies");
        button5.setCallbackData("Moovies");
        InlineKeyboardButton button6 = new InlineKeyboardButton();
        button6.setText("Weather");
        button6.setCallbackData("Weather");

        // Ligne 2 du clavier
        List<InlineKeyboardButton> row2 = new ArrayList<>();
        row1.add(button1);
        row1.add(button2);
        row1.add(button3);
        row2.add(button4);
        row2.add(button5);
        row2.add(button6);

        keyboard.add(row1);
        keyboard.add(row2);
        markup.setKeyboard(keyboard);
        message.setReplyMarkup(markup);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void sendResponse(String chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
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
}
