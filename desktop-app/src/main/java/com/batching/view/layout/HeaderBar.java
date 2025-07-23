package com.batching.view.layout;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class HeaderBar extends VBox {

    private final ComboBox<String> fromBatchDropdown;
    private final ComboBox<String> toBatchDropdown;
    private final Button viewButton;
    private final Button printButton;

    private BiConsumer<String, String> onViewClicked;
    private Runnable onPrintClicked;

    public HeaderBar() {
        super(10); // vertical spacing
        this.getStyleClass().add("header-bar");

        // --- Report Type Toggle
        ToggleGroup reportTypeGroup = new ToggleGroup();
        RadioButton challanRadio = new RadioButton("Delivery Challan");
        RadioButton batchReportRadio = new RadioButton("Batch Report");
        batchReportRadio.setSelected(true);
        challanRadio.setToggleGroup(reportTypeGroup);
        batchReportRadio.setToggleGroup(reportTypeGroup);

        VBox reportTypeBox = new VBox(5, new Label("Report Type"), challanRadio, batchReportRadio);

        // --- Batch number range
        fromBatchDropdown = new ComboBox<>();
        toBatchDropdown = new ComboBox<>();
        fromBatchDropdown.setPromptText("From");
        toBatchDropdown.setPromptText("To");

        HBox batchRange = new HBox(5, new Label("Batch Number Wise:"), fromBatchDropdown, toBatchDropdown);

        // --- Optional checkboxes
        CheckBox prodTotal = new CheckBox("Production Total");
        CheckBox percentVar = new CheckBox("% Variation");
        CheckBox sump = new CheckBox("Sump");
        CheckBox cementName = new CheckBox("Cement Name");
        CheckBox moisture = new CheckBox("Moisture Correction");

        VBox optionalBox = new VBox(5, new Label("Optional Features:"), prodTotal, percentVar, sump, cementName, moisture);

        // --- Layout radio
        ToggleGroup layoutGroup = new ToggleGroup();
        RadioButton portrait = new RadioButton("Portrait");
        RadioButton landscape = new RadioButton("Landscape");
        portrait.setToggleGroup(layoutGroup);
        landscape.setToggleGroup(layoutGroup);
        portrait.setSelected(true);

        VBox layoutBox = new VBox(5, new Label("Layout:"), portrait, landscape);

        // --- Action buttons
        viewButton = new Button("View Report");
        printButton = new Button("Print Report");
        viewButton.getStyleClass().add("button");
        printButton.getStyleClass().add("button");

        HBox buttonBox = new HBox(10, viewButton, printButton);

        // --- Assemble final VBox
        this.getChildren().addAll(
                reportTypeBox,
                batchRange,
                optionalBox,
                layoutBox,
                buttonBox
        );

        viewButton.setOnAction(e -> {
            if (onViewClicked != null)
                onViewClicked.accept(fromBatchDropdown.getValue(), toBatchDropdown.getValue());
        });

        printButton.setOnAction(e -> {
            if (onPrintClicked != null)
                onPrintClicked.run();
        });

        loadBatchDropdowns();
    }

    private void loadBatchDropdowns() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/api/cycle/batch-numbers"))
                    .build();

            HttpClient.newHttpClient().sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::body)
                    .thenAccept(json -> {
                        try {
                            ObjectMapper mapper = new ObjectMapper();
                            List<String> batches = mapper.readValue(json, new TypeReference<>() {});
                            Platform.runLater(() -> {
                                fromBatchDropdown.getItems().setAll(batches);
                                toBatchDropdown.getItems().setAll(batches);
                                if (!batches.isEmpty()) {
                                    fromBatchDropdown.setValue(batches.get(0));
                                    toBatchDropdown.setValue(batches.get(0));
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setOnViewClicked(BiConsumer<String, String> handler) {
        this.onViewClicked = handler;
    }

    public void setOnPrintClicked(Runnable handler) {
        this.onPrintClicked = handler;
    }
}
