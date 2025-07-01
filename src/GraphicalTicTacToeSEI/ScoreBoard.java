package GraphicalTicTacToeSEI;



public class ScoreBoard {
    private int scoreCross;
    private int scoreNought;
    private String playerXName;
    private String playerOName;

    // Constructor dengan nama pemain
    public ScoreBoard(String playerXName, String playerOName) {
        this.playerXName = playerXName;
        this.playerOName = playerOName;
        this.scoreCross = 0;
        this.scoreNought = 0;
    }

    // Menambahkan skor untuk pemain yang menang
    public void addScore(Seed winner) {
        if (winner == Seed.CROSS) {
            scoreCross++;
        } else if (winner == Seed.NOUGHT) {
            scoreNought++;
        }
    }

    // Mengambil skor sesuai pemain
    public int getScore(Seed player) {
        return (player == Seed.CROSS) ? scoreCross : scoreNought;
    }

    // Mengembalikan teks skor dengan nama pemain
    public String getScoreText() {
        return playerXName + ": " + scoreCross + "  |  " + playerOName + ": " + scoreNought;
    }

    // Reset semua skor
    public void resetScores() {
        scoreCross = 0;
        scoreNought = 0;
    }


    public void setPlayerNames(String playerXName, String playerOName) {
        this.playerXName = playerXName;
        this.playerOName = playerOName;
    }
}
