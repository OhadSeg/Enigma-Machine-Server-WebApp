package components.currConfiguration;

import components.firstTab.FirstTabComponentController;
import components.secondTab.SecondTabComponentController;
import enigmaDtos.FileDetailsDTO;
import enigmaDtos.TestedMachineDetailsDTO;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;

import java.util.ArrayList;

public class CurrConfiComponentController {
    @FXML
    private FlowPane currRotorsFP;
    @FXML
    private FlowPane currWindowPosFP;
    @FXML
    private FlowPane currNotchFP;
    @FXML
    private Label reflectorLabel;
    private FirstTabComponentController firstTabComponentController;
    private SecondTabComponentController secondTabComponentController;


    private ArrayList<Label> rotorsIdLabels = new ArrayList<>();
    private ArrayList<Label> windowPosLabels = new ArrayList<>();
    private ArrayList<Label> notchPosLabels = new ArrayList<>();
    private ArrayList<Label> plugBoardLabel = new ArrayList<>();

    public void initialize() {
        currRotorsFP.setAlignment(Pos.TOP_CENTER);
        currWindowPosFP.setAlignment(Pos.TOP_CENTER);
        currNotchFP.setAlignment(Pos.TOP_CENTER);
        reflectorLabel.setAlignment(Pos.TOP_CENTER);
    }

    public void buildNewCurrConfiguration(FileDetailsDTO fileDetailsDTO) {
        cleanCurrConfiguration();
        fileDetailsDTO.getRotorsInUse().forEach((rotor) -> {
            Label rotorLabel = new Label(rotor + "");
            rotorsIdLabels.add(rotorLabel);
            currRotorsFP.getChildren().add(rotorLabel);
            currRotorsFP.setHgap(10);
        });

        fileDetailsDTO.getRotorsInitialPositions().forEach((windowPos) -> {
            Label windowPosLabel = new Label(windowPos + "");
            windowPosLabels.add(windowPosLabel);
            currWindowPosFP.getChildren().add(windowPosLabel);
            currWindowPosFP.setHgap(10);
        });

        fileDetailsDTO.getNotchesPositionInUse().forEach((notchPos) -> {
            Label notchPosLabel = new Label(notchPos + "");
            notchPosLabels.add(notchPosLabel);
            currNotchFP.getChildren().add(notchPosLabel);
            currNotchFP.setHgap(10);
        });

        reflectorLabel.setText(fileDetailsDTO.getReflectorInUse() + "");

//        fileDetailsDTO.getChosenValidPlugs().forEach((plugPair) -> {
//            Label plugPairLabel = new Label(plugPair.getKey() + " <-> " + plugPair.getValue());
//            plugBoardLabel.add(plugPairLabel);
//            currPlugBoardFP.getChildren().add(plugPairLabel);
//            currPlugBoardFP.setHgap(10);
//        });
    }

    public void updateNewCurrConfiguration(TestedMachineDetailsDTO currConfiguration) {
        for (int i = 0; i < rotorsIdLabels.size(); i++) {
            rotorsIdLabels.get(i).setText(currConfiguration.getChosenValidInUseRotors().get(i) + "");
            windowPosLabels.get(i).setText(currConfiguration.getRotorsWindowDynamicPosition().get(i)+"");
            notchPosLabels.get(i).setText(currConfiguration.getNotchesDynamicPosition().get(i)+"");
        }
        reflectorLabel.setText(currConfiguration.getChosenValidReflector()+"");

        plugBoardLabel.clear();
        currConfiguration.getChosenValidPlugs().forEach((plugPair) -> {
            Label plugPairLabel = new Label(plugPair.getKey() + " <-> " + plugPair.getValue());
            plugBoardLabel.add(plugPairLabel);
        });
    }

    public void stepCurrConfiguration(FileDetailsDTO fileDetailsDTO) {
        for (int i = 0; i < windowPosLabels.size(); i++) {
            windowPosLabels.get(i).setText(fileDetailsDTO.getRotorsInitialPositions().get(i) + "");
            notchPosLabels.get(i).setText(fileDetailsDTO.getNotchesPositionInUse().get(i) + "");
        }
    }


    public void cleanCurrConfiguration() {
        currRotorsFP.getChildren().clear();
        currWindowPosFP.getChildren().clear();
        reflectorLabel.setText("");
        currNotchFP.getChildren().clear();

        rotorsIdLabels.clear();
        windowPosLabels.clear();
        notchPosLabels.clear();
        plugBoardLabel.clear();
    }

    public void setFirstTabComponentController(FirstTabComponentController firstTabComponentController) {
        this.firstTabComponentController = firstTabComponentController;
    }

    public void setSecondTabComponentController(SecondTabComponentController secondTabComponentController) {
        this.secondTabComponentController = secondTabComponentController;
    }
//    public void setThirdTabComponentController(ThirdTabComponentController thirdTabComponentController) {
//        this.thirdTabComponentController = thirdTabComponentController;
//    }
}