module com.uninavigator.uninavigatorapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.management;
//    requires jBCrypt;



    opens com.uninavigator.uninavigatorapp to javafx.fxml;
    exports com.uninavigator.uninavigatorapp;

}
