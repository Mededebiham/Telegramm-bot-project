package telegrambot.trivialchalenge;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import telegrambot.GetConfig;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class TrivialBot extends TelegramLongPollingBot {
    private String botUsername = "YOUR_BOT_USERNAME";
    private String botToken = "YOUR_BOT_TOKEN";

    private Map<Long, Integer> userScores = new HashMap<>();
    private Map<Long, String> currentQuestions = new HashMap<>();
    private Map<Long, String> currentAnswers = new HashMap<>();
    private Map<Long, String[]> currentOptions = new HashMap<>();

    private Connection connection;

    public TrivialBot() {
        try {
            // Connect to the SQLite database
            connection = DriverManager.getConnection("jdbc:sqlite:trivia.db");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        this.botUsername=(new GetConfig("config.txt")).getUserName();
        this.botToken=(new GetConfig("config.txt")).getToken();
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            long chatId = update.getMessage().getChatId();
            String messageText = update.getMessage().getText();

            if (messageText.equalsIgnoreCase("start")) {
                sendWelcomeMessage(chatId);
            } else if (messageText.equalsIgnoreCase("score")) {
                sendScore(chatId);
            } else if (messageText.equalsIgnoreCase("next")) {
                askQuestion(chatId, null);
            } else if (messageText.startsWith("category ")) {
                String category = messageText.substring(9).trim();
                askQuestion(chatId, category);
            } else if (messageText.equalsIgnoreCase("categories")) {
                listCategories(chatId);
            } else {
                handleUserResponse(chatId, messageText);
            }
        }
    }

    private void sendWelcomeMessage(long chatId) {
        sendMessage(chatId, "Welcome to Trivia Challenge! Type next to get a question, or category <category> to get a question from a specific category. Type categories to see the list of available categories. Type score to check your score.");
    }

    private void sendScore(long chatId) {
        int score = userScores.getOrDefault(chatId, 0);
        sendMessage(chatId, "Your current score is: " + score);
    }

    private void askQuestion(long chatId, String category) {
        String query = "SELECT question, answer, options FROM trivia_questions";
        if (category != null && !category.isEmpty()) {
            query += " WHERE category = ?";
        }
        else{
            query += " WHERE category = category";
        }
        query += " ORDER BY RANDOM() LIMIT 1";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            if (category != null && !category.isEmpty()) {
                stmt.setString(1, category);
            }
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String question = rs.getString("question");
                String answer = rs.getString("answer");
                String[] options = rs.getString("options").split(",");

                currentQuestions.put(chatId, question);
                currentAnswers.put(chatId, answer);
                currentOptions.put(chatId, options);

                StringBuilder questionWithOptions = new StringBuilder(question + "\n");
                for (int i = 0; i < options.length; i++) {
                    questionWithOptions.append((char) ('A' + i)).append(": ").append(options[i]).append("\n");
                }

                sendMessage(chatId, questionWithOptions.toString());
            } else {
                sendMessage(chatId, "No questions available in this category.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            sendMessage(chatId, "An error occurred while fetching the question. Please try again.");
        }
    }

    private void handleUserResponse(long chatId, String messageText) {
        String correctAnswer = currentAnswers.get(chatId);
        String[] options = currentOptions.get(chatId);

        if (correctAnswer != null && options != null) {
            int answerIndex = -1;
            if (messageText.length() == 1) {
                char optionChar = messageText.charAt(0);
                if (optionChar >= 'A' && optionChar < 'A' + options.length) {
                    answerIndex = optionChar - 'A';
                }
            }

            if (answerIndex >= 0 && options[answerIndex].equalsIgnoreCase(correctAnswer)) {
                userScores.put(chatId, userScores.getOrDefault(chatId, 0) + 1);
                sendMessage(chatId, "Correct! Your score: " + userScores.get(chatId));
            } else {
                userScores.put(chatId, userScores.getOrDefault(chatId, 0) - 1);
                sendMessage(chatId, "Incorrect. your score :"+ userScores.get(chatId)+". The correct answer was: " + correctAnswer);
            }

            currentQuestions.remove(chatId);
            currentAnswers.remove(chatId);
            currentOptions.remove(chatId);
        } else {
            sendMessage(chatId, "Please ask for a question by typing /next or /category <category>.");
        }
    }

    private void listCategories(long chatId) {
        String query = "SELECT DISTINCT category FROM trivia_questions";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            StringBuilder categories = new StringBuilder("Available categories:\n");
            while (rs.next()) {
                categories.append("- ").append(rs.getString("category")).append("\n");
            }
            sendMessage(chatId, categories.toString());
        } catch (SQLException e) {
            e.printStackTrace();
            sendMessage(chatId, "An error occurred while fetching the categories. Please try again.");
        }
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

}


