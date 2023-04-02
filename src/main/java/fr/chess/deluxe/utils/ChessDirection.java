package fr.chess.deluxe.utils;

public enum ChessDirection {

    LEFT(0, -1),
    RIGHT(7, 1);

    private final int firstLine;
    private final int oneForward;


    ChessDirection(int firstLine, int oneForward) {
        this.firstLine = firstLine;
        this.oneForward = oneForward;
    }

    public int getOneForward() {
        return oneForward;
    }

    public int getFirstLine() {
        return firstLine;
    }
}
