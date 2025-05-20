public class SlotMachine
{
    SlotMachine()       // Konstruktor
    {

    }

    public void einsatzFestlegen()  //durch Tasten gewünschten Einsatz festlegen;
                                    //überprüfen, ob der aktuelle Betrag den Kontostand üerbschreitet
                                    //bei Roulette auf Felder setzten
    {

    }
    public void spin()      //waretet auf Tasteninput zum starten Grafikanimation
                            //startet berechnen und zufallszahl
                            //schickt an Grafikfutzi
    {

    }

    public void berechnen()  //basierend auf festgelegtem Einsatz und gewünschtem Feld wird der Gewinn berechnet
                             // startet automatisch bei Spin
                            // falls gewonnen berechneten Betrag an CasinoController schicken
    {

    }
    public int zufallszahl()   //berechnet ob gewonnen oder verloren und gibt berechnen faktor zurück
                                //Slotmachine= 3 Zufallszahlen zwischen 1 und 5 jede Steht für ein Symbol, berechnet daaus dann Gewinnmultiplikator
    {
        return 0;
    }

    public void infoZuGrafiken()    //schickt infos an Grafikfenster; gewonnen oder verloren und berechneten Betrag
    {

    }
}

/* Gewinne: 7=25x
            Glocke= 10x
            3 gleiche Früchte= 5x
            3 Füchte(egal welche) = 2x
 */
