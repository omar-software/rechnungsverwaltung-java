-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Server-Version:               10.4.32-MariaDB - mariadb.org binary distribution
-- Server-Betriebssystem:        Win64
-- HeidiSQL Version:             12.8.0.6908
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

-- Exportiere Struktur von Tabelle rechnungsverwaltung.kostenarten
CREATE TABLE IF NOT EXISTS `kostenarten` (
  `kostenart_id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL,
  `einzelpreis` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`kostenart_id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Exportiere Daten aus Tabelle rechnungsverwaltung.kostenarten: ~3 rows (ungefähr)
DELETE FROM `kostenarten`;
INSERT INTO `kostenarten` (`kostenart_id`, `name`, `einzelpreis`) VALUES
	(1, 'Arbeitsstunden', 50.00),
	(2, 'Materialkosten', 200.00),
	(3, 'Anfahrt, km-Pauschale', 2.50);

-- Exportiere Struktur von Tabelle rechnungsverwaltung.kunden
CREATE TABLE IF NOT EXISTS `kunden` (
  `kunden_id` int(11) NOT NULL AUTO_INCREMENT,
  `nachname` varchar(50) DEFAULT NULL,
  `vorname` varchar(50) DEFAULT NULL,
  `strasse` varchar(100) DEFAULT NULL,
  `plz` varchar(10) DEFAULT NULL,
  `ort` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`kunden_id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Exportiere Daten aus Tabelle rechnungsverwaltung.kunden: ~5 rows (ungefähr)
DELETE FROM `kunden`;
INSERT INTO `kunden` (`kunden_id`, `nachname`, `vorname`, `strasse`, `plz`, `ort`) VALUES
	(1, 'Müller', 'Anna', 'Hauptstraße 1', '10115', 'Berlin'),
	(2, 'Schmidt', 'Peter', 'Marktweg 10', '80331', 'München'),
	(3, 'Klein', 'Julia', 'Bahnhofstraße 5', '50667', 'Köln'),
	(4, 'Weber', 'Michael', 'Dorfstraße 8', '20095', 'Hamburg'),
	(5, 'Fischer', 'Laura', 'Ringstraße 9', '40210', 'Düsseldorf');

-- Exportiere Struktur von Tabelle rechnungsverwaltung.rechnung
CREATE TABLE IF NOT EXISTS `rechnung` (
  `rechnungs_id` int(11) NOT NULL AUTO_INCREMENT,
  `rechnungsnummer` varchar(50) DEFAULT NULL,
  `datum` date DEFAULT NULL,
  `kunden_id` int(11) DEFAULT NULL,
  `projekt` varchar(100) DEFAULT NULL,
  `beschreibung` text DEFAULT NULL,
  `gesamt_betrag` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`rechnungs_id`),
  KEY `kunden_id` (`kunden_id`),
  CONSTRAINT `rechnung_ibfk_1` FOREIGN KEY (`kunden_id`) REFERENCES `kunden` (`kunden_id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Exportiere Daten aus Tabelle rechnungsverwaltung.rechnung: ~5 rows (ungefähr)
DELETE FROM `rechnung`;
INSERT INTO `rechnung` (`rechnungs_id`, `rechnungsnummer`, `datum`, `kunden_id`, `projekt`, `beschreibung`, `gesamt_betrag`) VALUES
	(1, 'INV-2024-001', '2024-10-01', 1, 'Renovierung Wohnzimmer', 'Malerarbeiten und Bodenverlegung', 2350.00),
	(2, 'INV-2024-002', '2024-10-05', 2, 'Kücheninstallation', 'Installation von Küchengeräten und Möbeln', 3200.50),
	(3, 'INV-2024-003', '2024-10-10', 3, 'Badezimmersanierung', 'Fliesenlegen und Installation von Sanitäranlagen', 4150.75),
	(4, 'INV-2024-004', '2024-10-15', 4, 'Gartenbau', 'Landschaftsarbeiten und Pflanzen von Bäumen', 1800.00),
	(5, 'INV-2024-005', '2024-10-20', 5, 'Dachreparatur', 'Erneuerung der Dachziegel und Abdichtung', 2750.25);

-- Exportiere Struktur von Tabelle rechnungsverwaltung.rechnungskosten
CREATE TABLE IF NOT EXISTS `rechnungskosten` (
  `rechnungskosten_id` int(11) NOT NULL AUTO_INCREMENT,
  `rechnungs_id` int(11) DEFAULT NULL,
  `kostenart_id` int(11) DEFAULT NULL,
  `anzahl` int(11) DEFAULT NULL,
  `gesamtpreis` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`rechnungskosten_id`),
  KEY `rechnungs_id` (`rechnungs_id`),
  KEY `kostenart_id` (`kostenart_id`),
  CONSTRAINT `rechnungskosten_ibfk_1` FOREIGN KEY (`rechnungs_id`) REFERENCES `rechnung` (`rechnungs_id`),
  CONSTRAINT `rechnungskosten_ibfk_2` FOREIGN KEY (`kostenart_id`) REFERENCES `kostenarten` (`kostenart_id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Exportiere Daten aus Tabelle rechnungsverwaltung.rechnungskosten: ~15 rows (ungefähr)
DELETE FROM `rechnungskosten`;
INSERT INTO `rechnungskosten` (`rechnungskosten_id`, `rechnungs_id`, `kostenart_id`, `anzahl`, `gesamtpreis`) VALUES
	(1, 1, 1, 30, 1500.00),
	(2, 1, 2, 4, 800.00),
	(3, 1, 3, 20, 50.00),
	(4, 2, 1, 40, 2000.00),
	(5, 2, 2, 5, 1000.00),
	(6, 2, 3, 80, 200.50),
	(7, 3, 1, 60, 3000.00),
	(8, 3, 2, 6, 1200.00),
	(9, 3, 3, 60, 150.75),
	(10, 4, 1, 25, 1250.00),
	(11, 4, 2, 2, 400.00),
	(12, 4, 3, 60, 150.00),
	(13, 5, 1, 35, 1750.00),
	(14, 5, 2, 5, 1000.00),
	(15, 5, 3, 0, 0.00);

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
