package telegrambot.detectivgame;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import telegrambot.GetConfig;

public class MysteryDetectiveBot extends TelegramLongPollingBot {
    private GetConfig getConfig=new GetConfig("config.txt");
    private String currentScenario = "stolen_painting";
    private Scenario scenario = ScenarioRepository.getScenario(currentScenario);



    @Override
    public void onUpdateReceived(Update update) {
        long chatId = update.getMessage().getChatId();
        String messageReceived = update.getMessage().getText();
        System.out.println(messageReceived);
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



    @Override
    public String getBotToken() {
        return getConfig.getToken();
    }

    @Override
    public String getBotUsername() {


        return getConfig.getUserName();
    }



}

