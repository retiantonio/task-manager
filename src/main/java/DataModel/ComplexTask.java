package DataModel;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public final class ComplexTask extends Task implements Serializable {

    @Serial
    private static final long serialVersionUID = 713L;

    List<Task> subTasks = new ArrayList<>();

    public ComplexTask(String statusTask, String nameTask) {
        super(statusTask, nameTask);
    }

    public void addTask(Task task) {
        subTasks.add(task);
    }

    public void removeTask(Task task) {
        subTasks.remove(task);
    }

    //estimateDuration inherited

    public LocalTime getStartHour() {
        LocalTime startHour = subTasks.getFirst().startHour;

        for(int i = 0; i < subTasks.size(); i++) {
            if(subTasks.get(i).getStartHour().getHour() < startHour.getHour()) {
                startHour = subTasks.get(i).getStartHour();
            } else if(subTasks.get(i).getStartHour().getHour() == startHour.getHour()) {
                if(subTasks.get(i).getStartHour().getMinute() < startHour.getMinute()) {
                    startHour = subTasks.get(i).getStartHour();
                }
            }
        }

        return startHour;
    }

    public LocalTime getEndHour() {
        LocalTime endHour = subTasks.getFirst().endHour;

        for(int i = 0; i < subTasks.size(); i++) {
            if(subTasks.get(i).getStartHour().getHour() > endHour.getHour()) {
                endHour = subTasks.get(i).getStartHour();
            } else if(subTasks.get(i).getStartHour().getHour() == endHour.getHour()) {
                if(subTasks.get(i).getStartHour().getMinute() > endHour.getMinute()) {
                    endHour = subTasks.get(i).getStartHour();
                }
            }
        }

        return endHour;
    }
}
