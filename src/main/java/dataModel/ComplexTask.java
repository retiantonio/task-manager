package dataModel;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public final class ComplexTask extends Task implements Serializable {

    @Serial
    private static final long serialVersionUID = 713L;

    private List<Task> subTasks = new ArrayList<>();

    public ComplexTask(int idTask, String statusTask, String nameTask) {
        super(idTask, statusTask, nameTask);
    }

    public void addTask(Task task) {
        if(!subTasks.contains(task))
            subTasks.add(task);
    }

    public void removeTask(Task task) {
        subTasks.remove(task);
    }

    //estimateDuration inherited

    public LocalTime getStartHour() {
        LocalTime startHour = subTasks.getFirst().getStartHour();

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
        LocalTime endHour = subTasks.getFirst().getEndHour();

        for(int i = 0; i < subTasks.size(); i++) {
            if(subTasks.get(i).getEndHour().getHour() > endHour.getHour()) {
                endHour = subTasks.get(i).getEndHour();
            } else if(subTasks.get(i).getEndHour().getHour() == endHour.getHour()) {
                if(subTasks.get(i).getEndHour().getMinute() > endHour.getMinute()) {
                    endHour = subTasks.get(i).getEndHour();
                }
            }
        }

        return endHour;
    }

    public List<Task> getSubTasks() {
        return subTasks;
    }

    public void modifyTaskStatus() {
        if(statusTask.equals("Uncompleted"))
            statusTask = "Completed";

        for(Task task : subTasks)
            task.modifyTaskStatus();
    }
}
