package src.Logic;

import java.util.Random;

import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.ToggleButton;
import src.View_GUI.MoneyEffect;
import src.View_GUI.MusicManager;
import src.View_GUI.ViewManager;

public class SlotMachinev2 {

    public IntegerProperty symbol1, symbol2, symbol3;

    public static int zufallszahl(int max) {
        return new Random().nextInt(max);
    }

    public SlotMachinev2() {
        // init IntProperties for spin()
        symbol1 = new SimpleIntegerProperty(0);
        symbol2 = new SimpleIntegerProperty(0);
        symbol3 = new SimpleIntegerProperty(0);
    }

    @SuppressWarnings("BusyWait") // Bei Thread.sleep(), wartet ja nicht aktiv auf etwas
    public void spin(int einsatz, ToggleButton slotArm) {
        if (einsatz > ViewManager.getInstance().getController().getMoney().get()) {
            // Slot Arm wieder freigeben
            slotArm.setSelected(false);
            slotArm.setDisable(false);
            ViewManager.getInstance().displayInfoMessage("Sie haben nicht die liquiden Mittel, bitte laden sie ihren Kontostand in unserem Shop auf");
            return;
        }
        ViewManager.getInstance().getController().setMoney(ViewManager.getInstance().getController().getMoney().get() - einsatz); // Geld abziehen
        slotArm.setDisable(true);
        slotArm.setSelected(true);

        MusicManager.playSoundEffect("src/assets/soundEffects/slotMachineSpinningSoundLeiser.wav", -6f);
        MusicManager.playSoundEffect("src/assets/soundEffects/slotStartingsound1.wav", -4f);

        // 3 threads, die die Bilder animieren
        Thread t1 = new Thread(() -> {
            int time = 0;
            int i = zufallszahl(4);
            double rotationSpeed = 50;
            while (time < zufallszahl(500) + 500) {
                try {
                    final int frame = i % 5;
                    Platform.runLater(() -> symbol1.set(frame));
                    i++;
                    time += (int) rotationSpeed;
                    rotationSpeed *= 1.3;
                    Thread.sleep((long) rotationSpeed);
                } catch (InterruptedException ignored) {
                }
            }
        });

        Thread t2 = new Thread(() -> {
            int time = 0;
            int i = zufallszahl(4);
            double rotationSpeed = 50;
            while (time < zufallszahl(500) + 1500) {
                try {
                    final int frame = i % 5;
                    Platform.runLater(() -> symbol2.set(frame));
                    i++;
                    time += (int) rotationSpeed;
                    rotationSpeed *= 1.2;
                    Thread.sleep((long) rotationSpeed);
                } catch (InterruptedException ignored) {
                }
            }
        });

        Thread t3 = new Thread(() -> {
            int time = 0;
            int i = zufallszahl(4);
            double rotationSpeed = 50;
            while (time < zufallszahl(500) + 2500) {
                try {
                    final int frame = i % 5;
                    Platform.runLater(() -> symbol3.set(frame));
                    i++;
                    time += (int) rotationSpeed;
                    rotationSpeed *= 1.1;
                    Thread.sleep((long) rotationSpeed);
                } catch (InterruptedException ignored) {
                }
            }

            // Gewinn bestimmen
            final int s1 = symbol1.get();
            final int s2 = symbol2.get();
            final int s3 = symbol3.get();

            Platform.runLater(() -> {
                if (s1 == s2 && s1 == s3) {
                    switch (s1) {
                        case 0:
                            berechnen(einsatz, 50); //bei dreimal 7
                            break;
                        case 1:
                            berechnen(einsatz, 25); //bei dreimal Glocke
                            break;
                        default:
                            berechnen(einsatz, 10);  //bei dreimal Frucht
                    }
                } else if (s1 >= 2 && s2 >= 2 && s3 >= 2 && s1 != s2 && s1 != s3 && s2 != s3) {
                    berechnen(einsatz, 5);          // 3 verschiedene Fruchtarten
                } else {
                    berechnen(einsatz, 0);          // kein Gewinn
                }
                // Slot Arm wieder freigeben
                slotArm.setSelected(false);
                slotArm.setDisable(false);
            });
        });

        // Threads starten
        t1.start();
        t2.start();
        t3.start();
    }

    public static void berechnen(int einsatz, int multiplikator) {
        if (multiplikator > 0) {
            int gewinn = einsatz * multiplikator;

            // macht die animation im money frame
            ViewManager.getInstance().getController().win(ViewManager.getInstance().getController().getMoney().get() + gewinn);

            MusicManager.playSoundEffect("src/assets/soundEffects/slotMachineWining1.wav", 0.0f);

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

            System.out.println("Niemals Aufgeben (" + ViewManager.getInstance().getController().getMoney().get() + ")");
        }
    }

}