package cs3500.pa05.controller;

import cs3500.pa05.model.Week;
import cs3500.pa05.model.assignments.Assignment;
import cs3500.pa05.model.assignments.Event;
import cs3500.pa05.model.assignments.Task;
import cs3500.pa05.model.day.Days;
import cs3500.pa05.model.reader.BujoFileReader;
import cs3500.pa05.model.reader.FileReader;
import cs3500.pa05.model.writer.BujoFileWriter;
import cs3500.pa05.view.CreateCategoryView;
import cs3500.pa05.view.EditEventView;
import cs3500.pa05.view.EditTaskView;
import cs3500.pa05.view.EventPopUpView;
import cs3500.pa05.view.NewEventView;
import cs3500.pa05.view.NewNoteQuoteView;
import cs3500.pa05.view.NewTaskView;
import cs3500.pa05.view.PasswordPromptView;
import cs3500.pa05.view.SetMaximumView;
import cs3500.pa05.view.TaskPopUpView;
import cs3500.pa05.view.TaskSearchView;
import java.io.File;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * main controller for a java journal
 */
public class JavaJournalController implements Controller {

  private Week week;

  @FXML
  private Label newWeek;

  @FXML
  private TextField weekName;

  @FXML
  private Button newEvent;

  @FXML
  private Label taskWarning;

  @FXML
  private Label eventWarning;

  @FXML
  private Button newTask;

  @FXML
  private Button setMaxEventsTasks;

  @FXML
  private Button newNote;

  @FXML
  private VBox notesContent;

  @FXML
  private Button createCategory;

  @FXML
  private VBox sundayContent;

  @FXML
  private VBox mondayContent;

  @FXML
  private VBox tuesdayContent;

  @FXML
  private VBox wednesdayContent;

  @FXML
  private VBox thursdayContent;

  @FXML
  private VBox fridayContent;

  @FXML
  private VBox saturdayContent;

  @FXML
  private Label totalEvents;

  @FXML
  private Label totalTasks;

  @FXML
  private Label tasksCompleted;

  @FXML
  private Button saveFile;

  @FXML
  private Button openFile;

  @FXML
  private Button showTaskQueue;

  @FXML
  private Label weeksTasks;

  @FXML
  private VBox tasksBox;

  @FXML
  private SplitPane sideBar;

  @FXML
  private Button saveName;

  @FXML
  private Button taskSearch;

  @FXML
  private Button savePassword;

  @FXML
  private TextField password;

  private Stage primaryStage;


  /**
   * Constructor for a java journal controller
   *
   * @param w     the week currently being displayed and edited
   * @param stage the main stage of the application
   */
  public JavaJournalController(Week w, Stage stage) {
    this.week = w;
    this.primaryStage = stage;
  }


  /**
   * runs the controller
   *
   * @throws IllegalStateException if the week is null
   */
  @Override
  public void run() throws IllegalStateException {
    initButtons();
  }

  /**
   * initializes all buttons in the week view
   */
  private void initButtons() {
    initNewEvent();
    initNewTask();
    initSetMax();
    initNewNote();
    initCreateCategory();
    initSaveFile();
    initShowSideBar();
    initOpenFile();
    initSaveName();
    initTaskSearch();
    initSavePassword();
  }

  /**
   * initializes the save password button
   */
  private void initSavePassword() {
    savePassword.setOnAction(event -> {
      try {
        String weekPassword = password.getText();
        if (!weekPassword.equals("") && weekPassword != null) {
          week.setPassword(weekPassword);
          password.clear();
        }
      } catch (IllegalStateException exc) {
        System.err.println("Unable to Save Password.");
      }
    });
  }

  /**
   * initializes the button to start searching for a task
   */
  private void initTaskSearch() {
    taskSearch.setOnAction(event -> {
      try {
        Stage stage = new Stage();
        TaskSearchController controller = new TaskSearchController(week);
        TaskSearchView view = new TaskSearchView(controller);
        stage.setTitle("Search for a Task");
        stage.setScene(view.load());
        controller.run();
        stage.show();
      } catch (IllegalStateException exc) {
        System.err.println("Unable to load GUI.");
      }
    });
  }

  /**
   * Formats a button to be only underlined with no background
   *
   * @param button the button to be formatted
   */
  private void formatButton(Button button) {
    button.setPrefWidth(300);
    button.setStyle("-fx-background-color: transparent; -fx-border-color: "
        +
        "transparent; -fx-underline: true;");
  }

