package fr.chess.deluxe.movement;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import fr.chess.deluxe.ChessBoard;
import fr.chess.deluxe.ChessMain;
import fr.chess.deluxe.ChessSquare;
import fr.chess.deluxe.piece.ChessPiece;
import fr.chess.deluxe.piece.ChessPieceType;
import fr.chess.deluxe.utils.ChessColor;
import fr.chess.deluxe.utils.ChessDirection;
import fr.chess.deluxe.utils.Coordinates;
import fr.chess.deluxe.utils.PlayerInformation;

import java.io.StringReader;
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
    NIGHTRIDER
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
            ChessBoard chessBoardJson = new ChessBoard(board);
            PieceMovementLog pieceMovementLog = new PieceMovementLog(fromPiece, fromCoordinates, toCoordinates, chessBoardJson);
            switch (rules) {
                case EN_PASSANT -> {
                    board.movePiece(fromCoordinates, toCoordinates);
                    board.removePiece(toCoordinates.add(0, -oneForward));
                }
                case CASTLING -> {
                    ChessDirection chessDirection = fromCoordinates.getX() - toCoordinates.getX() > 0 ? ChessDirection.LEFT : ChessDirection.RIGHT;
                    Coordinates toKingCoordinates = fromCoordinates.clone().addX(chessDirection.getOneStep()*2);
                    pieceMovementLog = new PieceMovementLog(pieceMovementLog.getPiece(), pieceMovementLog.getFromCoordinates(), toKingCoordinates, chessBoardJson);

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

                Coordinates king = new Coordinates(4, squarePieceColor.getFirstPiecesLine());

                Coordinates kingRight3 = new Coordinates(5, squarePieceColor.getFirstPiecesLine());
                Coordinates kingRight2 = new Coordinates(6, squarePieceColor.getFirstPiecesLine());
                Coordinates kingRight1 = new Coordinates(7, squarePieceColor.getFirstPiecesLine());

                //If the king has not moved
                if (chessBoard.getPieceMovementLogs().stream().noneMatch(pieceMovementLog -> pieceMovementLog.getPiece().getPieceColor().equals(squarePieceColor)
                                && pieceMovementLog.getPiece().getType() == ChessPieceType.KING) && chessBoard.getSquare(king).hasPiece() && chessBoard.getSquare(king).getPiece().getType().equals(ChessPieceType.KING)
                ) {


                    //If the left tower has not moved
                    if (!chessBoard.getSquare(kingLeft2).hasPiece() && !chessBoard.getSquare(kingLeft3).hasPiece() && !chessBoard.getSquare(kingLeft4).hasPiece() &&
                            chessBoard.getPieceMovementLogs().stream().noneMatch(pieceMovementLog -> pieceMovementLog.getPiece().getPieceColor().equals(squarePieceColor)
                                    && pieceMovementLog.getPiece().getType() == ChessPieceType.ROOK
                                    && pieceMovementLog.getFromCoordinates().equals(kingLeft1) && chessBoard.getSquare(kingLeft1).hasPiece() &&  chessBoard.getSquare(kingLeft1).getPiece().getType().equals(ChessPieceType.ROOK) )) {
                        check(chessBoard, squarePieceColor, squareCoordinates.clone(), coordinates -> coordinates.set(kingLeft1), false, possibleSquare, PieceMovementRules.CASTLING);
                        check(chessBoard, squarePieceColor,  squareCoordinates.clone(), coordinates -> coordinates.set(kingLeft2), false, possibleSquare, PieceMovementRules.CASTLING);
                        check(chessBoard, squarePieceColor, squareCoordinates.clone(), coordinates -> coordinates.set(kingLeft3), false, possibleSquare, PieceMovementRules.CASTLING);
                    }

                    //If the right tower has not moved
                    if (!chessBoard.getSquare(kingRight3).hasPiece() && !chessBoard.getSquare(kingRight2).hasPiece() &&
                            chessBoard.getPieceMovementLogs().stream().noneMatch(pieceMovementLog -> pieceMovementLog.getPiece().getPieceColor().equals(squarePieceColor)
                                    && pieceMovementLog.getPiece().getType() == ChessPieceType.ROOK
                                    && pieceMovementLog.getFromCoordinates().equals(kingRight1)) && chessBoard.getSquare(kingRight1).hasPiece() &&  chessBoard.getSquare(kingRight1).getPiece().getType().equals(ChessPieceType.ROOK)) {
                        check(chessBoard, squarePieceColor, squareCoordinates.clone(), coordinates -> coordinates.set(kingRight1), false, possibleSquare, PieceMovementRules.CASTLING);
                        check(chessBoard, squarePieceColor, squareCoordinates.clone(), coordinates -> coordinates.set(kingRight2), false, possibleSquare, PieceMovementRules.CASTLING);
                    }//TODO
                }
            }
            case PAWN -> {
                int oneStep = squarePieceColor.getOneStep();
                int secondLine = squarePieceColor.getFirstPiecesLine() + oneStep;
                int promotionLine = squarePieceColor.toggle().getFirstPiecesLine() - oneStep;

                //normal advancement
                ChessSquare oneStepSquare = chessBoard.getSquare(squareCoordinates.clone().addY(oneStep));
                 if (oneStepSquare != null && !oneStepSquare.hasPiece()) {
                     check(chessBoard, squarePieceColor, squareCoordinates.clone(), coordinates -> coordinates.addY(oneStep), false, possibleSquare, squareCoordinates.getY() == promotionLine ? PieceMovementRules.PROMOTION : PieceMovementRules.DEFAULT);

                     ChessSquare twoStepSquare = chessBoard.getSquare(squareCoordinates.clone().addY(oneStep*2));
                     if (twoStepSquare != null && squareCoordinates.getY() == secondLine && !twoStepSquare.hasPiece())
                         check(chessBoard, squarePieceColor, squareCoordinates.clone(), coordinates -> coordinates.addY(oneStep*2), false, possibleSquare, squareCoordinates.getY() == promotionLine ? PieceMovementRules.PROMOTION : PieceMovementRules.DEFAULT);
                 }

                 //normal capture left then right
                ChessSquare takeLeftSquare = chessBoard.getSquare(squareCoordinates.clone().add(-1, oneStep));
                if (takeLeftSquare != null && takeLeftSquare.hasPiece())
                    check(chessBoard, squarePieceColor, squareCoordinates.clone(), coordinates -> coordinates.add(-1, oneStep), false, possibleSquare, squareCoordinates.getY() == promotionLine ? PieceMovementRules.PROMOTION : PieceMovementRules.DEFAULT);


                ChessSquare takeRightSquare = chessBoard.getSquare(squareCoordinates.clone().add(1, oneStep));
                if (takeRightSquare != null && takeRightSquare.hasPiece())
                    check(chessBoard, squarePieceColor, squareCoordinates.clone(), coordinates -> coordinates.add(1, oneStep), false, possibleSquare, squareCoordinates.getY() == promotionLine ? PieceMovementRules.PROMOTION : PieceMovementRules.DEFAULT);


                //capture "en passant" left then right
                if(chessBoard.getLastPieceMovementLog() != null) {
                    PieceMovementLog lastPieceMovementLog = chessBoard.getLastPieceMovementLog();

                    Coordinates enPassantLeft = squareCoordinates.clone().add(-1, 0);
                    if (chessBoard.getSquare(enPassantLeft) != null && chessBoard.getSquare(enPassantLeft).hasPiece()
                            && lastPieceMovementLog.getToCoordinates().equals(enPassantLeft)
                            && lastPieceMovementLog.getPiece().getType() == ChessPieceType.PAWN
                            && abs(lastPieceMovementLog.getToCoordinates().getY() - lastPieceMovementLog.getFromCoordinates().getY()) == 2) {
                        Coordinates enPassantMovement = squareCoordinates.clone().add(-1, oneStep);
                        check(chessBoard, squarePieceColor, squareCoordinates.clone(), coordinates -> coordinates.set(enPassantMovement), false, possibleSquare, PieceMovementRules.EN_PASSANT);
                    }

                    Coordinates enPassantRight = squareCoordinates.clone().add(1, 0);
                    if (chessBoard.getSquare(enPassantRight) != null && chessBoard.getSquare(enPassantRight).hasPiece()
                            && lastPieceMovementLog.getToCoordinates().equals(enPassantRight)
                            && lastPieceMovementLog.getPiece().getType() == ChessPieceType.PAWN
                            && abs(lastPieceMovementLog.getToCoordinates().getY() - lastPieceMovementLog.getFromCoordinates().getY()) == 2) {
                        Coordinates enPassantRightMovement = squareCoordinates.clone().add(1, oneStep);
                        check(chessBoard, squarePieceColor, squareCoordinates.clone(), coordinates -> coordinates.set(enPassantRightMovement), false, possibleSquare, PieceMovementRules.EN_PASSANT);
                    }
                }
            }
            case NIGHTRIDER -> {
                check(chessBoard, squarePieceColor, squareCoordinates.clone(), coordinates -> coordinates.add(1, -2), true, possibleSquare);
                check(chessBoard, squarePieceColor, squareCoordinates.clone(), coordinates -> coordinates.add(2, -1), true, possibleSquare);
                check(chessBoard, squarePieceColor, squareCoordinates.clone(), coordinates -> coordinates.add(2, 1), true, possibleSquare);
                check(chessBoard, squarePieceColor, squareCoordinates.clone(), coordinates -> coordinates.add(1, 2), true, possibleSquare);
                check(chessBoard, squarePieceColor, squareCoordinates.clone(), coordinates -> coordinates.add(-1, 2), true, possibleSquare);
                check(chessBoard, squarePieceColor, squareCoordinates.clone(), coordinates -> coordinates.add(-2, 1), true, possibleSquare);
                check(chessBoard, squarePieceColor, squareCoordinates.clone(), coordinates -> coordinates.add(-2, -1), true, possibleSquare);
                check(chessBoard, squarePieceColor, squareCoordinates.clone(), coordinates -> coordinates.add(-1, -2), true, possibleSquare);
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
