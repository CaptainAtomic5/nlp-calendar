import java.util.*;

import javafx.stage.Stage;

import java.time.LocalTime;

public class Generator {

    public static void generateSchedule(ArrayList<Task> tasks, ArrayList<Integer> availableHours) {
        ArrayList<Block> blocks = new ArrayList<>();

        // Create hourly blocks from 9 to 18 (9 am to 5 pm)
        for (int i = 9; i < 17; i++) {
            LocalTime start = LocalTime.of(i, 0);
            LocalTime end = LocalTime.of(i + 1, 0);

            // By default the block should be marked busy unless checked to be available
            String type = "Busy";
            // Defines deep work hours
            if (availableHours.contains(i)) {
                if (i == 10 || i == 11 || i == 14 || i == 15) {
                    type = "Deep Work";
                } else {
                    type = "Available";
                }
            }

            blocks.add(new Block(start, end, type));
        }

        // Sort tasks by priority
        // 1 is the highest priortity, 4 is the lowest priority
        for (int i = 0; i < tasks.size(); i++) {
            for (int j = i + 1; j < tasks.size(); j++) {
                if (tasks.get(i).getPriority() > tasks.get(j).getPriority()) {
                    // uses temporary variable to switch task with the highest priority
                    Task temp = tasks.get(i);
                    tasks.set(i, tasks.get(j));
                    tasks.set(j, temp);
                }
            }
        }

        // Creating a new array list so that we can combine the break blocks with the task blocks
        ArrayList<Block> finalBlocks = new ArrayList<>();
        
        // Assigns tasks to blocks that are available based on user input
        for (int t = 0; t < tasks.size(); t++) {
            Task task = tasks.get(t);
            
            // Calculates the number of hourly blocks needed for all the tasks based on their duration
            int blocksNeeded = (int) Math.round(task.getDuration());
            int count = 0;
            
            // Searches for available blocks to assign the task
            for (int b = 0; b < blocks.size(); b++) {
                Block block = blocks.get(b);
                
                // Tasks can only be assigned to either deep work or available type blocks, not to busy blocks
                if ((block.getType().equals("Available") || block.getType().equals("Deep Work")) && block.getTask() == null) {
                    block.assignTask(task);
                    count++;

                    // After the blocks have been assigned, insert a break that's 15 mins long
                    if (count == blocksNeeded) {
                        Block breakBlock = new Block(block.getEndTime(), block.getEndTime().plusMinutes(15), "Break");
                        finalBlocks.add(breakBlock);
                        break;
                    }
                }
            }
        }

        // Adds break blocks to overall schedule
        blocks.addAll(finalBlocks);

        // Passes onthe list of blocks to the calendar for it to display
        CalView.blocks = blocks;

        // Launch the calendar view in a new window if there are no bugs
        try {
            CalView calendarView = new CalView();
            Stage stage = new Stage();
            calendarView.start(stage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
