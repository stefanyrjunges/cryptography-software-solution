module org.example.passwordmanager {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires java.desktop;
    requires org.apache.commons.validator;

    opens org.example.passwordmanager to javafx.fxml;
    exports org.example.passwordmanager;
}