package com.persondetails;


import com.databaseconnection.DBConnection;
import com.primarykeyprovider.PrimaryKey;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
/**
 * Librarians class extends the Person which is for describe the specific attributes and methods.
 * @author Sanjai Prakash
 * @since 16 Feb 2024
 */
public class Librarian extends Person{
    private LibrarianStatus status;
    private LocalDate joinDate;
    public Librarian()
    {
        this.joinDate = null;
        this.status = null;
    }


    public Librarian(String name, Gender gender, String emailAddress, String mobileNumber, String address, LocalDate joinDate, LibrarianStatus status) {
        super( name, gender, emailAddress,mobileNumber, address);
        this.joinDate = joinDate;
        this.status = status;
    }

    public Librarian( String name, Gender gender, String emailAddress, String contactNumber, String address, LibrarianStatus status, LocalDate joinDate) {

        this.status = status;
        this.joinDate = joinDate;
        this.input = input;
    }

    public Librarian(String name, Gender gender, String emailAddress, String mobileNumber, String address, LibrarianStatus status, LocalDate joinDate, BufferedReader input) {
        super(name, gender, emailAddress, mobileNumber, address);
        this.status = status;
        this.joinDate = joinDate;
        this.input = input;
    }

    public LibrarianStatus getStatus() {
        return status;
    }
    public LocalDate getJoinDate() {
        return joinDate;
    }
    BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

