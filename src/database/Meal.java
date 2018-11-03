package database;

public class Meal {
	public FoodCat entree;
	public FoodCat soup;
	public FoodCat sides;
	
	Meal(FoodCat ent, FoodCat so, FoodCat sid){
		entree = ent;
		soup = so;
		sides = sid;
	}
}
