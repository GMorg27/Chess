package game;

public class BoardState {
    public static final int RANKS = 8;
    public static final int FILES = 8;

    public Piece[][] pieces = new Piece[RANKS][FILES];
    public PieceColor pieceColor = PieceColor.WHITE;
    public boolean[] castlingRights = { true, true, true, true }; // index using CastlingRights enum
    public Square enPassantSquare = null;
    public int halfmoveClock = 0; // used for fifty-move rule
    public int moveNumber = 1;

    public void loadDefaultPosition() {
        this.pieces[0][0] = new Rook(PieceColor.WHITE);
        this.pieces[0][1] = new Knight(PieceColor.WHITE);
        this.pieces[0][2] = new Bishop(PieceColor.WHITE);
        this.pieces[0][3] = new Queen(PieceColor.WHITE);
        this.pieces[0][4] = new King(PieceColor.WHITE);
        this.pieces[0][5] = new Bishop(PieceColor.WHITE);
        this.pieces[0][6] = new Knight(PieceColor.WHITE);
        this.pieces[0][7] = new Rook(PieceColor.WHITE);

        for (int file = 1; file <= FILES; file++) {
            this.pieces[1][file - 1] = new Pawn(PieceColor.WHITE);
            for (int rank = 3; rank <= 6; rank++)
                this.pieces[rank - 1][file - 1] = null;
            this.pieces[6][file - 1] = new Pawn(PieceColor.BLACK);
        }

        this.pieces[7][0] = new Rook(PieceColor.BLACK);
        this.pieces[7][1] = new Knight(PieceColor.BLACK);
        this.pieces[7][2] = new Bishop(PieceColor.BLACK);
        this.pieces[7][3] = new Queen(PieceColor.BLACK);
        this.pieces[7][4] = new King(PieceColor.BLACK);
        this.pieces[7][5] = new Bishop(PieceColor.BLACK);
        this.pieces[7][6] = new Knight(PieceColor.BLACK);
        this.pieces[7][7] = new Rook(PieceColor.BLACK);
    }
}
