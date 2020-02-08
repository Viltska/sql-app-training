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
        //Tarkistetaan että syöte ei ole tyhjä
        if (!nimi.isEmpty()) {
            this.asiakkaat.uusiAsiakas(nimi);
        } else {
            System.out.println("Asiakkaan nimi ei saa olla tyhjä");
        }
    }

    public void uusiPaikka(String nimi) throws SQLException {
        //Tarkistetaan että syöte ei ole tyhjä
        if (!nimi.isEmpty()) {
            this.paikat.uusiPaikka(nimi);
        } else {
            System.out.println("Paikannimi ei saa olla tyhjä");
        }
    }

    public void uusiPaketti(String asiakas, String koodi) throws SQLException {
        // .getID(asiakas) hakee SQL tietokannasta asiakkaan id, jos asiakasta ei löydy palautetaan arvo -1

        //Tarkistetaan että syötteet eivät ole tyhjä
        if (!asiakas.isEmpty() && !koodi.isEmpty()) {
            int asiakas_id = asiakkaat.getID(asiakas);

            //Jos asiakasta ei löydy tietokannasta ei pakettia lisätä
            if (asiakas_id != -1) {
                paketit.uusiPaketti(asiakas_id, koodi);
            } else {
                System.out.println("Asiakasta ei löytynyt");
            }
        } else {
            System.out.println("Asiakkaan nimi tai seurantakoodi eivät saa olla tyhiä");
        }

    }

    public void uusiTapahtuma(String paikka, String seurantaKoodi, String kuvaus) throws SQLException {
        //Tarkistetaan että syötteet eivät ole tyhjä
        if (!paikka.isEmpty() && !seurantaKoodi.isEmpty() && !kuvaus.isEmpty()) {
            int paikka_id = paikat.getPaikkaID(paikka);
            int paketti_id = paketit.getID(seurantaKoodi);
            //Tarkistetaan että paikka ja paketti löytyvät tietokannasta
            if (paikka_id != -1) {
                if (paketti_id != -1) {
                    tapahtumat.uusiTapahtuma(paikka_id, paketti_id, kuvaus);
                } else {
                    System.out.println("Pakettia ei löytynyt koodilla");
                }
            } else {
                System.out.println("Paikkaa ei löytynyt");
            }

        } else {
            System.out.println("Syöteet eivät saa olla tyhjä");
        }

    }

    public void haePaketinTapahtumat(String seurantaKoodi) throws SQLException {
        //Tarkistetaan että syöte ei ole tyhjä
        if (!seurantaKoodi.isEmpty()) {
            int paketti_id = paketit.getID(seurantaKoodi);

            //Tarkistetaan että paketti löytyy tietokannasta
            if (paketti_id != -1) {
                try {
                    paketit.haePaketinTapahtumat(paketti_id);
                } catch (SQLException e) {
                    System.out.println(e);
                }
            } else {
                System.out.println("Pakettia ei löytynyt");
            }
        }
        System.out.println("Syöte ei saa olla tyhjä");
    }

    //Important
    public void tikapePrint() {
        System.out.println("----------------------------------------------------");
        System.out.println(" ______  ______   __  __   ______  ____    ____      ");
        System.out.println("/\\__  _\\/\\__  _\\ /\\ \\/\\ \\ /\\  _  \\/\\  _`\\ /\\  _`\\    ");
        System.out.println("\\/_/\\ \\/\\/_/\\ \\/ \\ \\ \\/'/'\\ \\ \\L\\ \\ \\ \\L\\ \\ \\ \\L\\_\\  ");
        System.out.println("   \\ \\ \\   \\ \\ \\  \\ \\ , <  \\ \\  __ \\ \\ ,__/\\ \\  _\\L  ");
        System.out.println("    \\ \\ \\   \\_\\ \\__\\ \\ \\\\`\\ \\ \\ \\/\\ \\ \\ \\/  \\ \\ \\L\\ \\");
        System.out.println("     \\ \\_\\  /\\_____\\\\ \\_\\ \\_\\\\ \\_\\ \\_\\ \\_\\   \\ \\____/");
        System.out.println("      \\/_/  \\/_____/ \\/_/\\/_/ \\/_/\\/_/\\/_/    \\/___/ ");
        System.out.println("                                                     ");
        System.out.println("----------------------------------------------------");
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
            s.execute("CREATE TABLE Paikat (id INTEGER PRIMARY KEY, paikannimi TEXT UNIQUE)");
            System.out.println("Luotu taulukko 'Paikat'");

        } catch (SQLException e) {
            System.out.println("Löytyi taulukko 'Paikat'");
        }
        try {
            s.execute("CREATE TABLE Paketit (id INTEGER PRIMARY KEY, asiakas_id INTEGER, seurantakoodi TEXT UNIQUE)");
            System.out.println("Luotu taulukko 'Paketit'");

        } catch (SQLException e) {
            System.out.println("Löytyi taulukko 'Paketit'");
        }
        try {
            s.execute("CREATE TABLE Tapahtumat (id INTEGER PRIMARY KEY, paikka_id INTEGER, paketti_id INTEGER, date DATE, kuvaus TEXT NOT NULL)");
            System.out.println("Luotu taulukko 'Tapahtumat'");

        } catch (SQLException e) {
            System.out.println("Löytyi taulukko 'Tapahtumat'");
        }
        System.out.println("Tietokanta valmis");

    }

}
