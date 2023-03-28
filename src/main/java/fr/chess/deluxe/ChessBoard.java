package fr.chess.deluxe;

import fr.chess.deluxe.piece.*;
import fr.chess.deluxe.utils.ChessColor;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class ChessBoard extends Application {

    private static final int CHESS_SQUARE_SIZE = 100;
    private static final int CHESS_SQUARE_LENGTH = 8;

    private static final String CHESS_SQUARE_COLOR_1 = "#EFCCA6";
    private static final String CHESS_SQUARE_COLOR_2 = "#3C1D18";


    private final ChessSquare[][] board = new ChessSquare[CHESS_SQUARE_LENGTH][CHESS_SQUARE_LENGTH];

    private ChessColor actualPlayer = ChessColor.WHITE;
    private ChessSquare selectedSquare = null;

    public static int getChessSquareLength() {
        return CHESS_SQUARE_LENGTH;
    }

    @Override
    public void start(Stage stage) {
        GridPane chessBoardRender = initChessBoardRender();

        loadPieces();

        Scene scene = new Scene(chessBoardRender, CHESS_SQUARE_SIZE*CHESS_SQUARE_LENGTH, CHESS_SQUARE_SIZE*CHESS_SQUARE_LENGTH);
        stage.setResizable(false);
        stage.setTitle("New Super Chess Deluxe Definitive Edition++");
        stage.setScene(scene);
        stage.show();
    }

    public ChessColor getActualPlayer() {
        return actualPlayer;
    }

    public void setActualPlayer(ChessColor actualPlayer) {
        this.actualPlayer = actualPlayer;
    }

    public GridPane initChessBoardRender() {
        GridPane chessBoardRender = new GridPane();

        for (int x = 0; x < CHESS_SQUARE_LENGTH; x++) {
            for (int y = 0; y < CHESS_SQUARE_LENGTH; y++) {
                String color = ((x+y) % 2) == 0 ? CHESS_SQUARE_COLOR_1 : CHESS_SQUARE_COLOR_2;

                Button button = new Button("");
                button.setPrefWidth(CHESS_SQUARE_SIZE);
                button.setPrefHeight(CHESS_SQUARE_SIZE);
                button.setPadding(new Insets(0));
                button.setStyle("-fx-background-color: " + color + "; -fx-background-radius: 0px;");

                chessBoardRender.add(button, x*CHESS_SQUARE_SIZE, y*CHESS_SQUARE_SIZE);
                this.getBoard()[x][y] = new ChessSquare(this, x, y, button);

                final int finalX = x;
                final int finalY = y;
                button.setOnAction(actionEvent -> {
                    ChessSquare clickedSquare = this.getBoard()[finalX][finalY];
                    //bouger la piece
                    if(selectedSquare != null && selectedSquare.hasPiece()
                            && selectedSquare.getPiece().getPieceColor() == actualPlayer
                            && selectedSquare.getPossibleMoves().contains(clickedSquare)) {
                        clickedSquare.setPiece(selectedSquare.getPiece());
                        selectedSquare.removePiece();
                        actualPlayer = actualPlayer == ChessColor.WHITE ? ChessColor.BLACK : ChessColor.WHITE;
                    } else {
                        selectedSquare = clickedSquare;
                    }

                    System.out.println(this.convertToString(finalX, finalY));
                });
            }
        }

        return chessBoardRender;
    }

    public void loadPieces() {
        this.getBoard()[0][CHESS_SQUARE_LENGTH-1].setPiece(new ChessPieceRook(ChessColor.WHITE));
        this.getBoard()[7][CHESS_SQUARE_LENGTH-1].setPiece(new ChessPieceRook(ChessColor.WHITE));
        this.getBoard()[1][CHESS_SQUARE_LENGTH-1].setPiece(new ChessPieceKnight(ChessColor.WHITE));
        this.getBoard()[6][CHESS_SQUARE_LENGTH-1].setPiece(new ChessPieceKnight(ChessColor.WHITE));
        this.getBoard()[2][CHESS_SQUARE_LENGTH-1].setPiece(new ChessPieceBishop(ChessColor.WHITE));
        this.getBoard()[5][CHESS_SQUARE_LENGTH-1].setPiece(new ChessPieceBishop(ChessColor.WHITE));
        this.getBoard()[3][CHESS_SQUARE_LENGTH-1].setPiece(new ChessPieceQueen(ChessColor.WHITE));
        this.getBoard()[4][CHESS_SQUARE_LENGTH-1].setPiece(new ChessPieceKing(ChessColor.WHITE));
        for (int i = 0; i < CHESS_SQUARE_LENGTH; i++) {
            this.getBoard()[i][CHESS_SQUARE_LENGTH-2].setPiece(new ChessPiecePawn(ChessColor.WHITE));
        }
        this.getBoard()[0][0].setPiece(new ChessPieceRook(ChessColor.BLACK));
        this.getBoard()[7][0].setPiece(new ChessPieceRook(ChessColor.BLACK));
        this.getBoard()[1][0].setPiece(new ChessPieceKnight(ChessColor.BLACK));
        this.getBoard()[6][0].setPiece(new ChessPieceKnight(ChessColor.BLACK));
        this.getBoard()[2][0].setPiece(new ChessPieceBishop(ChessColor.BLACK));
        this.getBoard()[5][0].setPiece(new ChessPieceBishop(ChessColor.BLACK));
        this.getBoard()[3][0].setPiece(new ChessPieceQueen(ChessColor.BLACK));
        this.getBoard()[4][0].setPiece(new ChessPieceKing(ChessColor.BLACK));
        for (int i = 0; i < CHESS_SQUARE_LENGTH; i++) {
            this.getBoard()[i][1].setPiece(new ChessPiecePawn(ChessColor.BLACK));
        }
    }

    public static void main(String[] args) {
        launch();
    }

    public ChessSquare[][] getBoard() {
        return board;
    }

    public String convertToString(int x, int y) {
        String xString = String.valueOf((char) ('a' + x));
        String yString = String.valueOf(CHESS_SQUARE_LENGTH-y);
        return xString + yString;
    }
}
