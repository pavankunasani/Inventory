package org.application.model;

public class InventoryItem {
	private int stockId;
    private String itemName;
    private String quantity;
    private double unitPrice;
    private int stockLeft;
    private int reorderLevel;
    private String createdBy;
    private String createdTime;
    private String updatedBy;
    private String status;

  

    public InventoryItem(int stockId, String itemName, String quantity, double unitPrice, int stockLeft, int reorderLevel,
			String createdBy, String createdTime, String updatedBy, String status) {
		super();
		this.stockId = stockId;
		this.itemName = itemName;
		this.quantity = quantity;
		this.unitPrice = unitPrice;
		this.stockLeft = stockLeft;
		this.reorderLevel = reorderLevel;
		this.createdBy = createdBy;
		this.createdTime = createdTime;
		this.updatedBy = updatedBy;
		this.status = status;
	}

	public String getItemName() {
        return itemName;
    }

    public String getQuantity() {
        return quantity;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public int getStockLeft() {
        return stockLeft;
    }

    public int getReorderLevel() {
        return reorderLevel;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public int getStockId() {
		return stockId;
	}

	public void setStockId(int stockId) {
		this.stockId = stockId;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public void setUnitPrice(double unitPrice) {
		this.unitPrice = unitPrice;
	}

	public void setStockLeft(int stockLeft) {
		this.stockLeft = stockLeft;
	}

	public void setReorderLevel(int reorderLevel) {
		this.reorderLevel = reorderLevel;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public void setCreatedTime(String createdTime) {
		this.createdTime = createdTime;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCreatedTime() {
        return createdTime;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public String getStatus() {
        return status;
    }
    
}
