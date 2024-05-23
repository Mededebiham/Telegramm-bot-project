package telegrambot.storingtelling;

import java.util.Map;

public class StoryNode {
    private String text;
    private Map<String, String> choices;


    public StoryNode(String text, Map<String, String> choices) {
        this.text = text;
        this.choices = choices;
    }

    public String getText() {
        return text;
    }

    public Map<String, String> getChoices() {
        return choices;
    }
}
