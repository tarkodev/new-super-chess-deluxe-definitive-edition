package fr.chess.deluxe;

import fr.chess.deluxe.piece.ChessPiece;
import fr.chess.deluxe.utils.Coordinates;
import javafx.scene.paint.Color;

public class ChessSquare {
    private final Coordinates coordinates;
    private ChessPiece piece;


    public ChessSquare(Coordinates coordinates, ChessPiece piece) {
        this.coordinates = coordinates;
        this.piece = piece;
    }

    public ChessSquare(Coordinates coordinates) {
        this(coordinates, null);
    }

    public void setPiece(ChessPiece chessPiece) {
        this.piece = chessPiece;

    }

    public void removePiece() {
        this.piece = null;
    }

    public boolean hasPiece() {
        return this.piece != null;
    }

    public Color getColor() {
        return ((coordinates.getX() + coordinates.getY()) % 2) == 0 ? ChessRender.CHESS_SQUARE_COLOR_1 : ChessRender.CHESS_SQUARE_COLOR_2;
    }

    public ChessPiece getPiece() {
        return piece;
    }

    public Coordinates getCoordinates() {
        return coordinates.clone();
    }

    @Override
    public String toString() {
        return coordinates.toString();
    }


}
