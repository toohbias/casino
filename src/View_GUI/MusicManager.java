package src.View_GUI;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.AudioInputStream;
import java.io.File;


public class MusicManager {
    private static Clip clip;

    public static void playBackgroundMusic(String filepath) {
        try {
            File musicPath = new File(filepath);

            if (!musicPath.exists()) {
                return;
            }

            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(musicPath);
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.loop(Clip.LOOP_CONTINUOUSLY);

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public static void stopBackgroundMusic() {
        if (clip != null && clip.isRunning()) {
            clip.stop();

        }
    }
}


