package game;

public class Queen extends Piece {
    public Queen(final PieceColor COLOR) { super("queen", 'Q', 9, COLOR); }

    @Override
    public boolean validateMove(final Square FROM, final Square TO, final BoardState STATE) { return true; }
}
