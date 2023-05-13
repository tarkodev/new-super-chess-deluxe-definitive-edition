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
    public static final int nbGameModes = 2;
    private final CompletableFuture<String> gameModeChosen = new CompletableFuture<>();

    public SelectGameMode() {
        stage = new Stage();
        GridPane gridPane = new GridPane();

        Button[] buttons = new Button[nbGameModes];
        buttons[0] = new Button("normal game mode");
        buttons[0].setStyle("-fx-background-color: " + ChessRender.getColorHexa(ChessRender.CHESS_SQUARE_COLOR_1.interpolate(ChessRender.CHESS_SQUARE_COLOR_2, 0.5)) + "; -fx-background-radius: 8px;");
        for (int i=0; i<nbGameModes; i++) {
            int finalI = i;
            if (finalI != 0) {
                buttons[i] = new Button("fairy game mode ");
                buttons[finalI].setStyle("-fx-background-color: " + ChessRender.getColorHexa(Color.PINK) + "; -fx-background-radius: 8px;");
            }
            buttons[i].setWrapText(true);
            buttons[i].setTextAlignment(TextAlignment.CENTER);
            buttons[i].setPrefWidth(ChessRender.CHESS_SQUARE_SIZE);
            buttons[i].setPrefHeight(ChessRender.CHESS_SQUARE_SIZE);
            buttons[i].setPadding(new Insets(0));

            gridPane.add(buttons[i], i, 0);
            buttons[i].setOnAction(event -> {
                if (finalI == 0) {
                    gameModeChosen.complete("normal");
                }
                else {
                    gameModeChosen.complete("fairy");
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

    public String getGameModeChosen() throws InterruptedException, ExecutionException {
        return gameModeChosen.get(); // wait for the user input and return the result
    }
}
