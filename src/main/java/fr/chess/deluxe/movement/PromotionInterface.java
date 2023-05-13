package fr.chess.deluxe.movement;

import fr.chess.deluxe.ChessRender;
import fr.chess.deluxe.piece.ChessPieceType;
import fr.chess.deluxe.utils.ChessColor;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class PromotionInterface {
    private final Stage stage;
    private final CompletableFuture<ChessPieceType> pieceChosen = new CompletableFuture<>();

    public PromotionInterface(int[] nbPossibilities, String gameMode, ChessColor pieceColor) {
        Button[] buttons;
        int actualNbPossibilities;
        if (gameMode.equals("normal"))
            actualNbPossibilities = nbPossibilities[0];
        else
            actualNbPossibilities = nbPossibilities[1];
        buttons = new Button[actualNbPossibilities];
        stage = new Stage();
        GridPane gridPane = new GridPane();

        for (int i = 0; i < actualNbPossibilities; i++) {
            buttons[i] = new Button("");
            buttons[i].setWrapText(true);
            buttons[i].setTextAlignment(TextAlignment.CENTER);
            buttons[i].setPrefWidth(ChessRender.CHESS_SQUARE_SIZE);
            buttons[i].setPrefHeight(ChessRender.CHESS_SQUARE_SIZE);
            buttons[i].setPadding(new Insets(0));
            if (gameMode.equals("normal")) {
                buttons[i].setStyle("-fx-background-color: " + ChessRender.getColorHexa(ChessRender.CHESS_SQUARE_COLOR_1.interpolate(ChessRender.CHESS_SQUARE_COLOR_2, 0.5)) + "; -fx-background-radius: 8px;");
                gridPane.add(buttons[i], i % 2, i / 2);
            } else {
                buttons[i].setStyle("-fx-background-color: " + ChessRender.getColorHexa(Color.PINK) + "; -fx-background-radius: 8px;");
                gridPane.add(buttons[i], i % 4, i / 4);
            }
            String color = (pieceColor.equals(ChessColor.WHITE) ? "w" : "b");
            String pieceName = switch (i) {
                case 1 -> "R";
                case 2 -> "B";
                case 3 -> "N";
                case 4 -> "X";
                case 5 -> "E";
                case 6 -> "S";
                default -> "Q";
            };
            StackPane stackPane = new StackPane();
            Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/assets/pieces/" + color + pieceName + ".png")));
            ImageView imageView = new ImageView(image);
            imageView.setFitHeight(ChessRender.CHESS_SQUARE_SIZE);
            imageView.setFitWidth(ChessRender.CHESS_SQUARE_SIZE);
            stackPane.getChildren().add(imageView);
            buttons[i].setGraphic(stackPane);
            int finalI = i;
            buttons[i].setOnAction(event -> {
                switch (finalI) {
                    case 1 -> pieceChosen.complete(ChessPieceType.ROOK);
                    case 2 -> pieceChosen.complete(ChessPieceType.BISHOP);
                    case 3 -> pieceChosen.complete(ChessPieceType.KNIGHT);
                    case 4 -> pieceChosen.complete(ChessPieceType.PRINCESS);
                    case 5 -> pieceChosen.complete(ChessPieceType.EMPRESS);
                    case 6 -> pieceChosen.complete(ChessPieceType.NIGHTRIDER);
                    default -> pieceChosen.complete(ChessPieceType.QUEEN);
                }
                stage.close();
            });
        }

        BorderPane root = new BorderPane();
        root.setCenter(gridPane);

        Scene scene;
        if (gameMode.equals("normal"))
            scene = new Scene(root, 2*ChessRender.CHESS_SQUARE_SIZE, 2*ChessRender.CHESS_SQUARE_SIZE);
        else
            scene = new Scene(root, 4*ChessRender.CHESS_SQUARE_SIZE, 2*ChessRender.CHESS_SQUARE_SIZE);
        stage.setResizable(false);
        stage.setTitle("Select promotion piece");
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL); // make the window modal so that the program waits for this window to close before doing the rest
        Image logo = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/assets/logo.png")));
        stage.getIcons().add(logo);

        stage.showAndWait(); // show the window and wait for it to be closed
    }

    public ChessPieceType pieceChosen() throws InterruptedException, ExecutionException {
        return pieceChosen.get(); // wait for the user input and return the result
    }
}
