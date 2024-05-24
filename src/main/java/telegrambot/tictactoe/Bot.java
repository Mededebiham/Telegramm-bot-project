package telegrambot.tictactoe;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.polls.SendPoll;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import telegrambot.GetConfig;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Bot extends TelegramLongPollingBot {
    private Map<Long, BlockGame> gamesblock = new HashMap<>();
    private int startblocgame = 0;
    private Quiz q;
    private GetConfig getConfig = new GetConfig("config.txt");
    private boolean getstart;
    private TelegramBotsApi botsApi;
    public boolean running = true;
    private int changegame = 0;
    public boolean getMain_Menu;

    public Bot() {
        sendStartupMessage();
    }

    @Override
    public void onUpdateReceived(Update update) {
        long chatId = update.getMessage().getChatId();
        String messageReceived = update.getMessage().getText();
        if (getstart) {
            getMain_Menu=false;
            if (changegame == 0) {
                if (messageReceived.equals("/play_puzzle")) {
                    sendResponse(chatId, "Welcome to Puzzle! Type /new_game, to begin, /cancel to back to menu, /quit to stop bot");
                    changegame = 1;


                } else if (messageReceived.equals("/play_quiz")) {
                    sendResponse(chatId, "welcome to quiz! Type /new_game, to begin, /cancel to back to menu, /quit to stop bot");
                    changegame = 2;
                } else if (messageReceived.equals("/cancel")) {
                    sendStartupMessage();
                    getstart = false;
                } else if (messageReceived.equals("/quit")) {
                    sendResponse(chatId, "Bot is stopping: Goodbye!");
                    stopBot();
                } else {
                    sendResponse(chatId, "Invalid choice. type /play_puzzle for playing puzzle or /play_quiz for playing quiz, " +
                            "/cancel to back to menu, /quit to stop bot");
                }

            }
            else if (changegame == 1) {
                if (messageReceived.equals("/new_game")) {
                    startblocgame = 1;
                    changegame=3;

                } else if (messageReceived.equals("/cancel")) {
                    sendStartupMessage();
                    getstart = false;
                    changegame = 0;
                    startblocgame = 0;
                }
                else if (messageReceived.equalsIgnoreCase("/main_menu")) {
                    getstart=false;
                    getMain_Menu=true;
                    sendResponse(chatId, "you go back to main menu!");
                }else if (messageReceived.equals("/quit")) {
                    sendResponse(chatId, "Bot is stopping: Goodbye!");
                    stopBot();
                } else {
                    sendResponse(chatId, "Invalid choice. Type /new_game, to begin, /cancel to back to menu, /quit to stop bot");

                }

            }
            else if (changegame == 2) {
                if (messageReceived.equals("/new_game")) {
                    startblocgame = 2;
                    changegame=3;
                }
                else if (messageReceived.equals("/cancel")) {
                    sendStartupMessage();
                    getstart = false;
                    changegame = 0;
                    startblocgame = 0;
                }
                else if (messageReceived.equals("/quit")) {
                    sendResponse(chatId, "Bot is stopping: Goodbye!");
                    stopBot();
                }
                else if (messageReceived.equalsIgnoreCase("/main_menu")) {
                    getstart=false;
                    getMain_Menu=true;
                    sendResponse(chatId, "you go back to main menu!" +
                            "typ /yes to back Main menu, /no to continue /quit to close bot");
                }
                else {
                    sendResponse(chatId, "Invalid choice. Type /new_game, to begin, /cancel to back to menu, /quit to stop bot");
                }
            }
            if(startblocgame==1){
                if (messageReceived.equals("/new_game")) {
                    startNewGameBlock(chatId);
                    sendResponse(chatId, "1: Please enter a number to move");
                    sendResponse(chatId, "2: type /new_game to start new game");
                    sendResponse(chatId, "3:type /cancel to back to menu");
                    sendResponse(chatId, "4: type /quit to stop bot");
                }
              else  if (messageReceived.equals("/cancel")) {
                    sendStartupMessage();
                    getstart = false;
                    startblocgame = 0;
                    changegame = 0;
                } else if (messageReceived.equals("/quit")) {
                    sendResponse(chatId, "Bot is stopping: Goodbye!");
                    stopBot();
                }
                else if (messageReceived.equalsIgnoreCase("/main_menu")) {
                    getstart=false;
                    getMain_Menu=true;
                    sendResponse(chatId, "you go back to main menu!" +
                            "typ /yes to back Main menu, /no to continue /quit to close bot");
                }else {
                    try {
                        int number = Integer.parseInt(messageReceived);
                        makeMoveblock(chatId, number);
                    } catch (NumberFormatException e) {
                        sendResponse(chatId, "Invalid input. Please enter a number less than 9");
                    }

                }

            }
            else if(startblocgame==2){
                if (messageReceived.equals("/new_game")) {
                    sendResponse(chatId, "Welcome to my quiz 🤖");
                    sendResponse(chatId, "Let's have some fun now.... 😎");
                    sendResponse(chatId, "1. Type '/quiz', if you want me to ask you something smart");
                    sendResponse(chatId, "2. Type /poll, if you want me to get poll");
                    sendResponse(chatId, "3. Type any text, if you want me to count its letters");
                    sendResponse(chatId, "4. Type /new game to begin new game");
                    sendResponse(chatId, "5. Type /cancel to back to menu");
                    sendResponse(chatId, "6. Type quit to stop bot");
                }
               else if (messageReceived.equals("/cancel")) {
                    sendStartupMessage();
                    getstart = false;
                    startblocgame = 0;
                    changegame = 0;
                }
                else if (messageReceived.equalsIgnoreCase("/main_menu")) {
                    getstart=false;
                    getMain_Menu=true;
                    sendResponse(chatId, "you go back to main menu!" +
                            "typ /yes to back Main menu, /no to continue /quit to close bot");
                }else if (messageReceived.equals("/quit")) {
                    sendResponse(chatId, "Bot is stopping: Goodbye!");
                    stopBot();
                }
                else if (messageReceived.toLowerCase().startsWith("/quiz")) {
                    q = new Quiz();
                    sendResponse(chatId, q.getRandomQuestion().toString());
                }
                else if (messageReceived.toLowerCase().startsWith("/poll")) {
                    sendPollToUser(chatId);
                }

                else {
                    sendResponse(chatId, "I counted the letters in your message: " + messageReceived);
                    sendResponse(chatId, countLetters(messageReceived).toString());

                }

            }

        }
        else {
            if (messageReceived.equals("/start")) {
                sendResponse(chatId, "Hello! Welcome to my bot. type /play_puzzle for playing puzzle or /play_quiz for playing quiz," +
                        "/cancel to back to menu, /quit to stop bot");
                getstart = true;

            } else if (messageReceived.equals("/quit")) {
                sendResponse(chatId, "Bot is stopping: Goodbye!");
                stopBot();
            }
            else if (messageReceived.equalsIgnoreCase("/main_menu")) {
                getstart=false;
                getMain_Menu=true;
                sendResponse(chatId, "you go back to main menu!" +
                        "typ /yes to back Main menu, /no to continue /quit to close bot");
            }else {
                sendResponse(chatId, "in valide choice. type /start  to begin or /quit to stop bot");
            }
        }

    }

    private void startNewGameBlock(long chatId) {
        BlockGame game = new BlockGame();
        gamesblock.put(chatId, game);

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText("New game started!\n" + game.getBoardString());

        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void makeMoveblock(long chatId, int number) {
        BlockGame game = gamesblock.get(chatId);
        if (game == null) {
            startNewGameBlock(chatId);
            return;
        }


        if (game.move(number)) {
            if (game.isSolved()) {
                sendResponse(chatId, "Congratulations! You solved the puzzle!\n" + game.getBoardString());
                gamesblock.remove(chatId);
            } else {
                sendResponse(chatId, "Moved!\n" + game.getBoardString());
            }
        } else {
            sendResponse(chatId, "Invalid move. enter number that is frei to move Try again.\n" + game.getBoardString());
        }
    }

    /**
     * This method counts the letters of a provided string parameter and saves them into a Map which is returned.
     * All letters are converted into lowercase. Digits and other non-letter characters are not counted.
     *
     * @param input the string to be evaluated
     * @return a map containing the result of
     */
    public Map<Character, Integer> countLetters(String input) {
        Map<Character, Integer> letterCountMap = new HashMap<>();

        for (char c : input.toCharArray()) {
            // Convert the character to lowercase to count both uppercase and lowercase letters together
            char lowercaseC = Character.toLowerCase(c);

            // Check if the character is a letter (alphabet character)
            if (Character.isLetter(lowercaseC)) {
                letterCountMap.put(lowercaseC, letterCountMap.getOrDefault(lowercaseC, 0) + 1);
            }
        }
        return letterCountMap;
    }

    private void sendStartupMessage() {
        SendMessage message = new SendMessage();
        message.setChatId("7163912977");
        message.setText("hello and welcome in Puzzle bot. please type /start to beginn or /quit to stop bot");
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void sendResponse(long chatId, String s) {
        SendMessage msg = new SendMessage();
        msg.setChatId(chatId);
        msg.setText(s);

        try {
            execute(msg);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void sendPollToUser(long chatId) {
        SendPoll sendPoll = new SendPoll();
        sendPoll.setChatId(chatId);
        sendPoll.setQuestion("Which programming language do you like the most?");
        sendPoll.setOptions(List.of("Java", "Python", "JavaScript", "C++"));
        try {
            execute(sendPoll);
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

    public Map<Long, BlockGame> getGamesblock() {
        return gamesblock;
    }

    public int getStartblocgame() {
        return startblocgame;
    }

    public Quiz getQ() {
        return q;
    }

    public GetConfig getGetConfig() {
        return getConfig;
    }

    public boolean isGetstart() {
        return getstart;
    }

    public TelegramBotsApi getBotsApi() {
        return botsApi;
    }

    public boolean isRunning() {
        return running;
    }

    public int getChangegame() {
        return changegame;
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
    public void setGetstart(boolean getstart) {
        this.getstart = getstart;
    }

}
