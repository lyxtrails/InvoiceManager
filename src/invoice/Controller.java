package invoice;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.nio.file.Paths;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

public class Controller implements Initializable {
    public MenuItem mFileCloseMenuItem;
    public AnchorPane mRootPane;
    public TextField mCustomerNameTextField;
    public TextField mCustomerPhoneTextField;
    public TextField mCustomerAddressTextField;
    public DatePicker mDateDatePicker;
    public TabPane mInvoiceTabPane;
    public ChoiceBox<String> mFloorNumberChoiceBox;
    public ChoiceBox<String> mPlaceChoiceBox;
    public ChoiceBox<String> mActionChoiceBox;
    public ChoiceBox<String> mObjectChoiceBox;
    public TextArea mCustomWorkDescriptionTextArea;
    public TextField mMaterialCostTextField;
    public TextField mLaborCostTextField;
    public TableView<InvoiceTableRow> mInvoicePayloadTableView;
    public TableColumn mWorkDescriptionTableColumn;
    public TableColumn mMaterialCostTableColumn;
    public TableColumn mLaborCostTableColumn;
    public TableColumn mWorkChineseDescriptionTableColumn;
    public TextField mTotalMaterialCostTextField;
    public TextField mTotalLaborCostTextField;
    public TextField mTotalCostTextField;


    public void exitApplication(ActionEvent e) {
        Stage stage = (Stage) mRootPane.getScene().getWindow();
        stage.close();
    }

    public void openSetting(ActionEvent e) throws Exception {
        Group group = new Group();
        group.getChildren().add(FXMLLoader.load(getClass().getResource("setting.fxml")));
        Stage stage = new Stage();
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(mRootPane.getScene().getWindow());
        stage.setScene(new Scene(group));
        stage.setTitle("Setting");
        stage.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        mDateDatePicker.setValue(LocalDate.now(TimeZone.getDefault().toZoneId()));

        for (Map.Entry<String,String> entry : Translator.floorTranslation.entrySet()) {
            mFloorNumberChoiceBox.getItems().add(entry.getKey());
        }
        for (Map.Entry<String,String> entry : Translator.placeTranslation.entrySet()) {
            mPlaceChoiceBox.getItems().add(entry.getKey());
        }
        for (Map.Entry<String,String> entry : Translator.actionTranslation.entrySet()) {
            mActionChoiceBox.getItems().add(entry.getKey());
        }
        for (Map.Entry<String,String> entry : Translator.objectTranslation.entrySet()) {
            mObjectChoiceBox.getItems().add(entry.getKey());
        }
        mWorkDescriptionTableColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        mWorkChineseDescriptionTableColumn.setCellValueFactory(new PropertyValueFactory<>("descriptionCN"));
        mMaterialCostTableColumn.setCellValueFactory(new PropertyValueFactory<>("materialCost"));
        mLaborCostTableColumn.setCellValueFactory(new PropertyValueFactory<>("laborCost"));

    }

    public void addWorkItem(ActionEvent e) {
        try {
            Double.parseDouble(mMaterialCostTextField.getText());
            Double.parseDouble(mLaborCostTextField.getText());
        } catch (NumberFormatException nfe) {
            showErrorWindow("材料费或者人工费格式不正确");
            return;
        }
        String workDescription = "";
        String workDescriptionCN = "";
        if (mInvoiceTabPane.getSelectionModel().getSelectedIndex() == 0) {
            workDescription = buildWorkDescription(
                    mFloorNumberChoiceBox.getSelectionModel().getSelectedItem(),
                    mPlaceChoiceBox.getSelectionModel().getSelectedItem(),
                    mActionChoiceBox.getSelectionModel().getSelectedItem(),
                    mObjectChoiceBox.getSelectionModel().getSelectedItem()
            );
            workDescriptionCN = buildWorkDescriptionCN(
                    mFloorNumberChoiceBox.getSelectionModel().getSelectedItem(),
                    mPlaceChoiceBox.getSelectionModel().getSelectedItem(),
                    mActionChoiceBox.getSelectionModel().getSelectedItem(),
                    mObjectChoiceBox.getSelectionModel().getSelectedItem()
            );
        } else if (mInvoiceTabPane.getSelectionModel().getSelectedIndex() == 1) {
            workDescription = mCustomWorkDescriptionTextArea.getText();
        }
        if (workDescription != "") {
            mInvoicePayloadTableView.getItems().add(new InvoiceTableRow(
                    workDescription,
                    workDescriptionCN,
                    mMaterialCostTextField.getText(),
                    mLaborCostTextField.getText()));
            refreshTotalCost();
        }
    }

