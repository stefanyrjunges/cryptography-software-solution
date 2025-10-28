module org.example.passwordmanager {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    requires org.apache.commons.validator;

    requires org.controlsfx.controls;
    requires java.desktop;
    requires jbcrypt;

    opens org.example.passwordmanager to javafx.fxml;
    exports org.example.passwordmanager;
}
