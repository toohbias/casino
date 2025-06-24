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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

import java.util.Arrays;

/**
 * wrappt die {@code getPane()}-Methode für die Slot Machine
 * und stellt wiederverwendbare Properties bereit
 */
public class SlotView {

    public static IntegerProperty spin1, spin2, spin3;
    private static final double SIGN_SIZE = 15;

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
        ImageView img = ViewManager.defaultView(new Image("src/assets/Slotmachinev2.png"), 1.5);
        Label slotMachine = new Label("", img);

        // show slot machine arm
        Image arm = new Image("src/assets/Armv2.png");
        Image armPressed = new Image("src/assets/ArmPressedv3.png");
        ImageView armView = ViewManager.defaultView(arm, 2.5);
        ToggleButton slotArm = new ToggleButton("", armView);
        // when pulled: change Image, disable Button, call method of SlotMachine
        armView.imageProperty().bind(Bindings.when(slotArm.selectedProperty()).then(armPressed).otherwise(arm));
        slotArm.setOnAction(e -> ViewManager.getInstance().leverPulled(50, slotArm)); // TODO: Einsatz bestimmen
        slotArm.setPadding(new Insets(0));

        // invisible slot arm to center the slot machine
        ImageView invArmView = ViewManager.defaultView(arm, 2.5);
        ToggleButton invSlotArm = new ToggleButton("", invArmView);
        invSlotArm.setDisable(true);
        invSlotArm.setVisible(false);
        invSlotArm.setPadding(new Insets(0));

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

        // show slot machine, arm and reels
        HBox hbox = new HBox(invSlotArm, new StackPane(slotMachine, signBox), slotArm);
        hbox.setAlignment(Pos.CENTER);
        root.setBottom(new BorderPane(hbox));

        ViewManager.getInstance().getFXLayer().setCenter(MoneyEffect.getRoot());
        return root;
    }

}
