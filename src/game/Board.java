package game;

import gui.BoardTheme;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Board {
    private BoardState state = new BoardState();
    private Square clickedOrigin = null;
    private Square lastOrigin = null;
    private Square lastDestination = null;
    private Map<Square, Set<Square>> validMoves = new HashMap<Square, Set<Square>>();

    public Board() { this.state.loadDefaultPosition(); }

    public void draw(Graphics g, final int SQUARE_SIZE, final BoardTheme THEME) {
        this.drawSquares(g, SQUARE_SIZE, THEME);
        this.drawCoordinates(g, SQUARE_SIZE, THEME);
        this.drawHighlights(g, SQUARE_SIZE, THEME);
        this.drawPieces(g, SQUARE_SIZE, THEME);
    }

    public void handleClick(MouseEvent me, final int SQUARE_SIZE) {
        Square square = this.calculateClickedSquare(me.getX(), me.getY(), SQUARE_SIZE);
        if (square == null) {
            this.clickedOrigin = null;
            return;
        }

        // left click
        if (me.getButton() == MouseEvent.BUTTON1) {
            Piece clickedPiece = this.state.getPiece(square);
            if (this.clickedOrigin == null) {
                if (clickedPiece != null && clickedPiece.getColor() == this.state.turnColor) {
                    this.clickedOrigin = square;
                    this.addValidMoves(clickedPiece);
                }
            }
            else if (this.clickedOrigin.equals(square))
                this.clickedOrigin = null;
            else if (this.validMoves.get(this.clickedOrigin).contains(square))
                this.makeMove(this.clickedOrigin, square);
            else if (clickedPiece != null && clickedPiece.getColor() == this.state.turnColor) {
                this.clickedOrigin = square;
                this.addValidMoves(clickedPiece);
            }
            else this.clickedOrigin = null;
        }

        // right click
        if (me.getButton() == MouseEvent.BUTTON3)
            this.clickedOrigin = null;
    }

    private Square calculateClickedSquare(final int MOUSE_X, final int MOUSE_Y, final int SQUARE_SIZE) {
        int rank = BoardState.RANKS - MOUSE_Y / SQUARE_SIZE;
        int file = MOUSE_X / SQUARE_SIZE + 1;
        if (this.state.turnColor == PieceColor.BLACK) {
            rank = MOUSE_Y / SQUARE_SIZE + 1;
            file = BoardState.FILES - MOUSE_X / SQUARE_SIZE;
        }

        if (rank >= 1 && rank <= BoardState.RANKS && file >= 1 && file <= BoardState.FILES)
            return new Square(rank, file);
        return null;
    }

    private void makeMove(Square origin, Square destination) {
        this.state.makeMove(origin, destination);
        this.clickedOrigin = null;
        this.lastOrigin = origin;
        this.lastDestination = destination;

        this.validMoves.clear();
    }

    private void addValidMoves(Piece piece) {
        if (this.validMoves.containsKey(this.clickedOrigin)) return;

        Set<Square> validDestinations = new HashSet<Square>();
        for (int rank = 1; rank <= BoardState.RANKS; rank++) {
            for (int file = 1; file <= BoardState.FILES; file++) {
                Square destination = new Square(rank, file);
                Piece capturedPiece = this.state.getPiece(destination);
                if (capturedPiece == null || capturedPiece.getColor() != this.state.turnColor) {
                    if (piece.validateMove(this.clickedOrigin, destination, this.state))
                        validDestinations.add(destination);
                }
            }
        }

        if (piece instanceof King) addCastlingMoves((King) piece, validDestinations);

        this.validMoves.put(this.clickedOrigin, validDestinations);
    }

    private void addCastlingMoves(King king, Set<Square> moveSet) {
        if (king.getColor() == PieceColor.WHITE) {
            if (this.state.castlingRights[CastlingRights.WHITE_KINGSIDE.getValue()]) {
                Square rookPosition = this.state.rookStartingPositions[CastlingRights.WHITE_KINGSIDE.getValue()];
                for (int file = this.clickedOrigin.getFile() + 1; file <= rookPosition.getFile(); file++) {
                    Square square = new Square(this.clickedOrigin.getRank(), file);
                    if (square.equals(rookPosition)) {
                        moveSet.add(rookPosition);
                        moveSet.add(BoardState.CASTLING_SQUARES[CastlingRights.WHITE_KINGSIDE.getValue()]);
                    }
                    if (this.state.getPiece(square) != null) break;
                }
            }
            if (this.state.castlingRights[CastlingRights.WHITE_QUEENSIDE.getValue()]) {
                Square rookPosition = this.state.rookStartingPositions[CastlingRights.WHITE_QUEENSIDE.getValue()];
                for (int file = this.clickedOrigin.getFile() - 1; file >= rookPosition.getFile(); file--) {
                    Square square = new Square(this.clickedOrigin.getRank(), file);
                    if (square.equals(rookPosition)) {
                        moveSet.add(rookPosition);
                        moveSet.add(BoardState.CASTLING_SQUARES[CastlingRights.WHITE_QUEENSIDE.getValue()]);
                    }
                    if (this.state.getPiece(square) != null) break;
                }
            }
        }
        else {
            if (this.state.castlingRights[CastlingRights.BLACK_KINGSIDE.getValue()]) {
                Square rookPosition = this.state.rookStartingPositions[CastlingRights.BLACK_KINGSIDE.getValue()];
                for (int file = this.clickedOrigin.getFile() + 1; file <= rookPosition.getFile(); file++) {
                    Square square = new Square(this.clickedOrigin.getRank(), file);
                    if (square.equals(rookPosition)) {
                        moveSet.add(rookPosition);
                        moveSet.add(BoardState.CASTLING_SQUARES[CastlingRights.BLACK_KINGSIDE.getValue()]);
                    }
                    if (this.state.getPiece(square) != null) break;
                }
            }
            if (this.state.castlingRights[CastlingRights.BLACK_QUEENSIDE.getValue()]) {
                Square rookPosition = this.state.rookStartingPositions[CastlingRights.BLACK_QUEENSIDE.getValue()];
                for (int file = this.clickedOrigin.getFile() - 1; file >= rookPosition.getFile(); file--) {
                    Square square = new Square(this.clickedOrigin.getRank(), file);
                    if (square.equals(rookPosition)) {
                        moveSet.add(rookPosition);
                        moveSet.add(BoardState.CASTLING_SQUARES[CastlingRights.BLACK_QUEENSIDE.getValue()]);
                    }
                    if (this.state.getPiece(square) != null) break;
                }
            }
        }
    }

    private void drawSquares(Graphics g, final int SQUARE_SIZE, final BoardTheme THEME) {
        for (int rank = 1; rank <= BoardState.RANKS; rank++) {
            int yPos = SQUARE_SIZE * (rank - 1);
            if (this.state.turnColor == PieceColor.WHITE)
                yPos = SQUARE_SIZE * (BoardState.RANKS - rank);

            for (int file = 1; file <= BoardState.FILES; file++) {
                Square square = new Square(rank, file);

                int xPos = SQUARE_SIZE * (BoardState.FILES - file);
                if (this.state.turnColor == PieceColor.WHITE)
                    xPos = SQUARE_SIZE * (file - 1);

                g.setColor(square.isDark() ? THEME.darkSquareColor : THEME.lightSquareColor);
                g.fillRect(xPos, yPos, SQUARE_SIZE, SQUARE_SIZE);
            }
        }
    }

    private void drawCoordinates(Graphics g, final int SQUARE_SIZE, final BoardTheme THEME) {
        Font squareFont = new Font("SansSerif", Font.BOLD, SQUARE_SIZE / 6);
        FontMetrics metrics = g.getFontMetrics(squareFont);
        g.setFont(squareFont);

        final int PADDING = SQUARE_SIZE / 20;

        // draw file letters
        int xPos = PADDING;
        int yPos = SQUARE_SIZE * BoardState.RANKS - PADDING;
        if (this.state.turnColor == PieceColor.WHITE) {
            for (int file = 1; file <= BoardState.FILES; file++) {
                Square square = new Square(1, file);
                g.setColor(square.isDark() ? THEME.lightSquareColor : THEME.darkSquareColor);
                g.drawString(Character.toString(square.getLetter()), xPos, yPos);
                xPos += SQUARE_SIZE;
            }
        }
        else {
            for (int file = BoardState.FILES; file >= 1; file--) {
                Square square = new Square(BoardState.RANKS, file);
                g.setColor(square.isDark() ? THEME.lightSquareColor : THEME.darkSquareColor);
                g.drawString(Character.toString(square.getLetter()), xPos, yPos);
                xPos += SQUARE_SIZE;
            }
        }

        // draw rank numbers
        xPos = SQUARE_SIZE * BoardState.RANKS - PADDING;
        yPos = PADDING + metrics.getAscent();
        if (this.state.turnColor == PieceColor.WHITE) {
            for (int rank = BoardState.RANKS; rank >= 1; rank--) {
                Square square = new Square(rank, BoardState.FILES);
                g.setColor(square.isDark() ? THEME.lightSquareColor : THEME.darkSquareColor);
                String text = Integer.toString(rank);
                g.drawString(text, xPos - metrics.stringWidth(text), yPos);
                yPos += SQUARE_SIZE;
            }
        }
        else {
            for (int rank = 1; rank <= BoardState.RANKS; rank++) {
                Square square = new Square(rank, 1);
                g.setColor(square.isDark() ? THEME.lightSquareColor : THEME.darkSquareColor);
                String text = Integer.toString(rank);
                g.drawString(text, xPos - metrics.stringWidth(text), yPos);
                yPos += SQUARE_SIZE;
            }
        }
    }

    private void drawHighlights(Graphics g, final int SQUARE_SIZE, final BoardTheme THEME) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f)); // 40% opacity

        if (this.clickedOrigin != null) {
            int xPos = SQUARE_SIZE * (this.clickedOrigin.getFile() - 1);
            int yPos = SQUARE_SIZE * (BoardState.RANKS - this.clickedOrigin.getRank());
            if (this.state.turnColor == PieceColor.BLACK) {
                xPos = SQUARE_SIZE * (BoardState.FILES - this.clickedOrigin.getFile());
                yPos = SQUARE_SIZE * (this.clickedOrigin.getRank() - 1);
            }

            g2.setColor(THEME.clickedHighlightColor);
            g2.fillRect(xPos, yPos, SQUARE_SIZE, SQUARE_SIZE);

            this.drawValidMoves(g2, SQUARE_SIZE);
        }

        if (this.lastOrigin != null) {
            if (this.clickedOrigin == null
                    || !this.validMoves.get(this.clickedOrigin).contains(this.lastOrigin)) {
                int xPos = SQUARE_SIZE * (this.lastOrigin.getFile() - 1);
                int yPos = SQUARE_SIZE * (BoardState.RANKS - this.lastOrigin.getRank());
                if (this.state.turnColor == PieceColor.BLACK) {
                    xPos = SQUARE_SIZE * (BoardState.FILES - this.lastOrigin.getFile());
                    yPos = SQUARE_SIZE * (this.lastOrigin.getRank() - 1);
                }

                g2.setColor(THEME.lastMoveHighlightColor);
                g2.fillRect(xPos, yPos, SQUARE_SIZE, SQUARE_SIZE);
            }
        }

        if (this.lastDestination != null) {
            if (!this.lastDestination.equals(this.clickedOrigin)) {
                if (this.clickedOrigin == null
                        || !this.validMoves.get(this.clickedOrigin).contains(this.lastDestination)) {
                    int xPos = SQUARE_SIZE * (this.lastDestination.getFile() - 1);
                    int yPos = SQUARE_SIZE * (BoardState.RANKS - this.lastDestination.getRank());
                    if (this.state.turnColor == PieceColor.BLACK) {
                        xPos = SQUARE_SIZE * (BoardState.FILES - this.lastDestination.getFile());
                        yPos = SQUARE_SIZE * (this.lastDestination.getRank() - 1);
                    }

                    g2.setColor(THEME.lastMoveHighlightColor);
                    g2.fillRect(xPos, yPos, SQUARE_SIZE, SQUARE_SIZE);
                }
            }
        }

        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f)); // reset opacity
    }

    private void drawValidMoves(Graphics2D g2, final int SQUARE_SIZE) {
        final int RADIUS = SQUARE_SIZE / 4;

        for (int rank = 1; rank <= BoardState.RANKS; rank++) {
            for (int file = 1; file <= BoardState.FILES; file++) {
                Square square = new Square(rank, file);
                if (this.validMoves.get(this.clickedOrigin).contains(square)) {
                    int xPos = SQUARE_SIZE * (file - 1);
                    int yPos = SQUARE_SIZE * (BoardState.RANKS - rank);
                    if (this.state.turnColor == PieceColor.BLACK) {
                        xPos = SQUARE_SIZE * (BoardState.FILES - file);
                        yPos = SQUARE_SIZE * (rank - 1);
                    }

                    Piece piece = this.state.getPiece(square);
                    if (piece == null) {
                        xPos += (SQUARE_SIZE - RADIUS) / 2;
                        yPos += (SQUARE_SIZE - RADIUS) / 2;
                        g2.fillOval(xPos, yPos, RADIUS, RADIUS);
                    } else {
                        g2.fillPolygon(new int[]{xPos, xPos + RADIUS, xPos},
                                new int[]{yPos, yPos, yPos + RADIUS},
                                3);
                        g2.fillPolygon(new int[]{xPos, xPos + RADIUS, xPos},
                                new int[]{yPos + SQUARE_SIZE, yPos + SQUARE_SIZE, yPos + SQUARE_SIZE - RADIUS},
                                3);
                        g2.fillPolygon(new int[]{xPos + SQUARE_SIZE, xPos + SQUARE_SIZE - RADIUS, xPos + SQUARE_SIZE},
                                new int[]{yPos, yPos, yPos + RADIUS},
                                3);
                        g2.fillPolygon(new int[]{xPos + SQUARE_SIZE, xPos + SQUARE_SIZE - RADIUS, xPos + SQUARE_SIZE},
                                new int[]{yPos + SQUARE_SIZE, yPos + SQUARE_SIZE, yPos + SQUARE_SIZE - RADIUS},
                                3);
                    }
                }
            }
        }
    }

    private void drawPieces(Graphics g, final int SQUARE_SIZE, final BoardTheme THEME) {
        for (int rank = 1; rank <= BoardState.RANKS; rank++) {
            for (int file = 1; file <= BoardState.FILES; file++) {
                Piece piece = state.pieces[rank-1][file-1];
                if (piece != null) {
                    int xPos = SQUARE_SIZE * (file - 1);
                    int yPos = SQUARE_SIZE * (BoardState.RANKS - rank);
                    if (this.state.turnColor == PieceColor.BLACK) {
                        xPos = SQUARE_SIZE * (BoardState.FILES - file);
                        yPos = SQUARE_SIZE * (rank - 1);
                    }
                    piece.draw(g, xPos, yPos, SQUARE_SIZE, THEME);
                }
            }
        }
    }
}
