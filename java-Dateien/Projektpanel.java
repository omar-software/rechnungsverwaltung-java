package Projekt;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Projektpanel extends JPanel implements ActionListener {

    /* ----------------------------* Konstanten* ---------------------------- */

    // JDBC - Java Database Connectivity
    private static final String TREIBER = "com.mysql.cj.jdbc.Driver";

    // Datenbank-Verbindungsstring
    private static final String URI = "jdbc:mysql://127.0.0.1/rechnungsverwaltung?user=javauser&password=profil";

    /* ----------------------------* Eigenschaften* ---------------------------- */
    
    /*
     * Formularfelder
     */
    private JButton btnSave = new JButton("speichern");
    private JButton btnCancel = new JButton("abbrechen");

    // Eingabefelder
    private JTextField tfRechnungsnummer = new JTextField();  
    private JTextField tfDatum = new JTextField();             
    private JTextField tfNachname = new JTextField();              
    private JTextField tfVorname = new JTextField();           
    private JTextField tfStrasse = new JTextField();           
    private JTextField tfPLZ = new JTextField();               
    private JTextField tfOrt = new JTextField();               
    private JTextField tfProjekt = new JTextField();           
    private JTextField tfBeschreibung = new JTextField();      
    private JTextField tfAnzahlArbeitsstunden = new JTextField(); 
    private JTextField tfAnzahlMaterialkosten = new JTextField(); 
    private JTextField tfAnzahlAnfahrt = new JTextField(); 

    // Beschriftungen
    private JLabel[] labels = {
        new JLabel("Rechnungsnummer:"),
        new JLabel("Datum:"),
        new JLabel("Nachname:"),
        new JLabel("Vorname:"),
        new JLabel("Straße:"),
        new JLabel("PLZ:"),
        new JLabel("Ort:"),
        new JLabel("Projekt:"),
        new JLabel("Beschreibung:"),
        new JLabel("Arbeitsstunden:"),
        new JLabel("Materialkosten:"),
        new JLabel("Anfahrt (km):")
    };

    /* ---------------------------- * Konstruktor * ---------------------------- */

    public Projektpanel() { 
        setLayout(null); // Layout auf null setzen
        int yPosition = 20; // vertikale Startposition

        for (int i = 0; i < labels.length; i++) {
            labels[i].setBounds(20, yPosition, 150, 25); // Position der Beschriftung festlegen
            add(labels[i]); // Beschriftung hinzufügen
            JTextField textField = getTextField(i); // passendes Textfeld abrufen
            textField.setBounds(180, yPosition, 150, 25); // Position des Textfelds festlegen
            add(textField); // Textfeld hinzufügen
            yPosition += 40; // vertikal für jedes Element erhöhen
        }

        btnSave.setBounds(200, yPosition, 100, 25); // Position des Speichern-Buttons festlegen
        add(btnSave); // Speichern-Button hinzufügen
        btnSave.addActionListener(this); // Button-Ereignis verknüpfen

        btnCancel.setBounds(80, yPosition, 100, 25); // Position des Abbrechen-Buttons festlegen
        add(btnCancel); // Abbrechen-Button hinzufügen
        btnCancel.addActionListener(this); // Button-Ereignis verknüpfen

        // DB-Treiber registrieren
        try {
            Class.forName(TREIBER);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /* ----------------------------* Sonstige Methoden* ---------------------------- */

    @Override
    public void actionPerformed(ActionEvent e) { 
        if (e.getSource() == btnSave) {
            saveData(); // Speichermethode aufrufen
        } else if (e.getSource() == btnCancel) {
            formulareLeeren(); // Felder leeren 
        }
    }

    private void saveData() {
        // Überprüfen, ob alle Felder ausgefüllt sind
        if (tfRechnungsnummer.getText().isEmpty() || tfDatum.getText().isEmpty() || 
            tfNachname.getText().isEmpty() || tfVorname.getText().isEmpty() || 
            tfStrasse.getText().isEmpty() || tfPLZ.getText().isEmpty() || 
            tfOrt.getText().isEmpty() || tfProjekt.getText().isEmpty() || 
            tfBeschreibung.getText().isEmpty() || tfAnzahlArbeitsstunden.getText().isEmpty() || 
            tfAnzahlMaterialkosten.getText().isEmpty() || tfAnzahlAnfahrt.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Bitte füllen Sie alle Felder aus.");
            return;
        }
    
        // Überprüfen, ob das Datum im richtigen Format ist
        LocalDate date;
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            date = LocalDate.parse(tfDatum.getText(), formatter);
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "Bitte geben Sie ein gültiges Datum ein (dd.MM.yyyy).");
            return;
        }
    
        // Überprüfen, ob die Anzahl der Kostenarten gültig ist (keine negativen Zahlen)
        int anzahlArbeitsstunden, anzahlMaterialkosten, anzahlAnfahrt;
        try {
            anzahlArbeitsstunden = Integer.parseInt(tfAnzahlArbeitsstunden.getText());
            anzahlMaterialkosten = Integer.parseInt(tfAnzahlMaterialkosten.getText());
            anzahlAnfahrt = Integer.parseInt(tfAnzahlAnfahrt.getText());
            if (anzahlArbeitsstunden < 0 || anzahlMaterialkosten < 0 || anzahlAnfahrt < 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Bitte geben Sie gültige Zahlen ein.");
            return;
        }
    
        try (Connection conn = DriverManager.getConnection(URI)) {
            // SQL-Abfrage zum Einfügen in die Tabelle kunden
            String sqlKunden = "INSERT INTO kunden (nachname, vorname, strasse, plz, ort) VALUES (?, ?, ?, ?, ?)";
            // SQL-Abfrage zum Einfügen in die Tabelle rechnung
            String sqlRechnung = "INSERT INTO rechnung (rechnungsnummer, datum, kunden_id, projekt, beschreibung, gesamt_betrag) " +
                                 "VALUES (?, ?, ?, ?, ?, ?)";
    
            // Daten in die Tabelle kunden einfügen
            PreparedStatement stmtKunden = conn.prepareStatement(sqlKunden, PreparedStatement.RETURN_GENERATED_KEYS);
            stmtKunden.setString(1, tfNachname.getText());
            stmtKunden.setString(2, tfVorname.getText());
            stmtKunden.setString(3, tfStrasse.getText());
            stmtKunden.setString(4, tfPLZ.getText());
            stmtKunden.setString(5, tfOrt.getText());
            stmtKunden.executeUpdate();
    
            // Abrufen der generierten kunden_id für die weitere Verwendung
            ResultSet rs = stmtKunden.getGeneratedKeys();
            int kundenId = 0;
            if (rs.next()) {
                kundenId = rs.getInt(1);
            }
    
            // Berechnen des Gesamtbetrags
            double gesamtBetrag = 0;
            PreparedStatement stmtKosten = conn.prepareStatement("SELECT einzelpreis FROM kostenarten WHERE name = ?");
            
            stmtKosten.setString(1, "Arbeitsstunden");
            ResultSet rsKosten = stmtKosten.executeQuery();
            if (rsKosten.next()) {
                gesamtBetrag += rsKosten.getDouble(1) * anzahlArbeitsstunden;
            }
            stmtKosten.setString(1, "Materialkosten");
            rsKosten = stmtKosten.executeQuery();
            if (rsKosten.next()) {
                gesamtBetrag += rsKosten.getDouble(1) * anzahlMaterialkosten;
            }
            stmtKosten.setString(1, "Anfahrt, km-Pauschale");
            rsKosten = stmtKosten.executeQuery();
            if (rsKosten.next()) {
                gesamtBetrag += rsKosten.getDouble(1) * anzahlAnfahrt;
            }
    
            // Konvertieren des Datums in das SQL-Format für die Datenbank
            String formattedDate = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    
            // Daten in die Tabelle rechnung einfügen
            PreparedStatement stmtRechnung = conn.prepareStatement(sqlRechnung, PreparedStatement.RETURN_GENERATED_KEYS);
            stmtRechnung.setString(1, tfRechnungsnummer.getText());
            stmtRechnung.setString(2, formattedDate);
            stmtRechnung.setInt(3, kundenId);
            stmtRechnung.setString(4, tfProjekt.getText());
            stmtRechnung.setString(5, tfBeschreibung.getText());
            stmtRechnung.setDouble(6, gesamtBetrag);
            stmtRechnung.executeUpdate();
    
            // Abrufen der generierten rechnungs_id für die weitere Verwendung
            ResultSet rsRechnung = stmtRechnung.getGeneratedKeys();
            int rechnungsId = 0;
            if (rsRechnung.next()) {
                rechnungsId = rsRechnung.getInt(1);
            }
    
            // Kosten in die Tabelle rechnungskosten einfügen
            PreparedStatement stmtRechnungskosten = conn.prepareStatement("INSERT INTO rechnungskosten (rechnungs_id, kostenart_id, anzahl, gesamtpreis) VALUES (?, ?, ?, ?)");
    
            // Für Arbeitsstunden
            stmtRechnungskosten.setInt(1, rechnungsId);  // Rechnungs-ID
            stmtRechnungskosten.setInt(2, 1);  // Kostenart-ID für Arbeitsstunden
            stmtRechnungskosten.setInt(3, anzahlArbeitsstunden);  // Anzahl Arbeitsstunden
            stmtRechnungskosten.setDouble(4, anzahlArbeitsstunden * 50.00);  // Gesamtpreis für Arbeitsstunden
            stmtRechnungskosten.executeUpdate();
    
            // Für Materialkosten
            stmtRechnungskosten.setInt(1, rechnungsId);  // Rechnungs-ID
            stmtRechnungskosten.setInt(2, 2);  // Kostenart-ID für Materialkosten
            stmtRechnungskosten.setInt(3, anzahlMaterialkosten);  // Anzahl Materialkosten
            stmtRechnungskosten.setDouble(4, anzahlMaterialkosten * 200.00);  // Gesamtpreis für Materialkosten
            stmtRechnungskosten.executeUpdate();
    
            // Für Anfahrt (km-Pauschale)
            stmtRechnungskosten.setInt(1, rechnungsId);  // Rechnungs-ID
            stmtRechnungskosten.setInt(2, 3);  // Kostenart-ID für Anfahrt
            stmtRechnungskosten.setInt(3, anzahlAnfahrt);  // Anzahl km für Anfahrt
            stmtRechnungskosten.setDouble(4, anzahlAnfahrt * 2.50);  // Gesamtpreis für Anfahrt
            stmtRechnungskosten.executeUpdate();
    
            JOptionPane.showMessageDialog(this, "Rechnung erfolgreich gespeichert!");
            formulareLeeren();  // Felder nach erfolgreichem Speichern leeren
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Fehler beim Speichern der Rechnung.");
        }
    }

    // Methode zum Leeren aller Textfelder nach dem Speichern
    private void formulareLeeren() {
        tfRechnungsnummer.setText("");
        tfDatum.setText("");
        tfNachname.setText("");
        tfVorname.setText("");
        tfStrasse.setText("");
        tfPLZ.setText("");
        tfOrt.setText("");
        tfProjekt.setText("");
        tfBeschreibung.setText("");
        tfAnzahlArbeitsstunden.setText("");
        tfAnzahlMaterialkosten.setText("");
        tfAnzahlAnfahrt.setText("");
    }

    // Methode zur Rückgabe des entsprechenden Textfelds basierend auf der Position
    private JTextField getTextField(int index) {
        switch (index) {
            case 0: return tfRechnungsnummer;
            case 1: return tfDatum;
            case 2: return tfNachname;
            case 3: return tfVorname;
            case 4: return tfStrasse;
            case 5: return tfPLZ;
            case 6: return tfOrt;
            case 7: return tfProjekt;
            case 8: return tfBeschreibung;
            case 9: return tfAnzahlArbeitsstunden;
            case 10: return tfAnzahlMaterialkosten;
            case 11: return tfAnzahlAnfahrt;
            default: return null;
        }
    }
}
