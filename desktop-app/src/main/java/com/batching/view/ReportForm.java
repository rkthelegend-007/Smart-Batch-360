package com.batching.view;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.batching.model.Report;
import com.fasterxml.jackson.databind.ObjectMapper;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ReportForm {
    public void showForm(Stage primaryStage) {
        TextField operatorField = new TextField();
        TextField cementField = new TextField();
        TextField sandField = new TextField();
        TextField aggregateField = new TextField();

        Button submitBtn = new Button("Submit Report");

        submitBtn.setOnAction(e -> {
            try {
                Report report = new Report();
                report.setOperatorName(operatorField.getText());
                report.setCementWeight(Double.parseDouble(cementField.getText()));
                report.setSandWeight(Double.parseDouble(sandField.getText()));
                report.setAggregateWeight(Double.parseDouble(aggregateField.getText()));

                ObjectMapper mapper = new ObjectMapper();
                String requestBody = mapper.writeValueAsString(report);

                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create("http://localhost:8080/api/reports"))
                        .header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                        .build();

                HttpClient.newHttpClient().sendAsync(request, HttpResponse.BodyHandlers.ofString())
                        .thenAccept(response -> {
                            System.out.println("Response: " + response.statusCode());
                            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Report Submitted!");
                            alert.showAndWait();
                        });
            } catch (Exception ex) {
                ex.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Failed to submit report!").showAndWait();
            }
        });

        VBox root = new VBox(10,
                new Label("Operator Name:"), operatorField,
                new Label("Cement Weight:"), cementField,
                new Label("Sand Weight:"), sandField,
                new Label("Aggregate Weight:"), aggregateField,
                submitBtn
        );
        root.setPadding(new Insets(20));
        Scene scene = new Scene(root, 400, 400);

        primaryStage.setTitle("Batching Report Form");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
