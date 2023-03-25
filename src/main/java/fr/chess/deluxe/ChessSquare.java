package fr.chess.deluxe;

import fr.chess.deluxe.piece.ChessPiece;

public class ChessSquare {

    private final ChessBoard chessBoard;
    private final String position;
    private ChessPiece chessPiece;

    public ChessSquare(ChessBoard chessBoard, String position) {
        this.chessBoard = chessBoard;
        this.position = position;
    }

    public boolean hasChessPiece() {
        return chessPiece != null;
    }

    public ChessPiece getChessPiece() {
        return chessPiece;
    }

    public void setChessPiece(ChessPiece chessPiece) {
        this.chessPiece = chessPiece;
    }

    public ChessBoard getChessBoard() {
        return chessBoard;
    }

    public String getPosition() {
        return position;
    }
}
