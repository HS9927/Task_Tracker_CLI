package main.java;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
	/// Global Variables
	private static String pathFile = "src/main/resources/";
	private static String filename = "data.json";
	
	private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyy");
	
	public static void main(String[] args) throws FileNotFoundException, IOException {
		
		Scanner scanner = new Scanner(System.in);
        
        // Loop until user trying "exit"
        while (true) {
            System.out.print("Task-cli: ");
            String userInput = scanner.nextLine();            
            if (userInput.equals("exit")) {
                break;
            } else {
            	String[] userFunctionArr = userInput.split(" ");
            	String userFunction = userFunctionArr[0].trim();
//            	String typeList = userFunctionArr[1].trim();
            	
            	if (userFunction.equals("add")) {
            		/// Add New Task
            		funAddTask(userInput);
            		System.out.println("Task saved successfully");
            	} else if (userFunction.equals("update")) {
            		funUpdateTask(userInput);
            		System.out.println("Task updated successfully");
            	} else if (userFunction.equals("delete")) {
            		funDeleteTask(userInput);
            	} else if (userFunction.equals("mark-in-progress")) {
            		funMarkInProgress(userInput);
            	} else if (userFunction.equals("mark-done")) {
            		funMarkDone(userInput);
            	} else if (userFunction.equals("list")) {
            		
            		
            		if (userFunctionArr.length == 1) {
            			funListTask(userInput);
            		} else {
                		String typeList = userFunctionArr[1].trim();                		
                		switch (typeList) {
    	            		case "done" :
    	            			funListDoneTask(userInput);
    	            			break;
    	            		case "todo" :
    	            			funListTodoTask(userInput);
    	            			break;
    	            		case "in-progress" :
    	            			funListInProgressTask(userInput);
    	            			break;
    	            		default :
    	            			break;
                		}
            		}
            	} else {
            		System.out.println("Try again !");
            	}
            }
        }

        System.out.println("You have exit from application !");
        scanner.close();
		
	}
	

	
	private static void funAddTask (String userInput) {
		/// Pattern Regex
		String regex = "^add\\s\"([^\"]+)\"$";
		
		Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(userInput);
        
        if (matcher.matches()) {
        	
        	/// Get Task name
        	String[] arrUserInput = userInput.split(" ", 2);
        	String taskName = arrUserInput[1].replaceAll("^\"|\"$", "");
        	
        	/// Get date now
        	LocalDate now = LocalDate.now();
        	
        	/// Get all users from Json File
        	List<User> users = JsonFileUtil.funReadJsonFile(pathFile, filename);
        	
        	/// Create object for new user
        	User user = new User();
        	user.setId(users.size() +1);
        	user.setDesc(taskName);
        	user.setStatus("todo");
        	user.setcreatedAt(now.format(formatter).toString());
        	user.setupdatedAt("");
        	
        	/// Add new user to list users
        	users.add(user);
        	
        	/// Write all users to json file
        	JsonFileUtil.funWriteJsonFile(users, pathFile, filename);
        	
        } else {
        	System.out.println("Syntax is not valid");
        }		
	}
	
	private static void funUpdateTask (String userInput) {
		
		String regex = "^update\\s(\\d+)\\s\"([^\"]+)\"$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(userInput);
        
        if (matcher.matches()) {
        	LocalDate now = LocalDate.now();
        	String[] arrUserInput = userInput.split(" ", 3);
        	String taskName = arrUserInput[2].replaceAll("^\"|\"$", "").trim();
        	String taskId = arrUserInput[1];
        	
        	/// Get all task from json file
        	List<User> users = JsonFileUtil.funReadJsonFile(pathFile, filename);
        	
        	/// find and update task by task id
        	for (User user : users) {
        		if (taskId.equals(String.valueOf(user.getId()))) {
        			user.setDesc(taskName);
        			user.setupdatedAt(now.format(formatter).toString());
        		}
        	}
        	
        	/// Write data to json file
        	JsonFileUtil.funWriteJsonFile(users, pathFile, filename);
        	
        } else {
        	System.out.println("User Input is invalid, try again !");
        }
	}
	
	private static void funDeleteTask (String userInput) {
		String regex = "^delete\\s(\\d+)$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(userInput);

        if (matcher.matches()) {
        	String[] arrUserInput = userInput.split(" ");
        	String taskId = arrUserInput[1].trim();
        	
        	List<User> users = JsonFileUtil.funReadJsonFile(pathFile, filename);
        	
        	/// Check Task ID existing in list
        	boolean taskExists = users.stream().anyMatch(user -> String.valueOf(user.getId()).equals(taskId));
        	
        	if (taskExists) {
        		/// Task exists
        		users.removeIf(user -> taskId.equals(String.valueOf(user.getId())));
            	
            	JsonFileUtil.funWriteJsonFile(users, pathFile, filename);
            	
            	System.out.println("Task deleted successfully");
        	} else {
        		/// Task not exists
        		System.out.println("Task Id " + taskId + " not exists");
        	}	
        } else {
        	System.out.println("User Input is invalid, try again !");
        }
	}
	
	
	
	private static void funMarkInProgress (String userInput) {
        String regex = "^mark-in-progress\\s(\\d+)$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(userInput);
        
        if (matcher.matches()) {
        	LocalDate now = LocalDate.now();
        	String[] arrUserInput = userInput.split(" ", 2);
        	int taskId = Integer.valueOf(arrUserInput[1]);

        	List<User> users = JsonFileUtil.funReadJsonFile(pathFile, filename);
        	
        	/// Check Task ID existing in list
        	boolean taskExists = users.stream().anyMatch(user -> user.getId() == taskId);

        	if (taskExists) {
            	for (User user : users) {
            		if (taskId == user.getId()) {
            			user.setStatus("in-progress");
            			user.setupdatedAt(now.format(formatter).toString());
            		}
             	}
            	
            	JsonFileUtil.funWriteJsonFile(users, pathFile, filename);
            	
            	System.out.println("Task Id "+ taskId +" changed to In-Progress successfully");
        	} else {
        		/// Task not exists
        		System.out.println("Task Id " + taskId + " not exists");
        	}
        	
        } else {
        	System.out.println("User Input is invalid, try again !");
        }
	}
	
	private static void funMarkDone (String userInput) {
        String regex = "^mark-done\\s(\\d+)$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(userInput);
        
        if (matcher.matches()) {
        	LocalDate now = LocalDate.now();
        	String[] arrUserInput = userInput.split(" ", 2);
        	int taskId = Integer.valueOf(arrUserInput[1]);

        	List<User> users = JsonFileUtil.funReadJsonFile(pathFile, filename);
        	
        	/// Check Task ID existing in list
        	boolean taskExists = users.stream().anyMatch(user -> user.getId() == taskId);

        	if (taskExists) {
            	for (User user : users) {
            		if (taskId == user.getId()) {
            			user.setStatus("done");
            			user.setupdatedAt(now.format(formatter).toString());
            		}
             	}
            	
            	JsonFileUtil.funWriteJsonFile(users, pathFile, filename);
            	
            	System.out.println("Task Id "+ taskId +" changed to Done successfully");

        	} else {
        		/// Task not exists
        		System.out.println("Task Id " + taskId + " not exists");
        	}
        	

        	
        } else {
        	System.out.println("User Input is invalid, try again !");
        }
	}
	
	private static void funListTask (String userInput) {
        String regex = "^list$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(userInput.trim());
        
        if (matcher.matches()) {
        	List<User> users = JsonFileUtil.funReadJsonFile(pathFile, filename);
        	
        	users.forEach(user -> System.out.println("- " + user.getDesc()));
        } else {
        	System.out.println("User Input is invalid, try again !");
        }
	}
	
	private static void funListDoneTask (String userInput) {
        String regex = "^list\\sdone$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(userInput);
        
        if (matcher.matches()) {
        	List<User> users = JsonFileUtil.funReadJsonFile(pathFile, filename);
        	
        	users.forEach(user -> {
        		if (user.getStatus().equals("done")) {
        			System.out.println("- " + user.getDesc());
        		}
        	});
        } else {
        	System.out.println("User Input is invalid, try again !");
        }
	}
	
	private static void funListTodoTask (String userInput) {
        String regex = "^list\\stodo$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(userInput);
        
        if (matcher.matches()) {
        	List<User> users = JsonFileUtil.funReadJsonFile(pathFile, filename);
        	
        	users.forEach(user -> {
        		if (user.getStatus().equals("todo")) {
        			System.out.println("- " + user.getDesc());
        		}
        	});
        } else {
        	System.out.println("User Input is invalid, try again !");
        }
	}
	
	
	private static void funListInProgressTask (String userInput) {
        String regex = "^list\\sin-progress$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(userInput);
        
        if (matcher.matches()) {
        	List<User> users = JsonFileUtil.funReadJsonFile(pathFile, filename);
        	
        	users.forEach(user -> {
        		if (user.getStatus().equals("in-progress")) {
        			System.out.println("- " + user.getDesc());
        		}
        	});
        } else {
        	System.out.println("User Input is invalid, try again !");
        }
	}
	
}
