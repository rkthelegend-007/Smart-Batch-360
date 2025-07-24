package com.batching.view;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import com.batching.model.CycleData;
import com.batching.model.RecipeData;
//import com.batching.model.RecipeData;
import com.batching.view.export.ExportAsPdf;
import com.batching.view.layout.HeaderBar;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class CycleDataTable {

    private List<CycleData> dataList;
    private TableView<CycleData> table;
    final BooleanProperty isDuplicate = new SimpleBooleanProperty(false);

    public void showTable(Stage stage) {
        Label title = new Label("SmartBatch 360");
        title.getStyleClass().add("title-label");

        // 🔷 TabPane for Batching / Production / Consumption views
        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        // Tabs
        Tab batchingTab = new Tab("Batching", createBatchingContent(stage));
        Tab productionTab = new Tab("Production", createProductionContent());
        Tab consumptionTab = new Tab("Consumption", new Label("📊 Consumption data view coming soon..."));

        tabPane.getTabs().addAll(batchingTab, productionTab, consumptionTab);

        VBox root = new VBox(15, title, tabPane);
        root.setPadding(new Insets(20));

        Scene scene = new Scene(root, 1280, 700);
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());

        stage.setTitle("SmartBatch 360");
        stage.setScene(scene);
        stage.show();
    }

    private Pane createBatchingContent(Stage stage) {
        table = new TableView<>();

        // Define all columns
        TableColumn<CycleData, Integer> srNoCol = new TableColumn<>("Sr No");
        srNoCol.setCellValueFactory(new PropertyValueFactory<>("srNo"));
        table.getColumns().add(srNoCol);

        TableColumn<CycleData, String> batchCol = new TableColumn<>("Batch Number");
        batchCol.setCellValueFactory(new PropertyValueFactory<>("batchNumber"));
        table.getColumns().add(batchCol);

        for (int i = 1; i <= 20; i++) {
            TableColumn<CycleData, String> col = new TableColumn<>("Material " + i);
            col.setCellValueFactory(new PropertyValueFactory<>("material" + i));
            table.getColumns().add(col);
        }

        // Left header panel
        HeaderBar headerBar = new HeaderBar();
        headerBar.setPrefWidth(350);
        headerBar.setMinWidth(350);
        headerBar.setStyle("-fx-background-color: #f4f4f4; -fx-padding: 15; -fx-border-color: #cccccc; -fx-border-width: 0 1 0 0;");

        headerBar.setOnViewClicked((fromBatch, toBatch) -> {
            if (fromBatch == null || fromBatch.isEmpty()) {
                showAlert("Validation", "Please select a valid 'From Batch' number.");
                return;
            }
            loadDataByBatch(fromBatch); // Future: handle toBatch too
        });

        headerBar.setOnPrintClicked(() -> {
            if (dataList == null || dataList.isEmpty()) {
                showAlert("No Data", "Please load a report before exporting.");
                return;
            }
            ExportAsPdf.showPreviewAndExport(dataList, stage);
        });

        HBox layout = new HBox(headerBar, table);
        HBox.setHgrow(table, Priority.ALWAYS); // Let table expand

        return layout;
    }

    private Pane createProductionContent() {
        TabPane productionTabs = new TabPane();
        productionTabs.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        Tab recipeTab = new Tab("Recipe", createRecipeContent());
        Tab customerDetailsTab = new Tab("Customer Details", new CustomerDetails().createCustomerContent());
        Tab createSiteContent = new Tab("Site Details", new SiteDetails().createSiteContent());
        Tab vehicleTab = new Tab("Vehicle Details", new VehicleDetails().createVehicleContent());
        Tab createDriverContent = new Tab("Driver Details", new DriverDetails().createDriverContent());
        Tab createHeaderContent = new Tab("Header Details", new HeaderDetails().createHeaderContent());
        productionTabs.getTabs().addAll(recipeTab, customerDetailsTab,createSiteContent,vehicleTab,createDriverContent,createHeaderContent);

        VBox wrapper = new VBox(productionTabs);
        wrapper.setPadding(new Insets(10));
        return wrapper;
    }
    
    // paste recipe tab content here 

    private final List<RecipeData> recipeList = new ArrayList<>();
private final ListView<String> savedRecipeList = new ListView<>();

private final TextField recipeIdField = new TextField();
private final TextField recipeNameField = new TextField();
private final TextField recipeTotalField = new TextField();
private final TextField timestampField = new TextField();

private final List<TextField> materialFields = new ArrayList<>();
private final List<TextField> setpointFields = new ArrayList<>();

