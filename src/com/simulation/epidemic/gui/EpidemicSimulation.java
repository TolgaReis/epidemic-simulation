package com.simulation.epidemic.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * Driver class for the simulation that runs the gui.
 */
public class EpidemicSimulation extends Application {
    /**
     * Starts the interface by loading fxml.
     * @param stage represents the primary window of application.
     * @throws Exception raises when there is an exception about project.
     */
    @Override
    public void start(Stage stage) throws Exception {
        try {
            FXMLLoader loader = new FXMLLoader();
            BorderPane root = (BorderPane) loader.load(getClass().getResource("EpidemicSimulationApp.fxml").openStream());
            stage.setScene(new Scene(root));
            stage.show();
        }
        catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
