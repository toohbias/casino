package src.Logic;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import src.View_GUI.MoneyEffect;
import src.View_GUI.MusicManager;
import src.View_GUI.RouletteView;
import src.View_GUI.ViewManager;

import javafx.application.Platform;

import java.util.*;

public class Roulettev2 {

    public DoubleProperty deskRotation, ballRotation, speedHeight;

    private static final int[] numbers = {0, 32, 15, 19, 4, 21, 2, 25, 17, 34, 6, 27,
                                          13, 36, 11, 30, 8, 23, 10, 5, 24, 16, 33, 1,
                                          20, 14, 31, 9, 22, 18, 29, 7, 28, 12, 35, 3,
                                          26, 0};

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
    public void spin(Collection<Bet> einsatz) {
//        final int einsatzTotal = einsatz.values().stream().reduce(0, Integer::sum);
        final int einsatzTotal = einsatz.stream().map(bet -> bet.stakes).reduce(0, Integer::sum);
        System.out.println(einsatzTotal);
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

        double speedMultiplier = new Random().nextDouble(0.000, 1.500);

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

    public static void berechnen(int result, Collection<Bet> bets) {
        int gewinn = bets.stream().filter(bet -> bet.numbers.contains(result)).map(bet -> bet.stakes * bet.payout).reduce(0, Integer::sum); // avg rust line

        if(gewinn > 0) {
            // macht die animation im money frame
            Platform.runLater(() -> ViewManager.getInstance().getController().win(ViewManager.getInstance().getController().getMoney().get() + gewinn));

            System.out.println("Herzlichen Glückwunsch sie haben " + gewinn + " V-Bucks gewonnen");
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

    public static class Bet {
        public List<Integer> numbers;
        public final int stakes;
        public int payout;

        public Bet(int x, int y, int stakes) {
            this.stakes = stakes;
            numbers = new ArrayList<>();
            if(x == 0 && y == 1) { // Zahl 0
                payout = 36;
                numbers.add(0);
            } else if(x == 26) { // Reihen
                payout = 3;
                for(int i = 1 + (6 - y) / 2; i <= 36; i+=3) {
                    numbers.add(i);
                }
            } else if(y == 7) { // Dutzend
                payout = 3;
                for(int i = 1; i <= 12; i++) {
                    numbers.add(i + 12 * ((x - 2) / 8));
                }
            } else if(y == 9) {
                payout = 2;
                switch(x) {
                    case 2: { for(int i = 1; i <= 18; i++) { numbers.add(i); } break; } // unteres Halb
                    case 22: { for(int i = 19; i <= 36; i++) { numbers.add(i); } break; } // oberes Halb
                    case 6: { for(int i = 2; i <= 36; i+=2) { numbers.add(i); } break; } // Gerade
                    case 18: { for(int i = 1; i <= 35; i+=2) { numbers.add(i); } break; } // Ungerade
                    case 10: { numbers.addAll(RouletteView.RED); break; } // Rot
                    case 14: { numbers.addAll(RouletteView.BLACK); break; } // Schwarz
                }
            } else {
                if(x % 2 == 0 && y % 2 == 1) { // Ganze Zahlen
                    numbers.add((x / 2 - 1) * 3 + (3 - (y / 2)));
                    payout = 36;
                } else if(y == 0) {
                    for (int i = 0; i < 3; i++) {
                        numbers.add(3 * (x / 2) - i);
                    }
                    if (x % 2 == 1) { // Doppelte Strasse
                        for (int i = 0; i < 3; i++) {
                            numbers.add(3 * ((x + 1) / 2) - i);
                        }
                        payout = 6;
                    } else { // Strasse
                        payout = 12;
                    }
                } else if(x == 1) { // Felder mit 0 zu handlen ist bisschen schwer deshalb separat
                    numbers.add(0);
                    switch(y) {
                        case 1: { numbers.add(3); payout = 18; break; }
                        case 2: { numbers.add(3); numbers.add(2); payout = 12; break; }
                        case 3: { numbers.add(2); payout = 18; break; }
                        case 4: { numbers.add(2); numbers.add(1); payout = 12; break; }
                        case 5: { numbers.add(1); payout = 18; break; }
                    }
                } else if(x % 2 == 1 && y % 2 == 1) { // horizontaler Split
                    numbers.add(((x - 1) / 2 - 1) * 3 + (3 - (y / 2)));
                    numbers.add(((x + 1) / 2 - 1) * 3 + (3 - (y / 2)));
                    payout = 18;
                } else if(x % 2 == 0 && y % 2 == 0) { // vertikaler Split
                    numbers.add((x / 2 - 1) * 3 + (3 - ((y - 1) / 2)));
                    numbers.add((x / 2 - 1) * 3 + (3 - ((y + 1) / 2)));
                    payout = 18;
                } else { // Ecke
                    numbers.add(((x - 1) / 2 - 1) * 3 + (3 - ((y - 1) / 2)));
                    numbers.add(((x + 1) / 2 - 1) * 3 + (3 - ((y - 1) / 2)));
                    numbers.add(((x - 1) / 2 - 1) * 3 + (3 - ((y + 1) / 2)));
                    numbers.add(((x + 1) / 2 - 1) * 3 + (3 - ((y + 1) / 2)));
                    payout = 9;
                }
            }
            System.out.print(x + " " + y + ": ");
            numbers.forEach(n -> System.out.print(n + " "));
            System.out.println();
        }
    }
}