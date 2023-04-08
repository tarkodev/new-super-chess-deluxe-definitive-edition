package fr.chess.deluxe.movement;

import fr.chess.deluxe.ChessBoard;
import fr.chess.deluxe.piece.ChessPiece;
import fr.chess.deluxe.utils.Coordinates;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PieceMovementLog {

    private final ChessPiece piece;
    private final Coordinates fromCoordinates;
    private final Coordinates toCoordinates;
    private final List<PieceAction> pieceActions;

    public PieceMovementLog(ChessPiece chessPiece, Coordinates fromCoordinates, Coordinates toCoordinates) {
        piece = chessPiece;
        this.fromCoordinates = fromCoordinates;
        this.toCoordinates = toCoordinates;
        this.pieceActions = new ArrayList<>();
    }

    public void apply(ChessBoard chessBoard, boolean reverse) {
        if (reverse) Collections.reverse(pieceActions);
        pieceActions.forEach(pieceAction -> pieceAction.apply(chessBoard, reverse));
        if (reverse) Collections.reverse(pieceActions);
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

    public List<PieceAction> getPieceActions() {
        return pieceActions;
    }


}
