package com.prime;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import com.prime.app.xo.XO;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.StageStyle;

import static com.prime.app.xo.XO.*;

public class SecondaryController implements Initializable {
    private XO game;
    private SimpleStringProperty currentPlayerProp;

    @FXML
    private GridPane grid;
    @FXML private Label playerLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        game = App.getGame();
        currentPlayerProp = new SimpleStringProperty(Player.X.name()); //usually the first player
        //subscribe to event
        game.getMoveEvent().subscribe(pos -> markPosition((Position)pos), this::onError);
        game.getNextPlayerEvent().subscribe(player -> updateCurrentPlayer((Player)player));
        game.getGameCompleteEvent().subscribe(winner -> endGame((Optional<Player>)winner));

        grid.getChildren().forEach(node -> node.setOnMouseClicked(e -> {
            var i = game.makeMove(Position.valueOf(node.getId()));
            System.out.println(i);
        }));

        playerLabel.textProperty().bind(Bindings.createStringBinding(
                ()-> String.format("Player %s's turn", currentPlayerProp.get()),
                currentPlayerProp
        ));
        //endGame(Optional.empty()); //TODO
    }


    private void endGame(Optional<Player> winner) {
        var msg = winner.isPresent()? String.format("Player %s wins", winner.get()) :
                "It's a tie";
        var alert = new Alert(Alert.AlertType.NONE);
        alert.setTitle("Game over");
        alert.setContentText(msg);

        var retryType = new ButtonType("Retry");
        var endType = new ButtonType("Quit", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(retryType, endType);
        alert.setWidth(10);
        alert.initStyle(StageStyle.UTILITY);
        alert.getDialogPane().setMaxWidth(10);

        var result = alert.showAndWait();
        result.ifPresent(this::switchToPrimary);
    }

    private void updateCurrentPlayer(Player player) {
        currentPlayerProp.set(player.name());
        //playerLabel.setText("Player- " + player.name());
    }

    @FXML
    private void switchToPrimary(ButtonType bt) {
        if(bt.getButtonData().equals(ButtonBar.ButtonData.CANCEL_CLOSE))
            try{ App.setRoot("primary"); }
            catch(Exception e){ e.printStackTrace(); }
        else
            restart();
    }

    private void restart() {
        game.restart();
        Platform.runLater(()->
                grid.getChildren().forEach(node -> node.setStyle("")));
    }


    private void markPosition(Position pos) {
        System.out.println(pos.name());
        Pane pane = (Pane) grid.getChildrenUnmodifiable()
                .stream()
                .filter(n->n.getId().equals(pos.name()))
                .findFirst().get();
        //Platform.runLater(()->{
            try {
                if (currentPlayerProp.get().equals(Player.X.name()))
                    pane.setStyle("-fx-background-color: #ff0000");
                else pane.setStyle("-fx-background-color: blue");
            } catch (Exception e){
                System.out.println(e.getMessage());
            }
        //});
    }
    private void onError(Object t){
        ((Throwable) t).printStackTrace();
    }
}