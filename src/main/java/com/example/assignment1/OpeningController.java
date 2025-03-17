package com.example.assignment1;

import businessLogic.TaskManagement;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class OpeningController {

    @FXML private TextField nameTextField;

    private TaskManagement taskManagement;

    @FXML
    protected void switchToMainApp(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("main-app.fxml"));
        Parent root = loader.load();

        MainAppController controller = loader.getController();
        controller.setTaskManagement(taskManagement);

        controller.setUpWelcomeLabel(nameTextField.getText());

        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();

        Scene scene = new Scene(root);
        stage.setScene(scene);
    }

    public void setTaskManagement(TaskManagement taskManagement) {
        this.taskManagement = taskManagement;
    }
}