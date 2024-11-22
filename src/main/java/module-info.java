module com.mycompany.cardpro4 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;
    requires java.sql;
    opens dao;
    opens com.mycompany.cardpro4.controller to javafx.fxml;
    opens model to com.google.gson;
    exports com.mycompany.cardpro4;
    requires com.google.gson;
}
