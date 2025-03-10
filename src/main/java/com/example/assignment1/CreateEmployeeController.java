package com.example.assignment1;

import BusinessLogic.TaskManagement;
import DataModel.Employee;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class CreateEmployeeController {

    @FXML private TextField createEmployeeTF;

    private TaskManagement taskManagement;

    @FXML
    public void saveEmployee(ActionEvent event) {
        taskManagement.addEmployee(createEmployeeTF.getText());
    }

    public void setTaskManagement(TaskManagement taskManagement) {
        this.taskManagement = taskManagement;
    }

    public void setEmployeeName(String employeeName) {
        createEmployeeTF.setText(employeeName);
    }
}
