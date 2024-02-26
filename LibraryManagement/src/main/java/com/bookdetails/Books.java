package com.bookdetails;

import java.time.LocalDate;

public class Books implements Comparable<Books>{
    private int book_id;
    private String title;
    private Author author;
    private String ISBN;
    private String genre;
    private float rating;
    private LocalDate publishedDate;


    public Books(int book_id, String title, Author author, String ISBN, String genre, float rating, LocalDate publishedDate) {
        this.book_id = book_id;
        this.title = title;
        this.author = author;
        this.ISBN = ISBN;
        this.genre = genre;
        this.rating = rating;
        this.publishedDate = publishedDate;
    }

    public String getTitle() {
        return title;
    }


    public String getISBN() {
        return ISBN;
    }

    public String getGenre() {
        return genre;
    }

    public float getRating() {
        return rating;
    }

    public Author getAuthor() {
        return author;
    }

    public void setRating(float rating)
    {
        this.rating = rating;
    }

    @Override
    public String toString() {
        String bookData = " ";
        return bookData = String.format("%-8s %-50s %-20s %-15s %-20s %-10s %-15s%n", book_id, title, author.name, ISBN, genre, rating, publishedDate);

    }

    @Override
    public int compareTo(Books o) {
        return Double.compare(o.getRating(),this.getRating());
    }

    public int getBook_id() {
        return book_id;
    }

    public LocalDate getPublishedDate() {
        return publishedDate;
    }
}
