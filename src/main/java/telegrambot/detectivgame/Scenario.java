package telegrambot.detectivgame;

import java.util.Map;

@SuppressWarnings("ALL")
public class Scenario {
    private String description;
    private Map<String, Suspect> suspects;
    private String solution;

    public Scenario(String description, Map<String, Suspect> suspects, String solution) {
        this.description = description;
        this.suspects = suspects;
        this.solution = solution;
    }

    public String getDescription() {
        return description;
    }

    public Map<String, Suspect> getSuspects() {
        return suspects;
    }

    public String getSolution() {
        return solution;
    }
}

