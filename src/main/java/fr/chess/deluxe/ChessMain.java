package fr.chess.deluxe;

import javafx.application.Application;
import javafx.stage.Stage;

public class ChessMain extends Application {

    public static void main(String[] args) {
        launch();
    }
    @Override
    public void start(Stage stage) {
        ChessBoard chessBoard = new ChessBoard();
        ChessRender chessRender = new ChessRender(stage, chessBoard);
        chessRender.render();
    }
}
