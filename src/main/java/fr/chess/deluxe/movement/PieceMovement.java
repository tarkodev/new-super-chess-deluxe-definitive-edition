package fr.chess.deluxe.movement;

import fr.chess.deluxe.ChessBoard;
import fr.chess.deluxe.ChessSquare;
import fr.chess.deluxe.piece.ChessPiece;
import fr.chess.deluxe.piece.ChessPieceKing;
import fr.chess.deluxe.piece.ChessPiecePawn;
import fr.chess.deluxe.piece.ChessPieceRook;
import fr.chess.deluxe.utils.ChessColor;
import fr.chess.deluxe.utils.ChessDirection;
import fr.chess.deluxe.utils.Coordinates;

import java.util.*;
import java.util.function.Consumer;

import static java.lang.Math.abs;

public enum PieceMovement {

    ROOK,
    BISHOP,
    KNIGHT,

    PAWN,
    KING,
    ;

    /*private void laser(ChessBoard board, ChessColor currentPieceColor, Coordinates coordinates, Consumer<Coordinates> coordinatesConsumer, Set<ChessSquare> possibleSquare, boolean recursive) {
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
    }*/

    private void check(ChessBoard board, ChessColor playerPieceColor, Coordinates coordinates, Consumer<Coordinates> targetConsumer, boolean recursive,
                       Map<Coordinates, Consumer<Coordinates>> possibleTargetTriggerEventMap, Consumer<Coordinates> triggerEvent, boolean canEatAlly) {
        Coordinates fromCoordinates = coordinates.clone();
        targetConsumer.accept(coordinates);
        Coordinates toCoordinates = coordinates.clone();

        if (toCoordinates.isValid()) {
            ChessSquare fromSquare = board.getSquare(fromCoordinates);
            ChessPiece fromPiece = fromSquare.getPiece();

            ChessSquare toSquare = board.getSquare(toCoordinates);
            ChessPiece toPiece = toSquare.getPiece();

            boolean toHasPiece = toPiece != null;
            boolean toHasOppositePieceColor = toHasPiece && toPiece.getPieceColor() != playerPieceColor;

            if(canEatAlly || (!toHasPiece || toHasOppositePieceColor)) {
                possibleTargetTriggerEventMap.put(toCoordinates, triggerEvent);

                if(!toHasPiece && recursive)
                    check(board, playerPieceColor, coordinates, targetConsumer, true, possibleTargetTriggerEventMap, triggerEvent, canEatAlly);
            }
        }
    }

    private void check(ChessBoard board, ChessColor playerPieceColor, Coordinates coordinates, Consumer<Coordinates> target, boolean recursive,
                       Map<Coordinates, Consumer<Coordinates>> possibleTargetEventMap, PieceMovementRules rules) {
        Coordinates fromCoordinates = coordinates.clone();
        ChessSquare fromSquare = board.getSquare(fromCoordinates);
        ChessPiece fromPiece = fromSquare.getPiece();
        int oneForward = fromPiece.getPieceColor().getOneForward();
        check(board, playerPieceColor, coordinates, target, recursive, possibleTargetEventMap, toCoordinates -> {
            fromSquare.removePiece();
            switch (rules) {
                case EN_PASSANT -> {
                    board.setPiece(fromPiece, toCoordinates);
                    board.getSquare(toCoordinates.add(0, -oneForward)).removePiece();
                }
                case CASTLING -> {
                    ChessDirection chessDirection = fromCoordinates.getX() - toCoordinates.getX() > 0 ? ChessDirection.LEFT : ChessDirection.RIGHT;
                    ChessSquare toRookSquare = board.getSquare(toCoordinates.clone().setX(chessDirection.getFirstLine()));
                    ChessPiece toRookPiece = toRookSquare.getPiece();
                    toRookSquare.removePiece();
                    board.setPiece(fromPiece, fromCoordinates.clone().addX(chessDirection.getOneForward()*2));
                    board.setPiece(toRookPiece, fromCoordinates.clone().addX(chessDirection.getOneForward()));
                }
                default -> {
                    board.setPiece(fromPiece, toCoordinates);
                }
            }
            board.getPieceMovementLogs().add(new PieceMovementLog(fromPiece, fromCoordinates, toCoordinates));
        }, rules == PieceMovementRules.CASTLING);
    }

    private void check(ChessBoard board, ChessColor playerPieceColor, Coordinates coordinates, Consumer<Coordinates> target, boolean recursive,
                       Map<Coordinates, Consumer<Coordinates>> possibleTargetEventMap) {
        check(board, playerPieceColor, coordinates, target, recursive, possibleTargetEventMap, PieceMovementRules.DEFAULT);
    }



