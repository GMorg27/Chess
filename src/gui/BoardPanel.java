package gui;

import game.Board;
import game.BoardState;
import game.PieceColor;

import javax.swing.*;
import java.awt.*;

public class BoardPanel extends JPanel {
    public static final int SQUARE_SIZE = 80;

    private Board board = new Board();
    private final BoardTheme theme;

    private BoardPanel(final BoardTheme THEME) { this.theme = THEME; }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.board.draw(g, SQUARE_SIZE, PieceColor.WHITE, this.theme);
    }

    @Override
    public Dimension getPreferredSize() {
        final int WIDTH = SQUARE_SIZE * BoardState.FILES;
        final int HEIGHT = SQUARE_SIZE * BoardState.RANKS;
        return new Dimension(WIDTH, HEIGHT);
    }

    public static void main(String[] args) {
        Color dark = new Color(181, 136, 99);
        Color light = new Color(240, 217, 181);
        String piecesPath = System.getProperty("user.dir") + "/textures/pieces/default/";
        BoardTheme theme = new BoardTheme(dark, light, piecesPath);
        BoardPanel boardPanel = new BoardPanel(theme);

        JFrame testFrame = new JFrame();
        testFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        testFrame.add(boardPanel);
        testFrame.pack();
        testFrame.setVisible(true);
    }
}