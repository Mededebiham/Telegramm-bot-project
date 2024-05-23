package telegrambot.storingtelling;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import telegrambot.GetConfig;

import java.util.HashMap;
import java.util.Map;


public class StoringTellingBot extends TelegramLongPollingBot {
    private Map<Long, StoryState> userStoryStates = new HashMap<>();
    private GetConfig getConfig=new GetConfig("Bot_config");
    private String previousMessage;
    @Override
    public void onUpdateReceived(Update update) {
        long chatId = update.getMessage().getChatId();
        String messageReceived = update.getMessage().getText();
        System.out.println(messageReceived);
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


    @Override
    public String getBotToken() {
        return getConfig.getToken();
    }

    @Override
    public String getBotUsername() {


        return getConfig.getUserName();
    }

}
