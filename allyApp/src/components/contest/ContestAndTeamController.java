package components.contest;

import components.Battlefield;
import components.primary.AllyAppController;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ContestAndTeamController {

    AllyAppController appController;
    @FXML
    private Label nameBFLabel;

    @FXML
    private Label uboatBFLabel;

    @FXML
    private Label statusBFLabel;

    @FXML
    private Label levelBFLabel;

    @FXML
    private Label alliesBFLabel;
    public void setAppController(AllyAppController appController) {
        this.appController = appController;
    }

    public void updateContestAndTeamData(Battlefield battlefield) {
        Platform.runLater(() ->
        {
            nameBFLabel.setText(battlefield.getBattleName());
            uboatBFLabel.setText(battlefield.getUboatName());
            statusBFLabel.setText(battlefield.getStatus().name());
            levelBFLabel.setText(battlefield.getLevel().name());
            alliesBFLabel.setText(battlefield.getAlliesJoined() + "/" + battlefield.getAllies());
        });
    }
}
