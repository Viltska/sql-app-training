package Tikape;

import java.sql.*;

public class Asiakkaat {

    private final Connection db;
    private final Statement s;

    public Asiakkaat(String connection) throws SQLException {

        this.db = DriverManager.getConnection(connection);
        this.s = db.createStatement();
    }

    public void uusiAsiakas(String nimi) throws SQLException {
        try {
            s.execute("INSERT INTO Asiakkaat (nimi) VALUES ('" + nimi + "')");
            System.out.println("Asiakas lisätty");

        } catch (SQLException e) {
            System.out.println(e);
        }

    }

    public int getID(String asiakas) throws SQLException {
        int palaute;
        try {
            PreparedStatement p = db.prepareStatement("SELECT id FROM Asiakkaat WHERE nimi=?");
            p.setString(1, asiakas);

            ResultSet r = p.executeQuery();

            if (r.next()) {
                 palaute = r.getInt("id");
                 return palaute;
            } else {
                return -1;
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return -1;

    }


}
