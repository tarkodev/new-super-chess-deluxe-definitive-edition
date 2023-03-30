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

        if (0 <= x && x <= ChessBoard.CHESS_SQUARE_LENGTH-1 && 0 <= y && y <= ChessBoard.CHESS_SQUARE_LENGTH-1) {
            ChessSquare chessSquare = board.getBoard()[x][y];
            boolean chessSquareHasPiece = chessSquare.getPiece() != null;
            boolean chessSquareHasOppositePieceColor = chessSquareHasPiece && chessSquare.getPiece().getPieceColor() != color;

            if(!chessSquareHasPiece || chessSquareHasOppositePieceColor) {
                possibleSquare.add(chessSquare);
                if(!chessSquareHasPiece && recursive)
                    laser(board, possibleSquare, x, y, xLas, yLas, color, true);
            }
        }
    }

    public List<ChessSquare> getPossibleMoves(ChessSquare chessSquare) {
        int x = chessSquare.getX();
        int y = chessSquare.getY();
        ChessColor color = chessSquare.getPiece().getPieceColor();
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
                int oneForward = color == ChessColor.WHITE ? -1 : 1;
                int secondLine = color == ChessColor.WHITE ? ChessBoard.CHESS_SQUARE_LENGTH-2 : 1;




                 if (!chessSquare.getChessBoard().getBoard()[x][y+oneForward].hasPiece()) {
                    laser(chessSquare.getChessBoard(), possibleSquare, x, y, lasX -> lasX, lasY -> lasY + oneForward, color, false);
                    if (y == secondLine && !chessSquare.getChessBoard().getBoard()[x][y + oneForward*2].hasPiece()) {
                        laser(chessSquare.getChessBoard(), possibleSquare, x, y, lasX -> lasX, lasY -> lasY + oneForward*2, color, false);
                    }
                 }
                if (x > 0 && chessSquare.getChessBoard().getBoard()[x-1][y+oneForward].hasPiece())
                    laser(chessSquare.getChessBoard(), possibleSquare, x, y, lasX -> lasX-1, lasY -> lasY + oneForward, color, false);

                if (x < ChessBoard.CHESS_SQUARE_LENGTH-1 && chessSquare.getChessBoard().getBoard()[x+1][y+oneForward].hasPiece())
                    laser(chessSquare.getChessBoard(), possibleSquare, x, y, lasX -> lasX+1, lasY -> lasY + oneForward, color, false);




            }
        }

        return possibleSquare;
    }
}
