package org.application.dao;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import org.application.InventoryItems;
import org.application.model.Inventory;
import org.application.model.Inward;

public interface IInventoryDao {

	public int insertInventory(Inventory inventory) throws SQLException;
	public int updateMeasurementUnit(int itemId, String measurementUnit) throws SQLException;

	public List<String> getUnits() throws SQLException;

	public List<InventoryItems> getAllInventoryItems() throws SQLException;

	public void softDeleteInventory(int itemId) throws SQLException;
	
	public int insertStock(Inward saveInward) throws SQLException;
	
	 List<Inward> getInwardItems(int currentPage, int pageSize, LocalDate startDate, LocalDate endDate) throws SQLException;
}
