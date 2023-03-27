package fr.chess.deluxe.piece;

import fr.chess.deluxe.ChessBoard;
import fr.chess.deluxe.ChessSquare;
import fr.chess.deluxe.utils.ChessColor;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public enum PieceMovement {

    ROOK,
    BISHOP,
    KNIGHT,

    PAWN,
    KING,
    ;

    private void laser(ChessBoard board, List<ChessSquare> possibleSquare, int x, int y, Function<Integer, Integer> xLas, Function<Integer, Integer> yLas, ChessColor color, boolean recursive) {
        x = xLas.apply(x);
        y = yLas.apply(y);

        if ((0 <= x && x <= 7 && 0 <= y && y <= 7)) {
            ChessSquare chessSquare = board.getBoard()[x][y];
            boolean chessSquareHasPiece = chessSquare.getChessPiece() != null;
            boolean chessSquareHasOppositePieceColor = chessSquareHasPiece && chessSquare.getChessPiece().getPieceColor() != color;

            if(!chessSquareHasPiece || chessSquareHasOppositePieceColor) {
                possibleSquare.add(chessSquare);
                if(!chessSquareHasPiece && recursive)
                    laser(board, possibleSquare, x, y, xLas, yLas, color, true);
            }
        }
    }



    /*private void rookRec(ChessBoard board, List<ChessSquare> possibleSquare, int x, int y, PieceColor color, char dir) {
        if (0 <= x && x <= 7 && 0 <= y && y <= 7) {
            switch (dir)  {
                case ('l'): {
                    if (x==0) {
                        return;
                    } else if (board.getBoard()[x-1][y].getChessPiece().getPieceColor() == color) {

                    }
                }
                default: {
                    return;
                }
            }
        }
    }*/

    public List<ChessSquare> getPossibleMoves(ChessSquare chessSquare) {
        int x = chessSquare.getX();
        int y = chessSquare.getY();
        ChessColor color = chessSquare.getChessPiece().getPieceColor();
        List<ChessSquare> possibleSquare = new ArrayList<>();
        switch (this) {
            case ROOK -> {
                laser(chessSquare.getChessBoard(), possibleSquare, x, y, lasX -> lasX, lasY -> lasY-1, color, true);
                laser(chessSquare.getChessBoard(), possibleSquare, x, y, lasX -> lasX+1, lasY -> lasY, color, true);
                laser(chessSquare.getChessBoard(), possibleSquare, x, y, lasX -> lasX, lasY -> lasY+1, color, true);
                laser(chessSquare.getChessBoard(), possibleSquare, x, y, lasX -> lasX-1, lasY -> lasY, color, true);
            }
            case BISHOP -> {
                laser(chessSquare.getChessBoard(), possibleSquare, x, y, lasX -> lasX+1, lasY -> lasY-1, color, true);
                laser(chessSquare.getChessBoard(), possibleSquare, x, y, lasX -> lasX+1, lasY -> lasY+1, color, true);
                laser(chessSquare.getChessBoard(), possibleSquare, x, y, lasX -> lasX-1, lasY -> lasY+1, color, true);
                laser(chessSquare.getChessBoard(), possibleSquare, x, y, lasX -> lasX-1, lasY -> lasY-1, color, true);
            }
            case KNIGHT -> {
                laser(chessSquare.getChessBoard(), possibleSquare, x, y, lasX -> lasX+1, lasY -> lasY-2, color, false);
                laser(chessSquare.getChessBoard(), possibleSquare, x, y, lasX -> lasX+2, lasY -> lasY-1, color, false);
                laser(chessSquare.getChessBoard(), possibleSquare, x, y, lasX -> lasX+2, lasY -> lasY+1, color, false);
                laser(chessSquare.getChessBoard(), possibleSquare, x, y, lasX -> lasX+1, lasY -> lasY+2, color, false);
                laser(chessSquare.getChessBoard(), possibleSquare, x, y, lasX -> lasX-1, lasY -> lasY+2, color, false);
                laser(chessSquare.getChessBoard(), possibleSquare, x, y, lasX -> lasX-2, lasY -> lasY+1, color, false);
                laser(chessSquare.getChessBoard(), possibleSquare, x, y, lasX -> lasX-2, lasY -> lasY-1, color, false);
                laser(chessSquare.getChessBoard(), possibleSquare, x, y, lasX -> lasX-1, lasY -> lasY-2, color, false);
            }
            case KING -> {
                laser(chessSquare.getChessBoard(), possibleSquare, x, y, lasX -> lasX, lasY -> lasY-1, color, false);
                laser(chessSquare.getChessBoard(), possibleSquare, x, y, lasX -> lasX+1, lasY -> lasY-1, color, false);
                laser(chessSquare.getChessBoard(), possibleSquare, x, y, lasX -> lasX+1, lasY -> lasY, color, false);
                laser(chessSquare.getChessBoard(), possibleSquare, x, y, lasX -> lasX+1, lasY -> lasY+1, color, false);
                laser(chessSquare.getChessBoard(), possibleSquare, x, y, lasX -> lasX, lasY -> lasY+1, color, false);
                laser(chessSquare.getChessBoard(), possibleSquare, x, y, lasX -> lasX-1, lasY -> lasY+1, color, false);
                laser(chessSquare.getChessBoard(), possibleSquare, x, y, lasX -> lasX-1, lasY -> lasY, color, false);
                laser(chessSquare.getChessBoard(), possibleSquare, x, y, lasX -> lasX-1, lasY -> lasY-1, color, false);
            }
            case PAWN -> {
                if (color == ChessColor.WHITE) {
                    laser(chessSquare.getChessBoard(), possibleSquare, x, y, lasX -> lasX, lasY -> lasY-1, color, false);
                    if (y == 6) {
                        laser(chessSquare.getChessBoard(), possibleSquare, x, y, lasX -> lasX, lasY -> lasY-2, color, false);
                    }
                }
                else {
                    laser(chessSquare.getChessBoard(), possibleSquare, x, y, lasX -> lasX, lasY -> lasY+1, color, false);

                    if (y == 1) {
                        laser(chessSquare.getChessBoard(), possibleSquare, x, y, lasX -> lasX, lasY -> lasY+2, color, false);
                    }
                }
            }
        }

        return possibleSquare;
    }
}
