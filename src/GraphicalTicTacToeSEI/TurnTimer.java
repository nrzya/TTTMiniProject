package GraphicalTicTacToeSEI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;

public class TurnTimer {
    private Timer timer;
    private int timeLeft;
    private int initialTime;
    private JLabel statusLabel;
    private Runnable onTimeout;

    public TurnTimer(int seconds, JLabel statusLabel, Runnable onTimeout) {
        this.initialTime = seconds;
        this.statusLabel = statusLabel;
        this.onTimeout = onTimeout;
        this.timeLeft = seconds;
    }

    public void start() {
        stop(); // stop existing timer if any
        timeLeft = initialTime;
        timer = new Timer(1000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                timeLeft--;
                if (timeLeft > 0) {
                    statusLabel.setText("Giliran " + (statusLabel.getText().contains("X") ? "X" : "O") +
                            " | Sisa waktu: " + timeLeft + " detik");
                    if (timeLeft <= 3) playTickSound();
                } else {
                    stop();
                    onTimeout.run(); // waktu habis -> kalah
                }
            }
        });
        timer.start();
    }

    public void stop() {
        if (timer != null && timer.isRunning()) {
            timer.stop();
        }
    }

    private void playTickSound() {
        try {
            URL url = getClass().getClassLoader().getResource("audio/tick.wav");
            if (url != null) {
                Clip clip = AudioSystem.getClip();
                AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
                clip.open(audioIn);
                clip.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
