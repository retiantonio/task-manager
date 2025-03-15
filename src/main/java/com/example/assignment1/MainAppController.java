package com.example.assignment1;

import BusinessLogic.TaskManagement;
import DataModel.ComplexTask;
import DataModel.Employee;
import DataModel.SimpleTask;
import DataModel.Task;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainAppController {

    private TaskManagement taskManagement;

    private Thread updateThread;

    List<TaskController> simpleTaskControllers = new ArrayList<>();
    List<ComplexTaskController> complexTaskControllers = new ArrayList<>();

    @FXML private HBox mainHBox;
    @FXML private HBox employeeHBox;

    @FXML private VBox uncompletedVBox;
    @FXML private VBox completedVBox;

    @FXML private TabPane appTabPane;

    @FXML private Tab taskTab;
    @FXML private Tab employeeTab;

    private void updateComplexTask() {
        for(ComplexTaskController controller : complexTaskControllers) {
            if(controller.isTaskCompleted()) {
                Node component = controller.getComponentReference();
                if(!completedVBox.getChildren().contains(component))
                    completedVBox.getChildren().add(component);
            }
        }
    }

    private void updateSimpleTask() {
        for(TaskController controller : simpleTaskControllers)
            if(controller.isTaskCompleted()) {
                Node component = controller.getComponentReference();
                if(!completedVBox.getChildren().contains(component))
                    completedVBox.getChildren().add(component);
            }
    }

    @FXML
    public void addTask(ActionEvent event) throws IOException {
        appTabPane.getSelectionModel().select(taskTab);
        createTaskNode();
    }

    @FXML
    public void addEmployee(ActionEvent event) throws IOException {
        appTabPane.getSelectionModel().select(employeeTab);

        createEmployeeNode();
    }

    @FXML
    public void addComplexTask(ActionEvent event) throws IOException {
        appTabPane.getSelectionModel().select(taskTab);
        createComplexTaskNode();
    }

    private void restoreEmployees() throws IOException {
            //get ALL employees and RESTORE them
        for(Employee employee: taskManagement.getEmployees()) {
            CreateEmployeeController createEmployeeController = createEmployeeNode();
            createEmployeeController.restoreEmployee(employee.getNameEmployee());
        }
    }

    private void restoreTasks() throws IOException {
        for (Task task : taskManagement.getTasks()) {
            if(task instanceof SimpleTask) {
                    //Simple Task Restore
                TaskController taskController = createTaskNode();
                taskController.restoreTask(task);
            } else {
                    //Complex Task Restore
                ComplexTaskController complexTaskController = createComplexTaskNode();
                complexTaskController.restoreComplexTask(task);
            }
        }
    }

    private TaskController createTaskNode() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Task.fxml"));
        Node component = loader.load();

        TaskController controller = loader.getController();
        controller.setTaskManagement(taskManagement);
        controller.setComponentReference(component);

        simpleTaskControllers.add(controller);

        uncompletedVBox.getChildren().add(component);

        return controller;
    }

    private ComplexTaskController createComplexTaskNode() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("complex-task.fxml"));
        Node component = loader.load();

        ComplexTaskController controller = loader.getController();
        controller.setTaskManagement(taskManagement);
        controller.setComponentReference(component);

        complexTaskControllers.add(controller);

        uncompletedVBox.getChildren().add(1, component);

        return controller;
    }

    private CreateEmployeeController createEmployeeNode() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("create-employee.fxml"));
        Node component = loader.load();

        CreateEmployeeController controller = loader.getController();
        controller.setTaskManagement(taskManagement);

        employeeHBox.getChildren().add(component);
        System.out.println("Employee Added");

        return controller;
    }

    public void setTaskManagement(TaskManagement taskManagement) {
        this.taskManagement = taskManagement;

        try {
            restoreTasks();

            for(ComplexTaskController complexTaskController : complexTaskControllers) {
                complexTaskController.setTaskControllers(simpleTaskControllers);
            }

            restoreEmployees();
        } catch (IOException e) {
            e.printStackTrace();
        }

        startUpdateThread();
    }

    private void startUpdateThread() {
        updateThread = new Thread(() -> {
           while(!Thread.currentThread().isInterrupted()) {
               Platform.runLater(() -> {
                   updateComplexTask();
                   updateSimpleTask();
               });

               try {
                   Thread.sleep(500);
               } catch (InterruptedException e) {
                   System.out.println("Thread Interrupted");
                   Thread.currentThread().interrupt();
               }
           }
        });

        updateThread.setDaemon(true);
        updateThread.start();
    }
}
