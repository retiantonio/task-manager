package com.example.assignment1;

import BusinessLogic.TaskManagement;
import DataModel.Employee;
import DataModel.SimpleTask;
import DataModel.Task;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
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
import javafx.util.Duration;

import java.io.IOException;
import java.text.ParseException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class TaskController {

    @FXML private VBox employeeVBox;

    @FXML private TextField startHourTF;
    @FXML private TextField endHourTF;
    @FXML private TextField taskTitleTF;

    @FXML private Label estimatedTimeLabel;

    private TaskManagement taskManagement;

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

    private boolean isExisting = false;
    private boolean isCompleted = false;
    private boolean isTaskAssigned = false;

    private SimpleTask taskObject;

    private Node componentReference;

    private ObservableList<Employee> employeeList = FXCollections.observableArrayList();
    private List<Employee> selectedEmployees = new ArrayList<>();

    @FXML
    public void addEmployeeToTask(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("EmployeeSelection.fxml"));
        Parent root = loader.load();

        employeeList = FXCollections.observableArrayList(new ArrayList<>(taskManagement.getEmployees()));

        EmployeeSelectController controller = loader.getController();
        controller.setEmployeeList(employeeList);

        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Add Employee");

        stage.setScene(new Scene(root));
        stage.showAndWait();

        Employee selectedEmployee = controller.getSelectedEmployee();
        selectedEmployees.add(selectedEmployee);

        addEmployeeTag(selectedEmployee);
    }

    @FXML
    public void saveTask(ActionEvent event) {
        LocalTime startHour;
        LocalTime endHour;

        boolean isItFirst = true;

        try {
            startHour = parseTime(startHourTF.getText());
            isItFirst = false;
            endHour = parseTime(endHourTF.getText());

            if(isExisting) {
                taskObject.setTaskName(taskTitleTF.getText());
                taskObject.setStartHour(startHour);
                taskObject.setEndHour(endHour);
            } else {
                taskObject = new SimpleTask("Uncompleted", taskTitleTF.getText(), startHour, endHour);
                isExisting = true;
            }

            for(Employee employee : selectedEmployees)
                taskManagement.assignTaskToEmployee(employee, taskObject);

            estimatedTimeLabel.setText(taskObject.estimateDuration() + " Minutes");
        } catch (ParseException | DateTimeParseException exception) {
            inputExceptionAnimation(isItFirst);
        }
        System.out.println("Task Saved");
    }

    @FXML
    private void completeTask(ActionEvent event) {
        isCompleted = true;
        taskObject.modifyTaskStatus();
    }

    public void addEmployeeTag(Employee employee) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Employee.fxml"));

        Node component = fxmlLoader.load();

        EmployeeController controller = fxmlLoader.getController();
        controller.setEmployeeLabel(employee.getNameEmployee());

        employeeVBox.getChildren().addFirst(component);
    }

    public void restoreTask(Task task) throws IOException {
        isExisting = true;

        taskObject = (SimpleTask) task;

        selectedEmployees = taskManagement.getEmployeesWithTask(task);
        for(Employee employee : selectedEmployees)
            addEmployeeTag(employee);

        if(taskObject.getStatusTask().equals("Completed")) {
            isCompleted = true;
        }

        taskTitleTF.setText(task.getNameTask());

        startHourTF.setText(task.getStartHour().format(formatter));
        endHourTF.setText(task.getEndHour().format(formatter));

        setEstimatedTimeLabel();
    }

    private LocalTime parseTime(String input) throws ParseException {
        return LocalTime.parse(input, formatter);
    }

    private void inputExceptionAnimation(boolean isItFirst) {
        Timeline colorTransition;

        if(isItFirst) {
            if(bothDatesMismatchFormat()) {
                startHourTF.clear();
                endHourTF.clear();

                colorTransition = new Timeline(
                        new KeyFrame(Duration.ZERO, e -> {
                            startHourTF.setStyle("-fx-prompt-text-fill: red;");
                            endHourTF.setStyle("-fx-prompt-text-fill: red;");
                        }),
                        new KeyFrame(Duration.seconds(1), e -> {
                            startHourTF.setStyle("-fx-prompt-text-fill: #8C8686;");
                            endHourTF.setStyle("-fx-prompt-text-fill: #8C8686;");
                        })
                );
            } else {
                startHourTF.clear();

                colorTransition = new Timeline(
                        new KeyFrame(Duration.ZERO, e -> { startHourTF.setStyle("-fx-prompt-text-fill: red;"); }),
                        new KeyFrame(Duration.seconds(1), e -> { startHourTF.setStyle("-fx-prompt-text-fill: #8C8686;"); })
                );
            }
        } else {
            colorTransition =  new Timeline(
                    new KeyFrame(Duration.ZERO, e -> { endHourTF.setStyle("-fx-prompt-text-fill: red;"); }),
                    new KeyFrame(Duration.seconds(1), e -> { endHourTF.setStyle("-fx-prompt-text-fill: #8C8686;"); })
            );
            endHourTF.clear();
        }

        colorTransition.setCycleCount(1);
        colorTransition.setAutoReverse(true);
        colorTransition.playFromStart();
    }

    private boolean bothDatesMismatchFormat() {
        try {
            LocalTime temp = parseTime(endHourTF.getText());
        } catch(ParseException | DateTimeParseException exception) {
            return true;
        }

        return false;
    }

    public void setTaskManagement(TaskManagement taskManagement) {
        this.taskManagement = taskManagement;
    }

    public void setEstimatedTimeLabel() {
        estimatedTimeLabel.setText(taskObject.estimateDuration() + " Minutes");
    }

    public void setComponentReference(Node component) {
        componentReference = component;
    }

    public Node getComponentReference() {
        return componentReference;
    }

    public boolean isTaskCompleted() {
        return isCompleted;
    }

    public SimpleTask getTaskObject() {
        return taskObject;
    }
}