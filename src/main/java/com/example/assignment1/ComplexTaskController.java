package com.example.assignment1;

import BusinessLogic.TaskManagement;
import DataModel.Employee;
import DataModel.Task;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ComplexTaskController {

    @FXML private VBox containedTasksVBox;

    private TaskManagement taskManagement;
    private List<Task> selectedTasks = new ArrayList<>();

    @FXML
    public void addAssignedTask(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("task-selection.fxml"));
        Parent root = loader.load();

        ObservableList<Task> taskList = FXCollections.observableArrayList(new ArrayList<>(taskManagement.getTasks()));

        TaskSelectController controller = loader.getController();
        controller.setTaskList(taskList);

        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Assign Task");

        stage.setScene(new Scene(root));
        stage.showAndWait();

        Task selectedTask = controller.getSelectedTask();
        selectedTasks.add(selectedTask);

        addTaskTag(selectedTask);
    }

    @FXML
    public void saveComplexTask(ActionEvent event) {

    }

    private void addTaskTag(Task task) throws IOException {
        List<Employee> employeeOnTask = taskManagement.getEmployeesWithTask(task);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("Task.fxml"));
        Node component = loader.load();

        TaskController controller = loader.getController();
        controller.setTaskManagement(taskManagement);
        controller.restoreTask(task, employeeOnTask);

        for(Employee employee : employeeOnTask)
            controller.addEmployeeTag(employee);

        containedTasksVBox.getChildren().addFirst(component);
    }

    public void setTaskManagement(TaskManagement taskManagement) {
        this.taskManagement = taskManagement;
    }
}
