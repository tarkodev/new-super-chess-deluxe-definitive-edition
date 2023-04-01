package fr.chess.deluxe.movement;

import fr.chess.deluxe.ChessSquare;

import java.util.List;

public record Move(ChessSquare to, boolean enPassant) {


	public boolean equals(Move m) {
		return to() == m.to && enPassant() == m.enPassant;
	}

	public boolean isIn(List<Move> list) {
		for (Move move : list) {
			if (this.equals(move))
				return true;
		}
		return false;
	}
}
