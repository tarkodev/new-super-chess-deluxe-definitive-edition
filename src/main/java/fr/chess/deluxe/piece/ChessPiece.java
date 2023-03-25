package fr.chess.deluxe.piece;

import fr.chess.deluxe.ChessPieceType;
import fr.chess.deluxe.ChessSquare;

import java.util.Set;

public abstract class ChessPiece {

    private ChessPieceType chessPieceType;

    public abstract Set<Movement> getMovements();

    public void getPossibleCase(ChessSquare chessSquare) {

    }

}