    public String buildWorkDescription(String floor, String place, String action, String object) {
        return String.format("%s %s in %s %s",
                Translator.actionTranslation.get(action),
                Translator.objectTranslation.get(object),
                Translator.floorTranslation.get(floor),
                Translator.placeTranslation.get(place)
                );
    }

    public String buildWorkDescriptionCN(String floor, String place, String action, String object) {
        return String.format("%s层%s，%s%s", floor, place, action, object);
    }

    public void removeWorkItem(ActionEvent e) {
        mInvoicePayloadTableView.getItems().removeAll(
                mInvoicePayloadTableView.getSelectionModel().getSelectedItems()
        );
        refreshTotalCost();
    }

    public void showErrorWindow(String message) {
        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(mRootPane.getScene().getWindow());
        VBox dialogVbox = new VBox(20);
        dialogVbox.getChildren().add(new Text(message));
        Scene dialogScene = new Scene(dialogVbox, 300, 200);
        dialog.setScene(dialogScene);
        dialog.show();
    }

    public void refreshTotalCost() {
        double totalMaterialCost = 0.0;
        double totalLaborCost = 0.0;
        for (InvoiceTableRow item : mInvoicePayloadTableView.getItems()) {
            totalMaterialCost += Double.parseDouble((String)
                    mMaterialCostTableColumn.getCellObservableValue(item).getValue());
            totalLaborCost += Double.parseDouble((String)
                    mLaborCostTableColumn.getCellObservableValue(item).getValue());
        }
        mTotalMaterialCostTextField.setText(Double.toString(totalMaterialCost));
        mTotalLaborCostTextField.setText(Double.toString(totalLaborCost));
        mTotalCostTextField.setText(Double.toString(totalMaterialCost+totalLaborCost));
    }

    public void saveToPDF(ActionEvent actionEvent) {
        InvoiceData.Metadata invoiceMetadata = new InvoiceData.Metadata(
                mCustomerNameTextField.getText(),
                mCustomerPhoneTextField.getText(),
                mCustomerAddressTextField.getText(),
                Date.valueOf(mDateDatePicker.getValue())
        );
        List<InvoiceData.Row> rows = new ArrayList<>();
        for (int i = 0; i < mInvoicePayloadTableView.getItems().size(); i++) {
            InvoiceData.Row row = new InvoiceData.Row(
                    mWorkDescriptionTableColumn.getCellObservableValue(i).getValue().toString(),
                    mMaterialCostTableColumn.getCellObservableValue(i).getValue().toString(),
                    mLaborCostTableColumn.getCellObservableValue(i).getValue().toString()
            );
            rows.add(row);
        }
        InvoiceData.Cost cost = new InvoiceData.Cost(
                mTotalMaterialCostTextField.getText(),
                mTotalLaborCostTextField.getText(),
                mTotalCostTextField.getText()
        );
        InvoiceData invoiceData = new InvoiceData(invoiceMetadata, rows, cost);

        String filepath = InvoicePDFCreator.getInstance().createFile(invoiceData);

        Stage stage = new Stage();
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(mRootPane.getScene().getWindow());
        VBox comp = new VBox();
        comp.setAlignment(Pos.CENTER);
        Label label = new Label("PDF文件已保存: " + filepath);
        label.setAlignment(Pos.CENTER);
        comp.getChildren().add(label);
        Scene scene = new Scene(comp, 500, 150);
        stage.setScene(scene);
        stage.show();
    }

    public class InvoiceTableRow {
        private final String description;
        private final String descriptionCN;
        private final String materialCost;
        private final String laborCost;
        public InvoiceTableRow(String description, String descriptionCN, String materialCost, String laborCost) {
            this.description = description;
            this.descriptionCN = descriptionCN;
            this.materialCost = materialCost;
            this.laborCost = laborCost;
        }

        public String getDescription() {
            return this.description;
        }

        public String getDescriptionCN() {
            return this.descriptionCN;
        }

        public String getMaterialCost() {
            return this.materialCost;
        }

        public String getLaborCost() {
            return this.laborCost;
        }
    }
}
