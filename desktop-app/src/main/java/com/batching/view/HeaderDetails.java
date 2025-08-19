package com.batching.view;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import com.batching.model.HeaderData;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class HeaderDetails {

    private final List<HeaderData> headerList = new ArrayList<>();
    private final ListView<String> savedHeaderList = new ListView<>();
    private final TextField headerIdField = new TextField();
    private final TextField companyNameField = new TextField();
    private final TextField addressField = new TextField();
    private final TextField contactNumberField = new TextField();
    private final TextField gstNumberField = new TextField();
    private final TextField pinCodeField = new TextField();
    private final Label logoPathDisplayLabel = new Label("No file selected"); // 🔵 replaced textfield with label
    private final ImageView logoPreview = new ImageView();

    private String logoFilePath = "";
    private String lastSelectedItem = null; // 🟢 Track last selected

    public Pane createHeaderContent() {
        HBox rootLayout = new HBox(30);
        rootLayout.setPadding(new Insets(30));

        VBox formContainer = new VBox(20);
        formContainer.setPrefWidth(600);

        Label title = new Label("\uD83C\uDFE2 Company Details");
        title.getStyleClass().add("title-label");

        GridPane form = new GridPane();
        form.setHgap(20);
        form.setVgap(10);

        headerIdField.setTextFormatter(new TextFormatter<>(change ->
            change.getControlNewText().matches("\\d{0,3}") ? change : null));

        companyNameField.setTextFormatter(new TextFormatter<>(change ->
            change.getControlNewText().length() <= 30 ? change : null));

        addressField.setTextFormatter(new TextFormatter<>(change ->
            change.getControlNewText().length() <= 30 ? change : null));

        contactNumberField.setTextFormatter(new TextFormatter<>(change ->
            change.getControlNewText().matches("\\d{0,10}") ? change : null));

        gstNumberField.setTextFormatter(new TextFormatter<>(change ->
            change.getControlNewText().length() <= 15 ? change : null));

        pinCodeField.setTextFormatter(new TextFormatter<>(change ->
            change.getControlNewText().matches("\\d{0,6}") ? change : null));

        Button browseLogoBtn = new Button("\uD83D\uDCC2 Browse");
        browseLogoBtn.setOnAction(e -> browseLogo());

        form.add(new Label("Company ID:"), 0, 0); form.add(headerIdField, 1, 0);
        form.add(new Label("Company Name:"), 0, 1); form.add(companyNameField, 1, 1);
        form.add(new Label("Address:"), 0, 2); form.add(addressField, 1, 2);
        form.add(new Label("Contact Number:"), 0, 3); form.add(contactNumberField, 1, 3);
        form.add(new Label("GST Number:"), 0, 4); form.add(gstNumberField, 1, 4);
        form.add(new Label("PIN Code:"), 0, 5); form.add(pinCodeField, 1, 5);
        form.add(new Label("Logo Path:"), 0, 6);

        HBox logoBox = new HBox(10, logoPathDisplayLabel, browseLogoBtn); // 🟢 Only filename shown
        form.add(logoBox, 1, 6);

        logoPreview.setFitHeight(80);
        logoPreview.setFitWidth(100);
        logoPreview.setPreserveRatio(true);
        form.add(new Label("Preview:"), 0, 7);
        form.add(logoPreview, 1, 7);

        Button saveBtn = new Button("\uD83D\uDCBE Save");
        Button updateBtn = new Button("\uD83D\uDD04 Update");
        Button clearBtn = new Button("\uD83E\uDDF9 Clear");

        HBox actions = new HBox(10, saveBtn, updateBtn, clearBtn);
        actions.setPadding(new Insets(10, 0, 0, 0));

        formContainer.getChildren().addAll(title, form, actions);

        VBox savedBox = new VBox(10);
        savedBox.setPrefWidth(300);
        savedBox.setPadding(new Insets(10));
        savedBox.setStyle("-fx-background-color: #f9f9f9; -fx-border-color: #ccc;");

        TextField searchField = new TextField();
        searchField.setPromptText("\uD83D\uDD0D Search by company name");

        Button deleteBtn = new Button("\uD83D\uDDD1️ Delete");
        deleteBtn.setMaxWidth(Double.MAX_VALUE);

        savedBox.getChildren().addAll(new Label("\uD83D\uDCCB Saved Headers"), searchField, savedHeaderList, deleteBtn);

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
                    .thenApply(response -> response.statusCode() == 200)
                    .thenAccept(exists -> Platform.runLater(() -> {
                        if (exists) {
                            showAlert("Duplicate", "Header ID already exists.");
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
                        clearForm();
                        loadHeaders();
                    }));
        });

        clearBtn.setOnAction(e -> {
            clearForm();
            savedHeaderList.getSelectionModel().clearSelection(); // 🟢 Deselect
            lastSelectedItem = null;
        });


        savedHeaderList.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && newVal.contains("-")) {
                String id = newVal.split("-")[0].trim();
                headerList.stream()
                        .filter(h -> h.getHeaderId() == Integer.parseInt(id))
                        .findFirst()
                        .ifPresent(this::populateForm);
                        lastSelectedItem = newVal; // 🟢 Update tracker
            }
        });

        return rootLayout;
    }

    private void browseLogo() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Logo Image");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));

        File selectedFile = fileChooser.showOpenDialog(new Stage());
        if (selectedFile != null) {
            if (!selectedFile.getName().toLowerCase().matches(".*\\.(png|jpg|jpeg)$")) {
                showAlert("Invalid File", "Only PNG, JPG, or JPEG images are allowed.");
                return;
            }

            if (selectedFile.length() > 2 * 1024 * 1024) {
                showAlert("File Too Large", "Logo image must be less than or equal to 2MB.");
                return;
            }

            try {
                Path destDir = Paths.get("logos");
                Files.createDirectories(destDir);
                Path destination = destDir.resolve(selectedFile.getName());
                Files.copy(selectedFile.toPath(), destination, StandardCopyOption.REPLACE_EXISTING);
                logoFilePath = destination.toString(); // 🔵 Save full path for backend
                logoPathDisplayLabel.setText(destination.getFileName().toString()); // 🔵 Show only file name
                logoPreview.setImage(new Image(destination.toUri().toString()));
            } catch (IOException ex) {
                ex.printStackTrace();
                showAlert("Error", "Failed to copy logo file.");
            }
        }
    }

    private void saveHeader(String id, boolean isUpdate) {
        HeaderData h = new HeaderData();
        h.setHeaderId(Integer.parseInt(id));
        h.setCompanyName(companyNameField.getText());
        h.setCompanyAddress(addressField.getText());
        h.setContactNumber(contactNumberField.getText());
        h.setGstNumber(gstNumberField.getText());
        h.setPinCode(pinCodeField.getText());
        h.setLogoPath(logoFilePath); // 🔵 Send full path to backend

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
        headerIdField.clear();
        companyNameField.clear();
        addressField.clear();
        contactNumberField.clear();
        gstNumberField.clear();
        pinCodeField.clear();
        logoFilePath = "";
        logoPathDisplayLabel.setText("No file selected");
        logoPreview.setImage(null);
    }

    private void populateForm(HeaderData h) {
        headerIdField.setText(String.valueOf(h.getHeaderId()));
        companyNameField.setText(h.getCompanyName());
        addressField.setText(h.getCompanyAddress());
        contactNumberField.setText(h.getContactNumber());
        gstNumberField.setText(h.getGstNumber());
        pinCodeField.setText(h.getPinCode());
        logoFilePath = h.getLogoPath();
        logoPathDisplayLabel.setText(logoFilePath != null ? Path.of(logoFilePath).getFileName().toString() : "No file selected");
        if (logoFilePath != null && !logoFilePath.isEmpty()) {
            File logoFile = new File(logoFilePath);
            if (logoFile.exists()) {
                logoPreview.setImage(new Image(logoFile.toURI().toString()));
            }
        }
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
