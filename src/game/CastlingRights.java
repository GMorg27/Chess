package game;

public enum CastlingRights {
    WHITE_KINGSIDE(0), WHITE_QUEENSIDE(1), BLACK_KINGSIDE(2), BLACK_QUEENSIDE(3);

    private final int value;

    CastlingRights(final int VALUE) {
        this.value = VALUE;
    }

    public int getValue() {
        return this.value;
    }
}
