package dataAccess;

import businessLogic.TaskManagement;

import java.io.*;

public class AppSerialization {

    public static void serializeData(TaskManagement taskManagement) throws IOException {
        FileOutputStream fos = new FileOutputStream("task_management.dat");
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(taskManagement);
        oos.flush();
        oos.close();
    }

    public static TaskManagement deserializeData() throws IOException, ClassNotFoundException {
        FileInputStream fis = new FileInputStream("task_management.dat");
        ObjectInputStream ois = new ObjectInputStream(fis);

        TaskManagement taskManagement = (TaskManagement) ois.readObject();

        ois.close();

        return taskManagement;
    }
}
