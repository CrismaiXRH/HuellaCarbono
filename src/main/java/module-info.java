module org.example {
    requires java.naming;
    requires javafx.controls;
    requires javafx.fxml;
    requires jakarta.persistence;
    requires org.hibernate.orm.core;
    requires mysql.connector.j;
    requires jakarta.transaction;
    requires com.github.librepdf.openpdf;

    opens org.example to javafx.fxml;
    opens org.example.entities to javafx.fxml, org.hibernate.orm.core, javafx.base;

    exports org.example;
    exports org.example.view;

    opens org.example.view to javafx.fxml;
}