module com.uninavigator.uninavigatorapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.management;
    requires org.json;
    requires okhttp3;
    requires kotlin.stdlib;
    requires java.security.sasl;
//    requires jBCrypt;



    opens com.uninavigator.uninavigatorapp to javafx.fxml;
    exports com.uninavigator.uninavigatorapp;
    exports com.uninavigator.uninavigatorapp.controllers;
    opens com.uninavigator.uninavigatorapp.controllers to javafx.fxml;
    exports com.uninavigator.uninavigatorapp.controllers.course;
    opens com.uninavigator.uninavigatorapp.controllers.course to javafx.fxml;
    exports com.uninavigator.uninavigatorapp.controllers.user;
    opens com.uninavigator.uninavigatorapp.controllers.user to javafx.fxml;

}
