package src.Logic;

import java.util.Random;

import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class SlotMachinev2 {

    public IntegerProperty symbol1, symbol2, symbol3;
    final private CasinoController konto;

    public static int zufallszahl(int max) {
        return new Random().nextInt(max);
    }

    public SlotMachinev2(CasinoController konto) {
        this.konto = konto;
        // init IntProperties for spin()
        symbol1 = new SimpleIntegerProperty(0);
        symbol2 = new SimpleIntegerProperty(0);
        symbol3 = new SimpleIntegerProperty(0);
    }

    @SuppressWarnings("BusyWait") // Bei Thread.sleep(), wartet ja nicht aktiv auf etwas
    public void spin(int einsatz) throws IllegalAccessException {
        if (einsatz > konto.getMoney().get()) {
            throw new IllegalAccessException("Sie haben nicht die liquiden Mittel, bitte laden sie ihren Kontostand in unserem Shop auf");
        }
        konto.addMoney(-1 * einsatz);

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
                    rotationSpeed *= 1.1;
                    Thread.sleep((long) rotationSpeed);
                } catch (InterruptedException ignored) {}
            }
            System.out.println("1: " + i);
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
                    rotationSpeed *= 1.1;
                    Thread.sleep((long) rotationSpeed);
                } catch (InterruptedException ignored) {}
            }
            System.out.println("2: " + i);
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
                } catch (InterruptedException ignored) {}
            }
            System.out.println("3: " + i);

            // Gewinn bestimmen
            int s1 = symbol1.get();
            int s2 = symbol2.get();
            int s3 = symbol3.get();

            if (s1 == s2 && s1 == s3) {
                switch (s1) {
                    case 0:
                        berechnen(einsatz, 50); //bei dreimal 7
                        break;
                    case 1:
                        berechnen(einsatz, 25); //bei dreimal Glocke
                        break;
                    default:
                        berechnen(einsatz, 5);  //bei dreimal Frucht
                }
            } else if (s1 >=2 && s2 >= 2 && s3 >= 2 && s1 != s2 && s1 != s3 && s2 != s3) {
                berechnen(einsatz, 2);          // 3 verschiedene Fruchtarten
            } else {
                berechnen(einsatz, 0);          // kein Gewinn
            }
        });

        // Threads starten
        t1.start();
        t2.start();
        t3.start();

    }

    public void berechnen(int einsatz, int multiplikator) {
        if (multiplikator > 0) {
            int gewinn = einsatz * multiplikator;
            konto.addMoney(gewinn);
            System.out.println("Herzlichen Glückwunsch sie haben " + gewinn + " V-Bucks gewonnen");
            System.out.println("Ihr neuer Kontostand beträgt " + konto.getMoney().get() + " V-Bucks");
        } else {
            System.out.println("Niemals Aufgeben");
        }
    }

}