package game;

public class Bishop extends Piece {
    public Bishop(final PieceColor COLOR) { super("bishop", 'B', 3, COLOR); }

    @Override
    public boolean validateMove(final Square FROM, final Square TO, final BoardState STATE) { return true; }
}
