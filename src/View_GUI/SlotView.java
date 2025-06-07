package src.View_GUI;

import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import src.Logic.SlotMachine;
import src.Logic.SlotMaschineObserver;

import java.util.Arrays;
import java.util.List;

public class SlotView implements SlotMaschineObserver {

    private Button spinButton;
    private SlotMachine logic;
    public static IntegerProperty spin1, spin2, spin3;
    private static final double SIGN_SIZE = 12.5;

    public SlotView(SlotMachine logic) {
        this.logic = logic;
        logic.addObserver(this);

        spinButton = new Button("Spin");
        spinButton.setOnAction((event -> {
            try {
                logic.spin(1);
            } catch (IllegalAccessException e) {
                System.out.println(e.getMessage());
            }
        }));
    }

    public static Node getPane() {
        // init List of symbols
        List<Image> signs = Arrays.asList(
                new Image("src/assets/Bananasv2.png"),
                new Image("src/assets/Bell.png"),
                new Image("src/assets/Cherries.png"),
                new Image("src/assets/Grapes.png"),
                new Image("src/assets/Sevensign.png")
        );
        // init Integer Properties that determine the current Symbol shown on each reel TODO: init in ViewManager
        spin1 = new SimpleIntegerProperty(0);
        spin2 = new SimpleIntegerProperty(1);
        spin3 = new SimpleIntegerProperty(2);

        BorderPane root = new BorderPane();

        // show main slot machine
        ImageView img = ViewManager.defaultView(new Image("src/assets/Slotmachinev2.png"), 1.25);
        Label slotMachine = new Label("", img);

        // show slot machine arm
        Image arm = new Image("src/assets/Arm.png");
        Image armPressed = new Image("src/assets/ArmPressedv2.png");
        ImageView armView = ViewManager.defaultView(arm, 2);
        ToggleButton slotArm = new ToggleButton("", armView);
        // when pulled: change Image, disable Button, call method of SlotMachine
        armView.imageProperty().bind(Bindings.when(slotArm.selectedProperty()).then(armPressed).otherwise(arm));
        slotArm.setOnAction(e -> {
            slotArm.setDisable(true);
            ViewManager.getInstance().leverPulled(0); //TODO: Einsatz bestimmen
        });

        // init symbol on first reel
        ImageView spin1View = ViewManager.defaultView(null, SIGN_SIZE);
        ObservableList<Image> sign1 = FXCollections.observableArrayList(signs);
        spin1View.imageProperty().bind(Bindings.valueAt(sign1, spin1));
        // init symbol on second reel
        ImageView spin2View = ViewManager.defaultView(null, SIGN_SIZE);
        ObservableList<Image> sign2 = FXCollections.observableArrayList(signs);
        spin2View.imageProperty().bind(Bindings.valueAt(sign2, spin2));
        // init symbol on third reel
        ImageView spin3View = ViewManager.defaultView(null, SIGN_SIZE);
        ObservableList<Image> sign3 = FXCollections.observableArrayList(signs);
        spin3View.imageProperty().bind(Bindings.valueAt(sign3, spin3));

        // show reel symbols horizontally
        HBox signBox = new HBox();
        signBox.getChildren().addAll(spin1View, spin2View, spin3View);
        signBox.setAlignment(Pos.CENTER);
        signBox.spacingProperty().bind(ViewManager.getInstance().windowWidthProperty().divide(25));

        // show slot machine, arm and reels
        HBox hbox = new HBox(new StackPane(slotMachine, signBox), slotArm);
        hbox.setAlignment(Pos.CENTER);
        root.setBottom(new BorderPane(hbox));

        // show money frame
        root.setTop(
            new BorderPane(
                new Label(
                    "", ViewManager.defaultView(new Image("src/assets/Money Frame.png"), 7.5)
                )
            )
        );

        return root;
    }

    //TODO
    @Override
    public void updateSpielErgebnis(int symbol1, int symbol2, int symbol3, double gewinn) {

    }

    //TODO
    @Override
    public void fehler(String fehlermeldung) {

    }
}
