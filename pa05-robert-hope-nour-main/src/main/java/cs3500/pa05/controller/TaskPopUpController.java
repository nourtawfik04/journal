package cs3500.pa05.controller;

import cs3500.pa05.model.assignments.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

/**
 * Controller for a task pop up mini viewer
 */
public class TaskPopUpController implements Controller {
  @FXML
  private Label name;

  @FXML
  private Label description;

  @FXML
  private Label day;

  @FXML
  private Button markAsComplete;

  @FXML
  private Label category;

  @FXML
  private Button buttonClicked;

  private Task task;

  private JavaJournalController controller;

  /**
   * Constructor for a task pop up controller
   *
   * @param t the task in the popup window
   * @param controller the main controller
   * @param button the button clicked to create the popup
   */
  public TaskPopUpController(Task t, JavaJournalController controller, Button button) {
    this.task = t;
    this.controller = controller;
    this.buttonClicked = button;
  }

  /**
   * runs the controller
   *
   * @throws IllegalStateException if unable to load the scene
   */
  @Override
  public void run() throws IllegalStateException {
    determineName();
    description.setText("Description: " + task.getDescription());
    day.setText("Day: " + task.getDay());
    category.setText("Category: " + task.getCategory());
    initMarkAsComplete();
  }

  /**
   * determines the name display of the task
   */
  private void determineName() {
    if (task.isComplete()) {
      name.setText("COMPLETED TASK: " + task.getName());
    } else {
      name.setText("INCOMPLETE TASK: " + task.getName());
    }
  }

  /**
   * initializes the button to mark a task as complete
   */
  private void initMarkAsComplete() {
    HBox parent = (HBox) markAsComplete.getParent();
    if (task.isComplete()) {
      parent.getChildren().remove(markAsComplete);
    }
    markAsComplete.setOnAction(event -> {
      task.markAsComplete();
      parent.getChildren().remove(markAsComplete);
      name.setText("COMPLETED TASK: " + task.getName());
      controller.updateStatistics();
      controller.updateButtonTitleAsComplete(buttonClicked, task);
    });
  }
}
