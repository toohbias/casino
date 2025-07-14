package src.View_GUI;

import javafx.application.Platform;
import javafx.scene.control.Label;

/**
 * wrappt die {@code displayMessage(String text, String css)}-Methode, die von
 * {@code ViewManager.getInstance.displayInfoMessage(String text)} und {@code ViewManager.getInstance.displayErrorMessage(String text)}
 * nochmal gewrappt wird.
 * Damit lassen sich Info- bzw. Fehlermeldungen global anzeigen lassen.
 */
public class TextLayer {

    /**
     * Erzeugt eine Meldung auf dem {@code ViewManager.msglayer}
     * @param text zu zeigender Text
     * @param css css-ID: {@code info-pane} oder {@code error-pane}
     */
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