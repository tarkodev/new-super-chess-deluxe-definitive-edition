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

public class ChessSquare implements Cloneable {

    private ChessBoard chessBoard;
    private Coordinates coordinates;
    private ChessPiece piece;

    private Color color;

    public Color getColor() {
        return color;
    }

    public ChessSquare(ChessBoard chessBoard, Color color, Coordinates coordinates) {
        this.chessBoard = chessBoard;
        this.color = color;
        this.coordinates = coordinates;
    }

    public void setPiece(ChessPiece chessPiece) {
        this.piece = chessPiece;
    }

    public void removePiece() {
        this.piece = null;
    }

    public boolean hasPiece() {
        return this.piece != null;
    }

    public ChessPiece getPiece() {
        return piece;
    }

    public ChessBoard getChessBoard() {
        return chessBoard;
    }

    public void setChessBoard(ChessBoard chessBoard) {
        this.chessBoard = chessBoard;
    }

    public Coordinates getCoordinates() {
        return coordinates.clone();
    }

    @Override
    public String toString() {
        return coordinates.toString();
    }

    @Override
    public ChessSquare clone() {
        try {
            ChessSquare clone = (ChessSquare) super.clone();


            // Copier coordinates
            clone.coordinates = coordinates.clone();

            // Copier piece (nécessite que les sous-classes de ChessPiece aient des méthodes clone() correctes)
            if (piece != null) {
                clone.piece = piece.clone();
            }

            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
