package fr.chess.deluxe.movement;

import fr.chess.deluxe.ChessBoard;
import fr.chess.deluxe.ChessSquare;
import fr.chess.deluxe.piece.ChessPiece;
import fr.chess.deluxe.utils.Coordinates;

public class PieceAction {

    private final Type type;
    private final Coordinates coordinates;
    private final ChessPiece piece;

    public PieceAction(Coordinates coordinates, Type type, ChessPiece chessPiece) {
        this.type = type;
        this.coordinates = coordinates;
        piece = chessPiece;
    }

    public void apply(ChessBoard chessBoard, boolean reverse) {
        ChessSquare square = chessBoard.getSquare(coordinates);
        if (type.equals(reverse ? Type.REMOVE : Type.SET)) {
            square.setPiece(piece);
        } else {
            square.removePiece();
        }
    }

    public Type getType() {
        return type;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public ChessPiece getPiece() {
        return piece;
    }

    @Override
    public String toString() {
        return "PieceAction{" +
                "type=" + type +
                ", coordinates=" + coordinates +
                ", piece=" + piece +
                '}';
    }

    public enum Type {
        SET,
        REMOVE
    }
}
