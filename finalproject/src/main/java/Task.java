public class Task {
    // Name of task
    private String name;
    // Duration of task in hours (can be a fraction like 1.5)
    private double duration;
    // Priority level (1 = highest, 4 = lowest)
    private int priority; 
    private boolean isImportant;
    private boolean isUrgent;
    private boolean isDeepWork;

    public Task(String name, double duration, boolean isImportant, boolean isUrgent, int priority) {
        this.name = name;
        this.duration = duration;
        this.isImportant = isImportant;
        this.isUrgent = isUrgent;   
        this.priority = priority;    
    }

    // since this determines priority (through Eisenhower Matrix), no need to ask user to rank it's priority
    public void assignPriority() {
        if (isImportant && isUrgent) {
            priority = 1;
        } 
        else if (!isImportant && isUrgent){
            priority = 2;
        }
        else if (isImportant && !isUrgent) {
            priority = 3;
        }
        else {
            priority = 4;
        }
    }

    //getters
    public String getName() {
        return name;
    }

    public double getDuration() {
        return duration;
    }

    public int getPriority() {
        return priority;
    }

    @Override
    public String toString() {
        return "Task Name: " + name + " | Duration: " + duration + " hrs" + " | Priority: " + priority + " | Important: " + isImportant + " | Urgent: " + isUrgent;
    }
}