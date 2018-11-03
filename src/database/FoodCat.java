package database;

import java.util.ArrayList;
import java.util.Collection;

public class FoodCat {
	public ArrayList <FoodItem> item;
	
	FoodCat(Collection <FoodItem>items) {
		item = new ArrayList<FoodItem>(items);
	}
}
