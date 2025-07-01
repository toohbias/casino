package src.View_GUI;

import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import src.Logic.CasinoController;

import java.util.Arrays;

/**
 * wrappt die {@code getPane()}-Methode für die Slot Machine
 * und stellt wiederverwendbare Properties bereit
 */
public class SlotView {

    /**
     * IntegerProperties, die an die Bilder gebunden sind.
     * Über die Properties werden die Bilder von {@code SlotMachinev2.spin(int einsatz, ToggleButton slotArm)} gesteuert
     */
    public static IntegerProperty spin1, spin2, spin3;

    /**
     * wie groß die Bilder proportional zum Fenster sind
     */
    private static final int SIGN_SIZE = 15;

    /**
     * wie hoch der Einsatz ist
     */
    private static int stakes = CasinoController.DEFAULT_STAKES;

    /**
     * Slot Machine-Szene
     * hier drin kann der Spieler Slots spielen
     * @return Szene
     */
    public static Node getPane() {
        // init Integer Properties that determine the current Symbol shown on each reel
        spin1 = new SimpleIntegerProperty();
        spin2 = new SimpleIntegerProperty();
        spin3 = new SimpleIntegerProperty();

        // init List of symbols
        final ObservableList<Image> signs = FXCollections.observableArrayList(
                Arrays.asList(
                        new Image("src/assets/Sevensignv2.png"),
                        new Image("src/assets/Bellv2.png"),
                        new Image("src/assets/Bananasv2.png"),
                        new Image("src/assets/Cherriesv2.png"),
                        new Image("src/assets/Grapesv2.png")
                )
        );

        BorderPane root = new BorderPane();

        // show main slot machine
        ImageView img = ViewManager.defaultView(new Image("src/assets/Slotmachine v3.png"), 1.5);
        Label slotMachine = new Label("", img);

        // show slot machine arm
        Image arm = new Image("src/assets/Armv2.png");
        Image armPressed = new Image("src/assets/ArmPressedv3.png");
        ImageView armView = ViewManager.defaultView(arm, 2.5);
        ToggleButton slotArm = new ToggleButton("", armView);
        // when pulled: change Image, disable Button, call method of SlotMachine
        armView.imageProperty().bind(Bindings.when(slotArm.selectedProperty()).then(armPressed).otherwise(arm));
        slotArm.setOnAction(e -> ViewManager.getInstance().leverPulled(stakes, slotArm));
        slotArm.setPadding(Insets.EMPTY);

        // make slotmachine symmetric
        Region slotPlaceholder = new Region();
        slotPlaceholder.prefWidthProperty().bind(slotArm.widthProperty());

        // init symbol on first reel
        ImageView spin1View = ViewManager.defaultView(null, SIGN_SIZE);
        spin1View.imageProperty().bind(Bindings.valueAt(signs, spin1));
        // init symbol on second reel
        ImageView spin2View = ViewManager.defaultView(null, SIGN_SIZE);
        spin2View.imageProperty().bind(Bindings.valueAt(signs, spin2));
        // init symbol on third reel
        ImageView spin3View = ViewManager.defaultView(null, SIGN_SIZE);
        spin3View.imageProperty().bind(Bindings.valueAt(signs, spin3));

        // show reel symbols horizontally
        HBox signBox = new HBox();
        signBox.getChildren().addAll(spin1View, spin2View, spin3View);
        signBox.setAlignment(Pos.CENTER);
        signBox.spacingProperty().bind(ViewManager.getInstance().windowHeightProperty().divide(25));

        // buttons for setting the stake and to confirm
        Image arrowUp = new Image("src/assets/ButtonUpv2.png");
        Image arrowUpPressed = new Image("src/assets/ButtonUpPressedv2.png");
        ImageView arrowUpView = ViewManager.defaultView(arrowUp, 35);
        // when pressed: raise stakes
        arrowUpView.imageProperty().bind(Bindings.when(arrowUpView.pressedProperty()).then(arrowUpPressed).otherwise(arrowUp));
        arrowUpView.setOnMouseClicked(e -> stakes = ViewManager.getInstance().setStake(1, stakes));

        Image arrowDown = new Image("src/assets/ButtonDownv2.png");
        Image arrowDownPressed = new Image("src/assets/ButtonDownPressedv2.png");
        ImageView arrowDownView = ViewManager.defaultView(arrowDown, 35);
        // when pressed: reduce stakes
        arrowDownView.imageProperty().bind(Bindings.when(arrowDownView.pressedProperty()).then(arrowDownPressed).otherwise(arrowDown));
        arrowDownView.setOnMouseClicked(e -> stakes = ViewManager.getInstance().setStake(-1, stakes));

        Image confirm = new Image("src/assets/RoundButtonv2.png");
        Image confirmPressed = new Image("src/assets/RoundButtonPressedv2.png");
        ImageView confirmView = ViewManager.defaultView(confirm, 25);
        // when pressed: confirm stakes
        confirmView.imageProperty().bind(Bindings.when(confirmView.pressedProperty()).then(confirmPressed).otherwise(confirm));
        confirmView.setOnMouseClicked(e -> ViewManager.getInstance().setStake(0, stakes));

        // arranges the raise/reduce steaks buttons horizontally
        VBox raiseAndLowerStakes = new VBox(arrowUpView, arrowDownView);
        raiseAndLowerStakes.spacingProperty().bind(ViewManager.getInstance().windowHeightProperty().divide(50));

        // [ignore] setting up spacing between the steaks buttons and the left border
        Region leftDistPlaceholder = new Region();
        leftDistPlaceholder.setPrefWidth(0);

        // arranges the steaks buttons and confirm button horizontally
        HBox stake = new HBox(leftDistPlaceholder, raiseAndLowerStakes,confirmView);
        stake.spacingProperty().bind(ViewManager.getInstance().windowHeightProperty().divide(12.5));

        // [ignore] makes the whole reels and buttons layer centered on the slot machine
        Region upDownPlaceholder = new Region();
        upDownPlaceholder.prefHeightProperty().bind(raiseAndLowerStakes.heightProperty());

        // arranges the reels and the steak buttons vertically
        VBox slotOverlay = new VBox(upDownPlaceholder, signBox, stake);
        slotOverlay.spacingProperty().bind(ViewManager.getInstance().windowHeightProperty().divide(12.5));
        slotOverlay.setAlignment(Pos.CENTER);

        // show slot machine, arm, reels and steak buttons
        HBox hbox = new HBox(slotPlaceholder, new StackPane(slotMachine, slotOverlay), slotArm);
        hbox.setAlignment(Pos.CENTER);
        root.setBottom(new BorderPane(hbox));

        ViewManager.getInstance().getFXLayer().setCenter(MoneyEffect.getRoot());
        return root;
    }

}
