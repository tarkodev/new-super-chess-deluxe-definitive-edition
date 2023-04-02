package fr.chess.deluxe.utils;

public class CastleInfo {
	private boolean whiteKingMoved;
	private boolean whiteLeftRookMoved;
	private boolean whiteRightRookMoved;
	private boolean blackKingMoved;
	private boolean blackLeftRookMoved;
	private boolean blackRightRookMoved;

	public CastleInfo() {
		whiteKingMoved = false;
		whiteLeftRookMoved = false;
		whiteRightRookMoved = false;
		blackKingMoved = false;
		blackLeftRookMoved = false;
		blackRightRookMoved = false;
	}

	public boolean getWhiteKingMoved() {
		return whiteKingMoved;
	}

	public boolean getWhiteLeftRookMoved() {
		return whiteLeftRookMoved;
	}

	public boolean getWhiteRightRookMoved() {
		return whiteRightRookMoved;
	}

	public boolean getBlackKingMoved() {
		return blackKingMoved;
	}

	public boolean getBlackLeftRookMoved() {
		return blackLeftRookMoved;
	}

	public boolean getBlackRightRookMoved() {
		return blackRightRookMoved;
	}

	public void setWhiteKingMoved(boolean whiteKingMoved) {
		this.whiteKingMoved = whiteKingMoved;
	}

	public void setWhiteLeftRookMoved(boolean whiteLeftRookMoved) {
		this.whiteLeftRookMoved = whiteLeftRookMoved;
	}

	public void setWhiteRightRookMoved(boolean whiteRightRookMoved) {
		this.whiteRightRookMoved = whiteRightRookMoved;
	}

	public void setBlackKingMoved(boolean blackKingMoved) {
		this.blackKingMoved = blackKingMoved;
	}

	public void setBlackLeftRookMoved(boolean blackLeftRookMoved) {
		this.blackLeftRookMoved = blackLeftRookMoved;
	}

	public void setBlackRightRookMoved(boolean blackRightRookMoved) {
		this.blackRightRookMoved = blackRightRookMoved;
	}
}
