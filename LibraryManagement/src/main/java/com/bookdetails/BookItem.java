package com.bookdetails;

public class BookItem {
    private String bookTitle;
    private String ISBN;
    private int availableBooks;

    public BookItem(String bookTitle, String ISBN, int availableBooks) {
        this.bookTitle = bookTitle;
        this.ISBN = ISBN;
        this.availableBooks = availableBooks;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public String getISBN() {
        return ISBN;
    }

    public int getAvailableBooks() {
        return availableBooks;
    }

    public void setAvailableBooks(int available) {
        this.availableBooks = available;
    }
}
