package com.example.assignment1;

import businessLogic.TaskManagement;
import dataModel.ComplexTask;
import dataModel.Employee;
import dataModel.SimpleTask;
import dataModel.Task;
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
import java.util.NoSuchElementException;

public class ComplexTaskController {

    @FXML private VBox containedTasksVBox;

    @FXML private TextField complexTaskTitleTF;

    @FXML private Label startHourLabel;
    @FXML private Label endHourLabel;
    @FXML private Label complexEstimatedTimeLabel;

    private TaskManagement taskManagement;

    private Node componentReference;

    private List<Task> selectedTasks = new ArrayList<>();
    private List<TaskController> unassignedTaskControllers = new ArrayList<>();
    private List<ComplexTaskController> unassignedComplexTaskControllers = new ArrayList<>();
    private List<ComplexTagController> complexTagControllers = new ArrayList<>();
    private ComplexTask complexTaskObject;

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

    private boolean isExisting = false;
    private boolean needUpdate = false;

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
        if(selectedTask != null) {
            selectedTasks.add(selectedTask);
            addTaskTag(selectedTask);
        }
    }

    @FXML
    public void saveComplexTask(ActionEvent event) {
        if(isExisting) {
            complexTaskObject.setTaskName(complexTaskTitleTF.getText());
            needUpdate = true;
        } else {
            complexTaskObject = new ComplexTask(taskManagement.generateNewTaskID(), "Uncompleted", complexTaskTitleTF.getText());
            isExisting = true;
        }

        for(Task task : selectedTasks) {
            complexTaskObject.addTask(task);

            System.out.println(task.getNameTask());
            for(Employee employee : taskManagement.getEmployeesWithTask(task))
                System.out.println(employee.getNameEmployee());

            for(Employee employee : taskManagement.getEmployeesWithTask(task)) {
                taskManagement.assignTaskToEmployee(employee, complexTaskObject);
                taskManagement.removeTaskFromEmployee(employee, task);

                if(task instanceof SimpleTask)
                    taskManagement.storeEmployeeSimpleTask(employee, (SimpleTask) task);

                taskManagement.calculateEmployeeWorkDuration(employee);
            }
        }

        setAutomaticFeatures();
        System.out.println(unassignedTaskControllers.size());
    }

    @FXML
    private void completeComplexTask(ActionEvent event) {
        if(complexTaskObject != null)
            complexTaskObject.modifyTaskStatus();
    }

    private void addTaskTag(Task task) throws IOException {
        if(task instanceof SimpleTask) {
            List<TaskController> unassignedList = new ArrayList<>(unassignedTaskControllers);

            for (TaskController taskController : unassignedList) {
                if (taskController.getTaskObject() == task) {
                    Node component = taskController.getComponentReference();

                    unassignedTaskControllers.remove(taskController);

                    containedTasksVBox.getChildren().addFirst(component);
                }
            }
        } else {
            List<ComplexTaskController> unassignedComplexList = new ArrayList<>(unassignedComplexTaskControllers);

            for (ComplexTaskController complexTaskController : unassignedComplexList) {
                if(complexTaskController.getComplexTaskObject() == task) {
                    ComplexTagController complexTagController = createComplexTagNode();
                    complexTagController.restoreComplexTag(task);

                    complexTagControllers.add(complexTagController);
                }
            }
        }
    }

    private ComplexTagController createComplexTagNode() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("complex-tag.fxml"));
        Node component = loader.load();

        ComplexTagController controller = loader.getController();
        complexTagControllers.add(controller);

        containedTasksVBox.getChildren().addFirst(component);

        return controller;
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

    private void restoreComplexTaskTag(Task task) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("complex-tag.fxml"));
        Node component = loader.load();

        ComplexTagController controller = loader.getController();

        controller.restoreComplexTag(task);

        containedTasksVBox.getChildren().addFirst(component);
    }

    public void restoreComplexTask(Task task) throws IOException {
        isExisting = true;

        complexTaskObject = (ComplexTask) task;

        selectedTasks = complexTaskObject.getSubTasks();

        for(Task subTask : selectedTasks)
            if(subTask instanceof SimpleTask)
                restoreTaskTag(subTask);
            else restoreComplexTaskTag(subTask);


        complexTaskTitleTF.setText(complexTaskObject.getNameTask());
        setAutomaticFeatures();
    }

    public void setTaskManagement(TaskManagement taskManagement) {
        this.taskManagement = taskManagement;
    }

    public void setAutomaticFeatures() {
        try {
            //Sets startHour, endHour, estimatedTime (ALL LABELS)
            endHourLabel.setText(complexTaskObject.getEndHour().format(formatter));
            startHourLabel.setText(complexTaskObject.getStartHour().format(formatter));

            int estimateDuration = complexTaskObject.estimateDuration();
            int hourDuration = estimateDuration / 60;
            int minutesDuration = estimateDuration % 60;

            complexEstimatedTimeLabel.setText(String.format("%d hours : %d minutes", hourDuration, minutesDuration));
        } catch(NoSuchElementException e) {
            System.out.println("No Tasks Added");
        }
    }

    public void setComponentReference(Node componentReference) {
        this.componentReference = componentReference;
    }

    public Node getComponentReference() {
        return componentReference;
    }

    public void setTaskControllers(List<TaskController> simpleTaskControllers, List<ComplexTaskController> complexTaskControllers) {
        this.unassignedTaskControllers = simpleTaskControllers;
        this.unassignedComplexTaskControllers = complexTaskControllers;
    }

    public ComplexTask getComplexTaskObject() {
        return complexTaskObject;
    }

    public List<ComplexTagController> getComplexTagControllers() {
        return complexTagControllers;
    }

    public boolean needUpdate() {
        return needUpdate;
    }

    public void resetUpdateNeeds() {
        needUpdate = false;
    }

    public void printList() {
        System.out.println("In Complex: " + unassignedTaskControllers.size());
    }

    public boolean isCompleted() {
        if(complexTaskObject != null)
            if(complexTaskObject.getStatusTask().equals("Completed"))
                return true;

        return false;
    }
}
