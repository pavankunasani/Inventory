package org.application;

import org.application.daoconnection.DaoConnection;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
	public static String propertiesFilePath = "";

	@Override
	public void start(Stage stage) {
		// Create an instance of the LoginPane (the UI class)
		LoginPane loginPane = new LoginPane(stage); // Pass stage to LoginPane

		// Create a scene with the login pane
		Scene scene = new Scene(loginPane, 400, 400);

		// Load the CSS file
		scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());

		// Set the title and scene of the stage
		stage.setTitle("Inventory Login");
		stage.setScene(scene);
		stage.show();
	}

	public static void main(String[] args) {

		// Iterate over the command-line arguments
		System.out.println(args.length + " args leng");
		for (String arg : args) {
			if (arg.startsWith("--db=")) {
				propertiesFilePath = arg.split("=")[1];
				break;
			}
		}

		if (propertiesFilePath == null) {
			System.err.println("Please provide the path to the db.properties file using the --db argument.");
			return;
		}

		DaoConnection.initialize(propertiesFilePath);

		launch(args);
	}
}
