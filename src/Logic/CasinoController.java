package src.Logic;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import src.View_GUI.MoneyFrame;

public class CasinoController {

    private static final int[] stakeValues = {1, 2, 5, 10, 25, 100, 500, 1000, 5000, -1};

    public static final int DEFAULT_STAKES = 5;

    private final IntegerProperty money;

    public CasinoController() {
        money = new SimpleIntegerProperty(0);
    }

    public IntegerProperty getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money.set(money);
    }

    public void addMoney(int money) {
        this.money.set(this.money.get() + money);
    }

    /**
     * Wie setMoney, aber mit money animation
     * @param money finaler Geldwert
     */
    public void win(int money) {
        MoneyFrame.stopStakesAnimation(); // bug, wenn man den Einsatz ändert während man gewinnt
        MoneyFrame.animateMoneyFrame(this.money.get(), money);
    }

    /**
     * diese Methode setzt den Einsatz auf den nächsten Wert
     * und aktiviert bei gültiger Eingabe die Einsatz-Animation in {@code MoneyFrame}
     * @param raiseOrReduce Einsatz erhöhen oder erniedrigen?
     * @param stakes wie hoch ist der Einsatz gerade?
     * @return neuer Einsatz
     */
    public static int setStakes(int raiseOrReduce, int stakes) {
        int stakesIndex;
        for(stakesIndex = 0; stakesIndex < stakeValues.length; stakesIndex++) {
            if(stakeValues[stakesIndex] == stakes) {
                break; // setzt stakesIndex auf den momentanen Wert des Einsatzes
            }
        }
        if(stakesIndex == stakeValues.length - 1) {
            return DEFAULT_STAKES; // wenn es den Einsatz nicht in der Liste gibt, sollte aber nicht vorkommen
        }
        if((stakesIndex == stakeValues.length - 2 && raiseOrReduce > 0) || (stakesIndex == 0 && raiseOrReduce < 0)) {
            MoneyFrame.runStakesAnimation(stakes);
            return stakes; // wenn der Einsatz bereits der höchste/niedrigste ist
        }
        int newStakes = raiseOrReduce > 0 ? stakeValues[stakesIndex + 1] : stakeValues[stakesIndex - 1];
        MoneyFrame.runStakesAnimation(newStakes);
        return newStakes;
    }

    public static int getNextStakes(int raiseOrReduce, int stakes) {
        int stakesIndex;
        for(stakesIndex = 0; stakesIndex < stakeValues.length; stakesIndex++) {
            if(stakeValues[stakesIndex] == stakes) {
                break; // setzt stakesIndex auf den momentanen Wert des Einsatzes
            }
        }
        if(stakesIndex == stakeValues.length - 1) {
            return DEFAULT_STAKES; // wenn es den Einsatz nicht in der Liste gibt, sollte aber nicht vorkommen
        }
        if((stakesIndex == stakeValues.length - 2 && raiseOrReduce > 0) || (stakesIndex == 0 && raiseOrReduce < 0)) {
            return stakes; // wenn der Einsatz bereits der höchste/niedrigste ist
        }
        return raiseOrReduce > 0 ? stakeValues[stakesIndex + 1] : stakeValues[stakesIndex - 1];
    }

    /**
     * überprüft ob der vergelichswert den Kontostand überschreitet
     * @param vergleichsWert
     * @return
     */
    public boolean kontostandUeberpruefen(int vergleichsWert){
        return money.get() < vergleichsWert;
    }

}
