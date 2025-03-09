package com.example.assignment1;

import BusinessLogic.TaskManagement;
import DataModel.Employee;
import DataModel.SimpleTask;
import DataModel.Task;
import javafx.animation.FadeTransition;
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
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.sql.Time;
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

    private TaskManagement taskManagement;

    private ObservableList<Employee> employeeList = FXCollections.observableArrayList();
    private List<Employee> selectedEmployees = new ArrayList<>();

    @FXML
    private void initialize() {}

    @FXML
    public void addEmployeeToTask(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("EmployeeSelection.fxml"));
        Parent root = loader.load();

        employeeList = FXCollections.observableArrayList(new ArrayList<>(taskManagement.getEmployeeSet()));

        EmployeeSelectController controller = loader.getController();
        controller.setEmployeeList(employeeList);

        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Add Employee");

        stage.setScene(new Scene(root));
        stage.showAndWait();

        Employee selectedEmployee = controller.getSelectedEmployee();
        selectedEmployees.add(selectedEmployee);

        addUIComponent(selectedEmployee);
    }

    @FXML
    public void saveTask(ActionEvent event) {
        LocalTime startHour;
        LocalTime endHour;
        Task task;

        boolean isItFirst = true;

        try {
            startHour = parseTime(startHourTF.getText());
            isItFirst = false;
            endHour = parseTime(endHourTF.getText());

            task = new SimpleTask("Uncompleted", taskTitleTF.getText(), startHour, endHour);
            for(Employee employee : selectedEmployees)
                taskManagement.assignTaskToEmployee(employee, task);
        } catch (ParseException | DateTimeParseException exception) {
            inputExceptionAnimation(isItFirst);
        }
        System.out.println("Task Saved");
    }



    public void addUIComponent(Employee employee) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Employee.fxml"));

        Node component = fxmlLoader.load();

        EmployeeController controller = fxmlLoader.getController();
        controller.setEmployeeLabel(employee.getNameEmployee());

        employeeVBox.getChildren().addFirst(component);
    }

    public void setTaskManagement(TaskManagement taskManagement) {
        this.taskManagement = taskManagement;
    }

    private LocalTime parseTime(String input) throws ParseException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
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
}