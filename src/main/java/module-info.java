module com.example.GanttchDB {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires com.almasb.fxgl.all;
    requires java.sql;
    requires com.h2database;
    requires java.desktop;
    requires javafx.swing;

    opens com.example.GanttchDB to javafx.fxml;
    exports com.example.GanttchDB;
    exports DAO;
    opens DAO to javafx.fxml;
    exports Model;
    opens Model to javafx.fxml;
    exports DAO.H2;
    opens DAO.H2 to javafx.fxml;
}