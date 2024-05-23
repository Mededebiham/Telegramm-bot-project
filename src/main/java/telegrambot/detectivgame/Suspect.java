package telegrambot.detectivgame;

public class Suspect {
    private String name;
    private String alibi;
    private String[] clues;

    public Suspect(String name, String alibi, String[] clues) {
        this.name = name;
        this.alibi = alibi;
        this.clues = clues;
    }

    public String getName() {
        return name;
    }

    public String getAlibi() {
        return alibi;
    }

    public String[] getClues() {
        return clues;
    }
}
