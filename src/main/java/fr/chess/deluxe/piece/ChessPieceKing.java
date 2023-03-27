package fr.chess.deluxe.piece;

import fr.chess.deluxe.utils.ChessColor;

import java.util.Set;

public class ChessPieceKing extends ChessPiece{

    public ChessPieceKing(ChessColor chessColor) {
        super(chessColor);
    }

    @Override
    public Set<PieceMovement> getMovements() {
        return Set.of(PieceMovement.KING);
    }

    @Override
    public char getChar() {
        return 'K';
    }
}
