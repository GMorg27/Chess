package gui;

import java.awt.*;

public class BoardTheme {
    public Color darkSquareColor;
    public Color lightSquareColor;
    public String piecesPath;

    public BoardTheme(Color darkSquareColor, Color lightSquareColor, String piecesPath) {
        this.darkSquareColor = darkSquareColor;
        this.lightSquareColor = lightSquareColor;
        this.piecesPath = piecesPath;
    }
}
