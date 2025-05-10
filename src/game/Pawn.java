package game;

public class Pawn extends Piece {
    private boolean hasMoved = false;

    public Pawn(final PieceColor COLOR) { super("pawn", 'P', 1, COLOR); }

    @Override
    public boolean validateMove(final Square FROM, final Square TO, final BoardState STATE) { return true; }
}
