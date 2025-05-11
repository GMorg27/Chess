package gui;

import java.awt.*;

public class BoardTheme {
    public Color darkSquareColor;
    public Color lightSquareColor;
    public Color clickedHighlightColor;
    public Color lastMoveHighlightColor;
    public String piecesPath;

    public BoardTheme(Color darkSquareColor, Color lightSquareColor, Color clickedHighlightColor,
                      Color lastMoveHighlightColor, String piecesPath) {
        this.darkSquareColor = darkSquareColor;
        this.lightSquareColor = lightSquareColor;
        this.lastMoveHighlightColor = lastMoveHighlightColor;
        this.clickedHighlightColor = clickedHighlightColor;
        this.piecesPath = piecesPath;
    }
}
