package org.application.model;

import java.sql.Timestamp;

public class Outward {
    private int outwardId;          // Unique identifier for outward record
    private int itemId;             // ID of the item being outwarded
    private int quantity;            // Quantity of the item being outwarded
    private String destination;      // Where the stock is going
    private Timestamp outwardDate;   // Date of the outward movement
    private int processedBy;         // User ID of the person processing the outward
    private String notes;            // Any additional notes

    // Getters and Setters

    public int getOutwardId() {
        return outwardId;
    }

    public void setOutwardId(int outwardId) {
        this.outwardId = outwardId;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public Timestamp getOutwardDate() {
        return outwardDate;
    }

    public void setOutwardDate(Timestamp outwardDate) {
        this.outwardDate = outwardDate;
    }

    public int getProcessedBy() {
        return processedBy;
    }

    public void setProcessedBy(int processedBy) {
        this.processedBy = processedBy;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
