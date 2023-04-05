package fr.chess.deluxe;

import fr.chess.deluxe.movement.PieceMovementLog;
import fr.chess.deluxe.piece.*;
import fr.chess.deluxe.utils.ChessColor;
import fr.chess.deluxe.utils.Coordinates;
import javafx.scene.paint.Color;

import java.util.*;

public class ChessBoard implements Cloneable{

    public static final int CHESS_SQUARE_LENGTH = 8;

    private final ChessSquare[][] squareBoard = new ChessSquare[CHESS_SQUARE_LENGTH][CHESS_SQUARE_LENGTH];
    private final List<PieceMovementLog> pieceMovementLogs = new ArrayList<>();

    private ChessColor currentPlayer = ChessColor.WHITE;
    private ChessSquare selectedSquare = null;

    public List<PieceMovementLog> getPieceMovementLogs() {
        return pieceMovementLogs;
    }

    public PieceMovementLog getLastPieceMovementLog() {
        return pieceMovementLogs.isEmpty() ? null : pieceMovementLogs.get(pieceMovementLogs.size()-1);
    }

    public ChessSquare getSelectedSquare() {
        return selectedSquare;
    }

    public void setSelectedSquare(ChessSquare selectedSquare) {
        this.selectedSquare = selectedSquare;
    }

    public ChessSquare getSquare(Coordinates coordinates) {
        return coordinates.isValid() ? this.squareBoard[coordinates.getX()][coordinates.getY()] : null;
    }

    public ChessColor getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(ChessColor currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public void switchCurrentPlayer() {
        setCurrentPlayer(getCurrentPlayer() == ChessColor.WHITE ? ChessColor.BLACK : ChessColor.WHITE);
        selectedSquare = null;
    }

    public ChessBoard() {
        initChessBoardRender();
        loadPieces();
    }




    public void render() {
        for (int x = 0; x < CHESS_SQUARE_LENGTH; x++) {
            for (int y = 0; y < CHESS_SQUARE_LENGTH; y++) {
                //board[x][y].render();
            }
        }
    }

    private void initChessBoardRender() {
        for (int x = 0; x < CHESS_SQUARE_LENGTH; x++) {
            for (int y = 0; y < CHESS_SQUARE_LENGTH; y++) {
                Color color = ((x + y) % 2) == 0 ? ChessRender.CHESS_SQUARE_COLOR_1 : ChessRender.CHESS_SQUARE_COLOR_2;
                ChessSquare chessSquare = new ChessSquare(this, color, new Coordinates(x, y));
                squareBoard[x][y] = chessSquare;
            }
        }
    }

    public void loadPieces() {
        // White
        setPiece(new ChessPieceRook(ChessColor.WHITE), new Coordinates("a1"));
        setPiece(new ChessPieceRook(ChessColor.WHITE), new Coordinates("h1"));
        setPiece(new ChessPieceKnight(ChessColor.WHITE), new Coordinates("b1"));
        setPiece(new ChessPieceKnight(ChessColor.WHITE), new Coordinates("g1"));
        setPiece(new ChessPieceBishop(ChessColor.WHITE), new Coordinates("c1"));
        setPiece(new ChessPieceBishop(ChessColor.WHITE), new Coordinates("f1"));
        setPiece(new ChessPieceQueen(ChessColor.WHITE), new Coordinates("d1"));
        setPiece(new ChessPieceKing(ChessColor.WHITE), new Coordinates("e1"));
        Coordinates firstPawnWhite = new Coordinates("a2");
        for (int i = 0; i < CHESS_SQUARE_LENGTH; i++) {
            setPiece(new ChessPiecePawn(ChessColor.WHITE), firstPawnWhite);
            firstPawnWhite.setX(firstPawnWhite.getX()+1);
        }

        // Black
        setPiece(new ChessPieceRook(ChessColor.BLACK), new Coordinates("a8"));
        setPiece(new ChessPieceRook(ChessColor.BLACK), new Coordinates("h8"));
        setPiece(new ChessPieceKnight(ChessColor.BLACK), new Coordinates("b8"));
        setPiece(new ChessPieceKnight(ChessColor.BLACK), new Coordinates("g8"));
        setPiece(new ChessPieceBishop(ChessColor.BLACK), new Coordinates("c8"));
        setPiece(new ChessPieceBishop(ChessColor.BLACK), new Coordinates("f8"));
        setPiece(new ChessPieceQueen(ChessColor.BLACK), new Coordinates("d8"));
        setPiece(new ChessPieceKing(ChessColor.BLACK), new Coordinates("e8"));
        Coordinates firstPawnBlack = new Coordinates("a7");
        for (int i = 0; i < CHESS_SQUARE_LENGTH; i++) {
            setPiece(new ChessPiecePawn(ChessColor.BLACK), firstPawnBlack);
            firstPawnBlack.setX(firstPawnBlack.getX()+1);
        }
    }

    public Set<Coordinates> getPossibleMoves(ChessColor player, Coordinates from, Coordinates to) {
        Set<Coordinates> result = new HashSet<>();
        for (int x = 0; x < CHESS_SQUARE_LENGTH; x++) {
            for (int y = 0; y < CHESS_SQUARE_LENGTH; y++) {
                Coordinates coordinates = new Coordinates(x, y);
                System.out.println(coordinates);
                ChessSquare chessSquare = getSquare(coordinates);

                if(chessSquare.hasPiece() && chessSquare.getPiece().getPieceColor() == player && !coordinates.equals(from) && !coordinates.equals(to)){}
                    result.addAll(chessSquare.getPiece().getPossibleMoves(chessSquare.getChessBoard(), coordinates).keySet());
                //if(coordinates.equals(to))
                  //  result.addAll(getSquare(from).getPiece().getPossibleMoves(this, to).keySet());
            }
        }
        return result;
    }

    public void setPiece(ChessPiece piece, Coordinates coordinates) {
        getSquare(coordinates).setPiece(piece);
    }

    @Override
    public ChessBoard clone() {
        try {
            ChessBoard clone = (ChessBoard) super.clone();
            // TODO: copy mutable state here, so the clone can't change the internals of the original
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