       /* private void enPassant(ChessBoard board, ChessColor currentPieceColor, Coordinates coordinates, Set<ChessSquare> possibleSquare, boolean left, int oneForward) {
        System.out.println("Test");
        int xPawn = coordinates.getX();
        int yPawn = coordinates.getY();

        if ((currentPieceColor == ChessColor.WHITE && yPawn == 3) || (currentPieceColor == ChessColor.BLACK && yPawn == ChessBoard.CHESS_SQUARE_LENGTH-4)) {
            if ((xPawn == 0 && !left) || 1 <= xPawn && xPawn <= ChessBoard.CHESS_SQUARE_LENGTH - 2 || (xPawn == ChessBoard.CHESS_SQUARE_LENGTH - 1 && left)) {
                int xCaptured = left ? coordinates.getX()-1 : coordinates.getX()+1;
                ChessSquare chessSquare = board.getBoard()[xCaptured][yPawn+oneForward];

                possibleSquare.add(chessSquare);
            }
        }
    }*/

    public Map<Coordinates, Consumer<Coordinates>> getPossibleSquare(ChessSquare chessSquare) {
        ChessBoard chessBoard = chessSquare.getChessBoard();
        ChessColor squarePieceColor = chessSquare.getPiece().getPieceColor();
        Coordinates squareCoordinates = chessSquare.getCoordinates();

        Map<Coordinates, Consumer<Coordinates>> possibleSquare = new HashMap<>();
        switch (this) {
            case ROOK -> {
                check(chessBoard, squarePieceColor, squareCoordinates.clone(), coordinates -> coordinates.addY(-1), true, possibleSquare);
                check(chessBoard, squarePieceColor, squareCoordinates.clone(), coordinates -> coordinates.addX(1), true, possibleSquare);
                check(chessBoard, squarePieceColor, squareCoordinates.clone(), coordinates -> coordinates.addY(1), true, possibleSquare);
                check(chessBoard, squarePieceColor, squareCoordinates.clone(), coordinates -> coordinates.addX(-1), true, possibleSquare);

            }
            case BISHOP -> {
                check(chessBoard, squarePieceColor, squareCoordinates.clone(), coordinates -> coordinates.add(1, -1), true, possibleSquare);
                check(chessBoard, squarePieceColor, squareCoordinates.clone(), coordinates -> coordinates.add(1, 1), true, possibleSquare);
                check(chessBoard, squarePieceColor, squareCoordinates.clone(), coordinates -> coordinates.add(-1, 1), true, possibleSquare);
                check(chessBoard, squarePieceColor, squareCoordinates.clone(), coordinates -> coordinates.add(-1, -1), true, possibleSquare);
                break;
            }
            case KNIGHT -> {
                check(chessBoard, squarePieceColor, squareCoordinates.clone(), coordinates -> coordinates.add(1, -2), false, possibleSquare);
                check(chessBoard, squarePieceColor, squareCoordinates.clone(), coordinates -> coordinates.add(2, -1), false, possibleSquare);
                check(chessBoard, squarePieceColor, squareCoordinates.clone(), coordinates -> coordinates.add(2, 1), false, possibleSquare);
                check(chessBoard, squarePieceColor, squareCoordinates.clone(), coordinates -> coordinates.add(1, 2), false, possibleSquare);
                check(chessBoard, squarePieceColor, squareCoordinates.clone(), coordinates -> coordinates.add(-1, 2), false, possibleSquare);
                check(chessBoard, squarePieceColor, squareCoordinates.clone(), coordinates -> coordinates.add(-2, 1), false, possibleSquare);
                check(chessBoard, squarePieceColor, squareCoordinates.clone(), coordinates -> coordinates.add(-2, -1), false, possibleSquare);
                check(chessBoard, squarePieceColor, squareCoordinates.clone(), coordinates -> coordinates.add(-1, -2), false, possibleSquare);
            }
            case KING -> {
                check(chessBoard, squarePieceColor, squareCoordinates.clone(), coordinates -> coordinates.add(0, -1), false, possibleSquare);
                check(chessBoard, squarePieceColor, squareCoordinates.clone(), coordinates -> coordinates.add(1, -1), false, possibleSquare);
                check(chessBoard, squarePieceColor, squareCoordinates.clone(), coordinates -> coordinates.add(1, 0), false, possibleSquare);
                check(chessBoard, squarePieceColor, squareCoordinates.clone(), coordinates -> coordinates.add(1, 1), false, possibleSquare);
                check(chessBoard, squarePieceColor, squareCoordinates.clone(), coordinates -> coordinates.add(0, 1), false, possibleSquare);
                check(chessBoard, squarePieceColor, squareCoordinates.clone(), coordinates -> coordinates.add(-1, 1), false, possibleSquare);
                check(chessBoard, squarePieceColor, squareCoordinates.clone(), coordinates -> coordinates.add(-1, 0), false, possibleSquare);
                check(chessBoard, squarePieceColor, squareCoordinates.clone(), coordinates -> coordinates.add(-1, -1), false, possibleSquare);


                Coordinates kingLeft1 = new Coordinates(0, squarePieceColor.getFirstPiecesLine());
                Coordinates kingLeft2 = new Coordinates(1, squarePieceColor.getFirstPiecesLine());
                Coordinates kingLeft3 = new Coordinates(2, squarePieceColor.getFirstPiecesLine());
                Coordinates kingLeft4 = new Coordinates(3, squarePieceColor.getFirstPiecesLine());


                Coordinates kingRight3 = new Coordinates(5, squarePieceColor.getFirstPiecesLine());
                Coordinates kingRight2 = new Coordinates(6, squarePieceColor.getFirstPiecesLine());
                Coordinates kingRight1 = new Coordinates(7, squarePieceColor.getFirstPiecesLine());

                //If the king has not moved
                if (chessBoard.getPieceMovementLogs().stream().noneMatch(pieceMovementLog -> pieceMovementLog.getPiece().getPieceColor().equals(squarePieceColor)
                                && pieceMovementLog.getPiece() instanceof ChessPieceKing)) {

                    //If the left tower has not moved
                    if (!chessBoard.getSquare(kingLeft2).hasPiece() && !chessBoard.getSquare(kingLeft3).hasPiece() && !chessBoard.getSquare(kingLeft4).hasPiece() &&
                            chessBoard.getPieceMovementLogs().stream().noneMatch(pieceMovementLog -> pieceMovementLog.getPiece().getPieceColor().equals(squarePieceColor)
                                    && pieceMovementLog.getPiece() instanceof ChessPieceRook
                                    && pieceMovementLog.getFromCoordinates().equals(kingLeft1))) {
                        check(chessBoard, squarePieceColor, squareCoordinates.clone(), coordinates -> coordinates.set(kingLeft1), false, possibleSquare, PieceMovementRules.CASTLING);
                        check(chessBoard, squarePieceColor,  squareCoordinates.clone(), coordinates -> coordinates.set(kingLeft2), false, possibleSquare, PieceMovementRules.CASTLING);
                        check(chessBoard, squarePieceColor, squareCoordinates.clone(), coordinates -> coordinates.set(kingLeft3), false, possibleSquare, PieceMovementRules.CASTLING);
                    }

                    //If the right tower has not moved
                    if (!chessBoard.getSquare(kingRight3).hasPiece() && !chessBoard.getSquare(kingRight2).hasPiece() &&
                            chessBoard.getPieceMovementLogs().stream().noneMatch(pieceMovementLog -> pieceMovementLog.getPiece().getPieceColor().equals(squarePieceColor)
                                    && pieceMovementLog.getPiece() instanceof ChessPieceRook
                                    && pieceMovementLog.getFromCoordinates().equals(kingRight1))) {
                        check(chessBoard, squarePieceColor, squareCoordinates.clone(), coordinates -> coordinates.set(kingRight1), false, possibleSquare, PieceMovementRules.CASTLING);
                        check(chessBoard, squarePieceColor, squareCoordinates.clone(), coordinates -> coordinates.set(kingRight2), false, possibleSquare, PieceMovementRules.CASTLING);
                    }
                }

            

              /*  //left Castle
                if (!(chessBoard.getBoard()[1][KingY].hasPiece()) && !(chessBoard.getBoard()[2][KingY].hasPiece()) && !(chessBoard.getBoard()[3][KingY].hasPiece())) {  //no piece between the king and the rook
                    if (chessBoard.getCurrentPlayer().equals(ChessColor.WHITE) && !(chessBoard.getCastleInfo().getWhiteKingMoved() || chessBoard.getCastleInfo().getWhiteLeftRookMoved())) //neither the rook nor the king moved
                        possibleSquare.add(new Move(chessBoard.getSquare(new Coordinates(1, ChessBoard.CHESS_SQUARE_LENGTH-1)), false, new Castled(true, false, false, false)));
                    else if (chessBoard.getCurrentPlayer().equals(ChessColor.BLACK) && !(chessBoard.getCastleInfo().getBlackKingMoved() || chessBoard.getCastleInfo().getBlackLeftRookMoved()))    //neither the rook nor the king moved
                        possibleSquare.add(new Move(chessBoard.getSquare(new Coordinates(1, 0)), false, new Castled(false, false, true, false)));
                }
                //right Castle
                if (!(chessBoard.getBoard()[ChessBoard.CHESS_SQUARE_LENGTH-3][KingY].hasPiece()) && !(chessBoard.getBoard()[ChessBoard.CHESS_SQUARE_LENGTH-2][KingY].hasPiece())) {  //no piece between the king and the rook
                    if (chessBoard.getCurrentPlayer().equals(ChessColor.WHITE) && !(chessBoard.getCastleInfo().getWhiteKingMoved() || chessBoard.getCastleInfo().getWhiteRightRookMoved())) //neither the rook nor the king moved
                        possibleSquare.add(new Move(chessBoard.getSquare(new Coordinates(ChessBoard.CHESS_SQUARE_LENGTH-2, ChessBoard.CHESS_SQUARE_LENGTH-1)), false, new Castled(false, true, false, false)));
                    else if (chessBoard.getCurrentPlayer().equals(ChessColor.BLACK) && !(chessBoard.getCastleInfo().getBlackKingMoved() || chessBoard.getCastleInfo().getBlackRightRookMoved()))    //neither the rook nor the king moved
                        possibleSquare.add(new Move(chessBoard.getSquare(new Coordinates(ChessBoard.CHESS_SQUARE_LENGTH-2, 0)), false, new Castled(false, false, false, true)));
                }*/

            }
            case PAWN -> {
                int oneForward = squarePieceColor == ChessColor.WHITE ? -1 : 1;
                int secondLine = squarePieceColor == ChessColor.WHITE ? ChessBoard.CHESS_SQUARE_LENGTH-2 : 1;

                //normal advancement, 1 then 2 squares
                 if (!chessBoard.getSquare(squareCoordinates.clone().addY(oneForward)).hasPiece()) {
                     check(chessBoard, squarePieceColor, squareCoordinates.clone(), coordinates -> coordinates.addY(oneForward), false, possibleSquare);
                     if (squareCoordinates.getY() == secondLine && !chessBoard.getSquare(squareCoordinates.clone().addY(oneForward*2)).hasPiece()) {
                         check(chessBoard, squarePieceColor, squareCoordinates.clone(), coordinates -> coordinates.addY(oneForward*2), false, possibleSquare);
                     }
                 }

                 //normal capture left then right
                if (squareCoordinates.getX() > 0 && chessBoard.getSquare(squareCoordinates.clone().add(-1, oneForward)).hasPiece())
                    check(chessBoard, squarePieceColor, squareCoordinates.clone(), coordinates -> coordinates.add(-1, oneForward), false, possibleSquare);

                if (squareCoordinates.getX() < ChessBoard.CHESS_SQUARE_LENGTH-2 && chessBoard.getSquare(squareCoordinates.clone().add(1, oneForward)).hasPiece())
                    check(chessBoard, squarePieceColor, squareCoordinates.clone(), coordinates -> coordinates.add(1, oneForward), false, possibleSquare);

                //capture "en passant" left then right
                if(chessBoard.getLastPieceMovementLog() != null) {
                    PieceMovementLog lastPieceMovementLog = chessBoard.getLastPieceMovementLog();

                    Coordinates enPassantLeft = squareCoordinates.clone().add(-1, 0);
                    if (chessBoard.getSquare(enPassantLeft) != null && chessBoard.getSquare(enPassantLeft).hasPiece()
                            && lastPieceMovementLog.getToCoordinates().equals(enPassantLeft)
                            && lastPieceMovementLog.getPiece() instanceof ChessPiecePawn
                            && abs(lastPieceMovementLog.getToCoordinates().getY() - lastPieceMovementLog.getFromCoordinates().getY()) == 2) {
                        Coordinates enPassantMovement = squareCoordinates.clone().add(-1, oneForward);
                        check(chessBoard, squarePieceColor, squareCoordinates.clone(), coordinates -> coordinates.set(enPassantMovement), false, possibleSquare, PieceMovementRules.EN_PASSANT);
                    }

                    Coordinates enPassantRight = squareCoordinates.clone().add(1, 0);
                    if (chessBoard.getSquare(enPassantRight) != null && chessBoard.getSquare(enPassantRight).hasPiece()
                            && lastPieceMovementLog.getToCoordinates().equals(enPassantRight)
                            && lastPieceMovementLog.getPiece() instanceof ChessPiecePawn
                            && abs(lastPieceMovementLog.getToCoordinates().getY() - lastPieceMovementLog.getFromCoordinates().getY()) == 2) {
                        Coordinates enPassantRightMovement = squareCoordinates.clone().add(1, oneForward);
                        check(chessBoard, squarePieceColor, squareCoordinates.clone(), coordinates -> coordinates.set(enPassantRightMovement), false, possibleSquare, PieceMovementRules.EN_PASSANT);
                    }
                }
            }
        }

        return possibleSquare;
    }
}
