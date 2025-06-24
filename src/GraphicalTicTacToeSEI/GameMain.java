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
    private final int MAX_TURN_TIME = 5; // detik per giliran

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
                            } else {
                                SoundEffect.SERI.play();
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

        statusBar = new JLabel();
        statusBar.setFont(FONT_STATUS);
        statusBar.setBackground(COLOR_BG_STATUS);
        statusBar.setOpaque(true);
        statusBar.setPreferredSize(new Dimension(300, 30));
        statusBar.setHorizontalAlignment(JLabel.LEFT);
        statusBar.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 12));

        setLayout(new BorderLayout());
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
        });
        turnTimer.start();
    }

    private void stopTimerIfRunning() {
        if (turnTimer != null) {
            turnTimer.stop();
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        setBackground(COLOR_BG);
        board.paint(g);

        if (currentState == State.PLAYING) {
            statusBar.setForeground(Color.BLACK);
            statusBar.setText((currentPlayer == Seed.CROSS ? "Giliran X" : "Giliran O"));
        } else if (currentState == State.DRAW) {
            statusBar.setForeground(Color.RED);
            statusBar.setText("Hasil seri! Klik untuk main lagi.");
        } else if (currentState == State.CROSS_WON) {
            statusBar.setForeground(Color.RED);
            statusBar.setText("X menang! Klik untuk main lagi.");
        } else if (currentState == State.NOUGHT_WON) {
            statusBar.setForeground(Color.RED);
            statusBar.setText("O menang! Klik untuk main lagi.");
        }
    }

    public static void main(String[] args) throws ClassNotFoundException {
        SwingUtilities.invokeLater(() -> { //login
            boolean loginPassed = showLoginDialog();
            if (loginPassed) {
                JFrame frame = new JFrame(TITLE);
                frame.setContentPane(new GameMain());
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            } else {
                System.exit(0);
            }
        });
    }

    private static boolean showLoginDialog() {
        JPanel panel = new JPanel(new GridLayout(3, 2));
        JTextField usernameField = new JTextField(10);
        JPasswordField passwordField = new JPasswordField(10);
        JLabel messageLabel = new JLabel("");

        panel.add(new JLabel("Username:"));
        panel.add(usernameField);
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);
        panel.add(messageLabel);

        int result;
        boolean loginSuccess = false;

        do {
            result = JOptionPane.showConfirmDialog(
                    null, panel, "Login", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (result == JOptionPane.OK_OPTION) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                try {
                    String truePassword = getPassword(username);
                    if (password.equals(truePassword)) {
                        loginSuccess = true;
                    } else {
                        messageLabel.setText("Login gagal. Coba lagi.");
                        usernameField.setText("");
                        passwordField.setText("");
                    }
                } catch (ClassNotFoundException e) {
                    JOptionPane.showMessageDialog(null, "Koneksi database gagal.", "Error", JOptionPane.ERROR_MESSAGE);
                    e.printStackTrace();
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
