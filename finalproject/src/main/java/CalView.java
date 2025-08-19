import java.util.*;

// https://github.com/dlsc-software-consulting-gmbh/CalendarFX
import com.calendarfx.view.CalendarView;
import com.calendarfx.model.*;
import com.calendarfx.model.Calendar;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class CalView extends Application {
    // Contains all the final time blocks
    public static List<Block> blocks;
    

    @Override
    public void start(Stage primaryStage) {
        // Creates three calendars for each type of block which is useful for seperating tasks
        Calendar availableCal = new Calendar("Available Time");
        availableCal.setStyle(Calendar.Style.STYLE1);

        Calendar taskCal = new Calendar("Scheduled Tasks");
        taskCal.setStyle(Calendar.Style.STYLE2);

        Calendar deepWorkCal = new Calendar("Deep Work");
        deepWorkCal.setStyle(Calendar.Style.STYLE3);

        // Lops through all the generated blocks and look at their type to put them into their respective cal
        for (Block block : blocks) {
            String label;

            // Deep Work block
            if (block.getType().equals("Deep Work")) {
                // If the deep work block has a task add a name to it's calendar block
                if (block.isTask() && block.getTask() != null) {
                    label = "Deep Work: " + block.getTask().getName();   
                } 
                // If the deep work block does not have a task, just leave the calendar block name as Deep Work
                else {
                    label = "Deep Work";
                }
        
                Entry<String> entry = new Entry<>(label);
                entry.setInterval(block.getStartTime(), block.getEndTime());
                // Adds entry to the deep work calendar
                deepWorkCal.addEntry(entry);
            }

            // Available block with task
            else if (block.isTask() && block.getTask() != null) {
                label = block.getTask().getName();
                Entry<String> entry = new Entry<>(label);
                entry.setInterval(block.getStartTime(), block.getEndTime());
                // Adds entry to the available calendar
                taskCal.addEntry(entry);
            }

            // Non-task blocks (just Available or Busy)
            else {
                label = block.getType();
                Entry<String> entry = new Entry<>(label);
                entry.setInterval(block.getStartTime(), block.getEndTime());
                // Marks time block as available but does't assign a task and appends to available calendar
                availableCal.addEntry(entry);
            }
        }

        // Groups all calendars into a "source" whcih is just a compilation
        CalendarSource source = new CalendarSource("My Calendars");
        source.getCalendars().addAll(availableCal, taskCal, deepWorkCal);

        // Create CalendarFX view and configure its appearance
        CalendarView calendarView = new CalendarView();
        calendarView.getCalendarSources().add(source);
        calendarView.setRequestedTime(LocalDateTime.now().toLocalTime());

        // This is just for visual appeal
        // What this does is limits the amount of time in the 24 hour day that you can put things into
        // I didn't want to be able to accidentally place a task at 1am
        // This means you can only put tasks from 8 AM to 8 PM
        calendarView.setStartTime(java.time.LocalTime.of(8, 0));
        calendarView.setEndTime(java.time.LocalTime.of(20,0));

        // Sets up the window with the calendar
        Scene scene =  new Scene(calendarView);
        primaryStage.setScene(scene);
        primaryStage.setTitle("CalendarFX View");
        primaryStage.setWidth(1000);
        primaryStage.setHeight(800);
        primaryStage.show();
    }
}