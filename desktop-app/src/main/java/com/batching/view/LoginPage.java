package com.batching.view;

import com.batching.license.LicenseValidator;
import javafx.animation.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

public class LoginPage {

    public void show(Stage primaryStage) {
        // Logo aligned to top-right
        ImageView logo = new ImageView(new Image(getClass().getResource("/images/smartbatch360.png").toExternalForm()));
        logo.setFitHeight(100);
        logo.setPreserveRatio(true);
        StackPane logoPane = new StackPane(logo);
        logoPane.setAlignment(Pos.TOP_RIGHT);
        logoPane.setPadding(new Insets(10));

        // Form content
        Label title = new Label("SmartBatch 360");
        title.getStyleClass().add("login-title");

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        Button loginButton = new Button("Login");

        Label messageLabel = new Label();
        messageLabel.getStyleClass().add("error-label");
        messageLabel.setVisible(false);

        VBox formBox = new VBox(12, title, usernameField, passwordField, loginButton, messageLabel);
        formBox.setAlignment(Pos.CENTER);
        formBox.setPadding(new Insets(20));
        formBox.getStyleClass().add("glass-card");

        formBox.setMaxWidth(320);
        formBox.setMinWidth(280);
        formBox.setMaxHeight(300);

        StackPane centerPane = new StackPane(formBox);
        centerPane.setAlignment(Pos.TOP_CENTER);

        BorderPane root = new BorderPane();
        root.setTop(logoPane);
        root.setCenter(centerPane);
        root.getStyleClass().add("login-root");

        // Standard dialog size
        Scene scene = new Scene(root, 1280, 720);
        scene.getStylesheets().add(getClass().getResource("/login.css").toExternalForm());

        animateForm(formBox);

        // Login logic
        loginButton.setOnAction(e -> {
            String user = usernameField.getText();
            String pass = passwordField.getText();

            if (user.equals("admin") && pass.equals("123")) {
                if (LicenseValidator.isLicenseValid()) {
                    Stage stage = (Stage) loginButton.getScene().getWindow();
                    new SmartBatch360UI(stage).showMain();
                } else {
                    messageLabel.setText("License expired.");
                    messageLabel.setVisible(true);
                    shake(formBox);
                }
            } else {
                messageLabel.setText("Invalid credentials.");
                messageLabel.setVisible(true);
                shake(formBox);
            }
        });

        primaryStage.setTitle("SmartBatch 360 Login");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.centerOnScreen(); // ✅ center window on screen
        primaryStage.show();
    }

    private void animateForm(Node node) {
        node.setOpacity(0);
        node.setTranslateY(20);

        FadeTransition fade = new FadeTransition(Duration.millis(600), node);
        fade.setFromValue(0);
        fade.setToValue(1);

        TranslateTransition slide = new TranslateTransition(Duration.millis(600), node);
        slide.setFromY(20);
        slide.setToY(0);

        new ParallelTransition(fade, slide).play();
    }

    private void shake(Node node) {
        TranslateTransition shake = new TranslateTransition(Duration.millis(50), node);
        shake.setFromX(0);
        shake.setByX(10);
        shake.setCycleCount(6);
        shake.setAutoReverse(true);
        shake.play();
    }
}
