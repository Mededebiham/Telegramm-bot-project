package telegrambot;

import kotlin.random.Random;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

import telegrambot.APiKey.MovieBot;
import telegrambot.APiKey.WeatherBot;
import telegrambot.detectivgame.MysteryDetectiveBot;
import telegrambot.storingtelling.StoringTellingBot;
import telegrambot.tictactoe.Bot;
import telegrambot.trivialchalenge.TrivialBot;

public class GlobalBot extends TelegramLongPollingBot {
    private TrivialBot trivialbot = new TrivialBot();
    private Bot bot = new Bot();
    private StoringTellingBot storingTellingBot = new StoringTellingBot();
    MovieBot movieBot = new MovieBot();
    private WeatherBot weatherBot = new WeatherBot();
    MysteryDetectiveBot mysteryDetectiveBot = new MysteryDetectiveBot();
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
            } else {
                String chatId = message.getChatId().toString();
                sendResponse(chatId, "invalid choice. type /start to begin");
            }


        } else {
            if (mainMenu) {
                getMainMenu(update);
            } else {

                if (changeGame == 1) {
                    trivialbot.onUpdateReceived(update);
                } else if (changeGame == 2) {

                    bot.onUpdateReceived(update);
                } else if (changeGame == 3) {
                    storingTellingBot.onUpdateReceived(update);

                } else if (changeGame == 4) {
                    mysteryDetectiveBot.onUpdateReceived(update);

                } else if (changeGame == 5) {
                    movieBot.onUpdateReceived(update);

                } else if (changeGame == 6) {
                    weatherBot.onUpdateReceived(update);

                }


            }
        }

    }

    private void sendStartupMessage() {
        SendMessage message = new SendMessage();
        message.setChatId("7163912977");
        message.setText("hello and welcome in mein telegramm bot. please type /start to beginn");
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

            if ("/start".equals(message.getText())) {
                sendMainMenu(chatId);
            }
        } else if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            String chatId = callbackQuery.getMessage().getChatId().toString();
            String data = callbackQuery.getData();

            // Gérer la sélection d'une option du menu
            switch (data) {
                case "Trivialchallenge":
                    sendResponse(chatId, "Vous avez sélectionné l'option 1.");
                    changeGame = 1;
                    mainMenu = false;
                    break;
                case "Puzzle":
                    sendResponse(chatId, "Vous avez sélectionné l'option 2.");
                    changeGame = 2;
                    mainMenu = false;
                    break;
                case "Storingtelling":
                    sendResponse(chatId, "Vous avez sélectionné l'option 3.");
                    changeGame = 3;
                    mainMenu = false;
                    break;
                case "Detectivgame":
                    sendResponse(chatId, "Vous avez sélectionné l'option 1.");
                    changeGame = 4;
                    mainMenu = false;
                    break;
                case "Moovies":
                    sendResponse(chatId, "Vous avez sélectionné l'option 2.");
                    changeGame = 5;
                    mainMenu = false;
                    break;
                case "Weather":
                    sendResponse(chatId, "Vous avez sélectionné l'option 3.");
                    changeGame = 6;
                    mainMenu = false;
                    break;
            }
        }

    }

    private void sendMainMenu(String chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Sélectionnez une option :");

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


}
