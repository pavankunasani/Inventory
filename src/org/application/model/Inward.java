package org.application.model;

import java.sql.Timestamp;

public class Inward {
	private int stockId; // Unique identifier for the stock record
	private int itemId; // Foreign key referencing the items table
	private String itemName;
	private double initialStock; // Initial quantity of stock when first added, allowing fractional values
	private double stockLeft; // Current quantity of stock left, allowing fractional values
	private double unitPrice; // Price per unit of the item
	private int reorderLevel; // Minimum quantity to trigger a reorder
	private Timestamp createdAt; // Timestamp of when the record was created
	private String createdBy; // User ID of the creator
	private Timestamp updatedAt; // Timestamp of the last update
	private String updatedBy; // User ID of the last modifier
	private String status; // Status of the stock record (active/closed)
	private String inwardSmartInfo;
	private Double overAllPrice;

	// Getters and Setters

	public int getStockId() {
		return stockId;
	}

	public void setStockId(int stockId) {
		this.stockId = stockId;
	}

	public int getItemId() {
		return itemId;
	}

	public void setItemId(int itemId) {
		this.itemId = itemId;
	}

	public double getInitialStock() {
		return initialStock;
	}

	public void setInitialStock(double initialStock) {
		this.initialStock = initialStock;
	}

	public double getStockLeft() {
		return stockLeft;
	}

	public void setStockLeft(double stockLeft) {
		this.stockLeft = stockLeft;
	}

	public double getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(double unitPrice) {
		this.unitPrice = unitPrice;
	}

	public int getReorderLevel() {
		return reorderLevel;
	}

	public void setReorderLevel(int reorderLevel) {
		this.reorderLevel = reorderLevel;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Timestamp getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}

	public Timestamp getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Timestamp updatedAt) {
		this.updatedAt = updatedAt;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getInwardSmartInfo() {
		return inwardSmartInfo;
	}

	public void setInwardSmartInfo(String inwardSmartInfo) {
		this.inwardSmartInfo = inwardSmartInfo;
	}

	public Double getOverAllPrice() {
		return overAllPrice;
	}

	public void setOverAllPrice(Double overAllPrice) {
		this.overAllPrice = overAllPrice;
	}
	

}
