package chess.dto;

import chess.domain.piece.*;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.function.Function;

public enum PieceDeserializeTable {
    KING("King", King::new),
    QUEEN("Queen", Queen::new),
    BISHOP("Bishop", Bishop::new),
    KNIGHT("Knight", Knight::new),
    ROOK("Rook", Rook::new),
    PAWN("Pawn", Pawn::new);

    private final String name;
    private final Function<Color, Piece> constructorFunction;

    PieceDeserializeTable(String name, Function<Color, Piece> factoryFunction) {
        this.name = name;
        this.constructorFunction = factoryFunction;
    }

    public static Piece deserializeFrom(String string, Color color) {
        return Arrays.stream(values())
                .filter(value -> value.name.equalsIgnoreCase(string))
                .findAny()
                .map(value -> value.constructorFunction.apply(color))
                .orElseThrow(() -> new NoSuchElementException("시스템 에러 - Deserialization이 불가합니다."));
    }
}
