package dataModel;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.Serial;
import java.io.Serializable;
import java.time.Duration;
import java.time.LocalTime;
import java.util.Objects;

public abstract sealed class Task implements Serializable permits SimpleTask, ComplexTask {

    @Serial
    private static final long serialVersionUID = 234L;

    protected int idTask;

    protected String statusTask;
    protected String nameTask;

    protected LocalTime startHour;
    protected LocalTime endHour;

    public Task(int idTask, String statusTask, String nameTask) {
        this.idTask = idTask;
        this.statusTask = statusTask;
        this.nameTask = nameTask;
    }

    public int estimateDuration() {
        Duration duration = Duration.between(getStartHour(), getEndHour());
        //return the minutes
        return (int) duration.toMinutes();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(idTask);
    }

    public void modifyTaskStatus() {
        if(statusTask.equals("Uncompleted"))
            statusTask = "Completed";
    }

    public void setTaskName(String nameTask) {
        this.nameTask = nameTask;
    }

    public void setStartHour(LocalTime startHour) {
        this.startHour = startHour;
    }

    public void setEndHour(LocalTime endHour) {
        this.endHour = endHour;
    }

    public LocalTime getStartHour() {
        return startHour;
    }

    public LocalTime getEndHour() {
        return endHour;
    }

    public String getStatusTask() {
        return statusTask;
    }

    public String getNameTask() { return nameTask; }

    public IntegerProperty idTaskProperty() {
        IntegerProperty idTaskProperty = new SimpleIntegerProperty(idTask);
        return idTaskProperty;
    }

    public StringProperty nameTaskProperty() {
        SimpleStringProperty nameTaskProperty = new SimpleStringProperty(nameTask);
        return nameTaskProperty;
    }


    public int getIDTask() {
        return idTask;
    }
}
