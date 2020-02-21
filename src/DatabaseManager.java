
import java.io.File;
import java.sql.Connection;
import java.sql.*;
import java.util.Random;

public class DatabaseManager {

    private final Asiakkaat asiakkaat;
    private final Paikat paikat;
    private final Paketit paketit;
    private final Tapahtumat tapahtumat;
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
        if (!nimi.isEmpty()) {
            this.asiakkaat.uusiAsiakas(nimi);
        } else {
            System.out.println("Asiakkaan nimi ei saa olla tyhjä");
        }
    }

    public void uusiPaikka(String nimi) throws SQLException {
        if (!nimi.isEmpty()) {
            this.paikat.uusiPaikka(nimi);
        } else {
            System.out.println("Paikannimi ei saa olla tyhjä");
        }
    }

    public void uusiPaketti(String asiakas, String seurantakoodi) throws SQLException {
        if (!asiakas.isEmpty() && !seurantakoodi.isEmpty()) {
            paketit.uusiPaketti(asiakas, seurantakoodi);
        } else {
            System.out.println("Syöte ei saa olla tyhjä");
        }
    }

    public void uusiTapahtuma(String paikka, String seurantaKoodi, String kuvaus) throws SQLException {
        if (!paikka.isEmpty() && !seurantaKoodi.isEmpty() && !kuvaus.isEmpty()) {
            tapahtumat.uusiTapahtuma(paikka, seurantaKoodi, kuvaus);
        } else {
            System.out.println("Syöteet eivät saa olla tyhjä");
        }
    }

    public void haePaketinTapahtumat(String seurantaKoodi) throws SQLException {
        if (!seurantaKoodi.isEmpty()) {
            try {
                System.out.println("Paketin (" + seurantaKoodi + ") tapahtumat:");
                paketit.haePaketinTapahtumat(seurantaKoodi);
            } catch (SQLException e) {
                System.out.println(e);
            }
        } else {
            System.out.println("Syöte ei saa olla tyhjä");
        }
    }

    public void haeAsiakkaanPaketit(String asiakas) throws SQLException {
        if (!asiakas.isEmpty()) {
            try {
                asiakkaat.haeAsiakkaanPaketit(asiakas);
            } catch (SQLException e) {
                System.out.println(e);
            }
        } else {
            System.out.println("Syöte ei saa olla tyhjä");
        }
    }

    public void haePaikanTapahtumatPaivamaaralla(String paivamaara, String paikannimi) throws SQLException {
        String regex = "^((19|20)\\d\\d)-(0?[1-9]|1[012])-(0?[1-9]|[12][0-9]|3[01])$";
        if (paivamaara.matches(regex)) {
            try {
                tapahtumat.haeTapahtumatPaikasta(paivamaara, paikannimi);
            } catch (SQLException e) {
                System.out.println("Ongelma haettaessa tapahtumia päivämäärällä");
                System.out.println(e);
            }
        } else {
            System.out.println("Tarkista että päivämäärä on oikein ja muodossa 'YYYY-MM-DD'");
        }
    }

    public void tehokkuusTesti(boolean poistetaan) throws SQLException {
        //Luodaan testi tietokanta 'tehokkuus.db'
        this.db = DriverManager.getConnection("jdbc:sqlite:tehokkuus.db");
        createTables();
        Statement s = db.createStatement();
        // db.setAutoCommit(false);
        Random rand = new Random(1137);

        System.out.println("");
        System.out.println("Tehokkuustesti: ");
        System.out.println("");

        try {
            s.execute("BEGIN TRANSACTION");

            // 1. Asiakkaiden lisäys
            long t1 = System.nanoTime();
            for (int i = 1; i < 1001; i++) {
                PreparedStatement p = db.prepareStatement("INSERT INTO Asiakkaat (nimi) VALUES (?)");
                p.setString(1, ("A" + i));
                p.execute();

            }
            long t2 = System.nanoTime();
            System.out.println("Lisätty 1000 asiakasta ajassa: " + (t2 - t1) / 1e9 + "s.");

            // 2. Paikkojen lisäys
            for (int i = 1; i < 1001; i++) {
                PreparedStatement p = db.prepareStatement("INSERT INTO Paikat (paikannimi) VALUES (?)");
                p.setString(1, ("P" + i));
                p.execute();
            }
            long t3 = System.nanoTime();
            System.out.println("Lisätty 1000 paikkaa ajassa: " + (t3 - t2) / 1e9 + "s.");

            // 3. Pakettien lisäys
            for (int i = 1; i < 1001; i++) {
                PreparedStatement p = db.prepareStatement("INSERT INTO Paketit (asiakas_id,seurantakoodi) VALUES ((SELECT id FROM Asiakkaat WHERE nimi = ?),?)");
                p.setString(1, ("A" + i));
                p.setString(2, ("koodi" + i));
                p.execute();
            }
            long t4 = System.nanoTime();
            System.out.println("Lisätty 1000 pakettia ajassa: " + (t4 - t3) / 1e9 + "s.");

            // 4. Tapahtumien lisäys
            for (int i = 1; i < 1000001; i++) {
                // PreparedStatement p = db.prepareStatement("INSERT INTO Tapahtumat (paikka_id,paketti_id,date,kuvaus) VALUES (?,?,datetime(),'testi')");
                PreparedStatement p = db.prepareStatement("INSERT INTO Tapahtumat (paikka_id,paketti_id,date,kuvaus)"
                        + " VALUES ((SELECT id FROM Paikat WHERE paikannimi = ?),(SELECT id FROM Paketit WHERE seurantakoodi = ?),datetime(),?)");
                int j = rand.nextInt(1000) + 1;
                p.setString(1, ("P" + j));
                p.setString(2, ("koodi" + j));
                p.setString(3, ("testi" + i));
                p.execute();

            }

            s.execute("COMMIT");
            long t5 = System.nanoTime();
            System.out.println("Lisätty 1000 000 tapahtumaa ja pakettia ajassa: " + (t5 - t4) / 1e9 + "s.");

            // 5. Asiakkaan pakettien määrä
            for (int i = 1; i < 1001; i++) {
                PreparedStatement p = db.prepareStatement("SELECT COUNT(*) FROM Paketit, Asiakkaat WHERE Paketit.asiakas_id = Asiakkaat.id AND Asiakkaat.nimi = ?");
                p.setString(1, ("A" + i));
                ResultSet r = p.executeQuery();
            }
            long t6 = System.nanoTime();
            System.out.println("Haettu 1000 asiakkaan pakettien määrä ajassa: " + (t6 - t5) / 1e9 + "s.");

            // 6. Paketin tapahtumien määrä
            for (int i = 1; i < 1001; i++) {
                int j = rand.nextInt(1000) + 1;
                PreparedStatement p = db.prepareStatement("SELECT COUNT(*) FROM Tapahtumat, Paketit WHERE Tapahtumat.paketti_id = Paketit.id AND Paketit.seurantakoodi = ?");
                p.setString(1, ("koodi" + j));
                ResultSet r = p.executeQuery();
                do {
                    System.out.print(r.getInt("COUNT(*)"+ " "));
                } while (r.next());
            }
            System.out.println("");
            long t7 = System.nanoTime();
            System.out.println("Haettu 1000 paketin tapahtumien määrä ajassa: " + (t7 - t6) / 1e9 + "s.");

        } catch (SQLException e) {
            System.out.println("Ongelma tehokuustestissä");
            System.out.println(e);
        }

        /*if (!poistetaan) {
            db.commit();
        } else {
            db.rollback();
        } */
        // Pudottaa yhteyden testi tietokantaan ja ottaa yhteyden alkuperäiseen tietokantaan
        this.db.close();
        this.db = DriverManager.getConnection(connectionName);
        db.setAutoCommit(true);
    }

    public void createTables() throws SQLException {
        Statement s = db.createStatement();
        System.out.println("");
        try {
            s.execute("CREATE TABLE Asiakkaat (id INTEGER PRIMARY KEY, nimi TEXT NOT NULL UNIQUE)");
            System.out.println("Luotu taulukko 'Asiakkaat'");

        } catch (SQLException e) {
            System.out.println("Taulukkoa 'Asiakkaat' ei voitu luoda");
            System.out.println(e);
        }
        System.out.println("");
        try {
            s.execute("CREATE TABLE Paikat (id INTEGER PRIMARY KEY, paikannimi TEXT NOT NULL UNIQUE)");
            System.out.println("Luotu taulukko 'Paikat'");

        } catch (SQLException e) {
            System.out.println("Taulukkoa 'Paikat' ei voitu luoda");
            System.out.println(e);
        }
        System.out.println("");
        try {

            s.execute("CREATE TABLE Paketit (id INTEGER PRIMARY KEY, asiakas_id INTEGER NOT NULL, seurantakoodi TEXT NOT NULL UNIQUE)");
            System.out.println("Luotu taulukko 'Paketit'");

        } catch (SQLException e) {
            System.out.println("Taulukkoa 'Paketit' ei voitu luoda");
            System.out.println(e);
        }
        System.out.println("");
        try {
            s.execute("CREATE TABLE Tapahtumat (id INTEGER PRIMARY KEY, paikka_id INTEGER NOT NULL, paketti_id INTEGER NOT NULL, date DATETIME NOT NULL, kuvaus TEXT NOT NULL)");
            System.out.println("Luotu taulukko 'Tapahtumat'");
        } catch (SQLException e) {
            System.out.println("Taulukkoa 'Tapahtumat' ei voitu luoda");
            System.out.println(e);
        }
        System.out.println("");
        try {
            s.execute("CREATE INDEX idx_paketti ON Tapahtumat(paketti_id)");
            s.execute("CREATE INDEX idx_seurantakoodi ON Paketit(seurantakoodi)");
            s.execute("CREATE INDEX idx_paikka ON Paikat(paikannimi)");
            System.out.println("Indeksit luotu");
        } catch (SQLException e) {
            System.out.println("Indeksejä ei voitu luoda");
            System.out.println(e);
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
