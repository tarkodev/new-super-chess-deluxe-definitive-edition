package fr.chess.deluxe.piece;

import java.util.Set;

public class ChessPieceQueen extends ChessPiece {

    @Override
    public Set<Movement> getMovements() {
        return Set.of(Movement.TOUR, Movement.BISHOP);
    }
}
