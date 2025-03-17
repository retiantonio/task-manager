package com.example.assignment1;

import businessLogic.TaskManagement;
import businessLogic.Utility;
import dataModel.Employee;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.util.Map;

public class CreateEmployeeController {

    @FXML private TextField createEmployeeTF;

    @FXML private Label createEmployeeWorkDurationLabel;
    @FXML private Label createEmployeeUncompletedLabel;
    @FXML private Label createEmployeeCompletedLabel;

    private TaskManagement taskManagement;

    private Employee employeeObject;

    private boolean isExisting = false;

    private Node componentReference;


    @FXML
    public void saveEmployee(ActionEvent event) {
        if(isExisting)
            employeeObject.setNameEmployee(createEmployeeTF.getText());
        else {
            if(!taskManagement.employeeExists(createEmployeeTF.getText())) {
                employeeObject = new Employee(taskManagement.generateNewEmployeeID(), createEmployeeTF.getText());
                taskManagement.addEmployee(employeeObject);
            }
            isExisting = true;
        }
    }

    public void setTaskManagement(TaskManagement taskManagement) {
        this.taskManagement = taskManagement;
    }

    public void restoreEmployee(Employee employee) {
        isExisting = true;

        employeeObject = employee;

        createEmployeeTF.setText(employee.getNameEmployee());

        int workDuration = employee.getWorkDuration();
        int hoursDuration = workDuration / 60;
        int minutesDuration = workDuration % 60;

        createEmployeeWorkDurationLabel.setText(String.format("%d hours : %d minutes", hoursDuration, minutesDuration));
    }

    public void updateEmployeeTag() {
        if(isExisting) {
            int workDuration = taskManagement.calculateEmployeeWorkDuration(employeeObject);
            int hoursDuration = workDuration / 60;
            int minutesDuration = workDuration % 60;

            createEmployeeWorkDurationLabel.setText(String.format("%d hours : %d minutes", hoursDuration, minutesDuration));

            Map<String, Integer> completedUncompleted = Utility.numberOfCompletedAndUncompleted(taskManagement, employeeObject);

            createEmployeeCompletedLabel.setText(String.valueOf(completedUncompleted.get("Completed")));
            createEmployeeUncompletedLabel.setText(String.valueOf(completedUncompleted.get("Uncompleted")));
        }
    }

    public void setComponentReference(Node componentReference) {
        this.componentReference = componentReference;
    }

    public Node getComponentReference() {
        return componentReference;
    }
}
