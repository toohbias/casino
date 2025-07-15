package src.View_GUI;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.AudioInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import javax.sound.sampled.*;


public class MusicManager {
    private static Clip clip;
    private static Clip soundClip;

    public static void playBackgroundMusic(String filepath, float volumeDb) {
        try {
//            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(filepath));
            Path temp = Files.createTempFile(null, null);
            try(InputStream is = Objects.requireNonNull(MusicManager.class.getResourceAsStream("/" + filepath))) {
                Files.copy(is, temp, StandardCopyOption.REPLACE_EXISTING);
            }
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(temp.toFile());
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);

            // Lautstärke einstellen
            if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                gainControl.setValue(volumeDb);  // -10.0f = leiser, 0.0f = Original, +6.0f = lauter
            }

            clip.loop(Clip.LOOP_CONTINUOUSLY);

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public static void playSoundEffect(String filepath, float volumeDb) {
        try {
//            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(filepath));
            Path temp = Files.createTempFile(null, null);
            try(InputStream is = Objects.requireNonNull(MusicManager.class.getResourceAsStream("/" + filepath))) {
                Files.copy(is, temp, StandardCopyOption.REPLACE_EXISTING);
            }
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(temp.toFile());
            soundClip = AudioSystem.getClip();
            soundClip.open(audioInputStream);

            // Lautstärke einstellen
            if (soundClip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                FloatControl gainControl = (FloatControl) soundClip.getControl(FloatControl.Type.MASTER_GAIN);
                gainControl.setValue(volumeDb);
            }

            soundClip.start();

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }


    public static void stopBackgroundMusic() {                //braucht man nicht aber man kann ja nie wissen
        if (clip != null && clip.isRunning()) {
            clip.stop();

        }
    }
}


