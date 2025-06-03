package src.ViewManager;

import javax.swing.*;              // Für Fenster und Buttons
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * ViewManager zeigt ein Fenster mit einem Button.
 * Wenn der Button gedrückt wird, wird die Spin-Methode der SlotMachine aufgerufen.
 */
public class ViewManager {

    private SlotMachine slotMachine;  // Verbindung zur Logik-Klasse SlotMachine

    // Konstruktor: bekommt die SlotMachine übergeben
    public ViewManager(SlotMachine slotMachine) {
        this.slotMachine = slotMachine; // Fenster und Button erstellen
    }

    private void initGUI() {
        JFrame frame = new JFrame("Slot Maschine");  // Fenster erstellen
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Fenster schließen beendet Programm

        // Button erstellen mit Namen "SlotMaschine"
        JButton slotButton = new JButton("SlotMaschine");

        // Wenn der Button gedrückt wird, soll die Methode startSpin() ausgeführt werden
        slotButton.addActionListener(new ActionListener() {
        });

        frame.add(slotButton);   // Button zum Fenster hinzufügen
        frame.pack();            // Fenstergröße automatisch anpassen
        frame.setVisible(true);  // Fenster anzeigen
    }


}