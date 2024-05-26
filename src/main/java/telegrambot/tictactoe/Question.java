package telegrambot.tictactoe;

import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("ALL")
public class Question {
    private final String title;
    private final String text;
    private final List<String> options;
    private final String solution;

    public Question(String title, String text, List<String> options, String solution) {
        this.title = title;
        this.text = text;
        this.options = options;
        this.solution = solution;
    }

    public String toString() {
        String opt = this.options.stream().collect(Collectors.joining("\n"));
        return this.title + "\n" + this.text + "\n" + opt;
    }
}
