package com.example.assignment1;

import BusinessLogic.TaskManagement;
import DataModel.ComplexTask;
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
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ComplexTaskController {

    @FXML private VBox containedTasksVBox;

    @FXML private TextField complexTaskTitleTF;

    @FXML private Label startHourLabel;
    @FXML private Label endHourLabel;
    @FXML private Label complexEstimatedTimeLabel;

    private TaskManagement taskManagement;

    private Node componentReference;

    private List<Task> selectedTasks = new ArrayList<>();
    private List<TaskController> unassginedTaskControllers = new ArrayList<>();
    private ComplexTask complexTaskObject;

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

    private boolean isExisting = false;
    private boolean isCompleted = false;

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
        if(isExisting) {
            complexTaskObject.setTaskName(complexTaskTitleTF.getText());
        } else {
            complexTaskObject = new ComplexTask("Uncompleted", complexTaskTitleTF.getText());
            isExisting = true;
        }

        for(Task task : selectedTasks) {
            complexTaskObject.addTask(task);
            for(Employee employee : taskManagement.getEmployeesWithTask(task)) {
                taskManagement.assignTaskToEmployee(employee, complexTaskObject);
                taskManagement.removeTaskFromEmployee(employee, task);
            }
        }

        setAutomaticFeatures();
    }

    @FXML
    private void completeComplexTask(ActionEvent event) {
        complexTaskObject.modifyTaskStatus();
        isCompleted = true;
    }

    private void addTaskTag(Task task) throws IOException {
        List<TaskController> unassignedList = new ArrayList<>(unassginedTaskControllers);

        for(TaskController taskController : unassignedList)
            if(taskController.getTaskObject() == task) {
                Node component = taskController.getComponentReference();
                taskController.restoreTask(task);

                unassginedTaskControllers.remove(taskController);

                containedTasksVBox.getChildren().add(component);
            }
    }

    private void restoreTaskTag(Task task) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Task.fxml"));
        Node component = loader.load();

        TaskController controller = loader.getController();
        controller.setTaskManagement(taskManagement);
        controller.setComponentReference(component);

        controller.restoreTask(task);

        containedTasksVBox.getChildren().addFirst(component);
    }



    public void restoreComplexTask(Task task) throws IOException {
        isExisting = true;

        complexTaskObject = (ComplexTask) task;


        selectedTasks = complexTaskObject.getSubTasks();

        if(complexTaskObject.getStatusTask().equals("Completed"))
            isCompleted = true;

        for(Task subTask : selectedTasks)
            restoreTaskTag(subTask);

        setAutomaticFeatures();
    }

    public void setTaskManagement(TaskManagement taskManagement) {
        this.taskManagement = taskManagement;
    }

    public void setAutomaticFeatures() {
            //Sets startHour, endHour, estimatedTime (ALL LABELS)
        endHourLabel.setText(complexTaskObject.getEndHour().format(formatter));
        startHourLabel.setText(complexTaskObject.getStartHour().format(formatter));
        complexEstimatedTimeLabel.setText(complexTaskObject.estimateDuration() + " Minutes");
    }

    public void setComponentReference(Node componentReference) {
        this.componentReference = componentReference;
    }

    public Node getComponentReference() {
        return componentReference;
    }

    public boolean isTaskCompleted() {
        return isCompleted;
    }

    public void setTaskControllers(List<TaskController> simpleTaskControllers) {
        this.unassginedTaskControllers = simpleTaskControllers;
    }
}
