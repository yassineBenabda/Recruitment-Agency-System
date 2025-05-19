module com.agence.recruitment {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens com.agence.recruitment.controller to javafx.fxml;
    opens com.agence.recruitment.model to javafx.base;
    exports com.agence.recruitment;
}
