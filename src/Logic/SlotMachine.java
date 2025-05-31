package src.Logic;

import java.util.Random;

public class SlotMachine {

    Random random = new Random();
    private int aktuellerkontostand;
    int einsatz;

    public SlotMachine()       // Konstruktor
    {
        aktuellerkontostand = 0;
    }

    public void spin(int einsatz) throws IllegalAccessException {      //Methode wartet bis Attribute true ist(man darf spinnen) Methode wird am Ende von CasinoController true gesetzt
        if (einsatz <= aktuellerkontostand) {
            zufallszahl();      //brauchen wir Zufallszahl hier? würde die in die berechnen Methode packen
            berechnen(einsatz);
        } else {
            throw new IllegalAccessException("Sie haben nicht die liquiden Mittel, bitte laden sie ihren Kontostand in unserem Shop auf");
            //Benutzer ruft die Methode auf; throw= werfen eines Fehlers; Methode überprüft die Eingabe; bei gültiger Eingabe läuft der Code weiter, ansonsten neuer Fehler
        }
    }

    //waretet auf Tasteninput zum starten Grafikanimation
    //startet berechnen und Zufallszahl
    //schickt an Grafikfutzi


    public void berechnen(int einsatz)       //würde berechnen, auf int setzen, war davor noch void // basierend auf festgelegtem Einsatz und gewünschtem Feld wird der Gewinn berechnet
    // startet automatisch bei Spin,
    // falls gewonnen berechneten Betrag an CasinoController schicken
    {
        int gewinn = einsatz * zufallszahl();
        aktuellerkontostand = aktuellerkontostand + gewinn;
        System.out.println("Herzlichen Glückwunsch sie haben " + gewinn + "V-Bucks gewonnen");
        System.out.println("Ihr neuer Kontostand beträgt" + aktuellerkontostand + "V-Bucks");
    }

    public int zufallszahl()   //berechnet, ob gewonnen oder verloren und gibt berechnen Faktor zurück
    //Slotmachine= 3 Zufallszahlen zwischen 1 und 5 jede Steht für ein Symbol, berechnet daaus dann Gewinnmultiplikator
    {
        int symbol1 = random.nextInt(5);
        int symbol2 = random.nextInt(5);
        int symbol3 = random.nextInt(5);

        if (symbol1 == symbol2) {
            if (symbol1 == symbol3)
            {
                if (symbol1 == 4)
                {
                    return 50;       //3 siebenen
                }
                if (symbol1 == 3)
                {
                    return 25;       //3 glocken
                }
                else
                {
                    return 5;        // 3 gleiche früchte
                }
            }
        }
        else if (symbol1 <= 2 && symbol2 <= 2 && symbol3 <= 2)   //3 unterschiedliche Früchte/ egal welche Früchte
        {
            return 2;
        }

        return 0;
    }


    public void infoZuGrafiken(int gewinn)    //schickt Infos an Grafikfenster; gewonnen oder verloren und berechneten Betrag
    //Observer controller
    {
        for (SlotMachineObserver observer : observers) {
            observer.updateSpielErgebnis(symbol1, symbol2, symbol3, gewinn, aktuellerkontostand);

        }

    }

    public void removeObserver(SlotMachineObserver observer) {
        observers.remove(observer);
    }

    public void addObserver(SlotMachineObserver observer) {
        observers.add(observer);
    }

}
/* Gewinne: 7 = 25x
            Glocke= 10x
            3 gleiche Früchte= 5x
            3 Füchte(egal welche) = 2x
 */
