package org.application;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.application.model.Inward; // Import the Inward class
import org.application.service.IInventoryService;
import org.application.service.InventoryServiceImpl;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.converter.DoubleStringConverter;

public class MaterialInward {

    private ComboBox<String> itemNameComboBox;
    private TextField quantityField;
    private TextField unitPriceField;
    private VBox displayArea;
    private TableView<Inward> table;
    private DatePicker startDatePicker;
    private DatePicker endDatePicker;
    private Button searchButton;
    private Button addButton; // Declare add button
    private Button nextButton; // Next page button
    private Button previousButton; // Previous page button
    private int currentPage = 0;
    private  int pageSize = 5; // Set your page size
    private ObservableList<String> itemNames = FXCollections.observableArrayList();
    private Map<String, Integer> inventoryMap;

    public MaterialInward() {
        // Initialize UI components
        itemNameComboBox = new ComboBox<>(itemNames);
        itemNameComboBox.setPromptText("Select Item Name");

        quantityField = new TextField();
        quantityField.setPromptText("Quantity");

        unitPriceField = new TextField();
        unitPriceField.setPromptText("Unit Price");

        startDatePicker = new DatePicker();
        endDatePicker = new DatePicker();
        searchButton = new Button("Search");
        addButton = new Button("Add Item");

        // Initialize pagination buttons
        previousButton = new Button("Previous");
        nextButton = new Button("Next");

        // Set up button actions
        addButton.setOnAction(e -> addItemToDisplay());
        previousButton.setOnAction(e -> goToPreviousPage());
        nextButton.setOnAction(e -> goToNextPage());

        // Create the TableView before updating pagination buttons
        table = new TableView<>();
        setupTableColumns();
        updatePaginationButtons(); // Ensure buttons are updated after table creation

        // Set initial state for pagination buttons
        fetchItems(currentPage, pageSize); // Fetch initial items

        displayArea = new VBox();
        displayArea.setPadding(new Insets(10));

        populateInventoryItems();
        execute();
    }

    public VBox getDisplayArea() {
        return displayArea; // Return the display area for UI
    }

    public void execute() {
        // Create the main layout section
        VBox mainSection = new VBox();
        mainSection.setSpacing(20);
        mainSection.setPadding(new Insets(20)); // Padding around the main section
        mainSection.setPrefWidth(800); // Set a preferred width for the panel

        // Create the Add Item section
        VBox addItemSection = createSectionBox("Add Item", createAddItemFields());

        // Create the Search section
        VBox searchSection = createSectionBox("Search Inward Items", createSearchFields());

        // Combine Add Item and Search sections into a single panel
        HBox combinedSection = new HBox(20);
        combinedSection.getChildren().addAll(addItemSection, searchSection);

        // Clear the display area and add the sections and table
        displayArea.getChildren().clear();
        displayArea.getChildren().addAll(combinedSection, table, createPaginationControls());
    }

    private HBox createPaginationControls() {
        HBox paginationControls = new HBox(10);
        paginationControls.getChildren().addAll(previousButton, nextButton);
        return paginationControls;
    }

    private VBox createSectionBox(String title, Node content) {
        VBox sectionBox = new VBox();
        sectionBox.setPadding(new Insets(15));
        sectionBox.setStyle(
                "-fx-background-color: #f0f0f0; -fx-border-color: #d0d0d0; -fx-border-radius: 5; -fx-background-radius: 5;");
        sectionBox.getChildren().addAll(new Label(title), content);
        return sectionBox;
    }

    private HBox createAddItemFields() {
        HBox addItemFields = new HBox(10);
        addItemFields.getChildren().addAll(itemNameComboBox, quantityField, unitPriceField, addButton);
        return addItemFields;
    }

    private HBox createSearchFields() {
        HBox searchFields = new HBox(10);
        searchFields.getChildren().addAll(new Label("From:"), startDatePicker, new Label("To:"), endDatePicker,
                searchButton);
        return searchFields;
    }

