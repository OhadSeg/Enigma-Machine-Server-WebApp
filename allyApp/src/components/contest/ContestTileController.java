package components.contest;

import components.primary.AllyAppController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

public class ContestTileController {
    private AllyAppController mainController;
    @FXML
    private Label battlefieldNameLabel;

    @FXML
    private Label uboatNameLabel;

    @FXML
    private Label isActiveStatusLabel;

    @FXML
    private Label difficultyLevelLabel;

    @FXML
    private Label alliesJoinedLabel;

    @FXML
    private Button joinButton;

    public void setBattlefieldNameLabel(String battlefieldNameStr) {
        this.battlefieldNameLabel.setText(battlefieldNameStr);
    }

    public void setUboatNameLabel(String uboatNameStr) {
        this.uboatNameLabel.setText(uboatNameStr);
    }

    public void setDifficultyLevelLabel(String difficultyLevelLabel) {
        this.difficultyLevelLabel.setText(difficultyLevelLabel);
    }

    public void setAlliesJoinedLabel(int allies, int alliesJoined) {
        this.alliesJoinedLabel.setText(alliesJoined + "/" + allies);
    }

    public void setIsActiveStatusLabel() {

    }

    @FXML
    void joinToBattlefield(MouseEvent event) {
        mainController.allyClickedToJoin(battlefieldNameLabel.getText());
    }

    public void setMainController(AllyAppController mainController) {
        this.mainController = mainController;
    }

    public void setJoinVisible(boolean joined) {
        joinButton.setDisable(joined);
    }

    public void setIsActiveStatusLabel(String battlefieldStatus) {
        isActiveStatusLabel.setText(battlefieldStatus);
    }
}