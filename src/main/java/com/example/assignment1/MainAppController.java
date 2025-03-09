package com.example.assignment1;

import BusinessLogic.TaskManagement;
import DataAccess.AppSerialization;
import DataModel.Employee;
import DataModel.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class MainAppController {

    private TaskManagement taskManagement;

    @FXML private HBox mainHBox;
    @FXML private HBox employeeHBox;

    @FXML private VBox uncompletedVBox;
    @FXML private VBox completedVBox;

    @FXML private TabPane appTabPane;

    @FXML private Tab taskTab;
    @FXML private Tab employeeTab;

    @FXML
    public void addTask(ActionEvent event) throws IOException {
        appTabPane.getSelectionModel().select(taskTab);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("Task.fxml"));
        Node component = loader.load();

        TaskController controller = loader.getController();
        controller.setTaskManagement(taskManagement);


        uncompletedVBox.getChildren().add(component);
    }

    @FXML
    public void addEmployee(ActionEvent event) throws IOException {
        appTabPane.getSelectionModel().select(employeeTab);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("create-employee.fxml"));
        Node component = loader.load();

        CreateEmployeeController controller = loader.getController();
        controller.setTaskManagement(taskManagement);

        employeeHBox.getChildren().add(component);
        System.out.println("Employee Added");
    }

    public void setTaskManagement(TaskManagement taskManagement) {
        this.taskManagement = taskManagement;
    }
}
