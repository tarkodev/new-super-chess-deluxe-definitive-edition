package fr.chess.deluxe;

import fr.chess.deluxe.movement.PieceAction;
import fr.chess.deluxe.movement.PieceMovementLog;
import fr.chess.deluxe.piece.*;
import fr.chess.deluxe.utils.ChessColor;
import fr.chess.deluxe.utils.Coordinates;
import fr.chess.deluxe.utils.PlayerInformation;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ChessBoard{

    public static final int CHESS_SQUARE_LENGTH = 8;

    private final ChessSquare[][] squareBoard = new ChessSquare[CHESS_SQUARE_LENGTH][CHESS_SQUARE_LENGTH];
    private final List<PieceMovementLog> pieceMovementLogs = new ArrayList<>();

    private ChessColor currentPlayer = ChessColor.WHITE;
    private ChessSquare selectedSquare = null;

    private boolean forTestPurpose = false;

    public void setForTestPurpose(boolean forTestPurpose) {
        this.forTestPurpose = forTestPurpose;
    }

    public boolean isForTestPurpose() {
        return forTestPurpose;
    }

    public List<PieceMovementLog> getPieceMovementLogs() {
        return pieceMovementLogs;
    }

    public PieceMovementLog getLastPieceMovementLog() {
        return pieceMovementLogs.isEmpty() ? null : pieceMovementLogs.get(pieceMovementLogs.size()-1);
    }

    public ChessSquare getSelectedSquare() {
        return selectedSquare;
    }

    public void setSelectedSquare(ChessSquare selectedSquare) {
        this.selectedSquare = selectedSquare;
    }

    public ChessSquare getSquare(Coordinates coordinates) {
        return coordinates.isValid() ? this.squareBoard[coordinates.getX()][coordinates.getY()] : null;
    }

    public ChessColor getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(ChessColor currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public void switchCurrentPlayer() {
        setCurrentPlayer(getCurrentPlayer() == ChessColor.WHITE ? ChessColor.BLACK : ChessColor.WHITE);
        selectedSquare = null;
    }

    public ChessBoard() {
        initChessBoardSquare();
        loadPieces();
    }

    private void initChessBoardSquare() {
        for (int x = 0; x < CHESS_SQUARE_LENGTH; x++) {
            for (int y = 0; y < CHESS_SQUARE_LENGTH; y++) {
                Color color = ((x + y) % 2) == 0 ? ChessRender.CHESS_SQUARE_COLOR_1 : ChessRender.CHESS_SQUARE_COLOR_2;
                ChessSquare chessSquare = new ChessSquare(color, new Coordinates(x, y));
                squareBoard[x][y] = chessSquare;
            }
        }
    }

    public void loadPieces() {
        // White
        setPiece(new Coordinates("a1"), new ChessPieceRook(ChessColor.WHITE));
        setPiece(new Coordinates("h1"), new ChessPieceRook(ChessColor.WHITE));
        setPiece(new Coordinates("b1"), new ChessPieceKnight(ChessColor.WHITE));
        setPiece(new Coordinates("g1"), new ChessPieceKnight(ChessColor.WHITE));
        setPiece(new Coordinates("c1"), new ChessPieceBishop(ChessColor.WHITE));
        setPiece(new Coordinates("f1"), new ChessPieceBishop(ChessColor.WHITE));
        setPiece(new Coordinates("d1"), new ChessPieceQueen(ChessColor.WHITE));
        setPiece(new Coordinates("e1"), new ChessPieceKing(ChessColor.WHITE));
        Coordinates firstPawnWhite = new Coordinates("a2");
        for (int i = 0; i < CHESS_SQUARE_LENGTH; i++) {
            setPiece(firstPawnWhite, new ChessPiecePawn(ChessColor.WHITE));
            firstPawnWhite.setX(firstPawnWhite.getX()+1);
        }

        // Black
        setPiece(new Coordinates("a8"), new ChessPieceRook(ChessColor.BLACK));
        setPiece(new Coordinates("h8"), new ChessPieceRook(ChessColor.BLACK));
        setPiece(new Coordinates("b8"), new ChessPieceKnight(ChessColor.BLACK));
        setPiece(new Coordinates("g8"), new ChessPieceKnight(ChessColor.BLACK));
        setPiece(new Coordinates("c8"), new ChessPieceBishop(ChessColor.BLACK));
        setPiece(new Coordinates("f8"), new ChessPieceBishop(ChessColor.BLACK));
        setPiece(new Coordinates("d8"), new ChessPieceQueen(ChessColor.BLACK));
        setPiece(new Coordinates("e8"), new ChessPieceKing(ChessColor.BLACK));
        Coordinates firstPawnBlack = new Coordinates("a7");
        for (int i = 0; i < CHESS_SQUARE_LENGTH; i++) {
            setPiece(firstPawnBlack, new ChessPiecePawn(ChessColor.BLACK));
            firstPawnBlack.setX(firstPawnBlack.getX()+1);
        }
    }


    public Map<ChessColor, PlayerInformation> getPlayerInformation() {
        Map<ChessColor, PlayerInformation> playersInformationSet = new ConcurrentHashMap<>();
        Arrays.stream(ChessColor.values()).forEach(chessColors -> playersInformationSet.put(chessColors, new PlayerInformation()));

        for (int x = 0; x < CHESS_SQUARE_LENGTH; x++) {
            for (int y = 0; y < CHESS_SQUARE_LENGTH; y++) {
                Coordinates searchCoordinates = new Coordinates(x, y);
                ChessSquare searchSquare = getSquare(searchCoordinates);

                if(searchSquare.hasPiece()) {
                    ChessColor searchColor = searchSquare.getPiece().getPieceColor();
                    PlayerInformation playerInformation = playersInformationSet.get(searchColor);
                    if(searchSquare.getPiece().getPieceColor().equals(searchColor)) {
                        playerInformation.getPossibleMoves().addAll(searchSquare.getPiece().getPossibleMoves(this, searchCoordinates).keySet());
                        if(searchSquare.getPiece() instanceof ChessPieceKing) {
                            playerInformation.setKingPosition(searchCoordinates);
                        }
                    }
                }
            }
        }

        playersInformationSet.forEach((chessColors, playerInformation) -> {
            if(playersInformationSet.get(chessColors.toggle()).getPossibleMoves().contains(playerInformation.getKingPosition())) {
                playerInformation.setCheckStatus(PlayerInformation.CheckStatus.CHECK);
                if(playerInformation.getPossibleMoves().isEmpty()) {
                    playerInformation.setCheckStatus(PlayerInformation.CheckStatus.CHECKMATE);
                }
            }
        });

        return playersInformationSet;
    }

    public void moveEvent(Coordinates from, Coordinates to) {
        if(getSquare(from).hasPiece()) {
            getSquare(from).getPiece().getPossibleMoves(this, this.getSquare(from).getCoordinates()).get(to).accept(to);
        }
    }

    public void setPiece(Coordinates coordinates, ChessPiece piece) {
        getSquare(coordinates).setPiece(piece);
    }

    public void cancelLastPieceMovement() {
        PieceMovementLog lastPieceMovementLog = pieceMovementLogs.get(pieceMovementLogs.size()-1);
        lastPieceMovementLog.apply(this, true);
        pieceMovementLogs.remove(lastPieceMovementLog);
    }

    public void setPiece(Coordinates coordinates, ChessPiece piece, PieceMovementLog pieceMovementLog) {
        ChessSquare square = getSquare(coordinates);
        if(square.hasPiece()) removePiece(coordinates, pieceMovementLog);
        pieceMovementLog.getPieceActions().add(new PieceAction(coordinates, PieceAction.Type.SET, piece));
    }

    public ChessPiece removePiece(Coordinates coordinates, PieceMovementLog pieceMovementLog) {
        ChessSquare square = getSquare(coordinates);
        ChessPiece piece = square.getPiece();
        pieceMovementLog.getPieceActions().add(new PieceAction(coordinates, PieceAction.Type.REMOVE, piece));
        return piece;
    }

    public void movePiece(Coordinates from, Coordinates to, PieceMovementLog pieceMovementLog) {
        setPiece(to, removePiece(from, pieceMovementLog), pieceMovementLog);
    }


}
