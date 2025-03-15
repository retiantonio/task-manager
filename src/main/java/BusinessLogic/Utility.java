package BusinessLogic;

import DataModel.Employee;

import java.util.ArrayList;
import java.util.List;

public class Utility {

    public static List<Employee> employeeProductivitySort(TaskManagement taskManagement) {
        List<Employee> employees = taskManagement.getEmployees();
        List <Employee> sortedEmployees = new ArrayList<>(employees);




        return sortedEmployees;
    }
}
