package com.payment;

import com.borrowdetails.Borrow;
import com.databaseconnection.DBConnection;
import oracle.jdbc.proxy.annotation.Pre;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

/**
 * Payment class is implement to calculate the fine amount according to the due date
 * @author Sanjai Prakash
 * @since 23 Feb 2024
 */
public class Payment {
    private int payment_id;
    private int borrow_id;
    private Double fine;

    public double calculateFine(int account_id)
    {
        try
        {
            int fine =0;
            String fineCalculateQuery = "SELECT p.fine FROM LibraryManagement.Payment p JOIN LibraryManagement.Borrow b ON p.borrow_id = b.borrow_id JOIN LibraryManagement.Account a ON b.account_id = a.account_id WHERE a.account_id = ?";
            PreparedStatement fineCalculateStatement = DBConnection.getDBConnection().prepareStatement(fineCalculateQuery);
            ResultSet fineCalculateSet = fineCalculateStatement.executeQuery();
            if(fineCalculateSet.next())
            {
                fineCalculateSet.getDouble()
            }

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

}
