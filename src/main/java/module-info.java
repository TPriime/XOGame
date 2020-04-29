module com.prime {
    requires javafx.controls;
    requires javafx.fxml;
    requires xno;
    requires io.reactivex.rxjava3;


    opens com.prime to javafx.fxml;
    exports com.prime;
}