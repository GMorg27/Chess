package game;

public class Square {
    private int rank;
    private int file;

    public Square(final int RANK, final int FILE) { this.setPosition(RANK, FILE); }

    public void setPosition(final int RANK, final int FILE) {
        if (RANK < 1 || RANK > BoardState.RANKS)
            throw new RuntimeException("Rank " + RANK + " out of range");
        if (FILE < 1 || FILE > BoardState.FILES)
            throw new RuntimeException("File " + FILE + " out of range");

        this.rank = RANK;
        this.file = FILE;
    }

    public int getRank() { return this.rank; }

    public int getFile() { return this.file; }

    public char getLetter() { return (char) ('a' + this.file - 1); }

    public boolean isDark() { return (this.rank % 2 == this.file % 2); }

    @Override
    public String toString() { return Character.toString(this.getLetter()) + Integer.toString(this.rank); }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Square)
            return ((Square) other).rank == this.rank && ((Square) other).file == this.file;
        return false;
    }

    @Override
    public int hashCode() {
        return 31 * rank + file;
    }
}
