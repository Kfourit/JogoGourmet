package main;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.json.*;



/**
 * Class for the Objective challenge. When instantiated, starts the game bringing
 * the first pop-up.
 * @author Tiago Kfouri
 * @version 1.1
 */
public class JogoGourmet{

	JFrame f;
	
	private JSONObject jsonObject;
	
	private List<String> foodList = new ArrayList<String>();
	private List<String> categoryList = new ArrayList<String>();
	
	
	JogoGourmet() throws JSONException{
	    f=new JFrame();
	    String jsonFileName = "data.json";
	    
	    startJSON(jsonFileName);
	    System.out.println(jsonObject);
	    startCategoryList();

	    
	    int response = JOptionPane.showConfirmDialog(f, "Pense em um prato que gosta", "Jogo Gourmet", 
	    		-1, JOptionPane.PLAIN_MESSAGE);

	    
	    // If pressed OK
	    if(response == JOptionPane.OK_OPTION) {
	    	
	    	// Loop of all categories
	    	for(int j = 0; j<categoryList.size() + 1; j++) {
	    		
	    		String category = "";
	    		
	    		if (j<categoryList.size()) {
	    			category = categoryList.get(j);
	    			response = JOptionPane.showConfirmDialog(f, "O prato que você pensou é " + category, "Confirm",
	    					JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null);
	    		}
	    		else {
	    			category = "default";
	    			response = JOptionPane.YES_OPTION;
	    		}
	    		
		    	
	    		// If pressed YES for the category
		    	if (response == JOptionPane.YES_OPTION) {
		    				    		
		    		startFoodListFromCategory(category);
		    		
		    		// Loop of the food on the category
		    		int i;
		    		String food = "";
			    	for (i = 0; i < foodList.size(); i++) {
			    		
			    		food = foodList.get(i);
				    	response = JOptionPane.showConfirmDialog(f, "O prato que você pensou é " + food + "?", 
				    			"Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null);
				    		
				    	// If it is the food
				    	if (response == JOptionPane.YES_OPTION) {
				    		JOptionPane.showConfirmDialog(f, "Acertei de novo!", "Jogo Gourmet", 
				    		    		-1, JOptionPane.INFORMATION_MESSAGE);
				    		break;
				    	}
				    		
			    	}
			    	// If it is not the food
			    	if (i >= foodList.size()) {
			    		String foodInput = JOptionPane.showInputDialog(f, "Qual prato você pensou?", "Desisto",
			    				JOptionPane.QUESTION_MESSAGE);
			    		
			    		String categoryInput = JOptionPane.showInputDialog(f, foodInput + " é ______ mas " + food + " não.",
			    				"Complete",	JOptionPane.QUESTION_MESSAGE);
			    		
			    		addCategoryOnJSON(categoryInput);
			    		addFoodToCategoryOnJSON(foodInput, categoryInput);
			    		writeToFile(jsonFileName);
			    		
			    	}
			    	break;
		    	}
		    	
		    	// If pressed NO on the current category, continues loop to the next category
		    	
	    	}
	    	
	    	
	    }
	    
	} 
	
	/**
	 * Writes the jsonObject attribute of this class into the file given. If file doesn't exist,
	 * creates it and then write on it.
	 * @param fileName
	 */
	private void writeToFile(String fileName) {
		
		try {
		      File myObj = new File(fileName);
		      if (myObj.createNewFile()) {
		    	  System.out.println("File created: " + myObj.getName());
		      } 
		      else {
		    	  System.out.println("File already exists.");
		      }
		      
		      FileWriter myWriter = new FileWriter(fileName);
		      myWriter.write(jsonObject.toString());
		      myWriter.close();
		      
	    } catch (IOException e) {
	      System.out.println("An error occurred.");
	      e.printStackTrace();
	    }
	}
	
	
	/**
	 * Receives a category and reads all foods from that category in the
	 * jsonObject attribute. Saves that list of foods on foodList attribute.
	 * Must have jsonObject initialized first with the method startJson().
	 * @param category
	 * @throws JSONException
	 */
	private void startFoodListFromCategory(String category) throws JSONException {
		
		JSONArray foodListJSON = jsonObject.getJSONArray(category);
		
		for (int i = 0; i < foodListJSON.length(); i++) {
            this.foodList.add(foodListJSON.get(i).toString());
        }
	}
	
	/**
	 * Reads all categories of the jsonObject attribute. Saves that list in
	 * the categoryList attribute. 
	 * Must have jsonObject initialized first with the method startJson().
	 * @throws JSONException
	 */
	private void startCategoryList() throws JSONException {
		
		JSONArray categoryListJSON = jsonObject.getJSONArray("categories");
		
		for (int i = 0; i < categoryListJSON.length(); i++) {
            this.categoryList.add(categoryListJSON.get(i).toString());
        }

	}
	

	/**
	 * Reads the file with name fileName and parses the content into JSON.
	 * The JSON is saved in the jsonObject attribute.
	 * @param fileName
	 */
	private void startJSON (String fileName) {
		File file = new File(fileName);
	    Scanner sc;
	    String fileContent = "";
	    
	    checkIfFileExists(fileName);
	    
	    // Reading file
		try {
			sc = new Scanner(file);
			
			// we just need to use \\Z as delimiter to read entire file
			sc.useDelimiter("\\Z");
			
			fileContent = sc.next();
			System.out.println(fileContent);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// Parsing content into JSON
		JSONTokener tokener;
		try {
			tokener = new JSONTokener(fileContent);
			this.jsonObject = new JSONObject(tokener);
		} catch (JSONException e) {
			e.printStackTrace();
		}		
	}
	
	/**
	 * Adds a new category to the jsonObject attribute. Doesn't change categoryList attribute
	 * @param category
	 * @throws JSONException
	 */
	private void addCategoryOnJSON (String category) throws JSONException {
		
		if (category != null) {
			jsonObject.append("categories", category);
			jsonObject.put(category, new ArrayList<String>());
		}
		
	}
	
	/**
	 * Adds a new food in a specific category to the jsonObject attribute. Doesn't change
	 * foodList attribute
	 * @param food
	 * @param category
	 * @throws JSONException
	 */
	private void addFoodToCategoryOnJSON (String food, String category) throws JSONException {
		
		if (category != null && food != null) {
			jsonObject.append(category, food);
		}
	}
	
	/**
	 * Checks if the file with JSON already exists. If not, creates a default one.
	 * @param fileName
	 */
	private void checkIfFileExists (String fileName) {
		File file = new File(fileName);
		boolean exists = file.isFile();

		if (!exists) {
			try {
				file.createNewFile();
				FileWriter myWriter = new FileWriter(fileName);
			    myWriter.write("{\"massa\":[\"Lasanha\"],\"default\":[\"Bolo de Chocolate\"],\"categories\":[\"massa\"]}");
			    myWriter.close();
			    System.out.println("New default file was created");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
}
