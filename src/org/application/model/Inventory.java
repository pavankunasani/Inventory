package org.application.model;

public class Inventory {
	private int itemId;
	private String itemName;
	private String measurementUnit;
	private String supplier;
	private String description;
	private String barcode;

	// Constructors
	public Inventory(String itemName, String measurementUnit, String supplier, String description, String barcode) {
		this.itemName = itemName;
		this.measurementUnit = measurementUnit;
		this.supplier = supplier;
		this.description = description;
		this.barcode = barcode;
	}

	public Inventory() {
		super();
		// TODO Auto-generated constructor stub
	}

	// Getters
	public String getItemName() {
		return itemName;
	}

	public String getSupplier() {
		return supplier;
	}

	public String getDescription() {
		return description;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getMeasurementUnit() {
		return measurementUnit;
	}

	public void setMeasurementUnit(String measurementUnit) {
		this.measurementUnit = measurementUnit;
	}

	public void setSupplier(String supplier) {
		this.supplier = supplier;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public int getItemId() {
		return itemId;
	}

	public void setItemId(int itemId) {
		this.itemId = itemId;
	}

}
