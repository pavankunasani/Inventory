package org.application;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.application.model.Inventory;
import org.application.service.IInventoryService;
import org.application.service.InventoryServiceImpl;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class WelcomePane extends BorderPane {

	private VBox contentArea; // To switch content dynamically
	private ObservableList<InventoryItems> masterData; // List to hold inventory data
	private TableView<InventoryItems> masterDataTable; // Table to display inventory data
	private TextField stockNameField; // TextField for stock name input
	private ComboBox<String> quantityUnitComboBox; // ComboBox for quantity units (Meter, Square Meter, etc.)
	private Button addButton;
	private Button updateButton;
	private Button clearButton;

	private Label usernameLabel; // Label to display the logged-in username
	List<String> units = new ArrayList<>();

	public WelcomePane(Stage stage, String username) {
		// Create the MenuBar
		MenuBar menuBar = new MenuBar();

		// Create Master Data Menu
		Menu masterDataMenu = new Menu("Master Data");
		MenuItem inventoryItem = new MenuItem("Inventory Master Data");
		inventoryItem.setOnAction(e -> showInventoryMasterData());

		// Create Show Stock Menu
		Menu showStockMenu = new Menu("Show Stock");
		MenuItem showStockItem = new MenuItem("Available Stock");
		showStockItem.setOnAction(e -> showAvailableStock());

		// Create Material Inward Menu
		Menu materialInwardMenu = new Menu("Material Inward");
		MenuItem inwardItem = new MenuItem("Inward");
		inwardItem.setOnAction(e -> showMaterialInward());
		materialInwardMenu.getItems().add(inwardItem);

		// Create Material Outward Menu
		Menu materialOutwardItem = new Menu("Material Outward");
		MenuItem outwardItem = new MenuItem("Outward");
		outwardItem.setOnAction(e -> showMaterialOutward());
		materialOutwardItem.getItems().add(outwardItem);

		// Add MenuItems to Menus
		masterDataMenu.getItems().add(inventoryItem);
		showStockMenu.getItems().add(showStockItem);
		menuBar.getMenus().addAll(masterDataMenu, showStockMenu, materialInwardMenu, materialOutwardItem);

		// Create a label to show the username
		usernameLabel = new Label("Logged user: " + username);
		usernameLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: blue;"); // Optional styling

		// Create an HBox to hold the menu bar and username label
		HBox header = new HBox(menuBar, usernameLabel);
		HBox.setHgrow(menuBar, Priority.ALWAYS); // Allow the menu bar to take up available space
		header.setAlignment(Pos.CENTER_RIGHT); // Align to the right

		// Set the HBox at the top of the BorderPane
		setTop(header);

		// Set up the content area for displaying stock information
		contentArea = new VBox();
		contentArea.setPadding(new Insets(20));
		contentArea.setSpacing(20);
		setCenter(contentArea);

		// Initialize the master data list
		masterData = FXCollections.observableArrayList();
	}

	private void showAvailableStock() {
		// Clear previous content
		contentArea.getChildren().clear();

		// Create headers
		Text title = new Text("Available Stock");
		title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-padding: 10;");

		// Create table for stock display
		TableView<StockItem> stockTable = new TableView<>();

		// Define columns
		TableColumn<StockItem, String> nameColumn = new TableColumn<>("Stock Name");
		nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());

		TableColumn<StockItem, Integer> quantityColumn = new TableColumn<>("Available Quantity");
		quantityColumn.setCellValueFactory(cellData -> cellData.getValue().quantityProperty().asObject());

		// Add columns to table
		stockTable.getColumns().add(nameColumn);
		stockTable.getColumns().add(quantityColumn);

		// Sample stock data
		stockTable.getItems().addAll(new StockItem("Item 1", 100, 0), new StockItem("Item 2", 250, 0),
				new StockItem("Item 3", 50, 0));

		// Add title and table to content area
		contentArea.getChildren().addAll(title, stockTable);
	}

	private void showInventoryMasterData() {
		// Clear previous content
		contentArea.getChildren().clear();

		// Create headers
		Text title = new Text("Inventory Master Data");
		title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-padding: 10;");

		// Create a GridPane for inventory inputs and buttons
		GridPane gridPane = createInputGrid();

		// Sample data area (TableView)
		masterData.clear();
		masterDataTable = new TableView<>(masterData);
		masterDataTable.setPrefWidth(400); // Set a preferred width for the table
		masterDataTable.setMinHeight(200); // Set a minimum height for the table

		// Define columns for master data table
		setupMasterDataTableColumns();

		// Load initial data into the master data
		loadInventoryData();

		// Wrap the TableView in a ScrollPane
		ScrollPane scrollPane = new ScrollPane(masterDataTable);
		scrollPane.setFitToWidth(true); // Adjust width to fit
		scrollPane.setPrefHeight(300); // Set a preferred height for the ScrollPane

		// Add title, input fields, buttons, and table to content area
		contentArea.getChildren().addAll(title, gridPane, scrollPane);

		// Set up button actions
		setupButtonActions();

		// Set up listener for row selection
		setupRowSelectionListener();
	}

	private void setupMasterDataTableColumns() {
		TableColumn<InventoryItems, String> masterNameColumn = new TableColumn<>("Stock Name");
		masterNameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
		masterNameColumn.setPrefWidth(200); // Set preferred width for Stock Name column

		TableColumn<InventoryItems, String> masterUnitColumn = new TableColumn<>("Unit");
		masterUnitColumn.setCellValueFactory(cellData -> cellData.getValue().unitProperty());
		masterUnitColumn.setPrefWidth(100); // Set preferred width for Unit column

		// Add a delete button column
		TableColumn<InventoryItems, Void> deleteColumn = createDeleteColumn();
		deleteColumn.setPrefWidth(100); // Set preferred width for Action column

		masterDataTable.getColumns().addAll(masterNameColumn, masterUnitColumn, deleteColumn);
	}

	private GridPane createInputGrid() {
		GridPane gridPane = new GridPane();
		gridPane.setPadding(new Insets(10));
		gridPane.setHgap(10);
		gridPane.setVgap(10);
		gridPane.setPrefWidth(400); // Set reduced width

		// Stock Name
		stockNameField = new TextField();
		stockNameField.setPromptText("Stock Name");
		gridPane.add(stockNameField, 0, 0);

		// Stock Quantity (ComboBox for Meter, Square Meter, No's)
		quantityUnitComboBox = new ComboBox<>(FXCollections.observableArrayList(loadUnits()));
		quantityUnitComboBox.setPromptText("Select Unit");
		gridPane.add(quantityUnitComboBox, 1, 0);

		// Buttons
		addButton = new Button("Add");
		updateButton = new Button("Update");

		clearButton = new Button("Clear"); // Add clear button
		gridPane.add(addButton, 0, 1);
		gridPane.add(updateButton, 1, 1);
		gridPane.add(clearButton, 2, 1);

		return gridPane;
	}

	private List<String> loadUnits() {
		IInventoryService inventoryService = new InventoryServiceImpl();
		try {
			return inventoryService.getUnits();
		} catch (SQLException e) {
			e.printStackTrace();
			return Collections.emptyList();
		}
	}

	private TableColumn<InventoryItems, Void> createDeleteColumn() {
		TableColumn<InventoryItems, Void> deleteColumn = new TableColumn<>("Action");
		deleteColumn.setCellFactory(param -> new TableCell<InventoryItems, Void>() {
			private final Button deleteButton = new Button("Delete");

			{
				deleteButton.setOnAction(event -> {
					InventoryItems stockItem = getTableView().getItems().get(getIndex());
					masterData.remove(stockItem);

					IInventoryService service = new InventoryServiceImpl();
					try {
						service.softDeleteInventory(stockItem.getInventoryId());
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				});
			}

			@Override
			protected void updateItem(Void item, boolean empty) {
				super.updateItem(item, empty);
				setGraphic(empty ? null : deleteButton);
			}
		});
		return deleteColumn;
	}

	private void loadInventoryData() {
		IInventoryService service = new InventoryServiceImpl();
		try {
			List<InventoryItems> allInventoryItems = service.getAllInventoryItems();
			masterData.addAll(allInventoryItems);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void setupButtonActions() {
		addButton.setOnAction(e -> addInventory());
		updateButton.setOnAction(e -> updateInventory());
		clearButton.setOnAction(e -> clearFields()); // Clear button action
	}

	private void setupRowSelectionListener() {
		masterDataTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue != null) {
				stockNameField.setText(newValue.getName());
				quantityUnitComboBox.setValue(newValue.getUnit());
				addButton.setDisable(true);

			}
		});
	}

	private void clearFields() {
		stockNameField.clear();
		quantityUnitComboBox.getSelectionModel().clearSelection();
		addButton.setDisable(false);
	}

	private void addInventory() {
		String name = stockNameField.getText();
		String selectedUnit = quantityUnitComboBox.getValue();

		IInventoryService inventoryService = new InventoryServiceImpl();
		Inventory inventory = new Inventory();
		inventory.setItemName(name);
		inventory.setMeasurementUnit(selectedUnit);
		try {
			inventoryService.insertInventory(inventory);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (!name.isEmpty() && selectedUnit != null) {
			masterData.add(new InventoryItems(name, selectedUnit)); // Assuming StockItem takes name and unit
			stockNameField.clear();
			quantityUnitComboBox.getSelectionModel().clearSelection();
		} else {
			showAlert("Please fill in both fields.");
		}
	}

	private void updateInventory() {
		InventoryItems selectedItem = masterDataTable.getSelectionModel().getSelectedItem();

		if (selectedItem != null) {
			String name = stockNameField.getText().trim();
			String selectedUnit = quantityUnitComboBox.getValue();

			if (!name.isEmpty() && selectedUnit != null) {
				// Update properties of the selected stock item
				selectedItem.nameProperty().set(name);
				selectedItem.unitProperty().set(selectedUnit); // Set the selected unit
				IInventoryService inventoryService = new InventoryServiceImpl();
				try {
					inventoryService.updateMeasurementUnit(selectedItem.getInventoryId(), selectedUnit);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// Clear the input fields
				stockNameField.clear();
				quantityUnitComboBox.getSelectionModel().clearSelection();
			} else {
				showAlert("Please fill in both fields."); // Alert for empty fields
			}
		} else {
			showAlert("Please select an item to update."); // Alert if no item is selected
		}
	}


	private void showMaterialInward() {
		System.out.println("in ward material");
		// Clear previous content
		contentArea.getChildren().clear();

		// Create an instance of MaterialInward
		MaterialInward materialInward = new MaterialInward();

		// Add the display area of Material Inward to the content area
		contentArea.getChildren().add(materialInward.getDisplayArea());
	}

	private void showMaterialOutward() {
		// Implement the action for Material Outward
		contentArea.getChildren().clear();
		MaterialOutward materialOutward = new MaterialOutward();
		materialOutward.execute();
	}

	private void showAlert(String message) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("Information Dialog");
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}
}
