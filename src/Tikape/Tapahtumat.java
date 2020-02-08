package Tikape;

import java.sql.*;

public class Tapahtumat {

    private final Connection db;
    private final Statement s;

    public Tapahtumat(Connection db) throws SQLException {
        this.db = db;
        this.s = db.createStatement();

    }

    public void uusiTapahtuma(int paikka_id, String paketinKoodi, String kuvaus) throws SQLException {
        try {
            PreparedStatement p = db.prepareStatement("INSERT INTO Tapahtumat (paikka_id,koodi,datetime,kuvaus) VALUES (?,?,?,?)");
            p.setInt(1, paikka_id);
            p.setString(2, paketinKoodi);
            p.setString(3, "datetime");
            p.setString(4, kuvaus);
            p.executeUpdate();
            System.out.println("Asiakas lisätty");
        } catch (SQLException e) {
            System.out.println(e);
        }
    }
}
