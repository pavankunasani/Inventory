package org.application;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.application.model.Outward; // Import the Outward class
import org.application.service.IInventoryService;
import org.application.service.InventoryServiceImpl;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class MaterialOutward {

    private ComboBox<String> itemNameComboBox;
    private TextField stockLeftField; // Disabled field for stock left
    private TextField quantityField;
    private VBox displayArea;
    private TableView<Outward> table;
    private Button submitButton; // Declare submit button
    private Map<String, Integer> inventoryMap;

    public MaterialOutward() {
        // Initialize UI components
        itemNameComboBox = new ComboBox<>();
        itemNameComboBox.setPromptText("Select Item Name");
        itemNameComboBox.setOnAction(e -> updateStockLeftField());

        stockLeftField = new TextField();
        stockLeftField.setPromptText("Stock Left");
        stockLeftField.setEditable(false); // Make it read-only

        quantityField = new TextField();
        quantityField.setPromptText("Quantity");

        submitButton = new Button("Submit");
        submitButton.setOnAction(e -> submitOutwardData());

        displayArea = new VBox();
        displayArea.setPadding(new Insets(10));

        populateInventoryItems();
        setupTable();
        execute();
    }

    public void execute() {
        // Create the main layout section
        VBox mainSection = new VBox();
        mainSection.setSpacing(20);
        mainSection.setPadding(new Insets(20)); // Padding around the main section
        mainSection.setPrefWidth(800); // Set a preferred width for the panel

        // Create the Add Outward section
        VBox addOutwardSection = createSectionBox("Add Outward", createAddOutwardFields());
        
        // Clear the display area and add the sections and table
        displayArea.getChildren().clear();
        displayArea.getChildren().addAll(addOutwardSection, table);
    }

    private VBox createSectionBox(String title, Node content) {
        VBox sectionBox = new VBox();
        sectionBox.setPadding(new Insets(15));
        sectionBox.setStyle("-fx-background-color: #f0f0f0; -fx-border-color: #d0d0d0; -fx-border-radius: 5; -fx-background-radius: 5;");
        sectionBox.getChildren().addAll(new Label(title), content);
        return sectionBox;
    }

    private HBox createAddOutwardFields() {
        HBox addOutwardFields = new HBox(10);
        addOutwardFields.getChildren().addAll(itemNameComboBox, stockLeftField, quantityField, submitButton);
        return addOutwardFields;
    }

    private void populateInventoryItems() {
        IInventoryService service = new InventoryServiceImpl();
        try {
            List<InventoryItems> allInventoryItems = service.getAllInventoryItems();
            inventoryMap = allInventoryItems.stream()
                    .collect(Collectors.toMap(InventoryItems::getName, InventoryItems::getInventoryId));
            itemNameComboBox.getItems().addAll(allInventoryItems.stream().map(InventoryItems::getName).collect(Collectors.toList()));
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error loading inventory items.");
        }
    }

    private void updateStockLeftField() {
        String itemName = itemNameComboBox.getValue();
        if (itemName != null && inventoryMap.containsKey(itemName)) {
            IInventoryService service = new InventoryServiceImpl();
            int itemId = inventoryMap.get(itemName);
          //   double stockLeft = service.getStockLeft(itemId); // Method to fetch stock left
			stockLeftField.setText(String.valueOf(0));
        } else {
            stockLeftField.clear();
        }
    }

    private void setupTable() {
        table = new TableView<>();
        
        TableColumn<Outward, String> itemNameCol = new TableColumn<>("Item Name");
        itemNameCol.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        itemNameCol.setPrefWidth(200);

        TableColumn<Outward, Double> quantityCol = new TableColumn<>("Quantity");
        quantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        quantityCol.setPrefWidth(100);

        TableColumn<Outward, Timestamp> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("createdAt"));
        dateCol.setPrefWidth(150);

        table.getColumns().addAll(itemNameCol, quantityCol, dateCol);
    }

    private void submitOutwardData() {
        String itemName = itemNameComboBox.getValue();
        String quantityText = quantityField.getText();

        if (itemName != null && !quantityText.isEmpty()) {
            double quantity;
            try {
                quantity = Double.parseDouble(quantityText);
            } catch (NumberFormatException e) {
                showAlert("Quantity must be a valid number.");
                return;
            }

            Integer itemId = inventoryMap.get(itemName);
            Outward newOutward = new Outward();
            newOutward.setItemId(itemId);
//            newOutward.setQuantity(quantity);
//            newOutward.setCreatedAt(Timestamp.valueOf(LocalDate.now().atStartOfDay())); // Set current date

            table.getItems().add(newOutward);

            // Clear the fields after submission
            itemNameComboBox.setValue(null);
            stockLeftField.clear();
            quantityField.clear();
        } else {
            showAlert("Please fill in all fields.");
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public VBox getDisplayArea() {
        return displayArea;
    }
}
