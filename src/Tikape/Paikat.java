package Tikape;

import java.sql.*;

public class Paikat {

    private final Connection db;
    private final Statement s;

    public Paikat(Connection db) throws SQLException {
        this.db = db;
        this.s = db.createStatement();

    }

    public void uusiPaikka(String nimi) throws SQLException {
        if (!nimi.isEmpty()) {
            try {
                s.execute("INSERT INTO Paikat (nimi) VALUES ('" + nimi + "')");
                System.out.println(nimi + " lisätty");

            } catch (SQLException e) {
                System.out.println(e);
            }
        }
    }

    public int getPaikkaID(String paikka) throws SQLException {
        int id = -1;
        try {
            ResultSet r = s.executeQuery("SELECT id FROM Asiakkaat WHERER nimi = '" + paikka + "'");
            id = r.getInt("id");

        } catch (SQLException e) {
            System.out.println(e);

        }
        return id;
    }
}
