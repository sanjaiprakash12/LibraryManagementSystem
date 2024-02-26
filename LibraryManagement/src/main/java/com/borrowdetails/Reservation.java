package com.borrowdetails;

import com.databaseconnection.DBConnection;
import com.primarykeyprovider.PrimaryKey;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class Reservation {
    private int reservation_id;
    private int book_id;
    private int member_id;
    private LocalDate reservation_Date;

    public Reservation(int reservation_id, int book_id, int member_id, LocalDate reservation_Date) {
        this.reservation_id = reservation_id;
        this.book_id = book_id;
        this.member_id = member_id;
        this.reservation_Date = reservation_Date;
    }

    public Reservation() {
        this.reservation_id = 0;
        this.book_id = 0 ;
        this.member_id = 0;
        this.reservation_Date = null;
    }
    @Override
    public String toString()
    {
        return "reservation_id=" + reservation_id +
                ", book_id=" + book_id +
                ", member_id=" + member_id +
                ", reservation_Date=" + reservation_Date;
    }
    BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
    public void reservation(int account_id)  {
        try {
            System.out.println("Enter the book title : ");
            String title = input.readLine();

            HashMap<Integer, String> map = new HashMap<>(); //HashMap to store book_id as key and title as value

            String insertInMapQuery = "SELECT book_id, title FROM LibraryManagement.Books";// Query to select book_id, title from books
            PreparedStatement insertInMapStatement = DBConnection.getDBConnection().prepareStatement(insertInMapQuery);
            ResultSet insertSet = insertInMapStatement.executeQuery();
            while (insertSet.next()) {
                map.put(insertSet.getInt(1), insertSet.getString(2));
            }
            int book_id = -1;
            if (map.containsValue(title)) {

                for (Map.Entry<Integer, String> entry : map.entrySet()) {
                    if (entry.getValue().equals(title)) {
                        book_id = entry.getKey();
                        break;
                    }
                }
            }

            /*To check the availability of the book in rack*/

            int availableQuantity = -1;
            System.out.println("Book Id = " + book_id);
            String checkAvailability = "Select r.availability from LibraryManagement.rack r Join LibraryManagement.Books b ON r.book_id = ?";
            PreparedStatement checkAvailabilityStatement = DBConnection.getDBConnection().prepareStatement(checkAvailability);
            checkAvailabilityStatement.setInt(1, book_id);
            ResultSet checkAvailabilityResultSet = checkAvailabilityStatement.executeQuery();
            if (checkAvailabilityResultSet.next()) {
                availableQuantity = checkAvailabilityResultSet.getInt(1);
            }
            System.out.println(availableQuantity);
            if (availableQuantity > 0) {
                String updateQuery = "Update LibraryManagement.rack SET Availability = Availability - 1 where book_id = ? ";
                PreparedStatement UpdateQueryStatement = DBConnection.getDBConnection().prepareStatement(updateQuery);
                UpdateQueryStatement.setInt(1, book_id);
                if (UpdateQueryStatement.executeUpdate() > 0) {
                    System.out.println("UpdateQuery");
                }
                String borrowQuery = "INSERT INTO LIBRARYMANAGEMENT.Reservations (reservation_id,book_id,account_id,reservation_date) Values (?,?,?,?)";
                PreparedStatement ReservationStatement = DBConnection.getDBConnection().prepareStatement(borrowQuery);
                int reservation_id = PrimaryKey.getPrimaryKey("Reservations");
                ReservationStatement.setInt(1, reservation_id);
                ReservationStatement.setInt(2, book_id);
                ReservationStatement.setInt(3, account_id);
                java.sql.Date sqlDate = java.sql.Date.valueOf(LocalDate.now());
                ReservationStatement.setDate(4, sqlDate);
                if (ReservationStatement.executeUpdate() > 0) {
                    System.out.println("Inserted Borrow");
                } else {
                    System.out.println("Couldn't insert");
                }
            }
        }catch (SQLException | IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}

