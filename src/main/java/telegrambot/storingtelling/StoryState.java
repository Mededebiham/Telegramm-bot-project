package telegrambot.storingtelling;

import telegrambot.detectivgame.Suspect;

import java.util.HashMap;
import java.util.Map;

public class StoryState {
    private boolean startGame;
    private static final Map<String, StoryNode> storyNodes = new HashMap<>();
    private boolean correctInput;


    static {
        // Define story nodes
        storyNodes.put("start", new StoryNode(
                "You are in a dark forest. Do you go left or right?",
                Map.of("left", "left", "right", "right")
        ));
        storyNodes.put("left", new StoryNode(
                "You encounter a wolf. Do you fight or run?",
                Map.of("fight", "fight", "run", "run")
        ));
        storyNodes.put("right", new StoryNode(
                "You find a village. Do you talk to the villagers or explore alone?",
                Map.of("talk", "talk", "explore", "explore")
        ));
        storyNodes.put("fight", new StoryNode(
                "You bravely fight the wolf and win! do you want restart or stop?",
                Map.of("restart", "restart", "stop", "stop")
        ));
        storyNodes.put("stop", new StoryNode(
                "The end.",
                Map.of()
        ));
        storyNodes.put("restart", new StoryNode(
                "You are in a dark forest. Do you go left or right?",
                Map.of("left", "left", "right", "right")
        ));
        storyNodes.put("run", new StoryNode(
                "You run away safely but miss out on an adventure. do you want restart or stop",
                Map.of("restart", "restart", "stop", "stop")
        ));
        storyNodes.put("talk", new StoryNode(
                "The villagers welcome you and you find a new home. do you want restart or stop",
                Map.of("restart", "restart", "stop", "stop")
        ));
        storyNodes.put("explore", new StoryNode(
                "You get lost in the forest and have to find your way back. do you want restart or stop",
                Map.of("restart", "restart", "stop", "stop")
        ));
    }


    public String processUserMessage(String curentMessage, String previousMessage) {
        String response = "";
        StoryNode currentNode = storyNodes.get(curentMessage);
        if (startGame) {
            if (currentNode != null) {
                if (curentMessage.equals("stop") || curentMessage.equals("start") || curentMessage.equals("restart")) {
                    response = currentNode.getText();
                    correctInput=true;
                } else {

                    StoryNode eventual = storyNodes.get(previousMessage);
                    Map<String, String> choices = eventual.getChoices();
                    if (choices.containsValue(curentMessage)) {
                        response = currentNode.getText();
                        correctInput=true;
                    } else {
                        correctInput=false;
                        response = "invalide choice " + eventual.getText();

                    }


                }

                if (curentMessage.equals("stop")) {
                    startGame = false;
                }
            } else {
                StoryNode eventual = storyNodes.get(previousMessage);
                correctInput=false;
                return "invalide choice " + eventual.getText();

            }
        } else {
            if (curentMessage.equals("start")) {
                response = storyNodes.get("start").getText();
                startGame = true;
                correctInput=true;
            } else {
                correctInput=false;
                response = "invalid choise. Please enter start to begin or stop to cancel or /quit to stop bot";
            }

        }


        return response;
    }

    public boolean isCorrectInput() {
        return correctInput;
    }
}


