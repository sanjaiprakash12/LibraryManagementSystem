package com.searchdetails;
import com.bookdetails.Books;

import java.sql.SQLException;
import java.util.LinkedList;

interface Search {
    LinkedList<Books> bookList = new LinkedList<>();
    public void sortByTitle() throws SQLException, ClassNotFoundException;
    public void sortByAuthor();
    public void sortByGenre();
    public void sortByRating();

}
