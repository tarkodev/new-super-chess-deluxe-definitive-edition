package fr.chess.deluxe;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import fr.chess.deluxe.utils.ChessSquareTypeAdapter;
import fr.chess.deluxe.utils.Coordinates;
import fr.chess.deluxe.utils.CoordinatesTypeAdapter;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Classe principale, génère:
 *  -le GSON qui contiendra les différents coups déjà fait dans la partie,
 *    ainsi que les différents états du plateau au fil de la partie
 *  -chessBoard stockes les différentes cases du plateau, ainsi que des informations comme les règles,
 *    le joueur dont c'est le tour...
 *  -chessRender contient les informations sur comment afficher le plateau,
 *    comme les couleurs des différents états possibles des cases
 */
public class ChessMain extends Application {

    public static final Gson GSON = new GsonBuilder().disableHtmlEscaping()
            .registerTypeAdapter(Coordinates .class, new CoordinatesTypeAdapter())
            .registerTypeAdapter(ChessSquare[][].class, new ChessSquareTypeAdapter())
            .setPrettyPrinting()
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
