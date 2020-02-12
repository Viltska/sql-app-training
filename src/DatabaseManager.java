
import java.io.File;
import java.sql.Connection;
import java.sql.*;

public class DatabaseManager {

    // Taulukko luokat
    private final Asiakkaat asiakkaat;
    private final Paikat paikat;
    private final Paketit paketit;
    private final Tapahtumat tapahtumat;
    // SQL
    private final String connectionName;
    private Connection db;

    public DatabaseManager(String database) throws SQLException {
        this.connectionName = "jdbc:sqlite:" + database;
        this.db = DriverManager.getConnection(connectionName);

        this.asiakkaat = new Asiakkaat(this.db);
        this.paikat = new Paikat(this.db);
        this.paketit = new Paketit(this.db);
        this.tapahtumat = new Tapahtumat(this.db);
    }

    public void uusiAsiakas(String nimi) throws SQLException {
        // Tarkistetaan että syöte ei ole tyhjä
        if (!nimi.isEmpty()) {
            this.asiakkaat.uusiAsiakas(nimi);
        } else {
            System.out.println("Asiakkaan nimi ei saa olla tyhjä");
        }
    }

    public void uusiPaikka(String nimi) throws SQLException {
        // Tarkistetaan että syöte ei ole tyhjä
        if (!nimi.isEmpty()) {
            this.paikat.uusiPaikka(nimi);
        } else {
            System.out.println("Paikannimi ei saa olla tyhjä");
        }
    }

    public void uusiPaketti(String asiakas, String seurantakoodi) throws SQLException {
        if (!asiakas.isEmpty() && !seurantakoodi.isEmpty()) {
            int asiakas_id = asiakkaat.getID(asiakas);
            if (asiakas_id != -1) {
                paketit.uusiPaketti(asiakas_id, seurantakoodi);
            } else {
                System.out.println("Asiakasta ei löytynyt");
            }
        } else {
            System.out.println("Asiakkaan nimi tai seurantakoodi eivät saa olla tyhiä");
        }
    }

    public void uusiTapahtuma(String paikka, String seurantaKoodi, String kuvaus) throws SQLException {
        // Tarkistetaan että syötteet eivät ole tyhjä
        if (!paikka.isEmpty() && !seurantaKoodi.isEmpty() && !kuvaus.isEmpty()) {
            int paikka_id = paikat.getPaikkaID(paikka);
            int paketti_id = paketit.getID(seurantaKoodi);
            // Tarkistetaan että paikka ja paketti löytyvät tietokannasta
            if (paikka_id != -1) {
                if (paketti_id != -1) {
                    tapahtumat.uusiTapahtuma(paikka_id, paketti_id, kuvaus);
                } else {
                    System.out.println("Seurantakoodilla ei löytynyt pakettia");
                }
            } else {
                System.out.println("Paikkaa ei löytynyt");
            }

        } else {
            System.out.println("Syöteet eivät saa olla tyhjä");
        }

    }

    public void haePaketinTapahtumat(String seurantaKoodi) throws SQLException {
        // Tarkistetaan että syöte ei ole tyhjä
        if (!seurantaKoodi.isEmpty()) {
            int paketti_id = paketit.getID(seurantaKoodi);

            // Tarkistetaan että paketti löytyy tietokannasta
            if (paketti_id != -1) {
                try {
                    System.out.println("Paketin: " + seurantaKoodi + ", ID: " + paketti_id + ", tapahtumat:");
                    paketit.haePaketinTapahtumat(paketti_id);
                } catch (SQLException e) {
                    System.out.println(e);
                }
            } else {
                System.out.println("Pakettia ei löytynyt");
            }
        } else {
            System.out.println("Syöte ei saa olla tyhjä");
        }
    }

    public void haeAsiakkaanPaketit(String asiakas) throws SQLException {
        int asiakkaan_id = asiakkaat.getID(asiakas);
        // Tarkistetaan että syöte ei ole tyhjä
        if (!asiakas.isEmpty()) {
            // Tarkistetaan että asiakas löytyy tietokannasta
            if (asiakkaan_id != -1) {
                try {
                    asiakkaat.haeAsiakkaanPaketit(asiakkaan_id);
                } catch (SQLException e) {
                    System.out.println(e);
                }
            } else {
                System.out.println("Asiakasta ei löytynyt tietokannasta");
            }

        } else {
            System.out.println("Syöte ei saa olla tyhjä");
        }
    }

