package com.example.bibliotek2;

/*******
 * Enkel biblioteksapp, der fra en database kan vise og redigere bøger i en tabel.
 * EK nov 2023
 *******/

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import java.util.Optional;

public class BibliotekController {
    public TextArea meddelelse;;
    @FXML
    private TableView<Book> bogtabel = new TableView<Book>();
    @FXML
    private TableColumn<Book, String> isbnkolonne = new TableColumn();
    @FXML
    private TableColumn<Book, String> titelkolonne  = new TableColumn();
    @FXML
    private TableColumn<Book, String> typekolonne  = new TableColumn();

    // Def. af listen der holder dataene
    private final ObservableList<Book> tabeldata = FXCollections.observableArrayList();

    @FXML
    private Button insertBook, getAllBooks;

    private BookDao bdi;

    @FXML
    protected void insertBookHandler(Event e) {
        // Lav tom bog og åben redigeringsdialog, så bogen kan få data og blive oprettet
        Book book = new Book("","","");
        redigerLinje(book);
    }

    public void initialize() {
        // Start database og sæt gui op med alle bøger i en tabel
        bdi = new BookDaoImpl(meddelelse);
        bogtabel.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        // Kolonnerne sættes op med forbindelse til klassen Person med hver sit felt
        isbnkolonne.setCellValueFactory(new PropertyValueFactory<Book, String>("isbn"));
        titelkolonne.setCellValueFactory(new PropertyValueFactory<Book, String>("title"));
        typekolonne.setCellValueFactory(new PropertyValueFactory<Book, String>("category"));

        bogtabel.setItems(tabeldata);

        // Læs alle bøger ind i tabellen
        bdi.getAllBooks(tabeldata);

        // Sortér som udgangspunkt efter id
        isbnkolonne.setSortType(TableColumn.SortType.ASCENDING);
        bogtabel.getSortOrder().add(isbnkolonne);
        bogtabel.sort();

        meddelelse.appendText("Sorteret\n");
    }

    public void klikPåRække(MouseEvent event) {
        // Ved dobbeltklik på bog åbnes redigervindue
        if(event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2) {
            Book b = bogtabel.getSelectionModel().getSelectedItem();
            if (b != null)
                redigerLinje(b);
        }
    }

    private void redigerLinje(Book b) {
        // Lav dialogvindue med felter og knapper, så man kan redigere eller oprette en bog
        Dialog<ButtonType> dialogvindue = new Dialog();
        dialogvindue.setTitle("Rediger/opret bog");
        dialogvindue.setHeaderText("Bog");
        dialogvindue.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        TextField isbnfelt = new TextField();
        TextField titelfelt = new TextField();
        TextField typefelt = new TextField();
        VBox box = new VBox(isbnfelt, titelfelt, typefelt);
        dialogvindue.getDialogPane().setContent(box);

        // Sæt data fra bogen b i felterne
        // hvis bogens isbn er tom regner vi med at der er tale om oprettelse af en ny bog.
        boolean ny = (b.getIsbn() == "");
        isbnfelt.setText(b.getIsbn());
        titelfelt.setText(b.getTitle());
        typefelt.setText(b.getCategory());

        // Hvis der trykkes ok, så skal database og bogtabel opdateres
        Optional<ButtonType> knap = dialogvindue.showAndWait();
        if (knap.get() == ButtonType.OK) {
            b.setIsbn(isbnfelt.getText());
            b.setTitle(titelfelt.getText());
            b.setCategory(typefelt.getText());
            // Indsæt ny bog eller opdater eksisterende bog
            if (ny) {
                if (bdi.saveBook(b)) {
                    tabeldata.add(b);
                    meddelelse.appendText("Bogen er oprettet\n");
                }
                bogtabel.sort();
            } else {
                bdi.updateBook(b);
                meddelelse.appendText("Bogen er opdateret\n");
            }
            bogtabel.refresh();
            bogtabel.sort();
        }
    }

    public void sletBog(ActionEvent event) {
        Book b = bogtabel.getSelectionModel().getSelectedItem();
        if (b != null)
            if (bdi.deleteBook(b))
                tabeldata.remove(b);
    }

    public void findBog(ActionEvent event) {
        // Lav vindue hvor der kan søges efter bøger
        Dialog<ButtonType> dialogvindue = new Dialog();
        dialogvindue.setTitle("Find bog");
        dialogvindue.setHeaderText("Bog");
        dialogvindue.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        TextField isbnfelt = new TextField(); isbnfelt.setPromptText("isbn");
        TextField titelfelt = new TextField(); titelfelt.setPromptText("titel");
        TextField typefelt = new TextField(); typefelt.setPromptText("type");
        VBox box = new VBox(isbnfelt, titelfelt, typefelt);
        dialogvindue.getDialogPane().setContent(box);

        // Hvis der trykkes ok, så skal database og bogtabel opdateres
        Optional<ButtonType> knap = dialogvindue.showAndWait();
        if (knap.get() == ButtonType.OK) {
            bdi.findBook(isbnfelt.getText(), titelfelt.getText(), typefelt.getText(), tabeldata);
            bogtabel.refresh();
            bogtabel.sort();
        }
    }

    public void visAlleBøger(ActionEvent actionEvent) {
        bdi.getAllBooks(tabeldata);
    }
}