public Pane createRecipeContent() {
    HBox rootLayout = new HBox(30);
    rootLayout.setPadding(new Insets(30));

    VBox formContainer = new VBox(20);
    formContainer.setPrefWidth(600);

    Label title = new Label("🧪 Recipe Details");
    title.getStyleClass().add("title-label");

    GridPane topForm = new GridPane();
    topForm.setHgap(20);
    topForm.setVgap(10);

    recipeIdField.setTextFormatter(new TextFormatter<>(c -> c.getControlNewText().matches("\\d{0,4}") ? c : null));
    recipeNameField.setTextFormatter(new TextFormatter<>(c -> c.getControlNewText().length() <= 20 ? c : null));

    recipeIdField.setPromptText("Enter ID (1-99)");
    recipeNameField.setPromptText("Max 20 chars");
    recipeTotalField.setPromptText("Auto Total");
    timestampField.setPromptText("Auto Timestamp");
    timestampField.setEditable(false);

    topForm.add(new Label("Recipe ID:"), 0, 0);     topForm.add(recipeIdField, 1, 0);
    topForm.add(new Label("Recipe Name:"), 2, 0);   topForm.add(recipeNameField, 3, 0);
    topForm.add(new Label("Total:"), 0, 1);         topForm.add(recipeTotalField, 1, 1);
    topForm.add(new Label("Timestamp:"), 2, 1);     topForm.add(timestampField, 3, 1);

    GridPane materialGrid = new GridPane();
    materialGrid.setHgap(15);
    materialGrid.setVgap(8);

    for (int i = 0; i < 20; i++) {
        Label label = new Label((i + 1) + ".");
        label.setMinWidth(25);
        label.setStyle("-fx-alignment: CENTER_RIGHT;");

        TextField materialField = new TextField();
        materialField.setPromptText("Material " + (i + 1));
        materialField.setTextFormatter(new TextFormatter<>(c -> c.getControlNewText().length() <= 10 ? c : null));

        TextField setpointField = new TextField();
        setpointField.setPromptText("Setpoint");
        setpointField.setTextFormatter(new TextFormatter<>(c -> c.getControlNewText().matches("\\d{0,4}(\\.\\d{0,2})?") ? c : null));
        setpointField.textProperty().addListener((obs, oldVal, newVal) -> calculateTotal());

        materialFields.add(materialField);
        setpointFields.add(setpointField);

        if (i < 10) {
            materialGrid.add(label, 0, i);
            materialGrid.add(materialField, 1, i);
            materialGrid.add(setpointField, 2, i);
        } else {
            materialGrid.add(label, 3, i - 10);
            materialGrid.add(materialField, 4, i - 10);
            materialGrid.add(setpointField, 5, i - 10);
        }
    }

    Button saveBtn = new Button("💾 Save");
    Button updateBtn = new Button("🔄 Update");
    Button clearBtn = new Button("🧹 Clear");

    HBox actions = new HBox(10, saveBtn, updateBtn, clearBtn);
    actions.setPadding(new Insets(10, 0, 0, 0));

    formContainer.getChildren().addAll(title, topForm, materialGrid, actions);

    VBox savedBox = new VBox(10);
    savedBox.setPrefWidth(300);
    savedBox.setPadding(new Insets(10));
    savedBox.setStyle("-fx-background-color: #f9f9f9; -fx-border-color: #ccc;");

    TextField searchField = new TextField();
    searchField.setPromptText("🔍 Search by name");

    Button deleteBtn = new Button("🗑️ Delete");
    deleteBtn.setMaxWidth(Double.MAX_VALUE);

    savedBox.getChildren().addAll(
            new Label("📋 Saved Recipes"),
            searchField,
            savedRecipeList,
            deleteBtn
    );

    rootLayout.getChildren().addAll(formContainer, savedBox);

    loadRecipes();

    searchField.textProperty().addListener((obs, oldVal, newVal) -> {
        savedRecipeList.getItems().clear();
        recipeList.stream()
                .filter(r -> r.getRecipeName().toLowerCase().contains(newVal.toLowerCase()))
                .forEach(r -> savedRecipeList.getItems().add(r.getRecipeId() + " - " + r.getRecipeName()));
    });

    saveBtn.setOnAction(e -> {
        int id = getRecipeId();
        if (id == -1 || recipeNameField.getText().isEmpty()) {
            showAlert("Validation", "Please enter a valid Recipe ID and Name.");
            return;
        }

        HttpRequest checkReq = HttpRequest.newBuilder()
        .uri(URI.create("http://localhost:8080/api/recipes/" + id))
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
                saveRecipe(id, false);
            }
        }));

    });

    updateBtn.setOnAction(e -> {
        int id = getRecipeId();
        if (id == -1) return;
        saveRecipe(id, true);
    });

    deleteBtn.setOnAction(e -> {
        String selected = savedRecipeList.getSelectionModel().getSelectedItem();
        if (selected == null) return;
        int id = Integer.parseInt(selected.split("-")[0].trim());

        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/api/recipes/" + id))
                .DELETE()
                .build();

        HttpClient.newHttpClient().sendAsync(req, HttpResponse.BodyHandlers.discarding())
                .thenRun(() -> Platform.runLater(() -> {
                    showAlert("Deleted", "Recipe deleted.");
                    clearForm();
                    loadRecipes();
                }));
    });

    clearBtn.setOnAction(e -> clearForm());

    savedRecipeList.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
        if (newVal != null && newVal.contains("-")) {
            int id = Integer.parseInt(newVal.split("-")[0].trim());
            recipeList.stream()
                    .filter(r -> r.getRecipeId() == id)
                    .findFirst()
                    .ifPresent(this::populateForm);
        }
    });

    return rootLayout;
}

