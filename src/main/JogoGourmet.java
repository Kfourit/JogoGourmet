package main;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.json.*;




public class JogoGourmet{

	JFrame f;
	
	List<String> foodList = new ArrayList<String>();
	List<String> categoryList = new ArrayList<String>();
	
	private JSONObject jsonObject;
	
	JogoGourmet() throws JSONException{
	    f=new JFrame();
	    String jsonFileName = "data.json";
	    
	    startJSON(jsonFileName);
	    //System.out.println(jsonObject);
	    startCategoryList();

	    
	    int response = JOptionPane.showConfirmDialog(f, "Pense em um prato que gosta", "Jogo Gourmet", 
	    		-1, JOptionPane.PLAIN_MESSAGE);

	    
	    // Se apertou OK
	    if(response == 0) {
	    	
	    	// Loop de todas as categorias
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
	    		
		    	
		    	// Se apertou YES no prato ser a categoria
		    	if (response == JOptionPane.YES_OPTION) {
		    		
		    		startFoodListFromCategory(category);
		    		
		    		// Loop das comidas da categoria
		    		int i;
		    		String food = "";
			    	for (i = 0; i < foodList.size(); i++) {
			    		
			    		food = foodList.get(i);
				    	response = JOptionPane.showConfirmDialog(f, "O prato que você pensou é " + food + "?", 
				    			"Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null);
				    		
				    	// Se for a comida
				    	if (response == JOptionPane.YES_OPTION) {
				    		JOptionPane.showConfirmDialog(f, "Acertei de novo!", "Jogo Gourmet", 
				    		    		-1, JOptionPane.INFORMATION_MESSAGE);
				    		break;
				    	}
				    		
			    	}
			    	// Se nao for nenhuma das comidas
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
		    	
		    	// Se apertou NO no prato ser a categoria, continua o loop para a proxima categoria
		    	
	    	}
	    	
	    	
	    }
	    
	} 
	
	
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
	
	private void startFoodListFromCategory(String category) throws JSONException {
		
		JSONArray foodListJSON = jsonObject.getJSONArray(category);
		
		for (int i = 0; i < foodListJSON.length(); i++) {
            this.foodList.add(foodListJSON.get(i).toString());
        }
	}
	
	private void startCategoryList() throws JSONException {
		
		JSONArray categoryListJSON = jsonObject.getJSONArray("categories");
		
		for (int i = 0; i < categoryListJSON.length(); i++) {
            this.categoryList.add(categoryListJSON.get(i).toString());
        }

	}
	
	private void startJSON(String fileName) {
		InputStream inputStream = JogoGourmet.class.getResourceAsStream(fileName);
		if (inputStream == null) {
            throw new NullPointerException("Cannot find resource file " + fileName);
        }
		JSONTokener tokener;
		try {
			tokener = new JSONTokener(inputStream);
			this.jsonObject = new JSONObject(tokener);
		} catch (JSONException e) {
			e.printStackTrace();
		}		
	}
	
	private void addCategoryOnJSON (String category) throws JSONException {
		
		if (category != null) {
			jsonObject.append("categories", category);
			jsonObject.put(category, new ArrayList<String>());
		}
		
	}
	
	private void addFoodToCategoryOnJSON (String food, String category) throws JSONException {
		
		if (category != null && food != null) {
			jsonObject.append(category, food);
		}
	}
	
}
