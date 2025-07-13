package src.View_GUI;

import javafx.application.Platform;
import javafx.scene.control.Label;

public class TextLayer {

    public static void displayMessage(String text, String css) {
        Label lbl = new Label(text);
        lbl.setId(css);
        lbl.setWrapText(true);
        lbl.maxWidthProperty().bind(ViewManager.getInstance().windowHeightProperty().divide(17.0 / 16.0));
        lbl.setMinWidth(0);
        new Thread(() -> {
            Platform.runLater(() -> {
                ViewManager.getInstance().getMsgLayer().setCenter(lbl);
                ViewManager.getInstance().getMsgLayer().setVisible(true);
            });
            try {
                Thread.sleep(text.split("\\s").length * 300L); // Anzeigedauer relativ zur Anzahl Worte
            } catch (InterruptedException ignored) {
            }
            Platform.runLater(() -> {
                ViewManager.getInstance().getMsgLayer().setVisible(false);
                ViewManager.getInstance().getMsgLayer().setCenter(null);
            });
        }).start();
    }
}