package fr.chess.deluxe.movement;

import fr.chess.deluxe.piece.ChessPiece;
import fr.chess.deluxe.utils.Coordinates;

public class PieceMovementLog {

    private final ChessPiece piece;
    private final Coordinates fromCoordinates;
    private final Coordinates toCoordinates;
    private final String chessBoardJson;

    public PieceMovementLog(ChessPiece piece, Coordinates fromCoordinates, Coordinates toCoordinates, String chessBoardJson) {
        this.piece = piece;
        this.fromCoordinates = fromCoordinates;
        this.toCoordinates = toCoordinates;
        this.chessBoardJson = chessBoardJson;
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

    public String getChessBoardJson() {
        return chessBoardJson;
    }

    @Override
    public String toString() {
        return "PieceMovementLog{" +
                "movedPiece=" + piece +
                ", fromCoordinates=" + fromCoordinates +
                ", toCoordinates=" + toCoordinates +
                '}';
    }
}
