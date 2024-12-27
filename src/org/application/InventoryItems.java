package org.application;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class InventoryItems {

	private final SimpleIntegerProperty inventoryId; // Property for stock quantity
	private final SimpleStringProperty name; // Property for stock name
	private final SimpleIntegerProperty quantity; // Property for stock quantity
	private final SimpleStringProperty unit; // Property for unit (e.g., Meter, Square Meter, No's)

	// Constructor with name, quantity, and unit
	public InventoryItems(String name, int quantity, String unit, int inventoryId) {
		this.name = new SimpleStringProperty(name);
		this.quantity = new SimpleIntegerProperty(quantity);
		this.unit = new SimpleStringProperty(unit);
		this.inventoryId = new SimpleIntegerProperty(inventoryId);
	}

	// Constructor with name and quantity only
	public InventoryItems(String name, int quantity, int inventoryId) {
		this(name, quantity, null, 0); // Call the main constructor with null unit
	}

	// Constructor with name and unit only
	public InventoryItems(String name, String unit, int inventoryId) {
		this(name, 0, unit, inventoryId); // Call the main constructor with quantity set to 0
	}

	public InventoryItems(String name, String unit) {
		this(name, 0, unit, 0); // Call the main constructor with quantity set to 0
	}

	// Property getters
	public StringProperty nameProperty() {
		return name;
	}

	public SimpleIntegerProperty quantityProperty() {
		return quantity;
	}

	public StringProperty unitProperty() {
		return unit; // Getter for the unit property
	}

	public SimpleIntegerProperty inventoryIdProperty() {
		return inventoryId;
	}

	// Getters for the properties
	public String getName() {
		return name.get();
	}

	public int getQuantity() {
		return quantity.get();
	}

	public String getUnit() {
		return unit != null ? unit.get() : null; // Handle null case
	}

	public int getInventoryId() {
		return inventoryId != null ? inventoryId.get() : null; // Handle null case
	}

	// Setters for the properties
	public void setName(String name) {
		this.name.set(name);
	}

	public void setQuantity(int quantity) {
		this.quantity.set(quantity);
	}

	public void setUnit(String unit) {
		this.unit.set(unit);
	}

	public void setInventoryId(int inventoryId) {
		this.inventoryId.set(inventoryId);
	}

}
