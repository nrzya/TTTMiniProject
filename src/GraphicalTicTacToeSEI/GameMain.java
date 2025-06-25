package GraphicalTicTacToeSEI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class GameMain extends JPanel {
    private static final long serialVersionUID = 1L;

    public static final String TITLE = "Tic Tac Toe";
    public static final Color COLOR_BG = Color.WHITE;
    public static final Color COLOR_BG_STATUS = new Color(216, 216, 216);
    public static final Font FONT_STATUS = new Font("OCR A Extended", Font.PLAIN, 14);

    private Board board;
    private State currentState;
    private Seed currentPlayer;
    private JLabel statusBar;

    private TurnTimer turnTimer;
    private final int MAX_TURN_TIME = 5;

    public static String loggedInUser = "";
    private Theme currentTheme = Theme.LIGHT;
    private JButton themeButton;

    // [MODIFIKASI] Tambahkan ScoreBoard dan scoreLabel
    private ScoreBoard scoreBoard;
    private JLabel scoreLabel;

    public GameMain() {
        super.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = e.getY() / Cell.SIZE;
                int col = e.getX() / Cell.SIZE;

                if (currentState == State.PLAYING) {
                    if (row >= 0 && row < Board.ROWS && col >= 0 && col < Board.COLS &&
                            board.cells[row][col].content == Seed.NO_SEED) {

                        currentState = board.stepGame(currentPlayer, row, col);

                        if (currentState == State.PLAYING) {
                            currentPlayer = (currentPlayer == Seed.CROSS) ? Seed.NOUGHT : Seed.CROSS;
                            SoundEffect.CLICK.play();
                            startPlayerTimer();
                        } else {
                            stopTimerIfRunning();

                            if (currentState == State.CROSS_WON || currentState == State.NOUGHT_WON) {
                                SoundEffect.WIN.play();

                                for (Cell cell : board.getWinningCells()) {
                                    if (cell != null) {
                                        cell.setHighlight(true);
                                    }
                                }

                                // [MODIFIKASI] Tambahkan skor dan update label skor
                                scoreBoard.addScore(currentPlayer);
                                scoreLabel.setText(scoreBoard.getScoreText());

                                String pemenang = (currentState == State.CROSS_WON) ? "X" : "O";
                                JOptionPane.showMessageDialog(null, "Selamat " + pemenang + " menang!", "Game Selesai", JOptionPane.INFORMATION_MESSAGE);
                            } else {
                                SoundEffect.SERI.play();
                                JOptionPane.showMessageDialog(null, "Permainan Seri!", "Game Selesai", JOptionPane.INFORMATION_MESSAGE);
                            }
                        }

                    } else {
                        statusBar.setText("Kotak sudah terisi. Pilih kotak kosong!");
                    }

                } else {
                    newGame();
                    startPlayerTimer();
                }

                repaint();
            }
        });

        // [MODIFIKASI] Inisialisasi scoreBoard dan label skor
        scoreBoard = new ScoreBoard();
        scoreLabel = new JLabel(scoreBoard.getScoreText());
        scoreLabel.setFont(FONT_STATUS);
        scoreLabel.setBackground(COLOR_BG_STATUS);
        scoreLabel.setOpaque(true);
        scoreLabel.setPreferredSize(new Dimension(300, 30));
        scoreLabel.setHorizontalAlignment(JLabel.CENTER);
        scoreLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 12));

        statusBar = new JLabel();
        statusBar.setFont(FONT_STATUS);
        statusBar.setBackground(COLOR_BG_STATUS);
        statusBar.setOpaque(true);
        statusBar.setPreferredSize(new Dimension(300, 30));
        statusBar.setHorizontalAlignment(JLabel.LEFT);
        statusBar.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 12));

        // [MODIFIKASI] Panel atas: berisi tombol tema dan skor
        JPanel topPanel = new JPanel(new BorderLayout());
        themeButton = new JButton("Ganti Tema");
        themeButton.setFocusPainted(false);
        themeButton.setBackground(Color.LIGHT_GRAY);
        themeButton.setFont(FONT_STATUS);
        themeButton.addActionListener(e -> toggleTheme());
        topPanel.add(themeButton, BorderLayout.WEST);
        topPanel.add(scoreLabel, BorderLayout.CENTER);

        setLayout(new BorderLayout());
        add(topPanel, BorderLayout.PAGE_START); // [MODIFIKASI]
        add(statusBar, BorderLayout.PAGE_END);

        setPreferredSize(new Dimension(Board.CANVAS_WIDTH, Board.CANVAS_HEIGHT + 30));
        setBorder(BorderFactory.createLineBorder(COLOR_BG_STATUS, 2));

        initGame();
        newGame();
    }

    private void initGame() {
        board = new Board();
    }

    public void newGame() {
        board.newGame();
        currentPlayer = Seed.CROSS;
        currentState = State.PLAYING;
        startPlayerTimer();
    }

    private void startPlayerTimer() {
        stopTimerIfRunning();
        turnTimer = new TurnTimer(MAX_TURN_TIME, statusBar, () -> {
            currentState = (currentPlayer == Seed.CROSS) ? State.NOUGHT_WON : State.CROSS_WON;
            statusBar.setText("Waktu habis! " + (currentPlayer == Seed.CROSS ? "O" : "X") + " menang!");
            SoundEffect.WIN.play();
            repaint();

            // [MODIFIKASI] Tambahkan skor karena menang karena waktu habis
            scoreBoard.addScore(currentPlayer == Seed.CROSS ? Seed.NOUGHT : Seed.CROSS);
            scoreLabel.setText(scoreBoard.getScoreText());

            repaint();
        });
        turnTimer.start();
    }

    private void toggleTheme() {
        if (currentTheme == Theme.LIGHT) {
            currentTheme = Theme.DARK;
            setBackground(Color.DARK_GRAY);
            statusBar.setBackground(Color.DARK_GRAY);
            statusBar.setForeground(Color.WHITE);
            scoreLabel.setBackground(Color.DARK_GRAY);
            scoreLabel.setForeground(Color.WHITE);
            themeButton.setBackground(Color.GRAY);
            themeButton.setForeground(Color.WHITE);
        } else {
            currentTheme = Theme.LIGHT;
            setBackground(COLOR_BG);
            statusBar.setBackground(COLOR_BG_STATUS);
            statusBar.setForeground(Color.BLACK);
            scoreLabel.setBackground(COLOR_BG_STATUS);
            scoreLabel.setForeground(Color.BLACK);
            themeButton.setBackground(Color.LIGHT_GRAY);
            themeButton.setForeground(Color.BLACK);
        }
        repaint();
    }

    private void stopTimerIfRunning() {
        if (turnTimer != null) {
            turnTimer.stop();
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        setBackground(currentTheme == Theme.DARK ? Color.DARK_GRAY : COLOR_BG);
        board.paint(g);

        if (currentState == State.PLAYING) {
            statusBar.setText((currentPlayer == Seed.CROSS ? "X's Turn" : "O's Turn"));
        } else if (currentState == State.DRAW) {
            statusBar.setText("It's a draw! Click to play again.");
        } else if (currentState == State.CROSS_WON) {
            statusBar.setText("Congratulations! X wins! Click to play again.");
        } else if (currentState == State.NOUGHT_WON) {
            statusBar.setText("Congratulations! O wins! Click to play again.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            BackgroundMusic.playLoop();     // 🔊 Play background music
            new SplashScreen().setVisible(true);  // 🖼️ Show splash screen
        });
    }

    static boolean showLoginDialog() { //recommit
        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBackground(new Color(255, 245, 230)); // soft cream
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JTextField usernameField = new JTextField(10);
        usernameField.setBackground(Color.WHITE);
        usernameField.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));

        JPasswordField passwordField = new JPasswordField(10);
        passwordField.setBackground(Color.WHITE);
        passwordField.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));

        JLabel messageLabel = new JLabel("");
        messageLabel.setForeground(Color.RED);

        JLabel userLabel = new JLabel("Nama Pengguna:");
        userLabel.setForeground(new Color(80, 40, 40));
        userLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 14));

        JLabel passLabel = new JLabel("Kata Sandi:");
        passLabel.setForeground(new Color(80, 40, 40));
        passLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 14));

        panel.add(userLabel);
        panel.add(usernameField);
        panel.add(passLabel);
        panel.add(passwordField);
        panel.add(messageLabel);
        panel.add(new JLabel());

        int result;
        boolean loginSuccess = false;

        do {
            result = JOptionPane.showConfirmDialog(
                    null, panel, "Let's Play TicTactoe🦕",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE
            );

            if (result == JOptionPane.OK_OPTION) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                try {
                    String truePassword = getPassword(username);
                    if (password.equals(truePassword)) {
                        loggedInUser = username;
                        loginSuccess = true;
                    } else {
                        messageLabel.setText("❌ Wrong! Try Again.");
                        usernameField.setText("");
                        passwordField.setText("");
                    }
                } catch (ClassNotFoundException e) {
                    JOptionPane.showMessageDialog(null, "Koneksi database gagal.", "Error", JOptionPane.ERROR_MESSAGE);
                    break;
                }
            } else {
                break;
            }
        } while (!loginSuccess);

        return loginSuccess;
    }

    static String getPassword(String uName) throws ClassNotFoundException {
        String host = "mysql-156473cc-tictactoeproject.f.aivencloud.com";
        String userName = "avnadmin";
        String password = "AVNS_eLPau0fYxH3P0wSTUQG";
        String databaseName = "tictactoedb";
        String port = "16206";

        Class.forName("com.mysql.cj.jdbc.Driver");
        String userPassword = "";

        try (Connection connection =
                     DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + databaseName + "?sslmode=require", userName, password);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT password FROM game_user WHERE username = '" + uName + "'")) {
            while (resultSet.next()) {
                userPassword = resultSet.getString("password");
            }
        } catch (SQLException e) {
            System.out.println("Database error.");
            e.printStackTrace();
        }

        return userPassword;
    }
}
