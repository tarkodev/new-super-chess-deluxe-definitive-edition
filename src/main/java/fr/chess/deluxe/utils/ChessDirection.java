package fr.chess.deluxe.utils;

import fr.chess.deluxe.ChessBoard;

/**
 * Utilisé pour le roque pour stocker les informations: le bord gauche et droit, ainsi que la direction que le roi
 *  et la tour doivent bouger pour faire le roque.
 */
public enum ChessDirection {

    LEFT(0, -1),
    RIGHT(ChessBoard.CHESS_SQUARE_LENGTH-1, 1);

    private final int borderLine;
    private final int oneStep;


    ChessDirection(int borderLine, int oneStep) {
        this.borderLine = borderLine;
        this.oneStep = oneStep;
    }

    public int getOneStep() {
        return oneStep;
    }

    public int getBorderLine() {
        return borderLine;
    }
}
