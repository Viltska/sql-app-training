package Tikape;

import java.sql.*;

public class Paketit {

    private final Connection db;
    private final Statement s;

    public Paketit(String connection) throws SQLException {

        this.db = DriverManager.getConnection(connection);
        this.s = db.createStatement();

    }

    public void uusiPaketti(String asiakas, String seurantaKoodi) throws SQLException {

    }

}
