package database;

public class NutritionVals {
	public int calories; 
	public double totalFat;
	public double saturatedFat;
	public double transFat;
	public double totalCarbs;
	public int sugars;
	public int cholesterol;
	public double protein;
	public int sodium;
	
	public NutritionVals() {
		
	};
	public NutritionVals( int calories, double totalFat2, double d, double e,
			 double totalCarbs2, int sugars, int cholesterol,	 double totalProtein, int sodium){

			 this.calories = calories;
			 this.cholesterol = cholesterol;
			 this.protein = totalProtein;
			 this.saturatedFat = d;
			 this.sodium = sodium;
			 this.sugars = sugars;
			 this.totalCarbs = totalCarbs2;
			 this.totalFat = totalFat2;
			 this.transFat = e;
			 
	}
}
