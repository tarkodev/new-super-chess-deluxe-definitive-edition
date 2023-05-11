package fr.chess.deluxe.utils;

import com.google.gson.*;
import fr.chess.deluxe.ChessBoard;
import fr.chess.deluxe.ChessSquare;

import java.lang.reflect.Type;

/**
 * S'occupe de la lecture et de l'écriture d'un échiquier depuis ou vers le JSON
 */
public class ChessSquareTypeAdapter implements JsonSerializer<ChessSquare[][]>, JsonDeserializer<ChessSquare[][]> {

    @Override
    public JsonElement serialize(ChessSquare[][] chessSquares, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject jsonObject = new JsonObject();

        for (int x = 0; x < ChessBoard.CHESS_SQUARE_LENGTH; x++) {
            for (int y = 0; y < ChessBoard.CHESS_SQUARE_LENGTH; y++) {
                ChessSquare chessSquare = chessSquares[x][y];
                if (chessSquare.getPiece() != null) {
                    JsonObject jsonSquareObject = jsonSerializationContext.serialize(chessSquare).getAsJsonObject();
                    jsonSquareObject.remove("coordinates");
                    jsonObject.add(chessSquare.getCoordinates().toString(), jsonSquareObject);
                }
            }
        }

        return jsonObject;
    }

    @Override
    public ChessSquare[][] deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        ChessSquare[][] chessSquares = new ChessSquare[ChessBoard.CHESS_SQUARE_LENGTH][ChessBoard.CHESS_SQUARE_LENGTH];

        for (int x = 0; x < ChessBoard.CHESS_SQUARE_LENGTH; x++) {
            for (int y = 0; y < ChessBoard.CHESS_SQUARE_LENGTH; y++) {
                Coordinates coordinates = new Coordinates(x, y);
                String coordinatesString = coordinates.toString();
                if (jsonObject.has(coordinatesString)) {
                    JsonElement pieceJsonElement = jsonObject.get(coordinatesString);
                    ChessSquare chessSquare = jsonDeserializationContext.deserialize(pieceJsonElement, ChessSquare.class);
                    chessSquares[x][y] = new ChessSquare(chessSquare, coordinates);
                } else {
                    chessSquares[x][y] = new ChessSquare(coordinates);
                }
            }
        }

        return chessSquares;
    }
}
