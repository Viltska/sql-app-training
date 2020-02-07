
package Tikape;

import java.sql.*;

public class Tapahtumat {

    private final Connection db;
    private final Statement s;

    public Tapahtumat(Connection db) throws SQLException {
        this.db = db;
        this.s = db.createStatement();

    }

}
