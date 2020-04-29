package com.prime;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.prime.app.xo.XO;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;

public class PrimaryController implements Initializable {

    @FXML
    private Spinner spinner;
    @FXML
    private Button primaryButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        var options = FXCollections.observableArrayList(XO.Mode.MULTI, XO.Mode.SINGLE);
        var factory = new SpinnerValueFactory.ListSpinnerValueFactory<>(options);
        factory.setValue(XO.Mode.SINGLE);
        spinner.setValueFactory(factory);
        Platform.runLater(()->primaryButton.requestFocus());
    }


    @FXML
    private void switchToSecondary() throws IOException {
        var selectedMode = (XO.Mode) spinner.getValue();
        var game = XO.build(XO.Mode.valueOf(selectedMode.name()));
        App.setGame(game);
        App.setRoot("secondary"); //start game
    }
}
