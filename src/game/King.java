package game;

public class King extends Piece {
    public King(final PieceColor COLOR) { super("king", 'K', 0, COLOR); }

    @Override
    public boolean validateMove(final Square FROM, final Square TO, final BoardState STATE) { return true; }
}
