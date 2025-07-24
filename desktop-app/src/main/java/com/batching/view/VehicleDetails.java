package com.batching.view;

import com.batching.model.VehicleData;
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

public class VehicleDetails {

    private final List<VehicleData> vehicleList = new ArrayList<>();
    private final ListView<String> savedVehicleList = new ListView<>();
    private final TextField idField = new TextField();
    private final TextField numberField = new TextField();

    public Pane createVehicleContent() {
        HBox rootLayout = new HBox(30);
        rootLayout.setPadding(new Insets(30));

        VBox formContainer = new VBox(20);
        formContainer.setPrefWidth(400);

        Label title = new Label("🚚 Vehicle Details");
        title.getStyleClass().add("title-label");

        GridPane form = new GridPane();
        form.setVgap(15);
        form.setHgap(20);

        idField.setTextFormatter(new TextFormatter<>(change -> change.getControlNewText().matches("\\d{0,3}") ? change : null));
        numberField.setTextFormatter(new TextFormatter<>(change -> change.getControlNewText().length() <= 20 ? change : null));

        form.add(new Label("Vehicle ID:"), 0, 0);
        form.add(idField, 1, 0);

        form.add(new Label("Vehicle Number:"), 0, 1);
        form.add(numberField, 1, 1);

        Button saveBtn = new Button("💾 Save");
        Button updateBtn = new Button("🔄 Update");
        Button deleteBtn = new Button("🗑️ Delete");
        deleteBtn.setMaxWidth(Double.MAX_VALUE);
        Button clearBtn = new Button("🧹 Clear");

        HBox actions = new HBox(10, saveBtn, updateBtn, clearBtn);
        actions.setPadding(new Insets(20, 0, 0, 0));

        formContainer.getChildren().addAll(title, form, actions);

        VBox savedBox = new VBox(10);
        savedBox.setPrefWidth(300);
        savedBox.setPadding(new Insets(10));
        savedBox.setStyle("-fx-background-color: #f9f9f9; -fx-border-color: #ccc;");

        TextField searchField = new TextField();
        searchField.setPromptText("🔍 Search by vehicle number");

        savedBox.getChildren().addAll(new Label("📦 Saved Vehicles"), searchField, savedVehicleList, deleteBtn);

        rootLayout.getChildren().addAll(formContainer, savedBox);

        loadVehicles();

        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            savedVehicleList.getItems().clear();
            vehicleList.stream()
                    .filter(v -> v.getVehicleNumber().toLowerCase().contains(newVal.toLowerCase()))
                    .forEach(v -> savedVehicleList.getItems().add(v.getVehicleId() + " - " + v.getVehicleNumber()));
        });

        saveBtn.setOnAction(e -> {
            int id = getId();
            if (id == -1 || numberField.getText().isEmpty()) {
                showAlert("Validation", "Please enter valid Vehicle ID and Number");
                return;
            }

            HttpRequest checkReq = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/api/vehicles/check?id=" + id))
                    .build();

            HttpClient.newHttpClient().sendAsync(checkReq, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::body)
                    .thenAccept(body -> Platform.runLater(() -> {
                        if ("true".equalsIgnoreCase(body)) {
                            showAlert("Duplicate", "Vehicle already exists");
                        } else {
                            saveVehicle(id);
                        }
                    }));
        });

        updateBtn.setOnAction(e -> {
            int id = getId();
            if (id == -1) return;
            saveVehicle(id); // PUT
        });

        deleteBtn.setOnAction(e -> {
            String selected = savedVehicleList.getSelectionModel().getSelectedItem();
            if (selected == null) return;
            int id = Integer.parseInt(selected.split("-")[0].trim());

            HttpRequest req = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/api/vehicles/" + id))
                    .DELETE()
                    .build();

            HttpClient.newHttpClient().sendAsync(req, HttpResponse.BodyHandlers.discarding())
                    .thenRun(() -> Platform.runLater(() -> {
                        showAlert("Deleted", "Vehicle deleted");
                        clearForm();
                        loadVehicles();
                    }));
        });

        clearBtn.setOnAction(e -> clearForm());

        savedVehicleList.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && newVal.contains("-")) {
                int id = Integer.parseInt(newVal.split("-")[0].trim());
                vehicleList.stream()
                        .filter(v -> v.getVehicleId() == id)
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

    private void populateForm(VehicleData v) {
        idField.setText(String.valueOf(v.getVehicleId()));
        numberField.setText(v.getVehicleNumber());
    }

    private void clearForm() {
        idField.clear();
        numberField.clear();
    }

    private void saveVehicle(int id) {
        VehicleData v = new VehicleData();
        v.setVehicleId(id);
        v.setVehicleNumber(numberField.getText());

        ObjectMapper mapper = new ObjectMapper();
        try {
            String json = mapper.writeValueAsString(v);
            boolean isUpdate = vehicleList.stream().anyMatch(x -> x.getVehicleId() == id);

            HttpRequest req = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/api/vehicles" + (isUpdate ? "/" + id : "")))
                    .header("Content-Type", "application/json")
                    .method(isUpdate ? "PUT" : "POST", HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpClient.newHttpClient().sendAsync(req, HttpResponse.BodyHandlers.ofString())
                    .thenAccept(resp -> Platform.runLater(() -> {
                        showAlert("Success", isUpdate ? "Updated!" : "Saved!");
                        clearForm();
                        loadVehicles();
                    }));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadVehicles() {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/api/vehicles"))
                .build();

        HttpClient.newHttpClient().sendAsync(req, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(json -> {
                    ObjectMapper mapper = new ObjectMapper();
                    try {
                        List<VehicleData> loaded = mapper.readValue(json, new TypeReference<>() {});
                        vehicleList.clear();
                        vehicleList.addAll(loaded);
                        Platform.runLater(() -> {
                            savedVehicleList.getItems().clear();
                            loaded.forEach(v -> savedVehicleList.getItems().add(v.getVehicleId() + " - " + v.getVehicleNumber()));
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
