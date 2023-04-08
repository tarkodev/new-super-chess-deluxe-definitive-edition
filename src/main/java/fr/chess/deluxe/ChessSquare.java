package fr.chess.deluxe;

import fr.chess.deluxe.piece.ChessPiece;
import fr.chess.deluxe.utils.Coordinates;
import javafx.scene.paint.Color;

public class ChessSquare {
    private final Coordinates coordinates;
    private final Color color;
    private ChessPiece piece;

    public Color getColor() {
        return color;
    }

    public ChessSquare(Color color, Coordinates coordinates) {
        this.color = color;
        this.coordinates = coordinates;
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