  /**
   * updates the week display based on the new assignment
   *
   * @param assignment the assignment being added to the week view
   */
  public void updateAssignmentDisplay(Assignment assignment) {
    Days day = week.determineDay(assignment);
    Button button = new Button(assignment.getName());
    formatButton(button);
    if (day.equals(Days.MONDAY)) {
      initAssignmentDisplayData(button, mondayContent, assignment);
    } else if (day.equals(Days.TUESDAY)) {
      initAssignmentDisplayData(button, tuesdayContent, assignment);
    } else if (day.equals(Days.WEDNESDAY)) {
      initAssignmentDisplayData(button, wednesdayContent, assignment);
    } else if (day.equals(Days.THURSDAY)) {
      initAssignmentDisplayData(button, thursdayContent, assignment);
    } else if (day.equals(Days.FRIDAY)) {
      initAssignmentDisplayData(button, fridayContent, assignment);
    } else if (day.equals(Days.SATURDAY)) {
      initAssignmentDisplayData(button, saturdayContent, assignment);
    } else if (day.equals(Days.SUNDAY)) {
      initAssignmentDisplayData(button, sundayContent, assignment);
    }
    if (assignment instanceof Event) {
      initEventDisplay(button, (Event) assignment);
    } else if (assignment instanceof Task) {
      initTaskDisplay(button, (Task) assignment);
    }
  }

  /**
   * initializes the data to be displayed in a week with an assignment
   *
   * @param content the vbox the data will be added to
   * @param a       the assignment whose data will be added
   */
  private void initAssignmentDisplayData(Button button, VBox content, Assignment a) {
    Button editButton = new Button("Edit");
    formatButton(editButton);
    Label description = new Label("Description: " + a.getDescription());
    description.setWrapText(true);
    VBox assignmentBox = new VBox();
    Label category = new Label("Category: " + a.getCategory());
    category.setWrapText(true);
    assignmentBox.getChildren().addAll(button, editButton, description, category);
    content.getChildren().add(assignmentBox);
    if (a instanceof Event) {
      initEventData(assignmentBox, (Event) a);
      initEditEventDisplay(editButton, (Event) a, assignmentBox);
    } else if (a instanceof Task) {
      initEditTaskDisplay(editButton, (Task) a, assignmentBox);
    }
  }

  /**
   * initializes the edit button for an event
   *
   * @param button the edit button
   * @param e the event associated with the button
   * @param eventBox the week view of the event
   */
  private void initEditEventDisplay(Button button, Event e, VBox eventBox) {
    button.setOnAction(event -> {
      try {
        Stage stage = new Stage();
        EditEventController controller = new EditEventController(e, stage, eventBox, week, this);
        EditEventView view = new EditEventView(controller);
        stage.setTitle("Edit Event");
        stage.setScene(view.load());
        controller.run();
        stage.show();
      } catch (IllegalStateException exc) {
        System.err.println("Unable to load GUI.");
      }
    });
  }

  /**
   * initializes the edit button for a task
   *
   * @param button the edit button
   * @param task the task associated with the button
   * @param taskBox the week view of the task
   */
  private void initEditTaskDisplay(Button button, Task task, VBox taskBox) {
    button.setOnAction(event -> {
      try {
        Stage stage = new Stage();
        EditTaskController controller = new EditTaskController(task, stage, taskBox, week, this);
        EditTaskView view = new EditTaskView(controller);
        stage.setTitle("Edit Task");
        stage.setScene(view.load());
        controller.run();
        stage.show();
      } catch (IllegalStateException exc) {
        System.err.println("Unable to load GUI.");
      }
    });
  }

  /**
   * initializes the data to be displayed in a week with an event
   *
   * @param assignmentBox the vbox the data will be added to
   * @param event         the event whose data will be added
   */
  private void initEventData(VBox assignmentBox, Event event) {
    Label startTime = new Label("StartTime: " + event.getStartTime());
    Label duration = new Label("Duration: " + event.getDuration());
    duration.setWrapText(true);
    assignmentBox.getChildren().addAll(startTime, duration);
  }

  /**
   * initializes the task pop up windows button for each task
   *
   * @param button the button attached to the task title
   * @param task   the task used to format the popup
   */
  public void initTaskDisplay(Button button, Task task) {
    if (task.isComplete()) {
      button.setText(task.getName() + ": Complete");
    } else {
      button.setText(task.getName() + ": Not Complete");
    }
    button.setOnAction(event -> {
      try {
        Stage stage = new Stage();
        TaskPopUpController controller = new TaskPopUpController(task, this, button);
        TaskPopUpView view = new TaskPopUpView(controller);
        stage.setTitle(task.getName());
        stage.setScene(view.load());
        controller.run();
        stage.show();
      } catch (IllegalStateException exc) {
        System.err.println("Unable to load GUI.");
      }
    });
  }

