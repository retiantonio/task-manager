package com.example.assignment1;

import businessLogic.TaskManagement;
import businessLogic.Utility;
import dataModel.ComplexTask;
import dataModel.Employee;
import dataModel.SimpleTask;
import dataModel.Task;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainAppController {

    private TaskManagement taskManagement;

    private Thread updateThread;

    private List<TaskController> simpleTaskControllers = new ArrayList<>();
    private List<ComplexTaskController> complexTaskControllers = new ArrayList<>();
    private List<CreateEmployeeController> createEmployeeControllers = new ArrayList<>();
    private List<EmployeeStatisticsController> employeeStatisticsControllerList = new ArrayList<>();

    @FXML private VBox uncompletedVBox;
    @FXML private VBox completedVBox;
    @FXML private VBox employeeVBox;
    @FXML private VBox statisticsVBox;

    @FXML private TabPane appTabPane;

    @FXML private Tab taskTab;
    @FXML private Tab employeeTab;

    @FXML private Label welcomeLabel;

    private void updateComplexTask() {
        for(ComplexTaskController controller : complexTaskControllers) {
            if(controller.isCompleted()) {
                Node component = controller.getComponentReference();
                if(!completedVBox.getChildren().contains(component))
                    completedVBox.getChildren().add(component);
            }
        }
    }

    private void updateSimpleTask() {
        //System.out.println(simpleTaskControllers);
        for(TaskController controller : simpleTaskControllers)
            if(controller.isCompleted()) {
                Node component = controller.getComponentReference();
                if(!completedVBox.getChildren().contains(component))
                    completedVBox.getChildren().add(component);
            }
    }

    private void updateEmployee() {
        for(CreateEmployeeController controller : createEmployeeControllers) {
            try {
                controller.updateEmployeeTag();
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    private void updateEmployeeStatistics() {
        for(EmployeeStatisticsController controller : employeeStatisticsControllerList) {
            Node component = controller.getComponentReference();
            statisticsVBox.getChildren().remove(component);
        }

        try {
            restoreEmployeesStatistics();
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }
    }

    private void updateComplexTag() {
        for(ComplexTaskController complexController : complexTaskControllers) {
            if(complexController.needUpdate()) {
                System.out.println("Updating Complex Tag");
                for (ComplexTagController tagController : complexController.getComplexTagControllers()) {
                    tagController.updateNameTag();
                }
                complexController.resetUpdateNeeds();
            }
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
            createEmployeeController.restoreEmployee(employee);
        }
    }

    private void restoreEmployeesStatistics() throws IOException {
        List<Employee> employeeList = Utility.employeeProductivitySort(taskManagement);

        for(Employee employee : employeeList) {
            EmployeeStatisticsController createEmployeeController = createEmployeeStatisticsNode();
            createEmployeeController.restoreEmployeeStatistics(employee, employeeList.indexOf(employee), employeeList.size());
        }
    }

    private void restoreTasks() throws IOException {
        List<Task> tasksToBeRestored = taskManagement.getTasks();
        List<Task> taskIterator = new ArrayList<>(tasksToBeRestored);

        for(Task task : taskIterator)
            if(task instanceof ComplexTask)
                tasksToBeRestored.addAll(taskManagement.recursiveGetComplexSubTasks(((ComplexTask) task).getSubTasks()));

        for (Task task : tasksToBeRestored) {
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

        System.out.println("In main: " + simpleTaskControllers.size());
        for(ComplexTaskController complexTaskController : complexTaskControllers) {
            complexTaskController.printList();
        }

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
        controller.setTaskControllers(simpleTaskControllers, complexTaskControllers);

        uncompletedVBox.getChildren().add(1, component);

        return controller;
    }

    private CreateEmployeeController createEmployeeNode() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("create-employee.fxml"));
        Node component = loader.load();

        CreateEmployeeController controller = loader.getController();
        controller.setTaskManagement(taskManagement);
        controller.setComponentReference(component);

        createEmployeeControllers.add(controller);

        employeeVBox.getChildren().add(component);

        return controller;
    }

    private EmployeeStatisticsController createEmployeeStatisticsNode() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("employee-statistics.fxml"));
        Node component = loader.load();

        EmployeeStatisticsController controller = loader.getController();
        controller.setTaskManagement(taskManagement);
        controller.setComponentReference(component);

        employeeStatisticsControllerList.add(controller);

        statisticsVBox.getChildren().add(component);

        return controller;
    }

    public void setTaskManagement(TaskManagement taskManagement) {
        this.taskManagement = taskManagement;

        try {
            restoreTasks();

            for(ComplexTaskController complexTaskController : complexTaskControllers)
                complexTaskController.setTaskControllers(simpleTaskControllers, complexTaskControllers);

            restoreEmployees();
            restoreEmployeesStatistics();
        } catch (IOException e) {
            e.printStackTrace();
        }

        startUpdateThread();
    }

    public void setUpWelcomeLabel(String name) {
        if(name.isBlank()) {
            welcomeLabel.setText("Welcome back, manager!\nStart managing your employees right now using Task Master!");
        } else welcomeLabel.setText("Welcome back, " + name + "!\n Start managing your employees right now using Task Master!");
    }

    private void startUpdateThread() {
        updateThread = new Thread(() -> {
           while(!Thread.currentThread().isInterrupted()) {
               Platform.runLater(() -> {
                   updateComplexTask();
                   updateSimpleTask();
                   updateComplexTag();

                   updateEmployee();
                   if(appTabPane.getSelectionModel().getSelectedItem().equals(employeeTab))
                       updateEmployeeStatistics();
               } );
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
