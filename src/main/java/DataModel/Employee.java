package DataModel;

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
    private String nameEmployee;

    private static int id = 0;

    public Employee(String nameEmployee) {
        this.nameEmployee = nameEmployee;
        id++;
        idEmployee = id;
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
}
