package fr.chess.deluxe.utils;

public class Castled {
	public boolean whiteLeft;
	public boolean whiteRight;
	public boolean blackLeft;
	public boolean blackRight;


	public Castled(boolean whiteLeft, boolean whiteRight, boolean blackLeft, boolean blackRight) {
		this.whiteLeft = whiteLeft;
		this.whiteRight = whiteRight;
		this.blackLeft = blackLeft;
		this.blackRight = blackLeft;
	}

	public Castled() {
		this(false, false, false, false);
	}
}
