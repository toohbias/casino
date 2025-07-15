package src.Logic;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import src.View_GUI.MoneyEffect;
import src.View_GUI.MusicManager;
import src.View_GUI.RouletteView;
import src.View_GUI.ViewManager;

import javafx.application.Platform;

import java.awt.Point;
import java.util.HashMap;
import java.util.Random;

public class Roulettev2 {

    public DoubleProperty deskRotation, ballRotation, speedHeight;

    private static final int[] numbers = {0, 32, 15, 19, 4, 21, 2, 25, 17, 34, 6, 27,
                                          13, 36, 11, 30, 8, 23, 10, 5, 24, 16, 33, 1,
                                          20, 14, 31, 9, 22, 18, 29, 7, 28, 12, 35, 3,
                                          26};

    /**
     * Anfangs-Abstand des Balles zur Scheibe
     */
    private static final double speedHeightStart = 2.6;

    /**
     * Abstand des Balles zur Scheibe, wenn er sich mit der Scheibe mitdreht
     */
    private static final double speedHeightDecision = 3.4;

    /**
     * Kleinster Abstand des Balles zum Mittelpunkt der Scheibe
     */
    private static final double speedHeightDone = 4.2;

    public Roulettev2() {
        // init DoubleProperties for spin()
        deskRotation = new SimpleDoubleProperty(0);
        ballRotation = new SimpleDoubleProperty(0);
        speedHeight = new SimpleDoubleProperty(speedHeightStart);
    }

    @SuppressWarnings("BusyWait") // Bei Thread.sleep(), wartet ja nicht aktiv auf etwas
    public void spin(HashMap<Point, Integer> einsatz) {
        final int einsatzTotal = einsatz.values().stream().reduce(0, Integer::sum);
        if (einsatzTotal > ViewManager.getInstance().getController().getMoney().get()) {
            // meldung zu wenig geld
            ViewManager.getInstance().displayErrorMessage("Sie haben nicht die liquiden Mittel, bitte laden sie ihren Kontostand in unserem Shop auf");
            return;
        }

        deskRotation.set(0);
        ballRotation.set(0);
        speedHeight.set(speedHeightStart);

        RouletteView.setGameView();

        ViewManager.getInstance().getController().setMoney(ViewManager.getInstance().getController().getMoney().get() - einsatzTotal); // Geld abziehen

        double speedMultiplier = new Random().nextDouble(0, 1.5);

        // 1 thread, der die rotation der scheibe animiert
        new Thread(() -> {
            double timer = 0.0;
            long timestep = 10; // ms

            // Ball dreht sich entgegen der Scheibe
            MusicManager.playSoundEffect("src/assets/soundEffects/rouletteSpinning.wav", 0.0f);    //soll starten, wenn der thread beginnt (sound geht 3 Sekunden)
            while(speedHeight.get() <= speedHeightDecision) {
                double currentSpeed = 0.0002 * Math.pow(timer, 8); // Verlauf dieses Grafen mit timer als x zeigt an, wie viel Grad
                                                                   // pro timestep gedreht werden
                Platform.runLater(() -> {
                    speedHeight.set(currentSpeed + speedHeightStart);
                    deskRotation.set(deskRotation.get() + (-currentSpeed + speedMultiplier + speedHeightStart));
                    ballRotation.set(ballRotation.get() - (-currentSpeed + speedMultiplier + speedHeightStart));
                });

                timer += timestep / 1000.0;
                try {
                    Thread.sleep(timestep);
                } catch (InterruptedException ignored) {}
            }

            // schonmal herausfinden, wo der Ball landet, und ihn in die Mitte des Feldes bewegen
            double deskAngleToBall = deskRotation.get() * 2 % 360;
            int numberIndexOnDesk = (int) Math.round(deskAngleToBall * 37.0 / 360.0);
            int result = numbers[numberIndexOnDesk];
            Platform.runLater(() -> ballRotation.set(deskRotation.get() - (360.0 / 37.0) * numberIndexOnDesk + 2));

            // beide drehen sich gemeinsam
            while(speedHeight.get() <= speedHeightDone) {
                double currentSpeed = 0.0002 * Math.pow(timer, 8);

                Platform.runLater(() -> {
                    speedHeight.set(currentSpeed + speedHeightStart);
                    deskRotation.set(deskRotation.get() + (-currentSpeed + speedMultiplier + speedHeightStart));
                    ballRotation.set(ballRotation.get() + (-currentSpeed + speedMultiplier + speedHeightStart));
                });

                timer += timestep / 1000.0;
                try {
                    Thread.sleep(timestep);
                } catch (InterruptedException ignored) {}
            }

            // die Animation stoppt
            MusicManager.playSoundEffect("src/assets/soundEffects/rouletteLanding.wav", 0.0f);   // soll starten, wenn der Ball kurz vor dem Landen ist, am besten also nach 3 sek, wenn der andere sound vorbei ist(sound geht 2 sek))
            double done = timer + 2;
            while(timer <= done) {
                double currentSpeed = Math.pow(2, -3 * (timer - 3)); // don't mind the math, the result looks fine

                Platform.runLater(() -> {
                    deskRotation.set(deskRotation.get() + (currentSpeed + speedMultiplier));
                    ballRotation.set(ballRotation.get() + (currentSpeed + speedMultiplier));
                });

                timer += timestep / 1000.0;
                try {
                    Thread.sleep(timestep);
                } catch (InterruptedException ignored) {}
            }
            berechnen(result, einsatz);
        }).start();

    }

