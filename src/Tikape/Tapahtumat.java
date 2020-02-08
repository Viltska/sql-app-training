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
            s.execute("INSERT INTO Tapahtumat (paikka_id,koodi,datetime,kuvaus) VALUES ("+paikka_id+", '"+paketinKoodi+"', datetime(), '"+kuvaus+"')");
            System.out.println("Tapahtuma lisätty");
        } catch(SQLException e) {
            System.out.println(e);
        }
    }
}
