package main.java;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JsonFileUtil {
	
	/**
	 * This function used for read json file and convert json to list
	 * @param pathFile
	 * @param filename
	 * @return List<User>
	 */
	public static List<User> funReadJsonFile (String pathFile ,String filename) {
		List<User> users = new ArrayList<>();
		
		/// Combine File Path and File name
		String fullFilePath = pathFile + filename;
		
		/// Validate Json File
		File file = new File(fullFilePath);
		
		try {
			if (file.createNewFile()) {
				System.out.println("Json File have created: " + file.getName());
				/// Append default data to json
				try (FileWriter writer = new FileWriter(file)) {
		            writer.write("[]");
		            System.out.println("Data written to file.");
		        } catch (IOException e) {
		            System.out.println("An error occurred.");
		            e.printStackTrace();
		        }
			} 
		} catch (IOException e) {
			System.out.println("Something went wrong on Json File");
			e.printStackTrace();
		}
		
		/// Read Json from file
		StringBuilder jsonContent = new StringBuilder();
		try (BufferedReader br = new BufferedReader(new FileReader(fullFilePath))) {
			String line;
			
			while((line = br.readLine()) != null) {
				jsonContent.append(line);
			}
			
			users = JsonFileUtil.funParseJsonToList(jsonContent.toString());
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return users;
		
	}
	
	/**
	 * This function used for convert list to json file 
	 * and append to file.
	 * @param users
	 * @param pathFile
	 * @param filename
	 */
	public static void funWriteJsonFile (List<User> users, String pathFile, String filename) {
		StringBuilder json = new StringBuilder("[");
		
		int userLength = users.size();
		for (User user : users) {
			
			json.append("{\"id\": " + user.getId() + ",\"desc\": \"" + user.getDesc().trim() + "\",\"status\": \"" + user.getStatus().trim() + "\",\"createdAt\": \"" + user.getcreatedAt().trim() + "\",\"updatedAt\": \"" + user.getupdatedAt().trim() + "\"}");
			
			if (!user.equals(users.get(users.size() - 1))) {
                json.append(",");
            } 
			
		}
		
		
		json.append("]");
	
		try {
            Files.write(Paths.get(pathFile + filename), json.toString().getBytes(), StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
		
	}
	
	/**
	 * This function used for parse json file to list.
	 * @param json
	 * @return List<User>
	 */
	public static List<User> funParseJsonToList (String json) {
		
		List<User> users = new ArrayList<>();
		
		json = json.substring(1, json.length() - 1);
		
		String[] userJsonObjects = json.split("\\},\\{");
		
		for (String userJson : userJsonObjects) {
			userJson = userJson.replaceAll("[\\{\\}]", "");
			
			String[] attrs = userJson.split(",");
			
			int id = -1;
			String desc = "";
			String status = "";
			String createdAt = "";
			String updatedAt = "";
			
			int attrLoopIndex = 1;
			
			for (String attr : attrs) {
				
				String[] keyValue = attr.split(":");
				String key = keyValue[0].replaceAll("\"", "").trim();
				String value = keyValue[1].replaceAll("\"", "").trim();
				
				if (key.equals("id")) {
					
					if (attrLoopIndex != 1) {
						users.add(new User(id, desc, status, createdAt, updatedAt));
					}
					
					id = Integer.parseInt(value);
				} else if (key.equals("desc")) {
					desc = value;
				} else if (key.equals("status")) {
					status = value;
				} else if (key.equals("createdAt")) {
					createdAt = value;
				} else if (key.equals("updatedAt")) {
					updatedAt = value;
				}
				
				attrLoopIndex++;
			}
			
			users.add(new User(id, desc, status, createdAt, updatedAt));			
		}
		
		return users;
	}
}
