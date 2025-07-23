package com.batching.view;

import com.batching.model.CustomerData;
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

public class CustomerDetails {

    private final List<CustomerData> customerList = new ArrayList<>();
    private final ListView<String> savedCustomerList = new ListView<>();
    private final TextField idField = new TextField();
    private final TextField nameField = new TextField();
    private final TextField companyField = new TextField();
    private final TextField addressField = new TextField();
    private final TextField contactField = new TextField();
    private final TextField gstField = new TextField();

    public Pane createCustomerContent() {
        HBox rootLayout = new HBox(30);
        rootLayout.setPadding(new Insets(30));

        VBox formContainer = new VBox(20);
        formContainer.setPrefWidth(400);

        Label title = new Label("\uD83D\uDCCB Customer Details");
        title.getStyleClass().add("title-label");

        GridPane form = new GridPane();
        form.setVgap(15);
        form.setHgap(20);

        idField.setTextFormatter(new TextFormatter<>(change -> change.getControlNewText().matches("\\d{0,3}") ? change : null));
        nameField.setTextFormatter(new TextFormatter<>(change -> change.getControlNewText().length() <= 30 ? change : null));
        companyField.setTextFormatter(new TextFormatter<>(change -> change.getControlNewText().length() <= 30 ? change : null));
        addressField.setTextFormatter(new TextFormatter<>(change -> change.getControlNewText().length() <= 30 ? change : null));
        contactField.setTextFormatter(new TextFormatter<>(change -> change.getControlNewText().matches("\\d{0,12}") ? change : null));
        gstField.setTextFormatter(new TextFormatter<>(change -> change.getControlNewText().length() <= 15 ? change : null));

        form.add(new Label("Customer ID:"), 0, 0); form.add(idField, 1, 0);
        form.add(new Label("Customer Name:"), 0, 1); form.add(nameField, 1, 1);
        form.add(new Label("Company Name:"), 0, 2); form.add(companyField, 1, 2);
        form.add(new Label("Address:"), 0, 3); form.add(addressField, 1, 3);
        form.add(new Label("Contact Info:"), 0, 4); form.add(contactField, 1, 4);
        form.add(new Label("GST Number:"), 0, 5); form.add(gstField, 1, 5);

        Button saveBtn = new Button("\uD83D\uDCBE Save");
        Button updateBtn = new Button("\uD83D\uDD04 Update");
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

        Button deleteBtn = new Button("🗑️ Delete");
        deleteBtn.setMaxWidth(Double.MAX_VALUE);

        savedBox.getChildren().addAll(
        new Label("\uD83D\uDCCB Saved Customers"),
        searchField,
        savedCustomerList,
        deleteBtn
);
        rootLayout.getChildren().addAll(formContainer, savedBox);

        loadCustomers();

        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            savedCustomerList.getItems().clear();
            customerList.stream()
                    .filter(c -> c.getCustomerName().toLowerCase().contains(newVal.toLowerCase()))
                    .forEach(c -> savedCustomerList.getItems().add(c.getCustomerId() + " - " + c.getCustomerName()));
        });

        saveBtn.setOnAction(e -> {
            int id = getId();
            if (id == -1 || nameField.getText().isEmpty()) {
                showAlert("Validation", "Please enter valid ID and Name");
                return;
            }

            HttpRequest checkReq = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/api/customers/check?id=" + id))
                    .build();

            HttpClient.newHttpClient().sendAsync(checkReq, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::body)
                    .thenAccept(body -> Platform.runLater(() -> {
                        if ("true".equalsIgnoreCase(body)) {
                            showAlert("Duplicate", "Customer already exists");
                        } else {
                            saveCustomer(id);
                        }
                    }));
        });

        updateBtn.setOnAction(e -> {
            int id = getId();
            if (id == -1) return;
            saveCustomer(id); // PUT method for update
        });

        deleteBtn.setOnAction(e -> {
    String selected = savedCustomerList.getSelectionModel().getSelectedItem();
    if (selected == null) return;
    int id = Integer.parseInt(selected.split("-")[0].trim());

    HttpRequest req = HttpRequest.newBuilder()
            .uri(URI.create("http://localhost:8080/api/customers/" + id))
            .DELETE()
            .build();

    HttpClient.newHttpClient().sendAsync(req, HttpResponse.BodyHandlers.discarding())
            .thenRun(() -> Platform.runLater(() -> {
                showAlert("Deleted", "Customer deleted");
                loadCustomers();
            }));
});

        clearBtn.setOnAction(e -> clearForm());

        savedCustomerList.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && newVal.contains("-")) {
                int id = Integer.parseInt(newVal.split("-")[0].trim());
                customerList.stream()
                        .filter(c -> c.getCustomerId() == id)
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

    private void populateForm(CustomerData c) {
        idField.setText(String.valueOf(c.getCustomerId()));
        nameField.setText(c.getCustomerName());
        companyField.setText(c.getCompanyName());
        addressField.setText(c.getAddress());
        contactField.setText(c.getContactInfo());
        gstField.setText(c.getGstNumber());
    }

    private void clearForm() {
        idField.clear(); nameField.clear(); companyField.clear();
        addressField.clear(); contactField.clear(); gstField.clear();
    }

    private void saveCustomer(int id) {
        CustomerData c = new CustomerData();
        c.setCustomerId(id);
        c.setCustomerName(nameField.getText());
        c.setCompanyName(companyField.getText());
        c.setAddress(addressField.getText());
        c.setContactInfo(contactField.getText());
        c.setGstNumber(gstField.getText());

        ObjectMapper mapper = new ObjectMapper();
        try {
            String json = mapper.writeValueAsString(c);
            boolean isUpdate = customerList.stream().anyMatch(x -> x.getCustomerId() == id);
            HttpRequest req = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/api/customers" + (isUpdate ? "/" + id : "")))
                    .header("Content-Type", "application/json")
                    .method(isUpdate ? "PUT" : "POST", HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpClient.newHttpClient().sendAsync(req, HttpResponse.BodyHandlers.ofString())
                    .thenAccept(resp -> Platform.runLater(() -> {
                        showAlert("Success", isUpdate ? "Updated!" : "Saved!");
                        clearForm();
                        loadCustomers();
                    }));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadCustomers() {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/api/customers"))
                .build();

        HttpClient.newHttpClient().sendAsync(req, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(json -> {
                    ObjectMapper mapper = new ObjectMapper();
                    try {
                        List<CustomerData> loaded = mapper.readValue(json, new TypeReference<>() {});
                        customerList.clear();
                        customerList.addAll(loaded);
                        Platform.runLater(() -> {
                            savedCustomerList.getItems().clear();
                            loaded.forEach(c -> savedCustomerList.getItems().add(c.getCustomerId() + " - " + c.getCustomerName()));
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
