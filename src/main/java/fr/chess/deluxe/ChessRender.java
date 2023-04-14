package fr.chess.deluxe;

import fr.chess.deluxe.movement.PieceMovementLog;
import fr.chess.deluxe.piece.ChessPieceType;
import fr.chess.deluxe.utils.ChessColor;
import fr.chess.deluxe.utils.Coordinates;
import fr.chess.deluxe.utils.PlayerInformation;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;

public class ChessRender {

    public static final int CHESS_SQUARE_SIZE = 100;

    public static final Color CHESS_SQUARE_COLOR_1 = Color.valueOf("#EFCCA6"); //White
    public static final Color CHESS_SQUARE_COLOR_2 = Color.valueOf("#3C1D18"); //Black
    public static final Color CHESS_BACKGROUND_COLOR =
            new Color(CHESS_SQUARE_COLOR_1.getRed(), CHESS_SQUARE_COLOR_1.getGreen(), CHESS_SQUARE_COLOR_1.getBlue(), 0.5)
                    .interpolate(CHESS_SQUARE_COLOR_2, 0.5); //Mix between Black and White

    public static final Color CHESS_BACKGROUND_PREVIOUS = Color.BLUE;

    public static final Color CHESS_BACKGROUND_CHECK = Color.ORANGE;
    public static final Color CHESS_BACKGROUND_CHECKMATE = Color.RED;
    public static final Color CHESS_BACKGROUND_SELECTED = Color.valueOf("#00ff00"); //Green

    private final Stage stage;
    private ChessBoard chessBoard;
    private final Button[][] renderBoard = new Button[ChessBoard.CHESS_SQUARE_LENGTH][ChessBoard.CHESS_SQUARE_LENGTH];

    public ChessRender(Stage stage, ChessBoard chessBoard) {
        this.stage = stage;
        this.chessBoard = chessBoard;
        loadStage();
    }

