
import java.sql.*;

public class Tapahtumat {

    private final Connection db;
    private final Statement s;

    public Tapahtumat(Connection db) throws SQLException {
        this.db = db;
        this.s = db.createStatement();

    }

    public void uusiTapahtuma(int paikka_id, int paketti_id, String kuvaus) throws SQLException {
        try {
            PreparedStatement p = db.prepareStatement("INSERT INTO Tapahtumat (paikka_id,paketti_id,date,kuvaus) VALUES (?,?,datetime(),?)");
            p.setInt(1, paikka_id);
            p.setInt(2, paketti_id);
            p.setString(3, kuvaus);
            p.executeUpdate();
            System.out.println("Tapahtuma lisätty");

        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    public void haeTapahtumatPaikasta(String paivamaara, String paikannimi) throws SQLException {
        try {
            PreparedStatement p = db.prepareStatement("SELECT COUNT(Tapahtumat.id) AS Tapahtumia FROM Tapahtumat, Paikat WHERE Paikat.id = Tapahtumat.paikka_id AND DATE(Tapahtumat.date) = ? AND Paikat.paikannimi = ?");
            p.setString(1, paivamaara);
            p.setString(2, paikannimi);
            ResultSet r = p.executeQuery();

            if (r.next() == false) {
                System.out.println("Paikassa ei ollut tapahtumia annetulla päivämäärällä");
            } else {
                do {
                    System.out.println("Tapahtumia: " + r.getString("Tapahtumia"));
                } while (r.next());
            }

        } catch (SQLException e) {
            System.out.println(e);
        }

    }
}
