package fr.chess.deluxe.utils;

import fr.chess.deluxe.ChessBoard;

import java.util.Objects;

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

    public Coordinates set(Coordinates coordinates) {
        setX(coordinates.getX());
        setY(coordinates.getY());
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

    public boolean isValid() {
        return 0 <= x && x < ChessBoard.CHESS_SQUARE_LENGTH
                &&  0 <= y && y < ChessBoard.CHESS_SQUARE_LENGTH;
    }

    public Coordinates clone() {
        try {
            return (Coordinates) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean equals(Coordinates coord) {
        return this.getX() == coord.getX() && this.getY() == coord.getY();
    }

    @Override
    public String toString() {
        String xString = String.valueOf((char) ('a' + this.x));
        String yString = String.valueOf(ChessBoard.CHESS_SQUARE_LENGTH - this.y);
        return xString + yString;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordinates that = (Coordinates) o;
        return x == that.x && y == that.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
