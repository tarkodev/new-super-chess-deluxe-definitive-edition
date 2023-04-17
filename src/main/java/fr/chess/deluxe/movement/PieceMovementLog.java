package fr.chess.deluxe.movement;

import fr.chess.deluxe.ChessBoard;
import fr.chess.deluxe.piece.ChessPiece;
import fr.chess.deluxe.utils.Coordinates;

public class PieceMovementLog {

    private final ChessPiece piece;
    private final Coordinates fromCoordinates;
    private final Coordinates toCoordinates;
    private final ChessBoard chessBoard;

    public PieceMovementLog(ChessPiece piece, Coordinates fromCoordinates, Coordinates toCoordinates, ChessBoard chessBoard) {
        this.piece = piece;
        this.fromCoordinates = fromCoordinates;
        this.toCoordinates = toCoordinates;
        this.chessBoard = chessBoard;
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

    public ChessBoard getChessBoard() {
        return chessBoard;
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
