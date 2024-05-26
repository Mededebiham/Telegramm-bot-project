package telegrambot;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("ALL")
public class GetConfig {
    private String userName;
    private String token;
    private String filePath;

    public GetConfig(String filePath) {
        this.filePath = filePath;
        getUserAndToken();

    }

    private List<String> readFile(String fileName) {
        List<String> toReadFile = new ArrayList<>();
        File file = new File(fileName);
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                toReadFile.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return toReadFile;
    }

    private void getUserAndToken() {
        List<String> config = readFile(this.filePath);
        String first = config.get(0);
        String second = config.get(1);
        this.userName = first.split("\\{:")[1];
        this.token = second.split("\\{:")[1];

    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
