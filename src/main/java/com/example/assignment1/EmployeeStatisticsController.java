package com.example.assignment1;

import businessLogic.TaskManagement;
import businessLogic.Utility;
import dataModel.Employee;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;

import java.util.Map;

public class EmployeeStatisticsController {

    @FXML private Label employeeStatisticsNameLabel;
    @FXML private Label employeeStatisticsRankLabel;
    @FXML private Label employeeStatisticsWorkDurationLabel;
    @FXML private Label employeeStatisticsCompletedLabel;
    @FXML private Label employeeStatisticsUncompletedLabel;

    private Employee employeeObject;

    private TaskManagement taskManagement;

    private Node componentReference;

    public void updateEmployeeTag() {
        int workDuration = employeeObject.getWorkDuration();
        int hoursDuration = workDuration / 60;
        int minutesDuration = workDuration % 60;

        employeeStatisticsWorkDurationLabel.setText(String.format("%d hours : %d minutes", hoursDuration, minutesDuration));
    }

    public void restoreEmployeeStatistics(Employee employee, int index, int length) {
        employeeObject = employee;

        employeeStatisticsNameLabel.setText(employee.getNameEmployee());

        int workDuration = employee.getWorkDuration();
        int hoursDuration = workDuration / 60;
        int minutesDuration = workDuration % 60;

        employeeStatisticsRankLabel.setText("#" + (length - index));
        employeeStatisticsWorkDurationLabel.setText(String.format("%d hours : %d minutes", hoursDuration, minutesDuration));

        Map<String, Integer> completedUncompleted = Utility.numberOfCompletedAndUncompleted(taskManagement, employee);

        employeeStatisticsCompletedLabel.setText(String.valueOf(completedUncompleted.get("Completed")));
        employeeStatisticsUncompletedLabel.setText(String.valueOf(completedUncompleted.get("Uncompleted")));
    }

    public void setComponentReference(Node componentReference) {
        this.componentReference = componentReference;
    }

    public Node getComponentReference() {
        return componentReference;
    }

    public void setTaskManagement(TaskManagement taskManagement) {
        this.taskManagement = taskManagement;
    }
}
