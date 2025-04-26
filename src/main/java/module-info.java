module com.wmp.callroll {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.almasb.fxgl.all;
    requires javafx.media;
    requires java.desktop;

    opens com.wmp.callroll to javafx.fxml;
    exports com.wmp.callroll;
    exports com.wmp.callroll.test;
    opens com.wmp.callroll.test to javafx.fxml;
}