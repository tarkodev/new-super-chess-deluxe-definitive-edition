package fr.chess.deluxe.movement;

import fr.chess.deluxe.ChessBoard;
import fr.chess.deluxe.ChessMain;
import fr.chess.deluxe.ChessSquare;
import fr.chess.deluxe.piece.ChessPiece;
import fr.chess.deluxe.piece.ChessPieceType;
import fr.chess.deluxe.utils.ChessColor;
import fr.chess.deluxe.utils.ChessDirection;
import fr.chess.deluxe.utils.Coordinates;
import fr.chess.deluxe.utils.PlayerInformation;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

import static java.lang.Math.abs;

public enum PieceMovement {

    ROOK,
    BISHOP,
    KNIGHT,

    PAWN,
    KING,
    ;

    private void check(ChessBoard board, ChessColor playerPieceColor, Coordinates coordinates, Consumer<Coordinates> targetConsumer, boolean recursive,
                       Map<Coordinates, Consumer<Coordinates>> possibleTargetTriggerEventMap, Consumer<Coordinates> triggerEvent, boolean canEatAlly) {
        targetConsumer.accept(coordinates);
        Coordinates toCoordinates = coordinates.clone();

        if (toCoordinates.isValid()) {

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
        int oneForward = fromPiece.getPieceColor().getOneStep();
        check(board, playerPieceColor, coordinates, target, recursive, possibleTargetEventMap, toCoordinates -> {
            PieceMovementLog pieceMovementLog = new PieceMovementLog(fromPiece, fromCoordinates, toCoordinates, ChessMain.GSON.toJson(board));
            switch (rules) {
                case EN_PASSANT -> {
                    board.movePiece(fromCoordinates, toCoordinates);
                    board.removePiece(toCoordinates.add(0, -oneForward));
                }
                case CASTLING -> {
                    ChessDirection chessDirection = fromCoordinates.getX() - toCoordinates.getX() > 0 ? ChessDirection.LEFT : ChessDirection.RIGHT;
                    Coordinates toKingCoordinates = fromCoordinates.clone().addX(chessDirection.getOneStep()*2);
                    pieceMovementLog = new PieceMovementLog(pieceMovementLog.getPiece(), pieceMovementLog.getFromCoordinates(), toKingCoordinates, ChessMain.GSON.toJson(board));

                    board.removePiece(fromCoordinates);
                    board.movePiece(toCoordinates.clone().setX(chessDirection.getFirstLine()), fromCoordinates.clone().addX(chessDirection.getOneStep()));
                    board.setPiece(toKingCoordinates, fromPiece);
                }
                case PROMOTION -> {
                    board.movePiece(fromCoordinates, toCoordinates);
                    board.setPiece(toCoordinates, new ChessPiece(ChessPieceType.QUEEN, playerPieceColor));
                }
                default -> board.movePiece(fromCoordinates, toCoordinates);
            }
            board.getPieceMovementLogs().add(pieceMovementLog);
        }, rules == PieceMovementRules.CASTLING);
    }

    private void check(ChessBoard board, ChessColor playerPieceColor, Coordinates coordinates, Consumer<Coordinates> target, boolean recursive,
                       Map<Coordinates, Consumer<Coordinates>> possibleTargetEventMap) {
        check(board, playerPieceColor, coordinates, target, recursive, possibleTargetEventMap, PieceMovementRules.DEFAULT);
    }

    public Map<Coordinates, Consumer<Coordinates>> getPossibleSquare(ChessBoard chessBoard, ChessColor squarePieceColor, Coordinates squareCoordinates) {
        Map<Coordinates, Consumer<Coordinates>> possibleSquare = new ConcurrentHashMap<>();
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
                                && pieceMovementLog.getPiece().getType() == ChessPieceType.KING)) {

                    //If the left tower has not moved
                    if (!chessBoard.getSquare(kingLeft2).hasPiece() && !chessBoard.getSquare(kingLeft3).hasPiece() && !chessBoard.getSquare(kingLeft4).hasPiece() &&
                            chessBoard.getPieceMovementLogs().stream().noneMatch(pieceMovementLog -> pieceMovementLog.getPiece().getPieceColor().equals(squarePieceColor)
                                    && pieceMovementLog.getPiece().getType() == ChessPieceType.ROOK
                                    && pieceMovementLog.getFromCoordinates().equals(kingLeft1))) {
                        check(chessBoard, squarePieceColor, squareCoordinates.clone(), coordinates -> coordinates.set(kingLeft1), false, possibleSquare, PieceMovementRules.CASTLING);
                        check(chessBoard, squarePieceColor,  squareCoordinates.clone(), coordinates -> coordinates.set(kingLeft2), false, possibleSquare, PieceMovementRules.CASTLING);
                        check(chessBoard, squarePieceColor, squareCoordinates.clone(), coordinates -> coordinates.set(kingLeft3), false, possibleSquare, PieceMovementRules.CASTLING);
                    }

                    //If the right tower has not moved
                    if (!chessBoard.getSquare(kingRight3).hasPiece() && !chessBoard.getSquare(kingRight2).hasPiece() &&
                            chessBoard.getPieceMovementLogs().stream().noneMatch(pieceMovementLog -> pieceMovementLog.getPiece().getPieceColor().equals(squarePieceColor)
                                    && pieceMovementLog.getPiece().getType() == ChessPieceType.ROOK
                                    && pieceMovementLog.getFromCoordinates().equals(kingRight1))) {
                        check(chessBoard, squarePieceColor, squareCoordinates.clone(), coordinates -> coordinates.set(kingRight1), false, possibleSquare, PieceMovementRules.CASTLING);
                        check(chessBoard, squarePieceColor, squareCoordinates.clone(), coordinates -> coordinates.set(kingRight2), false, possibleSquare, PieceMovementRules.CASTLING);
                    }
                }
            }
            case PAWN -> {
                int oneForward = squarePieceColor == ChessColor.WHITE ? -1 : 1;
                int secondLine = squarePieceColor == ChessColor.WHITE ? ChessBoard.CHESS_SQUARE_LENGTH-2 : 1;
                int secondToLastLine = squarePieceColor == ChessColor.WHITE ? 1 : ChessBoard.CHESS_SQUARE_LENGTH-2;

                //normal advancement
                 if (!chessBoard.getSquare(squareCoordinates.clone().addY(oneForward)).hasPiece()) {
                     if (squareCoordinates.getY() == secondLine && !chessBoard.getSquare(squareCoordinates.clone().addY(oneForward*2)).hasPiece()) {    //1 or 2 squares
                         check(chessBoard, squarePieceColor, squareCoordinates.clone(), coordinates -> coordinates.addY(oneForward), false, possibleSquare);
                         check(chessBoard, squarePieceColor, squareCoordinates.clone(), coordinates -> coordinates.addY(oneForward*2), false, possibleSquare);
                     } else if (squareCoordinates.getY() == secondToLastLine)   //1 square with promotion
                         check(chessBoard, squarePieceColor, squareCoordinates.clone(), coordinates -> coordinates.addY(oneForward), false, possibleSquare, PieceMovementRules.PROMOTION);
                     else //1 square default
                         check(chessBoard, squarePieceColor, squareCoordinates.clone(), coordinates -> coordinates.addY(oneForward), false, possibleSquare);
                 }

                 //normal capture left then right
                if (squareCoordinates.getX() > 0 && chessBoard.getSquare(squareCoordinates.clone().add(-1, oneForward)).hasPiece()) {
                    if (squareCoordinates.getY() == secondToLastLine)//1 square with promotion
                        check(chessBoard, squarePieceColor, squareCoordinates.clone(), coordinates -> coordinates.add(-1, oneForward), false, possibleSquare, PieceMovementRules.PROMOTION);
                    else
                        check(chessBoard, squarePieceColor, squareCoordinates.clone(), coordinates -> coordinates.add(-1, oneForward), false, possibleSquare);
                }


                if (squareCoordinates.getX() < ChessBoard.CHESS_SQUARE_LENGTH-2 && chessBoard.getSquare(squareCoordinates.clone().add(1, oneForward)).hasPiece()) {
                    if (squareCoordinates.getY() == secondToLastLine)//1 square with promotion
                        check(chessBoard, squarePieceColor, squareCoordinates.clone(), coordinates -> coordinates.add(1, oneForward), false, possibleSquare, PieceMovementRules.PROMOTION);
                    else
                        check(chessBoard, squarePieceColor, squareCoordinates.clone(), coordinates -> coordinates.add(1, oneForward), false, possibleSquare);
                }

                //capture "en passant" left then right
                if(chessBoard.getLastPieceMovementLog() != null) {
                    PieceMovementLog lastPieceMovementLog = chessBoard.getLastPieceMovementLog();

                    Coordinates enPassantLeft = squareCoordinates.clone().add(-1, 0);
                    if (chessBoard.getSquare(enPassantLeft) != null && chessBoard.getSquare(enPassantLeft).hasPiece()
                            && lastPieceMovementLog.getToCoordinates().equals(enPassantLeft)
                            && lastPieceMovementLog.getPiece().getType() == ChessPieceType.PAWN
                            && abs(lastPieceMovementLog.getToCoordinates().getY() - lastPieceMovementLog.getFromCoordinates().getY()) == 2) {
                        Coordinates enPassantMovement = squareCoordinates.clone().add(-1, oneForward);
                        check(chessBoard, squarePieceColor, squareCoordinates.clone(), coordinates -> coordinates.set(enPassantMovement), false, possibleSquare, PieceMovementRules.EN_PASSANT);
                    }

                    Coordinates enPassantRight = squareCoordinates.clone().add(1, 0);
                    if (chessBoard.getSquare(enPassantRight) != null && chessBoard.getSquare(enPassantRight).hasPiece()
                            && lastPieceMovementLog.getToCoordinates().equals(enPassantRight)
                            && lastPieceMovementLog.getPiece().getType() == ChessPieceType.PAWN
                            && abs(lastPieceMovementLog.getToCoordinates().getY() - lastPieceMovementLog.getFromCoordinates().getY()) == 2) {
                        Coordinates enPassantRightMovement = squareCoordinates.clone().add(1, oneForward);
                        check(chessBoard, squarePieceColor, squareCoordinates.clone(), coordinates -> coordinates.set(enPassantRightMovement), false, possibleSquare, PieceMovementRules.EN_PASSANT);
                    }
                }
            }
        }

        //remove when check
        if(!chessBoard.isClone()) possibleSquare.forEach((toCoordinates, coordinatesConsumer) -> {
            ChessBoard cloneBoard = new ChessBoard(chessBoard);
            cloneBoard.setClone(true);
            cloneBoard.moveEvent(squareCoordinates, toCoordinates);
            if(!cloneBoard.getPlayerInformation().get(chessBoard.getCurrentPlayer()).getCheckStatus().equals(PlayerInformation.CheckStatus.NONE)) {
                possibleSquare.remove(toCoordinates);
            }
        });

        return possibleSquare;
    }

}
