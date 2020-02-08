package Tikape;

import java.sql.Connection;
import java.sql.*;

public class DatabaseManager {
//Taulukot

    private final Asiakkaat asiakkaat;
    private final Paikat paikat;
    private final Paketit paketit;
    private final Tapahtumat tapahtumat;
// SQL 
    private final String databaseName;
    private final String connection;
    private final Connection db;

    public DatabaseManager(String database) throws SQLException {
        this.databaseName = database;
        this.connection = "jdbc:sqlite:" + database;
        this.db = DriverManager.getConnection(connection);

        this.asiakkaat = new Asiakkaat(this.db);
        this.paikat = new Paikat(this.db);
        this.paketit = new Paketit(this.db);
        this.tapahtumat = new Tapahtumat(db);

    }

    public void uusiAsiakas(String nimi) throws SQLException {
        this.asiakkaat.uusiAsiakas(nimi);
    }

    public void uusiPaikka(String nimi) throws SQLException {
        this.paikat.uusiPaikka(nimi);
    }

    public void uusiPaketti(String asiakas, String koodi) throws SQLException {
        //Haetaan asiakas_id nimen perusteella
        int asiakas_id = asiakkaat.getID(asiakas);
        //Jos nimeä ei löydy palautetaan arvo -1
        if (asiakas_id != -1) {
            paketit.uusiPaketti(asiakas_id, koodi);
        } else {
            System.out.println("Asiakasta ei löytynyt");
        }

    }

    public void uusiTapahtuma(String paikka, String seurantaKoodi, String kuvaus) throws SQLException {
        if (!paikka.isEmpty() && !seurantaKoodi.isEmpty() && !kuvaus.isEmpty()) {
            int paikka_id = paikat.getPaikkaID(paikka);
            int paketti_id = paketit.getID(seurantaKoodi);
            if (paikka_id != -1) {
                if (paketti_id != -1) {
                    tapahtumat.uusiTapahtuma(0, seurantaKoodi, kuvaus);
                } else {
                    System.out.println("Pakettia ei löytynyt koodilla");
                }
            } else {
                System.out.println("Paikkaa ei löytynyt");
            }
        } else {
            System.out.println("Syöte ei saa olla tyhjä");
        }

    }

    public int haeAsiakkaanID(String nimi) throws SQLException {
        return asiakkaat.getID(nimi);
    }

    public void createTables() throws SQLException {
        Statement s = db.createStatement();
        try {
            s.execute("CREATE TABLE Asiakkaat (id INTEGER PRIMARY KEY, nimi TEXT UNIQUE)");
            System.out.println("Luotu taulukko 'Asiakkaat'");

        } catch (SQLException e) {
            System.out.println("Löytyi taulukko 'Asiakkaat'");
        }
        try {
            s.execute("CREATE TABLE Paikat (id INTEGER PRIMARY KEY, nimi TEXT UNIQUE)");
            System.out.println("Luotu taulukko 'Paikat'");

        } catch (SQLException e) {
            System.out.println("Löytyi taulukko 'Paikat'");
        }
        try {
            s.execute("CREATE TABLE Paketit (id INTEGER PRIMARY KEY, asiakas_id INTEGER, koodi TEXT UNIQUE)");
            System.out.println("Luotu taulukko 'Paketit'");

        } catch (SQLException e) {
            System.out.println("Löytyi taulukko 'Paketit'");
        }
        try {
            s.execute("CREATE TABLE Tapahtumat (id INTEGER PRIMARY KEY, paikka_id INTEGER, koodi TEXT, datetime DATETIME NOT NULL, kuvaus TEXT NOT NULL)");
            System.out.println("Luotu taulukko 'Tapahtumat'");

        } catch (SQLException e) {
            System.out.println("Löytyi taulukko 'Tapahtumat'");
        }

    }

}
