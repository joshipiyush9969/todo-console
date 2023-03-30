import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

class Task {
  private String taskName;
  private String taskDate;
  private Boolean isCompleted;

  Task(String taskName, String Date, boolean isCompleted) {
    String date;
    Boolean is_completed;

    if (Date.equals(" ")) {

      LocalDate today = LocalDate.now();
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
      date = today.format(formatter);
      is_completed = false;
    } else {
      date = Date;
      is_completed = isCompleted;

    }
    this.taskName = taskName;
    this.taskDate = date;
    this.isCompleted = is_completed;
  }

  public String getTask() {
    return taskName;
  }
  public String getDate() {
    return taskDate;
  }

  public Boolean getStatus() {
    return isCompleted;
  }

  public void setStatus(Boolean status) {
    this.isCompleted = !status;
  }
}

public class App {
  public static final String ANSI_RESET = "\u001B[0m";
  public static final String ANSI_RED = "\u001B[31m";
  public static final String ANSI_GREEN = "\u001B[32m";
  public static final String ANSI_YELLOW = "\u001B[33m";
  private String fileName = "database.txt";

  private ArrayList < Task > tasks;

  App() {
    tasks = new ArrayList < Task > ();
    try {

      FileReader reader = new FileReader(fileName);
      BufferedReader bufferedReader = new BufferedReader(reader);

      String line;
      String[] data;
      while ((line = bufferedReader.readLine()) != null) {
        data = line.split(",");
        for (int i = 0; i < data.length; i++) {
          String[] temp = data[i].split(":");
          boolean boolVal = Boolean.parseBoolean(temp[2]);
          Task newTask = new Task(temp[0], temp[1], boolVal);
          tasks.add(newTask);

        }

      }

      bufferedReader.close();
    } catch (IOException e) {
      if (e instanceof java.io.FileNotFoundException) {
        System.out.println("The file \"" + fileName + "\" does not exist.");
      } else {
        System.out.println("An error occurred.");
        e.printStackTrace();
      }
    }

  }

  public void save() {
    try {
      FileWriter writer = new FileWriter(fileName, false);

      for (Task task: tasks) {
        writer.write(task.getTask() + ":" + task.getDate() + ":" + task.getStatus() + ",");
      }

      writer.close();
      System.out.println(ANSI_GREEN+"Successfully saved todos in " + fileName+ANSI_RESET);
    } catch (IOException e) {
      System.out.println("An error occurred.");
      e.printStackTrace();
    }
  }

  public void addTask(String taskName) {
    Task newTask = new Task(taskName.trim(), " ", false);
    tasks.add(newTask);
  }

  public void removeTask(int index) {
    tasks.remove(index);
  }

  public void printTasks() {

    if (tasks.size() == 0) {
      System.out.println("\tNo Todos");
    } else {
      System.out.println("\n");
      for (int i = 0; i < tasks.size(); i++) {
        Task task = tasks.get(i);
        System.out.println((i + 1) + ". " + task.getTask() + "\nOn: " + task.getDate() + "\nCompleted : " + task.getStatus() + "\n");
      }
    }
  }

  public void changeStatus(int id) {
    Task task = tasks.get(id);
    Boolean currentStatus = task.getStatus();
    task.setStatus(currentStatus);
  }

  public static void main(String[] args) throws Exception {

    try {

      List < String > commands = new ArrayList < > ();
      commands.add("help");
      commands.add("change-status");
      commands.add("status");
      commands.add("remove");

      Boolean is_open = true;
      int taskIndex;
      App todoList = new App();
      Scanner sc = new Scanner(System.in);
      System.out.println("Start Adding...");
      System.out.print("\n" + ANSI_GREEN + "For Help: todo help\n" + ANSI_RESET + "\n");
      while (is_open) {

        System.out.print("->");
        String myTask = sc.nextLine();
        String[] command = myTask.trim().split(" ");

        if (myTask.equals("todo change-status")) {

          System.out.println("Tasks: ");
          todoList.printTasks();
          try {

            System.out.print("\nEnter Task Number from above: ");
            taskIndex = sc.nextInt();
            sc.nextLine();
            todoList.changeStatus(taskIndex - 1);
          } catch (InputMismatchException e) {
            System.out.println(ANSI_RED + "\tPlease enter a valid index(integer)." + ANSI_RESET);
            sc.nextLine(); // clear the invalid input from scanner buffer
          } catch (IndexOutOfBoundsException e) {
            System.out.println(ANSI_RED + "\tTask index is out of range." + ANSI_RESET);
          }

        } else if (myTask.equals("todo quit")) {
          todoList.save();
          is_open = false;
        } else if (myTask.equals("todo help")) {
          System.out.println("\n\ttodo change-status ------- Changes the status from true to false and vice versa.\n\ttodo quit\t   ------- Quits the application\n\ttodo help\t   ------- Shows all todo commands\n\ttodo status  \t   ------- Shows Todos\n\ttodo remove  \t   ------- Removes the specified todo");
        } else if (myTask.equals("todo status")) {
          todoList.printTasks();
        } else if (myTask.equals("todo remove")) {
          try {
            todoList.printTasks();
            System.out.print("\nEnter Task Number from above: ");
            taskIndex = sc.nextInt();
            sc.nextLine();
            todoList.removeTask(taskIndex - 1);
          } catch (InputMismatchException e) {
            System.out.println(ANSI_RED + "\tPlease enter a valid index(integer)." + ANSI_RESET);
            sc.nextLine(); // clear the invalid input from scanner buffer
          } catch (IndexOutOfBoundsException e) {
            System.out.println(ANSI_RED + "\tTask index is out of range." + ANSI_RESET);
          }

        } else if (command[0].equals("todo") && !commands.contains(command[1])) {
          System.out.println(ANSI_RED + "\ttodo " + "'" + command[1] + "'" + " is not a todo command." + ANSI_RESET + " See 'todo help'.");
        } else {

          todoList.addTask(myTask);
        }

      }

      sc.close();

    } catch (Exception e) {
      System.out.println(ANSI_RED + "Something Went Wrong, hope you didn't mess up something ;)" + ANSI_RESET);
    //   System.out.println(e);
    }

  }
}