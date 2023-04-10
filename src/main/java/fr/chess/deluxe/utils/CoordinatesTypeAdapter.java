package fr.chess.deluxe.utils;

import com.google.gson.*;

import java.lang.reflect.Type;

public class CoordinatesTypeAdapter implements JsonSerializer<Coordinates>, JsonDeserializer<Coordinates> {

    @Override
    public JsonElement serialize(Coordinates coordinates, Type type, JsonSerializationContext jsonSerializationContext) {
        return new JsonPrimitive(coordinates.toString());
    }

    @Override
    public Coordinates deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return new Coordinates(jsonElement.getAsString());
    }
}
