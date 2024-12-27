package org.application;

import org.application.service.ILoginService;
import org.application.service.LoginServiceImpl;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class LoginPane extends GridPane {
	
	

	public LoginPane(Stage stage) {
		// Set properties of the GridPane
		setMinSize(400, 400);
		setPadding(new Insets(10, 10, 10, 10));
		setVgap(5);
		setHgap(5);
		setAlignment(Pos.CENTER);
		setStyle("-fx-background-color: BEIGE;");

		// Create labels and fields
		Text usernameLabel = new Text("username");
		Text passwordLabel = new Text("Password");

		TextField usernameField = new TextField();
		usernameField.setPromptText("Enter your username");

		PasswordField passwordField = new PasswordField();
		passwordField.setPromptText("Enter your password");

		// Create buttons
		Button submitButton = new Button("Submit");
		Button clearButton = new Button("Clear");

		Label errorLabel = new Label();
		errorLabel.setStyle("-fx-text-fill: red;"); // Set error message color
		errorLabel.setVisible(false); // Initially hidden

		// Add components to the GridPane
		add(usernameLabel, 0, 0);
		add(usernameField, 1, 0);
		add(passwordLabel, 0, 1);
		add(passwordField, 1, 1);
		add(submitButton, 0, 2);
		add(clearButton, 1, 2);
		add(errorLabel, 0, 3, 2, 1); // Span across two columns

		// Set action for Submit button
		submitButton.setOnAction(e -> {
			// Get the input values
			String username = usernameField.getText();
			String password = passwordField.getText();

			// Validate input
			if (username.isEmpty() || password.isEmpty()) {
				errorLabel.setText("username and Password cannot be empty.");
				errorLabel.setVisible(true); // Show error message
			} else {
				ILoginService loginService = new LoginServiceImpl();
				boolean isAuthenticated = loginService.userLogin(username, password);

				// Check if authentication was successful
				if (isAuthenticated) {
					errorLabel.setVisible(false); // Hide error message
					UserDetails.username=username;
					// Proceed to the WelcomePane
					 WelcomePane welcomePane = new WelcomePane(stage, username);
					Scene welcomeScene = new Scene(welcomePane, 600, 600);
					stage.setScene(welcomeScene);
				} else {
					// Show error message if authentication fails
					errorLabel.setText("Invalid username or password.");
					errorLabel.setVisible(true);
				}
			}
		});

		// Set action for Clear button
		clearButton.setOnAction(e -> {
			usernameField.clear(); // Clear the email field
			passwordField.clear(); // Clear the password field
		});
	}
}
