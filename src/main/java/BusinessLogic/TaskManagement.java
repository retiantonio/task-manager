package BusinessLogic;

import DataModel.Employee;
import DataModel.Task;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;

public class TaskManagement implements Serializable {

    @Serial
    private static final long serialVersionUID = 1234L;

    private Map<Employee, List<Task>> hashMapTasks = new HashMap<>();
    private Map<Integer, Employee> hashMapEmployees = new HashMap<>();

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

    public Set<Employee> getEmployees() {
        return hashMapTasks.keySet();
    }

    public boolean employeeExists(String employeeName) {
        Set<Employee> employeeSet = getEmployees();

        if(!employeeSet.isEmpty())
            for(Employee employee : employeeSet) {
                if(employee.getNameEmployee().equals(employeeName))
                    return true;
            }

        return false;
    }

    public void printEmployees() {
        if(!hashMapTasks.keySet().isEmpty())
            for(Employee employee: hashMapTasks.keySet())
                System.out.println(employee.getNameEmployee());
        else System.out.println("No employee found");
    }

    public  Map<Employee, List<Task>> getTaskMap() {
        return this.hashMapTasks;
    }

    public List<Task> getTasks() {
        List<Task> allTasks = new ArrayList<>();

        for(List<Task> taskList: hashMapTasks.values()) {
            allTasks.addAll(taskList);
        }

        return allTasks;
    }

    public List<Employee> getEmployeesWithTask(Task task) {
        List<Employee> employeesWithTask = new ArrayList<>();

        for(Map.Entry<Employee, List<Task>> entry : hashMapTasks.entrySet()) {
            if(entry.getValue().contains(task)) {
                employeesWithTask.add(entry.getKey());
            }
        }

        return employeesWithTask;
    }
}

