package components.firstTab;

import components.currConfiguration.CurrConfiComponentController;
import constants.Constants;
import enigmaDtos.FileDetailsDTO;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import components.primary.UboatAppController;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Region;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import utils.HttpClientUtil;
import utils.RomanNumbers;

import java.io.IOException;
import java.util.*;

import static constants.Constants.GSON_INSTANCE;

public class FirstTabComponentController {
    private UboatAppController mainController;
    @FXML
    private CurrConfiComponentController currConfiComponentController;

    @FXML
    private FlowPane setRotorsFlowPane;

    @FXML
    private ComboBox<String> reflectorComb;
    @FXML
    private Label machineDetailsLabel;

    @FXML
    private Label currConfigurationLabel;

    private SimpleStringProperty machineDetailsProperty;

    private ArrayList<ComboBox<Integer>> rotorsInUseCBArray = new ArrayList<>();

    private ArrayList<ComboBox<Character>> rotorStartingPosCBArray = new ArrayList<>();

    public FirstTabComponentController() {
        machineDetailsProperty = new SimpleStringProperty();
    }

    public void setFirstTabComponent(){
        rotorsInUseCBArray = new ArrayList<>();
        rotorStartingPosCBArray = new ArrayList<>();
        reflectorComb.valueProperty().set(null);
    }

    public void initialize() {
        if (currConfiComponentController != null) {
            currConfiComponentController.setFirstTabComponentController(this);
        }
        machineDetailsLabel.textProperty().bind(machineDetailsProperty);
    }

    public void setEnigmaFileDetailsLabel(FileDetailsDTO fileDetailsDTO){
        StringBuilder enigmaFileDetails = new StringBuilder();

        enigmaFileDetails.append("1. Possible amount of rotors / Amount of in-use rotors: ");
        enigmaFileDetails.append(fileDetailsDTO.getAmountOfAllRotors() + " / " + fileDetailsDTO.getAmountOfAllRotorsInUse());
        enigmaFileDetails.append(System.lineSeparator());
        enigmaFileDetails.append("2. Amount of reflectors: " + fileDetailsDTO.getNumOfReflectors());
        enigmaFileDetails.append(System.lineSeparator());
        enigmaFileDetails.append("Origin Configuration: ");
        enigmaFileDetails.append(fetchCurrConfiguration(fileDetailsDTO).toString());
        machineDetailsProperty.set(enigmaFileDetails.toString());
    }

    public void setConfigurationDetailsLabel(FileDetailsDTO fileDetailsDTO) {
        StringBuilder configurationDetails = new StringBuilder();
        configurationDetails.append("Current Configuration: ");
        configurationDetails.append(fetchCurrConfiguration(fileDetailsDTO));
        currConfigurationLabel.setText(configurationDetails.toString());
    }

    protected String fetchCurrConfiguration(FileDetailsDTO fileDetailsDTO){
        StringBuilder configurationDetails = new StringBuilder();
        if (fileDetailsDTO.getReflectorInUse() != null) {
            configurationDetails.append("<");
            for (int i = 0; i < fileDetailsDTO.getRotorsInUse().size(); i++) {
                if (i != fileDetailsDTO.getRotorsInUse().size() - 1) {
                    configurationDetails.append(fileDetailsDTO.getRotorsInUse().get(i) + ",");
                } else {
                    configurationDetails.append(fileDetailsDTO.getRotorsInUse().get(i));
                }
            }
            configurationDetails.append("><");
            for (int i = 0; i < fileDetailsDTO.getRotorsInUse().size(); i++) {
                configurationDetails.append(fileDetailsDTO.getRotorsInitialPositions().get(i) + "(" +
                        fileDetailsDTO.getNotchesPositionInUse().get(i) + ")");
            }
            configurationDetails.append("><");
            configurationDetails.append(fileDetailsDTO.getReflectorInUse());
            configurationDetails.append(">");
        } else {
            configurationDetails.append("----/----");
        }
        configurationDetails.append(System.lineSeparator());
        return configurationDetails.toString();
    }

