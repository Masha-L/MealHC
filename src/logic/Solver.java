package logic;

import database.*;
import personal.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

public class Solver {
	static Personal configs;
	static JParser parser = new JParser();

	public static void main(String [] args) throws Exception {

		solve();
	}

	public static void solve() throws Exception 
	{
		int [] restrictions = {0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
		LinkedList <String> str = new LinkedList <String>();
		str.add("Baraka - Halal");
		configs = new Personal(20, true, 0, restrictions, str);
		ArrayList<NutritionVals> limits = findLim();
		Day todayMeals = parser.readDay();

		// create an array of items: 0-2 breakfast, 3-5 for lunch, 6-8 for dinner
		ArrayList<FoodItem>[] itemsToChoose = new ArrayList[9];
		itemsToChoose[0] = todayMeals.breakfast.entree.item;
		itemsToChoose[1] = todayMeals.breakfast.sides.item;
		itemsToChoose[2] = todayMeals.breakfast.soup.item;
		itemsToChoose[3] = todayMeals.lunch.entree.item;
		itemsToChoose[4] = todayMeals.lunch.sides.item;
		itemsToChoose[5] = todayMeals.lunch.soup.item;
		itemsToChoose[6] = todayMeals.dinner.entree.item;
		itemsToChoose[7] = todayMeals.dinner.sides.item;
		itemsToChoose[8] = todayMeals.dinner.soup.item;		

		// Excludes invalid items from the list (allergens etc)
		for (ArrayList list : itemsToChoose) {
			excludeInvalid(list);
		}

		// Array of length 3 (b,l,d) of lists with working options
		ArrayList<MealOption>[] allOptions = new ArrayList[3];
		for (int i = 0; i < allOptions.length; i++)
			allOptions[i] = new ArrayList<MealOption>();

		// For every meal, puts all the variations into finalOptions[0], [1], or [2]
		for (int i = 0; i < 3; i++) {
			for (FoodItem item1 : itemsToChoose[i]) {
				for (FoodItem item2 : itemsToChoose[i+3]) {
					for (FoodItem item3 : itemsToChoose[i+6]) {
						allOptions[i].add(new MealOption(item1, item2, item3));
					}
				}
			}
		}

		ArrayList<Menu> menus = new ArrayList<Menu>();
		for (MealOption meal1 : allOptions[0]) {
			for (MealOption meal2 : allOptions[1]) {
				for (MealOption meal3 : allOptions[2]) {
					Menu menuOpt = new Menu(meal1, meal2, meal3);
					if (inBounds(menuOpt, limits)) {
						menus.add(menuOpt);
					}
				}
			}
		}

		// AT THIS POINT MENUS IS SUPPOSED TO HAVE ALL OPTIONS TO DISPLAY 

	}


	private static void excludeInvalid(ArrayList<FoodItem> options) {
		Iterator<FoodItem> iter = options.iterator();
		ArrayList<FoodItem> rem = new ArrayList<FoodItem> ();
		while (iter.hasNext()) {
			FoodItem current = iter.next();
			if (!isValid(current))
				rem.add(current);
		}
		options.removeAll(rem);

		//return options;
	}

	private static boolean isValid(FoodItem current) {
		if (!configs.stationPrefs.contains(current.stationName))
			return false;
		//(configs.restrictions, current.restrictions);
		return true;

	}

	private static boolean inBounds(Menu menu, ArrayList<NutritionVals> limits) {
		/*
		 * 		meals[0] = breakfast;
		meals[1] = lunch;
		meals[2] = dinner;
		this.entree = entree;
		this.side = side;
		this.soup = soup;
		 */

		NutritionVals lb = limits.get(0);
		NutritionVals hb = limits.get(1);

		NutritionVals currNutritionVal = helperBounds(menu);
		if(currNutritionVal.calories>=lb.calories&&currNutritionVal.calories<=hb.calories
				&&currNutritionVal.cholesterol>=lb.cholesterol&&currNutritionVal.cholesterol<=hb.cholesterol&&
				currNutritionVal.protein >= lb.protein&&currNutritionVal.protein<=hb.protein&&
				currNutritionVal.saturatedFat >= lb.saturatedFat && currNutritionVal.saturatedFat<=hb.saturatedFat&&
				currNutritionVal.sodium >= lb.sodium && currNutritionVal.sodium<= hb.sodium &&		
				currNutritionVal.totalCarbs >= lb.totalCarbs && currNutritionVal.totalCarbs <=hb.totalCarbs&&
				currNutritionVal.totalFat>= lb.totalFat && currNutritionVal.totalFat<= hb.totalFat&&
				currNutritionVal.transFat>=lb.transFat && currNutritionVal.transFat<= hb.transFat)
			return true;


		return false;

	}
	private static NutritionVals helperBounds(Menu menu) {
		NutritionVals currNutritionVal = new NutritionVals();
		for(int i = 0; i<3; i++) {
			assign(currNutritionVal, menu.meals[i].entree);
			assign(currNutritionVal, menu.meals[i].side);
			assign(currNutritionVal, menu.meals[i].soup);
		}
		return currNutritionVal;
	}

	private static void assign(NutritionVals currNutritionVal, FoodItem curr) {
		currNutritionVal.calories += curr.calories;
		currNutritionVal.cholesterol += curr.cholesterol;
		currNutritionVal.protein += curr.protein;
		currNutritionVal.saturatedFat += curr.saturatedFat;
		currNutritionVal.sodium += curr.sodium;
		currNutritionVal.totalCarbs += curr.totalCarbs;
		currNutritionVal.totalFat += curr.totalFat;
		currNutritionVal.transFat += curr.transFat;
	}

	private static ArrayList <NutritionVals> findLim(){

		int calories = findCalories();
		double[] totalFat = findFat(calories);
		double[] saturatedFat = findSatFat(calories);
		double[] totalCarbs = findCarbs(calories);
		double[] totalProtein = findProtein (calories);
		NutritionVals lowBounds = new NutritionVals(calories,totalFat[0], 0.0, 0.0,totalCarbs[0], 0,0, totalProtein[0],0);
		NutritionVals upperBounds = new NutritionVals(calories,totalFat[1],saturatedFat[1], calories*0.01,
				totalCarbs[1], 0, 300,totalProtein[1], 0 );

		int protein;
		int sodium;

		return null;

	}

	private static double[] findFat(int calories) {
		double lowb = (0.2 * calories) / 9;
		double upperb = (0.35 * calories) / 9;
		double[] array = { lowb, upperb };
		return array;

	}

	private static double[] findCarbs(int calories) {
		double lowb = (0.45 * calories) / 4;
		double upperb = (0.65 * calories) / 4;
		double[] array = { lowb, upperb };
		return array;

	}

	private static double[] findProtein(int calories) {
		double lowb = (0.1 * calories) / 4;
		double upperb = (0.65 * calories) / 4;
		double[] array = { lowb, upperb };
		return array;
	}

	private static double[] findSatFat(int calories) {
		double lowb = 0;
		double upperb = (0.1 * calories) / 9;
		double[] array = { lowb, upperb };
		return array;

	}

	private static int findCalories() {
		// 1 - fem
		if (configs.sex) {
			if (configs.age < 30) {
				if (configs.activityLevel == 1) {
					return 2000;
				} else if (configs.activityLevel == 2) {
					return 2100;
				} else
					return 2400;
			} else if (configs.age < 50) {
				if (configs.activityLevel == 1) {
					return 1800;
				} else if (configs.activityLevel == 2) {
					return 2000;
				} else
					return 2200;
			} else {
				if (configs.activityLevel == 1) {
					return 1600;
				} else if (configs.activityLevel == 2) {
					return 1800;
				} else
					return 2100;
			}
		} else {
			if (configs.age < 30) {
				if (configs.activityLevel == 1) {
					return 2400;
				} else if (configs.activityLevel == 2) {
					return 2700;
				} else
					return 3000;
			} else if (configs.age < 50) {
				if (configs.activityLevel == 1) {
					return 2200;
				} else if (configs.activityLevel == 2) {
					return 2500;
				} else
					return 2900;
			} else {
				if (configs.activityLevel == 1) {
					return 2000;
				} else if (configs.activityLevel == 2) {
					return 2300;
				} else
					return 2600;
			}
		}

	}

}
