package businessLogic;

import dataModel.Employee;
import dataModel.Task;

import java.util.*;

public class Utility {

    public static List<Employee> employeeProductivitySort(TaskManagement taskManagement) {
       List<Employee> allEmployees = new ArrayList<>(taskManagement.getEmployees());
       List<Employee> sortedEmployees = new ArrayList<>(allEmployees);

       for(Employee employee: allEmployees)
           if(taskManagement.calculateEmployeeWorkDuration(employee) <= 2400)
               sortedEmployees.remove(employee);

       sortedEmployees.sort(Comparator.comparingInt(Employee::getWorkDuration));

       return sortedEmployees;
    }

    public static Map<String, Integer> numberOfCompletedAndUncompleted(TaskManagement taskManagement, Employee employee) {
            //initialize
        Map<String, Integer> completedAndUncompleted = new HashMap<>();

        int numberOfCompleted = 0;
        int numberOfUncompleted = 0;

        completedAndUncompleted.put("Uncompleted", numberOfUncompleted);
        completedAndUncompleted.put("Completed", numberOfCompleted);

        for(Task task : taskManagement.getTaskMap().get(employee)) {
            if(task.getStatusTask().equals("Completed")) {
                completedAndUncompleted.put("Completed", ++numberOfCompleted);
            } else {
                completedAndUncompleted.put("Uncompleted", ++numberOfUncompleted);
            }
        }

        return completedAndUncompleted;
    }
}
