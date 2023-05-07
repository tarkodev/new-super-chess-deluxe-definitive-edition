package fr.chess.deluxe.piece;

import fr.chess.deluxe.movement.PieceMovement;

import java.util.Set;

public enum ChessPieceType {

    PAWN('P', Set.of(PieceMovement.PAWN)),
    KNIGHT('N', Set.of(PieceMovement.KNIGHT)),
    BISHOP('B', Set.of(PieceMovement.BISHOP)),
    ROOK('R', Set.of(PieceMovement.ROOK)),
    QUEEN('Q', Set.of(PieceMovement.ROOK, PieceMovement.BISHOP)),
    KING('K', Set.of(PieceMovement.KING)),
    PRINCESS('X', Set.of(PieceMovement.KNIGHT, PieceMovement.BISHOP)),
    EMPRESS('E', Set.of(PieceMovement.KNIGHT, PieceMovement.ROOK)),
    NIGHTRIDER('S', Set.of(PieceMovement.NIGHTRIDER)),
    MASTODON('M', Set.of(PieceMovement.MASTODON));

    private final char character;
    private final Set<PieceMovement> movements;
    ChessPieceType(char character, Set<PieceMovement> movements) {
        this.character = character;
        this.movements = movements;
    }

    public char getChar() {
        return character;
    }

    public Set<PieceMovement> getMovements() {
        return movements;
    }

    public String getName() {
        return name().toLowerCase();
    }
}
