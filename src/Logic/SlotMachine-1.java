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
    private ArrayList<SlotMaschineObserver> observers;

    public static int Zufallszahl(int e) {
        Random zufall = new Random();
        int zufallszahl = zufall.nextInt(e);
		return zufallszahl; // Generiert eine Zahl zwischen 0 und 99
    }
    public SlotMachine(CasinoController konto){
        this.konto = konto;
        aktuellerkontostand = 100;
        observers = new ArrayList<>();
        // init IntProperties for spin()
        symbol1 = new SimpleIntegerProperty(0);
        symbol2 = new SimpleIntegerProperty(0);
        symbol3 = new SimpleIntegerProperty(0);
    }
    public void spin(int einsatz) throws IllegalAccessException {     
     if (einsatz <= aktuellerkontostand) {
    	 Thread t3 = new Thread(new Runnable())
	{
	int expected = zufallszahl(1000);
	int rotationSpeed = 5;
	public void run() {
        int Time = 0;
        int i = 0;
        while (Time < expected) {
				i = 0;
            for (i = 0; i < 4; i++) {
                try {
                    Thread.sleep(rotationSpeed);
                } catch (InterruptedException e) {
                    // Handle exception
                }
                Time = Time + rotationSpeed;
            }
        }
        System.out.println(i);
			symbol1.set(i);
    }

    	 Thread t2 = new Thread(new Runnable())
	{
	int expected = zufallszahl(1000);
	int rotationSpeed = 5;
	public void run() {
        int Time = 0;
        int i = 0;
        while (Time < expected + 1000) {
				i = 0;
            for (i = 0; i < 4; i++) {
                try {
                    Thread.sleep(rotationSpeed);
                } catch (InterruptedException e) {
                    // Handle exception
                }
                Time = Time + rotationSpeed;
            }
        }
        System.out.println(i);
		  symbol2.set(i);

		}
    
		 Thread t1 = new Thread(new Runnable())
	{
	int expected = zufallszahl(1000);
	int rotationSpeed = 5;
	public void run() {
        int Time = 0;
        int i = 0;
        while (Time < expected + 2000) {
				i = 0;
            for (i = 0; i < 4; i++) {
                try {
                    Thread.sleep(rotationSpeed);
                } catch (InterruptedException e) {
                    // Handle exception
                }
                Time = Time + rotationSpeed;
            }
        }
        System.out.println(i);
			symbol3.set(i);
   	 }
		t1.start();
		t2.start();
		t3.start();
		try {
    t1.join();
    t2.join();
    t3.join();
	} catch (InterruptedException e) {
	}
		if (symbol1.get() == symbol2.get() && symbol3.get() == symbol1.get())
		switch (	symbol1.get()){
		case 0:  berechnen(einsatz, 50); //be drei mal 7
		break;
		case 1:  berechnen(einsatz, 25);//bei drei Glocke 
		break; 
		case 2:  berechnen(einsatz, 5); 
		break;
		case 3:  berechnen(einsatz, 5); 
		break;
		case 4:  berechnen(einsatz, 5); 
		break;
		}
   	else if (symbol1.get() + symbol2.get() + symbol3.get() >= 3 && symbol1.get() != symbol2.get() 		&& symbol2.get() != symbol3.get() && symbol1.get() != symbol3.get())
		{
		berechnen(einsatz,2);
		}
		else{
		berechnen(einsatz,0);
		}
	}
	else { throw new IllegalAccessException("Sie haben nicht die liquiden Mittel, bitte laden sie ihren Kontostand in unserem Shop auf"); }
    public void berechnen(int einsatz, int multiplikator)      
    {
        if(multiplikator > 0){
        int gewinn = einsatz * multiplikator;
        aktuellerkontostand = aktuellerkontostand + gewinn;
        System.out.println("Herzlichen Glückwunsch sie haben " + gewinn + "Hodenkobolde gewonnen");
        System.out.println("Ihr neuer Kontostand beträgt" + aktuellerkontostand + "Hodenkobolde"); }
        else{
            System.out.println("Niemals Aufgeben");}
    }
   public void addObserver(SlotMaschineObserver observer) {
       observers.add(observer);
   }
}

