package Tikape;

import java.sql.*;

/**
 *
 * @author villemanninen
 */
public class Main {

    /**
     * @param args the command line arguments
     * @throws java.sql.SQLException
     */
    public static void main(String[] args) throws SQLException {
        Connection db = DriverManager.getConnection("jdbc:sqlite:testi.db");
        Statement s = db.createStatement();

    }

}
