package fr.chess.deluxe.piece;

import fr.chess.deluxe.movement.PieceMovement;
import fr.chess.deluxe.utils.ChessColor;

import java.util.Set;

public class ChessPieceKnight extends ChessPiece{

    public ChessPieceKnight(ChessColor chessColor) {
        super(chessColor);
    }

    @Override
    public Set<PieceMovement> getMovements() {
        return Set.of(PieceMovement.KNIGHT);
    }

    @Override
    public char getChar() {
        return 'N';
    }
}
