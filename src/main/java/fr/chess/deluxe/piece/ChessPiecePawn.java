package fr.chess.deluxe.piece;

import fr.chess.deluxe.utils.ChessColor;

import java.util.Set;

public class ChessPiecePawn extends ChessPiece {

    public ChessPiecePawn(ChessColor chessColor) {
        super(chessColor);
    }

    @Override
    public Set<PieceMovement> getMovements() {
        return Set.of(PieceMovement.PAWN);
    }

    @Override
    public char getChar() {
        return 'P';
    }
}
