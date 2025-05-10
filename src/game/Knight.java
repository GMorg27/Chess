package game;

public class Knight extends Piece {
    public Knight(final PieceColor COLOR) { super("knight", 'N', 3, COLOR); }

    @Override
    public boolean validateMove(final Square FROM, final Square TO, final BoardState STATE) { return true; }
}
