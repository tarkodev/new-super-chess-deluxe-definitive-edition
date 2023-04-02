package fr.chess.deluxe.movement;

import fr.chess.deluxe.piece.ChessPiece;
import fr.chess.deluxe.utils.Coordinates;

public class PieceMovementLog {

    private final ChessPiece piece;
    private final Coordinates fromCoordinates;
    private final Coordinates toCoordinates;

    public PieceMovementLog(ChessPiece chessPiece, Coordinates fromCoordinates, Coordinates toCoordinates) {
        piece = chessPiece;
        this.fromCoordinates = fromCoordinates;
        this.toCoordinates = toCoordinates;
    }

    public ChessPiece getPiece() {
        return piece;
    }

    public Coordinates getFromCoordinates() {
        return fromCoordinates;
    }

    public Coordinates getToCoordinates() {
        return toCoordinates;
    }
}
