package fr.chess.deluxe;

import fr.chess.deluxe.piece.ChessPiece;
import fr.chess.deluxe.utils.Coordinates;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;

import java.util.ArrayList;
import java.util.List;

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
        if(this.equals(getChessBoard().getSelectedSquare())) {
            renderColor = color.interpolate(ChessBoard.CHESS_BACKGROUND_SELECTED, 0.5);
        }
        button.setStyle("-fx-background-color: " + ChessBoard.getColorHexa(renderColor) + "; -fx-background-radius: 8px;");
        StackPane stackPane = new StackPane();

        if(piece != null) {
            Image image = new Image(piece.getId() + ".png");
            ImageView imageView = new ImageView(image);
            stackPane.getChildren().add(imageView);
        }

        if(chessBoard.getSelectedSquare() != null &&chessBoard.getSelectedSquare().getPossibleMoves().contains(this)) {
            int circleSize = 10;
            int innerCircleSize = 0;
            Color circleColor = color == ChessBoard.CHESS_SQUARE_COLOR_1 ? ChessBoard.CHESS_SQUARE_COLOR_2 : ChessBoard.CHESS_SQUARE_COLOR_1;
            if(piece != null && piece.getPieceColor() != chessBoard.getCurrentPlayer()) {
                circleColor = Color.RED;
                circleSize = 49;
                innerCircleSize = 45;
            }
            Circle circle = new Circle(circleSize);
            Circle innerCircle = new Circle(innerCircleSize, Color.TRANSPARENT);
            Shape donut = Shape.subtract(circle, innerCircle);
            donut.setFill(circleColor);
            stackPane.getChildren().add(donut);
        }



        button.setGraphic(stackPane);
    }

    public List<ChessSquare> getPossibleMoves() {
        List<ChessSquare> result = new ArrayList<>();
        piece.getMovements().forEach(movement -> result.addAll(movement.getPossibleMoves(this)));
        return result;
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
