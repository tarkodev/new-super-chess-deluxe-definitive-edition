package fr.chess.deluxe.piece;

import fr.chess.deluxe.utils.ChessColor;

import java.util.Set;

public class ChessPieceRook extends ChessPiece{

    public ChessPieceRook(ChessColor chessColor) {
        super(chessColor);
    }

    @Override
    public Set<PieceMovement> getMovements() {
        return Set.of(PieceMovement.ROOK);
    }

    @Override
    public char getChar() {
        return 'R';
    }
}
