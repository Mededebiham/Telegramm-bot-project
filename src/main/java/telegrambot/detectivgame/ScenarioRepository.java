package telegrambot.detectivgame;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("ALL")
public class ScenarioRepository {
    private static Map<String, Scenario> scenarios = new HashMap<>();

    static {
        Map<String, Suspect> suspects1 = new HashMap<>();
        suspects1.put("John Doe", new Suspect("John Doe", "Was in his office preparing for an exhibition.", new String[]{"Seen by the security guard at 8 PM"}));
        suspects1.put("Jane Smith", new Suspect("Jane Smith", "Was visiting a friend in another city.", new String[]{"Train ticket to another city at 6 PM"}));
        scenarios.put("stolen_painting", new Scenario("A famous painting has been stolen from the city museum.", suspects1, "The security guard is the thief."));
    }

    public static Scenario getScenario(String name) {
        return scenarios.get(name);
    }
}
