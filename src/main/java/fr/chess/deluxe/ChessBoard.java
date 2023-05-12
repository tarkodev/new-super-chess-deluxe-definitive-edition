package fr.chess.deluxe;

import fr.chess.deluxe.movement.PieceMovementLog;
import fr.chess.deluxe.movement.PieceMovementRules;
import fr.chess.deluxe.piece.ChessPiece;
import fr.chess.deluxe.piece.ChessPieceType;
import fr.chess.deluxe.utils.ChessColor;
import fr.chess.deluxe.utils.Coordinates;
import fr.chess.deluxe.utils.PlayerInformation;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Cette classe gère le modèle, elle contient les informations comme les règles, les cases du plateau (qui elle mêmes
 *  peuvent contenir des pièces), le joueur à qui c'est le tour, ou encore la liste des mouvements faits.
 */
public class ChessBoard {

    public static final int CHESS_SQUARE_LENGTH = 8;


    private Set<PieceMovementRules> rules;
    private final ChessSquare[][] squareBoard = new ChessSquare[CHESS_SQUARE_LENGTH][CHESS_SQUARE_LENGTH];

    private ChessColor currentPlayer = ChessColor.WHITE;
    private List<PieceMovementLog> pieceMovementLogs = new ArrayList<>();
    private transient ChessSquare selectedSquare = null;

    private transient boolean clone = false;
    private String gameMode;



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

    public String getGameMode() {
        return gameMode;
    }

    public void setCurrentPlayer(ChessColor currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public void switchCurrentPlayer() {
        setCurrentPlayer(getCurrentPlayer() == ChessColor.WHITE ? ChessColor.BLACK : ChessColor.WHITE);
        selectedSquare = null;
    }

    public ChessBoard(Set<PieceMovementRules> rules, String gameMode) {
        this.rules = rules;
        this.gameMode = gameMode;
        initChessBoardSquare();
        loadPieces(gameMode);
    }

    public ChessBoard(String gameMode) {
        this(Arrays.stream(PieceMovementRules.values()).collect(Collectors.toSet()), gameMode);
    }

    public ChessBoard(ChessBoard chessBoard) {
        this.clone = true;
        this.pieceMovementLogs = new ArrayList<>();

        this.rules = chessBoard.getRules();
        this.currentPlayer = chessBoard.currentPlayer;
        this.selectedSquare = chessBoard.selectedSquare;
        this.gameMode = chessBoard.getGameMode();

        for (int x = 0; x < CHESS_SQUARE_LENGTH; x++) {
            for (int y = 0; y < CHESS_SQUARE_LENGTH; y++) {
                ChessSquare chessSquare = new ChessSquare(chessBoard.squareBoard[x][y]);
                squareBoard[x][y] = chessSquare;
            }
        }
    }

    public void copyFrom(ChessBoard chessBoard) {
        this.rules = chessBoard.getRules();
        this.currentPlayer = chessBoard.getCurrentPlayer();
        this.gameMode = chessBoard.getGameMode();
        this.selectedSquare = null;
        for (int i=0; i<CHESS_SQUARE_LENGTH; i++) {
            System.arraycopy(chessBoard.squareBoard[i], 0, this.squareBoard[i], 0, CHESS_SQUARE_LENGTH);
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

    public void loadPieces(String gameMode) {
        String path = new String("src/main/resources/JSONSaveConfigurations/" + gameMode + ".chessboardsavefileforthebestgameevercreatednamednewsuperchessdeluxedefinitiveeditionplusplus");
        File selectedFile;
        selectedFile = new File(path);

        System.out.println("Selected file: " + selectedFile.getAbsolutePath());
        try {
            String content = new String(Files.readAllBytes(selectedFile.toPath()), StandardCharsets.UTF_8);

            // Process the content as needed
            ChessBoard chessBoard = ChessMain.GSON.fromJson(content, ChessBoard.class);
            this.copyFrom(chessBoard);
        } catch (IOException e) {
            System.err.println("Error reading from file: " + e.getMessage());
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

    public Set<PieceMovementRules> getRules() {
        return rules;
    }

    public void movePiece(Coordinates from, Coordinates to) {
        setPiece(to, removePiece(from));
    }
}
