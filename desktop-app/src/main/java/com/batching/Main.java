package com.batching;

import com.batching.view.CycleDataTable;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        new CycleDataTable().showTable(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
