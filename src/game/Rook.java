package game;

public class Rook extends Piece {
    public Rook(final PieceColor COLOR) { super("rook", 'R', 5, COLOR); }

    @Override
    public boolean validateMove(final Square FROM, final Square TO, final BoardState STATE) {
        return this.validateRookMovement(FROM, TO, STATE);
    }
}
