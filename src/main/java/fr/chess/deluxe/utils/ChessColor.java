package fr.chess.deluxe.utils;

import fr.chess.deluxe.ChessBoard;
import fr.chess.deluxe.ChessRender;
import javafx.scene.paint.Color;

public enum ChessColor {
    WHITE(7, -1, 'w', ChessRender.CHESS_SQUARE_COLOR_1),
    BLACK(0, 1, 'b', ChessRender.CHESS_SQUARE_COLOR_2);

    private final char character;
    private final int firstPiecesLine;
    private final int oneStep;
    private final Color color;

    ChessColor(int firstLine, int oneStep, char character, Color color) {
        this.character = character;
        this.firstPiecesLine = firstLine;
        this.oneStep = oneStep;
        this.color = color;
    }

    public char getChar() {
        return character;
    }

    public int getOneStep() {
        return oneStep;
    }

    public int getFirstPiecesLine() {
        return firstPiecesLine;
    }

    public Color getColor() {
        return color;
    }

    public ChessColor inverse() {
        return this == ChessColor.WHITE ? ChessColor.BLACK : ChessColor.WHITE;
    }
}
