module com.zabguzxcoop.lotteryticketcounterapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires kotlin.stdlib;


    opens com.zabguzxcoop.lotteryticketcounterapp to javafx.fxml;
    exports com.zabguzxcoop.lotteryticketcounterapp;
}