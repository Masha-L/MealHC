package database;

public class FoodItem {
	public String name;
	public String stationName;
	public String serving; 
	public Long calories; 
	public Double totalFat;
	public Double saturatedFat;
	public Double transFat;
	public Double totalCarbs;
	public Double sugars;
	public Double cholesterol;
	public Double protein;
	public Double sodium;
	public Long [] restrictions;



	public FoodItem(String name, String stationName, String serving, Long calories, Double totalFat,
			Double saturatedFat, Double transFat, Double totalCarbs, Double sugars, Double cholesterol, Double protein,
			Double sodium, Long[] restrictions) {
			this.name = name;
			this.stationName = stationName;
			this.serving = serving;
			this.calories = calories;
			this.cholesterol = cholesterol;
			this.protein = protein;
			this.restrictions = restrictions;
			this.saturatedFat = saturatedFat;
			this.sodium = sodium;
			this.sugars = sugars;
			this.totalCarbs = totalCarbs;
			this.totalFat = totalFat;
			this.transFat = transFat;
		 }
}
