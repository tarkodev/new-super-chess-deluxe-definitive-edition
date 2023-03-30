package fr.chess.deluxe;

import fr.chess.deluxe.piece.*;
import fr.chess.deluxe.utils.ChessColor;
import fr.chess.deluxe.utils.Coordinates;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class ChessBoard extends Application {

    public static final int CHESS_SQUARE_SIZE = 100;
    public static final int CHESS_SQUARE_LENGTH = 8;

    public static final String CHESS_SQUARE_COLOR_1 = "#EFCCA6";
    public static final String CHESS_SQUARE_COLOR_2 = "#3C1D18";
    public static final String CHESS_BACKGROUND_COLOR = "#96755F";


    private final ChessSquare[][] board = new ChessSquare[CHESS_SQUARE_LENGTH][CHESS_SQUARE_LENGTH];

    private ChessColor currentPlayer = ChessColor.WHITE;
    private ChessSquare selectedSquare = null;

    @Override
    public void start(Stage stage) {
        GridPane chessBoardRender = initChessBoardRender();
        chessBoardRender.setStyle("-fx-background-color: " + CHESS_BACKGROUND_COLOR);

        loadPieces();

        Scene scene = new Scene(chessBoardRender, CHESS_SQUARE_SIZE * CHESS_SQUARE_LENGTH, CHESS_SQUARE_SIZE * CHESS_SQUARE_LENGTH);
        stage.setResizable(false);
        stage.setTitle("New Super Chess Deluxe Definitive Edition++");
        stage.setScene(scene);
        stage.show();
    }

    public ChessSquare[][] getBoard() {
        return board;
    }

    public ChessSquare getSquare(Coordinates coordinates) {
        return this.board[coordinates.getX()][coordinates.getY()];
    }

    public ChessColor getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(ChessColor currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    private Button initButton(int x, int y) {
        String color = ((x + y) % 2) == 0 ? CHESS_SQUARE_COLOR_1 : CHESS_SQUARE_COLOR_2;

        Button button = new Button("");
        button.setPrefWidth(CHESS_SQUARE_SIZE);
        button.setPrefHeight(CHESS_SQUARE_SIZE);
        button.setPadding(new Insets(0));
        button.setStyle("-fx-background-color: " + color + "; -fx-background-radius: 8px;");

        actionButton(button, new Coordinates(x, y));

        return button;
    }

    private void actionButton(Button button, Coordinates coordinates) {
        button.setOnAction(actionEvent -> {
            ChessSquare clickedSquare = getSquare(coordinates);
            //bouger la piece
            if (selectedSquare != null && selectedSquare.hasPiece()
                    && selectedSquare.getPiece().getPieceColor() == currentPlayer
                    && selectedSquare.getPossibleMoves().contains(clickedSquare)) {



                move(selectedSquare.getCoordinates(), clickedSquare.getCoordinates());
                currentPlayer = currentPlayer == ChessColor.WHITE ? ChessColor.BLACK : ChessColor.WHITE;
            } else {
                selectedSquare = clickedSquare;
            }
        });
    }

    private GridPane initChessBoardRender() {
        GridPane chessBoardRender = new GridPane();

        for (int x = 0; x < CHESS_SQUARE_LENGTH; x++) {
            for (int y = 0; y < CHESS_SQUARE_LENGTH; y++) {
                Button button = initButton(x, y);

                chessBoardRender.add(button, x * CHESS_SQUARE_SIZE, y * CHESS_SQUARE_SIZE);
                board[x][y] = new ChessSquare(this, new Coordinates(x, y), button);
            }
        }

        return chessBoardRender;
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

    public static void main(String[] args) {
        launch();
    }


    public void setPiece(ChessPiece piece, Coordinates coordinates) {
        getSquare(coordinates).setPiece(piece);
    }

    public void move(Coordinates from, Coordinates to) {
        ChessSquare fromSquare = getSquare(from);
        ChessPiece fromPiece = fromSquare.getPiece();
        fromSquare.removePiece();
        setPiece(fromPiece, to);
        if (getSquare(to).getPiece() instanceof ChessPiecePawn) {
            if (to.getY() == 0) {
                getSquare(to).removePiece();
                setPiece(new ChessPieceQueen(ChessColor.WHITE), getSquare(to).getCoordinates());
            } else if (to.getY() == CHESS_SQUARE_LENGTH-1) {
                getSquare(to).removePiece();
                setPiece(new ChessPieceQueen(ChessColor.BLACK), to);
            }
        }
    }
}
