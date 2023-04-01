package fr.chess.deluxe.piece;

import fr.chess.deluxe.movement.PieceMovement;
import fr.chess.deluxe.utils.ChessColor;

import java.util.Set;

public class ChessPieceBishop extends ChessPiece{

    public ChessPieceBishop(ChessColor chessColor) {
        super(chessColor);
    }

    @Override
    public Set<PieceMovement> getMovements() {
        return Set.of(PieceMovement.BISHOP);
    }

    @Override
    public char getChar() {
        return 'B';
    }
}
