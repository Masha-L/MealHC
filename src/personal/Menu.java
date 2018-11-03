package personal;

public class Menu {
	public MealOption[] meals;
	
	public Menu(MealOption breakfast, MealOption lunch, MealOption dinner){
		meals = new MealOption[3];
		meals[0] = breakfast;
		meals[1] = lunch;
		meals[2] = dinner;
	}
}
