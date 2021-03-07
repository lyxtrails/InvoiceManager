package invoice;

import javafx.event.ActionEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class SettingController implements Initializable{
    public AnchorPane mSettingPane;

    public void closeWindow(ActionEvent e) {
        Stage stage = (Stage) mSettingPane.getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println(System.getProperty("user.dir"));
    }
}
