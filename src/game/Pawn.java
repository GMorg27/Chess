package game;

public class Pawn extends Piece {
    private boolean hasMoved = false;

    public Pawn(final PieceColor COLOR) { super("pawn", 'P', 1, COLOR); }

    @Override
    public boolean validateMove(final Square FROM, final Square TO, final BoardState STATE) {
        Piece capturedPiece = STATE.getPiece(TO);
        if (capturedPiece == null) {
            if (TO.getFile() == FROM.getFile()) {
                if (this.color == PieceColor.WHITE) {
                    if (TO.getRank() == FROM.getRank() + 1) return true;
                    else if (!this.hasMoved && TO.getRank() == FROM.getRank() + 2)
                        return STATE.getPiece(FROM.getRank() + 1, FROM.getFile()) == null;
                }
                else {
                    if (TO.getRank() == FROM.getRank() - 1) return true;
                    else if (!this.hasMoved && TO.getRank() == FROM.getRank() - 2)
                        return STATE.getPiece(FROM.getRank() - 1, FROM.getFile()) == null;
                }
            }
        }
        if (capturedPiece != null || TO.equals(STATE.enPassantSquare)) {
            if (TO.getFile() == FROM.getFile() - 1 || TO.getFile() == FROM.getFile() + 1) {
                if (this.color == PieceColor.WHITE) return TO.getRank() == FROM.getRank() + 1;
                else return TO.getRank() == FROM.getRank() - 1;
            }
        }

        return false;
    }

    public void move() { this.hasMoved = true; }
}
