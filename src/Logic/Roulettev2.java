package src.Logic;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import src.View_GUI.MoneyEffect;
import src.View_GUI.MusicManager;
import src.View_GUI.RouletteView;
import src.View_GUI.ViewManager;

import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.ToggleButton;

import java.util.Random;

public class Roulettev2 {

    public DoubleProperty deskRotation, ballRotation;

    public static int zufallszahl(int max) {
        return new Random().nextInt(max);
    }

    public Roulettev2() {
        // init DoubleProperties for spin()
        deskRotation = new SimpleDoubleProperty(0);
        ballRotation = new SimpleDoubleProperty(0);
    }

    @SuppressWarnings("BusyWait") // Bei Thread.sleep(), wartet ja nicht aktiv auf etwas
    public void spin(int einsatz) {
        if (einsatz > ViewManager.getInstance().getController().getMoney().get()) {
            // meldung zu wenig geld
            ViewManager.getInstance().displayErrorMessage("Sie haben nicht die liquiden Mittel, bitte laden sie ihren Kontostand in unserem Shop auf");
            return;
        }
        RouletteView.setGameView();

        ViewManager.getInstance().getController().setMoney(ViewManager.getInstance().getController().getMoney().get() - einsatz); // Geld abziehen
        //animation start

        MusicManager.playSoundEffect("src/assets/soundEffects/rouletteSpinning.wav", 0.0f);    //soll starten wenn der thread beginnt (sound geht 3 sekunden )
        MusicManager.playSoundEffect("src/assets/soundEffects/rouletteLanding.wav", 0.0f);   // soll starten wenn der ball kurz vor dem landen ist, am besten also nach 3 sek wenn der andere sound vorbei ist(sound geht 2 sek))
        //Die sounds gehen zsm 5 sek, wäre geil wenn dann die animation auch 5 sek geht

        // 1 thread, der die rotation der scheibe animiert


            // Gewinn bestimmen


    }

    public static void berechnen(int einsatz, int multiplikator) {
        if (multiplikator > 0) {
            int gewinn = einsatz * multiplikator;

            // macht die animation im money frame
            ViewManager.getInstance().getController().win(ViewManager.getInstance().getController().getMoney().get() + gewinn);

            System.out.println("Herzlichen Glückwunsch sie haben " + gewinn + " V-Bucks gewonnen");
            System.out.println("Ihr neuer Kontostand beträgt " + ViewManager.getInstance().getController().getMoney().get() + " V-Bucks");
            // coin animation
            new Thread(() -> {
//                Thread anim = new MoneyEffect.AnimationThread();
//                anim.start();
                MoneyEffect.animate(100);
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ignored) {}
                Platform.runLater(MoneyEffect::stop);
//                anim.interrupt();
            }).start();
        } else {
            System.out.println("Niemals Aufgeben");
        }
    }

}