package dataModel;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

public class Employee implements Serializable {

    @Serial
    private static final long serialVersionUID = 449L;

    private int idEmployee;
    private int workDuration = -2;

    private String nameEmployee;

    public Employee(int idEmployee, String nameEmployee) {
        this.nameEmployee = nameEmployee;
        this.idEmployee = idEmployee;
    }

    public String getNameEmployee() {
        return nameEmployee;
    }

    public int getIdEmployee() { return idEmployee; }

    public IntegerProperty idEmployeeProperty() {
        IntegerProperty idEmployeeProperty = new SimpleIntegerProperty(idEmployee);
        return idEmployeeProperty;
    }

    public StringProperty nameEmployeeProperty() {
        SimpleStringProperty nameEmployeeProperty = new SimpleStringProperty(nameEmployee);
        return nameEmployeeProperty;
    }

    public int hashCode() {
        return Objects.hash(nameEmployee);
    }

    public boolean equals(Object obj) {
        if(this == obj)
            return true;

        if(obj == null || this.getClass() != obj.getClass())
            return false;

        return Objects.equals(nameEmployee, ((Employee)obj).nameEmployee);
    }

    public void setWorkDuration(int workDuration) {
        this.workDuration = workDuration;
    }

    public int getWorkDuration() {
        return workDuration;
    }

    public void setNameEmployee(String nameEmployee) {
        this.nameEmployee = nameEmployee;
    }
}
