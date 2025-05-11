package game;

public class Knight extends Piece {
    public Knight(final PieceColor COLOR) { super("knight", 'N', 3, COLOR); }

    @Override
    public boolean validateMove(final Square FROM, final Square TO, final BoardState STATE) {
        int rankDifference = TO.getRank() - FROM.getRank();
        int fileDifference = TO.getFile() - FROM.getFile();
        return (Math.abs(rankDifference) == 1 && Math.abs(fileDifference) == 2 ||
                Math.abs(rankDifference) == 2 && Math.abs(fileDifference) == 1);
    }
}
