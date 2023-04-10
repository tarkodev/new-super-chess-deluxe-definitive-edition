package fr.chess.deluxe.piece;

import fr.chess.deluxe.ChessBoard;
import fr.chess.deluxe.utils.ChessColor;
import fr.chess.deluxe.utils.Coordinates;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class ChessPiece implements Serializable {

    private final ChessPieceType type;
    private final ChessColor color;

    public ChessPiece(ChessPieceType type, ChessColor color) {
        this.type = type;
        this.color = color;
    }

    public ChessPieceType getType() {
        return type;
    }

    public ChessColor getPieceColor() {
        return color;
    }

    public String getId() {
        return color.getChar() + "" + type.getChar();
    }

    public Map<Coordinates, Consumer<Coordinates>> getPossibleMoves(ChessBoard chessBoard, Coordinates coordinates) {
        Map<Coordinates, Consumer<Coordinates>> result = new HashMap<>();
        type.getMovements().forEach(movement -> result.putAll(movement.getPossibleSquare(chessBoard, getPieceColor(), coordinates)));
        return result;
    }
}
