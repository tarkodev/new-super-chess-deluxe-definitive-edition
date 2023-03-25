package fr.chess.deluxe;

import fr.chess.deluxe.piece.ChessPiece;

import java.util.Map;

public class ChessBoard {

    public static final int SIZE = 8;
    private final ChessSquare[][] board;

    public ChessBoard() {
        this.board = new ChessSquare[SIZE][SIZE];
    }

    public void load() {
        for (int y = 0; y < SIZE; y++) {
            for (int x = 0; x < SIZE; x++) {
                board[y][x] = new ChessSquare(this, "");
            }
        }
    }

    public void convertToString(int x, int y) {
        String xString = String.valueOf((char) ('a' + x));
        String yString = String.valueOf(8-y);
        System.out.println(xString + yString);
    }


}
