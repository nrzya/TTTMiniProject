package GraphicalTicTacToeSEI;

import javax.swing.*;
import java.awt.*;

public class WelcomeScreen extends JFrame {

    public WelcomeScreen(String username) {
        setTitle("WOOHOOO YOU START THE GAME!");
        setSize(350, 150);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel welcomeLabel = new JLabel("Welcome, " + username + "!", JLabel.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        add(welcomeLabel);

        // Hanya sekali jalan: tampil 2 detik, lalu lanjut ke permainan
        Timer timer = new Timer(2000, e -> {
            dispose(); // tutup welcome window

            JFrame frame = new JFrame(GameMain.TITLE);
            frame.setContentPane(new GameMain());
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
        timer.setRepeats(false); // <--- INI YANG PENTING
        timer.start();
    }
}
