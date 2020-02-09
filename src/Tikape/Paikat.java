package Tikape;

import java.sql.*;

public class Paikat {

    private final Connection db;

    public Paikat(Connection db) throws SQLException {
        this.db = db;

    }

    public void uusiPaikka(String nimi) throws SQLException {

        try {
            PreparedStatement p = db.prepareStatement("INSERT INTO Paikat (paikannimi) VALUES (?)");
            p.setString(1, nimi);
            p.executeUpdate();
            System.out.println("Paikka lisätty");
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    public int getPaikkaID(String paikka) throws SQLException {
        //Palauttaa -1 jos paikkaa ei löydy tietokannasta
        try {
            PreparedStatement p = db.prepareStatement("SELECT id FROM Paikat WHERE paikannimi=?");
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
