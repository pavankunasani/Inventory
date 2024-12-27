package org.application.service;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import org.application.InventoryItems;
import org.application.dao.IInventoryDao;
import org.application.dao.InventoryDaoImpl;
import org.application.model.Inventory;
import org.application.model.Inward;

public class InventoryServiceImpl implements IInventoryService {

	private IInventoryDao inventoryDao;

	public InventoryServiceImpl() {
		inventoryDao = new InventoryDaoImpl();
	}

	public int insertInventory(Inventory inventory) throws SQLException {
		inventoryDao = new InventoryDaoImpl();
		return inventoryDao.insertInventory(inventory);
	}

	public int updateMeasurementUnit(int itemId, String measurementUnit) throws SQLException {
		return inventoryDao.updateMeasurementUnit(itemId, measurementUnit);
	}

	public List<String> getUnits() throws SQLException {
		inventoryDao = new InventoryDaoImpl();
		return inventoryDao.getUnits();
	}

	@Override
	public List<InventoryItems> getAllInventoryItems() throws SQLException {
		inventoryDao = new InventoryDaoImpl();
		return inventoryDao.getAllInventoryItems();
	}

	public void softDeleteInventory(int itemId) throws SQLException {
		inventoryDao = new InventoryDaoImpl();
		inventoryDao.softDeleteInventory(itemId);
	}

	public int insertStock(Inward saveInward) throws SQLException {

		return inventoryDao.insertStock(saveInward);

	}

	@Override
	public List<Inward> getInwardItems(int currentPage, int pageSize, LocalDate startDate, LocalDate endDate)
			throws SQLException {
		return inventoryDao.getInwardItems(currentPage, pageSize, startDate, endDate);
	}
}
