package fr.chess.deluxe.piece;

import fr.chess.deluxe.ChessBoard;
import fr.chess.deluxe.movement.PieceMovement;
import fr.chess.deluxe.utils.ChessColor;
import fr.chess.deluxe.utils.Coordinates;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

public abstract class ChessPiece {

    private final ChessColor chessColor;

    protected ChessPiece(ChessColor chessColor) {
        this.chessColor = chessColor;
    }

    public ChessColor getPieceColor() {
        return chessColor;
    }

    public abstract Set<PieceMovement> getMovements();

    public String getId() {
        return chessColor.getChar() + "" + this.getChar();
    }

    public abstract char getChar();

    public Map<Coordinates, Consumer<Coordinates>> getPossibleMoves(ChessBoard chessBoard, Coordinates coordinates) {
        Map<Coordinates, Consumer<Coordinates>> result = new HashMap<>();
        getMovements().forEach(movement -> result.putAll(movement.getPossibleSquare(chessBoard, getPieceColor(), coordinates)));
        return result;
    }
}
