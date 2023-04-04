package fr.chess.deluxe;

import fr.chess.deluxe.movement.PieceMovementLog;
import fr.chess.deluxe.piece.ChessPiece;
import fr.chess.deluxe.utils.Coordinates;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.*;
import java.util.function.Consumer;

public class ChessSquare {

    private final ChessBoard chessBoard;
    private final Coordinates coordinates;
    private ChessPiece piece;

    private final Color color;

    private final Button button;

    public ChessSquare(ChessBoard chessBoard, Color color, Coordinates coordinates, Button button) {
        this.chessBoard = chessBoard;
        this.color = color;
        this.coordinates = coordinates;
        this.button = button;
    }

    public void setPiece(ChessPiece chessPiece) {
        this.piece = chessPiece;
    }

    public void removePiece() {
        this.piece = null;
        ImageView imageView = new ImageView((Image) null);
        button.setGraphic(imageView);
    }

    public boolean hasPiece() {
        return this.piece != null;
    }

    public ChessPiece getPiece() {
        return piece;
    }

    public void render() {
        Color renderColor = color;
        List<PieceMovementLog> pieceMovementLogs = this.getChessBoard().getPieceMovementLogs();
        if(this.equals(getChessBoard().getSelectedSquare())) {
            renderColor = color.interpolate(ChessBoard.CHESS_BACKGROUND_SELECTED, 0.5);
        } else if (!pieceMovementLogs.isEmpty()) {
            PieceMovementLog pieceMovementLog = pieceMovementLogs.get(pieceMovementLogs.size()-1);
            if(this.coordinates.equals(pieceMovementLog.getFromCoordinates()) || this.coordinates.equals(pieceMovementLog.getToCoordinates()))
                renderColor = color.interpolate(ChessBoard.CHESS_BACKGROUND_PREVIOUS, 0.5);
        }
        button.setStyle("-fx-background-color: " + ChessBoard.getColorHexa(renderColor) + "; -fx-background-radius: 8px;");
        StackPane stackPane = new StackPane();

        if(piece != null) {
            Image image = new Image(piece.getId() + ".png");
            ImageView imageView = new ImageView(image);
            imageView.setFitHeight(ChessBoard.CHESS_SQUARE_SIZE);
            imageView.setFitWidth(ChessBoard.CHESS_SQUARE_SIZE);
            stackPane.getChildren().add(imageView);
        }

        Color inverseColor = color == ChessBoard.CHESS_SQUARE_COLOR_1 ? ChessBoard.CHESS_SQUARE_COLOR_2 : ChessBoard.CHESS_SQUARE_COLOR_1;
        if(chessBoard.getSelectedSquare() != null && chessBoard.getSelectedSquare().getPossibleMoves().containsKey(this.getCoordinates())) {
            int circleSize = ChessBoard.CHESS_SQUARE_SIZE / 10;
            int innerCircleSize = 0;
            Color circleColor = inverseColor;
            if(piece != null) {
                if(piece.getPieceColor() == chessBoard.getCurrentPlayer()) {
                    circleSize = ChessBoard.CHESS_SQUARE_SIZE * 15 / 100;
                    circleColor = Color.PURPLE;
                } else {
                    circleColor = Color.RED;
                    circleSize = ChessBoard.CHESS_SQUARE_SIZE * 49 / 100;
                    innerCircleSize = ChessBoard.CHESS_SQUARE_SIZE * 45 / 100;
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



    public Map<Coordinates, Consumer<Coordinates>> getPossibleMoves() {
        return hasPiece() ? piece.getPossibleMoves(chessBoard, coordinates) : new HashMap<>();
    }


    public Button getButton() {
        return button;
    }

    public ChessBoard getChessBoard() {
        return chessBoard;
    }

    public Coordinates getCoordinates() {
        return coordinates.clone();
    }

    @Override
    public String toString() {
        return coordinates.toString();
    }
}
