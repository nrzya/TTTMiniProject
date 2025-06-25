package GraphicalTicTacToeSEI;

import java.awt.*;

public class Cell {
    public static final int SIZE = 120;
    public static final int PADDING = SIZE / 5;
    public static final int SEED_SIZE = SIZE - PADDING * 2;

    Seed content;
    int row, col;
    private boolean highlight = false;

    public Cell(int row, int col) {
        this.row = row;
        this.col = col;
        content = Seed.NO_SEED;
    }

    public void newGame() {
        content = Seed.NO_SEED;
        highlight = false;
    }

    public void setHighlight(boolean highlight) {
        this.highlight = highlight;
    }

    public void paint(Graphics g) {
        // Highlight background if needed
        if (highlight) {
            g.setColor(new Color(255, 255, 0, 100)); // light yellow
            g.fillRect(col * SIZE, row * SIZE, SIZE, SIZE);
        }

        // Draw symbol
        int x1 = col * SIZE + PADDING;
        int y1 = row * SIZE + PADDING;
        if (content == Seed.CROSS || content == Seed.NOUGHT) {
            g.drawImage(content.getImage(), x1, y1, SEED_SIZE, SEED_SIZE, null);
        }
    }
}
