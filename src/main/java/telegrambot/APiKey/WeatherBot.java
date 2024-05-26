package telegrambot.APiKey;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Location;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import telegrambot.GetConfig;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@SuppressWarnings("ALL")
public class WeatherBot extends TelegramLongPollingBot {
    private final String OPENWEATHERMAP_API_KEY = "cc47c7898ff675372c235a70e81c2de3";
    private boolean getstart;
    private TelegramBotsApi botsApi;
    public boolean running = true;
    public boolean getMain_Menu;

    public WeatherBot() {
        sendStartupMessage();
    }

    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasMessage()) {
            Message message = update.getMessage();
            String chatId = message.getChatId().toString();
            if (getstart) {
                getMain_Menu = false;
                if (message.hasText()) {
                    String text = message.getText();
                    if (text.startsWith("/weather")) {
                        String location = text.replace("/weather", "").trim();
                        String weatherInfo = getWeatherInfo(location);
                        sendResponse(chatId, weatherInfo);
                    } else if (text.startsWith("/forecast")) {
                        String location = text.replace("/forecast", "").trim();
                        String forecastInfo = getForecastInfo(location);
                        sendResponse(chatId, forecastInfo);
                    } else if (message.getText().equals("/quit")) {
                        sendResponse(chatId, "Bot is stopping: Goodbye!");
                        stopBot();

                    } else if (message.getText().equalsIgnoreCase("/main_menu")) {
                        getstart = false;
                        getMain_Menu = true;
                        sendResponse(chatId, "you go back to main menu!" +
                                "typ /yes to back Main menu, /no to continue /quit to close bot");
                    } else if (message.getText().equals("/cancel")) {
                        sendStartupMessage();
                        getstart = false;

                    } else {
                        sendResponse(chatId, " Invalid choice. Typ /weather <city> for current weather or /forecast <city> for forecast." +
                                "or /quit to stop bot or /cancel to begin");
                    }
                } else if (message.hasLocation()) {
                    Location location = message.getLocation();
                    String weatherInfo = getWeatherInfoByLocation(location.getLatitude(), location.getLongitude());
                    sendResponse(chatId, weatherInfo);
                }
            } else {
                if (message.getText().equals("/start")) {
                    sendResponse(chatId, " Typ /weather <city> for current weather or /forecast <city> for forecast." +
                            "or /quit to stop bot or /cancel to begin");
                    getstart = true;

                } else if (message.getText().equalsIgnoreCase("/main_menu")) {
                    getstart = false;
                    getMain_Menu = true;
                    sendResponse(chatId, "you go back to main menu!" +
                            "typ /yes to back Main menu, /no to continue /quit to close bot");
                } else if (message.getText().equals("/quit")) {
                    sendResponse(chatId, "Bot is stopping: Goodbye!");
                    stopBot();

                } else if (message.getText().equals("/cancel")) {
                    sendStartupMessage();
                    getstart = false;

                } else {
                    sendResponse(chatId, "in valide choice. type /start  to begin");
                }
            }


        }
    }

    private void sendStartupMessage() {
        SendMessage message = new SendMessage();

        message.setChatId("7163912977");
        message.setText("hello and welcome in My wheater  bot. please type /start to beginn or /quit to stop bot");
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private String getWeatherInfo(String location) {
        try {
            OkHttpClient client = new OkHttpClient();
            String url = "http://api.openweathermap.org/data/2.5/weather?q=" + location + "&appid=" + OPENWEATHERMAP_API_KEY + "&units=metric";
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) {
                return "Failed to fetch weather data for " + location;
            }
            JSONObject jsonResponse = new JSONObject(response.body().string());
            String weatherDescription = jsonResponse.getJSONArray("weather").getJSONObject(0).getString("description");
            double temperature = jsonResponse.getJSONObject("main").getDouble("temp");
            return "Current weather in " + location + ": " + weatherDescription + ", Temperature: " + temperature + "°C";
        } catch (IOException e) {
            e.printStackTrace();
            return "An error occurred while fetching weather data.";
        }
    }


    private String getForecastInfo(String location) {
        try {
            OkHttpClient client = new OkHttpClient();
            String url = "http://api.openweathermap.org/data/2.5/forecast?q=" + location + "&appid=" + OPENWEATHERMAP_API_KEY + "&units=metric";
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) {
                return "Failed to fetch forecast data for " + location;
            }
            JSONObject jsonResponse = new JSONObject(response.body().string());
            JSONArray forecastList = jsonResponse.getJSONArray("list");

            StringBuilder forecastInfo = new StringBuilder();
            forecastInfo.append("Weather forecast for ").append(location).append(":\n");

            for (int i = 0; i < forecastList.length(); i++) {
                JSONObject forecast = forecastList.getJSONObject(i);
                String dateTimeStr = forecast.getString("dt_txt");
                LocalDateTime dateTime = LocalDateTime.parse(dateTimeStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

                LocalDate date = dateTime.toLocalDate();
                LocalTime time = dateTime.toLocalTime();

                JSONObject main = forecast.getJSONObject("main");
                double temperature = main.getDouble("temp");

                JSONArray weatherArray = forecast.getJSONArray("weather");
                String description = weatherArray.getJSONObject(0).getString("description");

                forecastInfo.append(date).append(" ").append(time).append(": ").append(description).append(", Temperature: ").append(temperature).append("°C\n");
            }
            return forecastInfo.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return "An error occurred while fetching forecast data.";
        }
    }


    private String getWeatherInfoByLocation(double latitude, double longitude) {
        try {
            OkHttpClient client = new OkHttpClient();
            String url = "http://api.openweathermap.org/data/2.5/weather?lat=" + latitude + "&lon=" + longitude + "&appid=" + OPENWEATHERMAP_API_KEY + "&units=metric";
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) {
                return "Failed to fetch weather data for the provided location.";
            }
            JSONObject jsonResponse = new JSONObject(response.body().string());
            String location = jsonResponse.getString("name");
            String weatherDescription = jsonResponse.getJSONArray("weather").getJSONObject(0).getString("description");
            double temperature = jsonResponse.getJSONObject("main").getDouble("temp");
            return "Current weather in " + location + ": " + weatherDescription + ", Temperature: " + temperature + "°C";
        } catch (IOException e) {
            e.printStackTrace();
            return "An error occurred while fetching weather data.";
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

    public boolean isGetstart() {
        return getstart;
    }

    public boolean isRunning() {
        return running;
    }
}

