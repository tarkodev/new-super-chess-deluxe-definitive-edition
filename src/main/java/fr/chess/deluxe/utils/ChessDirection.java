package fr.chess.deluxe.utils;

public enum ChessDirection {

    LEFT(0, -1),
    RIGHT(7, 1);

    private final int firstLine;
    private final int oneStep;


    ChessDirection(int firstLine, int oneStep) {
        this.firstLine = firstLine;
        this.oneStep = oneStep;
    }

    public int getOneStep() {
        return oneStep;
    }

    public int getFirstLine() {
        return firstLine;
    }
}
