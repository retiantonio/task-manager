package DataModel;

import java.io.Serial;
import java.io.Serializable;
import java.time.Duration;
import java.time.LocalTime;
import java.util.Objects;

public abstract sealed class Task implements Serializable permits SimpleTask, ComplexTask {

    @Serial
    private static final long serialVersionUID = 234L;

    protected int idTask;
    private static int id = 0;

    protected String statusTask;
    protected String nameTask;

    protected LocalTime startHour;
    protected LocalTime endHour;

    public Task(String statusTask, String nameTask) {
        id++;
        this.idTask = id;

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

    public int getIDTask() {
        return idTask;
    }

    public void modifyTaskStatus() {
        if(statusTask.equals("Completed"))
            statusTask = "Uncompleted";
        else statusTask = "Completed";
    }
}
