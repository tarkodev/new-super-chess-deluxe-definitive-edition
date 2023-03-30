package fr.chess.deluxe.piece;

import fr.chess.deluxe.ChessBoard;
import fr.chess.deluxe.ChessSquare;
import fr.chess.deluxe.utils.ChessColor;
import fr.chess.deluxe.utils.Coordinates;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public enum PieceMovement {

    ROOK,
    BISHOP,
    KNIGHT,

    PAWN,
    KING,
    ;

    private void laser(ChessBoard board, ChessColor currentPieceColor, Coordinates coordinates, Consumer<Coordinates> coordinatesConsumer, List<ChessSquare> possibleSquare, boolean recursive) {
        coordinatesConsumer.accept(coordinates);
        int x = coordinates.getX();
        int y = coordinates.getY();

        if (0 <= x && x <= ChessBoard.CHESS_SQUARE_LENGTH-1 && 0 <= y && y <= ChessBoard.CHESS_SQUARE_LENGTH-1) {
            ChessSquare chessSquare = board.getBoard()[x][y];
            boolean chessSquareHasPiece = chessSquare.getPiece() != null;
            boolean chessSquareHasOppositePieceColor = chessSquareHasPiece && chessSquare.getPiece().getPieceColor() != currentPieceColor;

            if(!chessSquareHasPiece || chessSquareHasOppositePieceColor) {
                possibleSquare.add(chessSquare);
                if(!chessSquareHasPiece && recursive)
                    laser(board, currentPieceColor, coordinates, coordinatesConsumer, possibleSquare, true);
            }
        }
    }

    public List<ChessSquare> getPossibleMoves(ChessSquare chessSquare) {
        ChessBoard chessBoard = chessSquare.getChessBoard();
        ChessColor squarePieceColor = chessSquare.getPiece().getPieceColor();
        Coordinates squareCoordinates = chessSquare.getCoordinates();

        List<ChessSquare> possibleSquare = new ArrayList<>();
        switch (this) {
            case ROOK -> {
                laser(chessBoard, squarePieceColor, squareCoordinates.clone(), coordinates -> coordinates.addY(-1), possibleSquare, true);
                laser(chessBoard, squarePieceColor, squareCoordinates.clone(), coordinates -> coordinates.addX(1), possibleSquare, true);
                laser(chessBoard, squarePieceColor, squareCoordinates.clone(), coordinates -> coordinates.addY(1), possibleSquare, true);
                laser(chessBoard, squarePieceColor, squareCoordinates.clone(), coordinates -> coordinates.addX(-1), possibleSquare, true);
            }
            case BISHOP -> {
                laser(chessBoard, squarePieceColor, squareCoordinates.clone(), coordinates -> coordinates.add(1, -1), possibleSquare, true);
                laser(chessBoard, squarePieceColor, squareCoordinates.clone(), coordinates -> coordinates.add(1, 1), possibleSquare, true);
                laser(chessBoard, squarePieceColor, squareCoordinates.clone(), coordinates -> coordinates.add(-1, 1), possibleSquare, true);
                laser(chessBoard, squarePieceColor, squareCoordinates.clone(), coordinates -> coordinates.add(-1, -1), possibleSquare, true);
            }
            case KNIGHT -> {
                laser(chessBoard, squarePieceColor, squareCoordinates.clone(), coordinates -> coordinates.add(1, -2), possibleSquare, false);
                laser(chessBoard, squarePieceColor, squareCoordinates.clone(), coordinates -> coordinates.add(2, -1), possibleSquare, false);
                laser(chessBoard, squarePieceColor, squareCoordinates.clone(), coordinates -> coordinates.add(2, 1), possibleSquare, false);
                laser(chessBoard, squarePieceColor, squareCoordinates.clone(), coordinates -> coordinates.add(1, 2), possibleSquare, false);
                laser(chessBoard, squarePieceColor, squareCoordinates.clone(), coordinates -> coordinates.add(-1, 2), possibleSquare, false);
                laser(chessBoard, squarePieceColor, squareCoordinates.clone(), coordinates -> coordinates.add(-2, 1), possibleSquare, false);
                laser(chessBoard, squarePieceColor, squareCoordinates.clone(), coordinates -> coordinates.add(-2, -1), possibleSquare, false);
                laser(chessBoard, squarePieceColor, squareCoordinates.clone(), coordinates -> coordinates.add(-1, -2), possibleSquare, false);
            }
            case KING -> {
                laser(chessBoard, squarePieceColor, squareCoordinates.clone(), coordinates -> coordinates.add(0, -1), possibleSquare, false);
                laser(chessBoard, squarePieceColor, squareCoordinates.clone(), coordinates -> coordinates.add(1, -1), possibleSquare, false);
                laser(chessBoard, squarePieceColor, squareCoordinates.clone(), coordinates -> coordinates.add(1, 0), possibleSquare, false);
                laser(chessBoard, squarePieceColor, squareCoordinates.clone(), coordinates -> coordinates.add(1, 1), possibleSquare, false);
                laser(chessBoard, squarePieceColor, squareCoordinates.clone(), coordinates -> coordinates.add(0, 1), possibleSquare, false);
                laser(chessBoard, squarePieceColor, squareCoordinates.clone(), coordinates -> coordinates.add(-1, 1), possibleSquare, false);
                laser(chessBoard, squarePieceColor, squareCoordinates.clone(), coordinates -> coordinates.add(-1, 0), possibleSquare, false);
                laser(chessBoard, squarePieceColor, squareCoordinates.clone(), coordinates -> coordinates.add(-1, -1), possibleSquare, false);
            }
            case PAWN -> {
                int oneForward = squarePieceColor == ChessColor.WHITE ? -1 : 1;
                int secondLine = squarePieceColor == ChessColor.WHITE ? ChessBoard.CHESS_SQUARE_LENGTH-2 : 1;

                 if (!chessBoard.getSquare(squareCoordinates.clone().addY(oneForward)).hasPiece()) {
                     laser(chessBoard, squarePieceColor, squareCoordinates.clone(), coordinates -> coordinates.addY(oneForward), possibleSquare, false);
                     if (squareCoordinates.getY() == secondLine && !chessBoard.getSquare(squareCoordinates.clone().addY(oneForward*2)).hasPiece()) {
                         laser(chessBoard, squarePieceColor, squareCoordinates.clone(), coordinates -> coordinates.addY(oneForward*2), possibleSquare, false);
                     }
                 }
                if (squareCoordinates.getX() > 0 && chessBoard.getSquare(squareCoordinates.clone().add(-1, oneForward)).hasPiece())
                    laser(chessBoard, squarePieceColor, squareCoordinates.clone(), coordinates -> coordinates.add(-1, oneForward), possibleSquare, false);

                if (squareCoordinates.getX() < ChessBoard.CHESS_SQUARE_LENGTH-1 && chessBoard.getSquare(squareCoordinates.clone().add(1, oneForward)).hasPiece())
                    laser(chessBoard, squarePieceColor, squareCoordinates.clone(), coordinates -> coordinates.add(1, oneForward), possibleSquare, false);
            }
        }

        return possibleSquare;
    }
}
