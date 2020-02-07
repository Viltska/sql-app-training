package Tikape;

import java.sql.*;

public class Asiakkaat {

    private final Connection db;
    private final Statement s;

    public Asiakkaat(Connection db) throws SQLException {
        this.db = db;
        this.s = db.createStatement();
    }

    public void uusiAsiakas(String nimi) throws SQLException {
        if (!nimi.isEmpty()) {
            try {
                s.execute("INSERT INTO Asiakkaat (nimi) VALUES ('" + nimi + "')");
                System.out.println(nimi + " lisätty");

            } catch (SQLException e) {
                System.out.println(e);
            }
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

}
