package dataModel;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalTime;

public final class SimpleTask extends Task implements Serializable {

    @Serial
    private static final long serialVersionUID = 334L;

    public SimpleTask(int idTask, String statusTask, String nameTask, LocalTime startHour, LocalTime endHour) {
        super(idTask, statusTask, nameTask);

        this.startHour = startHour;
        this.endHour = endHour;
    }

    //estimateDuration is inherited
    //getStartHour inherited
    //getEndHour inherited
}
