package src.Logic;

import java.util.ArrayList;
import java.util.Random;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class SlotMachine {

    Random random = new Random();
    private int aktuellerkontostand;
    int einsatz;
    public IntegerProperty symbol1, symbol2, symbol3;
    private CasinoController konto;
   // private ArrayList<SlotMaschineObserver> observers;


    public SlotMachine(CasinoController konto){
        this.konto = konto;
        aktuellerkontostand = 0;
       // observers = new ArrayList<>();
        // init IntProperties for spin()
        symbol1 = new SimpleIntegerProperty(0);
        symbol2 = new SimpleIntegerProperty(0);
        symbol3 = new SimpleIntegerProperty(0);
    }

    public void spin(int einsatz) throws IllegalAccessException {      //Methode wartet bis Attribute true ist(man darf spinnen) Methode wird am Ende von CasinoController true gesetzt
        if (einsatz <= aktuellerkontostand) {
            symbol1.set(random.nextInt(5));
            symbol2.set(random.nextInt(5));
            symbol3.set(random.nextInt(5));

            if (symbol1 == symbol2) {
                if (symbol1 == symbol3)
                {
                    if (symbol1.get() == 4)
                    {
                        berechnen(einsatz, 50);       //3 siebenen
                    }
                    if (symbol1.get() == 3)
                    {
                        berechnen(einsatz, 25);       //3 glocken
                    }
                    else
                    {
                        berechnen(einsatz, 5);        // 3 gleiche früchte
                    }
                }
            }
            else if (symbol1.get() <= 2 && symbol2.get() <= 2 && symbol3.get() <= 2)   //3 unterschiedliche Früchte/ egal welche Früchte
            {
                berechnen(einsatz,2);
            }

            berechnen(einsatz, 0) ;
        }
        else {
            throw new IllegalAccessException("Sie haben nicht die liquiden Mittel, bitte laden sie ihren Kontostand in unserem Shop auf");
            //Benutzer ruft die Methode auf; throw= werfen eines Fehlers; Methode überprüft die Eingabe; bei gültiger Eingabe läuft der Code weiter, ansonsten neuer Fehler
        }
    }

    //waretet auf Tasteninput zum starten Grafikanimation
    //startet berechnen und Zufallszahl
    //schickt an Grafikfutzi


    public void berechnen(int einsatz, int multiplikator)       //würde berechnen, auf int setzen, war davor noch void // basierend auf festgelegtem Einsatz und gewünschtem Feld wird der Gewinn berechnet
    // startet automatisch bei Spin,
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

   // public int zufallszahl()   //berechnet, ob gewonnen oder verloren und gibt berechnen Faktor zurück
    //Slotmachine= 3 Zufallszahlen zwischen 1 und 5 jede Steht für ein Symbol, berechnet daaus dann Gewinnmultiplikator
    //{
    //}


//   public void infoZuGrafiken(int gewinn)    //schickt Infos an Grafikfenster; gewonnen oder verloren und berechneten Betrag
//   //Observer controller
//   {
//       for (SlotMachineObserver observer : observers) {
//           observer.updateSpielErgebnis(symbol1, symbol2, symbol3, gewinn, aktuellerkontostand);
//
//       }
//
//   }
//
//   public void removeObserver(SlotMachineObserver observer) {
//       observers.remove(observer);
//   }
//


}
/* Gewinne: 7 = 25x
            Glocke= 10x
            3 gleiche Früchte= 5x
            3 Füchte(egal welche) = 2x
 */
