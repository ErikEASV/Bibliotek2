package com.example.bibliotek2;

import javafx.beans.property.SimpleStringProperty;

public class Book {
    private SimpleStringProperty isbn;
    private SimpleStringProperty title;
    private SimpleStringProperty category;

    public Book (String isbn,String title,String category){
        this.isbn = new SimpleStringProperty(isbn);
        this.title = new SimpleStringProperty(title);
        this.category = new SimpleStringProperty(category);
    }

    public String getIsbn(){
        return isbn.get();
    }

    public String getTitle(){
        return title.get();
    }

    public String getCategory() { return category.get(); }

    public void setIsbn(String s) { isbn.set(s); }

    public void setTitle(String s){
        title.set(s);
    }

    public void setCategory(String s) { category.set(s); }


}