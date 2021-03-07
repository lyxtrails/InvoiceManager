package invoice;

import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.fxml.Initializable;

import java.io.*;
import java.net.URL;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;

public class SettingController implements Initializable{
    public AnchorPane mSettingPane;
    public TextField fileSavePathTextField;
    public TextField companyNameTextField;
    public TextField companyPhoneNumberTextField;
    public TextField companyEmailTextField;

    private String defaultFileSavePath =
            Paths.get(System.getProperty("java.io.tmpdir"),  "tmp_invoices").toString();
    private String defaultCompanyName = "Company Name";
    private String defaultCompanyPhoneNumber = "(123)456-7890";
    private String defaultCompanyEmail = "company@company.com";

    public void closeWindow(ActionEvent e) {
        Stage stage = (Stage) mSettingPane.getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        File configFile = new File("config.properties");
        Map<String, String> configs = Config.getConfigurations();
        fileSavePathTextField.setText(configs.get(Config.FILE_SAVE_PATH_KEY));
        companyNameTextField.setText(configs.get(Config.COMPANY_NAME_KEY));
        companyPhoneNumberTextField.setText(configs.get(Config.COMPANY_PHONE_NUMBER_KEY));
        companyEmailTextField.setText(configs.get(Config.COMPANY_EMAIL_KEY));
    }

    public void saveConfig(ActionEvent actionEvent) {
        Map<String, String> configs = new HashMap<>();
        configs.put(Config.FILE_SAVE_PATH_KEY, fileSavePathTextField.getText());
        configs.put(Config.COMPANY_NAME_KEY, companyNameTextField.getText());
        configs.put(Config.COMPANY_PHONE_NUMBER_KEY, companyPhoneNumberTextField.getText());
        configs.put(Config.COMPANY_EMAIL_KEY, companyEmailTextField.getText());
        Config.saveConfigurations(configs);

        Stage stage = new Stage();
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(mSettingPane.getScene().getWindow());
        VBox comp = new VBox();
        comp.setAlignment(Pos.CENTER);
        Label label = new Label("设置已成功保存");
        label.setAlignment(Pos.CENTER);
        comp.getChildren().add(label);
        Scene scene = new Scene(comp, 500, 150);
        stage.setScene(scene);
        stage.show();
    }
}