    private void populateInventoryItems() {
        IInventoryService service = new InventoryServiceImpl();
        try {
            List<InventoryItems> allInventoryItems = service.getAllInventoryItems();
            inventoryMap = allInventoryItems.stream()
                    .collect(Collectors.toMap(InventoryItems::getName, InventoryItems::getInventoryId));
            itemNames.addAll(allInventoryItems.stream().map(InventoryItems::getName).collect(Collectors.toList()));
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error loading inventory items.");
        }
    }

    private void fetchItems(int currentPage, int pageSize) {
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();
        this.currentPage=currentPage;
        this.pageSize=pageSize;
        
        // Call the service to fetch items for the given date range and page
        IInventoryService invService = new InventoryServiceImpl();
        try {
            List<Inward> inwardItems = invService.getInwardItems(currentPage, pageSize, startDate, endDate);
            table.getItems().clear(); // Clear existing items
            table.getItems().addAll(inwardItems);
            updatePaginationButtons(); // Update button states based on the fetched items
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error fetching inward items.");
        }
    }

    private void updatePaginationButtons() {
        if (table != null) { // Check if table is initialized
            // Disable the Previous button if on the first page
            previousButton.setDisable(currentPage <= 0);
            // Disable the Next button based on whether there are enough items to fill the next page
            nextButton.setDisable(table.getItems().size() < pageSize);
        }
    }

    private void goToNextPage() {
        currentPage++;
        fetchItems(currentPage, pageSize); // Fetch items for the new page
    }

    private void goToPreviousPage() {
        if (currentPage > 0) { // Prevent going to negative page
            currentPage--;
            fetchItems(currentPage, pageSize); // Fetch items for the new page
        }
    }

    private void setupTableColumns() {
        TableColumn<Inward, String> itemNameCol = new TableColumn<>("Item Name");
        itemNameCol.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        itemNameCol.setPrefWidth(200);

        TableColumn<Inward, Double> initialStockCol = new TableColumn<>("Initial Stock");
        initialStockCol.setCellValueFactory(new PropertyValueFactory<>("initialStock"));
        initialStockCol.setPrefWidth(100);

        TableColumn<Inward, Double> stockLeftCol = new TableColumn<>("Stock Left");
        stockLeftCol.setCellValueFactory(new PropertyValueFactory<>("stockLeft"));
        stockLeftCol.setPrefWidth(100);

        TableColumn<Inward, Double> unitPriceCol = new TableColumn<>("Unit Price");
        unitPriceCol.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        unitPriceCol.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        unitPriceCol.setPrefWidth(100);

        TableColumn<Inward, Double> overallPriceCol = new TableColumn<>("Overall Price");
        overallPriceCol.setCellValueFactory(new PropertyValueFactory<>("overAllPrice"));
        overallPriceCol.setPrefWidth(100);

        TableColumn<Inward, LocalDate> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("createdAt"));
        dateCol.setPrefWidth(150);

        TableColumn<Inward, String> smartInfoCol = new TableColumn<>("Smart Info");
        smartInfoCol.setCellValueFactory(new PropertyValueFactory<>("inwardSmartInfo"));
        smartInfoCol.setPrefWidth(150);

        TableColumn<Inward, Void> viewButtonCol = new TableColumn<>("Actions");
        viewButtonCol.setCellFactory(col -> {
            Button btn = new Button("View");
            btn.setOnAction(event -> showAlert("Viewing item details..."));
            return new TableCell<Inward, Void>() {
                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        setGraphic(btn);
                    }
                }
            };
        });

        table.getColumns().addAll(itemNameCol, initialStockCol, stockLeftCol, unitPriceCol, overallPriceCol, dateCol, smartInfoCol, viewButtonCol);
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void addItemToDisplay() {
        // Implement your logic to add an item to the display
        showAlert("Item added: " + itemNameComboBox.getValue());
        fetchItems(currentPage, pageSize); // Refresh the table after adding an item
    }
}
