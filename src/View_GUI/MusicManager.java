package src.View_GUI;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.AudioInputStream;
import java.io.File;


public class MusicManager {
    private static Clip clip;
    private static Clip soundClip;

    public static void playBackgroundMusic(String filepath) {
        try {
            File musicPath = new File(filepath);

            if (!musicPath.exists()) {
                System.out.println("File existiert nicht");
            }

            else {
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(musicPath);
                clip = AudioSystem.getClip();
                clip.open(audioInputStream);
                clip.loop(Clip.LOOP_CONTINUOUSLY);
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public static void playSoundEffect(String filepath) {
        try {
            File musicPath = new File(filepath);

            if (!musicPath.exists()) {
                System.out.println("File existiert nicht");
            }

            else {
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(musicPath);
                soundClip = AudioSystem.getClip();
                soundClip.open(audioInputStream);
            }


        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public static void stopBackgroundMusic() {                //braucht man nicht aber man kann ja nie wissen
        if (clip != null && clip.isRunning()) {
            clip.stop();

        }
    }
}


