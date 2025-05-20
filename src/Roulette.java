package src;

public class Roulette {


    Roulette()       // Konstruktor
    {

    }

    //TODO remove
    public void einsatzFestlegen()  //durch Tasten gewünschten Einsatz festlegen;
    //überprüfen, ob der aktuelle Betrag den Kontostand üerbschreitet
    //bei src.Roulette auf Felder setzten
    {

    }

    /**
     * wird bei Tasteninput aufgerufen
     * @param moneyInput
     * @return
     */
    public boolean spin(double moneyInput)
    {
        //methode wartet bis Attribute true ist(man darf spinnen) Methode wird am Ende von CasinoController true gesetzt
        //waretet auf Tasteninput zum starten Grafikanimation
        //startet berechnen und zufallszahl
        //schickt an Grafikfutzi

        return false;
    }

    public void berechnen()  //basierend auf festgelegtem Einsatz und gewünschtem Feld wird der Gewinn berechnet
    // startet automatisch bei Spin
    // falls gewonnen berechneten Betrag an CasinoController schicken
    {

    }
    public double zufallszahl()   //berechnet ob gewonnen oder verloren und gibt berechnen faktor zurück
    //Slotmachine= 3 Zufallszahlen zwischen 1 und 5 jede Steht für ein Symbol, berechnet daaus dann Gewinnmultiplikator
    {
        return Math.random()*3.0;
    }

    public void infoZuGrafiken()    //schickt infos an Grafikfenster; gewonnen oder verloren und berechneten Betrag
    //Observer controller

            //upadteGraphics(int drawnNumber, double winSum)
    {

    }
}

/* Gewinn bei src.Roulette schwarz/rot und gerade/ungerade 2x
bei einzelner Zahl x35

 */


