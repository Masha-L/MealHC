package database;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Iterator; 
import java.util.Map; 

import org.json.simple.JSONArray; 
import org.json.simple.JSONObject; 
import org.json.simple.parser.*; 

public class JParser {
	// Java program to read JSON from a file 
		
		public Day readDay() throws Exception 
		{ 
			
			// parsing file "JSONExample.json" 
			Object obj = new JSONParser().parse(new FileReader("dataTest.json")); 
			
			// typecasting obj to JSONObject 
			JSONObject jo = (JSONObject) obj; 
			
	        JSONObject breakfast = (JSONObject) jo.get("breakfast");
	        	JSONObject lunch = (JSONObject) jo.get("lunch"); 
	        JSONObject dinner = (JSONObject) jo.get("dinner");
	        
	        Meal br = readMeal(breakfast);
	        Meal lu = readMeal(lunch);
	        Meal di = readMeal(dinner);
	        
	        
	        return new Day(br, lu, di);
		} 

		private BufferedReader readURL() throws IOException {
			  URL url = new URL("http://www.oracle.com/");
		        URLConnection yc = url.openConnection();
		        BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
				return in;
		}
		
		private Meal readMeal(JSONObject meal) {
			JSONArray entree = (JSONArray) meal.get("entrees");
			JSONArray soup = (JSONArray) meal.get("soups");
			//JSONArray accomp = (JSONArray) meal.get("accomp");
			JSONArray sides = (JSONArray) meal.get("sides");
			
			return new Meal(nutrVals(entree), nutrVals(soup), nutrVals(sides));
		}
		
		private FoodCat nutrVals(JSONArray foodCat) {
			
			if(!foodCat.isEmpty()) {
			ArrayList <FoodItem> items = new ArrayList <FoodItem>();
			for(int i = 0; i <foodCat.size(); i++) {
				JSONObject item = (JSONObject) foodCat.get(i);
				items.add(parse(item));
			}
			return new FoodCat(items);
			}
			return new FoodCat(new ArrayList <FoodItem>());
			
		}

		private FoodItem parse(JSONObject item) {
			
			String name = (String) item.get("name");
			String stationName = (String) item.get("stationName");
			String serving = (String) item.get("serving"); 
			Long calories = (Long) item.get("calories"); 
			Double totalFat = (Double) item.get("totalFat");
			Double saturatedFat = (Double) item.get("saturatedFat");
			Double transFat = (Double) item.get("transFat");
			Double totalCarbs = (Double) item.get("totalCarbs");
			Double sugars = (Double) item.get("sugars");
			Double cholesterol = (Double) item.get("cholesterol");
			Double protein = (Double) item.get("protein");
			Double sodium = (Double) item.get("sodium");
			
			JSONArray restriction = (JSONArray) item.get("restrictions");
			Long [] restrictions = new Long[restriction.size()];
			for(int i = 0; i <restriction.size(); i++) {
				restrictions[i] = (Long) restriction.get(i);
			}
			return new FoodItem(name, stationName, serving,
			 calories, totalFat, saturatedFat, transFat,
			 totalCarbs, sugars, cholesterol, protein, sodium,
			 restrictions);
		}
}
