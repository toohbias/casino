package src.Logic;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import src.View_GUI.MoneyFrame;

public class CasinoController {

    private static final double[] stakeValues = {1, 2, 5, 10, 25, 50, 100, 250, 500, 1000, 2000, 5000, -1};

    private final DoubleProperty money;

    public CasinoController() {
        money = new SimpleDoubleProperty(0);
    }

    public DoubleProperty getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money.set(money);
    }

    public void addMoney(double money) {
        this.money.set(this.money.get() + money);
    }

    /**
     * Wie setMoney, aber mit money animation
     * @param money finaler Geldwert
     */
    public void win(double money) {
        MoneyFrame.animateMoneyFrame(this.money.get(), money);
        this.money.set(money);
    }

    /**
     * diese Methode setzt den Einsatz auf den nächsten Wert
     * und aktiviert bei gültiger Eingabe die Einsatz-Animation in {@code MoneyFrame}
     * @param raiseOrReduce Einsatz erhöhen oder erniedrigen?
     * @param stakes wie hoch ist der Einsatz gerade?
     * @return neuer Einsatz
     */
    public static double setStakes(int raiseOrReduce, double stakes) {
        int stakesIndex;
        for(stakesIndex = 0; stakesIndex < stakeValues.length; stakesIndex++) {
            if(stakeValues[stakesIndex] == stakes) {
                break; // setzt stakesIndex auf den momentanen Wert des Einsatzes
            }
        }
        if(stakesIndex == stakeValues.length - 1) {
            return stakes; // wenn es den Einsatz nicht in der Liste gibt, sollte aber nicht vorkommen
        }
        if((stakesIndex == stakeValues.length - 2 && raiseOrReduce > 0) || (stakesIndex == 0 && raiseOrReduce < 0)) {
            MoneyFrame.runStakesAnimation(stakes);
            return stakes; // wenn der Einsatz bereits der höchste/niedrigste ist
        }
        double newStakes = raiseOrReduce > 0 ? stakeValues[stakesIndex + 1] : stakeValues[stakesIndex - 1];
        MoneyFrame.runStakesAnimation(newStakes);
        return newStakes;
    }

    /**
     * überprüft ob der vergelichswert den Kontostand überschreitet
     * @param vergleichsWert
     * @return
     */
    public boolean kontostandUeberpruefen(double vergleichsWert){
        return money.get() < vergleichsWert;
    }

}
