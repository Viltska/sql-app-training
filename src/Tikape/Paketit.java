
package Tikape;

import java.sql.*;

public class Paketit {

    private final Connection db;
    private final Statement s;

    public Paketit(String connection) throws SQLException {

        this.db = DriverManager.getConnection(connection);
        this.s = db.createStatement();

    }

    public void uusiPaketti(Integer asiakasID, String seurantaKoodi) throws SQLException {
        System.out.println("UUSI PAKETTI: " + seurantaKoodi + " Asiakas ID:" + asiakasID);

        try {
            s.execute("INSERT INTO Paketit (asiakas_id, koodi) VALUES (" + asiakasID + ", '" + seurantaKoodi + "'");

        } catch (SQLException e) {
            System.out.println(e);
        }

    }

}
