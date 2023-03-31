package fr.chess.deluxe.utils;

import fr.chess.deluxe.ChessBoard;
import javafx.scene.paint.Color;

public enum ChessColor {
    WHITE('w', ChessBoard.CHESS_SQUARE_COLOR_1),
    BLACK('b', ChessBoard.CHESS_SQUARE_COLOR_1);

    private final char c;
    private final Color color;

    ChessColor(char c, Color color) {
        this.c = c;
        this.color = color;
    }

    public char getChar() {
        return c;
    }

    public Color getColor() {
        return color;
    }
}
