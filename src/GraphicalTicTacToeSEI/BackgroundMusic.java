package GraphicalTicTacToeSEI;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;

public class BackgroundMusic {
    private static Clip clip;

    public static void playLoop() {
        try {
            if (clip == null || !clip.isRunning()) {
                URL soundURL = BackgroundMusic.class.getClassLoader().getResource("audio/backsound.wav");
                if (soundURL == null) {
                    System.err.println("Background music file not found.");
                    return;
                }

                AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundURL);
                clip = AudioSystem.getClip();
                clip.open(audioIn);
                clip.loop(Clip.LOOP_CONTINUOUSLY); // Loop forever
                clip.start();
            }
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public static void stop() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
            clip.close();
        }
    }
}
