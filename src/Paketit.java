
import java.sql.*;

public class Paketit {

    private final Connection db;

    public Paketit(Connection db) throws SQLException {
        this.db = db;
    }

    public void uusiPaketti(String asiakas, String seurantakoodi) throws SQLException {
        try {
            PreparedStatement p = db.prepareStatement("INSERT INTO Paketit (asiakas_id,seurantakoodi) VALUES ((SELECT id FROM Asiakkaat WHERE nimi = ?),?)");
            p.setString(1, asiakas);
            p.setString(2, seurantakoodi);
            p.executeUpdate();
            System.out.println("Paketti lisätty");
        } catch (SQLException e) {
            System.out.println("Pakettia ei voitu lisätä");
            System.out.println(e);
        }
    }

    public void haePaketinTapahtumat(String seurantaKoodi) throws SQLException {
        int kertoja = 1;
        try {
            PreparedStatement p = db.prepareStatement("SELECT date, paikannimi, kuvaus FROM Tapahtumat \n"
                    + "JOIN Paketit ON Paketit.id = Tapahtumat.paketti_id\n"
                    + "JOIN Paikat ON Tapahtumat.paikka_id = Paikat.id\n"
                    + "WHERE Paketit.id = (SELECT id FROM Paketit WHERE seurantakoodi = ?)");
            p.setString(1, seurantaKoodi);
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
            System.out.println("Ongelma haettaessa paketin tapahtumia");
            System.out.println(e);
        }
    }

    public int paketinTapahtumienMaara(int paketti_id) throws SQLException {
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
