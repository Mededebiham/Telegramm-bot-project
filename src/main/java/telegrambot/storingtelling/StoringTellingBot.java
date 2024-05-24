package telegrambot.storingtelling;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import telegrambot.GetConfig;

import java.util.HashMap;
import java.util.Map;


public class StoringTellingBot extends TelegramLongPollingBot {
    private Map<Long, StoryState> userStoryStates = new HashMap<>();
    private GetConfig getConfig=new GetConfig("config.txt");
    private String previousMessage;
    private boolean getstart;
    private TelegramBotsApi botsApi;
    public boolean running = true;
    public StoringTellingBot()
    {
        sendStartupMessage();
    }
    @Override
    public void onUpdateReceived(Update update) {
        long chatId = update.getMessage().getChatId();
        String messageReceived = update.getMessage().getText();
        System.out.println(messageReceived);
        if(getstart){
            if(messageReceived.equals("/quit")){
               stopBot();
            }
            else if(messageReceived.equals("/cancel")){
                getstart=false;
            }
            else{
                StoryState state = userStoryStates.getOrDefault(chatId, new StoryState());
                String responseText = state.processUserMessage(messageReceived,previousMessage);
                //System.out.println(responseText);
                userStoryStates.put(chatId, state);

                SendMessage message = new SendMessage();
                message.setChatId(chatId);
                message.setText(responseText);

                try {
                    execute(message);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
                if(state.isCorrectInput()){
                    previousMessage=messageReceived;
                }


            }

        }
        else{
            if(messageReceived.equals("/start")){
                sendWelcomeMessage(chatId);
                getstart = true;
            }
            else if (messageReceived.equals("/quit")) {
                sendMessage(chatId, "Bot is stopping: Goodbye!");
                stopBot();
            }
            else{
                sendMessage(chatId, "in valide choice. type /start  to begin");
            }
        }



    }
    private void sendWelcomeMessage(long chatId) {
        sendMessage(chatId, "Welcome to Storring telling!");
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
    private void sendStartupMessage() {
        SendMessage message = new SendMessage();
        message.setChatId("7163912977");
        message.setText("hello and welcome in my Storing telling bot. please type /start to beginn or /quit to stop bot");
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
