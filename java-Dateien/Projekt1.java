package Projekt;

import javax.swing.JFrame;

public class Projekt1 extends JFrame {

    public static void main(String[] args) {
        new Projekt1("Erfassung von Rechnungen");
    }

    public Projekt1(String titel) {
        super(titel);
        setBounds(400, 200, 400, 580); // Größe des Fensters anpassen, um alle Felder aufzunehmen
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        add(new Projektpanel()); // Hinzufügen des Panels, das die Felder und Beschriftungen enthält
        setVisible(true); // Fenster sichtbar machen
    }
}
