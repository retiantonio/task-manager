package com.example.assignment1;

import DataModel.Employee;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

public class EmployeeSelectController {

    @FXML private TableView<Employee> employeeTable;

    @FXML private TableColumn<Employee, Integer> IDColumn;
    @FXML private TableColumn<Employee, String> firstNameColumn;

    @FXML private Button selectButton;

    private Employee selectedEmployee;

    @FXML
    public void initialize() {
        IDColumn.setCellValueFactory(cellData -> cellData.getValue().idEmployeeProperty().asObject());
        firstNameColumn.setCellValueFactory(cellData -> cellData.getValue().nameEmployeeProperty());
    }

    public void setEmployeeList(ObservableList<Employee> employees) {
        employeeTable.setItems(employees);
    }

    public void confirmEmployee(ActionEvent event) {
        selectedEmployee = employeeTable.getSelectionModel().getSelectedItem();

        if(selectedEmployee != null)
            ((Stage) selectButton.getScene().getWindow()).close();
    }

    public Employee getSelectedEmployee() { return selectedEmployee; }


}
