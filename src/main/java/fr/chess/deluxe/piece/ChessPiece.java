package fr.chess.deluxe.piece;

import fr.chess.deluxe.utils.ChessColor;

import java.util.Set;

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

}
