package fr.chess.deluxe;

import fr.chess.deluxe.piece.ChessPiece;
import fr.chess.deluxe.utils.Coordinates;
import javafx.scene.paint.Color;

import java.io.Serializable;

public class ChessSquare implements Serializable {
    private final Coordinates coordinates;
    private ChessPiece piece;

    public ChessSquare(Coordinates coordinates) {
        this.coordinates = coordinates;
        this.piece = null;
    }

    public ChessSquare(ChessSquare chessSquare, Coordinates coordinates) {
        this.coordinates = coordinates;
        this.piece = chessSquare.getPiece();
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
