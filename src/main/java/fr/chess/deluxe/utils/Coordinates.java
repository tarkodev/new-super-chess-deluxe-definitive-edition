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
        System.out.println(this);
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

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    @Override
    public String toString() {
        String xString = String.valueOf((char) ('a' + this.x));
        String yString = String.valueOf(ChessBoard.CHESS_SQUARE_LENGTH - this.y);
        return xString + yString;
    }
}
