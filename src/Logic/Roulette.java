package src.Logic;

import java.util.Random;

public class Roulette {

    int aktuellerkontostand;
    int[] gerade = {2, 4, 6, 8, 10, 12, 14, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36};
    int[] ungerade = {1, 3, 5, 7, 9, 11, 13, 15, 17, 19, 21, 23, 25, 27, 29, 31, 33, 35};
    int [] schwarz = {15, 4, 2, 17, 6, 13, 11, 8, 10, 24, 33, 20, 31, 22, 29, 28, 35, 26};
    int [] rot = {32, 19, 21, 25, 34, 27, 36, 36, 30, 23, 5, 16, 1, 14, 9, 18, 7, 12, 3};
    int zufallsZahl;

    Random random = new Random();

    Roulette()       // Konstruktor
    {
    aktuellerkontostand = 0;
    }

    //TODO remove
    public boolean einsatzFestlegen(int i) {
        if (i <= 0) {
            System.out.println("Der Einsatz muss größer als 0 sein.");
            return false;
        }

        if (i > aktuellerkontostand) {
            System.out.println("Nicht genügend V-Bucks! Ihr aktueller Kontostand beträgt: " + aktuellerkontostand);
            return false;
        }

        System.out.println("Einsatz von " + i + " V-Bucks wurde akzeptiert.");
        return true;
    }

    /**
     * wird bei Tasteninput aufgerufen
     *
     * //@param moneyInput
     * @return
     */
    public void spin(int einsatz, int tipp, String farbe, String geradeUngerade)
    //methode wartet bis Attribute true ist(man darf spinnen) Methode wird am Ende von CasinoController true gesetzt
    //waretet auf Tasteninput zum starten Grafikanimation
    //startet berechnen und zufallszahl
    //schickt an Grafikfutzi

    {
        if (einsatzFestlegen(einsatz)) {
            int zufaelligeZahl = random.nextInt(37);

            if (tipp == zufaelligeZahl)
            {
                berechnen(einsatz, 35);
            }
            if (farbe.equals("rot") || zufaelligeZahl == rot[zufaelligeZahl] ){
                berechnen(einsatz, 2);
            }
            if (farbe.equals("schwarz") || zufaelligeZahl == schwarz[zufaelligeZahl]){
                berechnen(einsatz, 2);
            }
            if (geradeUngerade.equals("gerade") || zufaelligeZahl == gerade[zufaelligeZahl]){
                berechnen(einsatz, 2);
            }
            if (geradeUngerade.equals("ungerade") || zufaelligeZahl == ungerade[zufaelligeZahl]){
                berechnen(einsatz, 2);
            }
            else{
                berechnen(einsatz, 0);
            }
        }
    }
        public void berechnen (int einsatz, int multiplikator)  //basierend auf festgelegtem Einsatz und gewünschtem Feld wird der Gewinn berechnet
        // startet automatisch bei Spin
        // falls gewonnen berechneten Betrag an CasinoController schicken
        {

                if(multiplikator > 0){
                    int gewinn = einsatz * multiplikator;
                    aktuellerkontostand = aktuellerkontostand + gewinn;
                    System.out.println("Herzlichen Glückwunsch sie haben " + gewinn + "V-Bucks gewonnen");
                    System.out.println("Ihr neuer Kontostand beträgt" + aktuellerkontostand + "V-Bucks"); }
                else{
                    System.out.println("Niemals Aufgeben");}

        }
        public double zufallszahl ()
    //berechnet ob gewonnen oder verloren und gibt berechnen faktor zurück
        //Slotmachine= 3 Zufallszahlen zwischen 1 und 5 jede Steht für ein Symbol, berechnet daaus dann Gewinnmultiplikator
        {
            return Math.random() * 3.0;
        }

        public void infoZuGrafiken ()
    //schickt infos an Grafikfenster; gewonnen oder verloren und berechneten Betrag
        //Observer controller

        //upadteGraphics(int drawnNumber, double winSum)
        {

        }
    }


/* Gewinn bei src.Logic.Roulette schwarz/rot und gerade/ungerade 2x
bei einzelner Zahl x35

 */


