package com.example.assignment1;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class EmployeeController {

    @FXML private Label employeeLabel;

    public void setEmployeeLabel(String employeeName) {
        employeeLabel.setText(employeeName);
    }
}