private int getRecipeId() {
    try {
        return Integer.parseInt(recipeIdField.getText());
    } catch (NumberFormatException e) {
        return -1;
    }
}

private void populateForm(RecipeData r) {
    recipeIdField.setText(String.valueOf(r.getRecipeId()));
    recipeNameField.setText(r.getRecipeName());
    recipeTotalField.setText(r.getRecipeTotal());
    timestampField.setText(r.getTimestamp());

    for (int i = 0; i < 20; i++) {
        materialFields.get(i).setText(getField(r, "getMaterial" + (i + 1)));
        setpointFields.get(i).setText(getField(r, "getSetpoint" + (i + 1)));
    }
}

private String getField(RecipeData r, String methodName) {
    try {
        return (String) RecipeData.class.getMethod(methodName).invoke(r);
    } catch (Exception e) {
        return "";
    }
}

private void clearForm() {
    recipeIdField.clear(); recipeNameField.clear(); recipeTotalField.clear(); timestampField.clear();
    materialFields.forEach(TextField::clear);
    setpointFields.forEach(TextField::clear);
}

private void saveRecipe(int id, boolean isUpdate) {
    RecipeData r = new RecipeData();
    r.setRecipeId(id);
    r.setRecipeName(recipeNameField.getText());
    r.setRecipeTotal(recipeTotalField.getText());

    String timestamp = (isUpdate ? "U-" : "S-") +
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy_HH_mm_ss"));
    r.setTimestamp(timestamp);
    timestampField.setText(timestamp);

    for (int i = 0; i < 20; i++) {
        setField(r, "setMaterial" + (i + 1), materialFields.get(i).getText());
        setField(r, "setSetpoint" + (i + 1), setpointFields.get(i).getText());
    }

    try {
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(r);

        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/api/recipes" + (isUpdate ? "/" + id : "")))
                .header("Content-Type", "application/json")
                .method(isUpdate ? "PUT" : "POST", HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpClient.newHttpClient().sendAsync(req, HttpResponse.BodyHandlers.ofString())
                .thenAccept(resp -> Platform.runLater(() -> {
                    showAlert("Success", isUpdate ? "Updated!" : "Saved!");
                    clearForm();
                    loadRecipes();
                }));
    } catch (Exception e) {
        e.printStackTrace();
    }
}

private void setField(RecipeData r, String methodName, String value) {
    try {
        RecipeData.class.getMethod(methodName, String.class).invoke(r, value);
    } catch (Exception e) {
        e.printStackTrace();
    }
}

private void loadRecipes() {
    HttpRequest req = HttpRequest.newBuilder()
            .uri(URI.create("http://localhost:8080/api/recipes"))
            .build();

    HttpClient.newHttpClient().sendAsync(req, HttpResponse.BodyHandlers.ofString())
            .thenApply(HttpResponse::body)
            .thenAccept(json -> {
                try {
                    ObjectMapper mapper = new ObjectMapper();
                    List<RecipeData> loaded = mapper.readValue(json, new TypeReference<>() {});
                    recipeList.clear();
                    recipeList.addAll(loaded);

                    Platform.runLater(() -> {
                        savedRecipeList.getItems().clear();
                        loaded.forEach(r -> savedRecipeList.getItems().add(r.getRecipeId() + " - " + r.getRecipeName()));
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

private void calculateTotal() {
    double total = 0;
    for (TextField sp : setpointFields) {
        try {
            total += Double.parseDouble(sp.getText());
        } catch (NumberFormatException ignored) {
        }
    }
    recipeTotalField.setText(String.format("%.2f", total));
}


    //end of recipe tab content 
    private void loadDataByBatch(String batchNumber) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/api/cycle/by-batch/" + batchNumber))
                    .build();

            HttpClient.newHttpClient().sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::body)
                    .thenAccept(json -> {
                        try {
                            ObjectMapper mapper = new ObjectMapper();
                            dataList = mapper.readValue(json, new TypeReference<>() {});
                            Platform.runLater(() -> table.getItems().setAll(dataList));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // Moved Recipe UI to its own class (RecipeView) for separation
    public static class MaterialRow {
        private final SimpleStringProperty materialName;
        private final SimpleStringProperty setpoint;

        public MaterialRow(String materialName, String setpoint) {
            this.materialName = new SimpleStringProperty(materialName);
            this.setpoint = new SimpleStringProperty(setpoint);
        }

        public String getMaterialName() { return materialName.get(); }
        public void setMaterialName(String value) { materialName.set(value); }
        public SimpleStringProperty materialNameProperty() { return materialName; }

        public String getSetpoint() { return setpoint.get(); }
        public void setSetpoint(String value) { setpoint.set(value); }
        public SimpleStringProperty setpointProperty() { return setpoint; }
    }
}
