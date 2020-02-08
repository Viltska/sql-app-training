package Tikape;

import java.sql.*;

public class Asiakkaat {

    private final Connection db;

    public Asiakkaat(Connection db) throws SQLException {
        this.db = db;
    }

    public void uusiAsiakas(String nimi) throws SQLException {
        try {
            PreparedStatement p = db.prepareStatement("INSERT INTO Asiakkaat (nimi) VALUES (?)");
            p.setString(1, nimi);
            p.executeUpdate();
            System.out.println("Asiakas lisätty");
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    public int getID(String asiakas) throws SQLException {
        try {
            PreparedStatement p = db.prepareStatement("SELECT id FROM Asiakkaat WHERE nimi=?");
            p.setString(1, asiakas);

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

    public void haeAsiakkaanPaketit(String asiakas) throws SQLException {
        try {
            PreparedStatement p = db.prepareStatement("SELECT * FROM Asiakkaat WHERE nimi = ?");
            p.setString(1, asiakas);
            ResultSet r = p.executeQuery();

            while (r.next()) {

            }

        } catch (SQLException e) {
            System.out.println(e);
        }

    }

}
