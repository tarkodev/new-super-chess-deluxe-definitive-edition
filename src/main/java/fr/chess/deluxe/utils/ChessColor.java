package fr.chess.deluxe.utils;

public enum ChessColor {
    WHITE('w'),
    BLACK('b');

    private final char c;

    ChessColor(char c) {
        this.c = c;
    }

    public char getChar() {
        return c;
    }
}
