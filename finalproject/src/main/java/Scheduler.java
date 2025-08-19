import java.util.*;

import javafx.application.Application;
import javafx.scene.Scene;
// https://docs.oracle.com/javase/8/javafx/api/javafx/scene/Scene.html
// https://docs.oracle.com/javase/8/javafx/api/javafx/stage/Stage.html
// these are where we found out how to implement the different pop up 'stages'
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class Scheduler extends Application {

    // creates arraylist to store the user's tasks
    public static ArrayList<Task> taskList = new ArrayList<>();

    // creates arraylist to store the times that the user is available 
    public static ArrayList<Integer> availabilityList = new ArrayList<>();

    public void start(Stage primaryStage) {

        // these are the input fiels. this is kinda cool because it looks similar to how inputs
        // are taken in HTML. "#throwback" - lalit
        Label taskLabel = new Label("Task Name:");
        TextField taskField = new TextField();

        Label durationLabel = new Label("Duration (minutes):");
        TextField durationField = new TextField();

        // this is the eisenhower structure that defines the following possibilities:
        // important and urgent - highest priority and needs to be done NOQ
        // not important but urgent - usually just time-bounded busy work
        // important but not urgent - high priority, but can be put off for some time
        // not important and not urgent - not prioritized (as shown in task.java) and can be put off till much later
        // also defined in task.java
        CheckBox importantBox = new CheckBox("Important");
        CheckBox urgentBox = new CheckBox("Urgent");

        // just the button!
        Button addTaskButton = new Button("Add Task");

        ListView<String> taskView = new ListView<>();

        Label timeLabel = new Label("Select available times:");
        ListView<CheckBox> timeView = new ListView<>();

        // The available times for the "working day" of 9-5.
        for (int i = 9; i < 17; i++) {
            String timeSlot = i + ":00 - " + (i + 1) + ":00";
            CheckBox cb = new CheckBox(timeSlot);
            timeView.getItems().add(cb);
        }

        Button finishButton = new Button("Generate Schedule");

        // this is just the dimensions and padding
        GridPane pane = new GridPane();
        pane.setVgap(5);
        pane.setHgap(5);

        pane.add(taskLabel, 0, 0);
        pane.add(taskField, 1, 0);
        pane.add(durationLabel, 0, 1);
        pane.add(durationField, 1, 1);
        pane.add(importantBox, 0, 2);
        pane.add(urgentBox, 1, 2);
        pane.add(addTaskButton, 0, 3);
        pane.add(taskView, 0, 4, 2, 1);
        pane.add(timeLabel, 0, 5);
        pane.add(timeView, 0, 6, 2, 1);
        pane.add(finishButton, 0, 7);

        // Button action to add task to the pool below
        addTaskButton.setOnAction(new javafx.event.EventHandler<javafx.event.ActionEvent>() {
            public void handle(javafx.event.ActionEvent event) {

                //This grabs the info from the fields that the user inputs to
                String name = taskField.getText().trim();
                String dur = durationField.getText().trim();
                boolean imp = importantBox.isSelected();
                boolean urg = urgentBox.isSelected();

                // this doesn't activate if a blank task to be created or a task with no time associated
                if (!name.isEmpty() && !dur.isEmpty()) {

                    //turns the minutes input into hours so that we can quantify the number of blocks
                    double durationHours = Double.parseDouble(dur) / 60.0;
                    Task t = new Task(name, durationHours, imp, urg, 0);
                    t.assignPriority();
                    taskList.add(t);
                    taskView.getItems().add(t.toString());
                    //clears all the inputs
                    taskField.clear();
                    durationField.clear();
                    importantBox.setSelected(false);
                    urgentBox.setSelected(false);
                }
            }
        });

        // Button to generate schedul
        finishButton.setOnAction(new javafx.event.EventHandler<javafx.event.ActionEvent>() {
            public void handle(javafx.event.ActionEvent event) {

                // this is where we get info on what times we can use
                for (CheckBox cb : timeView.getItems()) {
                    if (cb.isSelected()) {
                        String timeRange = cb.getText();
                        int hour = Integer.parseInt(timeRange.split(":")[0]);
                        availabilityList.add(hour);
                    }
             }
            //the first window closes to open the calendarfx view
            primaryStage.close();
            Generator.generateSchedule(taskList, availabilityList);
            }
        }
    );
    
    Scene scene = new Scene(pane, 400, 500);
    primaryStage.setTitle("Scheduler");
    primaryStage.setScene(scene);
    primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}