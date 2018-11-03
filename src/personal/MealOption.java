package personal;

import database.*;

public class MealOption {
	public FoodItem entree;
	public FoodItem side;
	public FoodItem soup;
	public int calories;
	
	public MealOption(FoodItem entree, FoodItem side, FoodItem soup) {
		this.entree = entree;
		this.side = side;
		this.soup = soup;
	}
	
}
