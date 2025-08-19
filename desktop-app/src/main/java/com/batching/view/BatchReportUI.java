package com.batching.view;

import com.batching.model.BatchData;
import com.batching.view.export.ExportBatchReport;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BatchReportUI {

    private Stage primaryStage;
    private VBox previewBox;
    private ComboBox<Double> batchCombo;
    private DatePicker startDatePicker;
    private DatePicker endDatePicker;

    // Material checkboxes
    private CheckBox cb10mm, cb20mm, cb30mm, cb40mm,
            cbCmt1, cbCmt2, cbCmt3, cbWtr1, cbAdmix1, cbAdmix2;

    public BorderPane getView(Stage stage) {
        this.primaryStage = stage;

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        // --- Filters Left Panel ---
        VBox leftPane = new VBox(10);
        leftPane.setPadding(new Insets(10));
        leftPane.setPrefWidth(250);

        batchCombo = new ComboBox<>();
        batchCombo.setPromptText("Select Batch Number");

        startDatePicker = new DatePicker();
        endDatePicker = new DatePicker();

        Button viewBtn = new Button("View Data");
        Button generateBtn = new Button("Generate PDF");
        Button previewBtn = new Button("Preview PDF");

        // Material selection box
        VBox materialBox = new VBox(5);
        materialBox.setPadding(new Insets(5));
        materialBox.setStyle("-fx-border-color: lightgray; -fx-border-radius: 5;");
        Label matLabel = new Label("Select Materials:");

        cb10mm = new CheckBox("10MM");
        cb20mm = new CheckBox("20MM");
        cb30mm = new CheckBox("30MM");
        cb40mm = new CheckBox("40MM");
        cbCmt1 = new CheckBox("CMT1");
        cbCmt2 = new CheckBox("CMT2");
        cbCmt3 = new CheckBox("CMT3");
        cbWtr1 = new CheckBox("WTR1");
        cbAdmix1 = new CheckBox("ADMIX1");
        cbAdmix2 = new CheckBox("ADMIX2");

        materialBox.getChildren().addAll(matLabel,
                cb10mm, cb20mm, cb30mm, cb40mm,
                cbCmt1, cbCmt2, cbCmt3,
                cbWtr1, cbAdmix1, cbAdmix2
        );

        leftPane.getChildren().addAll(
                new Label("Batch:"), batchCombo,
                new Label("Start Date:"), startDatePicker,
                new Label("End Date:"), endDatePicker,
                materialBox,
                viewBtn, generateBtn, previewBtn
        );

        // --- Right PDF Preview ---
        previewBox = new VBox(10);
        previewBox.setPadding(new Insets(10));
        ScrollPane pdfScroll = new ScrollPane(previewBox);
        pdfScroll.setFitToWidth(true);

        SplitPane splitPane = new SplitPane();
        splitPane.getItems().addAll(leftPane, pdfScroll);
        splitPane.setDividerPositions(0.25);

        root.setCenter(splitPane);

        // --- Load batch numbers ---
        loadBatchNumbers();

        // --- Button Actions ---
        viewBtn.setOnAction(e -> {
            Double batchNo = batchCombo.getValue();
            LocalDate startDate = startDatePicker.getValue();
            LocalDate endDate = endDatePicker.getValue();
            fetchData(batchNo, startDate, endDate);
        });

        generateBtn.setOnAction(e -> {
            Double batchNo = batchCombo.getValue();
            LocalDate startDate = startDatePicker.getValue();
            LocalDate endDate = endDatePicker.getValue();
            fetchAndGeneratePdf(batchNo, startDate, endDate, false);
        });

        previewBtn.setOnAction(e -> {
            Double batchNo = batchCombo.getValue();
            LocalDate startDate = startDatePicker.getValue();
            LocalDate endDate = endDatePicker.getValue();
            fetchAndGeneratePdf(batchNo, startDate, endDate, true);
        });

        return root;
    }

    private void loadBatchNumbers() {
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
                            List<Double> batchNumbers = mapper.readValue(responseBody,
                                    new TypeReference<List<Double>>() {});
                            Platform.runLater(() -> batchCombo.getItems().addAll(batchNumbers));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void fetchData(Double batchNumber, LocalDate startDate, LocalDate endDate) {
        // optional if you want to preview data before generating
    }

    private void fetchAndGeneratePdf(Double batchNumber, LocalDate startDate, LocalDate endDate, boolean previewOnly) {
        try {
            String url = "http://localhost:8080/api/batch-data/filter?";
            if (batchNumber != null) url += "batchNumber=" + batchNumber + "&";
            if (startDate != null) url += "startDate=" + startDate.toString() + "&";
            if (endDate != null) url += "endDate=" + endDate.toString();

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
                            List<BatchData> list = mapper.readValue(responseBody,
                                    new TypeReference<List<BatchData>>() {});

                            List<String> selectedMaterials = getSelectedMaterials();

                            Platform.runLater(() -> {
                                try {
                                    File file;
                                    if (previewOnly) {
                                        file = File.createTempFile("batch_preview_", ".pdf");
                                        file.deleteOnExit();
                                    } else {
                                        FileChooser fileChooser = new FileChooser();
                                        fileChooser.setTitle("Save Batch Report");
                                        fileChooser.getExtensionFilters().add(
                                                new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
                                        file = fileChooser.showSaveDialog(primaryStage);
                                        if (file == null) return;
                                    }

                                    ExportBatchReport.generateReport(list, file.getAbsolutePath(), selectedMaterials);

                                    previewPdf(file.getAbsolutePath());

                                } catch (Exception e) {
                                    e.printStackTrace();
                                    showAlert("Error", "PDF generation failed: " + e.getMessage());
                                }
                            });

                        } catch (Exception ex) {
                            ex.printStackTrace();
                            showAlert("Error", "Failed to parse data: " + ex.getMessage());
                        }
                    });
        } catch (Exception ex) {
            ex.printStackTrace();
            showAlert("Error", "Failed to fetch data: " + ex.getMessage());
        }
    }

    private List<String> getSelectedMaterials() {
        List<String> selected = new ArrayList<>();
        if (cb10mm.isSelected()) selected.add("10MM");
        if (cb20mm.isSelected()) selected.add("20MM");
        if (cb30mm.isSelected()) selected.add("30MM");
        if (cb40mm.isSelected()) selected.add("40MM");
        if (cbCmt1.isSelected()) selected.add("CMT1");
        if (cbCmt2.isSelected()) selected.add("CMT2");
        if (cbCmt3.isSelected()) selected.add("CMT3");
        if (cbWtr1.isSelected()) selected.add("WTR1");
        if (cbAdmix1.isSelected()) selected.add("ADMIX1");
        if (cbAdmix2.isSelected()) selected.add("ADMIX2");
        return selected;
    }

    private void previewPdf(String filePath) {
        previewBox.getChildren().clear();
        try (PDDocument document = PDDocument.load(new File(filePath))) {
            PDFRenderer pdfRenderer = new PDFRenderer(document);
            for (int page = 0; page < document.getNumberOfPages(); page++) {
                BufferedImage bim = pdfRenderer.renderImageWithDPI(page, 120);
                Image fxImage = SwingFXUtils.toFXImage(bim, null);

                ImageView imageView = new ImageView(fxImage);
                imageView.setPreserveRatio(true);
                imageView.setFitWidth(700);

                previewBox.getChildren().add(imageView);
            }
        } catch (Exception ex) {
            showAlert("Error", "Could not load PDF: " + ex.getMessage());
        }
    }

    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
