package telegrambot.tictactoe;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BlockGame {
    private List<Integer> board;
    private final int size = 3;

    public BlockGame() {
        resetGame();
    }

    public void resetGame() {
        board = new ArrayList<>();
        for (int i = 1; i <= size * size - 1; i++) {
            board.add(i);
        }
        board.add(0); // 0 represents the empty space
        Collections.shuffle(board);
    }

    public boolean move(int number) {
        int index = board.indexOf(number);
        int emptyIndex = board.indexOf(0);

        if (isAdjacent(index, emptyIndex)) {
            Collections.swap(board, index, emptyIndex);
            return true;
        }
        return false;
    }

    private boolean isAdjacent(int index1, int index2) {
        int row1 = index1 / size;
        int col1 = index1 % size;
        int row2 = index2 / size;
        int col2 = index2 % size;

        return (Math.abs(row1 - row2) == 1 && col1 == col2) || (Math.abs(col1 - col2) == 1 && row1 == row2);
    }

    public boolean isSolved() {
        for (int i = 0; i < size * size - 1; i++) {
            if (board.get(i) != i + 1) {
                return false;
            }
        }
        return true;
    }

    public String getBoardString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                int value = board.get(i * size + j);
                sb.append(value == 0 ? " " : value).append("\t");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