  /**
   * updates the display of a task's button for a popup view to show its now complete
   *
   * @param button the associated button for the popup for the task
   * @param task   the task whose title is being updated
   */
  public void updateButtonTitleAsComplete(Button button, Task task) {
    button.setText(task.getName() + ": Complete");
  }

  /**
   * initializes the event pop up windows button for each event
   *
   * @param button the button attached to the event title
   * @param e      the event used to format the popup
   */
  private void initEventDisplay(Button button, Event e) {
    button.setOnAction(event -> {
      try {
        Stage stage = new Stage();
        EventPopUpController controller = new EventPopUpController(e);
        EventPopUpView view = new EventPopUpView(controller);
        stage.setTitle(e.getName());
        stage.setScene(view.load());
        controller.run();
        stage.show();
      } catch (IllegalStateException exc) {
        System.err.println("Unable to load GUI.");
      }
    });
  }

  /**
   * Updates the name of the week
   */
  private void initSaveName() {
    saveName.setOnAction(event -> {
      week.setName(weekName.getText());
      newWeek.setText(weekName.getText());
      primaryStage.setTitle(weekName.getText());
      weekName.setText("");
    });
  }

  /**
   * Opens the given week in the display
   *
   * @param w the week to be displayed
   */
  public void openWeek(Week w) {
    this.week = w;
    clearWeekDisplayContents();
    primaryStage.setTitle(week.getName());
    newWeek.setText(week.getName());
    for (Task task : week.getAllTasks()) {
      updateAssignmentDisplay(task);
    }
    for (Event event : week.getAllEvents()) {
      updateAssignmentDisplay(event);
    }
    for (String note : week.getQuotesAndNotes()) {
      Label label = new Label(note);
      notesContent.getChildren().add(label);
    }
    updateStatistics();
    displayMaxEventsWarning();
    displayMaxTasksWarning();
  }

  /**
   * Clears the existing week contents
   */
  private void clearWeekDisplayContents() {
    mondayContent.getChildren().clear();
    tuesdayContent.getChildren().clear();
    wednesdayContent.getChildren().clear();
    thursdayContent.getChildren().clear();
    fridayContent.getChildren().clear();
    saturdayContent.getChildren().clear();
    sundayContent.getChildren().clear();
    notesContent.getChildren().clear();
  }

  /**
   * initializes the open file button
   */
  private void initOpenFile() {
    openFile.setOnAction(event -> {
      FileChooser fileChooser = new FileChooser();
      fileChooser.setTitle("Open File");

      FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("Bujo (*.bujo)",
          "*.bujo");
      fileChooser.getExtensionFilters().add(extensionFilter);

      File selectedFile = fileChooser.showOpenDialog(primaryStage);


      if (selectedFile != null) {
        String filePath = selectedFile.getAbsolutePath();
        FileReader bujoFileReader = new BujoFileReader(this);
        bujoFileReader.read(filePath);

        Stage stage = new Stage();
        PasswordPromptController controller = new PasswordPromptController(
            week, this, stage);
        PasswordPromptView view = new PasswordPromptView(controller);
        stage.setTitle("Enter Password");
        stage.setScene(view.load());
        controller.run();
        stage.show();
      }
    });
  }

  /**
   * Sets the week of the controller
   *
   * @param week the week to be set
   */
  public void setWeek(Week week) {
    this.week = week;
  }

  /**
   * initializes the show task queue button
   */
  private void initShowSideBar() {
    showTaskQueue.setOnAction(event -> {
      if (sideBar.getDividerPositions()[0] >= 0.1) {
        sideBar.setDividerPosition(0, 0.0);
        weeksTasks.setText("");
        tasksBox.getChildren().clear();
      } else if (sideBar.getDividerPositions()[0] <= 0.1) {
        sideBar.setDividerPosition(0, 0.2);
        tasksBox.getChildren().clear();
        weeksTasks.setText("This Week's Tasks");
        for (Task task : week.getAllTasks()) {
          if (task.isComplete()) {
            Label label = new Label(task.getName() + ": Complete");
            tasksBox.getChildren().add(label);
          } else {
            Label label = new Label(task.getName() + ": Not Complete");
            tasksBox.getChildren().add(label);
          }
        }
      }
    });
  }

  /**
   * initializes the save file button
   */
  private void initSaveFile() {
    saveFile.setOnAction(event -> {
      if (week.getPassword() != null) {
        saveFile();
      } else {
        showSetPasswordAlert();
      }
    });
  }

