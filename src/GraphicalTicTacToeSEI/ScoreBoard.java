package GraphicalTicTacToeSEI;

/**
 * Kelas ScoreBoard untuk mencatat skor pemain.
 */
public class ScoreBoard {
    private int scoreCross;
    private int scoreNought;

    public ScoreBoard() {
        this.scoreCross = 0;
        this.scoreNought = 0;
    }

    public void addScore(Seed winner) {
        if (winner == Seed.CROSS) {
            scoreCross++;
        } else if (winner == Seed.NOUGHT) {
            scoreNought++;
        }
    }

    public int getScore(Seed player) {
        return (player == Seed.CROSS) ? scoreCross : scoreNought;
    }

    public String getScoreText() {
        return "Score - X: " + scoreCross + " | O: " + scoreNought;
    }

    public void resetScores() {
        scoreCross = 0;
        scoreNought = 0;
    }
}
