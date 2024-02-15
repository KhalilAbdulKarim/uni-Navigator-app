module com.uninavigator.uninavigatorapp {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.uninavigator.uninavigatorapp to javafx.fxml;
    exports com.uninavigator.uninavigatorapp;
}