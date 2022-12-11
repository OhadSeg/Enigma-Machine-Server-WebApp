package components.header;

import enigmaDtos.FileDetailsDTO;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import components.primary.UboatAppController;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import okhttp3.*;
import utils.HttpClientUtil;

import java.io.File;
import java.io.IOException;

import static components.primary.UboatAppController.showMessage;
import static constants.Constants.*;

public class HeaderComponentController {

    @FXML
    private Label filePathLabel;

    @FXML
    private Label fileErrorMessageLabel;

    @FXML
    private Button loadFileButton;
    @FXML
    private Label greetingLabel;

    private UboatAppController mainController;

    private SimpleStringProperty filePathProperty;

    private SimpleStringProperty fileErrorMessageProperty;


    public void initialize() {
//        greetingLabel = new Label();
        filePathLabel.textProperty().bind(filePathProperty);
        fileErrorMessageLabel.textProperty().bind(fileErrorMessageProperty);
    }

    public HeaderComponentController() {
        filePathProperty = new SimpleStringProperty();
        fileErrorMessageProperty = new SimpleStringProperty();

    }

    public void setFilePathProperty(String selectedFile) {
        this.filePathProperty.setValue(selectedFile);
    }

    public void setFileErrorMessageProperty(String errorMessage) {
        this.fileErrorMessageProperty.setValue(errorMessage);
    }

    @FXML
    public void loadFileButtonActionListener(ActionEvent event) {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select words file");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML files", "*.xml"));
        File f = fileChooser.showOpenDialog(new Stage());

        RequestBody body =
                new MultipartBody.Builder()
                        .addFormDataPart("xmlfile", f.getName(), RequestBody.create(f, MediaType.parse("text/xml")))
                        .build();

        Request request = new Request.Builder()
                .url(LOAD_FILE)
                .post(body)
                .build();

        HttpClientUtil.runAsync2(request, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Platform.runLater(() ->
                {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Unknown Error");
                    alert.showAndWait();
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String dtoAsStr = response.body().string();

                if (response.code() != 200) {
                    Platform.runLater(() ->
                    {
                        try {
                            showMessage("Wrong File Details", Alert.AlertType.WARNING, response.body().string());
                            response.body().close();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
                } else {
                    FileDetailsDTO fileDetailsDTO = GSON_INSTANCE.fromJson(dtoAsStr, FileDetailsDTO.class);
                    Platform.runLater(() ->
                    {

                        response.body().close();
                        filePathProperty.set(f.getAbsolutePath());
                        fileErrorMessageProperty.set("file loaded successfully");
                        //בדיקות!!
                        mainController.doLoadFile(fileDetailsDTO);
                    });
                }
            }
        });
    }

    public void setMainController(UboatAppController mainController) {
        this.mainController = mainController;
    }

    public void updateUserName(String userName) {
        greetingLabel.setText("Hello " + userName);
    }
}