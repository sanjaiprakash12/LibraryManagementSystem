package com.searchdetails;


import com.bookdetails.Author;
import com.bookdetails.Books;
import com.bookdetails.SortByTitle;
import com.databaseconnection.DBConnection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class BookArchive implements Search {

    public LinkedList<Books> getBookList() throws SQLException, ClassNotFoundException {
        LinkedList<Books> list = new LinkedList<>();
        String Query = "Select * from LibraryManagement.Books";
        PreparedStatement showBooksStatement = DBConnection.getDBConnection().prepareStatement(Query);
        ResultSet resultSet = showBooksStatement.executeQuery();
        while (resultSet.next()) {
            list.add(new Books(resultSet.getInt(1), resultSet.getString(2), new Author(resultSet.getString(3)),
                    resultSet.getString(4), resultSet.getString(5), resultSet.getFloat(6),
                    resultSet.getDate(7).toLocalDate()));
        }
        return list;
    }

    @Override
    public void sortByTitle(){
        try
        {
            LinkedList<Books>bookList = getBookList();
            bookList.sort(new SortByTitle());
            for(Books list:bookList)
            {
                System.out.println(list.getTitle());
            }
        }
        catch (SQLException| ClassNotFoundException e)
        {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void sortByAuthor() {
        try
        {
            System.out.println("---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
            System.out.format("%-8s %-40s %-40s %-25s %-30s %-20s %-25s%n", "Book ID", "Title", "Author", "ISBN", "Genre", "Rating", "PublishedDate");
            System.out.println("---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
            LinkedList<Books>bookList = getBookList();
            List<Books> sort = bookList.stream().sorted(Comparator.comparing(books->books.getAuthor().getName())).collect(Collectors.toList());
            sort.forEach(book -> System.out.format("%-8d %-40s %-40s %-25s %-30s %-20.1f %-25s%n",
                    book.getBook_id(), book.getTitle(), book.getAuthor().getName(), book.getISBN(),
                    book.getGenre(), book.getRating(), book.getPublishedDate()));
            System.out.println("---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
        }
        catch (SQLException| ClassNotFoundException e)
        {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void sortByGenre() {
        try
        {
            LinkedList<Books>bookList = getBookList();
            System.out.println("---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
            System.out.format("%-8s %-40s %-40s %-25s %-30s %-20s %-25s%n", "Book ID", "Title", "Author", "ISBN", "Genre", "Rating", "PublishedDate");
            System.out.println("---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
            List<Books> sort = bookList.stream().sorted(Comparator.comparing(Books::getGenre)).collect(Collectors.toList());
            sort.forEach(book -> System.out.format("%-8d %-40s %-40s %-25s %-30s %-20.1f %-25s%n",
                    book.getBook_id(), book.getTitle(), book.getAuthor().getName(), book.getISBN(),
                    book.getGenre(), book.getRating(), book.getPublishedDate()));
            System.out.println("---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
        }
        catch (SQLException| ClassNotFoundException e)
        {
            System.out.println(e.getMessage());
        }
    }
    @Override
    public void sortByRating() {
        try
        {
            LinkedList<Books>bookList = getBookList();
            System.out.println("---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
            System.out.printf("%-8s %-50s %-20s %-15s %-20s %-10s %-15s%n", "Book ID", "Title", "Author", "ISBN", "Genre", "Rating", "PublishedDate");
            System.out.println("---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
            Collections.sort(bookList);
            for(Books list:bookList)
            {
                System.out.println(list);
            }
            System.out.println("---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");

        }
        catch (SQLException| ClassNotFoundException e)
        {
            System.out.println(e.getMessage());
        }
    }
}
