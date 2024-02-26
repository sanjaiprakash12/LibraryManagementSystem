package com.persondetails;

import com.bookdetails.Books;
import com.databaseconnection.DBConnection;
import com.searchdetails.BookArchive;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;

public class ReportManipulation {
    public static void bookAvailability() throws SQLException, ClassNotFoundException {
        try
        {
            String bookAvailabilityQuery = "Select b.book_id,b.title,r.Availability from LibraryManagement.Books b Join LibraryManagement.Rack r on b.book_id = r.book_id";
            PreparedStatement bookAvailability = DBConnection.getDBConnection().prepareStatement(bookAvailabilityQuery);
            ResultSet bookAvailSet = bookAvailability.executeQuery();
            System.out.println("-----------------------------------------------------------------------------");
            System.out.printf("%-10s %-50s %-15s%n","Book ID","Title","Availability");
            System.out.println("-----------------------------------------------------------------------------");
            while(bookAvailSet.next())
            {
                System.out.format("%-10s %-50s %-15s%n",bookAvailSet.getInt(1) ,bookAvailSet.getString(2), bookAvailSet.getInt(3));
            }
            System.out.println("==============================================================================");
        }
        catch ( SQLException| ClassNotFoundException e)
        {
            System.out.println(e.getMessage());
        }
    }
    public static void borrowedFine() {
        try
        {
            String borrowedDetails = "SELECT p.person_id, p.name, b.book_id, b.title, py.fine FROM LibraryManagement.Person p JOIN LibraryManagement.Borrow bo ON p.person_id = bo.account_id JOIN LibraryManagement.Books b ON bo.book_id = b.book_id LEFT JOIN LibraryManagement.Payment py ON bo.borrow_id = py.borrow_id";
            PreparedStatement borrowedStatement = DBConnection.getDBConnection().prepareStatement(borrowedDetails);
            ResultSet borrowedSet = borrowedStatement.executeQuery();
            System.out.println("--------------------------------------------------------------------------------");
            while(borrowedSet.next())
            {
                System.out.format("%-10d %-20s %-10d %-20s %-20.2f%n", borrowedSet.getInt(1),borrowedSet.getString(2),borrowedSet.getInt(3),borrowedSet.getString(4),borrowedSet.getDouble(5));
            }
            System.out.println("--------------------------------------------------------------------------------");
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println(e.getMessage());;
        }
    }

    public static void allPatronList() {
        try {
            String allPatronListQuery = "select p.person_id, p.name, p.Gender, p.email, p.MobileNumber, p.Person_Address, a.Account_type from LibraryManagement.Person p join LibraryManagement.Account a on a.person_ID=p.person_ID";
            PreparedStatement statement = DBConnection.getDBConnection().prepareStatement(allPatronListQuery);
            ResultSet set = statement.executeQuery();
            System.out.println();
            System.out.println("--------------------------------------------------------------------------------------------------------------------------------------");
            while (set.next())
            {
                System.out.format("%-10s %-20s %-20s %-30s %-20s %-20s %-20s%n", set.getInt(1), set.getString(2),set.getString(3), set.getString(4),set.getString(5), set.getString(6), set.getString(7));
            }
            System.out.println("---------------------------------------------------------------------------------------------------------------------------------------");
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    public static void highlyRatedBooks() {
        try
        {
            BookArchive bookArchive = new BookArchive();
            LinkedList<Books> bookslist = bookArchive.getBookList();
            Collections.sort(bookslist);
            int i = 0;
            for (Books book: bookslist)
            {
                System.out.printf("%-50s %-10s%n",book.getTitle(),book.getRating());
                if((i++)==10)
                {
                    break;
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }
    public static void explore() {
        System.out.println("+---------------------------------------------------------------+");
        System.out.println("|   Libraries are the perfect home for the interminably curious |");
        System.out.println("+---------------------------------------------------------------+");
        System.out.println("=================================================================================");
        System.out.println("-------------------------------The High Rated Books------------------------------");
        highlyRatedBooks();
        System.out.println();
    }
}
