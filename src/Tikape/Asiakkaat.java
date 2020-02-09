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
        //Palauttaa -1 jos asiakasta ei löydy tietokannasta
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

    public void haeAsiakkaanPaketit(int asiakas_id) throws SQLException {
        try {
            PreparedStatement p = db.prepareStatement("SELECT seurantakoodi, COUNT(Tapahtumat.paketti_id) FROM Paketit\n"
                    + "LEFT JOIN Tapahtumat ON Tapahtumat.paketti_id = Paketit.id\n"
                    + "LEFT JOIN Asiakkaat ON Asiakkaat.id = Paketit.asiakas_id\n"
                    + "GROUP BY Paketit.seurantakoodi HAVING Paketit.asiakas_id = ?;");
            p.setInt(1, asiakas_id);
            ResultSet r = p.executeQuery();

            if (r.next() == false) {
                System.out.println("Asiakkaalla ei ollut paketteja");
            } else {
                System.out.println("Asiakkaan paketit");
                System.out.println("-------------");
                do {
                    System.out.println("Paketti (" + r.getString("seurantakoodi") + ")");
                    System.out.println("Tapahtumia(" + r.getString("COUNT(Tapahtumat.paketti_id)") + ")");
                    System.out.println("-------------");
                } while (r.next());
            }
        } catch (SQLException e) {
            System.out.println(e);
        }

    }

}
