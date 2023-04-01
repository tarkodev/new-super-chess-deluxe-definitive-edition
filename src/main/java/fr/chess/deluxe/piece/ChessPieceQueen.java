package fr.chess.deluxe.piece;

import fr.chess.deluxe.movement.PieceMovement;
import fr.chess.deluxe.utils.ChessColor;

import java.util.Set;

public class ChessPieceQueen extends ChessPiece {

    public ChessPieceQueen(ChessColor chessColor) {
        super(chessColor);
    }

    @Override
    public Set<PieceMovement> getMovements() {
        return Set.of(PieceMovement.ROOK, PieceMovement.BISHOP);
    }

    @Override
    public char getChar() {
        return 'Q';
    }
}
