package fr.chess.deluxe;

import fr.chess.deluxe.piece.ChessPiece;
import fr.chess.deluxe.utils.Coordinates;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.List;

public class ChessSquare {

    private final ChessBoard chessBoard;
    private final Coordinates coordinates;
    private ChessPiece piece;

    private final Button button;

    public ChessSquare(ChessBoard chessBoard, Coordinates coordinates, Button button) {
        this.chessBoard = chessBoard;
        this.coordinates = coordinates;
        this.button = button;
    }

    public void setPiece(ChessPiece chessPiece) {
        this.piece = chessPiece;
        renderPiece();
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

    public void renderPiece() {
        Image image = new Image(piece.getId() + ".png");
        ImageView imageView = new ImageView(image);
        button.setGraphic(imageView);
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
        return coordinates;
    }

    @Override
    public String toString() {
        return getCoordinates().toString();
    }
}
