package src.View_GUI;

import javafx.scene.control.Button;
import src.Logic.SlotMachine;

public class SlotView {

    private Button spinButton;
    private SlotMachine logic;

    public SlotView(SlotMachine logic) {
        this.logic = logic;

        spinButton = new Button("Spin");
        spinButton.setOnAction((event -> {
            try {
                logic.spin(1);
            } catch (IllegalAccessException e) {
                System.out.println(e.getMessage());
            }
        }));
    }
}
