package com.persondetails;
import com.databaseconnection.DBConnection;
import com.exceptions.NumberInputException;
import com.exceptions.Validate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class Admin extends Person{
    private LibrarianStatus status;
    private LocalDate joinDate;
    public Admin()
    {
    }
    public Admin(int librarianID, String qualifications,LocalDate joinDate, LibrarianStatus status) {
        this.joinDate=joinDate;
        this.status = status;
    }
    public Admin(String name, Gender gender, String emailAddress, String mobileNumber, String address, LocalDate joinDate, LibrarianStatus status) {
        super(name, gender, emailAddress, mobileNumber, address);
        this.joinDate = joinDate;
        this.status = status;
    }
    public LibrarianStatus getStatus() {
        return status;
    }
    BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
    public LocalDate getJoinDate() {
        return joinDate;
    }

    public void blockMember() throws SQLException {
        try
        {
            System.out.println("Enter the account_ID to Block");
            int account_id = Integer.parseInt(Validate.numberValidate(input.readLine()));
            String getStatusIdQuery = "SELECT status_id FROM LibraryManagement.Status WHERE account_id = ?";
            PreparedStatement getStatusIdStatement = DBConnection.getDBConnection().prepareStatement(getStatusIdQuery);
            getStatusIdStatement.setInt(1, account_id);
            ResultSet getStatusIdResult = getStatusIdStatement.executeQuery();

            if (getStatusIdResult.next()) {
                int status_id = getStatusIdResult.getInt("status_id");
                String blockQuery = "UPDATE LibraryManagement.Status SET membership_status='BLOCKED' WHERE status_id = ?";
                PreparedStatement blockStatement = DBConnection.getDBConnection().prepareStatement(blockQuery);
                blockStatement.setInt(1, status_id);
                int rowsAffected = blockStatement.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println("Account blocked successfully.");
                } else {
                    System.out.println("Failed to block the account.");
                }
            } else {
                System.out.println("Account not found in the Status table.");
            }
        } catch (NumberInputException | IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    public void unblockMember() {
        try {
            System.out.println("Enter the account_ID to Activate");
            int account_id = Integer.parseInt(Validate.numberValidate(input.readLine()));

            // Check if the account exists in the Status table
            String getStatusIdQuery = "SELECT status_id FROM LibraryManagement.Status WHERE account_id = ?";
            PreparedStatement getStatusIdStatement = DBConnection.getDBConnection().prepareStatement(getStatusIdQuery);
            getStatusIdStatement.setInt(1, account_id);
            ResultSet getStatusIdResult = getStatusIdStatement.executeQuery();

            if (getStatusIdResult.next()) {
                int status_id = getStatusIdResult.getInt("status_id");
                // Activate the account
                String activateQuery = "UPDATE LibraryManagement.Status SET membership_status='ACTIVE' WHERE status_id = ?";
                PreparedStatement activateStatement = DBConnection.getDBConnection().prepareStatement(activateQuery);
                activateStatement.setInt(1, status_id);
                int rowsAffected = activateStatement.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println("Account activated successfully.");
                } else {
                    System.out.println("Failed to activate the account.");
                }
            } else {
                System.out.println("Account not found in the Status table.");
            }
        }
        catch (IOException | NumberInputException | ClassNotFoundException | SQLException e)
        {
            System.out.println(e.getMessage());
        }
    }
}