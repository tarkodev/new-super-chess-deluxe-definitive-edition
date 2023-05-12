package fr.chess.deluxe;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class SelectGameMode {
    private final Stage stage;
    private static final int nbGameModes = 2;
    private Button button[] = new Button[nbGameModes];
    private final CompletableFuture<String> gamemodeChosen = new CompletableFuture<>();

    public SelectGameMode() {
        stage = new Stage();
        GridPane gridPane = new GridPane();

        for (int i=0; i<nbGameModes; i++) {
            int finalI = i;
            if (finalI == 0) {
                button[0] = new Button("normal game mode");
            }
            else {
                button[i] = new Button("fairy game mode ");
            }
            button[i].setWrapText(true);
            button[i].setTextAlignment(TextAlignment.CENTER);
            button[i].setPrefWidth(ChessRender.CHESS_SQUARE_SIZE);
            button[i].setPrefHeight(ChessRender.CHESS_SQUARE_SIZE);
            button[i].setPadding(new Insets(0));
            button[0].setStyle("-fx-background-color: " + ChessRender.getColorHexa(ChessRender.CHESS_SQUARE_COLOR_1.interpolate(ChessRender.CHESS_SQUARE_COLOR_2, 0.5)) + "; -fx-background-radius: 8px;");
            button[finalI].setStyle("-fx-background-color: " + ChessRender.getColorHexa(Color.PINK) + "; -fx-background-radius: 8px;");
            gridPane.add(button[i], i, 0);
            button[i].setOnAction(event -> {
                if (finalI == 0) {
                    gamemodeChosen.complete("normal");
                }
                else {
                    gamemodeChosen.complete("fairy");
                }
                stage.close();
            });
        }

        BorderPane root = new BorderPane();
        root.setCenter(gridPane);

        Scene scene = new Scene(root, 2*ChessRender.CHESS_SQUARE_SIZE, 1*ChessRender.CHESS_SQUARE_SIZE);

        stage.setResizable(false);
        stage.setTitle("Select gamemode");
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL); // make the window modal so that the program waits for this window to close before doing the rest
        Image logo = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/assets/logo.png")));
        stage.getIcons().add(logo);

        stage.showAndWait(); // show the window and wait for it to be closed
    }

    public String getGamemodeChosen() throws InterruptedException, ExecutionException {
        return gamemodeChosen.get(); // wait for the user input and return the result
    }
}
