package fr.chess.deluxe.utils;

import fr.chess.deluxe.ChessBoard;
import javafx.scene.paint.Color;

public enum ChessColor {
    WHITE(7, -1, 'w', ChessBoard.CHESS_SQUARE_COLOR_1),
    BLACK(0, 1, 'b', ChessBoard.CHESS_SQUARE_COLOR_2);

    private final char character;
    private final int firstPiecesLine;
    private final int oneForward;
    private final Color color;

    ChessColor(int firstLine, int oneForward, char character, Color color) {
        this.character = character;
        this.firstPiecesLine = firstLine;
        this.oneForward = oneForward;
        this.color = color;
    }

    public char getChar() {
        return character;
    }

    public int getOneForward() {
        return oneForward;
    }

    public int getFirstPiecesLine() {
        return firstPiecesLine;
    }

    public Color getColor() {
        return color;
    }
}
