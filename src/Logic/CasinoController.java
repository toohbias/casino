package src.Logic;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

public class CasinoController {
    private DoubleProperty money;
    public CasinoController() {
        money = new SimpleDoubleProperty(0);
    }
    public CasinoController(double startMoney) {
        money = new SimpleDoubleProperty(startMoney);
    }

    public DoubleProperty getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money.set(money);
    }

    public void addMoney(double money) {
        this.money.add(money);
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
