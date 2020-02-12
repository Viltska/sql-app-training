
import java.sql.*;

public class Paketit {

    private final Connection db;

    public Paketit(Connection db) throws SQLException {
        this.db = db;
    }

    public void uusiPaketti(int asiakas_id, String seurantakoodi) throws SQLException {
        //Paketin seurantakoodi ei saa löytyä tietokannasta
        try {
            PreparedStatement p = db.prepareStatement("INSERT INTO Paketit (asiakas_id,seurantakoodi) VALUES (?,?)");
            p.setInt(1, asiakas_id);
            p.setString(2, seurantakoodi);
            p.executeUpdate();
            System.out.println("Paketti lisätty");
        } catch (SQLException e) {
            System.out.println("ongelma metodissa 'Paketit.uusiPaketti'");
            System.out.println(e);
        }
    }

    public int getID(String seurantaKoodi) throws SQLException {
        // Palauttaa arvon -1 jos pakettia ei löydy tietokannasta
        try {
            PreparedStatement p = db.prepareStatement("SELECT id FROM Paketit WHERE seurantakoodi = ?");
            p.setString(1, seurantaKoodi);

            ResultSet r = p.executeQuery();

            if (r.next()) {
                return r.getInt("id");

            } else {
                return -1;
            }
        } catch (SQLException e) {
            System.out.println("Ongelma metodissa 'Paketit.getID'");
            System.out.println(e);
        }
        return -1;

    }

    public void haePaketinTapahtumat(int paketti_id) throws SQLException {
        int kertoja = 1;
        try {
            PreparedStatement p = db.prepareStatement("SELECT date, paikannimi, kuvaus FROM Tapahtumat \n"
                    + "JOIN Paketit ON Paketit.id = Tapahtumat.paketti_id\n"
                    + "JOIN Paikat ON Tapahtumat.paikka_id = Paikat.id\n"
                    + "WHERE Paketit.id = ?");
            p.setInt(1, paketti_id);
            ResultSet r = p.executeQuery();

            if (r.next() == false) {
                System.out.println("Paketilla ei ollut tapahtumia");
            } else {
                System.out.println("----------------------");
                do {
                    System.out.println("(" + kertoja + ")");
                    System.out.println("TAPAHTUMA (" + r.getString("date") + ")");
                    System.out.println("Sijainti: " + r.getString("paikannimi"));
                    System.out.println("Kuvaus: " + r.getString("kuvaus"));
                    System.out.println("----------------------");
                    kertoja++;

                } while (r.next());
                System.out.println("Tapahtumia yhteensä: " + (kertoja - 1));
            }
        } catch (SQLException e) {
            System.out.println(e);
        }

    }

    public int paketinTapahtumienMaara(int paketti_id) throws SQLException {
        //Palauttaa arvon -1 jos pakettia ei löydy tietokannasta
        try {
            PreparedStatement p = db.prepareStatement("SELECT COUNT(*) FROM Tapahtumat, Paikat, Paketit WHERE Tapahtumat.paikka_id = Paikat.id AND Tapahtumat.paketti_id = Paketit.id AND Paketit.id = ?");
            p.setInt(1, paketti_id);
            ResultSet r = p.executeQuery();

            while (r.next()) {
                return 0;
            }

        } catch (SQLException e) {
            System.out.println(e);

        }
        return -1;
    }

}
