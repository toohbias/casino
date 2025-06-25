package src.View_GUI;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

/**
 * Wrapper für kleinere UI-Elemente, für die sich eine ganze Klasse nicht wirklich lohnt.
 * CasinoView beinhaltet außerdem das Main Menu
 */
public class CasinoView {

    /**
     * Der Inhalt des Start-Menus, wo man das Spiel auswählen kann
     * @return Inhalt des Menus
     */
    public static Node getPane() {
        BorderPane root = new BorderPane();

        Label lblHeading = new Label("Spielauswahl");
        lblHeading.setPadding(new Insets(50, 0, 0, 0));

        // dumme animation, vielleicht integrieren mit neuem gif
        ImageView casinoLogo = new ImageView(new Image("src/assets/animation.gif"));

        root.setTop(new BorderPane(lblHeading));

        HBox selection = new HBox();

        // show slot machine selection button
        ImageView img = ViewManager.defaultView(new Image("src/assets/Slotmachinev2.png"), 3);
        Button slotImage = new Button("Slots", img);
        slotImage.setContentDisplay(ContentDisplay.TOP);
        slotImage.setOnAction(e -> ViewManager.getInstance().setView(ViewManager.SLOT_VIEW));
        selection.getChildren().add(slotImage);

        // show roulette selection button TODO: change Image
        ImageView img2 = ViewManager.defaultView(new Image("src/assets/Roulette Desk.png"), 3);
        Button rouletteImage = new Button("Roulette", img2);
        rouletteImage.setContentDisplay(ContentDisplay.TOP);
        rouletteImage.setOnAction(e -> ViewManager.getInstance().setView(ViewManager.ROULETTE_VIEW));
        selection.getChildren().add(rouletteImage);

        selection.spacingProperty().bind(ViewManager.getInstance().windowWidthProperty().divide(10));
        selection.setAlignment(Pos.CENTER);

        root.setCenter(selection);

        return root;
    }

    /**
     * Zurück-Knopf
     * wird nur einmal während der initialisierung des Spiels in {@code ViewManager.getInstance.setStage(Stage stage)} aufgerufen
     * und danach bei jedem {@code ViewManager.getInstance().setView(int view)} entweder angezeigt oder unsichtbar gemacht
     * @return Knopf mit passender margin
     */
    public static Node getBackButton() {
        ImageView backView = ViewManager.defaultView(new Image("src/assets/Pfeilzurück.png"), 10);
        Button backButton = new Button("", backView);
        backButton.setOnAction(e -> ViewManager.getInstance().setView(ViewManager.MAIN_MENU));
        BorderPane wrapper = new BorderPane(backButton);
        wrapper.prefWidthProperty().bind(wrapper.heightProperty()
                .subtract(backButton.heightProperty())
                .add(backButton.widthProperty()));
        return wrapper;
    }

    /**
     * Shop-Knopf
     * wird nur einmal während der initialisierung des Spiels in {@code ViewManager.getInstance.setStage(Stage stage)} aufgerufen
     * und danach bei jedem {@code ViewManager.getInstance().setView(int view)} entweder angezeigt oder unsichtbar gemacht
     * @return Knopf mit passender margin
     */
    public static Node getShopButton() {
        // Shop Button
        ImageView shopView = ViewManager.defaultView(new Image("src/assets/ShopSymbol.png"), 8);
        Button shopButton = new Button("", shopView);
        shopButton.setOnAction(e -> ViewManager.getInstance().setView(ViewManager.SHOP_VIEW));
        BorderPane wrapper = new BorderPane(shopButton);
        wrapper.prefWidthProperty().bind(wrapper.heightProperty()
                .subtract(shopButton.heightProperty())
                .add(shopButton.widthProperty()));
        return wrapper;
    }
}

