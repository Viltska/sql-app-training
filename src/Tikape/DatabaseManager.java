package Tikape;

import java.sql.Connection;
import java.sql.*;

public class DatabaseManager {

    private final Asiakkaat asiakkaat;
    private final Paikat paikat;
    private final Paketit paketit;

    private final String database;
    private final String connection;

    public DatabaseManager(String database) throws SQLException {
        this.database = database;
        this.connection = "jdbc:sqlite:" + database;

        this.asiakkaat = new Asiakkaat(connection);
        this.paikat = new Paikat(connection);
        this.paketit = new Paketit(connection);

    }

    public void uusiAsiakas(String nimi) throws SQLException {
        this.asiakkaat.uusiAsiakas(nimi);
    }

    public void uusiPaikka(String nimi) throws SQLException {
        this.paikat.uusiPaikka(nimi);
    }

    public void uusiPaketti(String asiakas, String koodi) throws SQLException {
        paketit.uusiPaketti(asiakas, koodi);

    }

    public int haeAsiakkaanID(String nimi) throws SQLException {
        return asiakkaat.getID(nimi);
    }

    public void createTables() throws SQLException {
        Connection db = DriverManager.getConnection(connection);
        Statement s = db.createStatement();
        try {
            s.execute("CREATE TABLE Asiakkaat (id INTEGER PRIMARY KEY, nimi TEXT UNIQUE)");

        } catch (SQLException e) {
            System.out.println("Löytyi taulukko 'Asiakkaat'");
        }
        try {
            s.execute("CREATE TABLE Paikat (id INTEGER PRIMARY KEY, nimi TEXT UNIQUE)");

        } catch (SQLException e) {
            System.out.println("Löytyi taulukko 'Paikat'");
        }
        try {
            s.execute("CREATE TABLE Paketit (id INTEGER PRIMARY KEY, asiakas_id INTEGER, koodi TEXT UNIQUE)");

        } catch (SQLException e) {
            System.out.println("Löytyi taulukko 'Paketit'");
        }

    }

}
