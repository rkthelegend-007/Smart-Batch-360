package com.batching.view;

import com.batching.model.SiteData;
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

public class SiteDetails {

    private final List<SiteData> siteList = new ArrayList<>();
    private final ListView<String> savedSiteList = new ListView<>();

    private final TextField idField = new TextField();
    private final TextField nameField = new TextField();
    private final TextField addressField = new TextField();
    private final TextField contactField = new TextField();
    private final TextField gstField = new TextField();

    public Pane createSiteContent() {
        HBox rootLayout = new HBox(30);
        rootLayout.setPadding(new Insets(30));

        VBox formContainer = new VBox(20);
        formContainer.setPrefWidth(400);

        Label title = new Label("🏗️ Site Details");
        title.getStyleClass().add("title-label");

        GridPane form = new GridPane();
        form.setVgap(15);
        form.setHgap(20);

        idField.setTextFormatter(new TextFormatter<>(change -> change.getControlNewText().matches("\\d{0,4}") ? change : null));
        nameField.setTextFormatter(new TextFormatter<>(change -> change.getControlNewText().length() <= 30 ? change : null));
        addressField.setTextFormatter(new TextFormatter<>(change -> change.getControlNewText().length() <= 50 ? change : null));
        contactField.setTextFormatter(new TextFormatter<>(change -> change.getControlNewText().matches("\\d{0,12}") ? change : null));
        gstField.setTextFormatter(new TextFormatter<>(change -> change.getControlNewText().length() <= 15 ? change : null));

        form.add(new Label("Site ID:"), 0, 0); form.add(idField, 1, 0);
        form.add(new Label("Site Name:"), 0, 1); form.add(nameField, 1, 1);
        form.add(new Label("Address:"), 0, 2); form.add(addressField, 1, 2);
        form.add(new Label("Contact Info:"), 0, 3); form.add(contactField, 1, 3);
        form.add(new Label("GST Number:"), 0, 4); form.add(gstField, 1, 4);

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
        searchField.setPromptText("🔍 Search by name");

        savedBox.getChildren().addAll(new Label("📁 Saved Sites"), searchField, savedSiteList,deleteBtn);

        rootLayout.getChildren().addAll(formContainer, savedBox);

        loadSites();

        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            savedSiteList.getItems().clear();
            siteList.stream()
                    .filter(s -> s.getSiteName().toLowerCase().contains(newVal.toLowerCase()))
                    .forEach(s -> savedSiteList.getItems().add(s.getSiteId() + " - " + s.getSiteName()));
        });

        saveBtn.setOnAction(e -> {
            int id = getId();
            if (id == -1 || nameField.getText().isEmpty()) {
                showAlert("Validation", "Please enter valid ID and Name");
                return;
            }

            HttpRequest checkReq = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/api/sites/check?id=" + id))
                    .build();

            HttpClient.newHttpClient().sendAsync(checkReq, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::body)
                    .thenAccept(body -> Platform.runLater(() -> {
                        if ("true".equalsIgnoreCase(body)) {
                            showAlert("Duplicate", "Site already exists");
                        } else {
                            saveSite(id);
                        }
                    }));
        });

        updateBtn.setOnAction(e -> {
            int id = getId();
            if (id == -1) return;
            saveSite(id); // PUT method for update
        });

        deleteBtn.setOnAction(e -> {
            String selected = savedSiteList.getSelectionModel().getSelectedItem();
            if (selected == null) return;
            int id = Integer.parseInt(selected.split("-")[0].trim());

            HttpRequest req = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/api/sites/" + id))
                    .DELETE()
                    .build();

            HttpClient.newHttpClient().sendAsync(req, HttpResponse.BodyHandlers.discarding())
                    .thenRun(() -> Platform.runLater(() -> {
                        showAlert("Deleted", "Site deleted");
                        loadSites();
                    }));
        });

        clearBtn.setOnAction(e -> clearForm());

        savedSiteList.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && newVal.contains("-")) {
                int id = Integer.parseInt(newVal.split("-")[0].trim());
                siteList.stream()
                        .filter(s -> s.getSiteId() == id)
                        .findFirst()
                        .ifPresent(this::populateForm);
            }
        });

        return rootLayout;
    }

    private void populateForm(SiteData s) {
        idField.setText(String.valueOf(s.getSiteId()));
        nameField.setText(s.getSiteName());
        addressField.setText(s.getSiteAddress());
        contactField.setText(s.getContactInfo());
        gstField.setText(s.getGstNumber());
    }

    private void clearForm() {
        idField.clear(); nameField.clear(); addressField.clear();
        contactField.clear(); gstField.clear();
    }

    private int getId() {
        try {
            return Integer.parseInt(idField.getText());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private void saveSite(int id) {
        SiteData s = new SiteData();
        s.setSiteId(id);
        s.setSiteName(nameField.getText());
        s.setSiteAddress(addressField.getText());
        s.setContactInfo(contactField.getText());
        s.setGstNumber(gstField.getText());

        ObjectMapper mapper = new ObjectMapper();
        try {
            String json = mapper.writeValueAsString(s);
            boolean isUpdate = siteList.stream().anyMatch(x -> x.getSiteId() == id);

            HttpRequest req = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/api/sites" + (isUpdate ? "/" + id : "")))
                    .header("Content-Type", "application/json")
                    .method(isUpdate ? "PUT" : "POST", HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpClient.newHttpClient().sendAsync(req, HttpResponse.BodyHandlers.ofString())
                    .thenAccept(resp -> Platform.runLater(() -> {
                        showAlert("Success", isUpdate ? "Updated!" : "Saved!");
                        clearForm();
                        loadSites();
                    }));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadSites() {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/api/sites"))
                .build();

        HttpClient.newHttpClient().sendAsync(req, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(json -> {
                    ObjectMapper mapper = new ObjectMapper();
                    try {
                        List<SiteData> loaded = mapper.readValue(json, new TypeReference<>() {});
                        siteList.clear();
                        siteList.addAll(loaded);
                        Platform.runLater(() -> {
                            savedSiteList.getItems().clear();
                            loaded.forEach(s -> savedSiteList.getItems().add(s.getSiteId() + " - " + s.getSiteName()));
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
