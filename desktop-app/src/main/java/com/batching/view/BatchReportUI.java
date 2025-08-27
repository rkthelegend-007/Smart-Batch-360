package com.batching.view;

import com.batching.model.BatchData;
import com.batching.view.export.ExportBatchReport;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BatchReportUI {

    private Stage primaryStage;
    private VBox previewBox;
    private ComboBox<Double> batchComboSingle;
    private ComboBox<Double> batchComboStart;
    private ComboBox<Double> batchComboEnd;
    private DatePicker startDatePicker;
    private DatePicker endDatePicker;
    private CheckBox rangeCheckbox;

    private RadioButton byBatchRadio;
    private RadioButton byDateRadio;

    private PDDocument currentPdfDocument;
    private List<String> selectedMaterials = new ArrayList<>();

    public BorderPane getView(Stage stage) {
        this.primaryStage = stage;

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        VBox leftPane = new VBox(10);
        leftPane.setPadding(new Insets(10));
        leftPane.setPrefWidth(250);

        // --- Instantiate all UI components first to prevent NullPointerException ---
        ToggleGroup filterToggleGroup = new ToggleGroup();
        byBatchRadio = new RadioButton("By Batch Number");
        byBatchRadio.setToggleGroup(filterToggleGroup);
        byBatchRadio.setSelected(true);
        byDateRadio = new RadioButton("By Date Range");
        byDateRadio.setToggleGroup(filterToggleGroup);

        batchComboSingle = new ComboBox<>();
        batchComboSingle.setPromptText("Select Single Batch Number");
        batchComboSingle.setEditable(false);
        batchComboSingle.setVisibleRowCount(5); // Set visible rows

        batchComboStart = new ComboBox<>();
        batchComboStart.setPromptText("Batch Start");
        batchComboStart.setEditable(false);
        batchComboStart.setVisibleRowCount(5); // Set visible rows

        batchComboEnd = new ComboBox<>();
        batchComboEnd.setPromptText("Batch End");
        batchComboEnd.setEditable(false);
        batchComboEnd.setVisibleRowCount(5); // Set visible rows

        rangeCheckbox = new CheckBox("Select Batch Range");

        startDatePicker = new DatePicker();
        startDatePicker.setPromptText("Start Date");
        endDatePicker = new DatePicker();
        endDatePicker.setPromptText("End Date");

        Button viewBtn = new Button("View Data");
        Button generateBtn = new Button("Generate PDF");
        Button previewBtn = new Button("Preview PDF");
        Button selectMaterialsBtn = new Button("Select Materials");

        // --- Add components to the layout and set up their properties ---
        leftPane.getChildren().addAll(
                new Label("Filter By:"), byBatchRadio, byDateRadio,
                new Label("Batch:"), rangeCheckbox,
                batchComboSingle,
                batchComboStart,
                batchComboEnd,
                new Label("Date Range:"), startDatePicker,
                endDatePicker,
                selectMaterialsBtn,
                viewBtn, generateBtn, previewBtn
        );

        // --- Bindings for visibility and management ---
        batchComboStart.visibleProperty().bind(rangeCheckbox.selectedProperty());
        batchComboStart.managedProperty().bind(rangeCheckbox.selectedProperty());
        batchComboEnd.visibleProperty().bind(rangeCheckbox.selectedProperty());
        batchComboEnd.managedProperty().bind(rangeCheckbox.selectedProperty());
        
        // Correcting the manual visibility setting for batchComboSingle
        batchComboSingle.visibleProperty().bind(rangeCheckbox.selectedProperty().not());
        batchComboSingle.managedProperty().bind(rangeCheckbox.selectedProperty().not());

        // --- Listeners for enabling/disabling controls and resetting preview ---
        byBatchRadio.selectedProperty().addListener((obs, oldVal, newVal) -> {
            boolean isBatchSelected = newVal;
            batchComboSingle.setDisable(!isBatchSelected);
            batchComboStart.setDisable(!isBatchSelected);
            batchComboEnd.setDisable(!isBatchSelected);
            rangeCheckbox.setDisable(!isBatchSelected);
            if (isBatchSelected) {
                startDatePicker.setValue(null);
                endDatePicker.setValue(null);
            }
            // Reset the PDF preview when the user changes the filter type
            previewBox.getChildren().clear();
            closeCurrentPdfDocument();
        });

        byDateRadio.selectedProperty().addListener((obs, oldVal, newVal) -> {
            boolean isDateSelected = newVal;
            startDatePicker.setDisable(!isDateSelected);
            endDatePicker.setDisable(!isDateSelected);
            if (isDateSelected) {
                batchComboSingle.setValue(null);
                batchComboStart.setValue(null);
                batchComboEnd.setValue(null);
            }
            // Reset the PDF preview when the user changes the filter type
            previewBox.getChildren().clear();
            closeCurrentPdfDocument();
        });

        // Set initial state
        startDatePicker.setDisable(true);
        endDatePicker.setDisable(true);
        
        previewBox = new VBox(10);
        previewBox.setPadding(new Insets(10));
        ScrollPane pdfScroll = new ScrollPane(previewBox);
        pdfScroll.setFitToWidth(true);

        SplitPane splitPane = new SplitPane();
        splitPane.getItems().addAll(leftPane, pdfScroll);
        splitPane.setDividerPositions(0.25);

        root.setCenter(splitPane);

        selectedMaterials = loadSelectionsFromFile();

        // This listener now only handles logic and no longer sets visibility
        rangeCheckbox.selectedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                batchComboSingle.setValue(null);
            } else {
                batchComboStart.setValue(null);
                batchComboEnd.setValue(null);
            }
        });

        batchComboSingle.setOnShowing(e -> {
            batchComboSingle.getItems().clear();
            loadBatchNumbers(batchComboSingle);
        });
        batchComboStart.setOnShowing(e -> {
            batchComboStart.getItems().clear();
            loadBatchNumbers(batchComboStart);
        });
        batchComboEnd.setOnShowing(e -> {
            batchComboEnd.getItems().clear();
            loadBatchNumbers(batchComboEnd);
        });

        batchComboSingle.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal == null && oldVal != null) Platform.runLater(() -> batchComboSingle.setValue(oldVal));
        });
        batchComboStart.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal == null && oldVal != null) Platform.runLater(() -> batchComboStart.setValue(oldVal));
        });
        batchComboEnd.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal == null && oldVal != null) Platform.runLater(() -> batchComboEnd.setValue(oldVal));
        });

        selectMaterialsBtn.setOnAction(e -> openMaterialSelectionDialog());

        viewBtn.setOnAction(e -> fetchDataBasedOnSelection(true));
        generateBtn.setOnAction(e -> fetchDataBasedOnSelection(false));
        previewBtn.setOnAction(e -> {
            closeCurrentPdfDocument();
            fetchDataBasedOnSelection(true);
        });

        primaryStage.setOnCloseRequest(e -> closeCurrentPdfDocument());

        return root;
    }

    private void fetchDataBasedOnSelection(boolean previewOnly) {
        Double batchNumber = null;
        Double batchStart = null;
        Double batchEnd = null;
        LocalDate startDate = null;
        LocalDate endDate = null;

        if (byBatchRadio.isSelected()) {
            if (rangeCheckbox.isSelected()) {
                batchStart = batchComboStart.getValue();
                batchEnd = batchComboEnd.getValue();
                if (batchStart == null || batchEnd == null) {
                    showAlert("Input Required", "Please select a valid batch number range.");
                    return;
                }
                if (batchStart > batchEnd) {
                    showAlert("Invalid Range", "The start batch number cannot be greater than the end batch number.");
                    return;
                }
            } else {
                batchNumber = batchComboSingle.getValue();
                if (batchNumber == null) {
                    showAlert("Input Required", "Please select a single batch number.");
                    return;
                }
            }
        } else if (byDateRadio.isSelected()) {
            startDate = startDatePicker.getValue();
            endDate = endDatePicker.getValue();
            if (startDate == null && endDate == null) {
                showAlert("Input Required", "Please select a start date and/or an end date.");
                return;
            }
            if (startDate != null && endDate != null && startDate.isAfter(endDate)) {
                showAlert("Invalid Date Range", "The start date cannot be after the end date.");
                return;
            }
        } else {
            showAlert("Input Required", "Please select a filtering method (By Batch or By Date).");
            return;
        }

        fetchData(batchNumber, batchStart, batchEnd, startDate, endDate, previewOnly);
    }
    
    private void fetchData(Double batchNumber, Double batchStart, Double batchEnd, LocalDate startDate, LocalDate endDate, boolean previewOnly) {
    try {
        StringBuilder urlBuilder = new StringBuilder("http://localhost:8080/api/batch-data/filter?");
        boolean hasParameters = false;

        // Check the batch number parameters passed from fetchDataBasedOnSelection
        if (batchNumber != null) {
            urlBuilder.append("batchNumber=").append(batchNumber);
            hasParameters = true;
        } else if (batchStart != null && batchEnd != null) {
            urlBuilder.append("batchStart=").append(batchStart).append("&batchEnd=").append(batchEnd);
            hasParameters = true;
        }

        // Check the date parameters passed from fetchDataBasedOnSelection
        if (startDate != null) {
            if (hasParameters) urlBuilder.append("&");
            urlBuilder.append("startDate=").append(startDate);
            hasParameters = true;
        }
        if (endDate != null) {
            if (hasParameters) urlBuilder.append("&");
            urlBuilder.append("endDate=").append(endDate);
            hasParameters = true;
        }

        if (!hasParameters) {
            Platform.runLater(() -> showAlert("Input Required", "Please select a batch number or a date range."));
            return;
        }

        String url = urlBuilder.toString();

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(responseBody -> {
                    try {
                        ObjectMapper mapper = new ObjectMapper();
                        mapper.registerModule(new JavaTimeModule());

                        List<BatchData> list = mapper.readValue(responseBody, new TypeReference<List<BatchData>>() {});

                        if (!list.isEmpty()) {
                            Platform.runLater(() -> {
                                try {
                                    boolean isRangeSearch = (batchStart != null && batchEnd != null) || (startDate != null || endDate != null);
                                    
                                    if (isRangeSearch || list.size() > 1) {
                                        generateConsolidatedPdf(list, previewOnly);
                                    } else {
                                        File file;
                                        if (previewOnly) {
                                            file = File.createTempFile("batch_preview_", ".pdf");
                                            file.deleteOnExit();
                                        } else {
                                            FileChooser fileChooser = new FileChooser();
                                            fileChooser.setTitle("Save Batch Report");
                                            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
                                            file = fileChooser.showSaveDialog(primaryStage);
                                            if (file == null) return;
                                        }

                                        List<String> finalSelectedMaterials = selectedMaterials.isEmpty()
                                                ? getAllMaterialsFromHeader(list.get(0))
                                                : new ArrayList<>(selectedMaterials);

                                        ExportBatchReport.generateReport(list, file.getAbsolutePath(), finalSelectedMaterials);
                                        if (previewOnly) {
                                            previewPdf(file.getAbsolutePath());
                                        } else {
                                            showAlert("Success", "PDF generated successfully at: " + file.getAbsolutePath());
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    showAlert("Error", "PDF generation failed: " + e.getMessage());
                                }
                            });
                        } else {
                            Platform.runLater(() -> showAlert("No Data", "No batch data found for the given filter."));
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        Platform.runLater(() -> showAlert("Error", "Failed to parse data: " + ex.getMessage()));
                    }
                })
                .exceptionally(ex -> {
                    Platform.runLater(() -> showAlert("Error", "Failed to fetch data from API: " + ex.getMessage()));
                    return null;
                });
    } catch (Exception ex) {
        ex.printStackTrace();
        Platform.runLater(() -> showAlert("Error", "Failed to construct API request: " + ex.getMessage()));
    }
}

    private Map<Double, List<BatchData>> groupDataByBatch(List<BatchData> allData) {
        return allData.stream()
                .collect(Collectors.groupingBy(BatchData::getBatchNumber));
    }

    private void generateConsolidatedPdf(List<BatchData> batchDataList, boolean previewOnly) throws IOException {
        File finalFile;
        List<File> tempFiles = new ArrayList<>();
        try {
            if (previewOnly) {
                finalFile = File.createTempFile("consolidated_preview_", ".pdf");
                finalFile.deleteOnExit();
            } else {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Save Consolidated Batch Report");
                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
                finalFile = fileChooser.showSaveDialog(primaryStage);
                if (finalFile == null) return;
            }

            PDFMergerUtility merger = new PDFMergerUtility();
            merger.setDestinationFileName(finalFile.getAbsolutePath());

            Map<Double, List<BatchData>> groupedData = groupDataByBatch(batchDataList);

            List<Double> sortedBatchNumbers = new ArrayList<>(groupedData.keySet());
            sortedBatchNumbers.sort(Comparator.naturalOrder());

            for (Double batchNumber : sortedBatchNumbers) {
                List<BatchData> singleBatchData = groupedData.get(batchNumber);
                File tempFile = File.createTempFile("temp_batch_" + batchNumber, ".pdf");
                tempFiles.add(tempFile);

                List<String> finalSelectedMaterials = selectedMaterials.isEmpty()
                        ? getAllMaterialsFromHeader(singleBatchData.get(0))
                        : new ArrayList<>(selectedMaterials);

                ExportBatchReport.generateReport(singleBatchData, tempFile.getAbsolutePath(), finalSelectedMaterials);
                merger.addSource(tempFile);
            }

            merger.mergeDocuments(null);

            if (previewOnly) {
                previewPdf(finalFile.getAbsolutePath());
            } else {
                showAlert("Success", "Consolidated PDF generated successfully at: " + finalFile.getAbsolutePath());
            }

        } finally {
            for (File file : tempFiles) {
                file.delete();
            }
        }
    }

    private void loadBatchNumbers(ComboBox<Double> combo) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/api/batch-data/numbers"))
                    .GET()
                    .build();

            client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::body)
                    .thenAccept(responseBody -> {
                        try {
                            ObjectMapper mapper = new ObjectMapper();
                            mapper.registerModule(new JavaTimeModule());
                            List<Double> batchNumbers = mapper.readValue(responseBody, new TypeReference<List<Double>>() {});
                            Platform.runLater(() -> combo.getItems().addAll(batchNumbers));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private List<String> getAllMaterialsFromHeader(BatchData header) {
        List<String> materials = new ArrayList<>();
        try {
            for (int i = 1; i <= 20; i++) {
                String methodName = "getMat" + i + "Name";
                Method method = BatchData.class.getMethod(methodName);
                String name = (String) method.invoke(header);
                if (name != null && !name.isBlank()) {
                    materials.add(name);
                }
            }
        } catch (Exception e) {
            System.err.println("Error fetching material names: " + e.getMessage());
            e.printStackTrace();
        }
        return materials;
    }

    private void openMaterialSelectionDialog() {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(primaryStage);
        dialog.setTitle("Select Materials");

        VBox container = new VBox(10);
        container.setPadding(new Insets(10));

        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(8);

        List<CheckBox> checkBoxes = new ArrayList<>();
        for (int i = 1; i <= 20; i++) {
            CheckBox cb = new CheckBox("Material " + i);
            if (selectedMaterials.contains(cb.getText())) {
                cb.setSelected(true);
            }
            checkBoxes.add(cb);
            int col = (i <= 10) ? 0 : 1;
            int row = (i - 1) % 10;
            grid.add(cb, col, row);
        }

        HBox actions = new HBox(10);
        Button selectAllBtn = new Button("Select All");
        Button clearAllBtn = new Button("Clear All");
        actions.getChildren().addAll(selectAllBtn, clearAllBtn);

        HBox bottom = new HBox(10);
        Button okBtn = new Button("OK");
        Button cancelBtn = new Button("Cancel");
        bottom.getChildren().addAll(okBtn, cancelBtn);

        container.getChildren().addAll(grid, actions, bottom);

        selectAllBtn.setOnAction(e -> checkBoxes.forEach(cb -> cb.setSelected(true)));
        clearAllBtn.setOnAction(e -> checkBoxes.forEach(cb -> cb.setSelected(false)));

        okBtn.setOnAction(e -> {
            selectedMaterials.clear();
            for (CheckBox cb : checkBoxes) {
                if (cb.isSelected()) {
                    selectedMaterials.add(cb.getText());
                }
            }
            saveSelectionsToFile(selectedMaterials);
            dialog.close();
        });

        cancelBtn.setOnAction(e -> dialog.close());

        Scene scene = new Scene(container, 350, 400);
        dialog.setScene(scene);
        dialog.showAndWait();
    }

    private void previewPdf(String filePath) {
        previewBox.getChildren().clear();
        try {
            currentPdfDocument = PDDocument.load(new File(filePath));
            PDFRenderer pdfRenderer = new PDFRenderer(currentPdfDocument);
            for (int page = 0; page < currentPdfDocument.getNumberOfPages(); page++) {
                BufferedImage bim = pdfRenderer.renderImageWithDPI(page, 120);
                Image fxImage = SwingFXUtils.toFXImage(bim, null);

                ImageView imageView = new ImageView(fxImage);
                imageView.setPreserveRatio(true);
                imageView.setFitWidth(700);

                previewBox.getChildren().add(imageView);
            }
        } catch (IOException ex) {
            showAlert("Error", "Could not load PDF: " + ex.getMessage());
        }
    }

    private void closeCurrentPdfDocument() {
        if (currentPdfDocument != null) {
            try {
                currentPdfDocument.close();
                currentPdfDocument = null;
                System.out.println("Previous PDF document closed successfully.");
            } catch (IOException e) {
                System.err.println("Error closing previous PDF: " + e.getMessage());
            }
        }
    }

    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    private void saveSelectionsToFile(List<String> selections) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(new File("selected_materials.json"), selections);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<String> loadSelectionsFromFile() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            File file = new File("selected_materials.json");
            if (file.exists()) {
                return mapper.readValue(file, new TypeReference<List<String>>() {});
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
}