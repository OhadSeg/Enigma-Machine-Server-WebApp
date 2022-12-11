package components.primary;

import components.firstTab.FirstTabComponentController;
import components.header.HeaderComponentController;
import components.login.LoginController;
import components.secondTab.SecondTabComponentController;
import enigmaDtos.BruteForceDetailsDTO;
import enigmaDtos.CandidateDTO;
import enigmaDtos.FileDetailsDTO;
import exceptions.UserInputException;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import javafx.util.Pair;
import operation.Operator;
import utils.EncryptionMode;
import java.io.IOException;
import java.net.URL;

public class UboatAppController {
    @FXML
    private ScrollPane headerComponent;
    @FXML
    private HeaderComponentController headerComponentController;
    @FXML
    private ScrollPane firstTabComponent;
    @FXML
    private FirstTabComponentController firstTabComponentController;
    @FXML
    private ScrollPane secondTabComponent;
    @FXML
    private SecondTabComponentController secondTabComponentController;
    @FXML
    private ScrollPane thirdTabComponent;
    @FXML
    private LoginController loginController;

    @FXML
    private Tab machineTab;
    @FXML
    private Tab contestTab;
    private BorderPane loginComponent;
    @FXML
    private BorderPane mainBP;
    private SimpleBooleanProperty ifFileSelected;

    private BorderPane toCheck;

    private Stage primaryStage;

    private Operator operator;

    public UboatAppController() {
        ifFileSelected = new SimpleBooleanProperty();
        operator = new Operator();
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void setOperator(Operator operator) {
        this.operator = operator;
    }

    @FXML
    public void initialize() {
        loadLoginPage();

        if (headerComponentController != null && firstTabComponentController != null && secondTabComponentController != null && loginController != null) {
            headerComponentController.setMainController(this);
            firstTabComponentController.setMainController(this);
            secondTabComponentController.setMainController(this);
            loginController.setMainController(this);
            machineTab.setDisable(true);
            contestTab.setDisable(true);
        }
    }

    public void switchToMainApp() {
        mainBP.getTop().setVisible(true);
        mainBP.getCenter().setVisible(true);
        mainBP.setBottom(null);
    }

    private void loadLoginPage() {
        URL loginPageUrl = getClass().getResource("/components/login/loginScreen.fxml");
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(loginPageUrl);
            loginComponent = fxmlLoader.load();
            loginController = fxmlLoader.getController();
            loginController.setMainController(this);
            setMainPanelTo(loginComponent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setMainPanelTo(Parent parent) {
        mainBP.getTop().setVisible(false);
        mainBP.getCenter().setVisible(false);
        mainBP.setBottom(parent);
    }

    public String doRunCode(String textToCode, EncryptionMode mode) {
        try {
            Pair codedTextAndTime = operator.setTextToCode(textToCode, mode);
            operator.updateDynamicCurrConfiguration();
            //firstTabComponentController.stepCurrConfiguration(operator.getNewCurrConfiguration());
            //firstTabComponentController.setMachineDetailsLabel(operator.getMachineDetails());
           // secondTabComponentController.stepCurrConfiguration(operator.getNewCurrConfiguration());

//            if (mode == EncryptionMode.WHOLEWORD) {
//                secondTabComponentController.addNewEncryptionTextToHistoryAndStats(new Pair<>(codedTextAndTime, textToCode));
//            }

            return codedTextAndTime.getKey().toString();
        } catch (UserInputException e) {
            showMessage("Code error", Alert.AlertType.WARNING, e.getMessage());
        }
        return null;
    }

//    public void doRandomCode() {
//        operator.randomMode();
//        contestTab.setDisable(false);
//        bruteForceTab.setDisable(false);
//         // thirdTabComponentController.buildCurrConfiguration(operator.getNewCurrConfiguration());
//    //    thirdTabComponentController.clearAllComponents();
//      //  thirdTabComponentController.machineDetailsLoaded();
//    }

    public void doEnterMachineDetailsManually(FileDetailsDTO fileDetailsDTO) {
        //try {
        firstTabComponentController.setConfigurationMode(fileDetailsDTO);
        secondTabComponentController.setConfigurationMode(fileDetailsDTO);
        //secondTabComponentController.setConfigurationMode(operator.getNewCurrConfiguration(), operator.getConsoleCurrConfiguration());
        //thirdTabComponentController.buildCurrConfiguration(operator.getNewCurrConfiguration());
        //thirdTabComponentController.clearAllComponents();
        //thirdTabComponentController.machineDetailsLoaded();
        contestTab.setDisable(false);
        //bruteForceTab.setDisable(false);
//        } catch (MachineDetailsFromUserException e) {
//            showMessage("File error", Alert.AlertType.ERROR, e.getMessage());
//        }
    }

    public void doLoadFile(FileDetailsDTO fileDetailsDTO) {

//        try {
//            operator.setXmlPath(absolutePath);
//            //אם הקובץ טוב


        machineTab.setDisable(false);
        contestTab.setDisable(true);
        firstTabComponentController.setFirstTabComponent();
        firstTabComponentController.cleanCurrConfiguration();
        secondTabComponentController.cleanCurrConfiguration();
        firstTabComponentController.fileLoadedEnablingButtons(fileDetailsDTO);
        firstTabComponentController.setEnigmaFileDetailsLabel(fileDetailsDTO);
        secondTabComponentController.loadFileMode(fileDetailsDTO.getAbc());
        secondTabComponentController.setWordsToDictionary(fileDetailsDTO.getDictionary());
//            thirdTabComponentController.setAgentsComb(operator.getMaxAmountOfAgents());
//            thirdTabComponentController.setWordsToDictionary(operator.getDictionary());
//            thirdTabComponentController.clearAllComponents();
//            headerComponentController.setFilePathProperty(absolutePath);
//            headerComponentController.setFileErrorMessageProperty("File is loaded successfully");
//
//        } catch (Exception e) {
//            //אם הקובץ פגום
//            showMessage("File error", Alert.AlertType.ERROR, e.getMessage());
//        }
    }

    public static void showMessage(String title, Alert.AlertType alertType, String content) {
        Alert alert = new Alert(alertType);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.setTitle(title);
        alert.setAlertType(alertType);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void updateConfiguration(FileDetailsDTO fileDetailsDTO) {
        firstTabComponentController.stepCurrConfiguration(fileDetailsDTO);
        secondTabComponentController.stepCurrConfiguration(fileDetailsDTO);
    }


    public void doBruteForce(BruteForceDetailsDTO bfDetails) {
        try {
            operator.startBruteForce(bfDetails);
        } catch (UserInputException e) {
            showMessage("Code error", Alert.AlertType.WARNING, e.getMessage());
            return;
        }

        Thread listenerThread = new Thread(() -> {
            try {
                while (true) {
                    if (operator.isBruteForceStillRunning()) {
                        CandidateDTO candidate = operator.getCandidateFromDM();
                    } else {
                        System.out.println("finish");
                        break;
                    }
                }
            } catch (Exception e) {
                showMessage("Error", Alert.AlertType.ERROR, e.getMessage());
            }
        });
        listenerThread.start();
    }

    public void doPause() {
        operator.doPause();
    }

    public void doResume() {
        operator.doResume();
    }

    public void doStop() {
        operator.doStop();
    }

    public void updateHeaderUserName(String userName) {
        headerComponentController.updateUserName(userName);
    }
}