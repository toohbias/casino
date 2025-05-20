package src;

import javafx.scene.control.Button;

public class SlotView {

    private Button spinButton;
    private SlotMachine logic;

    public SlotView(SlotMachine logic) {
        this.logic = logic;

        spinButton = new Button("Spin");
        spinButton.setOnAction(e -> {logic.spin();});
    }
}
