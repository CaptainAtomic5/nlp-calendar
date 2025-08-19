import java.time.LocalTime;
// https://docs.oracle.com/javase/8/docs/api/java/time/LocalTime.html <-- documentation

// storing when each task begins and ends
public class Block {
    //Start time of each block
    private LocalTime startTime;
    //End time of each block
    private LocalTime endTime;
    // Type of the block: Deep work, busy, break
    private String type; 
    
    // Checks if task is assigned
    private Task task;

    public Block(LocalTime startTime, LocalTime endTime, String type) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.type = type;
        this.task = null;
    }

    // getters

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }
    
    public String getType() {
        return type;
    }

    public Task getTask() {
        return task;
    }
    
    public String getDisplayText() {
        if (task != null) {
            return startTime + " - " + endTime + ": " + task.getName();
        } else {
            return startTime + " - " + endTime + ": " + type;
        }
    }
    
    // setters

    // Allows for changing the type of block
    public void setType(String type) {
        this.type = type;
    }

    // Used to distinguish task and non-task blocks
    public boolean isTask() {
        return task != null;
    }

    // Assigns a task to block
    public void assignTask(Task task) {
        this.task = task;
        // Makes sure deep work has a different type but still has the same property of available to have tasks assigned to it
        if (!this.type.equals("Deep Work")) {
            this.type = "Scheduled";
        }
    }

    @Override
    public String toString() {
        return getDisplayText();
    }
}