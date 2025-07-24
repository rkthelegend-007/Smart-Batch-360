package com.batching.view;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;

import com.batching.model.HeaderData;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import java.util.List;
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
import java.util.Optional;

public class HeaderDetails {
    
private final List<HeaderData> headerList = new ArrayList<>();
private final ListView<String> savedHeaderList = new ListView<>();

private final TextField headerIdField = new TextField();
private final TextField companyNameField = new TextField();
private final TextField addressField = new TextField();
private final TextField contactNumberField = new TextField();
private final TextField gstNumberField = new TextField();
private final TextField pinCodeField = new TextField();
private final TextField logoPathField = new TextField();

public Pane createHeaderContent() {
    HBox rootLayout = new HBox(30);
    rootLayout.setPadding(new Insets(30));

    VBox formContainer = new VBox(20);
    formContainer.setPrefWidth(600);

    Label title = new Label("🏢 Header Details");
    title.getStyleClass().add("title-label");

    GridPane form = new GridPane();
    form.setHgap(20);
    form.setVgap(10);

    headerIdField.setPromptText("Enter ID (manual)");
    companyNameField.setPromptText("Company Name");
    addressField.setPromptText("Address");
    contactNumberField.setPromptText("Contact Number");
    gstNumberField.setPromptText("GST Number");
    pinCodeField.setPromptText("PIN Code");
    logoPathField.setPromptText("Logo Path");

    form.add(new Label("Header ID:"), 0, 0); form.add(headerIdField, 1, 0);
    form.add(new Label("Company Name:"), 0, 1); form.add(companyNameField, 1, 1);
    form.add(new Label("Address:"), 0, 2); form.add(addressField, 1, 2);
    form.add(new Label("Contact Number:"), 0, 3); form.add(contactNumberField, 1, 3);
    form.add(new Label("GST Number:"), 0, 4); form.add(gstNumberField, 1, 4);
    form.add(new Label("PIN Code:"), 0, 5); form.add(pinCodeField, 1, 5);
    form.add(new Label("Logo Path:"), 0, 6); form.add(logoPathField, 1, 6);

    Button saveBtn = new Button("💾 Save");
    Button updateBtn = new Button("🔄 Update");
    Button clearBtn = new Button("🧹 Clear");

    HBox actions = new HBox(10, saveBtn, updateBtn, clearBtn);
    actions.setPadding(new Insets(10, 0, 0, 0));

    formContainer.getChildren().addAll(title, form, actions);

    VBox savedBox = new VBox(10);
    savedBox.setPrefWidth(300);
    savedBox.setPadding(new Insets(10));
    savedBox.setStyle("-fx-background-color: #f9f9f9; -fx-border-color: #ccc;");

    TextField searchField = new TextField();
    searchField.setPromptText("🔍 Search by company name");

    Button deleteBtn = new Button("🗑️ Delete");
    deleteBtn.setMaxWidth(Double.MAX_VALUE);

    savedBox.getChildren().addAll(
            new Label("📋 Saved Headers"),
            searchField,
            savedHeaderList,
            deleteBtn
    );

    rootLayout.getChildren().addAll(formContainer, savedBox);

    loadHeaders();

    searchField.textProperty().addListener((obs, oldVal, newVal) -> {
        savedHeaderList.getItems().clear();
        headerList.stream()
                .filter(h -> h.getCompanyName().toLowerCase().contains(newVal.toLowerCase()))
                .forEach(h -> savedHeaderList.getItems().add(h.getHeaderId() + " - " + h.getCompanyName()));
    });

    saveBtn.setOnAction(e -> {
        String id = headerIdField.getText();
        if (id.isEmpty() || companyNameField.getText().isEmpty()) {
            showAlert("Validation", "Please enter a valid Header ID and Company Name.");
            return;
        }

        HttpRequest checkReq = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/api/headers/" + id))
                .build();

        HttpClient.newHttpClient().sendAsync(checkReq, HttpResponse.BodyHandlers.ofString())
                .thenApply(response -> {
            int status = response.statusCode();
            return status == 200; 
        })
        .thenAccept(exists -> Platform.runLater(() -> {
            if (exists) {
                showAlert("Duplicate", "Recipe ID already exists.");
            } else {
                saveHeader(id, false);
            }
        }));
    });

    updateBtn.setOnAction(e -> {
        String id = headerIdField.getText();
        if (id.isEmpty()) return;
        saveHeader(id, true);
    });

    deleteBtn.setOnAction(e -> {
        String selected = savedHeaderList.getSelectionModel().getSelectedItem();
        if (selected == null) return;
        String id = selected.split("-")[0].trim();

        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/api/headers/" + id))
                .DELETE()
                .build();

        HttpClient.newHttpClient().sendAsync(req, HttpResponse.BodyHandlers.discarding())
                .thenRun(() -> Platform.runLater(() -> {
                    showAlert("Deleted", "Header deleted.");
                    loadHeaders();
                }));
    });

    clearBtn.setOnAction(e -> clearForm());

    savedHeaderList.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
        if (newVal != null && newVal.contains("-")) {
            String id = newVal.split("-")[0].trim();
            headerList.stream()
                    .filter(h -> h.getHeaderId() == Integer.parseInt(id))
                    .findFirst()
                    .ifPresent(this::populateForm);
        }
    });

    return rootLayout;
}

