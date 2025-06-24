package src.Logic;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import src.View_GUI.ViewManager;

public class Shop {
    public static final StringProperty errorMessage = new SimpleStringProperty("");
    public static final IntegerProperty Input = new SimpleIntegerProperty();
    public static void Geldverändern (double money){
        ViewManager.getInstance().getController().addMoney(money);
    }
}


