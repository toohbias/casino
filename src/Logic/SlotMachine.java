package src.Logic;

public class SlotMachine {
    private int aktuellerkontostand;
    int einsatz;

    public SlotMachine()       // Konstruktor
    {
        aktuellerkontostand = 0;
    }

    public void spin(int einsatz) throws IllegalAccessException {      //Methode wartet bis Attribute true ist(man darf spinnen) Methode wird am Ende von CasinoController true gesetzt
        if (einsatz < aktuellerkontostand) {
            zufallszahl();
            berechnen();
        } else {
            throw new IllegalAccessException("Sie haben nicht die liquiden Mittel, bitte laden sie ihren Kontostand in unserem Shop auf");
            //Benutzer ruft die Methode auf; throw= werfen eines Fehlers; Methode überprüft die Eingabe; bei gültiger Eingabe läuft der Code weiter, ansonsten neuer Fehler
        }
    }

    //waretet auf Tasteninput zum starten Grafikanimation
    //startet berechnen und Zufallszahl
    //schickt an Grafikfutzi


    public void berechnen()     //basierend auf festgelegtem Einsatz und gewünschtem Feld wird der Gewinn berechnet
    // startet automatisch bei Spin,
    // falls gewonnen berechneten Betrag an CasinoController schicken
    {

    }

    public int zufallszahl()   //berechnet, ob gewonnen oder verloren und gibt berechnen Faktor zurück
    //Slotmachine= 3 Zufallszahlen zwischen 1 und 5 jede Steht für ein Symbol, berechnet daaus dann Gewinnmultiplikator
    {
        return 0;
    }

    public void infoZuGrafiken()    //schickt Infos an Grafikfenster; gewonnen oder verloren und berechneten Betrag
    //Observer controller
    {

    }

}

/* Gewinne: 7 = 25x
            Glocke= 10x
            3 gleiche Früchte= 5x
            3 Füchte(egal welche) = 2x
 */
