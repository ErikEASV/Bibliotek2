package com.example.bibliotek2;

import javafx.collections.ObservableList;

public interface BookDao {

    // Mulige operationer på bogdatabasen
    public boolean saveBook(Book book);

    public void getAllBooks(ObservableList<Book> tabeldata);

    public boolean deleteBook(Book book);

    public void updateBook(Book book);

    public void findBook(String isbn, String titel, String type, ObservableList<Book> tabeldata);

}