package fr.chess.deluxe.utils;

import fr.chess.deluxe.ChessBoard;

public class Coordinates implements Cloneable {

    private int x;
    private int y;

    public Coordinates(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Coordinates(String coordinates) {
        this.x = getX(coordinates);
        this.y = getY(coordinates);
    }

    private int getX(String coordinates) {
        return coordinates.charAt(0) - 'a';
    }

    private int getY(String coordinates) {
        return ChessBoard.CHESS_SQUARE_LENGTH - 1 - (coordinates.charAt(1) - '1');
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Coordinates setX(int x) {
        this.x = x;
        return this;
    }

    public Coordinates setY(int y) {
        this.y = y;
        return this;
    }

    public Coordinates addX(int x) {
        this.x += x;
        return this;
    }

    public Coordinates addY(int y) {
        this.y += y;
        return this;
    }

    public Coordinates add(int x, int y) {
        addX(x);
        addY(y);
        return this;
    }

    public Coordinates clone() {
        try {
            return (Coordinates) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        String xString = String.valueOf((char) ('a' + this.x));
        String yString = String.valueOf(ChessBoard.CHESS_SQUARE_LENGTH - this.y);
        return xString + yString;
    }

}
