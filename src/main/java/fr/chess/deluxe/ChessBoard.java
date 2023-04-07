package fr.chess.deluxe;

import fr.chess.deluxe.movement.PieceMovementLog;
import fr.chess.deluxe.piece.*;
import fr.chess.deluxe.utils.ChessColor;
import fr.chess.deluxe.utils.Coordinates;
import fr.chess.deluxe.utils.PlayerInformation;
import javafx.scene.paint.Color;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ChessBoard implements Cloneable{

    public static final int CHESS_SQUARE_LENGTH = 8;

    private ChessSquare[][] squareBoard = new ChessSquare[CHESS_SQUARE_LENGTH][CHESS_SQUARE_LENGTH];
    private List<PieceMovementLog> pieceMovementLogs = new ArrayList<>();

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

    protected void setSqare(Coordinates coordinates, ChessSquare chessSquare) {
        this.squareBoard[coordinates.getX()][coordinates.getY()] = chessSquare;
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
                ChessSquare chessSquare = new ChessSquare(this, color, new Coordinates(x, y));
                squareBoard[x][y] = chessSquare;
            }
        }
    }

    public void loadPieces() {
        // White
        setPiece(new ChessPieceRook(ChessColor.WHITE), new Coordinates("a1"));
        setPiece(new ChessPieceRook(ChessColor.WHITE), new Coordinates("h1"));
        setPiece(new ChessPieceKnight(ChessColor.WHITE), new Coordinates("b1"));
        setPiece(new ChessPieceKnight(ChessColor.WHITE), new Coordinates("g1"));
        setPiece(new ChessPieceBishop(ChessColor.WHITE), new Coordinates("c1"));
        setPiece(new ChessPieceBishop(ChessColor.WHITE), new Coordinates("f1"));
        setPiece(new ChessPieceQueen(ChessColor.WHITE), new Coordinates("d1"));
        setPiece(new ChessPieceKing(ChessColor.WHITE), new Coordinates("e1"));
        Coordinates firstPawnWhite = new Coordinates("a2");
        for (int i = 0; i < CHESS_SQUARE_LENGTH; i++) {
            setPiece(new ChessPiecePawn(ChessColor.WHITE), firstPawnWhite);
            firstPawnWhite.setX(firstPawnWhite.getX()+1);
        }

        // Black
        setPiece(new ChessPieceRook(ChessColor.BLACK), new Coordinates("a8"));
        setPiece(new ChessPieceRook(ChessColor.BLACK), new Coordinates("h8"));
        setPiece(new ChessPieceKnight(ChessColor.BLACK), new Coordinates("b8"));
        setPiece(new ChessPieceKnight(ChessColor.BLACK), new Coordinates("g8"));
        setPiece(new ChessPieceBishop(ChessColor.BLACK), new Coordinates("c8"));
        setPiece(new ChessPieceBishop(ChessColor.BLACK), new Coordinates("f8"));
        setPiece(new ChessPieceQueen(ChessColor.BLACK), new Coordinates("d8"));
        setPiece(new ChessPieceKing(ChessColor.BLACK), new Coordinates("e8"));
        Coordinates firstPawnBlack = new Coordinates("a7");
        for (int i = 0; i < CHESS_SQUARE_LENGTH; i++) {
            setPiece(new ChessPiecePawn(ChessColor.BLACK), firstPawnBlack);
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
            if(playersInformationSet.get(chessColors.inverse()).getPossibleMoves().contains(playerInformation.getKingPosition())) {
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

    public void setPiece(ChessPiece piece, Coordinates coordinates) {
        getSquare(coordinates).setPiece(piece);
    }

    public void move(Coordinates from, Coordinates to) {
        ChessPiece fromPiece = getSquare(from).getPiece();
        getSquare(from).removePiece();
        getSquare(to).setPiece(fromPiece);
    }

    @Override
    public ChessBoard clone() {
        try {
            ChessBoard clone = (ChessBoard) super.clone();

            // Copier squareBoard
            clone.squareBoard = new ChessSquare[CHESS_SQUARE_LENGTH][CHESS_SQUARE_LENGTH];
            for (int x = 0; x < CHESS_SQUARE_LENGTH; x++) {
                for (int y = 0; y < CHESS_SQUARE_LENGTH; y++) {
                    clone.squareBoard[x][y] = squareBoard[x][y].clone();
                    clone.squareBoard[x][y].setChessBoard(clone);
                }
            }

            clone.forTestPurpose = true;

            // Copier pieceMovementLogs
            clone.pieceMovementLogs = new ArrayList<>(pieceMovementLogs);

            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
