package fr.chess.deluxe;

import fr.chess.deluxe.movement.PieceMovementLog;
import fr.chess.deluxe.piece.ChessPiece;
import fr.chess.deluxe.piece.ChessPieceType;
import fr.chess.deluxe.utils.ChessColor;
import fr.chess.deluxe.utils.Coordinates;
import fr.chess.deluxe.utils.PlayerInformation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ChessBoard {

    public static final int CHESS_SQUARE_LENGTH = 8;

    private final ChessSquare[][] squareBoard = new ChessSquare[CHESS_SQUARE_LENGTH][CHESS_SQUARE_LENGTH];

    private ChessColor currentPlayer = ChessColor.WHITE;
    private List<PieceMovementLog> pieceMovementLogs = new ArrayList<>();
    private transient ChessSquare selectedSquare = null;

    private transient boolean clone = false;



    public void setPieceMovementLogs(List<PieceMovementLog> pieceMovementLogs) {
        this.pieceMovementLogs = pieceMovementLogs;
    }

    public void setClone(boolean clone) {
        this.clone = clone;
    }

    public boolean isClone() {
        return clone;
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

    public ChessBoard(ChessBoard chessBoard) {
        this.clone = true;
        this.pieceMovementLogs = new ArrayList<>();

        this.currentPlayer = chessBoard.currentPlayer;
        this.selectedSquare = chessBoard.selectedSquare;

        for (int x = 0; x < CHESS_SQUARE_LENGTH; x++) {
            for (int y = 0; y < CHESS_SQUARE_LENGTH; y++) {
                ChessSquare chessSquare = new ChessSquare(chessBoard.squareBoard[x][y]);
                squareBoard[x][y] = chessSquare;
            }
        }
    }

    private void initChessBoardSquare() {
        for (int x = 0; x < CHESS_SQUARE_LENGTH; x++) {
            for (int y = 0; y < CHESS_SQUARE_LENGTH; y++) {
                ChessSquare chessSquare = new ChessSquare(new Coordinates(x, y));
                squareBoard[x][y] = chessSquare;
            }
        }
    }

    public void loadPieces() {
        // White
        setPiece(new Coordinates("a1"), new ChessPiece(ChessPieceType.EMPRESS, ChessColor.WHITE));
        setPiece(new Coordinates("h1"), new ChessPiece(ChessPieceType.ROOK, ChessColor.WHITE));
        setPiece(new Coordinates("b1"), new ChessPiece(ChessPieceType.KNIGHT, ChessColor.WHITE));
        setPiece(new Coordinates("g1"), new ChessPiece(ChessPieceType.NIGHTRIDER, ChessColor.WHITE));
        setPiece(new Coordinates("c1"), new ChessPiece(ChessPieceType.PRINCESS, ChessColor.WHITE));
        setPiece(new Coordinates("f1"), new ChessPiece(ChessPieceType.BISHOP, ChessColor.WHITE));
        setPiece(new Coordinates("d1"), new ChessPiece(ChessPieceType.QUEEN, ChessColor.WHITE));
        setPiece(new Coordinates("e1"), new ChessPiece(ChessPieceType.KING, ChessColor.WHITE));
        Coordinates firstPawnWhite = new Coordinates("a2");
        for (int i = 0; i < CHESS_SQUARE_LENGTH; i++) {
            setPiece(firstPawnWhite, new ChessPiece(ChessPieceType.PAWN, ChessColor.WHITE));
            firstPawnWhite.setX(firstPawnWhite.getX()+1);
        }

        // Black
        setPiece(new Coordinates("a8"), new ChessPiece(ChessPieceType.EMPRESS, ChessColor.BLACK));
        setPiece(new Coordinates("h8"), new ChessPiece(ChessPieceType.ROOK, ChessColor.BLACK));
        setPiece(new Coordinates("b8"), new ChessPiece(ChessPieceType.KNIGHT, ChessColor.BLACK));
        setPiece(new Coordinates("g8"), new ChessPiece(ChessPieceType.NIGHTRIDER, ChessColor.BLACK));
        setPiece(new Coordinates("c8"), new ChessPiece(ChessPieceType.PRINCESS, ChessColor.BLACK));
        setPiece(new Coordinates("f8"), new ChessPiece(ChessPieceType.BISHOP, ChessColor.BLACK));
        setPiece(new Coordinates("d8"), new ChessPiece(ChessPieceType.QUEEN, ChessColor.BLACK));
        setPiece(new Coordinates("e8"), new ChessPiece(ChessPieceType.KING, ChessColor.BLACK));
        Coordinates firstPawnBlack = new Coordinates("a7");
        for (int i = 0; i < CHESS_SQUARE_LENGTH; i++) {
            setPiece(firstPawnBlack, new ChessPiece(ChessPieceType.PAWN, ChessColor.BLACK));
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
                        if(searchSquare.getPiece().getType() == ChessPieceType.KING) {
                            playerInformation.setKingPosition(searchCoordinates);
                        }
                    }
                }
            }
        }


        playersInformationSet.forEach((chessColors, playerInformation) -> {
            if(playerInformation.getPossibleMoves().isEmpty())
                playerInformation.setCheckStatus(PlayerInformation.CheckStatus.STALEMATE);
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
            if(getSquare(from).getPiece().getPossibleMoves(this, this.getSquare(from).getCoordinates()).containsKey(to)) {
                getSquare(from).getPiece().getPossibleMoves(this, this.getSquare(from).getCoordinates()).get(to).accept(to);
            }
        }
    }

    public void setPiece(Coordinates coordinates, ChessPiece piece) {
        ChessSquare square = getSquare(coordinates);
        if(square != null) {
            if(square.hasPiece()) removePiece(coordinates);
            square.setPiece(piece);
        }
    }

    public ChessPiece removePiece(Coordinates coordinates) {
        ChessSquare square = getSquare(coordinates);
        ChessPiece piece = square.getPiece();
        square.setPiece(null);
        return piece;
    }

    public void movePiece(Coordinates from, Coordinates to) {
        setPiece(to, removePiece(from));
    }
}