    public void addBooks()  {
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        boolean flag =true;
        while(flag) {
            try {
                System.out.println("Enter Book's title ");
                String title = input.readLine();
                System.out.println("Enter Book's Author ");
                String author = input.readLine();
                System.out.println("Enter Book's ISBN number ");
                String ISBN = input.readLine();
                System.out.println("Enter Book's genre ");
                String genre = input.readLine();
                System.out.println("Enter Book's rating ");
                int rating = Integer.parseInt(input.readLine());
                System.out.println("Enter Book Published Date (yyyy/MM/dd) ");
                String publishedDate = input.readLine();

                int bookId = PrimaryKey.getPrimaryKey("Books");

                String addBookQuery = "INSERT INTO LIBRARYMANAGEMENT.Books(book_id, title, author, ISBN, genre, rating, publishedDate) VALUES (?, ?, ?, ?, ?, ?, TO_DATE(?, 'yyyy/MM/dd'))";
                PreparedStatement addBookStatement = DBConnection.getDBConnection().prepareStatement(addBookQuery);
                addBookStatement.setInt(1, bookId);
                addBookStatement.setString(2, title);
                addBookStatement.setString(3, author);
                addBookStatement.setString(4, ISBN);
                addBookStatement.setString(5, genre);
                addBookStatement.setInt(6, rating);
                addBookStatement.setString(7, publishedDate); // Convert LocalDate to java.sql.Date
                if (addBookStatement.executeUpdate() > 0) {
                    System.out.println("Book Added successfully");
                    System.out.println("Enter Rack ID ");
                    int rackId = Integer.parseInt(input.readLine());

                    System.out.println("Enter Availability (0 or 1):");
                    int availability = Integer.parseInt(input.readLine());

                    String addRackQuery = "INSERT INTO LIBRARYMANAGEMENT.Rack(rack_id, book_id, availability) VALUES (?,?,?)";
                    PreparedStatement addRackStatement = DBConnection.getDBConnection().prepareStatement(addRackQuery);
                    addRackStatement.setInt(1, rackId);
                    addRackStatement.setInt(2, bookId);
                    addRackStatement.setInt(3, availability);

                    if (addRackStatement.executeUpdate() > 0) {
                        System.out.println("Book added to Rack successfully");
                        flag = false;
                    } else {
                        System.out.println("Failed to add book to Rack");
                    }
                } else {
                    System.out.println("Failed to add Book");
                }
            } catch (SQLException | ClassNotFoundException | IOException e) {
                flag = true;
                System.out.println(e.getMessage());
            }
        }
    }
    public void removeBookByTitle()  {
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        boolean flag = true;
        while (flag) {
            try {
                int book_id = -1;
                int numberOfAvailableBooks = 0;
                System.out.println("Enter Book's title to remove ");
                String title = input.readLine();
                // Retrieve the book ID using the title

                String getBookIdQuery = "SELECT book_id FROM LibraryManagement.Books WHERE title = ?";
                PreparedStatement getBookIdStatement = DBConnection.getDBConnection().prepareStatement(getBookIdQuery);
                getBookIdStatement.setString(1, title);

                ResultSet resultSet = getBookIdStatement.executeQuery();
                if (resultSet.next()) {
                    book_id = resultSet.getInt(1);
                    System.out.println("Executed");
                }

                // Retrieve the number of Available books available
                String getBookAvailabilityQuery = "SELECT r.availability FROM LIBRARYMANAGEMENT.books b JOIN LIBRARYMANAGEMENT.rack r ON r.Book_id = ?";
                PreparedStatement getBookAvailabilityStatement = DBConnection.getDBConnection().prepareStatement(getBookAvailabilityQuery);
                getBookAvailabilityStatement.setInt(1, book_id);
                ResultSet getBookAvailabilitySet = getBookAvailabilityStatement.executeQuery();
                if (getBookAvailabilitySet.next()) {
                    numberOfAvailableBooks = getBookAvailabilitySet.getInt("availability");
                }
                if (numberOfAvailableBooks == 0) {
                    System.out.println("The Available books is zero");
                } else {
                    System.out.println("Available Books : " + numberOfAvailableBooks);
                    System.out.println("+--------------------------------+");
                    System.out.println("|       Enter your option        |");
                    System.out.println("+--------------------------------+");
                    System.out.println("| 1. Remove all book quantity    |");
                    System.out.println("| 2. Specific Quantity to remove |");
                    System.out.println("+--------------------------------+");

                    int option = Integer.parseInt(input.readLine());
                    if (option == 1) {
                        if (book_id != -1) {
                            String deleteRackQuery = "DELETE FROM LibraryManagement.Rack WHERE book_id = ?";
                            PreparedStatement deleteRackStatement = DBConnection.getDBConnection().prepareStatement(deleteRackQuery);
                            deleteRackStatement.setInt(1, book_id);
                            int rowsAffectedRack = deleteRackStatement.executeUpdate();

                            String deleteBookQuery = "DELETE FROM LibraryManagement.Books WHERE book_id = ?";
                            PreparedStatement deleteBookStatement = DBConnection.getDBConnection().prepareStatement(deleteBookQuery);
                            deleteBookStatement.setInt(1, book_id);
                            int rowsAffectedBooks = deleteBookStatement.executeUpdate();

                            if (rowsAffectedBooks > 0) {
                                System.out.println("Book removed successfully from Books table");
                            } else {
                                System.out.println("Failed to remove Book from Books table");
                            }

                            if (rowsAffectedRack > 0) {
                                System.out.println("Book removed successfully from Rack table");
                            } else {
                                System.out.println("Failed to remove Book from Rack table");
                            }
                        } else {
                            System.out.println("Book not found with the given title");
                        }
                    } else if (option == 2) {
                        System.out.println("+----------------------------------+");
                        System.out.println("| Enter number of books to remove: |");
                        System.out.println("+----------------------------------+");
                        int booksToRemove = Integer.parseInt(input.readLine());
                        if (booksToRemove <= numberOfAvailableBooks) {
                            String updateQuery = "Update LibraryManagement.rack SET Availability = Availability - ? where book_id = ? ";
                            PreparedStatement UpdateQueryStatement = DBConnection.getDBConnection().prepareStatement(updateQuery);
                            UpdateQueryStatement.setInt(1, booksToRemove);
                            UpdateQueryStatement.setInt(2, book_id);
                            if (UpdateQueryStatement.executeUpdate() > 0) {
                                System.out.println("Rack Executed Successfully");
                                flag = false;
                            }
                        }
                    }
                    else
                    {
                        System.out.println("Enter valid input");
                        flag = true;
                    }
                }
            } catch (NumberFormatException | SQLException | ClassNotFoundException| IOException e) {
                System.out.println(e.getMessage());
                flag = true;
            }
        }
    }
}
