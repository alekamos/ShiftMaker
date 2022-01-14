package model;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Union task and owner ex task: night_shift_1 owner id2
 */
public class Shift implements Comparable<Shift>{

    private UUID id;
    private Task task;
    private Worker worker;

    public Task getTask() {
        return task;
    }

    public Worker getWorker() {
        return worker;
    }

    public void setWorker(Worker worker) {
        this.worker = worker;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    @Override
    public int compareTo(Shift ohterShift) {
        return this.task.compareTo(ohterShift.task);
    }
}
