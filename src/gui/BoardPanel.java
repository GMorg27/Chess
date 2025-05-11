package gui;

import game.Board;
import game.BoardState;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class BoardPanel extends JPanel {
    public static final int SQUARE_SIZE = 80;

    private Board board = new Board();
    private final BoardTheme theme;

    public BoardPanel(final BoardTheme THEME) {
        this.theme = THEME;

        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent me) {
                board.handleClick(me, SQUARE_SIZE);
                repaint();
            }
        });
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.board.draw(g, SQUARE_SIZE, this.theme);
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
        Color clickedHighlight = new Color(140, 0, 220);
        Color lastMoveHighlight = new Color(255, 255, 0);
        String piecesPath = System.getProperty("user.dir") + "/textures/pieces/default/";
        BoardTheme theme = new BoardTheme(dark, light, clickedHighlight, lastMoveHighlight, piecesPath);
        BoardPanel boardPanel = new BoardPanel(theme);

        JFrame testFrame = new JFrame();
        testFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        testFrame.add(boardPanel);
        testFrame.pack();
        testFrame.setVisible(true);
    }
}