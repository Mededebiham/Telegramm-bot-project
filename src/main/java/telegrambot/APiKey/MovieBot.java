package telegrambot.APiKey;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.json.JSONArray;
import org.json.JSONObject;
import telegrambot.GetConfig;

public class MovieBot extends TelegramLongPollingBot {
    private final String TMDB_API_KEY = "8fb16d4f7e76a2e6e0f952ee0918b5e9";

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            Message message = update.getMessage();
            String chatId = message.getChatId().toString();
            String text = message.getText();

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
            } else {
                sendResponse(chatId, "Commande non reconnue.\nUtilisez /movie <titre du film> pour rechercher un film.\nUtilisez /actor <nom de l'acteur> pour rechercher des films par acteur.\nUtilisez /title <mot-clé> pour rechercher des films par titre.");
            }
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

    @Override
    public String getBotUsername() {
        return new GetConfig("config.txt").getUserName();
    }

    @Override
    public String getBotToken() {
        return new GetConfig("config.txt").getToken();
    }


}
