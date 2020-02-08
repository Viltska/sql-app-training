package Tikape;

import java.sql.*;

public class Paketit {

    private final Connection db;
    private final Statement s;

    public Paketit(Connection db) throws SQLException {
        this.db = db;
        this.s = db.createStatement();
    }

    public void uusiPaketti(int asiakas_id, String seurantaKoodi) throws SQLException {
        if (!seurantaKoodi.isEmpty()) {
            try {
                s.execute("INSERT INTO Paketit (asiakas_id,koodi) VALUES (" + asiakas_id + ",'" + seurantaKoodi + "')");
                System.out.println("Paketti lisätty (" + asiakas_id + ", " + seurantaKoodi + ")");

            } catch (SQLException e) {
                //Constraint koodi UNIQUE
                System.out.println(e);
            }

        }
    }
    public int getID(String seurantaKoodi) throws SQLException {
        try {
            PreparedStatement p = db.prepareStatement("SELECT id FROM Paketit WHERE koodi=?");
            p.setString(1, seurantaKoodi);

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
