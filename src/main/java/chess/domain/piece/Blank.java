package chess.domain.piece;

import chess.domain.exception.NullObjectSelectionException;
import chess.domain.order.MoveRoute;

public class Blank extends Piece {
    private static final String BLANK_NAME = ".";

    public Blank() {
        super(BLANK_NAME);
    }

    @Override
    public boolean canMove(MoveRoute moveRoute) {
        throw new NullObjectSelectionException("이동 명령을 수행할 수 없습니다. - 기물이 없습니다.");
    }

    @Override
    public Color getColor() {
        throw new NullObjectSelectionException("색상 속성을 읽을 수 없습니다. - 기물이 없습니다.");
    }

    @Override
    public boolean isSameColor(Color color) {
        throw new NullObjectSelectionException("색상을 비교할 수 없습니다. - 기물이 없습니다.");
    }

    @Override
    public boolean isSameColor(Piece piece) {
        throw new NullObjectSelectionException("색상을 비교할 수 없습니다. - 기물이 없습니다.");
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Blank) {
            return true;
        }
        return false;
    }
}
