package game;

public class BoardState {
    public static final int RANKS = 8;
    public static final int FILES = 8;
    public static final Square[] CASTLING_SQUARES = { new Square(1, 7), new Square(1, 3),
            new Square(8, 7), new Square(8, 3) }; // index using CastlingRights enum

    public Piece[][] pieces = new Piece[RANKS][FILES];
    public PieceColor turnColor = PieceColor.WHITE;
    public boolean[] castlingRights = { true, true, true, true }; // index using CastlingRights enum
    public Square[] rookStartingPositions; // index using CastlingRights enum
    public Square enPassantSquare = null;
    public int halfmoveClock = 0; // used for fifty-move rule
    public int moveNumber = 1;

    public void makeMove(Square origin, Square destination) {
        Piece piece = this.getPiece(origin);
        this.setPiece(piece, destination);
        this.setPiece(null, origin);

        if (piece instanceof Pawn) {
            ((Pawn) piece).move();

            if (destination.equals(this.enPassantSquare)) {
                if (this.turnColor == PieceColor.WHITE)
                    this.setPiece(null, this.enPassantSquare.getRank() - 1, this.enPassantSquare.getFile());
                else
                    this.setPiece(null, this.enPassantSquare.getRank() + 1, this.enPassantSquare.getFile());
            }

            if (destination.getRank() == origin.getRank() + 2)
                this.enPassantSquare = new Square(origin.getRank() + 1, origin.getFile());
            else if (destination.getRank() == origin.getRank() - 2)
                this.enPassantSquare = new Square(origin.getRank() - 1, origin.getFile());
            else this.enPassantSquare = null;
        }
        else this.enPassantSquare = null;

        if (piece instanceof King) {
            if (piece.getColor() == PieceColor.WHITE) {
                if (destination.equals(CASTLING_SQUARES[CastlingRights.WHITE_KINGSIDE.getValue()]) ||
                        destination.equals(this.rookStartingPositions[CastlingRights.WHITE_KINGSIDE.getValue()]) &&
                                this.castlingRights[CastlingRights.WHITE_KINGSIDE.getValue()]) {
                    Square castlingSquare = CASTLING_SQUARES[CastlingRights.WHITE_KINGSIDE.getValue()];
                    this.setPiece(null, destination);
                    this.setPiece(piece, castlingSquare);
                    this.setPiece(null, this.rookStartingPositions[CastlingRights.WHITE_KINGSIDE.getValue()]);
                    this.setPiece(new Rook(PieceColor.WHITE), castlingSquare.getRank(),
                            castlingSquare.getFile() - 1);
                }
                else if (destination.equals(CASTLING_SQUARES[CastlingRights.WHITE_QUEENSIDE.getValue()]) ||
                        destination.equals(this.rookStartingPositions[CastlingRights.WHITE_QUEENSIDE.getValue()]) &&
                                this.castlingRights[CastlingRights.WHITE_QUEENSIDE.getValue()]) {
                    Square castlingSquare = CASTLING_SQUARES[CastlingRights.WHITE_QUEENSIDE.getValue()];
                    this.setPiece(null, destination);
                    this.setPiece(piece, castlingSquare);
                    this.setPiece(null, this.rookStartingPositions[CastlingRights.WHITE_QUEENSIDE.getValue()]);
                    this.setPiece(new Rook(PieceColor.WHITE), castlingSquare.getRank(),
                            castlingSquare.getFile() + 1);
                }

                this.castlingRights[CastlingRights.WHITE_KINGSIDE.getValue()] = false;
                this.castlingRights[CastlingRights.WHITE_QUEENSIDE.getValue()] = false;
            }
            else {
                if (destination.equals(CASTLING_SQUARES[CastlingRights.BLACK_KINGSIDE.getValue()]) ||
                        destination.equals(this.rookStartingPositions[CastlingRights.BLACK_KINGSIDE.getValue()]) &&
                                this.castlingRights[CastlingRights.BLACK_KINGSIDE.getValue()]) {
                    Square castlingSquare = CASTLING_SQUARES[CastlingRights.BLACK_KINGSIDE.getValue()];
                    this.setPiece(null, destination);
                    this.setPiece(piece, castlingSquare);
                    this.setPiece(null, this.rookStartingPositions[CastlingRights.BLACK_KINGSIDE.getValue()]);
                    this.setPiece(new Rook(PieceColor.BLACK), castlingSquare.getRank(),
                            castlingSquare.getFile() - 1);
                }
                else if (destination.equals(CASTLING_SQUARES[CastlingRights.BLACK_QUEENSIDE.getValue()]) ||
                        destination.equals(this.rookStartingPositions[CastlingRights.BLACK_QUEENSIDE.getValue()]) &&
                                this.castlingRights[CastlingRights.BLACK_QUEENSIDE.getValue()]) {
                    Square castlingSquare = CASTLING_SQUARES[CastlingRights.BLACK_QUEENSIDE.getValue()];
                    this.setPiece(null, destination);
                    this.setPiece(piece, castlingSquare);
                    this.setPiece(null, this.rookStartingPositions[CastlingRights.BLACK_QUEENSIDE.getValue()]);
                    this.setPiece(new Rook(PieceColor.BLACK), castlingSquare.getRank(),
                            castlingSquare.getFile() + 1);
                }

                this.castlingRights[CastlingRights.BLACK_KINGSIDE.getValue()] = false;
                this.castlingRights[CastlingRights.BLACK_QUEENSIDE.getValue()] = false;
            }
        }

        if (piece instanceof Rook) {
            for (int i = 0; i < this.rookStartingPositions.length; i++) {
                if (origin.equals(this.rookStartingPositions[i]))
                    this.castlingRights[i] = false;
            }
        }

        this.turnColor = turnColor == PieceColor.WHITE ? PieceColor.BLACK : PieceColor.WHITE;
        if (this.turnColor == PieceColor.WHITE) moveNumber++;
    }

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

        this.rookStartingPositions = new Square[]{ new Square(1, 8), new Square(1, 1),
                new Square(8, 8), new Square(8, 1) };
    }

    public Piece getPiece(final int RANK, final int FILE) {
        if (RANK < 1 || RANK > BoardState.RANKS)
            throw new RuntimeException("Rank " + RANK + " out of range");
        if (FILE < 1 || FILE > BoardState.FILES)
            throw new RuntimeException("File " + FILE + " out of range");

        return this.pieces[RANK-1][FILE-1];
    }

    public Piece getPiece(final Square SQUARE) { return this.getPiece(SQUARE.getRank(), SQUARE.getFile()); }

    public void setPiece(Piece piece, final int RANK, final int FILE) {
        if (RANK < 1 || RANK > BoardState.RANKS)
            throw new RuntimeException("Rank " + RANK + " out of range");
        if (FILE < 1 || FILE > BoardState.FILES)
            throw new RuntimeException("File " + FILE + " out of range");

        this.pieces[RANK-1][FILE-1] = piece;
    }

    public void setPiece(Piece piece, final Square SQUARE) { this.setPiece(piece, SQUARE.getRank(), SQUARE.getFile()); }
}
