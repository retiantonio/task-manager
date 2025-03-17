package businessLogic;

import dataModel.ComplexTask;
import dataModel.Employee;
import dataModel.SimpleTask;
import dataModel.Task;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;

public class TaskManagement implements Serializable {

    @Serial
    private static final long serialVersionUID = 1234L;

    private Map<Employee, List<Task>> hashMapTasks = new HashMap<>();
    private Map<SimpleTask, List<Employee>> simpleTaskEmployeeDataStore = new HashMap<>();

    private int idGeneratorEmployee = 0;
    private int idGeneratorTask = 0;

    public TaskManagement() {}

    public void assignTaskToEmployee(Employee employee, Task task) {
        hashMapTasks.computeIfAbsent(employee, k -> new ArrayList<Task>());

        if(!hashMapTasks.get(employee).contains(task))
            hashMapTasks.get(employee).add(task);
    }

    public void removeTaskFromEmployee(Employee employee, Task task) {
        hashMapTasks.get(employee).remove(task);
    }

    public void storeEmployeeSimpleTask(Employee employee, SimpleTask task) {
        simpleTaskEmployeeDataStore.computeIfAbsent(task, _ -> new ArrayList<>());

        if(!simpleTaskEmployeeDataStore.get(task).contains(employee))
            simpleTaskEmployeeDataStore.get(task).add(employee);
    }

    public List<Employee> getEmployeesStoredForSimpleTask(SimpleTask task) {
        return simpleTaskEmployeeDataStore.get(task);
    }

    public int calculateEmployeeWorkDuration(Employee employee) {
        int workDuration = -1;

        List<Task> tasks = hashMapTasks.get(employee);

        if (tasks != null)
            for (Task task : tasks)
                if (task.getStatusTask().equals("Completed"))
                    workDuration += task.estimateDuration();


        employee.setWorkDuration(workDuration);
        return workDuration;
    }

//    public void modifyTaskStatus(int idEmployee, int idTask) {
//        Employee employee = getEmployee(idEmployee);
//
//        List<Task> tasks = hashMapTasks.get(employee);
//
//        if(tasks != null)
//            for(Task task : tasks)
//                if(task.getIDTask() == idTask)
//                    task.modifyTaskStatus();
//    }

//    private Employee getEmployee(int idEmployee) {
//        Employee employee = hashMapEmployees.get(idEmployee);
//
//        if (employee != null)
//            return employee;
//        return null;
//    }

    public void addEmployee(Employee employee) {
            hashMapTasks.put(employee, new ArrayList<>());
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

    public  Map<Employee, List<Task>> getTaskMap() {
        return this.hashMapTasks;
    }

    public List<Task> getTasks() {
        List<Task> allTasks = new ArrayList<>();
        Set<Task> duplicateCollision = new HashSet<>();

        for(List<Task> taskList: hashMapTasks.values())
            for(Task task: taskList)
                if(duplicateCollision.add(task))
                    allTasks.add(task);

        return allTasks;
    }

    public List<Task> recursiveGetComplexSubTasks(List<Task> tasks) {
        List<Task> restoreList = new ArrayList<>();

        for(Task task: tasks)
            if(task instanceof ComplexTask) {
                restoreList.addAll(recursiveGetComplexSubTasks(((ComplexTask) task).getSubTasks()));
                restoreList.add(task);
            }

        return restoreList;
    }

    public List<Employee> getEmployeesWithTask(Task task) {
        List<Employee> employeesWithTask = new ArrayList<>();

        for(Employee employee : hashMapTasks.keySet()) {
            if(hashMapTasks.get(employee).contains(task)) {
                employeesWithTask.add(employee);
            }
        }

        return employeesWithTask;
    }

    public int generateNewEmployeeID() {
        idGeneratorEmployee++;
        return idGeneratorEmployee;
    }

    public int generateNewTaskID() {
        idGeneratorTask++;
        return idGeneratorTask;
    }
}

