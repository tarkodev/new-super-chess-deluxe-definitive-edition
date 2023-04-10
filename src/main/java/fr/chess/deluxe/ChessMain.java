package fr.chess.deluxe;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import fr.chess.deluxe.utils.ChessSquareTypeAdapter;
import fr.chess.deluxe.utils.Coordinates;
import fr.chess.deluxe.utils.CoordinatesTypeAdapter;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class ChessMain extends Application {

    public static final Gson GSON = new GsonBuilder().disableHtmlEscaping()
            .registerTypeAdapter(Coordinates .class, new CoordinatesTypeAdapter())
            .registerTypeAdapter(ChessSquare[][].class, new ChessSquareTypeAdapter())
            .create();

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