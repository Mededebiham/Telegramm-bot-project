package telegrambot;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GetConfig {
    public List<String> readFile(String fileName){
        List<String>toReadFile=new ArrayList<>();
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
}
