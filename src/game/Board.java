package game;

import gui.BoardTheme;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLInvalidAuthorizationSpecException;

public class Board {
    private BoardState state = new BoardState();

    public Board() { this.state.loadDefaultPosition(); }

    public void draw(Graphics g, final int SQUARE_SIZE, final PieceColor PERSPECTIVE, final BoardTheme THEME) {
        this.drawSquares(g, SQUARE_SIZE, PERSPECTIVE, THEME);
        this.drawCoordinates(g, SQUARE_SIZE, PERSPECTIVE, THEME);
        this.drawPieces(g, SQUARE_SIZE, PERSPECTIVE, THEME);
    }

    private void drawSquares(Graphics g, final int SQUARE_SIZE, final PieceColor PERSPECTIVE, final BoardTheme THEME) {
        for (int rank = 1; rank <= BoardState.RANKS; rank++) {
            int yPos = SQUARE_SIZE * (rank - 1);
            if (PERSPECTIVE == PieceColor.WHITE)
                yPos = SQUARE_SIZE * (BoardState.RANKS - rank);

            for (int file = 1; file <= BoardState.FILES; file++) {
                Square square = new Square(rank, file);

                int xPos = SQUARE_SIZE * (BoardState.FILES - file);
                if (PERSPECTIVE == PieceColor.WHITE)
                    xPos = SQUARE_SIZE * (file - 1);

                g.setColor(square.isDark() ? THEME.darkSquareColor : THEME.lightSquareColor);
                g.fillRect(xPos, yPos, SQUARE_SIZE, SQUARE_SIZE);
            }
        }
    }

    private void drawCoordinates(Graphics g, final int SQUARE_SIZE, final PieceColor PERSPECTIVE,
                                 final BoardTheme THEME) {
        Font squareFont = new Font("SansSerif", Font.BOLD, SQUARE_SIZE / 6);
        FontMetrics metrics = g.getFontMetrics(squareFont);
        g.setFont(squareFont);

        final int PADDING = SQUARE_SIZE / 15;

        // draw file letters
        int xPos = PADDING;
        int yPos = SQUARE_SIZE * BoardState.RANKS - PADDING;
        if (PERSPECTIVE == PieceColor.WHITE) {
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
        if (PERSPECTIVE == PieceColor.WHITE) {
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

    private void drawPieces(Graphics g, final int SQUARE_SIZE, final PieceColor PERSPECTIVE, final BoardTheme THEME) {
        for (int rank = 1; rank <= BoardState.RANKS; rank++) {
            for (int file = 1; file <= BoardState.FILES; file++) {
                Piece piece = state.pieces[rank-1][file-1];
                if (piece != null) {
                    int xPos = SQUARE_SIZE * (file - 1);
                    int yPos = SQUARE_SIZE * (BoardState.RANKS - rank);
                    if (PERSPECTIVE == PieceColor.BLACK) {
                        xPos = SQUARE_SIZE * (BoardState.FILES - file);
                        yPos = SQUARE_SIZE * (rank - 1);
                    }
                    piece.draw(g, xPos, yPos, SQUARE_SIZE, THEME);
                }
            }
        }
    }
}