    private void loadStage() {
        GridPane chessBoardRender = loadButtons();
        chessBoardRender.setStyle("-fx-background-color: " + getColorHexa(ChessRender.CHESS_BACKGROUND_COLOR));

        BorderPane root = new BorderPane();

        MenuBar menuBar = new MenuBar();

        Menu fileMenu = new Menu("File");
        FileChooser fileChooser = new FileChooser();
        // Configure FileChooser to allow only the specific file extension
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("New Super Chess Deluxe Definition Edition++ (*.chessboardsavefileforthebestgameevercreatednamednewsuperchessdeluxedefinitiveeditionplusplus)", "*.chessboardsavefileforthebestgameevercreatednamednewsuperchessdeluxedefinitiveeditionplusplus");
        fileChooser.getExtensionFilters().clear();
        fileChooser.getExtensionFilters().add(extFilter);

        MenuItem newMenuItem = new MenuItem("New");
        newMenuItem.setOnAction(event -> {
            this.chessBoard = new ChessBoard();
            render();
        });

        MenuItem openMenuItem = new MenuItem("Open");
        openMenuItem.setOnAction(event -> {
            System.out.println("Open menu item clicked");
            // Show open file dialog
            File selectedFile = fileChooser.showOpenDialog(stage);
            if (selectedFile != null) {
                System.out.println("Selected file: " + selectedFile.getAbsolutePath());
                // Add your logic for the Open action here

                // Load the content of the file into a string
                try {
                    String content = new String(Files.readAllBytes(selectedFile.toPath()), StandardCharsets.UTF_8);

                    // Process the content as needed
                    this.chessBoard = ChessMain.GSON.fromJson(content, ChessBoard.class);
                } catch (IOException e) {
                    System.err.println("Error reading from file: " + e.getMessage());
                }
            }
            render();
        });
        MenuItem saveMenuItem = new MenuItem("Save");
        saveMenuItem.setOnAction(event -> {
            System.out.println("Save menu item clicked");
            // Show save file dialog
            File selectedFile = fileChooser.showSaveDialog(stage);
            if (selectedFile != null) {
                System.out.println("Selected file: " + selectedFile.getAbsolutePath());
                // Add your logic for the Save action here

                String chessBoardJson = ChessMain.GSON.toJson(chessBoard);

                // Save the string to the file
                try (FileWriter fileWriter = new FileWriter(selectedFile)) {
                    fileWriter.write(chessBoardJson);
                } catch (IOException e) {
                    System.err.println("Error writing to file: " + e.getMessage());
                }
            }
            render();
        });
        MenuItem exitMenuItem = new MenuItem("Exit");
        exitMenuItem.setOnAction(event -> {
            System.out.println("Exit menu item clicked");
            // Close the application
            stage.close();
        });
        fileMenu.getItems().addAll(newMenuItem, openMenuItem, saveMenuItem, exitMenuItem);

        Menu editMenu = new Menu("Edit");
        MenuItem backMenuItem = new MenuItem("Back");
        backMenuItem.setOnAction(actionEvent -> {
            ChessBoard oldChessBoard = chessBoard;
            PieceMovementLog pieceMovementLog = chessBoard.getLastPieceMovementLog();
            if(pieceMovementLog != null) {
                String chessBoardJson = pieceMovementLog.getChessBoardJson();
                if(chessBoardJson != null) {
                    chessBoard = ChessMain.GSON.fromJson(chessBoardJson, ChessBoard.class);
                    oldChessBoard.getPieceMovementLogs().remove(pieceMovementLog);
                    chessBoard.setPieceMovementLogs(oldChessBoard.getPieceMovementLogs());
                }
            }
            render();
        });
        MenuItem clearLogItem = new MenuItem("Clear Log");
        clearLogItem.setOnAction(actionEvent -> {
            chessBoard.getPieceMovementLogs().clear();
            render();
        });

        editMenu.getItems().addAll(backMenuItem, clearLogItem);

        menuBar.getMenus().addAll(fileMenu, editMenu);

        root.setTop(menuBar);
        root.setCenter(chessBoardRender);

        Scene scene = new Scene(root);
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
                    && chessBoard.getSelectedSquare().getPiece().getPossibleMoves(chessBoard, chessBoard.getSelectedSquare().getCoordinates()).containsKey(coordinates)) {
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
            PieceMovementLog pieceMovementLog = this.getChessBoard().getLastPieceMovementLog();
            if(coordinates.equals(pieceMovementLog.getFromCoordinates()) || coordinates.equals(pieceMovementLog.getToCoordinates()))
                renderColor = chessSquare.getColor().interpolate(ChessRender.CHESS_BACKGROUND_PREVIOUS, 0.5);
        }
        if(chessSquare.hasPiece() && chessSquare.getPiece().getType() == ChessPieceType.KING) {
            Map<ChessColor, PlayerInformation> chessColorPlayerInformationMap = chessBoard.getPlayerInformation();
            if(chessColorPlayerInformationMap.get(chessSquare.getPiece().getPieceColor()).getCheckStatus().equals(PlayerInformation.CheckStatus.CHECKMATE)) {
                renderColor = chessSquare.getColor().interpolate(ChessRender.CHESS_BACKGROUND_CHECKMATE, 0.5);
            } else if(chessColorPlayerInformationMap.get(chessSquare.getPiece().getPieceColor()).getCheckStatus().equals(PlayerInformation.CheckStatus.CHECK))
                renderColor = chessSquare.getColor().interpolate(ChessRender.CHESS_BACKGROUND_CHECK, 0.5);
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
        if(chessBoard.getSelectedSquare() != null && chessBoard.getSelectedSquare().hasPiece() ) {
            if(chessBoard.getSelectedSquare().getPiece().getPossibleMoves(chessBoard, chessBoard.getSelectedSquare().getCoordinates()).containsKey(coordinates)) {
                int circleSize = ChessRender.CHESS_SQUARE_SIZE / 10;
                int innerCircleSize = 0;
                Color circleColor = inverseColor;
                if (chessSquare.hasPiece()) {
                    if (chessSquare.getPiece().getPieceColor() == chessBoard.getCurrentPlayer()) {
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
