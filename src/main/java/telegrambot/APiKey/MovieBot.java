package telegrambot.APiKey;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import telegrambot.GetConfig;

@SuppressWarnings("ALL")
public class MovieBot extends TelegramLongPollingBot {
    private final String TMDB_API_KEY = "8fb16d4f7e76a2e6e0f952ee0918b5e9";
    private boolean getstart;
    private TelegramBotsApi botsApi;
    public boolean running = true;
    public boolean getMain_Menu;

    public MovieBot() {
        sendStartupMessage();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            Message message = update.getMessage();
            String chatId = message.getChatId().toString();
            String text = message.getText();

            if (getstart) {
                getMain_Menu = false;
                if (text.startsWith("/movie")) {
                    String query = text.replace("/movie", "").trim();
                    String movieInfo = getMovieInfo(query);
                    sendResponse(chatId, movieInfo);
                } else if (text.startsWith("/actor")) {
                    String actor = text.replace("/actor", "").trim();
                    String actorMovies = getMoviesByActor(actor);
                    sendResponse(chatId, actorMovies);
                } else if (text.startsWith("/title")) {
                    String title = text.replace("/title", "").trim();
                    String titleMovies = getMoviesByTitle(title);
                    sendResponse(chatId, titleMovies);
                } else if (text.equals("/cancel")) {
                    sendStartupMessage();
                    getstart = false;


                } else if (text.equalsIgnoreCase("/main_menu")) {
                    getstart = false;
                    getMain_Menu = true;
                    sendResponse(chatId, "you go back to main menu!" +
                            "typ /yes to back Main menu, /no to continue /quit to close bot");
                } else if (text.equals("/quit")) {
                    sendResponse(chatId, "Bot is stopping: Goodbye!");
                    stopBot();

                } else {
                    sendResponse(chatId, "Invalid choice. type /movie <Title> to search film by Title, " +
                            "/actor <name of actor> to search fim by actor, /title <keyword> to search " +
                            "for movies by title. /cancel to quit game and return to menu. /quit to stop bot");
                }

            } else {
                if (text.equals("/start")) {
                    sendResponse(chatId, "now  type /movie <Title> to search film by Title, " +
                            "/actor <name of actor> to search fim by actor, /title <keyword> to search " +
                            "for movies by title. /cancel to quit game and return to menu. /quit to stop bot");
                    getstart = true;

                } else if (text.equalsIgnoreCase("/main_menu")) {
                    getstart = false;
                    getMain_Menu = true;
                    sendResponse(chatId, "you go back to main menu!" +
                            "typ /yes to back Main menu, /no to continue /quit to close bot");
                } else if (text.equals("/quit")) {
                    sendResponse(chatId, "Bot is stopping: Goodbye!");
                    stopBot();

                } else {
                    sendResponse(chatId, "in valide choice. type /start  to begin");
                }
            }

        }
    }

    private void sendStartupMessage() {
        SendMessage message = new SendMessage();
        message.setChatId("7163912977");
        message.setText("hello and welcome in  Movie Bot. please type /start to beginn");
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private String getMovieInfo(String query) {
        try {
            OkHttpClient client = new OkHttpClient();
            String url = "https://api.themoviedb.org/3/search/movie?api_key=" + TMDB_API_KEY + "&query=" + query;
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) {
                return "Failed to fetch movie data.";
            }
            JSONObject jsonResponse = new JSONObject(response.body().string());
            JSONArray results = jsonResponse.getJSONArray("results");
            if (results.length() == 0) {
                return "No movie found with that title.";
            }
            JSONObject movie = results.getJSONObject(0); // Get the first movie in the results
            String title = movie.getString("title");
            String overview = movie.getString("overview");
            return "Title: " + title + "\nOverview: " + overview;
        } catch (Exception e) {
            e.printStackTrace();
            return "An error occurred while fetching movie data.";
        }
    }

    private String getMoviesByActor(String actor) {
        try {
            OkHttpClient client = new OkHttpClient();
            String url = "https://api.themoviedb.org/3/search/person?api_key=" + TMDB_API_KEY + "&query=" + actor;
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) {
                return "Failed to fetch actor data.";
            }
            JSONObject jsonResponse = new JSONObject(response.body().string());
            JSONArray results = jsonResponse.getJSONArray("results");
            if (results.length() == 0) {
                return "No actor found with that name.";
            }
            JSONObject actorData = results.getJSONObject(0); // Get the first actor in the results
            int actorId = actorData.getInt("id");

            url = "https://api.themoviedb.org/3/person/" + actorId + "/movie_credits?api_key=" + TMDB_API_KEY;
            request = new Request.Builder()
                    .url(url)
                    .build();
            response = client.newCall(request).execute();
            if (!response.isSuccessful()) {
                return "Failed to fetch movie credits for the actor.";
            }
            jsonResponse = new JSONObject(response.body().string());
            JSONArray movieCredits = jsonResponse.getJSONArray("cast");
            if (movieCredits.length() == 0) {
                return "No movies found for this actor.";
            }
            StringBuilder moviesList = new StringBuilder();
            moviesList.append("Movies featuring ").append(actor).append(":\n");
            for (int i = 0; i < Math.min(5, movieCredits.length()); i++) { // Limit to 5 movies
                JSONObject movie = movieCredits.getJSONObject(i);
                String movieTitle = movie.getString("title");
                moviesList.append("- ").append(movieTitle).append("\n");
            }
            return moviesList.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "An error occurred while fetching actor data.";
        }
    }

    private String getMoviesByTitle(String title) {
        try {
            OkHttpClient client = new OkHttpClient();
            String url = "https://api.themoviedb.org/3/search/movie?api_key=" + TMDB_API_KEY + "&query=" + title;
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) {
                return "Failed to fetch movie data.";
            }
            JSONObject jsonResponse = new JSONObject(response.body().string());
            JSONArray results = jsonResponse.getJSONArray("results");
            if (results.length() == 0) {
                return "No movie found with that title.";
            }
            StringBuilder moviesList = new StringBuilder();
            moviesList.append("Movies matching ").append(title).append(":\n");
            for (int i = 0; i < Math.min(5, results.length()); i++) { // Limit to 5 movies
                JSONObject movie = results.getJSONObject(i);
                String movieTitle = movie.getString("title");
                moviesList.append("- ").append(movieTitle).append("\n");
            }
            return moviesList.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "An error occurred while fetching movie data.";
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

    @Override
    public String getBotUsername() {
        return new GetConfig("config.txt").getUserName();
    }

    @Override
    public String getBotToken() {
        return new GetConfig("config.txt").getToken();
    }

    public boolean isGetstart() {
        return getstart;
    }

    public void setGetstart(boolean getstart) {
        this.getstart = getstart;
    }

    public boolean isRunning() {
        return running;
    }

    private void sendWelcomeMessage(long chatId) {
        sendResponse(String.valueOf(chatId), "hello and welcome in  Movie Bot! Type /start to begin, /cancel to get start or /quit to stop  bot." +
                " type /Where were you  to get position, /clues to get clues and /solution to get solution");
    }

}
