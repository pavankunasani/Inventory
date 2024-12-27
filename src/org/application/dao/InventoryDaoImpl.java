package org.application.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.application.InventoryItems;
import org.application.UserDetails;
import org.application.model.Inventory;
import org.application.model.Inward;

public class InventoryDaoImpl implements IInventoryDao {

	private Connection getConnection() throws SQLException {
		String host = "35.244.30.178";
		String port = "3306";
		String username = "udaydb";
		String database = "inventory";
		String password = "udaydb";
		String url = "jdbc:mysql://" + host + ":" + port + "/" + database;
		return DriverManager.getConnection(url, username, password);

	}

	public int insertInventory(Inventory inventory) throws SQLException {
		String sql = "INSERT INTO inventory_items (item_name, measurement_unit, supplier, description, barcode,history) VALUES (?, ?, ?, ?, ?,?)";

		try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

			// Set the values for the placeholders using the Inventory object
			pstmt.setString(1, inventory.getItemName());
			pstmt.setString(2, inventory.getMeasurementUnit()); // Use the enum's value
			pstmt.setString(3, inventory.getSupplier());
			pstmt.setString(4, inventory.getDescription());
			pstmt.setString(5, inventory.getBarcode());
			pstmt.setString(6, "N");

			// Execute the insert statement
			int executeUpdate = pstmt.executeUpdate();
			return executeUpdate;
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		}
	}

	public int updateMeasurementUnit(int itemId, String measurementUnit) throws SQLException {
		String sql = "UPDATE inventory_items SET measurement_unit = ? WHERE item_id = ?";

		try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			// Set the values for the placeholders
			pstmt.setString(1, measurementUnit);
			pstmt.setInt(2, itemId); // Assuming itemId is the primary key

			// Execute the update statement
			int executeUpdate = pstmt.executeUpdate();
			return executeUpdate;
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		}
	}

	public List<String> getUnits() throws SQLException {
		String SELECT_UNITS_SQL = "SELECT unit FROM units";
		List<String> unitsList = new ArrayList<>();
		// Get a new connection every time this method is called
		try (Connection conn = getConnection(); // Ensure getConnection returns a new connection
				PreparedStatement pstmt = conn.prepareStatement(SELECT_UNITS_SQL);
				ResultSet rs = pstmt.executeQuery()) {

			while (rs.next()) {
				unitsList.add(rs.getString("unit"));
			}
		} catch (SQLException e) {
			System.err.println("Error retrieving units from database: " + e.getMessage());
			throw e; // Rethrow the exception to notify the caller
		}
		return unitsList;
	}

	public List<InventoryItems> getAllInventoryItems() throws SQLException {
		String query = "SELECT item_id, item_name, measurement_unit, created_at, updated_at FROM inventory_items where history='N'";
		List<InventoryItems> inventoryList = new ArrayList<>();

		try (PreparedStatement stmt = getConnection().prepareStatement(query); ResultSet rs = stmt.executeQuery()) {
			while (rs.next()) {
//				Inventory item = new Inventory();
				InventoryItems stock = new InventoryItems(rs.getString("item_name"), rs.getString("measurement_unit"),
						rs.getInt("item_id"));
//				item.setItemId(rs.getInt("item_id"));
//				item.setItemName(rs.getString("item_name"));
//				item.setMeasurementUnit(rs.getString("measurement_unit"));

				inventoryList.add(stock);
			}
		}

		return inventoryList;
	}

	@Override
	public void softDeleteInventory(int itemId) throws SQLException {
		String sql = "UPDATE inventory_items SET history = 'Y' WHERE item_id = ?";

		try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, itemId);
			stmt.executeUpdate();
		}
	}

	@Override
	public int insertStock(Inward saveInward) throws SQLException {
		String query = "INSERT INTO inward (item_id, initial_stock, stock_left, unit_price, reorder_level, "
				+ "created_by, updated_by, status, item_name_year_month_date_init_quantity) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

		try (PreparedStatement statement = getConnection().prepareStatement(query)) {
			statement.setInt(1, saveInward.getItemId());
			statement.setDouble(2, saveInward.getInitialStock()); // Set initialStock as double
			statement.setDouble(3, saveInward.getInitialStock()); // Set stockLeft as double
			statement.setDouble(4, saveInward.getUnitPrice());
			statement.setInt(5, saveInward.getReorderLevel());
			statement.setString(6, UserDetails.username); // Set created_by
			statement.setString(7, UserDetails.username); // Set updated_by
			statement.setString(8, saveInward.getStatus()); // Set status
			LocalDate today = LocalDate.now();
			// Create the smart info string combining item name, year, month, day, and
			// initial quantity
			String smartInfo = String.format("%s_%d_%d_%d_%s", saveInward.getItemName(), today.getYear(),
					today.getMonthValue(), today.getDayOfMonth(), saveInward.getInitialStock());
			statement.setString(9, smartInfo); // Set item_name_year_month_date_init_quantity

			return statement.executeUpdate();
		}
	}

	@Override
	public List<Inward> getInwardItems(int currentPage, int pageSize, LocalDate startDate, LocalDate endDate)
			throws SQLException {
		List<Inward> inwardItems = new ArrayList<>();
		StringBuilder query = new StringBuilder("SELECT i.*, inv.item_name FROM inward i");
		boolean hasDateCondition = false;

		// Check if the dates are provided
		if (startDate != null && endDate != null) {
			query.append(" WHERE i.created_at BETWEEN ? AND ?");
			hasDateCondition = true;
		}

		// Join with the inventory_items table
		query.append(" JOIN inventory_items inv ON i.item_id = inv.item_id");
		// Add ORDER BY clause to order by created_at and status in descending order
		query.append(" ORDER BY i.created_at DESC, i.status DESC"); // Order by created_at then by status

		query.append(" LIMIT ? OFFSET ?");

		try (PreparedStatement preparedStatement = getConnection().prepareStatement(query.toString())) {
			int paramIndex = 1;

			// Set date parameters if provided
			if (hasDateCondition) {
				preparedStatement.setTimestamp(paramIndex++, Timestamp.valueOf(startDate.atStartOfDay()));
				preparedStatement.setTimestamp(paramIndex++, Timestamp.valueOf(endDate.atTime(23, 59, 59))); // Include
																												// end
																												// date
			}

			// Set pagination parameters
			preparedStatement.setInt(paramIndex++, pageSize);
			preparedStatement.setInt(paramIndex++, currentPage * pageSize);

			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				Inward inward = new Inward();
				inward.setItemId(resultSet.getInt("item_id")); // Assuming this is in the inward table
				inward.setItemName(resultSet.getString("item_name")); // Assuming you have a setItemName method in
																		// Inward
				inward.setStockLeft(resultSet.getDouble("stock_left"));
				inward.setUnitPrice(resultSet.getDouble("unit_price"));
				Timestamp timestamp = resultSet.getTimestamp("created_at");
				inward.setCreatedAt(resultSet.getTimestamp("created_at"));
				inward.setStatus(resultSet.getString("status"));
				inward.setInitialStock(resultSet.getDouble("initial_stock"));
				inward.setInwardSmartInfo(resultSet.getString("item_name_year_month_date_init_quantity"));
				inwardItems.add(inward);
			}
		}
		return inwardItems;
	}

	public LocalDate convertTimestampToLocalDate(Timestamp timestamp) {
		return timestamp.toLocalDateTime().toLocalDate();
	}

}
