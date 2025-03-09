package com.example.assignment1;

import BusinessLogic.TaskManagement;
import DataAccess.AppSerialization;
import DataModel.Task;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;


import javax.swing.*;
import java.io.IOException;

public class Main extends Application {

    private TaskManagement taskManagement;

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {
        try {
            taskManagement = AppSerialization.deserializeData();
            System.out.println("Data Deserialized");
        } catch (IOException e) {
            System.out.println("Data could not be Deserialized");
            taskManagement = new TaskManagement();
        } catch (Exception e) {
            e.printStackTrace();
        }

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        OpeningController openingController = fxmlLoader.getController();
        openingController.setTaskManagement(taskManagement);

        String css = getClass().getResource("OpeningStyle.css").toExternalForm();
        scene.getStylesheets().add(css);

        setStage(stage);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        AppSerialization.serializeData(taskManagement);
        System.out.println("Data Serialized");
    }

    private void setStage(Stage stage) {
        Image logo = new Image(getClass().getResource("logo.png").toExternalForm());

        stage.getIcons().add(logo);
        stage.setResizable(false);
        stage.setTitle("Task Master");
    }
}