package src.View_GUI;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;

public class RouletteView {

    public static Node getPane() {

        BorderPane root = new BorderPane();

        //Rückbotton
        ImageView zurckImg = ViewManager.defaultView(new Image("src/assets/Pfeilzurück.png"), 3);
        zurckImg.setFitWidth(75);
        Button zurueck = new Button("", zurckImg);
        zurueck.setContentDisplay(ContentDisplay.TOP);
        zurueck.setOnAction(e -> {ViewManager.getInstance().setView(1);}); //Was passiert wenn man den Button drückt
        root.setLeft(zurueck);
        return root;
    }

}
