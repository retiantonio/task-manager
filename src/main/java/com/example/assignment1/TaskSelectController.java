package com.example.assignment1;

import DataModel.Task;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.util.List;

public class TaskSelectController {

    @FXML private TableView<Task> taskTable;

    @FXML private TableColumn<Task, Integer> IDColumn;
    @FXML private TableColumn<Task, String> taskNameColumn;

    @FXML private Button taskSelectButton;

    private Task selectedTask;

    @FXML
    public void initialize() {
        IDColumn.setCellValueFactory(cellData -> cellData.getValue().idTaskProperty().asObject());
        taskNameColumn.setCellValueFactory(cellData -> cellData.getValue().nameTaskProperty());
    }

    @FXML
    public void confirmTask(ActionEvent event) {
        selectedTask = taskTable.getSelectionModel().getSelectedItem();

        if (selectedTask != null)
            ((Stage) taskSelectButton.getScene().getWindow()).close();
    }

    public void setTaskList(ObservableList<Task> tasks) {
        taskTable.setItems(tasks);
    }

    public Task getSelectedTask() { return selectedTask; }
}
