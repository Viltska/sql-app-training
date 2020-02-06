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
            System.out.println("UUSI ASIAKAS: " + nimi);

        } catch (SQLException e) {
            System.out.println(e);
        }

    }

    public void printAll() throws SQLException {
        try {
            ResultSet r = s.executeQuery("SELECT * FROM Asiakkaat");
            while (r.next()) {
                System.out.println(r.getInt("id") + " " + r.getString("nimi"));
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    public int getAsiakasID(String asiakas) throws SQLException {
        String sql = ("SELECT id FROM Asiakkaat WHERE nimi = '" + asiakas + "'");


        PreparedStatement p = db.prepareStatement(sql);
        p.setString(1, asiakas);

        ResultSet r = p.executeQuery();
        if (r.next()) {
            int id = r.getInt("id");
            return id;
        } else {
            System.out.println("Tuotetta ei löytynyt");
        }
        return -1;

    }

    public String getAsiakasNimi(int id) throws SQLException {

        PreparedStatement p = db.prepareStatement("SELECT hinta FROM Tuotteet WHERE nimi=?");

        ResultSet r = p.executeQuery();
        if (r.next()) {
            System.out.println("Hinta: " + r.getInt("hinta"));
        } else {
            System.out.println("Tuotetta ei löytynyt");
        }
        return "";
    }

}
