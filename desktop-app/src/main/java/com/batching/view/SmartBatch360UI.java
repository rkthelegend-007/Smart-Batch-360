package com.batching.view;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SmartBatch360UI {

    private Stage primaryStage; // keep reference

    public SmartBatch360UI(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public VBox getAnimatedUI() {
        Label title = new Label("SmartBatch 360");
        title.getStyleClass().add("title-label");

        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        // ✅ Create instances of other pages
        CycleDataTable cycleTable = new CycleDataTable();
        BatchReportUI batchReportUI = new BatchReportUI();

        // ✅ Add tabs
        Tab productionTab = new Tab("Production", cycleTable.createProductionContent());
        Tab consumptionTab = new Tab("Consumption", new Label("📊 Consumption data view coming soon..."));
        Tab batchReportTab = new Tab("Batch Report", batchReportUI.getView(primaryStage));

        tabPane.getTabs().addAll(productionTab, consumptionTab, batchReportTab);

        VBox appLayout = new VBox(15, title, tabPane);
        appLayout.setPadding(new Insets(20));
        return appLayout;
    }

    public void showMain() {
        VBox mainUI = getAnimatedUI();
        Scene mainScene = new Scene(mainUI, 1280, 700);
        mainScene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm()); // optional
        primaryStage.setScene(mainScene);
        primaryStage.setTitle("SmartBatch 360");
        primaryStage.show();
    }
}