    public void fileLoadedEnablingButtons(FileDetailsDTO fileDetailsDTO) {
        setRotorsFlowPane.getChildren().clear();
        ArrayList<String> reflectorsList = new ArrayList<>();
        ArrayList<Character> abcList = new ArrayList<>();
        ArrayList<Integer> rotorsIdList = new ArrayList<>();

        for (int i = 0; i < fileDetailsDTO.getNumOfReflectors(); i++) {
            reflectorsList.add(RomanNumbers.values()[i].name());
        }
        ObservableList<String> reflectorsObservList = FXCollections.observableList(reflectorsList);
        reflectorComb.setItems(reflectorsObservList);

        for (int i = 1; i <= fileDetailsDTO.getAmountOfAllRotors(); i++) {
            rotorsIdList.add(i);
        }
        ObservableList<Integer> rotorsIdObservList = FXCollections.observableList(rotorsIdList);

        for (int i = 0; i < fileDetailsDTO.getAbc().length(); i++) {
            abcList.add(fileDetailsDTO.getAbc().charAt(i));
        }
        ObservableList<Character> abcObservList = FXCollections.observableList(abcList);


        for (int i = 0; i < fileDetailsDTO.getAmountOfAllRotorsInUse(); i++) {

            ComboBox<Integer> rotorsIdComboBox = new ComboBox<>(rotorsIdObservList);
            rotorsInUseCBArray.add(rotorsIdComboBox);

            ComboBox<Character> rotorStartPosComboBox = new ComboBox<>(abcObservList);
            rotorStartingPosCBArray.add(rotorStartPosComboBox);

            Label rotorIndexLabel = new Label();
            rotorIndexLabel.setText(i + 1 + "st Rotor");

            setRotorsFlowPane.getChildren().add(rotorIndexLabel);
            setRotorsFlowPane.setHgap(30);
            setRotorsFlowPane.getChildren().add(rotorsIdComboBox);
            setRotorsFlowPane.setHgap(40);
            setRotorsFlowPane.getChildren().add(rotorStartPosComboBox);
            setRotorsFlowPane.setVgap(10);
        }
    }
    @FXML
    public void randomCodeButtonActionListener(ActionEvent event) {
        String finalUrl = HttpUrl
                .parse(Constants.RANDOM_CONFIGURATION)
                .newBuilder()
                .build()
                .toString();

        HttpClientUtil.runAsync(finalUrl, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() ->
                        showMessage("Error", Alert.AlertType.WARNING, "Something went wrong: " + e.getMessage())
                );
            }
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String responseBody = response.body().string();
                if (response.code() != 200) {
                    Platform.runLater(() -> {
                                showMessage("Error", Alert.AlertType.WARNING, "Something went wrong: " + responseBody);
                                response.body().close();
                            }
                    );
                } else {
                    FileDetailsDTO fileDetailsDTO = GSON_INSTANCE.fromJson(responseBody, FileDetailsDTO.class);
                    Platform.runLater(() -> {
                        response.body().close();
                        mainController.doEnterMachineDetailsManually(fileDetailsDTO);
                    });
                }
            }
        });
    }

    @FXML
    public void setCodeButtonActionListener(ActionEvent event) {
        FileDetailsDTO fileDetailsDTO = new FileDetailsDTO();
        rotorsInUseCBArray.forEach((rotorID) -> fileDetailsDTO.addToRotorsInUse(rotorID.getValue()));
        rotorStartingPosCBArray.forEach((StartingPos) -> fileDetailsDTO.addToStartingPos(StartingPos.getValue()));
        if (reflectorComb.getValue() != null) {
            fileDetailsDTO.setChosenValidReflector(RomanNumbers.valueOf(reflectorComb.getValue()));
        }

        String jsonSelectedCategories = GSON_INSTANCE.toJson(fileDetailsDTO, FileDetailsDTO.class);
        RequestBody body = RequestBody.create(jsonSelectedCategories, MediaType.parse("application/json charset=UTF-8"));

        String finalUrl = HttpUrl
                .parse(Constants.SET_CONFIGURATION)
                .newBuilder()
                .build()
                .toString();

        Request request = new Request.Builder()
                .url(finalUrl)
                .post(body)
                .build();
        HttpClientUtil.runAsync2(request, new Callback() {

                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        Platform.runLater(() ->
                                showMessage("Error", Alert.AlertType.WARNING, "Something went wrong: " + e.getMessage())
                        );
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        String responseBody = response.body().string();
                        if (response.code() != 200) {
                            Platform.runLater(() -> {
                                showMessage("Error", Alert.AlertType.WARNING, "Something went wrong: " + responseBody);
                                response.body().close();
                                    }
                            );
                        } else {
                            FileDetailsDTO fileDetailsDTO = GSON_INSTANCE.fromJson(responseBody, FileDetailsDTO.class);
                            Platform.runLater(() -> {
                                response.body().close();
                                mainController.doEnterMachineDetailsManually(fileDetailsDTO);
                            });
                        }
                    }
                });
    }

    public void setMainController(UboatAppController mainController) {
        this.mainController = mainController;
    }

    public void buildCurrConfiguration(FileDetailsDTO fileDetailsDTO){
        currConfiComponentController.buildNewCurrConfiguration(fileDetailsDTO);
    }

    public void cleanCurrConfiguration(){
        currConfiComponentController.cleanCurrConfiguration();
    }

    public void stepCurrConfiguration(FileDetailsDTO fileDetailsDTO){
        currConfiComponentController.stepCurrConfiguration(fileDetailsDTO);
    }

    public void setConfigurationMode(FileDetailsDTO fileDetailsDTO){
        setConfigurationDetailsLabel(fileDetailsDTO);
        buildCurrConfiguration(fileDetailsDTO);
    }

        public void showMessage(String title, Alert.AlertType alertType, String content) {
            Alert alert = new Alert(alertType);
            alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            alert.setTitle(title);
            alert.setAlertType(alertType);
            alert.setContentText(content);
            alert.showAndWait();
        }
}