private void saveHeader(String id, boolean isUpdate) {
    HeaderData h = new HeaderData();
    h.setHeaderId(Integer.parseInt(id));
    h.setCompanyName(companyNameField.getText());
    h.setCompanyAddress(addressField.getText());
    h.setContactNumber(contactNumberField.getText());
    h.setGstNumber(gstNumberField.getText());
    h.setPinCode(pinCodeField.getText());
    h.setLogoPath(logoPathField.getText());

    try {
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(h);

        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/api/headers" + (isUpdate ? "/" + id : "")))
                .header("Content-Type", "application/json")
                .method(isUpdate ? "PUT" : "POST", HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpClient.newHttpClient().sendAsync(req, HttpResponse.BodyHandlers.ofString())
                .thenAccept(resp -> Platform.runLater(() -> {
                    showAlert("Success", isUpdate ? "Updated!" : "Saved!");
                    clearForm();
                    loadHeaders();
                }));
    } catch (Exception e) {
        e.printStackTrace();
    }
}

private void clearForm() {
    headerIdField.clear(); companyNameField.clear(); addressField.clear();
    contactNumberField.clear(); gstNumberField.clear(); pinCodeField.clear(); logoPathField.clear();
}

private void populateForm(HeaderData h) {
    headerIdField.setText(String.valueOf(h.getHeaderId()));
    companyNameField.setText(h.getCompanyName());
    addressField.setText(h.getCompanyAddress());
    contactNumberField.setText(h.getContactNumber());
    gstNumberField.setText(h.getGstNumber());
    pinCodeField.setText(h.getPinCode());
    logoPathField.setText(h.getLogoPath());
}

private void loadHeaders() {
    HttpRequest req = HttpRequest.newBuilder()
            .uri(URI.create("http://localhost:8080/api/headers"))
            .build();

    HttpClient.newHttpClient().sendAsync(req, HttpResponse.BodyHandlers.ofString())
            .thenApply(HttpResponse::body)
            .thenAccept(json -> {
                try {
                    ObjectMapper mapper = new ObjectMapper();
                    List<HeaderData> loaded = mapper.readValue(json, new TypeReference<>() {});
                    headerList.clear();
                    headerList.addAll(loaded);

                    Platform.runLater(() -> {
                        savedHeaderList.getItems().clear();
                        loaded.forEach(h -> savedHeaderList.getItems().add(h.getHeaderId() + " - " + h.getCompanyName()));
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
