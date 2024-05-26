package telegrambot.example.dame;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class Board {
    private char[][] board = new char[8][8];
    private boolean isWhiteTurn = true;
    private boolean drawOffer = false;

    public Board() {
        initializeBoard();
    }

    private void initializeBoard() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if ((i + j) % 2 != 0) {
                    if (i < 3) {
                        board[i][j] = 'b'; // Black pieces
                    } else if (i > 4) {
                        board[i][j] = 'w'; // White pieces
                    } else {
                        board[i][j] = ' ';
                    }
                } else {
                    board[i][j] = ' ';
                }
            }
        }
    }

    public void resetBoard() {
        initializeBoard();
        isWhiteTurn = true;
        drawOffer = false;
    }

    private InputStream renderBoardImage() {
        int tileSize = 100;
        int imageSize = tileSize * 8;
        BufferedImage bufferedImage = new BufferedImage(imageSize, imageSize, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = bufferedImage.createGraphics();

        // Draw the board
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if ((i + j) % 2 == 0) {
                    g.setColor(Color.WHITE);
                } else {
                    g.setColor(Color.GRAY);
                }
                g.fillRect(j * tileSize, i * tileSize, tileSize, tileSize);

                // Draw the pieces
                if (board[i][j] != ' ') {
                    g.setColor(board[i][j] == 'w' || board[i][j] == 'W' ? Color.WHITE : Color.BLACK);
                    g.fillOval(j * tileSize + 10, i * tileSize + 10, tileSize - 20, tileSize - 20);
                    if (Character.isUpperCase(board[i][j])) {
                        g.setColor(Color.RED);
                        g.drawOval(j * tileSize + 10, i * tileSize + 10, tileSize - 20, tileSize - 20);
                    }
                }
            }
        }

        g.dispose();

        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "png", os);
            return new ByteArrayInputStream(os.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private boolean isValidMove(String move) {
        //check move format
        if (!move.matches("[a-h][1-8] to [a-h][1-8]")) {
            return false;
        }

        String[] positions = move.split(" to ");
        String from = positions[0];
        String to = positions[1];

        int fromRow = 8 - Character.getNumericValue(from.charAt(1));
        int fromCol = from.charAt(0) - 'a';
        int toRow = 8 - Character.getNumericValue(to.charAt(1));
        int toCol = to.charAt(0) - 'a';

        char piece = board[fromRow][fromCol];
        char destination = board[toRow][toCol];
//check that destination is empty
        if (destination != ' ' || fromRow == toRow || fromCol == toCol) {
            return false;
        }

        int rowDiff = Math.abs(toRow - fromRow);
        int colDiff = Math.abs(toCol - fromCol);

        if (piece == 'w' || piece == 'b') {
            if (isWhiteTurn && piece != 'w' || !isWhiteTurn && piece != 'b') {
                return false;
            }
            if (isWhiteTurn && piece == 'w') {
                if (rowDiff == 1 && colDiff == 1) {
                    return true;
                }

                if (rowDiff == 2 && (colDiff == 2||colDiff == -2)) {
                    int capturedRow = (fromRow + toRow) / 2;
                    int capturedCol = (fromCol + toCol) / 2;
                    char capturedPiece = board[capturedRow][capturedCol];
                    if ((isWhiteTurn && capturedPiece == 'b') || (!isWhiteTurn && capturedPiece == 'w')) {
                        return true;
                    }
                }

            }
            if (!isWhiteTurn && piece == 'b') {
                if (rowDiff == -1 && (colDiff == 1 || colDiff == -1)) {
                    return true;
                }
                if (rowDiff == -2 && (colDiff == 2 || colDiff == -2)) {
                    int capturedRow = (fromRow + toRow) / 2;
                    int capturedCol = (fromCol + toCol) / 2;
                    char capturedPiece = board[capturedRow][capturedCol];
                    if (capturedPiece == 'w' || capturedPiece == 'W') {
                        return true; // Capture en bas à droite
                    }
                }

            }

            return false;
        }

        if (piece == 'W' || piece == 'B') {
            if (isWhiteTurn && piece != 'W' || !isWhiteTurn && piece != 'B') {
                return false;
            }

            if (rowDiff == colDiff) {
                int rowDir = (toRow - fromRow) / rowDiff;
                int colDir = (toCol - fromCol) / colDiff;

                for (int i = 1; i < rowDiff; i++) {
                    if (board[fromRow + i * rowDir][fromCol + i * colDir] != ' ') {
                        return false;
                    }
                }

                return true;
            }

            return false;
        }
        if (Character.toUpperCase(piece) == 'W' || Character.toUpperCase(piece) == 'B') {
            if (rowDiff == colDiff) {
                int rowDir = (toRow - fromRow) / rowDiff;
                int colDir = (toCol - fromCol) / colDiff;

                for (int i = 1; i < rowDiff; i++) {
                    if (board[fromRow + i * rowDir][fromCol + i * colDir] != ' ') {
                        return false;
                    }
                }

                return true;
            }

            return false;
        }

        return false;
    }

    private boolean canCaptureMore(int row, int col) {
        char piece = board[row][col];
        int[][] directions = {{-2, -2}, {-2, 2}, {2, -2}, {2, 2}};

        for (int[] direction : directions) {
            int newRow = row + direction[0];
            int newCol = col + direction[1];

            if (newRow >= 0 && newRow < 8 && newCol >= 0 && newCol < 8) {
                if (isValidMove((char) (col + 'a') + "" + (8 - row) + " to " + (char) (newCol + 'a') + "" + (8 - newRow))) {
                    return true;
                }
            }
        }

        return false;
    }

    private void makeMove(String move) {
        String[] positions = move.split(" to ");
        String from = positions[0];
        String to = positions[1];

        int fromRow = 8 - Character.getNumericValue(from.charAt(1));
        int fromCol = from.charAt(0) - 'a';
        int toRow = 8 - Character.getNumericValue(to.charAt(1));
        int toCol = to.charAt(0) - 'a';

        char piece = board[fromRow][fromCol];
        board[fromRow][fromCol] = ' ';
        board[toRow][toCol] = piece;

        if (Math.abs(toRow - fromRow) == 2) {
            int capturedRow = (fromRow + toRow) / 2;
            int capturedCol = (fromCol + toCol) / 2;
            board[capturedRow][capturedCol] = ' ';
        }

        if (piece == 'w' && toRow == 0) {
            board[toRow][toCol] = 'W';
        } else if (piece == 'b' && toRow == 7) {
            board[toRow][toCol] = 'B';
        }

        if (Math.abs(toRow - fromRow) == 2 && canCaptureMore(toRow, toCol)) {
            isWhiteTurn = !isWhiteTurn;
        }
    }

    private boolean isGameOver() {
        boolean whiteExists = false;
        boolean blackExists = false;

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j] == 'w' || board[i][j] == 'W') {
                    whiteExists = true;
                }
                if (board[i][j] == 'b' || board[i][j] == 'B') {
                    blackExists = true;
                }
            }
        }

        return !whiteExists || !blackExists;
    }

    public char[][] getBoard() {
        return board;
    }

    public void setBoard(char[][] board) {
        this.board = board;
    }

    public boolean isWhiteTurn() {
        return isWhiteTurn;
    }

    public void setWhiteTurn(boolean whiteTurn) {
        isWhiteTurn = whiteTurn;
    }

    public boolean isDrawOffer() {
        return drawOffer;
    }

    public void setDrawOffer(boolean drawOffer) {
        this.drawOffer = drawOffer;
    }
}
