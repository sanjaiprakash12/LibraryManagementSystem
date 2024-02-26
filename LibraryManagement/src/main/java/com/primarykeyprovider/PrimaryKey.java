package com.primarykeyprovider;

import com.databaseconnection.DBConnection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class PrimaryKey {
    public static int getPrimaryKey(String table_name) throws SQLException, ClassNotFoundException {
        Statement stat = DBConnection.getDBConnection().createStatement();
        String insert = "";
        if(table_name.equalsIgnoreCase("Status"))
        {
            insert = "status_id";
        }
        else if (table_name.equalsIgnoreCase("Feedback")) {
            insert = "feedback_id";
        }
        else if (table_name.equalsIgnoreCase("Reservations")) {
            insert = "reservation_id";
        }
        else if (table_name.equalsIgnoreCase("Library")) {
            insert = "library_id";
        }
        else if (table_name.equalsIgnoreCase("Borrow")) {
            insert = "borrow_id";
        }
        else if (table_name.equalsIgnoreCase("Books")) {
            insert = "book_id";
        }
        else if (table_name.equalsIgnoreCase("Account")) {
            insert = "account_id";
        }
        else if (table_name.equalsIgnoreCase("Person")) {
            insert = "person_id";
        }
        else if (table_name.equalsIgnoreCase("payment")) {
            insert = "payment_id";
        }
        String query = "SELECT MAX("+insert+") FROM LIBRARYMANAGEMENT."+table_name;
        ResultSet resultSet = stat.executeQuery(query);
        int max = 0;
        if(resultSet.next())
        {
              max = resultSet.getInt(1);
              return max+1;
        }
        return max;
    }
}
