package src.View_GUI;

import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;

public class CasinoView {

    /**
     * Der Inhalt des Start-Menus, wo man das Spiel auswählen kann
     * @return Inhalt des Menus
     */
    public static Node getPane() {
        BorderPane root = new BorderPane();

        Label lblHeading = new Label("Spielauswahl");
        lblHeading.setPadding(new Insets(50, 0, 0, 0));
        root.setTop(new BorderPane(lblHeading));

        HBox selection = new HBox();

        // show slot machine selection button
        ImageView img = ViewManager.defaultView(new Image("src/assets/Slotmachinev2.png"), 3);
        Button slotImage = new Button("Slots", img);
        slotImage.setContentDisplay(ContentDisplay.TOP);
        slotImage.setOnAction(e -> ViewManager.getInstance().setView(ViewManager.SLOT_VIEW));
        selection.getChildren().add(slotImage);

        // show roulette selection button TODO: change Image
        ImageView img2 = ViewManager.defaultView(new Image("src/assets/Slotmachinev2.png"), 3);
        Button rouletteImage = new Button("Roulette", img2);
        rouletteImage.setContentDisplay(ContentDisplay.TOP);
        rouletteImage.setOnAction(e -> ViewManager.getInstance().setView(ViewManager.ROULETTE_VIEW));
        selection.getChildren().add(rouletteImage);

        selection.spacingProperty().bind(ViewManager.getInstance().windowWidthProperty().divide(10));
        selection.setAlignment(Pos.CENTER);

        root.setCenter(selection);

        return root;
    }

    public static Node getMoneyFrame(DoubleProperty money) {
        BorderPane root = new BorderPane();
        Label moneyLbl = new Label();
        moneyLbl.textProperty().bind(money.asString());
        moneyLbl.setAlignment(Pos.CENTER);
        moneyLbl.getStyleClass().clear();
        moneyLbl.setGraphic(ViewManager.defaultView(new Image("src/assets/Money Framev2.png"), 5));
        moneyLbl.setContentDisplay(ContentDisplay.CENTER);
        moneyLbl.styleProperty().bind(Bindings.format("-fx-font-size: %.2fpt;", ViewManager.getInstance().windowHeightProperty().divide(5).divide(moneyLbl.textProperty().length())));
        root.setCenter(new StackPane(moneyLbl));
        return root;
    }
}

