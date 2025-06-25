package GraphicalTicTacToeSEI;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class SplashScreen extends JFrame {

    public SplashScreen() { //recommit
        setTitle("Tic Tac Toe 🦕");
        setSize(960, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setResizable(false);


        JLabel imageLabel = new JLabel();
        URL imgURL = getClass().getClassLoader().getResource("images/showfirst.gif");
        if (imgURL != null) {
            ImageIcon icon = new ImageIcon(imgURL);
            Image scaledImage = icon.getImage().getScaledInstance(getWidth(), getHeight() - 100, Image.SCALE_SMOOTH);
            imageLabel.setIcon(new ImageIcon(scaledImage));
        } else {
            imageLabel.setText("picture not found.");
            imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        }

        // 🎮 Tombol Mulai
        JButton startButton = new JButton("Start The Game🦖");
        startButton.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
        startButton.setBackground(new Color(255, 223, 186));
        startButton.setForeground(Color.DARK_GRAY);
        startButton.setFocusPainted(false);
        startButton.setPreferredSize(new Dimension(260, 50));
        startButton.addActionListener(e -> {
            dispose();
            boolean loginPassed = GameMain.showLoginDialog();
            if (loginPassed) {
                new WelcomeScreen(GameMain.loggedInUser).setVisible(true);
            } else {
                System.exit(0);
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(250, 250, 250));
        buttonPanel.add(startButton);
        buttonPanel.setPreferredSize(new Dimension(getWidth(), 80));

        add(imageLabel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
}
