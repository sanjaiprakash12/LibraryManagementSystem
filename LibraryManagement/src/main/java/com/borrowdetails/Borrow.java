package com.borrowdetails;

import com.databaseconnection.DBConnection;
import com.exceptions.NumberInputException;
import com.exceptions.Validate;
import com.primarykeyprovider.PrimaryKey;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class Borrow {

    private int borrow_id;
    private int book_id;
    private int customer_id;
    private LocalDate borrowedDate;
    private LocalDate dueDate;
    public Borrow()
    {
        borrow_id=0;
        book_id = 0;
        customer_id=0;
        borrowedDate=null;
        dueDate = null;
    }

    public Borrow(int borrow_id, int book_id, int customer_id, LocalDate borrowedDate, LocalDate dueDate)
    {
        this.borrow_id = borrow_id;
        this.book_id = book_id;
        this.customer_id = customer_id;
        this.borrowedDate = borrowedDate;
        this.dueDate = dueDate;
    }

    public int getBorrow_id() {
        return borrow_id;
    }

    public void setBorrow_id(int borrow_id) {
        this.borrow_id = borrow_id;
    }

    public int getBook_id() {
        return book_id;
    }

    public void setBook_id(int book_id) {
        this.book_id = book_id;
    }

    public int getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(int customer_id) {
        this.customer_id = customer_id;
    }

    public LocalDate getBorrowedDate() {
        return borrowedDate;
    }

    public void setBorrowedDate(LocalDate borrowedDate) {
        this.borrowedDate = borrowedDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    @Override
    public String toString()
    {
        return "book_id= " + book_id + ", borrow_id= " + borrow_id + ", customer_id= " + customer_id + ", borrowedDate= " + borrowedDate ;
    }
    static BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
    Reservation bookReserved = new Reservation();
    public void borrowBooks(int account_id) throws IOException {
        boolean flag = true;
        while(flag) {
            try {
                System.out.println("Enter the book title : ");
                String title = input.readLine();

                HashMap<Integer, String> map = new HashMap<>();

                String insertInMapQuery = "SELECT book_id, title FROM LibraryManagement.Books";
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
                int availableQuantity = -1;
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
                    PreparedStatement updateQueryStatement = DBConnection.getDBConnection().prepareStatement(updateQuery);
                    updateQueryStatement.setInt(1, book_id);
                    updateQueryStatement.executeUpdate();


                    String borrowQuery = "INSERT INTO LIBRARYMANAGEMENT.borrow (borrow_id,book_id,account_id,borrowed_date,due_date,return_Date) Values (?,?,?,?,?,NULL)";
                    PreparedStatement borrowStatement = DBConnection.getDBConnection().prepareStatement(borrowQuery);
                    int borrow_id = PrimaryKey.getPrimaryKey("Borrow");
                    borrowStatement.setInt(1, borrow_id);
                    borrowStatement.setInt(2, book_id);
                    borrowStatement.setInt(3, account_id);
                    java.sql.Date sqlDateNow = java.sql.Date.valueOf(LocalDate.now());
                    borrowStatement.setDate(4, sqlDateNow);
                    java.sql.Date dueSqlDatePlus = java.sql.Date.valueOf(LocalDate.now().plusDays(10));
                    borrowStatement.setDate(5, dueSqlDatePlus);
                    borrowStatement.executeUpdate();
                    paymentUpdate(borrow_id);
                    flag = false;
                    break;
                } else {
                    int reservedBookCount = 0;
                    String reservationAvailabilityCheck = "Select count(book_id) from LIBRARYMANAGEMENT.Reservations where book_id = ?";
                    PreparedStatement reservationAvailStatement = DBConnection.getDBConnection().prepareStatement(reservationAvailabilityCheck);
                    reservationAvailStatement.setInt(1, book_id);
                    ResultSet reservationAvailSet = reservationAvailStatement.executeQuery();
                    if (reservationAvailSet.next()) {
                        System.out.println("The Book is Not available in our Archive ,The Book is available in your Reservation Cluster ");
                        reservedBookCount = reservationAvailSet.getInt(1) ;
                    }
                    if(reservedBookCount > 0) {
                        System.out.println("1.Borrow");
                        System.out.println("2.Exit");
                        int option = Integer.parseInt(input.readLine());
                        if (option == 1) {
                            String borrowQuery = "INSERT INTO LIBRARYMANAGEMENT.borrow (borrow_id, book_id, account_id, borrowed_date, due_date, return_Date) VALUES (?, ?, ?, ?, ?, NULL)";
                            PreparedStatement borrowStatement = DBConnection.getDBConnection().prepareStatement(borrowQuery);
                            int borrow_id = PrimaryKey.getPrimaryKey("Borrow");
                            borrowStatement.setInt(1, borrow_id);
                            borrowStatement.setInt(2, book_id);
                            borrowStatement.setInt(3, account_id);
                            java.sql.Date sqlDate = java.sql.Date.valueOf(LocalDate.now());
                            borrowStatement.setDate(4, sqlDate);
                            java.sql.Date dueSqlDate = java.sql.Date.valueOf(LocalDate.now().plusDays(10));
                            borrowStatement.setDate(5, dueSqlDate);
                            if (borrowStatement.executeUpdate() > 0) {
                                System.out.println("Borrow Your Reserved Book");
                            }

                            String removeFromReservations = "DELETE FROM LIBRARYMANAGEMENT.Reservations WHERE book_id = ?";
                            PreparedStatement removeStatement = DBConnection.getDBConnection().prepareStatement(removeFromReservations);
                            removeStatement.setInt(1, book_id);
                            if (removeStatement.executeUpdate() > 0) {
                                System.out.println("Removed from reservation Successfully");
                            }
                            paymentUpdate(borrow_id);
                            flag = false;
                        }
                    }
                    else
                    {
                        System.out.println("Book Not Fund");
                        System.out.println("Suggested books with same genre");
                        String suggestedBookQuery = "Select title,author,genre from LibraryManagement.Book where genre = (select genre from LibraryManagement.Book where Book = ?)";
                        PreparedStatement suggestedBookStatement = DBConnection.getDBConnection().prepareStatement(suggestedBookQuery);
                        ResultSet suggestedBookSet = suggestedBookStatement.executeQuery();
                        while(suggestedBookSet.next())
                        {
                            System.out.println(String.format("| %-14s | %-14s | %-14s |", suggestedBookSet.getString(1),suggestedBookSet.getString(2), suggestedBookSet.getString(2)));
                        }
                        flag =
                    }
                }
            } catch (SQLException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public static void paymentUpdate(int borrow_id)
    {
        try
        {
            String paymentQuery = "Insert INTO LIBRARYMANAGEMENT.payment (payment_id,borrow_id,fine,paid) VALUES(?,?,?,?)";
            PreparedStatement paymentStatement = DBConnection.getDBConnection().prepareStatement(paymentQuery);
            paymentStatement.setInt(1,PrimaryKey.getPrimaryKey("Payment"));
            paymentStatement.setInt(2,borrow_id);
            paymentStatement.setInt(3,0);
            paymentStatement.setString(4,null);
            if(paymentStatement.executeUpdate()>0)
            {
                System.out.println("Payment table Inserted");
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }


    }
    public static void returnBooks (int account_id){
        boolean flag = true;
        while(flag) {
            try {
                String borrowedBooks = "SELECT bor.borrow_id,b.book_id, b.title,bor.due_date,bor.due_date FROM LibraryManagement.Borrow bor JOIN LibraryManagement.Books b ON bor.book_id = b.book_id WHERE bor.account_id = ?;";
                PreparedStatement borrowedBooksStatement = DBConnection.getDBConnection().prepareStatement(borrowedBooks);
                borrowedBooksStatement.setInt(1, account_id);
                ResultSet borrowedBooksSet = borrowedBooksStatement.executeQuery();
                System.out.print("Borrow_ID\t\tBook_ID\t\tBook_Title\t\tDuedate");
                while (borrowedBooksSet.next()) {
                    System.out.print(borrowedBooksSet.getInt(1));
                    System.out.print(borrowedBooksSet.getInt(2));
                    System.out.print("\t" + borrowedBooksSet.getString(3));
                    System.out.print("\t" + borrowedBooksSet.getDate(4));
                }
                System.out.println("Select Book Title want to return");
                String bookTitle = input.readLine();
                String bookIdQuery = "SELECT book_id FROM LibraryManagement.Books where title = ?";
                PreparedStatement bookIdStatement = DBConnection.getDBConnection().prepareStatement(bookIdQuery);
                bookIdStatement.setString(1, bookTitle);
                ResultSet bookIdSet = bookIdStatement.getResultSet();
                int book_id = bookIdSet.getInt(1);
                String updateQuery = "Update LibraryManagement.rack SET Availability = Availability + 1 where book_id = ? ";
                PreparedStatement updateQueryStatement = DBConnection.getDBConnection().prepareStatement(updateQuery);
                updateQueryStatement.setInt(1, book_id);
                if (updateQueryStatement.executeUpdate() > 0) {
                    System.out.println("Returned the Book");
                }
            } catch (SQLException | ClassNotFoundException | IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}

