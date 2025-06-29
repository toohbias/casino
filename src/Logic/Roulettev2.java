package src.Logic;

import src.View_GUI.MoneyEffect;
import src.View_GUI.ViewManager;

import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.ToggleButton;

import java.util.Random;

public class Roulettev2 {

    public IntegerProperty zufallszahlwürfel;

    public static int zufallszahl(int max) {
        return new Random().nextInt(max);
    }

    public Roulettev2() {
        // init IntProperties for spin()
        zufallszahlwürfel = new SimpleIntegerProperty(0);
    }

    @SuppressWarnings("BusyWait") // Bei Thread.sleep(), wartet ja nicht aktiv auf etwas
    public void spin(int einsatz) throws IllegalAccessException {
        if (einsatz > ViewManager.getInstance().getController().getMoney().get()) {
            // meldung zu wenig geld
            throw new IllegalAccessException("Sie haben nicht die liquiden Mittel, bitte laden sie ihren Kontostand in unserem Shop auf");
        }
        ViewManager.getInstance().getController().setMoney(ViewManager.getInstance().getController().getMoney().get() - einsatz); // Geld abziehen
        //animation start

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