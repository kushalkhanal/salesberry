
package com.groupc.saleberry.Database;

import java.sql.Connection;
import java.sql.ResultSet;


public interface DbConnection {
    Connection openConnection();
    void closeConnection(Connection conn);
    ResultSet runQuery(Connection conn, String query);
    int executeUpdate(Connection conn, String query);
}
