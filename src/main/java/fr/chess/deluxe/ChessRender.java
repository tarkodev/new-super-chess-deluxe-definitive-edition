package fr.chess.deluxe;

import fr.chess.deluxe.movement.PieceMovementLog;
import fr.chess.deluxe.utils.Coordinates;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.List;

public class ChessRender {

    public static final int CHESS_SQUARE_SIZE = 100;

    public static final Color CHESS_SQUARE_COLOR_1 = Color.valueOf("#EFCCA6"); //White
    public static final Color CHESS_SQUARE_COLOR_2 = Color.valueOf("#3C1D18"); //Black
    public static final Color CHESS_BACKGROUND_COLOR =
            new Color(CHESS_SQUARE_COLOR_1.getRed(), CHESS_SQUARE_COLOR_1.getGreen(), CHESS_SQUARE_COLOR_1.getBlue(), 0.5)
                    .interpolate(CHESS_SQUARE_COLOR_2, 0.5); //Mix between Black and White

    public static final Color CHESS_BACKGROUND_PREVIOUS = Color.BLUE;
    public static final Color CHESS_BACKGROUND_SELECTED = Color.valueOf("#00ff00"); //Green

    private final Stage stage;
    private final ChessBoard chessBoard;
    private final Button[][] renderBoard = new Button[ChessBoard.CHESS_SQUARE_LENGTH][ChessBoard.CHESS_SQUARE_LENGTH];

    public ChessRender(Stage stage, ChessBoard chessBoard) {
        this.stage = stage;
        this.chessBoard = chessBoard;
        loadStage();
    }

    private void loadStage() {
        GridPane chessBoardRender = loadButtons();
        chessBoardRender.setStyle("-fx-background-color: " + getColorHexa(ChessRender.CHESS_BACKGROUND_COLOR));

        Scene scene = new Scene(chessBoardRender, CHESS_SQUARE_SIZE * ChessBoard.CHESS_SQUARE_LENGTH, CHESS_SQUARE_SIZE * ChessBoard.CHESS_SQUARE_LENGTH);
        stage.setResizable(false);
        stage.setTitle("New Super Chess Deluxe Definitive Edition++");
        stage.setScene(scene);
        Image logo = new Image("logo.png");
        stage.getIcons().add(logo);

        stage.show();
    }

    private GridPane loadButtons() {
        GridPane chessBoardRender = new GridPane();

        for (int x = 0; x < ChessBoard.CHESS_SQUARE_LENGTH; x++) {
            for (int y = 0; y < ChessBoard.CHESS_SQUARE_LENGTH; y++) {
                Button button = initButton();
                chessBoardRender.add(button, x * CHESS_SQUARE_SIZE, y * CHESS_SQUARE_SIZE);
                renderBoard[x][y] = button;
                addAction(new Coordinates(x, y));
                render(new Coordinates(x, y));
            }
        }

        return chessBoardRender;
    }
    private void addAction(Coordinates coordinates) {
        getButton(coordinates).setOnAction(actionEvent -> {
            ChessSquare clickedSquare = chessBoard.getSquare(coordinates);
            if (chessBoard.getSelectedSquare() != null && chessBoard.getSelectedSquare().hasPiece()
                    && chessBoard.getSelectedSquare().getPiece().getPieceColor() == chessBoard.getCurrentPlayer()
                    && chessBoard.getSelectedSquare().getPiece().getPossibleMoves(chessBoard, chessBoard.getSelectedSquare().getCoordinates()).containsKey(clickedSquare.getCoordinates())) {
                chessBoard.getSelectedSquare().getPiece().getPossibleMoves(chessBoard, chessBoard.getSelectedSquare().getCoordinates()).get(clickedSquare.getCoordinates()).accept(clickedSquare.getCoordinates());
                chessBoard.switchCurrentPlayer();
            } else if(chessBoard.getSelectedSquare() == clickedSquare || !clickedSquare.hasPiece()) {
                chessBoard.setSelectedSquare(null);
            } else if(clickedSquare.hasPiece() && clickedSquare.getPiece().getPieceColor() == chessBoard.getCurrentPlayer())  {
                chessBoard.setSelectedSquare(clickedSquare);
            }
            render();
        });
    }

    private Button initButton() {
        Button button = new Button("");
        button.setPrefWidth(ChessRender.CHESS_SQUARE_SIZE);
        button.setPrefHeight(ChessRender.CHESS_SQUARE_SIZE);
        button.setPadding(new Insets(0));
        return button;
    }

