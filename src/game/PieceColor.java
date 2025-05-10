package game;

public enum PieceColor {
    WHITE('w'), BLACK('b');

    private final char letter;

    PieceColor(final char LETTER) {
        this.letter = LETTER;
    }

    public int getLetter() {
        return this.letter;
    }
}
