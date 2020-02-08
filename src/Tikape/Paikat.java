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

        try {
            PreparedStatement p = db.prepareStatement("INSERT INTO Paikat (nimi) VALUES (?)");
            p.setString(1, nimi);
            p.executeUpdate();
            System.out.println("Paikka lisätty");
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    public int getPaikkaID(String paikka) throws SQLException {
        try {
            PreparedStatement p = db.prepareStatement("SELECT id FROM Paikat WHERE nimi=?");
            p.setString(1, paikka);

            ResultSet r = p.executeQuery();

            if (r.next()) {
                return r.getInt("id");

            } else {
                return -1;
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return -1;

    }
}
