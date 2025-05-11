package game;

import gui.BoardTheme;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public abstract class Piece {
    private final String name;
    private final char symbol;
    private final int value;
    protected final PieceColor color;

    public Piece(final String NAME, final char SYMBOL, final int VALUE, final PieceColor COLOR) {
        this.name = NAME;
        this.symbol = SYMBOL;
        this.value = VALUE;
        this.color = COLOR;
    }

    public abstract boolean validateMove(final Square FROM, final Square TO, final BoardState STATE);

    public void draw(Graphics g, int xPos, int yPos, final int SIZE, final BoardTheme THEME) {
        final String PATH = THEME.piecesPath + this.name + "-" + Character.toString(this.color.getLetter()) + ".png";

        try {
            Image pieceImage = ImageIO.read(new File(PATH));
            g.drawImage(pieceImage, xPos, yPos, SIZE, SIZE, null);
        } catch (IOException e) {
            System.err.println("Could not load image at path: " + PATH);
        }
    }

    public char getSymbol() {
        if (this.color == PieceColor.WHITE) return this.symbol;
        else return Character.toLowerCase(this.symbol);
    }

    public int getValue() { return this.value; }

    public PieceColor getColor() { return this.color; }

    @Override
    public String toString() {
        return this.name;
    }

    protected boolean validateRookMovement(final Square FROM, final Square TO, final BoardState STATE) {
        if (TO.getRank() == FROM.getRank()) {
            for (int file = FROM.getFile() + 1; file < TO.getFile(); file++)
                if (STATE.getPiece(FROM.getRank(), file) != null) return false;
            for (int file = FROM.getFile() - 1; file > TO.getFile(); file--)
                if (STATE.getPiece(FROM.getRank(), file) != null) return false;

            return true;
        }
        else if (TO.getFile() == FROM.getFile()) {
            for (int rank = FROM.getRank() + 1; rank < TO.getRank(); rank++)
                if (STATE.getPiece(rank, FROM.getFile()) != null) return false;
            for (int rank = FROM.getRank() - 1; rank > TO.getRank(); rank--)
                if (STATE.getPiece(rank, FROM.getFile()) != null) return false;

            return true;
        }

        return false;
    }

    protected boolean validateBishopMovement(final Square FROM, final Square TO, final BoardState STATE) {
        int rank = FROM.getRank();
        int file = FROM.getFile();
        while (rank < TO.getRank() && file < TO.getFile()) {
            rank++;
            file++;
            if (rank == TO.getRank() && file == TO.getFile()) return true;
            if (STATE.getPiece(rank, file) != null) return false;
        }

        rank = FROM.getRank();
        file = FROM.getFile();
        while (rank < TO.getRank() && file > TO.getFile()) {
            rank++;
            file--;
            if (rank == TO.getRank() && file == TO.getFile()) return true;
            if (STATE.getPiece(rank, file) != null) return false;
        }

        rank = FROM.getRank();
        file = FROM.getFile();
        while (rank > TO.getRank() && file < TO.getFile()) {
            rank--;
            file++;
            if (rank == TO.getRank() && file == TO.getFile()) return true;
            if (STATE.getPiece(rank, file) != null) return false;
        }

        rank = FROM.getRank();
        file = FROM.getFile();
        while (rank > TO.getRank() && file > TO.getFile()) {
            rank--;
            file--;
            if (rank == TO.getRank() && file == TO.getFile()) return true;
            if (STATE.getPiece(rank, file) != null) return false;
        }

        return false;
    }
}
