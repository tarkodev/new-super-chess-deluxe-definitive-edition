package fr.chess.deluxe.utils;

import fr.chess.deluxe.ChessBoard;
import fr.chess.deluxe.ChessRender;
import javafx.scene.paint.Color;

/**
 * Contient les informations pour les noirs et les blancs:
 *  -Leur ligne la plus à l'arrière (celle sur laquelle le roi est)
 *  -Le sens dans lequel les pions (et les mastodons) se déplacent
 *  -le charactère utilisé dans la notation pour caractériser cette couleur d'équipe
 *  -le nom de la couleur d'équipe
 *  -la couleur de l'équipe
 */
public enum ChessColor {
    WHITE(ChessBoard.CHESS_SQUARE_LENGTH-1, -1, 'w', "White", ChessRender.CHESS_SQUARE_COLOR_1),
    BLACK(0, 1, 'b', "Black", ChessRender.CHESS_SQUARE_COLOR_2);


    private final int firstPiecesLine;
    private final int oneStep;

    private final char character;

    private final String name;
    private final Color color;

    ChessColor(int firstLine, int oneStep, char character, String name, Color color) {
        this.character = character;
        this.firstPiecesLine = firstLine;
        this.oneStep = oneStep;
        this.name = name;
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

    public ChessColor toggle() {
        return this == ChessColor.WHITE ? ChessColor.BLACK : ChessColor.WHITE;
    }
}
