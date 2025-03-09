package BusinessLogic;

import DataModel.Employee;
import DataModel.Task;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;

public class TaskManagement implements Serializable {

    @Serial
    private static final long serialVersionUID = 1234L;

    private Map<Employee, List<Task>> hashMapTasks = new HashMap<Employee, List<Task>>();
    private Map<Integer, Employee> hashMapEmployees = new HashMap<Integer, Employee>();

    public TaskManagement() {}

    public void assignTaskToEmployee(Employee employee, Task task) {
        hashMapTasks.computeIfAbsent(employee, k -> new ArrayList<Task>()).add(task);
    }

    public int calculateEmployeeWorkDuration(int idEmployee) {
        int workDuration = -1;

        Employee employee = getEmployee(idEmployee);
        List<Task> tasks = hashMapTasks.get(employee);

        if (tasks != null)
            for (Task task : tasks)
                if (task.getStatusTask().equals("Completed"))
                    workDuration += task.estimateDuration();

        return workDuration;
    }

    public void modifyTaskStatus(int idEmployee, int idTask) {
        Employee employee = getEmployee(idEmployee);

        List<Task> tasks = hashMapTasks.get(employee);

        if(tasks != null)
            for(Task task : tasks)
                if(task.getIDTask() == idTask)
                    task.modifyTaskStatus();
    }

    private Employee getEmployee(int idEmployee) {
        Employee employee = hashMapEmployees.get(idEmployee);

        if (employee != null)
            return employee;
        return null;
    }

    public void addEmployee(String newEmployeeName) {
        if(!employeeExists(newEmployeeName)) {
            Employee newEmployee = new Employee(newEmployeeName);
            hashMapTasks.put(newEmployee, new ArrayList<>());
        }
    }

    public Set<Employee> getEmployeeSet() {
        return hashMapTasks.keySet();
    }

    public boolean employeeExists(String employeeName) {
        Set<Employee> employeeSet = getEmployeeSet();

        if(!employeeSet.isEmpty())
            for(Employee employee : employeeSet) {
                if(employee.getNameEmployee().equals(employeeName))
                    return true;
            }

        return false;
    }
}

