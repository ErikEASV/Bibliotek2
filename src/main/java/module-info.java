module com.example.bibliotek2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.bibliotek2 to javafx.fxml;
    exports com.example.bibliotek2;
}