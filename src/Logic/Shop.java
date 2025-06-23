package src.Logic;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import src.View_GUI.ViewManager;

public class Shop {
    public static final StringProperty Input = new SimpleStringProperty("");
    public static void Geldverändern (){
        double money=0;
        ViewManager.getInstance().getController().addMoney(money);
    }
}


