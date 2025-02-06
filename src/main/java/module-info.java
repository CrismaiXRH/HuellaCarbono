module org.example {
    requires java.naming;
    requires javafx.controls;
    requires javafx.fxml;
    requires jakarta.persistence;
    requires org.hibernate.orm.core;
    requires java.desktop;
    requires mysql.connector.j;
    requires jakarta.transaction;

    opens org.example to javafx.fxml;
    opens org.example.entities to javafx.fxml, org.hibernate.orm.core;
    exports org.example;
    exports org.example.view;

    opens org.example.view to javafx.fxml;

}
