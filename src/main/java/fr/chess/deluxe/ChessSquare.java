package fr.chess.deluxe;

import fr.chess.deluxe.piece.ChessPiece;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.List;

public class ChessSquare {

    private final ChessBoard chessBoard;
    private final int x, y;
    private ChessPiece piece;

    private final Button button;

    public ChessSquare(ChessBoard chessBoard, int x, int y, Button button) {
        this.chessBoard = chessBoard;
        this.x = x;
        this.y = y;
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

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }




    public ChessBoard getChessBoard() {
        return chessBoard;
    }

    @Override
    public String toString() {
        return chessBoard.convertToString(x, y);
    }
}