  /**
   * displays an alert if the week has no password and therefore can't be saved
   */
  private void showSetPasswordAlert() {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle("Missing Password");
    alert.setHeaderText(null);
    alert.setContentText("This week cannot be saved because you have not"
        +
        " set a password for the week. Please create a password and try again.");
    alert.showAndWait();
  }

  /**
   * saves the week to a .bujo file
   */
  private void saveFile() {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Save File");
    fileChooser.setInitialFileName(week.getName() + ".bujo");
    FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("Bujo (*.bujo)",
        "*.bujo");
    fileChooser.getExtensionFilters().add(extensionFilter);

    File file = fileChooser.showSaveDialog(null);

    if (file != null) {
      try {
        BujoFileWriter fileWriter = new BujoFileWriter();
        fileWriter.writeToFile(file.toPath(), week.toJsonFormat());
      } catch (Exception e) {
        System.out.println("An error occurred while saving the file.");
      }
    }
  }

  /**
   * initializes the create new note button
   */
  private void initNewNote() {
    newNote.setOnAction(event -> {
      try {
        Stage stage = new Stage();
        NewNoteQuoteController controller = new NewNoteQuoteController(stage, week, notesContent);
        NewNoteQuoteView view = new NewNoteQuoteView(controller);
        stage.setTitle("Add a New Note or Quote");
        stage.setScene(view.load());
        controller.run();
        stage.show();
      } catch (IllegalStateException exc) {
        System.err.println("Unable to load GUI." + exc.getMessage());
      }
    });
  }

  /**
   * initializes the create new category button
   */
  private void initCreateCategory() {
    createCategory.setOnAction(event -> {
      try {
        Stage stage = new Stage();
        CreateCategoryController controller = new CreateCategoryController(stage, week);
        CreateCategoryView view = new CreateCategoryView(controller);
        stage.setTitle("Create a New Category for the Week");
        stage.setScene(view.load());
        controller.run();
        stage.show();
      } catch (IllegalStateException exc) {
        System.err.println("Unable to load GUI." + exc.getMessage());
      }
    });
  }

  /**
   * displays a warning message if any day of the week has exceeded the maximum events
   */
  public void displayMaxEventsWarning() {
    if (week.maxEventsExceeded()) {
      eventWarning.setText("WARNING: You have reached your maximum "
          +
          "number of events for at least one day.");
    } else {
      eventWarning.setText(" ");
    }
  }

  /**
   * displays a warning message if any day of the week has exceeded the maximum tasks
   */
  public void displayMaxTasksWarning() {
    if (week.maxTasksExceeded()) {
      taskWarning.setText("WARNING: You have reached your maximum "
          +
          "number of tasks for at least one day.");
    } else {
      taskWarning.setText(" ");
    }
  }

  /**
   * initializes the set maximum events and tasks button
   */
  private void initSetMax() {
    setMaxEventsTasks.setOnAction(event -> {
      try {
        Stage newMaxStage = new Stage();
        SetMaxController controller = new SetMaxController(newMaxStage, week, this);
        SetMaximumView view = new SetMaximumView(controller);
        newMaxStage.setTitle("Set Maximum Tasks and Events");
        newMaxStage.setScene(view.load());
        controller.run();
        newMaxStage.show();
      } catch (IllegalStateException exc) {
        System.err.println("Unable to load GUI." + exc.getMessage());
      }
    });
  }

  /**
   * updates the week's statistics
   */
  public void updateStatistics() {
    totalEvents.setText("Total Events: " + week.numEvents());
    totalTasks.setText("Total Tasks: " + week.numTasks());
    tasksCompleted.setText("Percent Tasks Completed: " + week.getPercentTasksCompleted() + "%");
  }

  /**
   * initializes the create new event button
   */
  private void initNewEvent() {
    newEvent.setOnAction(event -> {
      try {
        Stage newEventStage = new Stage();
        NewEventController controller = new NewEventController(newEventStage, week, this);
        NewEventView view = new NewEventView(controller);
        newEventStage.setTitle("Create New Event");
        newEventStage.setScene(view.load());
        controller.run();
        newEventStage.show();
      } catch (IllegalStateException exc) {
        System.err.println("Unable to load GUI.");
      }
    });
  }

  /**
   * initializes the create new task button
   */
  private void initNewTask() {
    newTask.setOnAction(event -> {
      try {
        Stage newTaskStage = new Stage();
        NewTaskController controller = new NewTaskController(newTaskStage, week, this);
        NewTaskView view = new NewTaskView(controller);
        newTaskStage.setTitle("Create New Task");
        newTaskStage.setScene(view.load());
        controller.run();
        newTaskStage.show();
      } catch (IllegalStateException exc) {
        System.err.println("Unable to load GUI.");
        exc.printStackTrace();
      }
    });
  }

}
