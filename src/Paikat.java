
import java.sql.*;

public class Paikat {

    private final Connection db;

    public Paikat(Connection db) throws SQLException {
        this.db = db;
    }

    public void uusiPaikka(String paikannimi) throws SQLException {
        try {
            PreparedStatement p = db.prepareStatement("INSERT INTO Paikat (paikannimi) VALUES (?)");
            p.setString(1, paikannimi);
            p.executeUpdate();
            System.out.println("Paikka lisätty");
        } catch (SQLException e) {
            System.out.println("Pakettia ei voitu lisätä");
            System.out.println(e);
        }
    }
}