    public void haePaikanTapahtumatPaivamaaralla(String paivamaara, String paikannimi) throws SQLException {
        String regex = "^((19|20)\\d\\d)-(0?[1-9]|1[012])-(0?[1-9]|[12][0-9]|3[01])$";
        int paikka_id = paikat.getPaikkaID(paikannimi);
        if (paivamaara.matches(regex)) {
            if (paikka_id != -1) {
                try {
                    System.out.println("Haetaan paikan (" + paikannimi + ") tapahtumat, pvm(" + paivamaara + ").");
                    tapahtumat.haeTapahtumatPaikasta(paivamaara, paikannimi);

                } catch (SQLException e) {
                    System.out.println(e);
                }

            } else {
                System.out.println("Paikkaa ei löytynyt");
            }
        } else {
            System.out.println("Tarkista että päivämäärä on oikein ja muodossa 'YYYY-MM-DD'");
        }
    }

    // Tehokkuustesti (Performance test)
    public void tehokkuusTesti(boolean poistetaan) throws SQLException {
        //Luo uuden tietokannan nimellä 'tehokkuus.db' ja ottaa tietokantaan yhteyden
        this.db = DriverManager.getConnection("jdbc:sqlite:tehokkuus.db");
        createTables();
        Statement s = db.createStatement();
        db.setAutoCommit(false);

        try {
            System.out.println("Tehokkuustesti..");
            System.out.println("Tietokantaan lisätään tuhat käyttäjää, tuhat paikkaa ja miljoona tapahtumaa (Vaiheet 1-4)");

            long t1 = System.nanoTime();
            for (int i = 1; i < 1001; i++) {
                PreparedStatement p = db.prepareStatement("INSERT INTO Asiakkaat (nimi) VALUES (?)");
                PreparedStatement p2 = db.prepareStatement("INSERT INTO Paikat (paikannimi) VALUES (?)");
                p.setString(1, ("A" + i));
                p2.setString(1, ("P" + i));
                try {
                    p.execute();
                    p2.execute();

                } catch (SQLException e) {
                    System.out.println(e);
                }

            }
            String kuvaus = "-";
            for (int i = 1; i < 1000001; i++) {
                PreparedStatement p3 = db.prepareStatement("INSERT INTO Tapahtumat (paikka_id,paketti_id,date,kuvaus) VALUES (?,?,datetime(),?)");
                p3.setInt(1, 100);
                p3.setInt(2, 100);
                p3.setString(3, kuvaus);
                try {
                    p3.execute();
                } catch (SQLException e) {
                    System.out.println(e);
                }
            }
            long t2 = System.nanoTime();

            System.out.println("Aikaa kului (1-4): " + (t2 - t1) / 1e9 + " sekuntia");
        } catch (SQLException e) {
            System.out.println(e);
        }
        if (!poistetaan) {
            db.commit();
        } else {
            db.rollback();
        }

        // Sulkee yhteyden tehokkuustesti-tietokantaan ja ottaa yhteyden alkuperäiseen tietokantaan
        this.db.close();
        this.db = DriverManager.getConnection(connectionName);
        db.setAutoCommit(true);
    }

    public void createTables() throws SQLException {
        Statement s = db.createStatement();
        try {
            s.execute("CREATE TABLE Asiakkaat (id INTEGER PRIMARY KEY, nimi TEXT NOT NULL UNIQUE)");
            System.out.println("Luotu taulukko 'Asiakkaat'");

        } catch (SQLException e) {
            System.out.println("Löytyi taulukko 'Asiakkaat'");
        }
        try {
            s.execute("CREATE TABLE Paikat (id INTEGER PRIMARY KEY, paikannimi TEXT NOT NULL UNIQUE)");
            System.out.println("Luotu taulukko 'Paikat'");

        } catch (SQLException e) {
            System.out.println("Löytyi taulukko 'Paikat'");
        }
        try {
            s.execute("CREATE TABLE Paketit (id INTEGER PRIMARY KEY, asiakas_id INTEGER NOT NULL, seurantakoodi TEXT NOT NULL UNIQUE)");
            System.out.println("Luotu taulukko 'Paketit'");

        } catch (SQLException e) {
            System.out.println("Löytyi taulukko 'Paketit'");
        }
        try {
            s.execute("CREATE TABLE Tapahtumat (id INTEGER PRIMARY KEY, paikka_id INTEGER NOT NULL, paketti_id INTEGER NOT NULL, date DATETIME, kuvaus TEXT NOT NULL)");
            System.out.println("Luotu taulukko 'Tapahtumat'");

        } catch (SQLException e) {
            System.out.println("Löytyi taulukko 'Tapahtumat'");
        }
        System.out.println("Tietokanta valmis.");

    }

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

}
