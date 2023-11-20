package com.example.bibliotek2;

import javafx.collections.ObservableList;
import javafx.scene.control.TextArea;
import java.sql.*;

public class BookDaoImpl implements BookDao {
    private TextArea status;
    private Connection con; // forbindelsen til databasen
    public BookDaoImpl(TextArea s)  {
        status = s;
        try{
            con = DriverManager.getConnection("jdbc:sqlserver://10.176.111.34:1433;database=CSe2023t_t_11_LibDB;userName=CSe2023t_t_11;password=CSe2023tT11#23;encrypt=true;trustServerCertificate=true");
        } catch (SQLException e) {
            System.err.println("Kan ikke skabe forbindelse:" + e.getErrorCode() + e.getMessage());
            status.appendText("Kan ikke få forbindelse til databasen\n");
        }
        System.out.println("Forbundet til databasen... ");
        status.appendText("Forbundet til database\n");
    }

    public boolean saveBook(Book book) {
        try{
            Statement database = con.createStatement();
            String sql = "INSERT INTO BOOK (isbn, title, type) VALUES "
                    + "('"+book.getIsbn()
                    + "','"+book.getTitle()
                    + "','"+book.getCategory()
                    + "')";
            database.executeUpdate(sql);
            status.appendText("Bog oprettet\n");
            return true;
        } catch (SQLException e){
            System.err.println("Kan ikke indsætte record");
            status.appendText("Bog med samme isbn findes allerede\n");
            return false;
        }
    }

    public void getAllBooks(ObservableList<Book> tabeldata) {
        tabeldata.clear();
        int antal = 0;
        try{
            Statement database = con.createStatement();
            String sql = "SELECT * FROM Book";
            ResultSet rs = database.executeQuery(sql);
            while (rs.next()) {
                String isbn     = rs.getString("isbn");
                String title    = rs.getString("title");
                String type     = rs.getString("type");

                Book book = new Book(isbn, title, type);
                tabeldata.add(book);
                ++antal;
            }
            status.appendText(antal + " bøger fundet\n");
        } catch (SQLException e){
            System.err.println("Kan ikke læse records");
            status.appendText("Fejl ved læsning af bøger\n");
        }

    }

    public boolean deleteBook(Book book) {
        try{
            Statement database = con.createStatement();
            String sql = "DELETE FROM book WHERE isbn = '" + book.getIsbn() + "'";
            database.executeUpdate(sql);
            status.appendText("Bog slettet\n");
            return true;
        } catch (SQLException e){
            System.err.println("Sletning lykkedes ikke"+e.getMessage());
            status.appendText("Bog blev ikke slettet. Måske er den lånt ud.\n");
            return false;
        }
    }

    @Override
    public void updateBook(Book book) {
        try{
            Statement database = con.createStatement();
            String sql = "UPDATE book SET isbn='"
                    + book.getIsbn() + "', title='"
                    + book.getTitle() + "', type='"
                    + book.getCategory() + "' WHERE isbn='" + book.getIsbn() + "'";
            database.executeUpdate(sql);
            status.appendText("Bog opdateret\n");
        } catch (SQLException e){
            System.err.println("Opdatering lykkedes ikke: "+e.getMessage());
            status.appendText("Bog blev ikke opdateret\n");
        }
    }

    public void findBook(String isbn, String titel, String type, ObservableList<Book> tabeldata) {
        tabeldata.clear();
        int antal = 0;
        try{
            Statement database = con.createStatement();
            String sql = "SELECT * FROM Book WHERE isbn = '"
                    + isbn + "' OR title LIKE '"
                    + titel + "' OR type = '" + type + "'";
            ResultSet rs = database.executeQuery(sql);
            while (rs.next()) {
                String isbn1     = rs.getString("isbn");
                String title1    = rs.getString("title");
                String type1     = rs.getString("type");

                Book book = new Book(isbn1, title1, type1);
                tabeldata.add(book);
                ++antal;
            }
            status.appendText(antal + " bøger fundet\n");
        } catch (SQLException e){
            System.err.println("Kan ikke læse records: "+e.getMessage());
            status.appendText("Fejl ved læsning af bøger\n");
        }
    }

}