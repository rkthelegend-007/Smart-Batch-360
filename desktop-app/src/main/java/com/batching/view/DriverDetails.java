package com.batching.view;

import com.batching.model.DriverData;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class DriverDetails {

    private final List<DriverData> driverList = new ArrayList<>();
    private final ListView<String> savedDriverList = new ListView<>();
    private final TextField idField = new TextField();
    private final TextField nameField = new TextField();
    private final TextField contactField = new TextField();

    public Pane createDriverContent() {
        HBox rootLayout = new HBox(30);
        rootLayout.setPadding(new Insets(30));

        VBox formContainer = new VBox(20);
        formContainer.setPrefWidth(400);

        Label title = new Label("\uD83D\uDC69 Driver Details");
        title.getStyleClass().add("title-label");

        GridPane form = new GridPane();
        form.setVgap(15);
        form.setHgap(20);

        idField.setTextFormatter(new TextFormatter<>(change -> change.getControlNewText().matches("\\d{0,3}") ? change : null));
        nameField.setTextFormatter(new TextFormatter<>(change -> change.getControlNewText().length() <= 30 ? change : null));
        contactField.setTextFormatter(new TextFormatter<>(change -> change.getControlNewText().matches("\\d{0,12}") ? change : null));

        form.add(new Label("Driver ID:"), 0, 0); form.add(idField, 1, 0);
        form.add(new Label("Driver Name:"), 0, 1); form.add(nameField, 1, 1);
        form.add(new Label("Contact Info:"), 0, 2); form.add(contactField, 1, 2);

        Button saveBtn = new Button("\uD83D\uDCBE Save");
        Button updateBtn = new Button("\uD83D\uDD04 Update");
        Button deleteBtn = new Button("🗑️ Delete");
        deleteBtn.setMaxWidth(Double.MAX_VALUE);
        Button clearBtn = new Button("\uD83E\uDEA9 Clear");

        HBox actions = new HBox(10, saveBtn, updateBtn, clearBtn);
        actions.setPadding(new Insets(20, 0, 0, 0));

        formContainer.getChildren().addAll(title, form, actions);

        VBox savedBox = new VBox(10);
        savedBox.setPrefWidth(300);
        savedBox.setPadding(new Insets(10));
        savedBox.setStyle("-fx-background-color: #f9f9f9; -fx-border-color: #ccc;");

        TextField searchField = new TextField();
        searchField.setPromptText("\uD83D\uDD0D Search by name");

        savedBox.getChildren().addAll(new Label("\uD83D\uDCCB Saved Drivers"), searchField, savedDriverList, deleteBtn);

        rootLayout.getChildren().addAll(formContainer, savedBox);

        loadDrivers();

        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            savedDriverList.getItems().clear();
            driverList.stream()
                    .filter(d -> d.getDriverName().toLowerCase().contains(newVal.toLowerCase()))
                    .forEach(d -> savedDriverList.getItems().add(d.getDriverId() + " - " + d.getDriverName()));
        });

        saveBtn.setOnAction(e -> {
            int id = getId();
            if (id == -1 || nameField.getText().isEmpty()) {
                showAlert("Validation", "Please enter valid ID and Name");
                return;
            }

            HttpRequest checkReq = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/api/drivers/check?id=" + id))
                    .build();

            HttpClient.newHttpClient().sendAsync(checkReq, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::body)
                    .thenAccept(body -> Platform.runLater(() -> {
                        if ("true".equalsIgnoreCase(body)) {
                            showAlert("Duplicate", "Driver already exists");
                        } else {
                            saveDriver(id);
                        }
                    }));
        });

        updateBtn.setOnAction(e -> {
            int id = getId();
            if (id == -1) return;
            saveDriver(id); // PUT method for update
        });

        deleteBtn.setOnAction(e -> {
            String selected = savedDriverList.getSelectionModel().getSelectedItem();
            if (selected == null) return;
            int id = Integer.parseInt(selected.split("-")[0].trim());
            HttpRequest req = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/api/drivers/" + id))
                    .DELETE()
                    .build();

            HttpClient.newHttpClient().sendAsync(req, HttpResponse.BodyHandlers.discarding())
                    .thenRun(() -> Platform.runLater(() -> {
                        showAlert("Deleted", "Driver deleted");
                        clearForm();
                        loadDrivers();
                    }));
        });

        clearBtn.setOnAction(e -> clearForm());

        savedDriverList.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && newVal.contains("-")) {
                int id = Integer.parseInt(newVal.split("-")[0].trim());
                driverList.stream()
                        .filter(d -> d.getDriverId() == id)
                        .findFirst()
                        .ifPresent(this::populateForm);
            }
        });

        return rootLayout;
    }

    private int getId() {
        try {
            return Integer.parseInt(idField.getText());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private void populateForm(DriverData d) {
        idField.setText(String.valueOf(d.getDriverId()));
        nameField.setText(d.getDriverName());
        contactField.setText(d.getContactNumber());
    }

    private void clearForm() {
        idField.clear(); nameField.clear(); contactField.clear();
    }

    private void saveDriver(int id) {
        DriverData d = new DriverData();
        d.setDriverId(id);
        d.setDriverName(nameField.getText());
        d.setContactNumber(contactField.getText());

        ObjectMapper mapper = new ObjectMapper();
        try {
            String json = mapper.writeValueAsString(d);
            boolean isUpdate = driverList.stream().anyMatch(x -> x.getDriverId() == id);
            HttpRequest req = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/api/drivers" + (isUpdate ? "/" + id : "")))
                    .header("Content-Type", "application/json")
                    .method(isUpdate ? "PUT" : "POST", HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpClient.newHttpClient().sendAsync(req, HttpResponse.BodyHandlers.ofString())
                    .thenAccept(resp -> Platform.runLater(() -> {
                        showAlert("Success", isUpdate ? "Updated!" : "Saved!");
                        clearForm();
                        loadDrivers();
                    }));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadDrivers() {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/api/drivers"))
                .build();

        HttpClient.newHttpClient().sendAsync(req, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(json -> {
                    ObjectMapper mapper = new ObjectMapper();
                    try {
                        List<DriverData> loaded = mapper.readValue(json, new TypeReference<>() {});
                        driverList.clear();
                        driverList.addAll(loaded);
                        Platform.runLater(() -> {
                            savedDriverList.getItems().clear();
                            loaded.forEach(d -> savedDriverList.getItems().add(d.getDriverId() + " - " + d.getDriverName()));
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
    }

    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