    public static String getColorHexa(Color color) {
        return String.format("#%02X%02X%02X",
                (int)(color.getRed() * 255),
                (int)(color.getGreen() * 255),
                (int)(color.getBlue() * 255));
    }

    public Button getButton(Coordinates coordinates) {
        return renderBoard[coordinates.getX()][coordinates.getY()];
    }

    public ChessBoard getChessBoard() {
        return chessBoard;
    }

    public void render() {
        for (int x = 0; x < ChessBoard.CHESS_SQUARE_LENGTH; x++) {
            for (int y = 0; y < ChessBoard.CHESS_SQUARE_LENGTH; y++) {
                render(new Coordinates(x, y));
            }
        }
    }

    public void render(Coordinates coordinates) {
        ChessSquare chessSquare = chessBoard.getSquare(coordinates);
        Button button = renderBoard[coordinates.getX()][coordinates.getY()];
        Color renderColor = chessSquare.getColor();
        List<PieceMovementLog> pieceMovementLogs = this.getChessBoard().getPieceMovementLogs();
        if(chessSquare.equals(getChessBoard().getSelectedSquare())) {
            renderColor = chessSquare.getColor().interpolate(ChessRender.CHESS_BACKGROUND_SELECTED, 0.5);
        } else if (!pieceMovementLogs.isEmpty()) {
            PieceMovementLog pieceMovementLog = pieceMovementLogs.get(pieceMovementLogs.size()-1);
            if(coordinates.equals(pieceMovementLog.getFromCoordinates()) || coordinates.equals(pieceMovementLog.getToCoordinates()))
                renderColor = chessSquare.getColor().interpolate(ChessRender.CHESS_BACKGROUND_PREVIOUS, 0.5);
        }
        button.setStyle("-fx-background-color: " + getColorHexa(renderColor) + "; -fx-background-radius: 8px;");
        StackPane stackPane = new StackPane();

        if(chessSquare.hasPiece()) {
            Image image = new Image(chessSquare.getPiece().getId() + ".png");
            ImageView imageView = new ImageView(image);
            imageView.setFitHeight(ChessRender.CHESS_SQUARE_SIZE);
            imageView.setFitWidth(ChessRender.CHESS_SQUARE_SIZE);
            stackPane.getChildren().add(imageView);
        }

        Color inverseColor = chessSquare.getColor() == ChessRender.CHESS_SQUARE_COLOR_1 ? ChessRender.CHESS_SQUARE_COLOR_2 : ChessRender.CHESS_SQUARE_COLOR_1;
        if(chessBoard.getSelectedSquare() != null && chessBoard.getSelectedSquare().hasPiece() && chessBoard.getSelectedSquare().getPiece().getPossibleMoves(chessBoard, chessBoard.getSelectedSquare().getCoordinates()).containsKey(coordinates)) {
            int circleSize = ChessRender.CHESS_SQUARE_SIZE / 10;
            int innerCircleSize = 0;
            Color circleColor = inverseColor;
            if(chessSquare.hasPiece()) {
                if(chessSquare.getPiece().getPieceColor() == chessBoard.getCurrentPlayer()) {
                    circleSize = ChessRender.CHESS_SQUARE_SIZE * 15 / 100;
                    circleColor = Color.PURPLE;
                } else {
                    circleColor = Color.RED;
                    circleSize = ChessRender.CHESS_SQUARE_SIZE * 49 / 100;
                    innerCircleSize = ChessRender.CHESS_SQUARE_SIZE * 45 / 100;
                }

            }
            Circle circle = new Circle(circleSize);
            Circle innerCircle = new Circle(innerCircleSize, Color.TRANSPARENT);
            Shape donut = Shape.subtract(circle, innerCircle);
            donut.setFill(circleColor);
            stackPane.getChildren().add(donut);
        }


        if (coordinates.getY()==ChessBoard.CHESS_SQUARE_LENGTH-1) {
            Text letter = new Text(String.valueOf(coordinates.toString().charAt(0)));
            letter.setFont(Font.font("Comic Sans MS", 20));
            letter.setTranslateX(-43);
            letter.setTranslateY(35);
            letter.setFill(inverseColor);
            stackPane.getChildren().add(letter);
        }

        if (coordinates.getX()==ChessBoard.CHESS_SQUARE_LENGTH-1) {
            Text number = new Text(String.valueOf(coordinates.toString().charAt(1)));
            number.setFont(Font.font("Comic Sans MS", 20));
            number.setTranslateX(40);
            number.setTranslateY(-40);
            number.setFill(inverseColor);
            stackPane.getChildren().add(number);
        }

        button.setGraphic(stackPane);
    }


}
