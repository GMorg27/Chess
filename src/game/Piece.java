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
    private final PieceColor color;

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

    @Override
    public String toString() {
        return this.name;
    }
}