    public static void berechnen(int result, HashMap<Point, Integer> bets) {
        int totalerGewinn = 0;

        // Reihen
        if(result % 3 == 0 && result != 0 && bets.containsKey(new Point(26, 1))) { totalerGewinn += bets.get(new Point(26, 1)) * 3; }
        if((result + 1) % 3 == 0 && bets.containsKey(new Point(26, 3))) { totalerGewinn += bets.get(new Point(26, 3)) * 3; }
        if((result + 2) % 3 == 0 && bets.containsKey(new Point(26, 5))) { totalerGewinn += bets.get(new Point(26, 5)) * 3; }

        // Dutzend
        if(result <= 12 && result != 0 && bets.containsKey(new Point(2, 7))) { totalerGewinn += bets.get(new Point(2, 7)) * 3; }
        if(result > 12 && result <= 24 && bets.containsKey(new Point(10, 7))) { totalerGewinn += bets.get(new Point(10, 7)) * 3; }
        if(result > 24 && bets.containsKey(new Point(18, 7))) { totalerGewinn += bets.get(new Point(18, 7)) * 3; }

        // Halbe
        if(result <= 18 && result != 0 && bets.containsKey(new Point(2, 9))) { totalerGewinn += bets.get(new Point(2, 9)) * 2; }
        if(result > 18 && bets.containsKey(new Point(22, 9))) { totalerGewinn += bets.get(new Point(22, 9)) * 2; }

        // Gerade
        if(result % 2 == 0 && result != 0 && bets.containsKey(new Point(6, 9))) { totalerGewinn += bets.get(new Point(6, 9)) * 2; }
        if(result % 2 == 1 && bets.containsKey(new Point(18, 9))) { totalerGewinn += bets.get(new Point(18, 9)) * 2; }

        // Farbe
        if(!RouletteView.BLACK.contains(result) && result != 0 && bets.containsKey(new Point(10, 9))) { totalerGewinn += bets.get(new Point(10, 9)) * 2; }
        if(RouletteView.BLACK.contains(result) && bets.containsKey(new Point(14, 9))) { totalerGewinn += bets.get(new Point(14, 9)) * 2; }

        // TODO inner bets

        if(totalerGewinn > 0) {
            // macht die animation im money frame
            int finalTotalerGewinn = totalerGewinn;
            Platform.runLater(() -> ViewManager.getInstance().getController().win(ViewManager.getInstance().getController().getMoney().get() + finalTotalerGewinn));

            System.out.println("Herzlichen Glückwunsch sie haben " + totalerGewinn + " V-Bucks gewonnen");
            System.out.println("Ihr neuer Kontostand beträgt " + ViewManager.getInstance().getController().getMoney().get() + " V-Bucks");
            // coin animation
            new Thread(() -> {
//            Thread anim = new MoneyEffect.AnimationThread();
//            anim.start();
                MoneyEffect.animate(100);
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ignored) {
                }
                Platform.runLater(MoneyEffect::stop);
//            anim.interrupt();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ignored) {}
                Platform.runLater(() -> ViewManager.getInstance().setView(ViewManager.ROULETTE_VIEW));
            }).start();
        } else {
            System.out.println("Niemals Aufgeben (" + ViewManager.getInstance().getController().getMoney().get() + ")");
            Platform.runLater(() -> ViewManager.getInstance().setView(ViewManager.ROULETTE_VIEW));
        }
    }

}