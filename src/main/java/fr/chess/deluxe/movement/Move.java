package fr.chess.deluxe.movement;

import fr.chess.deluxe.ChessSquare;
import fr.chess.deluxe.utils.Castled;

import java.util.List;

public record Move(ChessSquare to, boolean enPassant, Castled castled) {

	public boolean equals(Move m) {
		return this.to.equals(m.to) && this.enPassant == m.enPassant && this.castled.whiteLeft == m.castled.whiteLeft && this.castled.whiteRight == m.castled.whiteRight && this.castled.blackLeft == m.castled.blackLeft && this.castled.blackRight == m.castled.blackRight;
	}

	public boolean isIn(List<Move> list) {
		for (Move move : list) {
			if (this.equals(move))
				return true;
		}
		return false;
	}
}
